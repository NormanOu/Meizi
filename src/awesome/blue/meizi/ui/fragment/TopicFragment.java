
package awesome.blue.meizi.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import awesome.blue.meizi.R;
import awesome.blue.meizi.control.CategoryControl;
import awesome.blue.meizi.model.TopicM;
import awesome.blue.meizi.ui.adapter.TopicAdapter;
import awesome.blue.meizi.util.DateUtil;
import awesome.blue.meizi.util.NavigationUtil;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class TopicFragment extends BaseFragment {

    private static final String TAG = TopicFragment.class.getSimpleName();

    private ListView mListView;

    private View mLoadingView;

    private View mReloadView;

    private Button mReloadButton;

    private TopicAdapter mAdapter;

    private View mHeader;

    private TextView mHeaderTitle;

    private TextView mHeaderPoster;

    private TextView mHeaderTopic;

    private TextView mHeaderDate;

    private TopicM mTopicM;

    private String mUrl;
    public static final String ARG_KEY_URL = "arg_key_url";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragmentArgs();
        View contentView = inflater.inflate(R.layout.fragment_topic, null);
        mAdapter = new TopicAdapter(getActivity());
        mListView = (ListView) contentView.findViewById(R.id.listView);
        mHeader = initHeader(inflater);
        mListView.addHeaderView(mHeader);
        mListView.setAdapter(mAdapter);

        mLoadingView = contentView.findViewById(R.id.loading);
        mReloadView = contentView.findViewById(R.id.retry);
        mReloadButton = (Button) mReloadView.findViewById(R.id.btn_reload);
        mReloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        showLoading();
        loadData();
        return contentView;
    }

    private void initFragmentArgs() {
        Bundle bundle = getArguments();
        mUrl = bundle.getString(ARG_KEY_URL);
    }

    private void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mReloadView.setVisibility(View.GONE);
    }

    private void showContent() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mReloadView.setVisibility(View.GONE);
    }

    private void showReload() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mReloadView.setVisibility(View.VISIBLE);
    }

    private View initHeader(LayoutInflater inflater) {
        View header = inflater.inflate(R.layout.listitem_topic, null);
        mHeaderTitle = (TextView) header.findViewById(R.id.title);
        mHeaderPoster = (TextView) header.findViewById(R.id.poster_ori);
        mHeaderTopic = (TextView) header.findViewById(R.id.topic_ori);
        mHeaderDate = (TextView) header.findViewById(R.id.date);

        mHeaderPoster.setOnClickListener(mOnClickListener);
        mHeaderTopic.setOnClickListener(mOnClickListener);

        return header;
    }

    private void loadData() {
        CategoryControl.getTopic(mUrl, new Response.Listener<TopicM>() {

            @Override
            public void onResponse(TopicM response) {
                mTopicM = response;
                mAdapter.setData(mTopicM.content);
                mHeaderDate.setText(DateUtil.getDateString(mTopicM.date));
                mHeaderTitle.setText(mTopicM.title);

                showContent();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showReload();
            }
        }, this);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.equals(mHeaderPoster)) {
                NavigationUtil.startBrowser(getActivity(), mTopicM.doubanPosterUrl);
            } else if (v.equals(mHeaderTopic)) {
                NavigationUtil.startBrowser(getActivity(), mTopicM.doubanTopicUrl);
            }
        }
    };
}
