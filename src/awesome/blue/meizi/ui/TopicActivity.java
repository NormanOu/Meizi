
package awesome.blue.meizi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import awesome.blue.meizi.R;
import awesome.blue.meizi.ui.fragment.TopicFragment;

public class TopicActivity extends FragmentActivity {

    private TopicFragment mTopicFragment;

    private String mTopicUrl;

    public static final String ARG_KEY_URL = "arg_key_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArgs();

        setContentView(R.layout.activity_topic);

        mTopicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopicFragment.ARG_KEY_URL, mTopicUrl);
        mTopicFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mTopicFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initArgs() {
        Intent intent = getIntent();
        mTopicUrl = intent.getStringExtra(ARG_KEY_URL);
    }
}
