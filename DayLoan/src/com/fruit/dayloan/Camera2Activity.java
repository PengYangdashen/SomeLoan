package com.fruit.dayloan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.shandai.view.MyProgressDialog;
import com.fruit.dayloan.R;
import com.fruit.dayloan.utils.AlertDialog;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;

public class Camera2Activity extends BaseActivity implements
		SurfaceHolder.Callback {

	private static final String TAG = "MainActivity";
	private SurfaceView mSurfaceview;
	private boolean mStartedFlg = false;// 是否正在录像
	private MediaRecorder mediarecorder;
	private SurfaceHolder mSurfaceHolder;
	private TextView time, content, cameratxt;
	private Camera camera;
	private String path;
	private int text = 10;
	private int cameraPosition = 0;// 0代表前置摄像头，1代表后置摄像头

	private android.os.Handler handler = new android.os.Handler() {
		@Override
		public void handleMessage(Message message) {
			// TODO Auto-generated method stub
			if (message.what == 1) {
				PutObjectRequest put = new PutObjectRequest("ttsd", name,
						path);

				// 文件元信息的设置是可选的
				 ObjectMetadata metadata = new ObjectMetadata();
				 metadata.setContentType("application/octet-stream"); // 设置content-type
				 try {
					metadata.setContentMD5(BinaryUtil.calculateBase64Md5(path));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 校验MD5
				 put.setMetadata(metadata);
				
				
				// 异步上传时可以设置进度回调
				put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
					@Override
					public void onProgress(PutObjectRequest request,
							long currentSize, long totalSize) {
						Log.d("PutObject", "currentSize: " + currentSize
								+ " totalSize: " + totalSize);
					}
				});

				OSSAsyncTask task = oss
						.asyncPutObject(
								put,
								new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
									@Override
									public void onSuccess(
											PutObjectRequest request,
											PutObjectResult result) {
										Log.d("PutObject", "UploadSuccess");
										// handler.sendEmptyMessage(0);
										HttpUtils.doGetAsyn(Config.VIDEO_CORD
												+ "&userid=" + userId
												+ "&videoUrl=" + name, handler,
												Config.CODE_VIDEO);
//										dialog.dismiss();
									}

									@Override
									public void onFailure(
											PutObjectRequest request,
											ClientException clientExcepion,
											ServiceException serviceException) {

										// 请求异常
										if (clientExcepion != null) {
											// 本地异常如网络异常等
											clientExcepion.printStackTrace();
										}
										if (serviceException != null) {
											// 服务异常
											Log.e("ErrorCode", serviceException
													.getErrorCode());
											Log.e("RequestId", serviceException
													.getRequestId());
											Log.e("HostId", serviceException
													.getHostId());
											Log.e("RawMessage",
													serviceException
															.getRawMessage());
										}
									}
								});
			} else {
				if (message.what == Config.CODE_VIDEO) {
					// case Config.CODE_VIDEO:
					String result2 = message.obj.toString();
					try {
						JSONObject json = new JSONObject(result2);
						if (json.getInt("err") == 0) {
							// tv.setText("上传成功！");
							// pb.setVisibility(ProgressBar.GONE);
							startActivity(new Intent(Camera2Activity.this,
									IndexActivity.class));
							finish();
							dialog.dismiss();
						} else {
							// setDialog(json.getString("msg"));
							new AlertDialog(Camera2Activity.this)
									.builder()
									.setMsg(json.getString("msg"))
									.setNegativeButton("确定",
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													finish();
												}
											}).show();
						}

					} catch (JSONException e) {
						Toast.makeText(Camera2Activity.this, "数据解析错误",
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
					System.out.println("上传成功");

				}
			}
		}
	};

	private void stopRecoder() {
		handler.removeCallbacks(runnable);
		if (mediarecorder != null) {
			mediarecorder.stop();
			mediarecorder.release(); // Now the object cannot be reused
			mediarecorder = null;
			Log.d(TAG, "surfaceDestroyed release mRecorder");
		}
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			text--;
			time.setText("00:0" + text);
			handler.postDelayed(this, 1000);
			if (text == 0) {
				stopRecoder();
				handler.sendEmptyMessage(1);
				dialog = new MyProgressDialog(Camera2Activity.this, "");
				dialog.show();
			}
		}
	};
	private MyProgressDialog dialog;
	private SurfaceHolder holder;
	private OSSClient oss;
	private String name;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_camera2);

		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
		// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
				"LTAIZmB1IQUSkHvU", "WZFGiWk1ferhNhdC2ZiFm76L0xYqoP");
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		oss = new OSSClient(getApplicationContext(), endpoint,
				credentialProvider, conf);

		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");

		Intent get = getIntent();
		String names = get.getStringExtra("name");
		String money = get.getStringExtra("money");
		mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
		time = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content);
		content.setText("拍摄视频时请说出下列话：我叫" + names + "通过人人闪贷成功借款" + money);
		cameratxt = (TextView) findViewById(R.id.camera);
		holder = mSurfaceview.getHolder();
		holder.setFixedSize(800, 480);
		holder.addCallback(this);
		// setType必须设置，要不出错.
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		cameratxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				cameratxt.setVisibility(View.INVISIBLE);
				mediarecorder = new MediaRecorder();// 创建mediarecorder对象
				/**
				 * 设置竖着录制
				 */
				if (camera != null) {
					camera.release();
				}

				camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
				camera.setDisplayOrientation(90);
				// camera.setParameters(parameters);

				camera.unlock();

				mediarecorder.setCamera(camera);
				
				 // 这两项需要放在setOutputFormat之前
