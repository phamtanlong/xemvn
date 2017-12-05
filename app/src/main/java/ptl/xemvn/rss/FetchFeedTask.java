package ptl.xemvn.rss;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ptl.xemvn.NavigationDrawerActivity;

/**
 * Created by phamtanlong on 12/3/17.
 */

public class FetchFeedTask extends AsyncTask<Void, Void, ArrayList<RssFeedModel>> {

    protected RssFetchListener parentActivity;
    protected String urlLink;

    public FetchFeedTask (RssFetchListener activity, String url) {
        super();
        parentActivity = activity;
        urlLink = url;
    }

    @Override
    protected ArrayList<RssFeedModel> doInBackground(Void... voids) {
        if (TextUtils.isEmpty(urlLink))
            return new ArrayList<>();

        try {
            if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "http://" + urlLink;

            URL url = new URL(urlLink);
            InputStream inputStream = url.openConnection().getInputStream();
            return parseFeed(inputStream);
        } catch (IOException e) {
            Log.e("Fuck", "Error", e);
        } catch (XmlPullParserException e) {
            Log.e("Fuck", "Error", e);
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(ArrayList<RssFeedModel> list) {
        super.onPostExecute(list);
        parentActivity.onComplete(list);
    }

    protected ArrayList<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        ArrayList<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item") || name.equalsIgnoreCase("items")) {
                        isItem = true;
                        continue;
                    }
                }

                if (!isItem)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);

                        //get link image from description
                        int start = description.indexOf("<img src=");
                        int end = description.lastIndexOf(".jpg");
                        String image = description.substring(start + "<img src=".length() + 1, end + ".jpg".length());

                        item.imageLink = image;
                        items.add(item);

                        //Log.i("Title", item.title);
                        //Log.i("Image", image);
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

}
