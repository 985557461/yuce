package com.example.YuCeClient.ui.mine.qian_dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "sign.db", null, 1);
	}

	/**
	 * @param
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="create table sinTB(" +
				"sin_id integer primary key autoincrement," +
				"userid varchar(20)," +
				"usernmae varchar(20)," +
				"sindate varchar(20)," +
				"yearmonth varchar(20)," +
				"nowdate integer" +
				")";
		db.execSQL(sql);
	}

	/**
	 * @param
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
