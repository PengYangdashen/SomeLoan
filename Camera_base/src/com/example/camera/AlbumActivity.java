package com.example.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class AlbumActivity extends Activity implements OnClickListener {

	private Button btnTakePhoto;
	private Button btnUpload;
	private RelativeLayout rlIDCard;
	private RelativeLayout rlCarCard;
	private RelativeLayout rlDriveCard;
	private RelativeLayout rlCountCard;
	private RelativeLayout rlHouseCard;
	private RelativeLayout rlBusynessCard;

	private List<String> filePath;
	private String[] document = { "身份证", "房产证", "汽车证", "驾驶证", "商业证", "会计证" };

	private OSSClient oss;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_albumlist);

		initview();

		filePath = new ArrayList<String>();
		
		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
		// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
				"LTAItbQRgOJVTQku", "IXmOG2XkNaIRBXpVe0St12F1Wb0S1j");
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		oss = new OSSClient(getApplicationContext(), endpoint,
				credentialProvider, conf);
	}

	private void initview() {
		btnTakePhoto = (Button) findViewById(R.id.btn_takephoto);
		btnTakePhoto.setOnClickListener(this);
		btnUpload = (Button) findViewById(R.id.btn_upload);
		btnUpload.setOnClickListener(this);
		rlIDCard = (RelativeLayout) findViewById(R.id.rl_IDcard);
		rlIDCard.setOnClickListener(this);
		rlCarCard = (RelativeLayout) findViewById(R.id.rl_carcard);
		rlCarCard.setOnClickListener(this);
		rlDriveCard = (RelativeLayout) findViewById(R.id.rl_drivecard);
		rlDriveCard.setOnClickListener(this);
		rlCountCard = (RelativeLayout) findViewById(R.id.rl_countcard);
		rlCountCard.setOnClickListener(this);
		rlHouseCard = (RelativeLayout) findViewById(R.id.rl_housecard);
		rlHouseCard.setOnClickListener(this);
		rlBusynessCard = (RelativeLayout) findViewById(R.id.rl_busynesscard);
		rlBusynessCard.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_IDcard:
			GotoNextPhotoActivity("身份证");
			break;

		case R.id.rl_carcard:
			GotoNextPhotoActivity("汽车证");
			break;

		case R.id.rl_drivecard:
			GotoNextPhotoActivity("驾驶证");
			break;

		case R.id.rl_countcard:
			GotoNextPhotoActivity("会计证");
			break;

		case R.id.rl_housecard:
			GotoNextPhotoActivity("房产证");
			break;

		case R.id.rl_busynesscard:
			GotoNextPhotoActivity("商业证");
			break;

		case R.id.btn_takephoto:
			Intent intent = new Intent(AlbumActivity.this,
					TakePhotoActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_upload:
			new Thread(new Runnable() {

				@Override
				public void run() {
					File sdcardDir = Environment.getExternalStorageDirectory();
					// 得到一个路径，内容是sdcard的文件夹路径和名字
					for (String name : document) {
						String path = sdcardDir.getPath() + "/" + name;
						Log.i(getClass().getSimpleName(), "currentpath:" + path);
						File dir = new File(path);
						if (dir.isDirectory()) {

							File[] fileArray = dir.listFiles();
							if (null != fileArray && 0 != fileArray.length) {
								for (int i = 0; i < fileArray.length; i++) {
									// fileArray[i].getName();
									Log.i(getClass().getSimpleName(),
											fileArray[i].getName());
									filePath.add(fileArray[i].getName());
									path = dir + "/" + fileArray[i].getName();
									Upload(name, path);
								}
							}
						}
					}

				}
			}).start();
			break;

		default:
			break;
		}
	}

	private void GotoNextPhotoActivity(String from) {
		Intent intent = new Intent(AlbumActivity.this, PhotoListActivity.class);
		intent.putExtra("From", from);
		startActivity(intent);
	}

	private void Upload(final String name, final String path) {

		PutObjectRequest put = new PutObjectRequest("zldzld", name, path);

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
			public void onProgress(PutObjectRequest request, long currentSize,
					long totalSize) {
				Log.d("PutObject", "currentSize: " + currentSize
						+ " totalSize: " + totalSize);
			}
		});

		OSSAsyncTask task = oss.asyncPutObject(put,
				new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
					@Override
					public void onSuccess(PutObjectRequest request,
							PutObjectResult result) {
						Log.d("PutObject", "UploadSuccess:" + path + "/" + name);
						// handler.sendEmptyMessage(0);
//						HttpUtils.doGetAsyn(Config.VIDEO_CORD + "&userid="
//								+ userId + "&videoUrl=" + name, handler,
//								Config.CODE_VIDEO);
						// dialog.dismiss();
					}

					@Override
					public void onFailure(PutObjectRequest request,
							ClientException clientExcepion,
							ServiceException serviceException) {

						// 请求异常
						if (clientExcepion != null) {
							// 本地异常如网络异常等
							clientExcepion.printStackTrace();
						}
						if (serviceException != null) {
							// 服务异常
							Log.e("ErrorCode", serviceException.getErrorCode());
							Log.e("RequestId", serviceException.getRequestId());
							Log.e("HostId", serviceException.getHostId());
							Log.e("RawMessage",
									serviceException.getRawMessage());
						}
					}
				});

	}

}
