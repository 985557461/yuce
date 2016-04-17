package com.example.YuCeClient.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.DisplayUtil;
import com.example.YuCeClient.widget.wheel_view.TosGallery;
import com.example.YuCeClient.widget.wheel_view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.YuCeClient.R.id.lineOne;

/**
 * Created by xiaoyuPC on 2015/4/25.
 */
public class HCDatePickDialog extends FrameLayout implements View.OnClickListener {
	private TextView yinLi;
	private View lineOne;
	private TextView yangLi;
	private View lineTwo;
	private TextView today;
	private TextView sure;
	private WheelView yearWheel;
	private WheelView monthWheel;
	private WheelView dayWheel;
	private WheelView hourWheel;
	private WheelView minWheel;
	private FrameLayout rootView;
	private Activity activity;

	private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static final String[] MONTH_NAME = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
	private static String[] HOUR_NAME = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	private ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
	private ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
	private ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
	private ArrayList<TextInfo> mHours = new ArrayList<TextInfo>();
	private ArrayList<TextInfo> mMins = new ArrayList<TextInfo>();

	private int mCurDate = 0;
	private int mCurMonth = 0;
	private int mCurYear = 0;
	private String mCurHour;
	private String mCurMin;

	private boolean isYangLi = true;

	private HCDatePickDialogListener listener;

	public interface HCDatePickDialogListener {
		void onDataPicked(boolean isYangLi,String year, String month, String day, String hour, String min);
	}

