package ptl.xemvn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import ptl.xemvn.rss.FetchFeedTask;
import ptl.xemvn.rss.RssFeedModel;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public int currentPage = R.id.nav_new;

    public String rssNew = "https://xem.vn/rss/";
    public String rssVote = "https://xem.vn/vote.rss/";
    public String rssHot = "https://xem.vn/hot.rss/";

    private View mProgressView;
    private View mTabbedContainter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("Fuck","Oncreate NavigationDrawerActivity ----------- ");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTabbedContainter = findViewById(R.id.tabbed_container);
        mProgressView = findViewById(R.id.download_progress);

        fetchRssFeed(rssNew);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        currentPage = id;

        if (id == R.id.nav_new) {
            fetchRssFeed(rssNew);
            Log.i("Fuck", "Go to New");
        } else if (id == R.id.nav_vote) {
            fetchRssFeed(rssVote);
            Log.i("Fuck", "Go to Vote");
        } else if (id == R.id.nav_hot) {
            fetchRssFeed(rssHot);
            Log.i("Fuck", "Go to Hot");
        } else if (id == R.id.nav_old) {

        } else if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_meme) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Tabbed View ----------------

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mTabbedContainter.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private void fetchRssFeed (String url) {
        showProgress(true);
        FetchFeedTask f = new FetchFeedTask(this, url);
        f.execute();
    }

    public void onUpdateData (ArrayList<RssFeedModel> list) {
        showProgress(false);
        Log.i("Item", "-----First Item: " + list.get(0).title);
        Log.i("Item", "-----: " + list.get(0).imageLink);

        // Set up the ViewPager with the sections adapter.
        if (mSectionsPagerAdapter != null && mViewPager != null) {

            Log.i("Fuck", "On update data");

            mSectionsPagerAdapter.updateData(list);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);

        } else {

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), list);

            mViewPager = findViewById(R.id.tabbed_container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }

    //Tabbed View End ----------------
}
