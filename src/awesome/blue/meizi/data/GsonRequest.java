
package awesome.blue.meizi.data;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import android.util.Log;
import awesome.blue.meizi.util.GlobalDebugControl;
import awesome.blue.meizi.util.LongLogHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by
 * Gson.
 */
public class GsonRequest<T> extends Request<T> {

    private static final String TAG = "GsonRequest";

    protected final Gson gson = HepaiGson.getGson();
    private final Class<T> clazz;
    private final Type classType;
    private Map<String, String> headers;
    private final Listener<T> listener;

    private final boolean withToken;

    /**
     * Make a GET request and return a parsed object from JSON.
     * 
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
            Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, headers, listener, errorListener, false);
    }

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
            Listener<T> listener, ErrorListener errorListener, boolean withToken) {
        this(Method.GET, url, clazz, headers, listener, errorListener, withToken);
    }

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
            Listener<T> listener, ErrorListener errorListener, boolean withToken) {
        super(method, url, errorListener);
        if (GlobalDebugControl.DEBUG_API) {
            Log.v(TAG, "url: " + url);
        }
        this.clazz = clazz;
        this.classType = null;
        this.headers = headers;
        this.listener = listener;
        this.withToken = withToken;
    }

    public GsonRequest(int method, String url, Type type, Map<String, String> headers,
            Listener<T> listener, ErrorListener errorListener, boolean withToken) {
        super(method, url, errorListener);
        this.clazz = null;
        this.classType = type;
        this.headers = headers;
        this.listener = listener;
        this.withToken = withToken;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // if (withToken && HepaiApi.sToken != null) {
        // if (headers == null) {
        // headers = new HashMap<String, String>();
        // }
        // headers.put("X-USER-ACCESS-TOKEN", HepaiApi.sToken);
        // }
        // return headers != null ? headers : super.getHeaders();
        return super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (GlobalDebugControl.DEBUG_API) {
            Log.v(TAG, "response.statusCode is " + response.statusCode);
        }

        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (GlobalDebugControl.DEBUG_API) {
                Log.v(TAG, "the result json is " + json);
                LongLogHelper.logLong(TAG, json);
            }
            if (clazz != null) {
                return Response.success(gson.fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success((T) gson.fromJson(json, classType),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