	public HCDatePickDialog(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, null);
	}

	public HCDatePickDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, null);
	}

	public HCDatePickDialog(Context context) {
		super(context);
		init(context, null);
	}

	public HCDatePickDialog(Context context, HCDatePickDialogListener l) {
		super(context);
		init(context, l);
	}

	public static HCDatePickDialog showDlg(Activity activity, HCDatePickDialogListener l) {
		HCDatePickDialog dlg = new HCDatePickDialog(activity, l);
		dlg.show();
		return dlg;
	}

	private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
		@Override
		public void onEndFling(TosGallery v) {
			int pos = v.getSelectedItemPosition();
			if (v == dayWheel) {
				TextInfo info = mDates.get(pos);
				setDate(info.mIndex);
			} else if (v == monthWheel) {
				TextInfo info = mMonths.get(pos);
				setMonth(info.mIndex);
			} else if (v == yearWheel) {
				TextInfo info = mYears.get(pos);
				setYear(info.mIndex);
			} else if (v == hourWheel) {
				TextInfo info = mHours.get(pos);
				setHour(info.mText.replace("点",""));
			} else if (v == minWheel) {
				TextInfo info = mMins.get(pos);
				setMin(info.mText.replace("分",""));
			}
		}
	};

	private void init(Context context, HCDatePickDialogListener l) {
		activity = (ActivityBase) context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.dialog_date_pick, this, true);

		yinLi = (TextView) findViewById(R.id.yinLi);
		lineOne = findViewById(R.id.lineOne);
		yangLi = (TextView) findViewById(R.id.yangLi);
		lineTwo = findViewById(R.id.lineTwo);
		today = (TextView) findViewById(R.id.today);
		sure = (TextView) findViewById(R.id.sure);

		yearWheel = (WheelView) findViewById(R.id.yearWheel);
		monthWheel = (WheelView) findViewById(R.id.monthWheel);
		dayWheel = (WheelView) findViewById(R.id.dayWheel);
		hourWheel = (WheelView) findViewById(R.id.hourWheel);
		minWheel = (WheelView) findViewById(R.id.minWheel);

		lineOne.setVisibility(View.INVISIBLE);
		lineTwo.setVisibility(View.VISIBLE);

		yinLi.setOnClickListener(this);
		yangLi.setOnClickListener(this);
		today.setOnClickListener(this);
		sure.setOnClickListener(this);

		dayWheel.setOnEndFlingListener(mListener);
		monthWheel.setOnEndFlingListener(mListener);
		yearWheel.setOnEndFlingListener(mListener);
		hourWheel.setOnEndFlingListener(mListener);
		minWheel.setOnEndFlingListener(mListener);

		dayWheel.setSoundEffectsEnabled(false);
		monthWheel.setSoundEffectsEnabled(false);
		yearWheel.setSoundEffectsEnabled(false);
		hourWheel.setSoundEffectsEnabled(false);
		minWheel.setSoundEffectsEnabled(false);

		dayWheel.setAdapter(new WheelTextAdapter(context));
		monthWheel.setAdapter(new WheelTextAdapter(context));
		yearWheel.setAdapter(new WheelTextAdapter(context));
		hourWheel.setAdapter(new WheelTextAdapter(context));
		minWheel.setAdapter(new WheelTextAdapter(context));

		prepareData();

		setVisibility(View.GONE);

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.view_date_pick_dlg);
		this.listener = l;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.yinLi:
			lineOne.setVisibility(View.VISIBLE);
			lineTwo.setVisibility(View.INVISIBLE);
			isYangLi = false;
			break;
		case R.id.yangLi:
			lineOne.setVisibility(View.INVISIBLE);
			lineTwo.setVisibility(View.VISIBLE);
			isYangLi = true;
			break;
		case R.id.today:
			prepareData();
			break;
		case R.id.sure:
			if (listener != null) {
				listener.onDataPicked(isYangLi,mCurYear + "", mCurMonth + "", mCurDate + "", mCurHour, mCurMin);
			}
			dismiss();
			break;
		}
	}

	public static boolean onBackPressed(Activity activity) {
		HCDatePickDialog dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			dlg.dismiss();
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCDatePickDialog dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCDatePickDialog dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCDatePickDialog getDlgView(Activity activity) {
		return (HCDatePickDialog) getRootView(activity).findViewById(R.id.view_date_pick_dlg);
	}

	private static FrameLayout getRootView(Activity activity) {
		return (FrameLayout) activity.findViewById(R.id.rootView);
	}

	public boolean isShowing() {
		return getVisibility() == View.VISIBLE;
	}

	public void show() {
		if (getParent() != null) {
			return;
		}
		rootView.addView(this);
		setVisibility(View.VISIBLE);
	}

	public void dismiss() {
		if (getParent() == null) {
			return;
		}
		setVisibility(View.GONE);
		rootView.removeView(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	private void setDate(int date) {
		if (date != mCurDate) {
			mCurDate = date;
		}
	}

	private void setYear(int year) {
		if (year != mCurYear) {
			mCurYear = year;
		}
	}

	private void setHour(String hourStr) {
		if (!hourStr.equals(mCurHour)) {
			mCurHour = hourStr;
		}
	}

	private void setMin(String minStr) {
		if (!minStr.equals(mCurMin)) {
			mCurMin = minStr;
		}
	}

	private void setMonth(int month) {
		if (month != mCurMonth) {
			mCurMonth = month;
			Calendar calendar = Calendar.getInstance();
			int date = calendar.get(Calendar.DATE);
			prepareDayData(mCurYear, month, date);
		}
	}

	private boolean isLeapYear(int year) {
		return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
	}

	private void prepareData() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);

		mCurYear = year;
		mCurMonth = month;
		mCurDate = day;
		mCurHour = hour + "";
		mCurMin = min + "";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < MONTH_NAME.length; ++i) {
			sb.setLength(0);
			sb.append(MONTH_NAME[i]).append("月");
			mMonths.add(new TextInfo(i, sb.toString(), (i == month)));
		}

		for (int i = 1900; i <= 2050; ++i) {
			sb.setLength(0);
			sb.append(i).append("年");
			mYears.add(new TextInfo(i, sb.toString(), (i == year)));
		}

		for (int i = 0; i < HOUR_NAME.length; i++) {
			sb.setLength(0);
			sb.append(HOUR_NAME[i]).append("点");
			mHours.add(new TextInfo(i, sb.toString(), (HOUR_NAME[i].equals(mCurHour))));
		}

		for (int i = 1; i < 61; i++) {
			sb.setLength(0);
			sb.append(i).append("分");
			mMins.add(new TextInfo(i, sb.toString(), ((i + "").equals(mCurHour))));
		}

		((WheelTextAdapter) monthWheel.getAdapter()).setData(mMonths);
		((WheelTextAdapter) yearWheel.getAdapter()).setData(mYears);
		((WheelTextAdapter) hourWheel.getAdapter()).setData(mHours);
		((WheelTextAdapter) minWheel.getAdapter()).setData(mMins);

		prepareDayData(year, month, day);

		yearWheel.setSelection(year - 1900);
		monthWheel.setSelection(month);
		dayWheel.setSelection(day - 1);
		hourWheel.setSelection(hour - 1);
		minWheel.setSelection(min - 1);
	}

	private void prepareDayData(int year, int month, int curDate) {
		mDates.clear();

		int days = DAYS_PER_MONTH[month];

		// The February.
		if (1 == month) {
			days = isLeapYear(year) ? 29 : 28;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= days; ++i) {
			sb.setLength(0);
			sb.append(i).append("日");
			mDates.add(new TextInfo(i, sb.toString(), (i == curDate)));
		}

		((WheelTextAdapter) dayWheel.getAdapter()).setData(mDates);
	}

	protected class TextInfo {
		public TextInfo(int index, String text, boolean isSelected) {
			mIndex = index;
			mText = text;
			mIsSelected = isSelected;

			if (isSelected) {
				mColor = getResources().getColor(R.color.common_black);
			}
		}

		public void setSelected(boolean selected) {
			mIsSelected = selected;
			if (selected) {
				mColor = getResources().getColor(R.color.common_black);
			} else {
				mColor = Color.parseColor("#b4b4b4");
			}
		}

		public int mIndex;
		public String mText;
		public boolean mIsSelected = false;
		public int mColor = Color.parseColor("#b4b4b4");
	}

	protected class WheelTextAdapter extends BaseAdapter {
		ArrayList<TextInfo> mData = null;
		int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		int mHeight = 25;
		Context mContext = null;

		public WheelTextAdapter(Context context) {
			mContext = context;
			mHeight = DisplayUtil.dip2px(context, mHeight);
		}

		public void setData(ArrayList<TextInfo> data) {
			mData = data;
			this.notifyDataSetChanged();
		}

		public void setItemSize(int width, int height) {
			mWidth = width;
			mHeight = DisplayUtil.dip2px(mContext, height);
		}

		@Override
		public int getCount() {
			return (null != mData) ? mData.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = null;
			if (null == convertView) {
				textView = new TextView(mContext);
				textView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(11);
			} else {
				textView = (TextView) convertView;
			}
			TextInfo info = mData.get(position);
			textView.setText(info.mText);
			textView.setTextColor(info.mColor);
			return textView;
		}
	}
}
