
package awesome.blue.meizi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TopicM {

    public String doubanPosterUrl;

    public String title;

    public String doubanTopicUrl;

    public List<TopicContentItem> content;

    public Date date;

    public TopicM() {
        date = new Date();
        content = new ArrayList<TopicM.TopicContentItem>();
    }

    public static class TopicContentItem {
        public ContentItemType type;

        public String imgUrl;

        public String msg;
    }

    public enum ContentItemType {
        MSG,
        IMAGE
    }
}
