package com.example.YuCeClient.background.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xiaoyu on 15-12-20.
 */
public class DBHelper  extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context, String name) {
		super(context, name, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableTiXian.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
