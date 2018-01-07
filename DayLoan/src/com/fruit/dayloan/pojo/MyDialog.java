package com.fruit.dayloan.pojo;


import com.fruit.dayloan.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog extends Dialog {

	  Context context;
	    public MyDialog(Context context) {
	        super(context);
	        // TODO Auto-generated constructor stub
	        this.context = context;
	    }
	    public MyDialog(Context context, int theme){
	        super(context, theme);
	        this.context = context;
	    }
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.pop_out_money_time);
	    }

}
