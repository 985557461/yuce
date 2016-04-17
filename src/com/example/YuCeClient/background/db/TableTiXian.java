package com.example.YuCeClient.background.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.mine.ti_xian.TiXianModel;
import com.example.YuCeClient.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-20.
 */
public class TableTiXian {
	public static String TableName = "account_tixian";
	public static String kColId = "clo_id";
	public static String kaccountId = "account_id";
	public static String ktixiantype = "tixiantype";
	public static String kcardnum = "cardnum";
	public static String kcardaddress = "cardaddress";
	public static String krealname = "realname";
	private static final String[] COLUMNS_INFO = new String[] { ktixiantype, kcardnum, kcardaddress, krealname };

	public static void create(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + TableName +
				"(" + kColId + " integer primary key autoincrement," + kaccountId + " text not null,"
				+ ktixiantype + " text not null," + kcardnum + " text not null,"
				+ kcardaddress + " text not null," + krealname + " text not null);");
	}

	public static void drop(SQLiteDatabase db) {
		db.execSQL("drop table " + TableName);
	}

	public static boolean addPayItem(String tixiantype, String cardnum, String cardaddress, String realname) {
		SQLiteDatabase db = HCApplicaton.getInstance().getDB();
		if (db == null) {
			return false;
		}
		Account account = HCApplicaton.getInstance().getAccount();
		if(TextUtils.isEmpty(account.userId)){
			ToastUtil.makeShortText("请登录");
			return false;
		}
		StringBuffer sb = new StringBuffer();
		ContentValues values = new ContentValues();
		values.put(kaccountId,account.userId);
		values.put(ktixiantype, tixiantype);
		values.put(kcardnum, cardnum);
		values.put(kcardaddress, cardaddress);
		values.put(krealname, realname);
		sb.setLength(0);
		sb.append(kaccountId).append("=?");
		sb.append(" and ");
		sb.append(ktixiantype).append("=?");
		sb.append(" and ");
		sb.append(kcardnum).append("=?");
		String[] args = new String[] { account.userId,tixiantype, cardnum };
		Cursor cursor = db.query(TableName, COLUMNS_INFO, sb.toString(), args, null, null, null);
		if (cursor != null && cursor.getCount() != 0) {
			db.update(TableName, values, sb.toString(), args);
			if (!cursor.isClosed()) {
				cursor.close();
			}
			return false;
		} else {
			db.insert(TableName, kColId, values);
			return true;
		}
	}

	public static List<TiXianModel> getTiXianModels(String accountId) {
		SQLiteDatabase db = HCApplicaton.getInstance().getDB();
		List<TiXianModel> tiXianModels = new ArrayList<TiXianModel>();
		if (db == null) {
			return tiXianModels;
		}
		String selection = kaccountId + "= '" + accountId + "'";
		Cursor cursor = db.query(TableName, COLUMNS_INFO, selection, null, null, null, null);
		if (cursor != null && cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				String tixiantype = cursor.getString(cursor.getColumnIndex(ktixiantype));
				String cardnum = cursor.getString(cursor.getColumnIndex(kcardnum));
				String cardaddress = cursor.getString(cursor.getColumnIndex(kcardaddress));
				String realname = cursor.getString(cursor.getColumnIndex(krealname));

				TiXianModel tiXianModel = new TiXianModel();
				tiXianModel.tixiantype = tixiantype;
				tiXianModel.cardnum = cardnum;
				tiXianModel.cardaddress = cardaddress;
				tiXianModel.realname = realname;

				tiXianModels.add(tiXianModel);
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return tiXianModels;
	}
}
