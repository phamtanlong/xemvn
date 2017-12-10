package ptl.xemvn.meme;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MemeContent {

    public static final List<MemeItem> listItems = new ArrayList<MemeItem>();

    static {
        for (int i = 0; i < 40; i++) {
            addItem(new MemeItem("meme_" + i, "http://192.168.1.109/meme/meme_" + i + ".jpg"));
        }
    }

    public static MemeItem getItem (String id) {
        for (MemeItem item : listItems) {
            if (item.id.equals(id))
                return item;
        }
        return null;
    }

    private static void addItem(MemeItem item) {
        listItems.add(item);
    }

    public static class MemeItem {
        public final String id;
        public final String link;

        public MemeItem(String id, String link) {
            this.id = id;
            this.link = link;
        }
    }
}
