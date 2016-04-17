package com.example.YuCeClient.ui.yinPanQiMen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;

@SuppressLint("WrongViewCast")
public class HCScrollViewDlg extends FrameLayout implements View.OnClickListener {
	private FrameLayout rootView;
	private ImageView addImageView;
	private TextView title;
	private ImageView cancelImageView;
	private TextView content;

	public HCScrollViewDlg(Context context) {
		super(context);
	}

	public static HCScrollViewDlg showDlg(Activity activity, String content) {
		HCScrollViewDlg dlg = new HCScrollViewDlg(activity, content);
		dlg.show();
		return dlg;
	}

	@SuppressWarnings("deprecation")
	public HCScrollViewDlg(Activity activity, String contentStr) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.hc_scroll_view_dialog, this, true);

		addImageView = (ImageView) findViewById(R.id.addImageView);
		title = (TextView) findViewById(R.id.title);
		cancelImageView = (ImageView) findViewById(R.id.cancelImageView);
		content = (TextView) findViewById(R.id.content);

		if (!TextUtils.isEmpty(contentStr)) {
			content.setText(Html.fromHtml(contentStr));
		}

		content.setMovementMethod(ScrollingMovementMethod.getInstance());

		setVisibility(View.GONE);
		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.scroll_view_dialog);
	}

	public static boolean onBackPressed(Activity activity) {
		HCScrollViewDlg dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			dlg.dismiss();
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCScrollViewDlg dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCScrollViewDlg dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCScrollViewDlg getDlgView(Activity activity) {
		return (HCScrollViewDlg) getRootView(activity).findViewById(R.id.scroll_view_dialog);
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

	@Override
	public void onClick(View v) {
		dismiss();
	}
}
