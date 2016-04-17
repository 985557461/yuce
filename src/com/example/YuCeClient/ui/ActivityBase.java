package com.example.YuCeClient.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class ActivityBase extends FragmentActivity {
	protected static final int NET_EORROR = 10001;
	protected static final int NET_BUSY = 10002;

	private ProgressDialog waitting_dialog;
	private boolean mhideSoftOutsideEditText = false;
	protected boolean isRequestServer = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getViews();
		initViews();
		setListeners();
		addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Request.cancelRequest(this);
		removeActivity(this);
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
		super.onDestroy();
	}

	public static List<Activity> activityList = new ArrayList<Activity>();

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NET_EORROR:
				ToastUtil.makeText(getApplicationContext(), getString(R.string.netError), Toast.LENGTH_SHORT).show();
				dismissDialog();
				isRequestServer = false;
				break;
			case NET_BUSY:
				ToastUtil.makeText(getApplicationContext(), getString(R.string.netBusy), Toast.LENGTH_SHORT).show();
				dismissDialog();
				isRequestServer = false;
				break;
			}
			handleDefaultMessage(msg);
		}
	};

	protected void handleDefaultMessage(Message msg) {
	}

	/**
	 * 当touch非输入框Edittext区域自动隐藏键盘
	 */
	protected void hideSoftInputOutsideEditText() {
		mhideSoftOutsideEditText = true;
	}

	/**
	 * 判断touch事件MotionEvent 是否发生在currentFocus上面
	 *
	 * @param ev
	 * @param currentFocus
	 * @return
	 */
	private boolean isTouchInsideView(final MotionEvent ev, final View currentFocus) {
		final int[] loc = new int[2];
		currentFocus.getLocationOnScreen(loc);
		return ev.getX() > loc[0] && ev.getY() > loc[1] && ev.getX() < (loc[0] + currentFocus.getWidth()) && ev.getY() < (loc[1] + currentFocus.getHeight());
	}

	public void showDialog() {
		showDialog(getString(R.string.waiting));
	}

	public void showDialog(String msg) {
		showDialog(msg, -1);
	}

	public void showDialog(String msg, final long requestId) {
		if (waitting_dialog == null) {
			waitting_dialog = new ProgressDialog(this);
			waitting_dialog.setMessage(msg);
			waitting_dialog.setCancelable(true);
			waitting_dialog.setCanceledOnTouchOutside(false);
		}
		waitting_dialog.setMessage(msg);
		if (!waitting_dialog.isShowing()) {
			waitting_dialog.show();
		}
		waitting_dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		// HGProgressDialog.show(this,msg);
	}

	public void dismissDialog() {
		if (!isFinishing() && waitting_dialog != null && waitting_dialog.isShowing()) {
			waitting_dialog.dismiss();
		}
		// HGProgressDialog.dismiss();
	}

	protected void addActivity(Activity activity) {
		if (activityList == null) {
			activityList = new ArrayList<Activity>();
		}
		activityList.add(activity);
	}

	protected void removeActivity(Activity activity) {
		if (activityList != null) {
			activityList.remove(activity);
		}
	}

	protected void killActivity() {
		if (activityList != null) {
			for (; 0 < activityList.size(); ) {
				Activity activity = activityList.remove(0);
				if (activity != null) {
					activity.finish();
				}
			}
		}
	}

	protected abstract void getViews();

	protected abstract void initViews();

	protected abstract void setListeners();

	/**
	 * 返回
	 *
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}
