
package awesome.blue.meizi.model;

public class MeiziM {

    public String smallPicUrl;

    public String largePicUrl;

    public int starCount;

    /**
     * If this MeiziM(pic) is stared by the current user, false if not logged in
     */
    public boolean stared;

    /**
     * The poster of this MeiziM(pic) in douban.com
     */
    public String doubanPosterUrl;

    /**
     * The url for the topic that shares this MeiziM(pci)
     */
    public String topicUrl;
}
