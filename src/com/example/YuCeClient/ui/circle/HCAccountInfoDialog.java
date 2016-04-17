package com.example.YuCeClient.ui.circle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.circle.search.ActivityPostSearch;

@SuppressLint("WrongViewCast")
public class HCAccountInfoDialog extends FrameLayout implements View.OnClickListener {
	private FrameLayout rootView;
	private LinearLayout container;
	private ImageView avatar;
	private TextView nickName;
	private TextView location;
	private RelativeLayout faYanRL;
	private TextView faYanCount;
	private RelativeLayout replyRL;
	private TextView replyCount;
	private RelativeLayout likeRL;
	private TextView likeCount;
	private RelativeLayout collectionRL;
	private TextView collectionCount;
	private RelativeLayout searchRL;

	public HCAccountInfoDialog(Context context) {
		super(context);
	}

	public static HCAccountInfoDialog showDlg(Activity activity) {
		HCAccountInfoDialog dlg = new HCAccountInfoDialog(activity);
		dlg.show();
		return dlg;
	}

	@SuppressWarnings("deprecation")
	public HCAccountInfoDialog(Activity activity) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.hc_account_info_dialog, this, true);

		container = (LinearLayout) findViewById(R.id.container);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		location = (TextView) findViewById(R.id.location);
		faYanRL = (RelativeLayout) findViewById(R.id.faYanRL);
		faYanCount = (TextView) findViewById(R.id.faYanCount);
		replyRL = (RelativeLayout) findViewById(R.id.replyRL);
		replyCount = (TextView) findViewById(R.id.replyCount);
		likeRL = (RelativeLayout) findViewById(R.id.likeRL);
		likeCount = (TextView) findViewById(R.id.likeCount);
		collectionRL = (RelativeLayout) findViewById(R.id.collectionRL);
		collectionCount = (TextView) findViewById(R.id.collectionCount);
		searchRL = (RelativeLayout) findViewById(R.id.searchRL);

		faYanRL.setOnClickListener(this);
		replyRL.setOnClickListener(this);
		likeRL.setOnClickListener(this);
		collectionRL.setOnClickListener(this);
		searchRL.setOnClickListener(this);

		setVisibility(View.GONE);
		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.account_info_dialog);
	}

	public static boolean onBackPressed(Activity activity) {
		HCAccountInfoDialog dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			dlg.dismiss();
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCAccountInfoDialog dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCAccountInfoDialog dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCAccountInfoDialog getDlgView(Activity activity) {
		return (HCAccountInfoDialog) getRootView(activity).findViewById(R.id.account_info_dialog);
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
		int touchX = (int) event.getRawX();
		int touchY = (int) event.getRawY();
		Rect rect = new Rect();
		container.getGlobalVisibleRect(rect);
		if (!rect.contains(touchX, touchY)) {
			dismiss();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.faYanRL:
			break;
		case R.id.replyRL:
			break;
		case R.id.likeRL:
			break;
		case R.id.collectionRL:
			break;
		case R.id.searchRL:
			ActivityPostSearch.open((Activity)getContext());
			break;
		}
		dismiss();
	}
}
