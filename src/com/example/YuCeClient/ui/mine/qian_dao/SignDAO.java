package com.example.YuCeClient.ui.mine.qian_dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SignDAO {
	//声明对象
	Context context;
	SQLiteDatabase db;
	DBHelper dbHelper;

	public SignDAO(Context context){
		this.context = context;
	}

	/**
	 * @param 打开数据库连接
	 */
	public boolean open(){
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		if(db == null){
			return false;
		}
		return true;
	}

	/**
	 * @param	关闭连接
	 */
	public void close(){
		dbHelper.close();
	}

	/**
	 * @param	插入信息
	 * @param uid
	 * @param name
	 * @param date
	 * @param ym
	 */
	public void insertSinInfo(String uid,String name,String date,String month){
		String sql="insert into sinTB(userid,usernmae,sindate,yearmonth,nowdate) values(?,?,?,?,?)";
		db.execSQL(sql,new Object[]{uid,name,date,month, System.currentTimeMillis()});
	}

	/**
	 * @param	查询信息
	 * @param uid
	 * @param date
	 * @param ym
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> findSinInfo(String uid,String date,String month){
		ArrayList<HashMap<String,Object>> alist = new ArrayList<HashMap<String,Object>>();
		alist.clear();
		HashMap<String, Object> rowMap;
		String sql;
		try{
			if("0".equals(month))
			{
				sql="select * from sinTB where userid='"+uid+"' and sindate='"+date+"'";
			}
			else
			{
				sql="select * from sinTB where userid='"+uid+"' and yearmonth='"+month+"'";
			}
			Cursor cur = db.rawQuery(sql, null);
			cur.moveToFirst();
			while(cur.moveToNext()){
				rowMap = new HashMap<String, Object>();
				rowMap.put("sin_id", cur.getInt(cur.getColumnIndex("sin_id")));
				rowMap.put("userid", cur.getString(cur.getColumnIndex("userid")));
				rowMap.put("usernmae", cur.getString(cur.getColumnIndex("usernmae")));
				rowMap.put("sindate", cur.getString(cur.getColumnIndex("sindate")));
				long aa = cur.getLong(cur.getColumnIndex("nowdate"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date(aa);
				String date1 = format.format(now);
				rowMap.put("nowdate", date1);
				Log.e("", cur.getString(cur.getColumnIndex("sindate")));
				alist.add(rowMap);
			}
			return alist;
		}catch(Exception e){
			return alist;
		}
	}
}
