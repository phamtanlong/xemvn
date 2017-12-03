package ptl.xemvn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ptl.xemvn.R;
import ptl.xemvn.TabbedActivity;
import ptl.xemvn.rss.RssFeedModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public int sectionNumber;
    public RssFeedModel rssFeedModel;

    public PlaceholderFragment() {
        super();
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, RssFeedModel model) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.sectionNumber = sectionNumber;
        fragment.rssFeedModel = model;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_title);
        textView.setText(rssFeedModel.title);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.section_image);
        DownloadImage downloadImage = new DownloadImage(imageView);

        //
        String url = rssFeedModel.imageLink;

        downloadImage.execute(url);

        return rootView;
    }
}
