
package awesome.blue.meizi.control;

import java.util.List;

import awesome.blue.meizi.data.HtmlRequest;
import awesome.blue.meizi.data.RequestManager;
import awesome.blue.meizi.model.MeiziM;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;

public class CategoryControl {
    private static String categoryUrl = "http://www.dbmeizi.com/category/10";

    // public static void getTimeLine(int page, int per,
    // Response.Listener<List<Activity>> listener,
    // Response.ErrorListener errorListener, Object tag) {
    // String url = HepaiApi.GET_TIMELINE;
    // url += String.format("?" + HepaiApi.PARAM_PAGE, page, per);
    // Request<List<Activity>> request = new
    // GsonRequest<List<Activity>>(Method.GET, url,
    // new TypeToken<List<Activity>>() {
    // }.getType(), null, listener, errorListener, true);
    // RequestManager.addRequest(request, tag);
    // }

    public static void getAll(int page, Response.Listener<List<MeiziM>> listener,
            Response.ErrorListener errorListener, Object tag) {
        Request<List<MeiziM>> request = new HtmlRequest<List<MeiziM>>(Method.GET, categoryUrl,
                listener, errorListener);

        RequestManager.addRequest(request, tag);
    }

    // public static class CategoryDecoder<List<MeiziM>> extends
    // HtmlDecoderBase<List<MeiziM>> {
    //
    // @Override
    // protected List<MeiziM> decode(String html) {
    // return null;
    // }
    // }
}
