package ptl.xemvn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadImageActivity extends AppCompatActivity {

    private ImageView imageview;

    private CheckBox checkBoxSource;
    private EditText editTextSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageview = findViewById(R.id.upload_imageview);

        editTextSource = findViewById(R.id.upload_source);

        checkBoxSource = findViewById(R.id.upload_isyourimage);
        checkBoxSource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    editTextSource.setVisibility(View.VISIBLE);
                } else {
                    editTextSource.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button buttonSelect = findViewById(R.id.upload_button_select);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSelectImage();
            }
        });

        Button buttonUpload = findViewById(R.id.upload_button_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        //max in day string
        TextView textViewMaxInDay = findViewById(R.id.upload_max_in_day);
        String maxInDay = getString(R.string.upload_maxnumber_in_day);
        maxInDay = maxInDay.replace("{0}", "<b>2</b>");
        textViewMaxInDay.setText(Html.fromHtml(maxInDay));

        //number in day string
        TextView textViewNumberInDay = findViewById(R.id.upload_number_in_day);
        String numberInDay = getString(R.string.upload_number_in_day);
        numberInDay = numberInDay.replace("{0}", "<b>1</b>");
        textViewNumberInDay.setText(Html.fromHtml(numberInDay));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadImage () {
        if (imageview.getDrawable() == null) {
            Toast.makeText(UploadImageActivity.this, "No image found", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText editTextTitle = findViewById(R.id.upload_title);
        if (TextUtils.isEmpty(editTextTitle.getText())) {
            editTextTitle.setError("Hãy nhập tên ảnh");
            editTextTitle.requestFocus();
            return;
        }

        String imageSource = "Tự sáng tác";
        boolean isYourImage = checkBoxSource.isChecked();
        if (!isYourImage) {
            imageSource = editTextSource.getText().toString();
        }

        if (TextUtils.isEmpty(imageSource)) {
            editTextSource.setError("Hãy nhập nguồn ảnh");
            editTextSource.requestFocus();
            return;
        }

        Log.i("Upload", "Not implemented");
    }

    private void showDialogSelectImage() {
        CharSequence colors[] = new CharSequence[]{"Thư viện ảnh", "Chụp mới"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert); //must be ActivityName.this

        builder.setTitle("Upload ảnh từ");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //thu vien
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 0);
                } else {
                    //camera
                    String _path = getNewImageName();
                    File file = new File(_path);
                    outputFileUri = Uri.fromFile(file);

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    startActivityForResult(intent, 1);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialogObject = builder.create();

        ListView listView = alertDialogObject.getListView();
        listView.setDivider(new ColorDrawable(Color.LTGRAY)); // set color
        listView.setDividerHeight(4); // set height

        alertDialogObject.show();
    }

    private Uri outputFileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    imageview.setImageURI(selectedImage);
                }
                break;

            case 1:
                if (resultCode == RESULT_OK) {
                    imageview.setImageURI(outputFileUri);
                }
                break;
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

}
