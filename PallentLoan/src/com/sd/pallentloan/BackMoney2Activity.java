package com.sd.pallentloan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.Formatdou;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.TimeUtils;
import com.sd.pallentloan.view.MyProgressDialog;
import com.yintong.pay.utils.BaseHelper;
import com.yintong.pay.utils.Constants;
import com.yintong.pay.utils.EnvConstants;
import com.yintong.pay.utils.MobileSecurePayer;
import com.yintong.pay.utils.PayOrder;
import com.yintong.pay.utils.Rsa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * 
 * 还款
 * 
 * @author Administrator
 * 
 */
public class BackMoney2Activity extends BaseActivity implements OnClickListener {

	private TextView get_money_refere, out_money_time, out_money_value,
			fuwuMoney, back_money_time, oldMoney;
	private Button confirm;

	private String name;// 用户名
	private String IdNo;// 证件号码
	private String bank_card;// 银行卡号
	private String MchNtCd = "";// 正版商户号
	private String UserId = "", jkId = "";// Id
	private String Amt = "500";// 金额
	private String MchntOrdId = "";// 商户订单号
	private String rephone;
	public static final String NOTIFY_URL = Config.URL
			+ "servlet/current/JBDcms3Action?function=GetLLPayRepResult";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_money);
		initView();
	}

	private void initView() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		UserId = sp.getString("userid", "");
		jkId = getIntent().getStringExtra("jkid");
		MchntOrdId = "lld" + System.currentTimeMillis();
		rephone = sp.getString("phone", "");
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("还款");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		out_money_value = (TextView) findViewById(R.id.out_money_value);
		out_money_time = (TextView) findViewById(R.id.out_money_time);
		fuwuMoney = (TextView) findViewById(R.id.fuwuMoney);
		get_money_refere = (TextView) findViewById(R.id.get_money_refere);
		back_money_time = (TextView) findViewById(R.id.back_money_time);
		oldMoney = (TextView) findViewById(R.id.oldMoney);
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		days = (TextView) findViewById(R.id.day);

		HttpUtils.doGetAsyn(Config.BACKMONEYINIT_CORD + "&jkid=" + jkId,
				mHandler, Config.CODE_BACKMONEYINIT);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			switch (msg.what) {
			case Constants.RQF_PAY:
				JSONObject objContent = BaseHelper.string2JSON(result);
				String retCode = objContent.optString("ret_code");
				String retMsg = objContent.optString("ret_msg");
				String agreementno = objContent.optString("agreementno", "");// 银行卡绑定协议号
				String no_order = objContent.optString("no_order");
				Double money_order = objContent.optDouble("money_order");

				// 成功
				if (Constants.RET_CODE_SUCCESS.equals(retCode)) {// 交易成功
					// TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉

					SharedPreferences sp = getSharedPreferences("config",
							MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putInt("rzstatus", 1);
					editor.commit();
					startActivity(new Intent(BackMoney2Activity.this,
							IndexActivity.class));
					finish();
					Toast.makeText(BackMoney2Activity.this, "还款成功",
							Toast.LENGTH_SHORT).show();
				} else {
					showDialog(objContent.optString("ret_msg"));
					MchntOrdId = "lld" + System.currentTimeMillis();
				}
				// try {
				// HttpUtils.doGetAsyn(Config.BACKMONEYLL_CORD + "&jkid="
				// + getIntent().getStringExtra("jkid") + "&userid="
				// + UserId + "&code=" + retCode + "&message="
				// + URLEncoder.encode(retMsg, "UTF-8") + "&bankcard="
				// + bank_card + "&amt=" + money_order + "&payOrder="
				// + no_order + "&bind_no=" + agreementno, mHandler,
				// Config.CODE_BACKMONEY);
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(BackMoney2Activity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(BackMoney2Activity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_CONFIRMBACK:
				try {
					JSONObject jsonResult = new JSONObject(result);
					int err = jsonResult.getInt("code");
					if (err == 1) {
						pay();
					} else {
						showDialog(jsonResult.optString("msg"));
					}
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;
			case Config.CODE_BACKMONEY:
				// dialog.dismiss();
				// try {
				// JSONObject jsonObject2 = new JSONObject(result);
				// int err = jsonObject2.getInt("err");
				// if (err == 0) {
				// Toast.makeText(BackMoney2Activity.this, "还款成功",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent(BackMoney2Activity.this,
				// OutMoneyRecordActivity.class);
				// intent.putExtra("type", "bn");
				// startActivity(intent);
				// finish();
				// } else {
				// new AlertDialog(BackMoney2Activity.this).builder()
				// .setMsg(jsonObject2.getString("msg"))
				// .setNegativeButton("确定", new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// // finish();
				// }
				// }).show();
				// }
				// } catch (JSONException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }

				break;
			case Config.CODE_BACKMONEYINIT:
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject backtime = jsonObject.getJSONObject("fkdz_time");
					JSONObject outtime = jsonObject.getJSONObject("hkyq_time");
					out_money_time.setText(""
							+ TimeUtils.parseDate(Long.parseLong(outtime
									.getString("time"))));
					back_money_time.setText(""
							+ TimeUtils.parseDate(Long.parseLong(backtime
									.getString("time"))));
					java.text.DecimalFormat df = new java.text.DecimalFormat(
							"###0.00");

					double all = jsonObject.getDouble("sjsh_money");

					int day = jsonObject.getInt("yuq_ts");
					days.setText("逾期天数:" + day);
					double oldM = jsonObject.getDouble("yuq_lx");
					// int annualrate = jsonObject.getInt("annualrate");
					int date = jsonObject.getInt("jk_date");
					oldMoney.setText("逾期金额    ￥" + oldM);
					if (date == 1) {
						fuwuMoney.setText("服务费   ￥ "
								+ df.format(all * 0.27 * 0.5));
						get_money_refere.setText("利息    ￥"
								+ df.format(all * 0.03 * 0.5));
					} else {
						fuwuMoney.setText("服务费   ￥ " + df.format(all * 0.27));
						get_money_refere.setText("利息    ￥"
								+ df.format(all * 0.03));
					}
					name = jsonObject.getString("realname");
					IdNo = jsonObject.getString("idno");
					bank_card = jsonObject.getString("cardno");
					String bank = Formatdou.formatdou((all + oldM));
					Amt = bank + "";
//					 Amt="0.01";
					out_money_value.setText(bank + "");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};
	private MyProgressDialog dialog;
	private TextView days;
	private String timeString;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.confirm:

			HttpUtils.doGetAsyn(Config.CONFIRMBM_CORD + "&jkid=" + jkId
					+ "&userid=" + UserId, mHandler, Config.CODE_CONFIRMBACK);
			break;
		default:
			break;
		}
	}

	private void pay() {
		// dialog.dismiss();
		PayOrder order = null;
		// 卡前置方式
		if (TextUtils.isEmpty(bank_card)) {
			Toast.makeText(BackMoney2Activity.this, "银行卡为空", Toast.LENGTH_LONG)
					.show();
			return;
		}
		order = constructPreCardPayOrder();
		// order.setShareing_data(((TextView)findViewById(R.id.share_money)).getText().toString().trim());
		String content4Pay = BaseHelper.toJSONString(order);
		// 关键 content4Pay 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的技术支持
		Log.i(BackMoney2Activity.class.getSimpleName(), content4Pay);

		MobileSecurePayer msp = new MobileSecurePayer();
		boolean bRet = msp.payAuth(content4Pay, mHandler, Constants.RQF_PAY,
				BackMoney2Activity.this, false);

		Log.i(BackMoney2Activity.class.getSimpleName(), String.valueOf(bRet));
	}

	private PayOrder constructPreCardPayOrder() {

		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		timeString = dataFormat.format(date);

		PayOrder order = new PayOrder();
		order.setBusi_partner("101001");// 虚拟商品
		order.setNo_order(MchntOrdId);// 唯一订单号
		order.setDt_order(timeString);// 订单时间
		order.setName_goods("用户还款");
		order.setNotify_url(NOTIFY_URL + "&userid=" + UserId + "&jkid=" + jkId);
		// MD5 签名方式
		// order.setSign_type(PayOrder.SIGN_TYPE_MD5);
		// RSA 签名方式
		order.setSign_type(PayOrder.SIGN_TYPE_RSA);

		order.setValid_order("100");// 订单有效时间

		order.setUser_id(UserId);
		order.setId_no(IdNo);// 身份证

		order.setAcct_name(name);
		order.setMoney_order(Amt);

		// 银行卡卡号，该卡首次支付时必填
		order.setCard_no(bank_card);
		// 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
		// order.setNo_agree(((EditText) findViewById(R.id.agree_no)).getText()
		// .toString());

		// 风险控制参数
		order.setRisk_item(constructRiskItem());

		String sign = "";
		order.setOid_partner(EnvConstants.PARTNER);
		String content = BaseHelper.sortParam(order);
		// MD5 签名方式
		// sign = Md5Algorithm.getInstance().sign(content,
		// EnvConstants.MD5_KEY);

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
			mRiskItem.put("user_info_mercht_userno", UserId);
			mRiskItem.put("user_info_full_name", name);
			mRiskItem.put("user_info_identify_state", 1);
			mRiskItem.put("user_info_identify_type", 4);
			mRiskItem.put("user_info_id_no", IdNo);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mRiskItem.toString();
	}

	private void showDialog(String Message) {
		// TODO Auto-generated method stub
		new AlertDialog(BackMoney2Activity.this).builder().setMsg(Message)
				.setNegativeButton("确定", new OnClickListener() {
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
