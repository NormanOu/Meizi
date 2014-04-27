
package awesome.blue.meizi.control;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;
import awesome.blue.meizi.data.HtmlRequest;
import awesome.blue.meizi.data.RequestManager;
import awesome.blue.meizi.model.MeiziM;
import awesome.blue.meizi.util.BLog;
import awesome.blue.meizi.util.GlobalDebugControl;
import awesome.blue.meizi.util.StringUtils;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;

public class CategoryControl {

    private static final String TAG = CategoryControl.class.getSimpleName();

    private static String sHost = "http://www.dbmeizi.com";

    private static String sCategoryUrl = sHost + "/category/10";

    /**
     * Get the data of Category All
     * 
     * @param page The page, which starts from 0
     * @param listener
     * @param errorListener
     * @param tag
     */
    public static void getAll(int page, Response.Listener<List<MeiziM>> listener,
            Response.ErrorListener errorListener, Object tag) {
        String url = sCategoryUrl + "?p=" + page;
        Request<List<MeiziM>> request = new HtmlRequest<List<MeiziM>>(Method.GET, url,
                listener, errorListener, CategoryDecoder.getInstance());

        RequestManager.addRequest(request, tag);
    }

    public static class CategoryDecoder extends
            HtmlDecoderBase<List<MeiziM>> {

        private static CategoryDecoder mDecoder = null;

        public static HtmlDecoderBase<List<MeiziM>> getInstance() {
            if (mDecoder == null) {
                mDecoder = new CategoryDecoder();
            }

            return mDecoder;
        }

        @Override
        public List<MeiziM> decode(String html) {
            List<MeiziM> result = new ArrayList<MeiziM>();

            if (!TextUtils.isEmpty(html)) {
                Document document = Jsoup.parse(html);
                if (GlobalDebugControl.DEBUG_API) {
                    BLog.dLong(TAG, "items are " + document.select("div.pic").html());
                }

                Elements meiziElements = document.select("div.pic");
                for (int i = 0; i < meiziElements.size(); i++) {
                    Element meiziE = meiziElements.get(i);

                    MeiziM meiziM = new MeiziM();
                    meiziM.dataId = meiziE.attr("data-id");
                    meiziM.smallPicUrl = meiziE.select("img").attr("src");
                    meiziM.largePicUrl = meiziE.select("img").attr("data-bigimg");
                    // TODO [Ou Runqiang] starCount might be loaded from a js
                    // script
                    // meiziM.starCount =
                    Elements urlHolder = meiziE.select("div.bottombar span.fr.p5");
                    if (urlHolder.size() != 0) {
                        meiziM.doubanPosterUrl = urlHolder.get(0).child(0).attr("href");
                        String topicUrl = urlHolder.get(0).child(1).attr("href");
                        if (!TextUtils.isEmpty(topicUrl)) {
                            meiziM.topicUrl = sHost + StringUtils.wrap(topicUrl);
                        }
                    }

                    result.add(meiziM);
                }
            }

            return result;
        }
    }
}
