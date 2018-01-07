package com.example.sendmsg;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Person> mlist;
	private Context mContext;
	
	public MyAdapter(Context context, List<Person> list) {
		mInflater = LayoutInflater.from(context);
		mlist = list;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.phoneNumber = (TextView) convertView.findViewById(R.id.tv_phone);
			holder.platform = (TextView) convertView.findViewById(R.id.tv_platform);
			holder.send = (TextView) convertView.findViewById(R.id.tv_send_result);
			holder.status = (TextView) convertView.findViewById(R.id.tv_repayment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mlist.size() == 0) {
			Toast.makeText(mContext, "今日无催款用户", Toast.LENGTH_LONG).show();
		} else {
			Person person = mlist.get(position);
			holder.name.setText(person.getName());
			holder.platform.setText(person.getPlatform());
			holder.money.setText(person.getRepaymoney());
			holder.phoneNumber.setText(person.getPhoneNumber());
			holder.send.setText(person.getSend() + "");
			holder.status.setText(person.getStatus());
		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView name;
		public TextView platform;
		public TextView money;
		public TextView phoneNumber;
		public TextView send;
		public TextView status;
	}

}
