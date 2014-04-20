
package awesome.blue.meizi.data;

import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import awesome.blue.meizi.util.BLog;
import awesome.blue.meizi.util.GlobalDebugControl;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class HtmlRequest<T> extends Request<T> {

    private static final String TAG = HtmlRequest.class.getSimpleName();

    private final Listener<T> mListener;

    public HtmlRequest(int method, String url, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);

        mListener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (GlobalDebugControl.isDebug) {
            BLog.dLong(TAG, "response.statusCode is " + response.statusCode);
        }

        try {
            String htmlString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (GlobalDebugControl.isDebug && htmlString != null) {
                BLog.dLong(TAG, htmlString);
            }

            // TODO should move to somewhere better
            Document document = Jsoup.parse(htmlString);
            BLog.dLong(TAG, document.select("div .pic").html());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}
