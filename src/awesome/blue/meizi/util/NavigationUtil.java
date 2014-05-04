
package awesome.blue.meizi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import awesome.blue.meizi.ui.TopicActivity;

public class NavigationUtil {

    public static void startBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void startTopicActivity(Activity activity, String topicUrl) {
        Intent intent = new Intent(activity, TopicActivity.class);
        intent.putExtra(TopicActivity.ARG_KEY_URL, topicUrl);
        activity.startActivity(intent);
    }
}
