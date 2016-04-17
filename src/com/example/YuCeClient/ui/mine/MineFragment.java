package com.example.YuCeClient.ui.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.circle.Actions;
import com.example.YuCeClient.ui.mine.qian_dao.ActivityQianDao;
import com.example.YuCeClient.ui.mine.question.ActivityMyPostQuestions;
import com.example.YuCeClient.ui.mine.reply.ActivityMyPostReplys;
import com.example.YuCeClient.ui.mine.ti_xian.ActivityTiXian;
import com.example.YuCeClient.util.Debug;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-3.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
	private ImageView avatar;
	private TextView nickName;
	private TextView level;
	private TextView jinbiCount;
	private ImageView chongZhi;
	private TextView accountDesc;
	private TextView accountId;
	private TextView accountLevelName;
	private TextView caiNaCount;
	private LinearLayout postQuestionCountLL;
	private LinearLayout reviewLL;
	private RelativeLayout qianDaoRL;
	private RelativeLayout tiXianRL;
	private RelativeLayout shenQingRL;

	private Account account;
	private ImageLoader imageLoader;

	public static final String RefreshInfoAction = "refresh_info_action";
	public static final String RefreshJinBiAction = "refresh_jinbi_action";
	private RefreshInfoBroadcastReceiver refreshInfoBroadcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mine_fragment, container, false);
		initViews(view);
		return view;
	}

	private void registerReceiver() {
		refreshInfoBroadcastReceiver = new RefreshInfoBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(RefreshInfoAction);
		intentFilter.addAction(RefreshJinBiAction);
		getActivity().registerReceiver(refreshInfoBroadcastReceiver, intentFilter);
	}

	private void unRegisterReceiver() {
		if (refreshInfoBroadcastReceiver != null) {
			getActivity().unregisterReceiver(refreshInfoBroadcastReceiver);
		}
	}

	private void initViews(View view) {
		registerReceiver();
		account = HCApplicaton.getInstance().getAccount();
		imageLoader = HCApplicaton.getInstance().getImageLoader();

		avatar = (ImageView) view.findViewById(R.id.avatar);
		nickName = (TextView) view.findViewById(R.id.nickName);
		level = (TextView) view.findViewById(R.id.level);
		jinbiCount = (TextView) view.findViewById(R.id.jinbiCount);
		chongZhi = (ImageView) view.findViewById(R.id.chongZhi);
		accountDesc = (TextView) view.findViewById(R.id.accountDesc);
		accountId = (TextView) view.findViewById(R.id.accountId);
		accountLevelName = (TextView) view.findViewById(R.id.accountLevelName);
		caiNaCount = (TextView) view.findViewById(R.id.caiNaCount);
		postQuestionCountLL = (LinearLayout) view.findViewById(R.id.postQuestionCountLL);
		reviewLL = (LinearLayout) view.findViewById(R.id.reviewLL);
		qianDaoRL = (RelativeLayout) view.findViewById(R.id.qianDaoRL);
		tiXianRL = (RelativeLayout) view.findViewById(R.id.tiXianRL);
		shenQingRL = (RelativeLayout) view.findViewById(R.id.shenQingRL);

		/**监听**/
		chongZhi.setOnClickListener(this);
		postQuestionCountLL.setOnClickListener(this);
		reviewLL.setOnClickListener(this);
		qianDaoRL.setOnClickListener(this);
		tiXianRL.setOnClickListener(this);
		avatar.setOnClickListener(this);
		shenQingRL.setOnClickListener(this);

		/**初始化信息**/
		if (!TextUtils.isEmpty(account.avatar)) {
			imageLoader.displayImage(account.avatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(account.userName)) {
			nickName.setText(account.userName);
		} else {
			nickName.setText("暂无名字");
		}

		jinbiCount.setText(account.jinBiCount + "");
		if (!TextUtils.isEmpty(account.personDesc)) {
			accountDesc.setText(account.personDesc);
		} else {
			accountDesc.setText("这个人很懒，什么也没留下。");
		}
		if (!TextUtils.isEmpty(account.userId)) {
			accountId.setText("我的账号：" + account.userId);
		} else {
			accountId.setText("我的账号：");
		}
		if (!TextUtils.isEmpty(account.levelNickName)) {
			accountLevelName.setText(account.levelNickName);
		} else {
			accountLevelName.setText("我的称谓：" + "无名小卒");
		}
		caiNaCount.setText("被采纳数：" + account.caiNaCount);
		if (account.userType == 1) {
			shenQingRL.setVisibility(View.GONE);
			tiXianRL.setVisibility(View.VISIBLE);
		} else {
			shenQingRL.setVisibility(View.VISIBLE);


			//todo
			tiXianRL.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroyView() {
		unRegisterReceiver();
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avatar:
			ActivityAccountInfo.open(getActivity());
			break;
		case R.id.chongZhi:
			ActivityChongZhi.open(getActivity());
			break;
		case R.id.postQuestionCountLL:
			ActivityMyPostQuestions.open(getActivity());
			break;
		case R.id.reviewLL:
			ActivityMyPostReplys.open(getActivity());
			break;
		case R.id.tiXianRL:
			ActivityTiXian.open(getActivity());
			break;
		case R.id.qianDaoRL:
			ActivityQianDao.open(getActivity());
			break;
		case R.id.shenQingRL:
			tryToShenQing();
			break;
		}
	}

	private void tryToShenQing() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		Request.doRequest(getActivity(), nameValuePairs, ServerConfig.URL_MASTER_SHENQING, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				Debug.debug("xiaoyu", "failed");
			}

			@Override
			public void onComplete(String response) {
				CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (commonModel != null && commonModel.result == 1) {
					account.userType = 1;
					account.saveMeInfoToPreference();
					ToastUtil.makeShortText("申请成功");
					Intent intent = new Intent(Actions.ACTION_REFRESH_MASTER_LIST);
					HCApplicaton.getInstance().sendBroadcast(intent);
				} else if (commonModel.result == 3) {
					ToastUtil.makeShortText("采纳数不足10个，申请条件不符合");
				} else {
					ToastUtil.makeShortText("申请失败");
				}
			}
		});
	}

	private class RefreshInfoBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (RefreshInfoAction.equals(action)) {
				/**初始化信息**/
				if (!TextUtils.isEmpty(account.avatar)) {
					imageLoader.displayImage(account.avatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
				} else {
					imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
				}
				if (!TextUtils.isEmpty(account.userName)) {
					nickName.setText(account.userName);
				} else {
					nickName.setText("暂无名字");
				}
				if (!TextUtils.isEmpty(account.personDesc)) {
					accountDesc.setText(account.personDesc);
				} else {
					accountDesc.setText("这个人很懒，什么也没留下。");
				}
			} else if (action.equals(RefreshJinBiAction)) {
				jinbiCount.setText(account.jinBiCount + "");
			}
		}
	}
}
