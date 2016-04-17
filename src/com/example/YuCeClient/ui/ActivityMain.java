package com.example.YuCeClient.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.ui.circle.Actions;
import com.example.YuCeClient.ui.circle.CircleNewFragment;
import com.example.YuCeClient.ui.circle.HCAccountInfoDialog;
import com.example.YuCeClient.ui.mine.MineFragment;
import com.example.YuCeClient.ui.xuanshang.XuanShangFragment;
import com.example.YuCeClient.ui.yinPanQiMen.PaiPanFragment;
import com.example.YuCeClient.ui.yuce_master.YuCeMasterFragment;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.HCDatePickDialog;
import com.example.YuCeClient.widget.HCPopListView;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends ActivityBase implements View.OnClickListener {
	/**
	 * Called when the activity is first created.
	 */
	public static boolean isConflict = false;

	/**
	 * tabs
	 * *
	 */
	private LinearLayout paiPanLL;
	private ImageView paiPanImageView;
	private TextView paiPanTextView;
	private LinearLayout xuanShangLL;
	private ImageView xuanShangImageView;
	private TextView xuanShangTextView;
	private LinearLayout cirCleLL;
	private ImageView circleImageView;
	private TextView circleTextView;
	private LinearLayout yuCeMasterLL;
	private ImageView yuCeMasterImageView;
	private TextView yuCeMasterTextView;
	private LinearLayout mineLL;
	private ImageView mineImageView;
	private TextView mineTextView;

	/**
	 * fragment
	 * *
	 */
	private Fragment mFragmentCurrent;
	private PaiPanFragment paiPanFragment;
	private XuanShangFragment xuanShangFragment;
	private CircleNewFragment circleFragment;
	private YuCeMasterFragment yuCeMasterFragment;
	private MineFragment mineFragment;

	/**
	 * double back to finish
	 * *
	 */
	private static int TIME_LONG = 3 * 1000;
	private long lastTime;

	/**
	 * 跳转到某一个tab的broadCastReceiver*
	 */
	public static final String kTabIndexAction = "tab_index_action";
	public static final String kTabIndex = "tab_index";
	private TabChangeReceiver tabChangeReceiver;

	private void registerTabChangeReceiver() {
		if (tabChangeReceiver == null) {
			tabChangeReceiver = new TabChangeReceiver();
		}
		registerReceiver(tabChangeReceiver, new IntentFilter(kTabIndexAction));
	}

	private void unRegisterTabChangeReceiver() {
		unregisterReceiver(tabChangeReceiver);
	}


	public class TabChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (kTabIndexAction.equals(action)) {
				int index = intent.getIntExtra(kTabIndex, 0);
				changeTabStatus(index);
			}
		}
	}

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityMain.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		registerTabChangeReceiver();
		paiPanLL = (LinearLayout) findViewById(R.id.paiPanLL);
		paiPanImageView = (ImageView) findViewById(R.id.paiPanImageView);
		paiPanTextView = (TextView) findViewById(R.id.paiPanTextView);
		xuanShangLL = (LinearLayout) findViewById(R.id.xuanShangLL);
		xuanShangImageView = (ImageView) findViewById(R.id.xuanShangImageView);
		xuanShangTextView = (TextView) findViewById(R.id.xuanShangTextView);
		cirCleLL = (LinearLayout) findViewById(R.id.cirCleLL);
		circleImageView = (ImageView) findViewById(R.id.circleImageView);
		circleTextView = (TextView) findViewById(R.id.circleTextView);
		yuCeMasterLL = (LinearLayout) findViewById(R.id.yuCeMasterLL);
		yuCeMasterImageView = (ImageView) findViewById(R.id.yuCeMasterImageView);
		yuCeMasterTextView = (TextView) findViewById(R.id.yuCeMasterTextView);
		mineLL = (LinearLayout) findViewById(R.id.mineLL);
		mineImageView = (ImageView) findViewById(R.id.mineImageView);
		mineTextView = (TextView) findViewById(R.id.mineTextView);
	}

	@Override
	protected void initViews() {
		setDefaultFragment();
		registerBroadcastReceivers();
		toSynJinBiCount();
	}

	private void toSynJinBiCount(){
		/**如果用户登录了，去同步金币数量**/
		final Account account = HCApplicaton.getInstance().getAccount();
		if(!TextUtils.isEmpty(account.userId)){
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("userid",account.userId));
			Request.doRequest(this,nameValuePairs,ServerConfig.URL_ACCOUNTS_GET_JINBI,Request.GET,new Request.RequestListener() {
				@Override
				public void onException(Request.RequestException e) {

				}

				@Override
				public void onComplete(String response) {
					JinBiModel jinBiModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response,JinBiModel.class);
					if(jinBiModel != null && jinBiModel.result == 1){
						account.jinBiCount = jinBiModel.jinbi;
						account.saveMeInfoToPreference();
					}
				}
			});
		}
	}

	private class JinBiModel{
		@SerializedName("jinbi")
		public int jinbi;

		@SerializedName("result")
		public int result;
	}

	@Override
	protected void setListeners() {
		paiPanLL.setOnClickListener(this);
		xuanShangLL.setOnClickListener(this);
		cirCleLL.setOnClickListener(this);
		yuCeMasterLL.setOnClickListener(this);
		mineLL.setOnClickListener(this);
	}

	private void registerBroadcastReceivers() {
		refreshPostListBroadcastReceiver = new RefreshPostListBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Actions.ACTION_REFRESH_POST_LIST);
		intentFilter.addAction(Actions.ACTION_REFRESH_XUAN_SHANG_LIST);
		registerReceiver(refreshPostListBroadcastReceiver, intentFilter);
	}

	private void unRegisterBroadcastReceivers() {
		if (refreshPostListBroadcastReceiver != null) {
			unregisterReceiver(refreshPostListBroadcastReceiver);
		}
	}

	/**
	 * 刷新帖子列表的广播*
	 */
	private RefreshPostListBroadcastReceiver refreshPostListBroadcastReceiver;

	private class RefreshPostListBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (!TextUtils.isEmpty(action)) {
				if (action.equals(Actions.ACTION_REFRESH_POST_LIST)) {
					refreshPostFragment();
				} else if (action.equals(Actions.ACTION_REFRESH_XUAN_SHANG_LIST)) {
					refreshXuanShangFragment();
				}else if(action.equals(Actions.ACTION_REFRESH_MASTER_LIST)){
					refreshMasterFragment();
				}
			}
		}
	}

	private void refreshPostFragment() {
		if (circleFragment != null) {
			circleFragment.onRefresh();
		}
	}

	private void refreshXuanShangFragment() {
		if (xuanShangFragment != null) {
			xuanShangFragment.onRefresh();
		}
	}

	private void refreshMasterFragment(){
		if(yuCeMasterFragment != null){
			yuCeMasterFragment.refreshData();
		}
	}

	private void setDefaultFragment() {
		xuanShangImageView.setSelected(true);
		xuanShangTextView.setSelected(true);
		circleImageView.setSelected(false);
		circleTextView.setSelected(false);
		paiPanImageView.setSelected(false);
		paiPanTextView.setSelected(false);
		yuCeMasterImageView.setSelected(false);
		yuCeMasterTextView.setSelected(false);
		mineImageView.setSelected(false);
		mineTextView.setSelected(false);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		xuanShangFragment = new XuanShangFragment();
		transaction.add(R.id.fragmentLayout, xuanShangFragment);
		transaction.commit();
		mFragmentCurrent = xuanShangFragment;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.xuanShangLL:
			changeTabStatus(0);
			break;
		case R.id.cirCleLL:
			changeTabStatus(1);
			break;
		case R.id.paiPanLL:
			changeTabStatus(2);
			break;
		case R.id.yuCeMasterLL:
			changeTabStatus(3);
			break;
		case R.id.mineLL:
			changeTabStatus(4);
			break;
		}
	}

	private void changeTabStatus(int index) {
		switch (index) {
		case 0: {
			xuanShangImageView.setSelected(true);
			xuanShangTextView.setSelected(true);
			circleImageView.setSelected(false);
			circleTextView.setSelected(false);
			paiPanImageView.setSelected(false);
			paiPanTextView.setSelected(false);
			yuCeMasterImageView.setSelected(false);
			yuCeMasterTextView.setSelected(false);
			mineImageView.setSelected(false);
			mineTextView.setSelected(false);

			if (xuanShangFragment == null) {
				xuanShangFragment = new XuanShangFragment();
			}
			switchContent(mFragmentCurrent, xuanShangFragment);
		}
		break;
		case 1: {
			xuanShangImageView.setSelected(false);
			xuanShangTextView.setSelected(false);
			circleImageView.setSelected(true);
			circleTextView.setSelected(true);
			paiPanImageView.setSelected(false);
			paiPanTextView.setSelected(false);
			yuCeMasterImageView.setSelected(false);
			yuCeMasterTextView.setSelected(false);
			mineImageView.setSelected(false);
			mineTextView.setSelected(false);

			if (circleFragment == null) {
				circleFragment = new CircleNewFragment();
			}
			switchContent(mFragmentCurrent, circleFragment);
		}
		break;
		case 2: {
			xuanShangImageView.setSelected(false);
			xuanShangTextView.setSelected(false);
			circleImageView.setSelected(false);
			circleTextView.setSelected(false);
			paiPanImageView.setSelected(true);
			paiPanTextView.setSelected(true);
			yuCeMasterImageView.setSelected(false);
			yuCeMasterTextView.setSelected(false);
			mineImageView.setSelected(false);
			mineTextView.setSelected(false);

			if (paiPanFragment == null) {
				paiPanFragment = new PaiPanFragment();
			}
			switchContent(mFragmentCurrent, paiPanFragment);
		}
		break;
		case 3: {
			xuanShangImageView.setSelected(false);
			xuanShangTextView.setSelected(false);
			circleImageView.setSelected(false);
			circleTextView.setSelected(false);
			paiPanImageView.setSelected(false);
			paiPanTextView.setSelected(false);
			yuCeMasterImageView.setSelected(true);
			yuCeMasterTextView.setSelected(true);
			mineImageView.setSelected(false);
			mineTextView.setSelected(false);

			if (yuCeMasterFragment == null) {
				yuCeMasterFragment = new YuCeMasterFragment();
			}
			switchContent(mFragmentCurrent, yuCeMasterFragment);
		}
		break;
		case 4: {
			Account account = HCApplicaton.getInstance().getAccount();
			if (TextUtils.isEmpty(account.userId)) {
				ActivityLogin.open(this);
				return;
			}
			if (mineFragment == null) {
				mineFragment = new MineFragment();
			}
			switchContent(mFragmentCurrent, mineFragment);
			xuanShangImageView.setSelected(false);
			xuanShangTextView.setSelected(false);
			circleImageView.setSelected(false);
			circleTextView.setSelected(false);
			paiPanImageView.setSelected(false);
			paiPanTextView.setSelected(false);
			yuCeMasterImageView.setSelected(false);
			yuCeMasterTextView.setSelected(false);
			mineImageView.setSelected(true);
			mineTextView.setSelected(true);
		}
		break;
		}
	}

	public void switchContent(Fragment from, Fragment to) {
		if (from != to) {
			mFragmentCurrent = to;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			if (!to.isAdded()) {
				if (from != null && from.isAdded()) {
					transaction.hide(from);
				}
				transaction.add(R.id.fragmentLayout, to).commitAllowingStateLoss();
			} else {
				transaction.hide(from).show(to).commitAllowingStateLoss();
			}
		}
	}

	@Override
	protected void onDestroy() {
		unRegisterTabChangeReceiver();
		unRegisterBroadcastReceivers();
		killActivity();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (HCDatePickDialog.hasDlg(this)) {
			HCDatePickDialog.getDlgView(this).dismiss();
			return;
		}
		if (HCAccountInfoDialog.hasDlg(this)) {
			HCAccountInfoDialog.getDlgView(this).dismiss();
			return;
		}
		if (HCPopListView.hasDlg(this)) {
			HCPopListView.getDlgView(this).dismiss();
			return;
		}
		long t = System.currentTimeMillis();
		if (t - lastTime < TIME_LONG) {
			killActivity();
		} else {
			ToastUtil.makeShortText(getString(R.string.click_finsh));
			lastTime = t;
			return;
		}
		super.onBackPressed();
	}
}
