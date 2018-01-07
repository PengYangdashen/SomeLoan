package com.example.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.LogUtil;

import com.example.camera.view.CameraPreview;
import com.example.camera.view.LoopView;
import com.example.camera.view.OnItemSelectedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TakePhotoActivity extends Activity {

	private String TAG = getClass().getSimpleName();
	private String destination = "汽车证";

	private Button btnTakePhoto;
	private LoopView lvOption;
	private List<String> items;

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	@SuppressWarnings("deprecation")
	private Camera mCamera;
	private CameraPreview mPreview;
	
	private File pictureDir;

	@SuppressWarnings("deprecation")
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			// pictureDir为SD主目录下picture文件夹
			// File pictureDir = Environment
			// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				for (int i = 0; i < items.size(); i++) {
					// 创建一个文件夹对象，赋值为外部存储器的目录
					File sdcardDir = Environment.getExternalStorageDirectory();
					// 得到一个路径，内容是sdcard的文件夹路径和名字
					String path = sdcardDir.getPath() + "/" + destination;
					pictureDir = new File(path);
					if (!pictureDir.exists()) {
						// 若不存在，创建目录，可以在应用启动的时候创建
						pictureDir.mkdirs();
					}
				}
			} else {
				finish();
			}
			
			if (pictureDir == null) {
				LogUtil.d(TAG,
						"Error creating media file, check storage permissions!");
				return;
			}

			final String picturePath = pictureDir
					+ File.separator
					+ new DateFormat().format("yyyyMMddHHmmss", new Date())
							.toString() + ".jpg";
			LogUtil.d(TAG, pictureDir.getAbsolutePath());
			new Thread(new Runnable() {
				@Override
				public void run() {
					File file = new File(picturePath);
					try {
						// 获取当前旋转角度, 并旋转图片
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						if (mCameraId == CameraInfo.CAMERA_FACING_BACK) {
							bitmap = rotateBitmapByDegree(bitmap, 90);
						} else {
							bitmap = rotateBitmapByDegree(bitmap, -90);
						}
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(file));
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
						bos.flush();
						bos.close();
						bitmap.recycle();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

			mCamera.startPreview();
		}
	};

	private static int mCameraId;

	private FrameLayout preview;

	// 切换前置和后置摄像头
	public void switchCamera() {
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(mCameraId, cameraInfo);
		if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
			mCameraId = CameraInfo.CAMERA_FACING_FRONT;
		} else {
			mCameraId = CameraInfo.CAMERA_FACING_BACK;
		}
		preview.removeView(mPreview);
		releaseCamera();
		openCamera();
		setCameraDisplayOrientation(TakePhotoActivity.this, mCameraId, mCamera);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_takephoto);

		lvOption = (LoopView) findViewById(R.id.lv_option);
		btnTakePhoto = (Button) findViewById(R.id.btn_takephoto);

		items = new ArrayList<String>();
		items.add("身份证");
		items.add("驾驶证");
		items.add("房产证");
		items.add("汽车证");
		items.add("商业证");
		
		lvOption.setItems(items);
		lvOption.setListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				LogUtil.d(TAG, "current item is " + items.get(index));
				destination = items.get(index);
			}
		});

		if (!checkCameraHardware(this)) {
			Toast.makeText(TakePhotoActivity.this, "相机不支持", Toast.LENGTH_SHORT)
					.show();
			finish();
		} else {

			btnTakePhoto.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					// mCamera.autoFocus(new AutoFocusCallback() {
					//
					// @Override
					// public void onAutoFocus(boolean success, Camera camera) {
					// if (success) {
					// mCamera.takePicture(null, null, mPicture);
					// }
					// }
					// });
					mCamera.takePicture(null, null, mPicture);
					LogUtil.d(TAG, "take photo --> OK");
				}
			});

			openCamera();
			setCameraDisplayOrientation(this, mCameraId, mCamera);

		}
	}

	/** A safe way to get an instance of the Camera object. */
	@SuppressWarnings("deprecation")
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(mCameraId); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	// 释放相机
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	// 开始预览相机
	public void openCamera() {
		if (mCamera == null) {
			// Create an instance of Camera
			mCamera = getCameraInstance();
			// get Camera parameters
			Camera.Parameters params = mCamera.getParameters();
			// set the focus mode
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			// set Camera parameters
			mCamera.setParameters(params);

			// Create our Preview view and set it as the content of our
			// activity.
			mPreview = new CameraPreview(this, mCamera);
			mPreview.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mCamera.autoFocus(null);
					return false;
				}
			});
			preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);
			mCamera.startPreview();
		}
	}

	/** Create a file Uri for saving an image or video */
	@SuppressWarnings("unused")
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				LogUtil.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	// 设置相机横竖屏
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;
		} else {
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	// 旋转图片
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	// 判断相机是否支持
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

}
