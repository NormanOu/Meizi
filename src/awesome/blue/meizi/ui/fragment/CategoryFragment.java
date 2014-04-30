
package awesome.blue.meizi.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import awesome.blue.meizi.R;
import awesome.blue.meizi.control.CategoryControl;
import awesome.blue.meizi.model.MeiziM;
import awesome.blue.meizi.ui.adapter.CardsAnimationAdapter;
import awesome.blue.meizi.ui.adapter.CategoryAdapter;
import awesome.blue.meizi.util.BLog;
import awesome.blue.meizi.view.LoadingFooter;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;

public class CategoryFragment extends BaseFragment {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    private ListView mListView;

    private CategoryAdapter mAdapter;

    private LoadingFooter mLoadingFooter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View conteView = inflater.inflate(R.layout.fragment_category, null);
        mListView = (ListView) conteView.findViewById(R.id.listView);
        mAdapter = new CategoryAdapter(getActivity(), mListView);
        mLoadingFooter = new LoadingFooter(getActivity());

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
                if (mLoadingFooter.getState() == LoadingFooter.State.Loading
                        || mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
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
                // TODO Auto-generated method stub

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                return true;
            }
        });

        loadFirstPage();
        CategoryControl.getTopic("http://www.dbmeizi.com/topic/47370", new Listener() {

            @Override
            public void onResponse(Object response) {
                // TODO Auto-generated method stub

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        }, this);
        return conteView;
    }

    private void loadFirstPage() {
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        loadPage(0);
    }

    private void loadNextPage() {
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        loadPage(mPage);
    }

    private int mPage = 0;

    private void loadPage(int page) {
        BLog.d("BLUE", "loading page " + mPage);
        CategoryControl.getMeiziList(page, new Response.Listener<List<MeiziM>>() {

            @Override
            public void onResponse(List<MeiziM> response) {
                mAdapter.addData(response);
                mPage++;
                if (response.size() != 0) {
                    mLoadingFooter.setState(LoadingFooter.State.Idle);
                } else {
                    mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadingFooter.setState(LoadingFooter.State.Idle);
            }
        }, this);
    }
}
