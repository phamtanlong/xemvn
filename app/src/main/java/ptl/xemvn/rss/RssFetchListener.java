package ptl.xemvn.rss;

import java.util.ArrayList;

/**
 * Created by phamtanlong on 12/5/17.
 */

public interface RssFetchListener {
    void onComplete (ArrayList<RssFeedModel> list);
}
