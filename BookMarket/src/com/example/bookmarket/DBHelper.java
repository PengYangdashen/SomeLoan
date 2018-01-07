package com.example.bookmarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private static String DB_NAME = "Mine.db";
	private static int version = 1;
	private Context mContext;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_book = "create table BookList (" +
				"id integer primary key autoincrement, " +
				"author text, " +
				"bookname text, " +
				"cover integer" +
				"price real)";
		db.execSQL(create_book);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