//                mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//                mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//                // Set output file format
//                mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//
//                // 这两项需要放在setOutputFormat之后
//                mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
//
//                mediarecorder.setVideoSize(640, 480);
//                mediarecorder.setVideoFrameRate(30);
//                mediarecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
//                mediarecorder.setOrientationHint(270);
//                //设置记录会话的最大持续时间（毫秒）
//                mediarecorder.setMaxDuration(30 * 1000);
//                mediarecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//				

				// 设置录制视频源为Camera(相机)
				
				mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
				mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
				mediarecorder
						.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				// 设置录制的视频编码h263 h264
				mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
				mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
				mediarecorder.setVideoSize(176, 144);
				// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//				mediarecorder.setVideoFrameRate(20);
				mediarecorder.setOrientationHint(270);
				mediarecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
				path = getSDPath();
				if (path != null) {
					File dir = new File(path + "/recordtest");
					if (!dir.exists()) {
						dir.mkdir();
					}
					name = userId + "=" + System.currentTimeMillis() + ".mp4";
					path = dir + "/" + name;
					System.out.println(name + path);
				}
				// 设置视频文件输出的路径
				mediarecorder.setOutputFile(path);
				try {
					// 准备录制
					mediarecorder.prepare();
					mediarecorder.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				handler.postDelayed(runnable, 1000);
			}
		});
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getDate() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR); // 获取年份
		int month = ca.get(Calendar.MONTH); // 获取月份
		int day = ca.get(Calendar.DATE); // 获取日
		int minute = ca.get(Calendar.MINUTE); // 分
		int hour = ca.get(Calendar.HOUR); // 小时
		int second = ca.get(Calendar.SECOND); // 秒

		String date = "" + year + (month + 1) + day + hour + minute + second;
		Log.d(TAG, "date:" + date);

		return date;
	}

	/**
	 * 获取SD path
	 * 
	 * @return
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		}

		return null;
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		mSurfaceHolder = surfaceHolder;
		if (!mStartedFlg) {
			camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
			camera.setDisplayOrientation(90);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1,
			int i2) {
		// 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
		mSurfaceHolder = surfaceHolder;
		try {
			camera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		stopRecoder();
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}
}
