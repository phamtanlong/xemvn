package ptl.xemvn;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.DeviceShareDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import ptl.xemvn.rss.FetchFeedTask;
import ptl.xemvn.rss.RssFeedModel;
import ptl.xemvn.rss.RssFetchListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RssFetchListener {

    public int currentPage = R.id.nav_new;

    public String rssNew = "https://xem.vn/rss/";
    public String rssVote = "https://xem.vn/vote.rss/";
    public String rssHot = "https://xem.vn/hot.rss/";

    private NavigationView navigationView;
    private MenuItem facebookItem;
    private View mProgressView;
    private View mTabbedContainter;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        Menu menuNav = navigationView.getMenu();
        facebookItem = menuNav.findItem(R.id.nav_login);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTabbedContainter = findViewById(R.id.tabbed_container);
        mProgressView = findViewById(R.id.download_progress);

        fetchRssFeed(rssNew);

        ignoreFileUriExposedException();

        requestPermissions();

        setupFacebook();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.big_smile)
                    .setTitle("XemVN")
                    .setMessage("Bạn có thực sự muốn thoát?")
                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
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
        } else if (id == R.id.action_refresh) {
            switch (currentPage) {
                case R.id.nav_new:
                    fetchRssFeed(rssNew);
                    break;
                case R.id.nav_vote:
                    fetchRssFeed(rssVote);
                    break;
                case R.id.nav_hot:
                    fetchRssFeed(rssHot);
                    break;
            }
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
            Intent intent = new Intent(getApplicationContext(), UploadImageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_meme) {
            Intent intent = new Intent(getApplicationContext(), MemeListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            if (isLogedIn()) {
                logoutFacebook();
            } else {
                loginFacebook();
            }
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_comment:
                    Log.i("Fuck", "Nagigate comment");
                    openCommentWebView();
                    return true;
                case R.id.navigation_download:
                    Log.i("Fuck", "Nagigate download");
                    downloadCurrentImage();
                    return true;
                case R.id.navigation_share:
                    Log.i("Fuck", "Nagigate share");
                    shareCurrentImage();
                    return true;
            }
            return false;
        }
    };

    private void ignoreFileUriExposedException () {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void requestPermissions () {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            },1024);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupFacebook () {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.i("Facebook", "Login success----------");
                    updateFacebookState();
                }

                @Override
                public void onCancel() {
                    // App code
                    Log.i("Facebook", "Login cancel----------");
                    updateFacebookState();
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.i("Facebook", "Login error------------");
                    updateFacebookState();
                }
            });

        updateFacebookState();
    }

    private void updateFacebookState () {
        //update logged in status & ui
        if (isLogedIn()) {
            facebookItem.setTitle("Đăng xuất");
        } else {
            facebookItem.setTitle("Đăng nhập");
        }
    }

    private void loginFacebook () {
        if (!isLogedIn()) {
            Log.i("Facebook", "Login begin...");
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }

    private void logoutFacebook () {
        if (isLogedIn()) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.com_facebook_favicon_blue)
                    .setTitle("XemVN")
                    .setMessage("Bạn có thực sự muốn thoát Facebook?")
                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Facebook", "Logout out out...");
                            LoginManager.getInstance().logOut();
                            updateFacebookState();
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }
    }

    private boolean isLogedIn () {
        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token != null) { // && !token.isExpired() && !TextUtils.isEmpty(token.getToken())
            Log.i("Facebook", "Token != null");
            return true;
        } else {
            Log.i("Facebook", "Token == null");
            return false;
        }
    }

    private void openCommentWebView () {
        RssFeedModel model = getCurrentModel();
        if (model == null) {
            Toast.makeText(MainActivity.this, "Nothing found", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("url", model.link);
        startActivity(intent);
    }

    private void downloadCurrentImage () {
        RssFeedModel model = getCurrentModel();
        if (model == null) {
            Toast.makeText(MainActivity.this, "Nothing found", Toast.LENGTH_LONG).show();
            return;
        }

        DownloadImage downloadImage = new DownloadImage(null, null);

        try {
            Bitmap bitmap = downloadImage.execute(model.imageLink).get();
            saveImage(bitmap, model.title.trim().replace(" ", "-") + "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void shareCurrentImage () {
        RssFeedModel model = getCurrentModel();
        if (model == null) {
            Toast.makeText(MainActivity.this, "Nothing found", Toast.LENGTH_LONG).show();
            return;
        }

        ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(model.link))
                        .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    private RssFeedModel getCurrentModel () {
        int current = mViewPager.getCurrentItem();
        if (mSectionsPagerAdapter.listData.size() > current) {
            return mSectionsPagerAdapter.listData.get(current);
        } else {
            return null;
        }
    }

    private void saveImage(Bitmap finalBitmap, String name) {

        //String root = Environment.getExternalStorageDirectory().toString();
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/XemVN");
        myDir.mkdirs();

        File file = new File(myDir, name + ".jpg");

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });

        Toast.makeText(this.getBaseContext(), "Save file done!", Toast.LENGTH_LONG).show();
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

    @Override
    public void onComplete(ArrayList<RssFeedModel> list) {
        showProgress(false);

        // Set up the ViewPager with the sections adapter.
        if (mSectionsPagerAdapter != null && mViewPager != null) {

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
