package com.example.YuCeClient.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;

@SuppressLint("WrongViewCast")
public class HCAlertDlgNoTitle extends FrameLayout implements View.OnClickListener {
	private TextView textMessage;
	private TextView title;
	private View dividerLine;
	private boolean cancelAble = true;
	private HCAlertDlgClickListener l;
	private FrameLayout rootView;

	public HCAlertDlgNoTitle(Context context) {
		super(context);
	}

	public static HCAlertDlgNoTitle showDlg(String titleStr, String message, Activity activity, HCAlertDlgClickListener l, boolean cancelAble) {
		if (!TextUtils.isEmpty(message)) {
			message = message.replace("\\n", "\n");

		}
		HCAlertDlgNoTitle dlg = new HCAlertDlgNoTitle(titleStr, message, activity, l, cancelAble);
		dlg.show();
		return dlg;
	}

	public static HCAlertDlgNoTitle showDlg(String titleStr, String message, Activity activity, HCAlertDlgClickListener l) {
		return showDlg(titleStr, message, activity, l, true);
	}

	public void setPositiveBnText(String text) {
		((TextView) findViewById(R.id.bnConfirm)).setText(text);
	}

	public void setPositiveBnColor(int color) {
		((TextView) findViewById(R.id.bnConfirm)).setTextColor(color);
	}

	public void setNegativeBnText(String text) {
		((TextView) findViewById(R.id.bnCancel)).setText(text);
	}

	public void setNegativeBnColor(int color) {
		((TextView) findViewById(R.id.bnCancel)).setTextColor(color);
	}

	public void setBackgroundColor(int color) {
		findViewById(R.id.alertDlgRoot).setBackgroundColor(color);
	}

	@SuppressWarnings("deprecation")
	public HCAlertDlgNoTitle(String titleStr, String message, Activity activity, HCAlertDlgClickListener l, boolean cancelAble) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.view_alert_dlg, this, true);
		title = (TextView) findViewById(R.id.title);
		dividerLine = findViewById(R.id.dividerLine);
		textMessage = (TextView) findViewById(R.id.textMessage);

		findViewById(R.id.bnConfirm).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);

		this.cancelAble = cancelAble;
		textMessage.setText(message);
		title.setText(titleStr);
		setVisibility(View.GONE);

		if (TextUtils.isEmpty(titleStr)) {
			title.setVisibility(View.GONE);
			dividerLine.setVisibility(View.GONE);
		} else {
			title.setVisibility(View.VISIBLE);
			dividerLine.setVisibility(View.VISIBLE);
		}

		if (TextUtils.isEmpty(message)) {
			textMessage.setVisibility(View.GONE);
			dividerLine.setVisibility(View.GONE);
		}

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.view_alert_dlg_no_title);
		this.l = l;
	}

	public static boolean onBackPressed(Activity activity) {
		HCAlertDlgNoTitle dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			if (dlg.cancelAble) {
				dlg.dismiss();
				dlg.l.onAlertDlgClicked(false);
			}
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCAlertDlgNoTitle dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCAlertDlgNoTitle dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCAlertDlgNoTitle getDlgView(Activity activity) {
		return (HCAlertDlgNoTitle) getRootView(activity).findViewById(R.id.view_alert_dlg_no_title);
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

	public interface HCAlertDlgClickListener {
		void onAlertDlgClicked(boolean isConfirm);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		l.onAlertDlgClicked(v.getId() == R.id.bnConfirm);
	}
}
