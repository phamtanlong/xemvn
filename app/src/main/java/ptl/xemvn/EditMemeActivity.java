package ptl.xemvn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ptl.xemvn.meme.MemeContent;

public class EditMemeActivity extends AppCompatActivity {

    private MemeContent.MemeItem memeItem;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meme);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String id = getIntent().getExtras().getString("meme");
        memeItem = MemeContent.getItem(id);

        try {
            ConstraintLayout layout = findViewById(R.id.meme_edit_layout);
            InputStream input = new java.net.URL(memeItem.link).openStream();
            layout.setBackground(Drawable.createFromStream(input, "name"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_meme_done) {
            onDone();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_meme_drawer, menu);
        return true;
    }

    private void onDone () {
        EditText editTextTop = findViewById(R.id.meme_top_text);
        EditText editTextBottom = findViewById(R.id.meme_bottom_text);

        try {
            InputStream input = new java.net.URL(memeItem.link).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input)
                    .copy(Bitmap.Config.ARGB_8888, true); //clone to an Mutable bitmap

            Paint paint = new Paint();
            paint.setColor(editTextTop.getCurrentTextColor());
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

            float spTextSize = editTextTop.getTextSize();
            float pxTextSize = convertSpToPixels(spTextSize, EditMemeActivity.this);
            paint.setTextSize(pxTextSize);
            paint.setTextAlign(Paint.Align.CENTER);

            Canvas canvas = new Canvas(bitmap);

            canvas.drawText(editTextTop.getText().toString(), 0.5f * bitmap.getWidth(), 0.3f * bitmap.getHeight(), paint);

            canvas.drawText(editTextBottom.getText().toString(), 0.5f * bitmap.getWidth(), 0.8f * bitmap.getHeight(), paint);

            BitmapDrawable res = new BitmapDrawable(bitmap);
            Bitmap resBitmap = res.getBitmap();

            saveBitmap(resBitmap, getNewImageName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveBitmap (Bitmap bitmap, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    MediaScannerConnection.scanFile(this, new String[] { path.toString() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });

                    Toast.makeText(EditMemeActivity.this, "Save file done!", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRootFolder() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
        file.mkdirs();
        return file.getAbsolutePath();
    }

    private String getNewImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return getRootFolder() + "/IMG_" + timeStamp + ".png";
    }

    public static float convertDpToPixels(float dp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static float convertSpToPixels(float sp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static float convertDpToSp(float dp, Context context) {
        float sp = (convertDpToPixels(dp, context) / (float) convertSpToPixels(dp, context));
        return sp;
    }

    public static float convertPxToDp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float convertSpToDp(float sp, Context context) {
        float px = convertSpToPixels(sp, context);
        float dp = convertPxToDp(px, context);
        return dp;
    }
}
