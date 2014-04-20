package awesome.blue.meizi.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.util.Log;
import awesome.blue.meizi.util.GlobalDebugControl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonPostRequest<TPost, TReturn> extends GsonRequest<TReturn> {

    private static Map<String, String> HEADER_JSON = new HashMap<String, String>();
    static {
        HEADER_JSON.put("Content-Type", "application/json");
        HEADER_JSON.put("charset", "utf-8");
    }

    private static final String TAG = "GsonPostRequest";

    private TPost postData;

    public GsonPostRequest(int method, TPost postData, String url,
            Class<TReturn> clazz, Listener<TReturn> listener,
            ErrorListener errorListener) {
        super(method, url, clazz, HEADER_JSON, listener, errorListener, false);

        this.postData = postData;
    }

    public GsonPostRequest(int method, TPost postData, String url,
            Class<TReturn> clazz, Listener<TReturn> listener,
            ErrorListener errorListener, boolean withToken) {
        super(method, url, clazz, HEADER_JSON, listener, errorListener, withToken);

        this.postData = postData;
    }
    
    public GsonPostRequest(int method, TPost postData, String url,
            Type type, Listener<TReturn> listener,
            ErrorListener errorListener, boolean withToken) {
        super(method, url, type, HEADER_JSON, listener, errorListener, withToken);

        this.postData = postData;
    }

    public byte[] getBody() throws AuthFailureError {
        if (postData != null) {
            String data = gson.toJson(postData);
            if (GlobalDebugControl.DEBUG_JSON) {
                Log.v(TAG, "json we sent: " + data);
            }
            return data.getBytes();
        }
        return null;
    }

}
