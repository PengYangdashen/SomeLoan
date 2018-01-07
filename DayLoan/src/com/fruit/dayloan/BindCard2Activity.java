package com.fruit.dayloan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shandai.view.MyProgressDialog;
import com.fruit.dayloan.utils.AlertDialog;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;
import com.fruit.dayloan.utils.Validator;
import com.yintong.pay.utils.BaseHelper;
import com.yintong.pay.utils.Constants;
import com.yintong.pay.utils.EnvConstants;
import com.yintong.pay.utils.MobileSecurePayer;
import com.yintong.pay.utils.PayOrder;
import com.yintong.pay.utils.Rsa;

/**
 * 银行绑定支付
 * 
 * @author Administrator
 * 
 */
public class BindCard2Activity extends BaseActivity implements OnClickListener {

	private EditText name;// 用户名
	private EditText IdNo;// 证件号码
	private EditText bank_card;// 银行卡号
	private CheckBox check;
	private String UserId = "";// Id
	private String MchntOrdId = "";// 商户订单号
	private String Amt = "20";// 金额
	public static final String NOTIFY_URL=Config.URL+"servlet/current/JBDcms3Action?function=GetLLPayResult";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_card);
		initView();
	}

	private void initView() {
		dialog = new MyProgressDialog(this, "");

		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		UserId = sp.getString("userid", "");
		MchntOrdId = "ttsd" + System.currentTimeMillis();
		rephone = sp.getString("phone", "");
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("添加银行卡");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		name = (EditText) findViewById(R.id.name);
		IdNo = (EditText) findViewById(R.id.id_card);
		bank_card = (EditText) findViewById(R.id.bank_card);
		check = (CheckBox) findViewById(R.id.check);
		bank_next_btn = (Button) findViewById(R.id.bank_next_btn);
		bank_next_btn.setOnClickListener(this);
		findViewById(R.id.bank_support).setOnClickListener(this);

		warm = (TextView) findViewById(R.id.warm);
		warm.setOnClickListener(this);

		warm.setText(Html.fromHtml("温馨提示：保持良好还款记录，可申请续借并提高额度。申请借款需缴纳认证费用20元 "
				+ "<font color=\"#F9872F\">" + "?" + "</font>"));
		findViewById(R.id.fuwu).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.fuwu:
			Intent intent = new Intent(BindCard2Activity.this,
					ProblemActivity.class);
			intent.putExtra("title", "银行卡服务协议");
			intent.putExtra("url", Config.BANKCARDPROTOL_CODE);
			startActivity(intent);
			break;
		case R.id.warm:
			showDialog("综合服务费/银行卡认证=20元/笔.包含但不限于第三方征信查询.手机运营商认证." +
					"电商查询.芝麻信用等.一经认证.相关费用就已产生.无论是否成功借款该费用不予退还.按时还款可提升额度！");
			break;
		case R.id.bank_next_btn:
			dialog.show();
			nameStr = name.getText().toString();
			cardStr = IdNo.getText().toString();
			bank_cardStr = bank_card.getText().toString();
			if (TextUtils.isEmpty(nameStr)) {
				showDialog("请先输入真实姓名");
				dialog.dismiss();
				return;
			}
			if (TextUtils.isEmpty(cardStr)) {
				showDialog("请先输入身份证号");
				dialog.dismiss();
				return;
			}
			if (!Validator.isIDCard(cardStr)) {
				showDialog("请先输入正确的身份证号");
				dialog.dismiss();
				return;
			}
			if (TextUtils.isEmpty(bank_cardStr)) {
				showDialog("请先输入银行卡号");
				dialog.dismiss();
				return;
			}
			if (!check.isChecked()) {
				showDialog("请勾上服务协议");
				dialog.dismiss();
				return;
			}
//			ad = new AlertDialog(BindCard2Activity.this);
//			ad.builder();
//			ad.setMsg("综合服务费/银行卡认证=20元/笔.包含但不限于第三方征信查询.手机运营商认证.电商查询.芝麻信用." +
//					"一经认证.相关费用就已产生.所以不管您是否成功借款该费用不予退还.请慎重选择！");
//			ad.setNegativeButton("取消", new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//			ad.setPositiveButton2("去支付", new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
					HttpUtils.doGetAsyn(Config.HOME_INIT + "&userid=" + UserId,
							mHandler, Config.CODE_BANK);
//				}
//			});
//			ad.show();
//			
			

			break;
		case R.id.bank_support:
			showDialog("中国工商银行，中国农业银行，中国银行，中国建设银行，"
					+ "中国邮政储蓄银行，平安银行，中国民生银行，中国光大银行，"
					+ "广发银行，中信银行，兴业银行，华夏银行，交通银行，招商银行，上海浦东发展银行");
			break;
		default:
			break;
		}
	}

	private void pay() {
		dialog.dismiss();
		PayOrder order = null;
		// 卡前置方式
		if (TextUtils.isEmpty(bank_cardStr) ) {
			Toast.makeText(BindCard2Activity.this, "银行卡不能为空",
					Toast.LENGTH_LONG).show();
			return;
		}
		order = constructPreCardPayOrder();
		// order.setShareing_data(((TextView)findViewById(R.id.share_money)).getText().toString().trim());
		String content4Pay = BaseHelper.toJSONString(order);
		// 关键 content4Pay 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的技术支持
		Log.i(BindCard2Activity.class.getSimpleName(), content4Pay);

		MobileSecurePayer msp = new MobileSecurePayer();
		boolean bRet = msp.payAuth(content4Pay, mHandler, Constants.RQF_PAY,
				BindCard2Activity.this, false);

		Log.i(BindCard2Activity.class.getSimpleName(), String.valueOf(bRet));
	}

	
	private PayOrder constructPreCardPayOrder() {

        SimpleDateFormat dataFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        Date date = new Date();
        timeString = dataFormat.format(date);

        PayOrder order = new PayOrder();
        order.setBusi_partner("101001");//虚拟商品
        order.setNo_order(MchntOrdId);//唯一订单号
        order.setDt_order(timeString);//订单时间
        order.setName_goods("用户认证");
        order.setNotify_url(NOTIFY_URL);
        // MD5 签名方式
//        order.setSign_type(PayOrder.SIGN_TYPE_MD5);
        // RSA 签名方式
         order.setSign_type(PayOrder.SIGN_TYPE_RSA);

        order.setValid_order("100");//订单有效时间

        order.setUser_id(UserId);
        order.setId_no(cardStr);//身份证

        order.setAcct_name(nameStr);
        order.setMoney_order(Amt);

        // 银行卡卡号，该卡首次支付时必填
        order.setCard_no(bank_cardStr);
        // 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
//        order.setNo_agree(((EditText) findViewById(R.id.agree_no)).getText()
//                .toString());
        
        // 风险控制参数
        order.setRisk_item(constructRiskItem());

        String sign = "";
        order.setOid_partner(EnvConstants.PARTNER);
        String content = BaseHelper.sortParam(order);
        // MD5 签名方式
//        sign = Md5Algorithm.getInstance().sign(content,
//                EnvConstants.MD5_KEY);
        
        // RSA 签名方式
        sign = Rsa.sign(content, EnvConstants.RSA_PRIVATE);
        order.setSign(sign);
        return order;
    }
	

	private String constructRiskItem() {
		JSONObject mRiskItem = new JSONObject();
		try {
			mRiskItem.put("user_info_bind_phone", rephone);
			mRiskItem.put("user_info_dt_register", timeString);
			mRiskItem.put("frms_ware_category", "2010");
//			mRiskItem.put("user_info_mercht_userno", UserId);
			mRiskItem.put("user_info_mercht_userno", UserId);
			mRiskItem.put("user_info_full_name", nameStr);
			mRiskItem.put("user_info_identify_state", 1);
			mRiskItem.put("user_info_identify_type", 4);
			mRiskItem.put("user_info_id_no", cardStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mRiskItem.toString();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			switch (msg.what) {
			case Constants.RQF_PAY:
				JSONObject objContent = BaseHelper.string2JSON(result);
				String retCode = objContent.optString("ret_code");
				String retMsg = objContent.optString("ret_msg");
				String agreementno = objContent.optString("agreementno", "");//银行卡绑定协议号
				String no_order = objContent.optString("no_order");
				Double money_order = objContent.optDouble("money_order");
				
				
				// 成功
				if (Constants.RET_CODE_SUCCESS.equals(retCode)) {//交易成功
					// TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
					
					new AlertDialog(BindCard2Activity.this).builder().setMsg("绑定成功")
					.setNegativeButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							SharedPreferences sp = getSharedPreferences(
									"config", MODE_PRIVATE);
							Editor editor = sp.edit();
							editor.putInt("rzstatus", 1);
							editor.commit();
							startActivity(new Intent(BindCard2Activity.this,
									IndexActivity.class));
							finish();
						}
					}).show();
				} else {
					showDialog(objContent.optString("ret_msg"));
					MchntOrdId = "ttsd" + System.currentTimeMillis();
				}
//			
				
//				String url;
//				try {
//					url = Config.PAY_RESULTLL_CORD + "&code=" + retCode + "&bankcard="
//							+ bank_cardStr + "&message=" + URLEncoder.encode(retMsg, "UTF-8")
//							+ "&amt=" + money_order+"&payOrder="+no_order+"&bind_no="+agreementno;
//					HttpUtils.doGetAsyn(url, mHandler, Config.CODE_PAY_RESULT);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(BindCard2Activity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(BindCard2Activity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;

			case Config.CODE_PAY_RESULT:
				// try {
				// JSONObject json = new JSONObject(result);
				// } catch (JSONException e) {
				// Toast.makeText(BindCardActivity.this, "数据解析错误",
				// Toast.LENGTH_SHORT).show();
				// e.printStackTrace();
				// }
				// finish();
				break;
			case Config.CODE_PAY_START:

				try {
					JSONObject jsonObject = new JSONObject(result);
					int err = jsonObject.getInt("err");
					if (err < 0) {
						showDialog(jsonObject.getString("msg"));
						dialog.dismiss();
					} else {
						pay();
						// bank_next_btn.setClickable(false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Config.CODE_BANK:
				
				// String result = msg.obj.toString();
				try {
					// {"mobilephone":"17620110768","username":"GSD22","phone":"17620***68","creditlimit":"5000",
					// "isyhbd":"0","isshenfen":"0","islianxi":"0",
					// "err":0,"isjob":"0","rzstatus":"0","usablecreditlimit":"500","lastdate":"2017-02-22 17:39:09.0"}
					JSONObject jsonObject = new JSONObject(result);

					int rzstatus = Integer.parseInt(jsonObject
							.getString("rzstatus"));
					if (rzstatus == 0) {
						try {
							String url = Config.PAY_LLSTART_CORD + "&userid=" + UserId
									+ "&bankCard=" + bank_cardStr + "&name="
									+ URLEncoder.encode(nameStr, "UTF-8")
									+ "&idNo=" + cardStr + "&payOrder="
									+ MchntOrdId;
							HttpUtils.doGetAsyn(url, mHandler,
									Config.CODE_PAY_START);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						new AlertDialog(BindCard2Activity.this).builder()
								.setMsg("您已经绑定银行卡！")
								.setNegativeButton("确定", new OnClickListener() {
									@Override
									public void onClick(View v) {
										startActivity(new Intent(
												BindCard2Activity.this,
												IndexActivity.class));
										finish();
									}
								}).show();
					}

					SharedPreferences sp = getSharedPreferences("config",
							MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putInt("rzstatus", rzstatus);
					editor.commit();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};
	private Button bank_next_btn;
	private TextView warm;
	private String nameStr;
	private String cardStr;
	private String bank_cardStr;
	private MyProgressDialog dialog;
	private String timeString;
	private String rephone;
	private AlertDialog ad;

	private void showDialog(String Message) {
		// TODO Auto-generated method stub
		new AlertDialog(BindCard2Activity.this).builder().setMsg(Message)
				.setPositiveButton3("同意", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}


	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

}
