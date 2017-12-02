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

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);

//        TextView textView = (TextView) rootView.findViewById(R.id.section_title);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        ImageView imageView = (ImageView) rootView.findViewById(R.id.section_image);
        DownloadImage downloadImage = new DownloadImage(imageView);

        int number = getArguments().getInt(ARG_SECTION_NUMBER);
        //
        String url = "http://192.168.1.104/images/image_" + number + ".jpg";

        downloadImage.execute(url);

        return rootView;
    }
}
