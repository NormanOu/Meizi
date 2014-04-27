
package awesome.blue.meizi.ui.fragment;

import android.support.v4.app.Fragment;
import awesome.blue.meizi.data.RequestManager;

public class BaseFragment extends Fragment {

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

}
