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
import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        caches = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if (position + 5 > nextMaxCount) {
            nextMaxCount += 5;
        } else if (position + 10 < maxCount) {
            nextMaxCount -= 5;
        }

        Log.i("Fuck","------Get Item = " + position);

        //get in preload
        PlaceholderFragment f;
        if (caches.containsKey(position)) {
            f = caches.get(position);
            caches.remove(position);
        } else {
            f = PlaceholderFragment.newInstance(position + 1);
        }

        //preload
        for (int i = 2; i < 7; ++i) {
            Integer index = position + i;
            if (!caches.containsKey(index)) {
                PlaceholderFragment fragment = PlaceholderFragment.newInstance(index + 1);
                caches.put(index, fragment);
                Log.i("Fuck", "@@@@@@ Preload Item " + index);
            }
        }

        return f;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        if (nextMaxCount != maxCount) {
            maxCount = nextMaxCount;
            notifyDataSetChanged();
        }
        Log.i("Fuck","Get Count = " + maxCount);
        return maxCount;
    }

    //preload
    public HashMap<Integer, PlaceholderFragment> caches;
    public int maxCount = 5;
    public int nextMaxCount = 5;

}

