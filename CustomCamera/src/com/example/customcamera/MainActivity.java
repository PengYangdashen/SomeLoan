package com.example.customcamera;

import java.io.File;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    public static final int TAKE_PHOTO = 111;

    private Button mButton;
    private ImageView mImageView;

    //保存 照片的目录
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DukeKD";
    private File photo_file = new File(path);
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.takephoto);
        mImageView = (ImageView) findViewById(R.id.img);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
    }

    public void takePhoto() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (!photo_file.exists()) {
            photo_file.mkdirs();
        }
        photo_file = new File(path, "/temp.jpg");
        photoPath = path + "/temp.jpg";
        if (photo_file != null) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo_file));
            startActivityForResult(captureIntent, TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == TAKE_PHOTO) {
                //这中情况下 data是为null的，因为自定义了路径 所以通过这个路径来获取
                Bitmap smallBitmap = BitmapUtil.getSmallBitmap(photoPath);

                // ok 拿到图片的base64 上传
                String base64 = BitmapToBase64Util.bitmapToBase64(smallBitmap);
                mImageView.setImageBitmap(smallBitmap);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("photoPath", photoPath);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(photoPath)) {
            photoPath = savedInstanceState.getString("photoPath");
        }
        Log.d(TAG, "onRestoreInstanceState");
    }

}