package com.fruit.dayloan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.fruit.dayloan.CameraActivity;
import com.fruit.dayloan.R;
import com.fruit.dayloan.pojo.MoneyPojo;
import com.fruit.dayloan.pojo.PersonPojo;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Out_MoneyAdapter extends BaseAdapter {

	private ArrayList<MoneyPojo> pojo;
	private LayoutInflater mInflater;
	private Context context;
	public Out_MoneyAdapter(Context context, ArrayList<MoneyPojo> pojo) {
		this.pojo = pojo;
		this.context = context;
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
			convertView = mInflater.inflate(R.layout.adapter_out_money, null);
			viewHolder.borrow_money = (TextView) convertView.findViewById(R.id.borrow_money);
			viewHolder.borrow_time = (TextView) convertView
					.findViewById(R.id.borrow_time);
			viewHolder.borrow_type=(TextView) convertView.findViewById(R.id.borrow_type);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHoler) convertView.getTag();
		}
		final MoneyPojo data = pojo.get(postition);
		viewHolder.borrow_time.setText(data.getTime());
		viewHolder.borrow_money.setText(data.getName()+"："+data.getMoney());
		
		
		viewHolder.borrow_type.setText(data.getType());
		viewHolder.borrow_type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (data.getType().equals("上传视频")) {
					Intent intentv = new Intent(context, CameraActivity.class);
					intentv.putExtra("jkid", data.getId()+"");
					context.startActivity(intentv);
				}				
			}
		});
		return convertView;
	}

	static class ViewHoler {
		public TextView borrow_money,borrow_type;
		public TextView borrow_time;
	}

}
