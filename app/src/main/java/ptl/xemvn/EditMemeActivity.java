package ptl.xemvn;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ptl.xemvn.meme.MemeContent;

public class EditMemeActivity extends AppCompatActivity {

    private MemeContent.MemeItem memeItem;
    private EditText editTextTop;
    private EditText editTextBottom;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meme);

        editTextTop = findViewById(R.id.meme_top_text);
        editTextBottom = findViewById(R.id.meme_bottom_text);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String id = getIntent().getExtras().getString("meme");
        memeItem = MemeContent.getItem(id);

        try {
            //ConstraintLayout layout = findViewById(R.id.meme_edit_layout);
            int resId = getResources().getIdentifier(memeItem.id, "drawable", getPackageName());
            ImageView imageView = findViewById(R.id.meme_image_edit);
            imageView.setImageResource(resId);
            //layout.setBackgroundResource(resId);
            //InputStream input = new java.net.URL(memeItem.link).openStream();
            //layout.setBackground(Drawable.createFromStream(input, "name"));
        } catch (Exception e) {
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
        }  else if (id == R.id.action_meme_textcolor) {
            onChooseTextColor(EditMemeActivity.this);
        }  else if (id == R.id.action_meme_bgcolor) {
            onChooseTextBackgroundColor(EditMemeActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_meme_drawer, menu);
        return true;
    }

    private void onChooseTextBackgroundColor(Context context) {
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Màu nền")
                .initialColor(Color.TRANSPARENT)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        editTextTop.setBackgroundColor(selectedColor);
                        editTextBottom.setBackgroundColor(selectedColor);
                    }
                })
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        editTextTop.setBackgroundColor(selectedColor);
                        editTextBottom.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void onChooseTextColor(Context context) {

        int currentBackgroundColor = editTextTop.getCurrentTextColor();

        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Màu chữ")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        editTextTop.setTextColor(selectedColor);
                        editTextBottom.setTextColor(selectedColor);
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        editTextTop.setTextColor(selectedColor);
                        editTextBottom.setTextColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void onDone () {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextTop.getWindowToken(), 0);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RelativeLayout layout = findViewById(R.id.meme_edit_layout);

        editTextTop.setFocusableInTouchMode(false);
        editTextTop.setFocusable(false);
        editTextTop.clearFocus();

        editTextBottom.setFocusableInTouchMode(false);
        editTextBottom.setFocusable(false);
        editTextBottom.clearFocus();

        layout.setDrawingCacheEnabled(true);
        Bitmap bmScreen = layout.getDrawingCache();

        saveBitmap(bmScreen, getNewImageName());

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
