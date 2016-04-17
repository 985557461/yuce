package com.example.YuCeClient.ui.mine.ti_xian;

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
public class HCAddZhiFuBaoCardDialog extends FrameLayout implements View.OnClickListener {
	private EditText nameEditText;
	private EditText bankNameEditText;
	private EditText moneyEditText;
	private TextView complete;

	private HCAddBankDlgClickListener l;
	private FrameLayout rootView;

	public HCAddZhiFuBaoCardDialog(Context context) {
		super(context);
	}

	public static HCAddZhiFuBaoCardDialog showDlg(Activity activity, HCAddBankDlgClickListener l) {
		HCAddZhiFuBaoCardDialog dlg = new HCAddZhiFuBaoCardDialog(activity, l);
		dlg.show();
		return dlg;
	}

	@SuppressWarnings("deprecation")
	public HCAddZhiFuBaoCardDialog(Activity activity, HCAddBankDlgClickListener l) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.add_zhifubao_card_dlg, this, true);

		nameEditText = (EditText) findViewById(R.id.nameEditText);
		bankNameEditText = (EditText) findViewById(R.id.bankNameEditText);
		moneyEditText = (EditText) findViewById(R.id.moneyEditText);
		complete = (TextView) findViewById(R.id.complete);

		complete.setOnClickListener(this);

		setVisibility(View.GONE);

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.hc_zhifubao_card_dialog);
		this.l = l;
	}

	public void setData(TiXianModel tiXianModel) {
		nameEditText.setText(tiXianModel.realname);
		bankNameEditText.setText(tiXianModel.cardnum);
		nameEditText.setEnabled(false);
		bankNameEditText.setEnabled(false);
	}

	public static boolean onBackPressed(Activity activity) {
		HCAddZhiFuBaoCardDialog dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			dlg.dismiss();
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HCAddZhiFuBaoCardDialog dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HCAddZhiFuBaoCardDialog dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HCAddZhiFuBaoCardDialog getDlgView(Activity activity) {
		return (HCAddZhiFuBaoCardDialog) getRootView(activity).findViewById(R.id.hc_zhifubao_card_dialog);
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

	public interface HCAddBankDlgClickListener {
		void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount);
	}

	@Override
	public void onClick(View v) {
		String nameStr = nameEditText.getText().toString();
		if (TextUtils.isEmpty(nameStr)) {
			ToastUtil.makeShortText("请输入真实姓名");
			return;
		}
		String bankNameStr = bankNameEditText.getText().toString();
		if (TextUtils.isEmpty(bankNameStr)) {
			ToastUtil.makeShortText("请输入银行名称");
			return;
		}
		String moneyStr = moneyEditText.getText().toString();
		if (TextUtils.isEmpty(moneyStr)) {
			ToastUtil.makeShortText("请输入提取金额");
			return;
		}
		int moneyCount = Integer.valueOf(moneyStr);
		if (moneyCount <= 0) {
			ToastUtil.makeShortText("输入金币数量必须大于0");
			return;
		}
		dismiss();
		if (l != null) {
			l.onSureBtnClicked("0", "支付宝", bankNameStr, nameStr, moneyCount);
		}
	}
}
