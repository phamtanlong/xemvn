package ptl.xemvn.rss;

/**
 * Created by phamtanlong on 12/3/17.
 */

public class RssFeedModel {

    public String title;
    public String link;
    public String description;

    public String imageLink;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
