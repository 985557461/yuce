package com.example.YuCeClient.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;

@SuppressLint("WrongViewCast")
public class HCDaShangConfirmAlert extends FrameLayout implements View.OnClickListener {
	private TextView jinBiCount;
	private HCDaShangConfirmAlertListener l;
	private FrameLayout rootView;

	public HCDaShangConfirmAlert(Context context) {
		super(context);
	}

	public static HCDaShangConfirmAlert showDlg(String jinBiCount, Activity activity, HCDaShangConfirmAlertListener l) {
		HCDaShangConfirmAlert dlg = new HCDaShangConfirmAlert(jinBiCount, activity, l);
		dlg.show();
		return dlg;
	}

	@SuppressWarnings("deprecation")
	public HCDaShangConfirmAlert(String jinBiCountStr, Activity activity, HCDaShangConfirmAlertListener l) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.hc_dashang_confirm_alert, this, true);
		jinBiCount = (TextView) findViewById(R.id.jinBiCount);

		findViewById(R.id.bnConfirm).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);

		jinBiCount.setText(jinBiCountStr);
		setVisibility(View.GONE);

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.hc_dashang_confirm_alert);
		this.l = l;
	}

	public static boolean onBackPressed(Activity activity) {
		HCDaShangConfirmAlert dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			dlg.dismiss();
			dlg.l.onSureClicked(false);
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCDaShangConfirmAlert dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCDaShangConfirmAlert dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCDaShangConfirmAlert getDlgView(Activity activity) {
		return (HCDaShangConfirmAlert) getRootView(activity).findViewById(R.id.hc_dashang_confirm_alert);
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

	public interface HCDaShangConfirmAlertListener {
		void onSureClicked(boolean isConfirm);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		l.onSureClicked(v.getId() == R.id.bnConfirm);
	}
}
