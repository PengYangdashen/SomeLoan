package com.example.bookmarket;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

	private static final String authority = "com.example.BookMarket.MyContentProvider";
	private static UriMatcher uriMatcher;
	public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
	private DBHelper dbHelper;
    
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(authority, "book", BOOK_DIR);
		uriMatcher.addURI(authority, "book/#", BOOK_ITEM);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			c = db.query("booklist", projection, selection, selectionArgs, null, null, sortOrder);
			break;

		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			c = db.query("booklist", projection, "id=?", new String [] {bookId}, null, null, sortOrder);
			break;
		default:
			break;
		}
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			return "vnd.android.cursor.dir/vnd.com.example.BookMarket.MyContentProvider.book";

		case BOOK_ITEM:
			return "vnd.android.cursor.item/vnd.com.example.BookMarket.MyContentProvider.book";
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
