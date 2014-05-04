
package awesome.blue.meizi.control;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.Html;
import android.text.TextUtils;
import awesome.blue.meizi.data.HtmlRequest;
import awesome.blue.meizi.data.RequestManager;
import awesome.blue.meizi.model.MeiziM;
import awesome.blue.meizi.model.TopicM;
import awesome.blue.meizi.model.TopicM.ContentItemType;
import awesome.blue.meizi.model.TopicM.TopicContentItem;
import awesome.blue.meizi.util.BLog;
import awesome.blue.meizi.util.DateUtil;
import awesome.blue.meizi.util.GlobalDebugControl;
import awesome.blue.meizi.util.StringUtils;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;

public class CategoryControl {

    private static final String TAG = CategoryControl.class.getSimpleName();

    private static String sHost = "http://www.dbmeizi.com";

    private static String sCategoryUrl = sHost + "/category/";

    /**
     * Get the data of Category All
     * 
     * @param page The page, which starts from 0
     * @param listener
     * @param errorListener
     * @param tag
     */
    public static void getMeiziList(int categoryID, int page,
            Response.Listener<List<MeiziM>> listener,
            Response.ErrorListener errorListener, Object tag) {
        String url = sCategoryUrl + categoryID + "?p=" + page;
        Request<List<MeiziM>> request = new HtmlRequest<List<MeiziM>>(Method.GET, url,
                listener, errorListener, CategoryDecoder.getInstance());

        RequestManager.addRequest(request, tag);
    }

    public static void getTopic(String url, Response.Listener<TopicM> listener,
            Response.ErrorListener errorListener, Object tag) {
        Request<TopicM> request = new HtmlRequest<TopicM>(Method.GET, url, listener, errorListener,
                TopicDecoder.getInstance());
        RequestManager.addRequest(request, tag);
    }

    public static class TopicDecoder extends HtmlDecoderBase<TopicM> {

        private static TopicDecoder mDecoder = null;

        public static HtmlDecoderBase<TopicM> getInstance() {
            if (mDecoder == null) {
                mDecoder = new TopicDecoder();
            }

            return mDecoder;
        }

        @Override
        public TopicM decode(String html) {
            TopicM resulTopicM = new TopicM();

            Document document = Jsoup.parse(html);

            // get title string
            resulTopicM.title = document.select("div.row div.span9 h4").html().trim();
            // get doubanPosterUrl and doubanTopicurl
            Elements urlElements = document.select("div.row div.span6 div.content-meta a");
            if (urlElements.size() >= 1) {
                resulTopicM.doubanPosterUrl = urlElements.get(0).attr("href");
            }
            if (urlElements.size() >= 2) {
                resulTopicM.doubanTopicUrl = urlElements.get(1).attr("href");
            }
            // get post time
            String dateString = document.select("div.row div.span6").html();
            String startString = "title=\"最后更新时间\"></span> ";
            int startPos = dateString.indexOf(startString) + startString.length();
            String endString = "<span class=\"icon-arrow-right";
            int endPos = dateString.indexOf(endString);
            dateString = dateString.substring(startPos, endPos).trim();
            BLog.d(TAG, "dateString is " + dateString);
            try {
                resulTopicM.date = DateUtil.getDate(dateString);
            } catch (ParseException e) {
                BLog.e(TAG, "got topic date failed, exception is \n" + e.getMessage());
            }

            // get the main content, pictures and messages
            Elements contentElements = document.select("div.row div.span6 div.content").first()
                    .children();

            for (int i = 0; i < contentElements.size(); i++) {
                Element element = contentElements.get(i);
                String tagName = element.tagName();
                if (tagName.equals("p")) {
                    // in case incorrect syntax
                    Elements pImgElements = element.select("img");
                    if (pImgElements.size() != 0) {
                        for (int j = 0; j < pImgElements.size(); j++) {
                            TopicContentItem item = new TopicContentItem();
                            item.type = ContentItemType.IMAGE;
                            item.imgUrl = pImgElements.get(j).attr("src");

                            if (!TextUtils.isEmpty(item.imgUrl)) {
                                resulTopicM.content.add(item);
                            }
                        }
                    } else {
                        TopicContentItem item = new TopicContentItem();
                        item.type = ContentItemType.MSG;
                        String pContent = contentElements.get(i).html();
                        item.msg = Html.fromHtml(pContent).toString().trim();

                        if (!TextUtils.isEmpty(item.msg)) {
                            resulTopicM.content.add(item);
                        }
                    }
                } else if (tagName.equals("div")) {
                    TopicContentItem item = new TopicContentItem();
                    item.type = ContentItemType.IMAGE;
                    Elements divElements = element.select("img");
                    Element imgElement = divElements.first();
                    if (imgElement != null) {
                        item.imgUrl = imgElement.attr("src");
                    }

                    if (!TextUtils.isEmpty(item.imgUrl)) {
                        resulTopicM.content.add(item);
                    }
                } else if (tagName.equals("img")) {
                    TopicContentItem item = new TopicContentItem();
                    item.type = ContentItemType.IMAGE;
                    item.imgUrl = element.attr("src");

                    if (!TextUtils.isEmpty(item.imgUrl)) {
                        resulTopicM.content.add(item);
                    }
                }
            }

            return resulTopicM;
        }
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
