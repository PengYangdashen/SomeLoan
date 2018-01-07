package com.fruit.dayloan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.fruit.dayloan.R;
import com.fruit.dayloan.pojo.MoneyPojo;
import com.fruit.dayloan.pojo.PersonPojo;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MoneyRecordAdapter extends BaseAdapter {

	private ArrayList<MoneyPojo> pojo;
	private LayoutInflater mInflater;

	public MoneyRecordAdapter(Context context, ArrayList<MoneyPojo> pojo) {
		this.pojo = pojo;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public ArrayList<MoneyPojo> getArrayList() {
		return pojo;
	}

	public void setArrayList(ArrayList<MoneyPojo> arrayList) {
		this.pojo = arrayList;
	}

	@Override
	public int getCount() {
		if (pojo == null) {
			return 0;
		} else {
			return pojo.size();
		}
	}

	@Override
	public Object getItem(int arg0) {

		return pojo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int postition, View convertView, ViewGroup arg2) {
		ViewHoler viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.adapter_money_time_item, null);
			viewHolder.money_name = (TextView) convertView.findViewById(R.id.money_name);
			viewHolder.money_time = (TextView) convertView
					.findViewById(R.id.money_time);
			viewHolder.money_type = (TextView) convertView.findViewById(R.id.money_type);
			viewHolder.get_money_value=(TextView) convertView.findViewById(R.id.get_money_value);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHoler) convertView.getTag();
		}
		MoneyPojo data = pojo.get(postition);
		viewHolder.money_time.setText(data.getTime());
		viewHolder.get_money_value.setText("￥"+data.getMoney());
		viewHolder.money_name.setText(data.getName());
		viewHolder.money_type.setText(data.getType());
		return convertView;
	}

	static class ViewHoler {
		public TextView money_name,get_money_value;
		public TextView money_time,money_type;
	}

}
