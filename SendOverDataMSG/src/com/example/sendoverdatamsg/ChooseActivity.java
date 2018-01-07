package com.example.sendoverdatamsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChooseActivity extends Activity implements OnClickListener {

	private Person person;
	private TextView tvSendTo;
	private TextView tvtemplate1;
	private TextView tvtemplate2;
	private TextView tvtemplate3;
	private TextView tvtemplate4;
	private TextView tvtemplate5;
	private TextView tvtemplate6;
	private TextView tvtemplate7;
	private TextView tvtemplate8;
	private TextView tvtemplate9;
	private TextView tvtemplate10;
	private TextView tvtemplate11;
	private TextView tvtemplate12;
	private TextView tvCancle;
	private Context mContext;

	private String name;
	private String mobile;
	private String allMoney;
	private String platform;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
		person = (Person) getIntent().getSerializableExtra("person");
		name = person.getName();
		mobile = person.getMobilePhone();
		allMoney = Double.toString(person.getRepaymoney()
				+ person.getInterest());
		if (person.getUserId().substring(0, 3).equalsIgnoreCase("JBD")) {
			platform = "人人闪貸";
		} else {
			platform = "粒粒貸";
		}

		mContext = this;
		initview();
	}

	private void initview() {
		tvSendTo = (TextView) findViewById(R.id.tv_sendto);
		tvtemplate1 = (TextView) findViewById(R.id.tv_template1);
//		tvtemplate2 = (TextView) findViewById(R.id.tv_template2);
//		tvtemplate3 = (TextView) findViewById(R.id.tv_template3);
		tvtemplate4 = (TextView) findViewById(R.id.tv_template4);
//		tvtemplate5 = (TextView) findViewById(R.id.tv_template5);
//		tvtemplate6 = (TextView) findViewById(R.id.tv_template6);
//		tvtemplate7 = (TextView) findViewById(R.id.tv_template7);
//		tvtemplate8 = (TextView) findViewById(R.id.tv_template8);
		tvtemplate9 = (TextView) findViewById(R.id.tv_template9);
		tvtemplate10 = (TextView) findViewById(R.id.tv_template10);
		tvtemplate11 = (TextView) findViewById(R.id.tv_template11);
		tvtemplate12 = (TextView) findViewById(R.id.tv_template12);
		tvCancle = (TextView) findViewById(R.id.tv_cancle);

		tvtemplate1.setOnClickListener(this);
//		tvtemplate2.setOnClickListener(this);
//		tvtemplate3.setOnClickListener(this);
		tvtemplate4.setOnClickListener(this);
//		tvtemplate5.setOnClickListener(this);
//		tvtemplate6.setOnClickListener(this);
//		tvtemplate7.setOnClickListener(this);
//		tvtemplate8.setOnClickListener(this);
		tvtemplate9.setOnClickListener(this);
		tvtemplate10.setOnClickListener(this);
		tvtemplate11.setOnClickListener(this);
		tvtemplate12.setOnClickListener(this);
		tvCancle.setOnClickListener(this);
		
		tvSendTo.setText("发送到：" + name);
		tvtemplate1.setText("模板1：    李嘉诚说：再穷不要借钱去消费，再难不要说话不算数，欠钱一定拼命挣钱来还，还不上要打电话让人知道你没忘，堂堂正正做人明明白白做事永远不要丢掉别人对你的信任，是你在别人心目中的存在的价值，人生路很长，自己别把路走窄了，把人做好了，什么都会有的，永远不要透支身边的人对你的信任，失去诚信=终生破产，" + platform + "的钱该还了，老是拖着对你有什么好处呢？迟早都要还的，每天还要惦记着，你图什么？还给你最亲的人带来没必要的麻烦，至于吗？");
//		tvtemplate2
//				.setText("模板2：    " + name
//						+ "在粒粒贷上借了一笔"
//						+ allMoney
//						+ "元的借款，现因遇到困难无法偿还，携手全家女性为各位大哥，大叔，大爷，提供最优质的服务，服务口活，胸活，双飞，样样齐全，保证不爽不要钱，价格好商量，事后付款，也可上门服务，现在就拿起手中电话拨打全家骚热线电话"
//						+ mobile);
//		tvtemplate3
//				.setText("模板3：    " + name
//						+ "，给粒粒贷这点钱今天2点前再不处理，我就打电话给你通讯录挨个草他们妈，然后说你借钱给他们家办葬礼，说了你还不起让他们全家女性去卖身给你还！");
		tvtemplate4.setText("模板4(催促联系)：    " + name + "，这里是" + platform + "，您在我们这里借钱"
				+ person.getRepaymoney() + "元，逾期" + person.getOverdue() + "天，欠钱"
				+ allMoney + "元，请您尽快还钱或与我们联系！");
//		tvtemplate5.setText("模板5：    " + name + "，我这里粒粒貸的，今天继续不接电话不回信息，拒绝沟通，我只能把你的问题提交到催收公司了，就几百块钱的事，到时候发生什么不愉快的事情后果请自负，谢谢！");
//		tvtemplate6.setText("模板6：    您的资料我先不转到催收公司了，希望您明天主动联系我，说明一下情况，什么时候可以还上这笔小钱，我们都是讲道理的人。");
//		tvtemplate7.setText("模板7：    " + name + ",你好！我这里" + platform+ "的，你在我们平台这笔小贷已经逾期" + person.getOverdue() + "了，虽然钱是不多，但是你这样每天不接电话不回短信也不是办法吧？最起码有个说法吧？有什么困难我们一起协商解决不就好了吗？逃避有用吗？");
//		tvtemplate8.setText("模板8(恶意赖账)：    " +  name + "，您在我们这里借钱" + person.getRepaymoney() + "，欠钱" + allMoney + "，逾期" + person.getOverdue() + "天一直拒绝沟通联系，收到短信请回电话短信，不然当恶意赖账处理了！【" + platform + "】");
		tvtemplate9.setText("模板9：    " + name + "跟你好好说你不听是吧？ 一直电话不接信息不回，你如果想我辱骂你家人亲朋好友，全家女卖淫进棺材到地狱下油锅这种恶毒的话吗？如果你需要这种办法才会出来还款，我部门将会如你所愿， 这么大的人逃避有意思吗？ 欠钱是要还的，躲是躲不掉的，最后通知你明天中午之前！");
		tvtemplate10.setText("模板10：    最后通告：今天晚上6点前不处理欠款不再给脸！后果自负，不回复默认自行愿意承担一切后果！不处理者群发通讯录，父母手机，父母单位同事手机，亲朋好友手机，一切名誉损失自负！");
		tvtemplate11.setText("模板11：    " + name + "，这里是" + platform + "，您在我们这里借钱" + person.getRepaymoney() + "元，逾期" + person.getOverdue() + "天，欠钱" + allMoney + "元，请您尽快还钱或与我们联系，主动联系可以申请利息减免！（此短信3日内有效）【" + platform + "】");
		tvtemplate12.setText("模板12：    这边是" + platform + "，马上就要过年了，我们这个月是抱着给客户清账的态度来的，我们真心不想给用户留下后遗症，您也知道我们" + platform + "一直以来的做法，绝没动过通讯录就算今天逾期" + person.getOverdue() + "天，不要让自己的债务及个人信息在外面转来转去，早点清账，过个好年，逾期费可以商量！与我联系！");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_template1:
			String template1 = "李嘉诚说：再穷不要借钱去消费，再难不要说话不算数，欠钱一定拼命挣钱来还，还不上要打电话让人知道你没忘，堂堂正正做人明明白白做事永远不要丢掉别人对你的信任，是你在别人心目中的存在的价值，人生路很长，自己别把路走窄了，把人做好了，什么都会有的，永远不要透支身边的人对你的信任，失去诚信=终生破产，" + platform + "的钱该还了，老是拖着对你有什么好处呢？迟早都要还的，每天还要惦记着，你图什么？还给你最亲的人带来没必要的麻烦，至于吗？";
			sendSMS(person.getMobilePhone(), template1);
			break;

//		case R.id.tv_template2:
//			String template2 = name
//					+ "在粒粒贷上借了一笔"
//					+ allMoney
//					+ "元的借款，现因遇到困难无法偿还，携手全家女性为各位大哥，大叔，大爷，提供最优质的服务，服务口活，胸活，双飞，样样齐全，保证不爽不要钱，价格好商量，事后付款，也可上门服务，现在就拿起手中电话拨打全家骚热线电话"
//					+ mobile;
//			sendSMS(person.getMobilePhone(), template2);
//			break;
//
//		case R.id.tv_template3:
//			String template3 = name
//					+ "，给粒粒贷这点钱今天2点前再不处理，我就打电话给你通讯录挨个草他们妈，然后说你借钱给他们家办葬礼，说了你还不起让他们全家女性去卖身给你还！";
//			sendSMS(person.getMobilePhone(), template3);
//			break;

		case R.id.tv_template4:

			String template4 = name + "，这里是" + platform + "，您在我们这里借钱"
					+ person.getRepaymoney() + "元，逾期" + person.getOverdue()
					+ "天，欠钱" + allMoney + "元，请您尽快还钱或与我们联系！";
			sendSMS(person.getMobilePhone(), template4);
			break;
			
//		case R.id.tv_template5:
//			
//			String template5 = name + "，我这里粒粒貸的，今天继续不接电话不回信息，拒绝沟通，我只能把你的问题提交到催收公司了，就几百块钱的事，到时候发生什么不愉快的事情后果请自负，谢谢！";
//			sendSMS(person.getMobilePhone(), template5);
//			break;
//			
//		case R.id.tv_template6:
//			
//			String template6 = "您的资料我先不转到催收公司了，希望您明天主动联系我，说明一下情况，什么时候可以还上这笔小钱，我们都是讲道理的人。";
//			sendSMS(person.getMobilePhone(), template6);
//			break;
//			
//		case R.id.tv_template7:
//			String template7 = name + ",你好！我这里" + platform+ "的，你在我们平台这笔小贷已经逾期" + person.getOverdue() + "天了，虽然钱是不多，但是你这样每天不接电话不回短信也不是办法吧？最起码有个说法吧？有什么困难我们一起协商解决不就好了吗？逃避有用吗？";
//			sendSMS(person.getMobilePhone(), template7);
//			break;
//			
//		case R.id.tv_template8:
//			String template8 = name + "，您在我们这里借钱" + person.getRepaymoney() + "，欠钱" + allMoney + "，逾期" + person.getOverdue() + "天一直拒绝沟通联系，收到短信请回电话短信，不然当恶意赖账处理了！\n【" + platform + "】";
//			sendSMS(person.getMobilePhone(), template8);
//			break;
		case R.id.tv_template9:
			String template9 = name + "跟你好好说你不听是吧？ 一直电话不接信息不回，你如果想我辱骂你家人亲朋好友，全家女卖淫进棺材到地狱下油锅这种恶毒的话吗？如果你需要这种办法才会出来还款，我部门将会如你所愿， 这么大的人逃避有意思吗？ 欠钱是要还的，躲是躲不掉的，最后通知你明天中午之前！";
			sendSMS(person.getMobilePhone(), template9);
			break;
		case R.id.tv_template10:
			String template10 = "最后通告：今天晚上6点前不处理欠款不再给脸！后果自负，不回复默认自行愿意承担一切后果！不处理者群发通讯录，父母手机，父母单位同事手机，亲朋好友手机，一切名誉损失自负！";
			sendSMS(person.getMobilePhone(), template10);
			break;
		case R.id.tv_template11:
			String template11 = name + "，这里是" + platform + "，您在我们这里借钱" + person.getRepaymoney() + "元，逾期" + person.getOverdue() + "天，欠钱" + allMoney + "元，请您尽快还钱或与我们联系，主动联系可以申请利息减免！（此短信3日内有效）\n【" + platform + "】";
			sendSMS(person.getMobilePhone(), template11);
			break;
		case R.id.tv_template12:
			String template12 = "这边是" + platform + "，马上就要过年了，逾期也就剩下了最后的几百来单，我们这个月是抱着给客户清账的态度来的，我们真心不想给用户留下后遗症，您也知道我们" + platform + "一直以来的做法，绝没动过通讯录就算今天逾期" + person.getOverdue() + "天，不要让自己的债务及个人信息在外面转来转去，早点清账，过个好年，逾期费可以商量！与我联系！";
			sendSMS(person.getMobilePhone(), template12);
			break;
		case R.id.tv_cancle:
			finish();
			break;

		default:
			break;
		}
	}

	private void sendSMS(String mobilePhone, String smsBody) {

		Uri smsToUri = Uri.parse("smsto:" + mobilePhone);

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		mContext.startActivity(intent);

	}

}
