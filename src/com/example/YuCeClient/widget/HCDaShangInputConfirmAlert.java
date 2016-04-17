package com.example.YuCeClient.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.ToastUtil;

@SuppressLint("WrongViewCast")
public class HCDaShangInputConfirmAlert extends FrameLayout implements View.OnClickListener {
	private EditText jinbiEditText;
	private HCDaShangInputConfirmAlertListener l;
	private FrameLayout rootView;

	public HCDaShangInputConfirmAlert(Context context) {
		super(context);
	}

	public static HCDaShangInputConfirmAlert showDlg(Activity activity, HCDaShangInputConfirmAlertListener l) {
		HCDaShangInputConfirmAlert dlg = new HCDaShangInputConfirmAlert(activity, l);
		dlg.show();
		return dlg;
	}

	@SuppressWarnings("deprecation")
	public HCDaShangInputConfirmAlert(Activity activity, HCDaShangInputConfirmAlertListener l) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.hc_dashang_input_confirm_alert, this, true);
		jinbiEditText = (EditText) findViewById(R.id.jinbiEditText);

		findViewById(R.id.bnConfirm).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);

		setVisibility(View.GONE);

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.hc_dashang_input_confirm_alert);
		this.l = l;
	}

	public static boolean onBackPressed(Activity activity) {
		HCDaShangInputConfirmAlert dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			dlg.dismiss();
			dlg.l.onSureClicked(false,0);
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCDaShangInputConfirmAlert dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCDaShangInputConfirmAlert dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCDaShangInputConfirmAlert getDlgView(Activity activity) {
		return (HCDaShangInputConfirmAlert) getRootView(activity).findViewById(R.id.hc_dashang_input_confirm_alert);
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

	public interface HCDaShangInputConfirmAlertListener {
		void onSureClicked(boolean isConfirm, int count);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bnConfirm) {
			String jinbiStr = jinbiEditText.getText().toString();
			if (TextUtils.isEmpty(jinbiStr)) {
				ToastUtil.makeShortText("请输入金币数量");
				return;
			}
			int jinbi = Integer.valueOf(jinbiStr);
			if (jinbi <= 0) {
				ToastUtil.makeShortText("金币数量不能小于0");
				return;
			}
			if (l != null) {
				l.onSureClicked(true, jinbi);
			}
		} else if (v.getId() == R.id.bnCancel) {
			if (l != null) {
				l.onSureClicked(false, 0);
			}
		}
		dismiss();
	}
}
