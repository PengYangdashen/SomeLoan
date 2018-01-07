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
			platform = "�������J";
		} else {
			platform = "�����J";
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
		
		tvSendTo.setText("���͵���" + name);
		tvtemplate1.setText("ģ��1��    ��γ�˵�����Ҫ��Ǯȥ���ѣ����Ѳ�Ҫ˵����������ǷǮһ��ƴ����Ǯ������������Ҫ��绰����֪����û���������������������װ�������Զ��Ҫ�������˶�������Σ������ڱ�����Ŀ�еĴ��ڵļ�ֵ������·�ܳ����Լ����·��խ�ˣ����������ˣ�ʲô�����еģ���Զ��Ҫ͸֧��ߵ��˶�������Σ�ʧȥ����=�����Ʋ���" + platform + "��Ǯ�û��ˣ��������Ŷ�����ʲô�ô��أ����綼Ҫ���ģ�ÿ�컹Ҫ����ţ���ͼʲô�����������׵��˴���û��Ҫ���鷳��������");
//		tvtemplate2
//				.setText("ģ��2��    " + name
//						+ "���������Ͻ���һ��"
//						+ allMoney
//						+ "Ԫ�Ľ��������������޷�������Я��ȫ��Ů��Ϊ��λ��磬���壬��ү���ṩ�����ʵķ��񣬷���ڻ�ػ˫�ɣ�������ȫ����֤��ˬ��ҪǮ���۸���������º󸶿Ҳ�����ŷ������ھ��������е绰����ȫ��ɧ���ߵ绰"
//						+ mobile);
//		tvtemplate3
//				.setText("ģ��3��    " + name
//						+ "�������������Ǯ����2��ǰ�ٲ������Ҿʹ�绰����ͨѶ¼�����������裬Ȼ��˵���Ǯ�����ǼҰ�����˵���㻹����������ȫ��Ů��ȥ������㻹��");
		tvtemplate4.setText("ģ��4(�ߴ���ϵ)��    " + name + "��������" + platform + "���������������Ǯ"
				+ person.getRepaymoney() + "Ԫ������" + person.getOverdue() + "�죬ǷǮ"
				+ allMoney + "Ԫ���������컹Ǯ����������ϵ��");
//		tvtemplate5.setText("ģ��5��    " + name + "�������������J�ģ�����������ӵ绰������Ϣ���ܾ���ͨ����ֻ�ܰ���������ύ�����չ�˾�ˣ��ͼ��ٿ�Ǯ���£���ʱ����ʲô���������������Ը���лл��");
//		tvtemplate6.setText("ģ��6��    �����������Ȳ�ת�����չ�˾�ˣ�ϣ��������������ϵ�ң�˵��һ�������ʲôʱ����Ի������СǮ�����Ƕ��ǽ�������ˡ�");
//		tvtemplate7.setText("ģ��7��    " + name + ",��ã�������" + platform+ "�ģ���������ƽ̨���С���Ѿ�����" + person.getOverdue() + "�ˣ���ȻǮ�ǲ��࣬����������ÿ�첻�ӵ绰���ض���Ҳ���ǰ취�ɣ��������и�˵���ɣ���ʲô��������һ��Э�̽�����ͺ������ӱ�������");
//		tvtemplate8.setText("ģ��8(��������)��    " +  name + "���������������Ǯ" + person.getRepaymoney() + "��ǷǮ" + allMoney + "������" + person.getOverdue() + "��һֱ�ܾ���ͨ��ϵ���յ�������ص绰���ţ���Ȼ���������˴����ˣ���" + platform + "��");
		tvtemplate9.setText("ģ��9��    " + name + "����ú�˵�㲻���ǰɣ� һֱ�绰������Ϣ���أ�������������������������ѣ�ȫ��Ů�������ײĵ��������͹����ֶ񶾵Ļ����������Ҫ���ְ취�Ż��������Ҳ��Ž���������Ը�� ��ô������ӱ�����˼�� ǷǮ��Ҫ���ģ����Ƕ㲻���ģ����֪ͨ����������֮ǰ��");
		tvtemplate10.setText("ģ��10��    ���ͨ�棺��������6��ǰ������Ƿ��ٸ���������Ը������ظ�Ĭ������Ը��е�һ�к������������Ⱥ��ͨѶ¼����ĸ�ֻ�����ĸ��λͬ���ֻ�����������ֻ���һ��������ʧ�Ը���");
		tvtemplate11.setText("ģ��11��    " + name + "��������" + platform + "���������������Ǯ" + person.getRepaymoney() + "Ԫ������" + person.getOverdue() + "�죬ǷǮ" + allMoney + "Ԫ���������컹Ǯ����������ϵ��������ϵ����������Ϣ���⣡���˶���3������Ч����" + platform + "��");
		tvtemplate12.setText("ģ��12��    �����" + platform + "�����Ͼ�Ҫ�����ˣ�����������Ǳ��Ÿ��ͻ����˵�̬�����ģ��������Ĳ�����û����º���֢����Ҳ֪������" + platform + "һֱ��������������û����ͨѶ¼�����������" + person.getOverdue() + "�죬��Ҫ���Լ���ծ�񼰸�����Ϣ������ת��תȥ��������ˣ��������꣬���ڷѿ���������������ϵ��");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_template1:
			String template1 = "��γ�˵�����Ҫ��Ǯȥ���ѣ����Ѳ�Ҫ˵����������ǷǮһ��ƴ����Ǯ������������Ҫ��绰����֪����û���������������������װ�������Զ��Ҫ�������˶�������Σ������ڱ�����Ŀ�еĴ��ڵļ�ֵ������·�ܳ����Լ����·��խ�ˣ����������ˣ�ʲô�����еģ���Զ��Ҫ͸֧��ߵ��˶�������Σ�ʧȥ����=�����Ʋ���" + platform + "��Ǯ�û��ˣ��������Ŷ�����ʲô�ô��أ����綼Ҫ���ģ�ÿ�컹Ҫ����ţ���ͼʲô�����������׵��˴���û��Ҫ���鷳��������";
			sendSMS(person.getMobilePhone(), template1);
			break;

//		case R.id.tv_template2:
//			String template2 = name
//					+ "���������Ͻ���һ��"
//					+ allMoney
//					+ "Ԫ�Ľ��������������޷�������Я��ȫ��Ů��Ϊ��λ��磬���壬��ү���ṩ�����ʵķ��񣬷���ڻ�ػ˫�ɣ�������ȫ����֤��ˬ��ҪǮ���۸���������º󸶿Ҳ�����ŷ������ھ��������е绰����ȫ��ɧ���ߵ绰"
//					+ mobile;
//			sendSMS(person.getMobilePhone(), template2);
//			break;
//
//		case R.id.tv_template3:
//			String template3 = name
//					+ "�������������Ǯ����2��ǰ�ٲ������Ҿʹ�绰����ͨѶ¼�����������裬Ȼ��˵���Ǯ�����ǼҰ�����˵���㻹����������ȫ��Ů��ȥ������㻹��";
//			sendSMS(person.getMobilePhone(), template3);
//			break;

		case R.id.tv_template4:

			String template4 = name + "��������" + platform + "���������������Ǯ"
					+ person.getRepaymoney() + "Ԫ������" + person.getOverdue()
					+ "�죬ǷǮ" + allMoney + "Ԫ���������컹Ǯ����������ϵ��";
			sendSMS(person.getMobilePhone(), template4);
			break;
			
//		case R.id.tv_template5:
//			
//			String template5 = name + "�������������J�ģ�����������ӵ绰������Ϣ���ܾ���ͨ����ֻ�ܰ���������ύ�����չ�˾�ˣ��ͼ��ٿ�Ǯ���£���ʱ����ʲô���������������Ը���лл��";
//			sendSMS(person.getMobilePhone(), template5);
//			break;
//			
//		case R.id.tv_template6:
//			
//			String template6 = "�����������Ȳ�ת�����չ�˾�ˣ�ϣ��������������ϵ�ң�˵��һ�������ʲôʱ����Ի������СǮ�����Ƕ��ǽ�������ˡ�";
//			sendSMS(person.getMobilePhone(), template6);
//			break;
//			
//		case R.id.tv_template7:
//			String template7 = name + ",��ã�������" + platform+ "�ģ���������ƽ̨���С���Ѿ�����" + person.getOverdue() + "���ˣ���ȻǮ�ǲ��࣬����������ÿ�첻�ӵ绰���ض���Ҳ���ǰ취�ɣ��������и�˵���ɣ���ʲô��������һ��Э�̽�����ͺ������ӱ�������";
//			sendSMS(person.getMobilePhone(), template7);
//			break;
//			
//		case R.id.tv_template8:
//			String template8 = name + "���������������Ǯ" + person.getRepaymoney() + "��ǷǮ" + allMoney + "������" + person.getOverdue() + "��һֱ�ܾ���ͨ��ϵ���յ�������ص绰���ţ���Ȼ���������˴����ˣ�\n��" + platform + "��";
//			sendSMS(person.getMobilePhone(), template8);
//			break;
		case R.id.tv_template9:
			String template9 = name + "����ú�˵�㲻���ǰɣ� һֱ�绰������Ϣ���أ�������������������������ѣ�ȫ��Ů�������ײĵ��������͹����ֶ񶾵Ļ����������Ҫ���ְ취�Ż��������Ҳ��Ž���������Ը�� ��ô������ӱ�����˼�� ǷǮ��Ҫ���ģ����Ƕ㲻���ģ����֪ͨ����������֮ǰ��";
			sendSMS(person.getMobilePhone(), template9);
			break;
		case R.id.tv_template10:
			String template10 = "���ͨ�棺��������6��ǰ������Ƿ��ٸ���������Ը������ظ�Ĭ������Ը��е�һ�к������������Ⱥ��ͨѶ¼����ĸ�ֻ�����ĸ��λͬ���ֻ�����������ֻ���һ��������ʧ�Ը���";
			sendSMS(person.getMobilePhone(), template10);
			break;
		case R.id.tv_template11:
			String template11 = name + "��������" + platform + "���������������Ǯ" + person.getRepaymoney() + "Ԫ������" + person.getOverdue() + "�죬ǷǮ" + allMoney + "Ԫ���������컹Ǯ����������ϵ��������ϵ����������Ϣ���⣡���˶���3������Ч��\n��" + platform + "��";
			sendSMS(person.getMobilePhone(), template11);
			break;
		case R.id.tv_template12:
			String template12 = "�����" + platform + "�����Ͼ�Ҫ�����ˣ�����Ҳ��ʣ�������ļ�������������������Ǳ��Ÿ��ͻ����˵�̬�����ģ��������Ĳ�����û����º���֢����Ҳ֪������" + platform + "һֱ��������������û����ͨѶ¼�����������" + person.getOverdue() + "�죬��Ҫ���Լ���ծ�񼰸�����Ϣ������ת��תȥ��������ˣ��������꣬���ڷѿ���������������ϵ��";
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
