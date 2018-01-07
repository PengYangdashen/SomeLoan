package com.sd.pallentloan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sd.pallentloan.R;
import com.sd.pallentloan.pojo.PersonForHome;
import com.sd.pallentloan.pojo.PersonPojo;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomeAdapterForHome extends BaseAdapter {

	private ArrayList<PersonForHome> pojo;
	private LayoutInflater mInflater;

	public HomeAdapterForHome(Context context, ArrayList<PersonForHome> pojo) {
		this.pojo = pojo;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int arg0) {
		return pojo.get(arg0 % pojo.size());
//		return pojo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
//		return arg0;
		if (pojo.size()==0) {
			return 0;
		}else {
			return arg0 % pojo.size();
		}
		
	}

	@Override
	public View getView(int postition, View convertView, ViewGroup arg2) {
		ViewHoler viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.adapter_home, null);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHoler) convertView.getTag();
		}
		if (pojo.size()==0) {
			
		}else {
			PersonForHome data = pojo.get(postition%pojo.size());
			viewHolder.time.setText(data.getTime());

			viewHolder.content.setText(
					Html.fromHtml(data.getName()+ "**  成功申请借款   "+"<font color=\"#2375FF\">"+"￥"+data.getMoney()+"</font>")	);
		}
		
		return convertView;
	}

	static class ViewHoler {
		public TextView time;
		public TextView content;
	}

}
