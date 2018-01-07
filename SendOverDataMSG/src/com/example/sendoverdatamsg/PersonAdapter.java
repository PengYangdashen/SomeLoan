package com.example.sendoverdatamsg;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PersonAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Person> mlist;
	private Context mContext;
	
	public PersonAdapter(Context context, List<Person> list) {
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
			convertView = mInflater.inflate(R.layout.item_person, null);
			holder.allMoney = (TextView) convertView.findViewById(R.id.tv_allmoney);
			holder.overdue = (TextView) convertView.findViewById(R.id.tv_overdue);
			holder.interest = (TextView) convertView.findViewById(R.id.tv_interest);
			holder.phoneNumber = (TextView) convertView.findViewById(R.id.tv_phone);
			holder.money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.platform = (TextView) convertView.findViewById(R.id.tv_platform);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mlist.size() == 0) {
			Toast.makeText(mContext, "无催款用户", Toast.LENGTH_LONG).show();
		} else {
			Person person = mlist.get(position);
			holder.name.setText(person.getName());
			holder.platform.setText(person.getUserId());
			holder.money.setText(Double.toString(person.getRepaymoney()));
			holder.phoneNumber.setText(person.getMobilePhone());
			holder.overdue.setText(person.getOverdue() + "");
			double all = person.getInterest() + person.getRepaymoney();
			holder.allMoney.setText(Double.toString(all));
			holder.interest.setText(Double.toString(person.getInterest()));
		}
		return convertView;
	}
	
	class ViewHolder {
		public TextView name;
		public TextView platform;
		public TextView money;
		public TextView phoneNumber;
		public TextView interest;
		public TextView overdue;
		public TextView allMoney;
	}

}
