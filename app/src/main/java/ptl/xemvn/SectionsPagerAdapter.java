package ptl.xemvn;

/**
 * Created by phamtanlong on 12/2/17.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ptl.xemvn.rss.RssFeedModel;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public ArrayList<RssFeedModel> listData = new ArrayList<>();
    public HashMap<Integer, PlaceholderFragment> listFragment = new HashMap<>();

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<RssFeedModel> list) {
        super(fm);
        listData = list;
        listFragment = new HashMap<>();
    }

    public void updateData (ArrayList<RssFeedModel> list) {
        listData = list;

        for(Map.Entry<Integer, PlaceholderFragment> entry : listFragment.entrySet()) {
            Integer number = entry.getKey();
            PlaceholderFragment fragment = entry.getValue();
            if (fragment != null) {
                fragment.updateView(number, listData.get(number));
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        PlaceholderFragment f = PlaceholderFragment.newInstance(position + 1, listData.get(position));
        listFragment.put(position, f);
        return f;
    }

    @Override
    public int getCount() {
        return listData.size();
    }
}

