package com.example.YuCeClient.ui.mine.qian_dao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sreay on 15-12-16.
 */
public class ActivityQianDao extends ActivityBase implements View.OnClickListener {

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityQianDao.class);
		activity.startActivity(intent);
	}

	@Override
	protected void getViews() {

	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {

	}

	@Override
	public void onClick(View v) {

	}

	//Log标签
	private static final String TAG = "SIGN";
	//声明对象
	private Button sign;
	private TextView show;
	private GridView myDate;
	//获取本地时间
	Time nowTime = new Time();
	//一个月内的天数
	private int dayMaxNum;
	private int year, month, day, ym;
	private SignDAO sdao;
	//查询结果
	private List<String> list = new ArrayList<String>();
	private ArrayList<HashMap<String, Object>> sinalist, alisttmp;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qian_dao);
		Log.i(TAG, "SIGN is onCreate");
		//初始化对象
		init();
		//初始化数据库信息
		initdata();
		myDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//判断是否已经签到 从服务器获取签到信息
				//模拟从本地数据库获取信息
				if (day == arg2 + 1)//只能当天签到
				{
					sinalist = sdao.findSinInfo("zhangsan", year + "-" + month + "-" + (arg2 + 1), "0");
					if (sinalist.size() > 0) {
						Toast.makeText(getApplicationContext(), "已经签过到不能重复签到", 200).show();
						Log.d("", "已签到");
					} else {
						//在数据库插入一条数据
						sdao.insertSinInfo("zhangsan", "张三", year + "-" + month + "-" + (arg2 + 1), year + "" + month);
						initdata();
					}
				}

			}
		});
	}

	/**
	 * @param
	 */
	private void init() {
		sign = (Button) this.findViewById(R.id.sign);
		show = (TextView) this.findViewById(R.id.show);
		myDate = (GridView) this.findViewById(R.id.myDate);
		//取本地时间（时间应该从服务器获取）
		nowTime.setToNow();
		year = nowTime.year;
		month = nowTime.month + 1;
		day = nowTime.monthDay;
		show.setText(year + "-" + month + "-" + day);
	}

	/**
	 * @param
	 */
	private void initdata() {
		sdao = new SignDAO(ActivityQianDao.this);
		sdao.open();
		sinalist = sdao.findSinInfo("zhangsan", "", year + "" + month);//查询当月已签到的日期
		list.clear();
		dayMaxNum = getCurrentMonthDay();
		for (int i = 0; i < dayMaxNum; i++) {
			list.add(i, i + 1 + "");
		}
		myDate.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myDate.setAdapter(new getDayNumAdapter(getApplicationContext()));
	}

	class getDayNumAdapter extends BaseAdapter {

		Context c;

		public getDayNumAdapter(Context c) {
			this.c = c;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = LinearLayout.inflate(c, R.layout.date, null);
			TextView txtWeek = (TextView) v.findViewById(R.id.txtWeekDateMB);
			TextView txtDay = (TextView) v.findViewById(R.id.txtDayDateMB);
			switch (position) {
			case 0:
				txtWeek.setText("一");
				break;
			case 1:
				txtWeek.setText("二");
				break;
			case 2:
				txtWeek.setText("三");
				break;
			case 3:
				txtWeek.setText("四");
				break;
			case 4:
				txtWeek.setText("五");
				break;
			case 5:
				txtWeek.setText("六");
				break;
			case 6:
				txtWeek.setText("日");
				break;
			}
			if (position < 7) {
				txtWeek.setVisibility(View.VISIBLE);
			}
			int lstDay = Integer.parseInt(list.get(position));
			//标记当前日期
			if (day == lstDay) {
				txtDay.setText(list.get(position).toString());
				txtDay.setTextColor(Color.RED);
			} else
				txtDay.setText(list.get(position).toString());
			//标记已签到后的背景
			for (int i = 0; i < sinalist.size(); i++) {
				String nowdate = sinalist.get(i).get("sindate").toString();
				String[] nowdatearr = nowdate.split("-");
				if (lstDay == Integer.parseInt(nowdatearr[2])) {
					txtDay.setBackgroundColor(Color.BLUE);
					++ym;
				}
				sign.setText("已经签到天数:" + ym);
			}
			return v;
		}

	}


	//获取当月的 天数
	public int getCurrentMonthDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
}
