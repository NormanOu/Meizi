package awesome.blue.meizi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

	private ConnectivityManager mConnMgr;
	private Context mContext;

	public NetworkHelper(Context context) {
		mContext = context;
		mConnMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public boolean isOnline() {
		boolean result = false;
		NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();
		if (networkInfo != null) {
			result = networkInfo.isConnected();
		}
		
		return result;
	}

}
