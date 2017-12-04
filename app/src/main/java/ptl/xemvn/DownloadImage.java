package ptl.xemvn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by phamtanlong on 12/2/17.
 */

// DownloadImage AsyncTask
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    public ImageView imageView;
    public View mProgressView;

    public DownloadImage (ImageView imageView1, View progressView) {
        super();
        this.imageView = imageView1;
        this.mProgressView = progressView;
    }

    @Override
    protected Bitmap doInBackground(String... URL) {
        String imageURL = URL[0];

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // Set the bitmap into ImageView
        showProgress(false);
        if (imageView != null)
            imageView.setImageBitmap(result);
    }

    public void showProgress(final boolean show) {
        if (mProgressView != null)
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (imageView != null)
            imageView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}