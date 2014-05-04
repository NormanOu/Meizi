
package awesome.blue.meizi.ui.fragment;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import awesome.blue.meizi.data.RequestManager;

public class BaseFragment extends Fragment {

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

    /**
     * TODO unimplemented yet, might be useful
     * 
     * @param key
     * @param value
     */
    @Deprecated
    public void addArgument(String key, String value) {
    }

    @Deprecated
    public void getArgument(String key, String value) {
    }

    protected void showToast(String toast) {

    }

    private static final int MSG_TOAST = 1;

    private Handler mHandler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOAST:
                    String toast = (String) msg.obj;
                    Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
            return false;
        }
    });
}
