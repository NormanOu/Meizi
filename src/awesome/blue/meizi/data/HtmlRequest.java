
package awesome.blue.meizi.data;

import java.io.UnsupportedEncodingException;

import awesome.blue.meizi.control.HtmlDecoderBase;
import awesome.blue.meizi.util.BLog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

public class HtmlRequest<T> extends Request<T> {

    private static final String TAG = HtmlRequest.class.getSimpleName();

    private final Listener<T> mListener;

    private HtmlDecoderBase<T> mHtmlDecoder;

    public HtmlRequest(int method, String url, Listener<T> listener, ErrorListener errorListener,
            HtmlDecoderBase<T> htmlDecoder) {
        super(method, url, errorListener);

        mListener = listener;
        mHtmlDecoder = htmlDecoder;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000,
                2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        BLog.dLong(TAG, "response.statusCode is " + response.statusCode);
        String htmlString = "";
        try {
            htmlString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }

        T result = mHtmlDecoder.decode(htmlString);
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}
