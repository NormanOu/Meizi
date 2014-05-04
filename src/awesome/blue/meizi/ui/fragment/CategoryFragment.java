
package awesome.blue.meizi.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import awesome.blue.meizi.R;
import awesome.blue.meizi.control.CategoryControl;
import awesome.blue.meizi.model.Category.CategoryItem;
import awesome.blue.meizi.model.MeiziM;
import awesome.blue.meizi.ui.adapter.CardsAnimationAdapter;
import awesome.blue.meizi.ui.adapter.CategoryAdapter;
import awesome.blue.meizi.util.BLog;
import awesome.blue.meizi.util.ListViewUtils;
import awesome.blue.meizi.util.NavigationUtil;
import awesome.blue.meizi.view.LoadingFooter;
import awesome.blue.meizi.view.LoadingFooter.ReloadListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;

public class CategoryFragment extends BaseFragment {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    private ListView mListView;

    private View mLoadingView;

    private View mReloadView;

    private Button mReloadButton;

    private CategoryAdapter mAdapter;

    private LoadingFooter mLoadingFooter;

    private CategoryItem mCategoryItem;

    public CategoryFragment(CategoryItem categoryItem) {
        mCategoryItem = categoryItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_category, null);
        mListView = (ListView) contentView.findViewById(R.id.listView);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter = new CategoryAdapter(getActivity(), mListView);
        mLoadingFooter = new LoadingFooter(getActivity(), new ReloadListener() {

            @Override
            public void onReload() {
                loadPage(mPage);
            }
        });

        mListView.addFooterView(mLoadingFooter.getView());

        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        animationAdapter.setListView(mListView);
        mListView.setAdapter(animationAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if (mLoadingFooter.getState() != LoadingFooter.State.Idle) {
                    return;
                }

                if (firstVisibleItem + visibleItemCount >= totalItemCount - 3
                        && totalItemCount != 0
                        && totalItemCount != mListView.getHeaderViewsCount()
                                + mListView.getFooterViewsCount() && mAdapter.getCount() > 0) {
                    loadNextPage();
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open TopicActivity
                MeiziM meiziM = (MeiziM) mListView.getAdapter().getItem(position);
                if (meiziM != null) {
                    String topicUrl = meiziM.topicUrl;
                    NavigationUtil.startTopicActivity(getActivity(), topicUrl);
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                return true;
            }
        });

        mLoadingView = contentView.findViewById(R.id.loading);
        mReloadView = contentView.findViewById(R.id.retry);
        mReloadButton = (Button) mReloadView.findViewById(R.id.btn_reload);
        mReloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadPage(mPage);
            }
        });

        showLoading();
        loadFirstPage();
        return contentView;
    }

    private void showContent() {
        mLoadingView.setVisibility(View.GONE);
        mReloadView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mReloadView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }

    private void showReload() {
        mLoadingView.setVisibility(View.GONE);
        mReloadView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    private void loadFirstPage() {
        mPage = 0;
        mAdapter.clearData();
        loadPage(mPage);
    }

    private void loadNextPage() {
        loadPage(mPage);
    }

    private int mPage = 0;

    private void loadPage(int page) {
        BLog.d("BLUE", "loading page " + mPage);
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        CategoryControl.getMeiziList(mCategoryItem.getID(), page,
                new Response.Listener<List<MeiziM>>() {

                    @Override
                    public void onResponse(List<MeiziM> response) {
                        mAdapter.addData(response);
                        mPage++;
                        if (response.size() != 0) {
                            mLoadingFooter.setState(LoadingFooter.State.Idle);
                        } else {
                            mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                        }

                        showContent();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mPage == 0) {
                            showReload();
                        } else {
                            mLoadingFooter.setState(LoadingFooter.State.Error);
                        }
                        Toast.makeText(getActivity(), R.string.msg_loading_failed,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }, this);
    }

    public void loadFirstPageAndScrollToTop() {
        ListViewUtils.smoothScrollListViewToTop(mListView);
        loadFirstPage();
    }
}
