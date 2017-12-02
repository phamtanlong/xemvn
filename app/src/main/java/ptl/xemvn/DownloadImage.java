package ptl.xemvn;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by phamtanlong on 12/2/17.
 */

// DownloadImage AsyncTask
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    public ImageView imageView;

    public DownloadImage (ImageView imageView1) {
        super();
        this.imageView = imageView1;
    }

//    public ProgressDialog mProgressDialog;
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        // Create a progressdialog
//        mProgressDialog = new ProgressDialog(TabbedActivity.this);
//        // Set progressdialog title
//        mProgressDialog.setTitle("Image Downloader");
//        // Set progressdialog message
//        mProgressDialog.setMessage("Loading...");
//        mProgressDialog.setIndeterminate(false);
//        // Show progressdialog
//        mProgressDialog.show();
//    }

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
        imageView.setImageBitmap(result);
//         //Close progressdialog
//        mProgressDialog.dismiss();
    }
}