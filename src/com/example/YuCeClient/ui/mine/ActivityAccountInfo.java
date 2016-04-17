package com.example.YuCeClient.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.ActivityMain;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.util.*;
import com.example.YuCeClient.widget.cropimage.ActivityCropImage;
import com.example.YuCeClient.widget.photo.PhotoActivity;
import com.example.YuCeClient.widget.photo.PhotoAlbumActivity;
import com.meilishuo.gson.annotations.SerializedName;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-12.
 */
public class ActivityAccountInfo extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private TextView save;
	private RelativeLayout avatarRL;
	private ImageView avatar;
	private RelativeLayout nickNameRL;
	private TextView nickName;
	private LinearLayout womenLL;
	private LinearLayout manLL;
	private RelativeLayout qianMingRL;
	private TextView qianMing;
	private TextView logout;

	/**
	 * modify avatar about
	 * *
	 */
	private static final int kActivitySettingSelectPicRequest = 101;
	private static final int kPhotoCropImageRequest = 102;
	private String avatarPath = "";

	/**
	 * 签名，名字*
	 */
	private static final int kQianMingRequest = 103;
	private static final int kNameRequest = 104;

	private Account account;
	private ImageLoader imageLoader;

	private int sex = 0;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityAccountInfo.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_account_info);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		save = (TextView) findViewById(R.id.save);
		avatarRL = (RelativeLayout) findViewById(R.id.avatarRL);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickNameRL = (RelativeLayout) findViewById(R.id.nickNameRL);
		nickName = (TextView) findViewById(R.id.nickName);
		womenLL = (LinearLayout) findViewById(R.id.womenLL);
		manLL = (LinearLayout) findViewById(R.id.manLL);
		qianMingRL = (RelativeLayout) findViewById(R.id.qianMingRL);
		qianMing = (TextView) findViewById(R.id.qianMing);
		logout = (TextView) findViewById(R.id.logout);
	}

	@Override
	protected void initViews() {
		if (!TextUtils.isEmpty(account.avatar)) {
			imageLoader.displayImage(account.avatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(account.userName)) {
			nickName.setText(account.userName);
		} else {
			nickName.setText("");
		}
		sex = account.sex;
		if (sex == 1) {
			womenLL.setBackground(null);
			manLL.setBackgroundResource(R.drawable.sex_man_bg_shape);
		} else {
			manLL.setBackground(null);
			womenLL.setBackgroundResource(R.drawable.sex_women_bg_shape);
		}
		if (!TextUtils.isEmpty(account.personDesc)) {
			qianMing.setText(account.personDesc);
		} else {
			qianMing.setText("");
		}
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		avatarRL.setOnClickListener(this);
		nickNameRL.setOnClickListener(this);
		womenLL.setOnClickListener(this);
		manLL.setOnClickListener(this);
		qianMingRL.setOnClickListener(this);
		logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.save:
			tryToSave();
			break;
		case R.id.avatarRL:
			Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, this);
			startActivityForResult(intent, kActivitySettingSelectPicRequest);
			break;
		case R.id.nickNameRL:
			ActivityModifyStr.open(this, account.userName, kNameRequest);
			break;
		case R.id.womenLL:
			sex = 0;
			manLL.setBackground(null);
			womenLL.setBackgroundResource(R.drawable.sex_women_bg_shape);
			break;
		case R.id.manLL:
			sex = 1;
			womenLL.setBackground(null);
			manLL.setBackgroundResource(R.drawable.sex_man_bg_shape);
			break;
		case R.id.qianMingRL:
			ActivityModifyStr.open(this, account.personDesc, kQianMingRequest);
			break;
		case R.id.logout:
			account.clearMeInfo();
			Intent intent1 = new Intent(ActivityMain.kTabIndexAction);
			intent1.putExtra(ActivityMain.kTabIndex, 0);
			HCApplicaton.getInstance().sendBroadcast(intent1);
			ActivityLogin.open(this);
			finish();
			break;
		}
	}

	private void tryToSave() {
		final String nickNameStr = nickName.getText().toString();
		if (TextUtils.isEmpty(nickNameStr)) {
			ToastUtil.makeShortText("昵称不能为空");
			return;
		}
		final String qianmingstr = qianMing.getText().toString();
		if (TextUtils.isEmpty(qianmingstr)) {
			ToastUtil.makeShortText("签名不能为空");
			return;
		}
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("nickname", nickNameStr));
		nameValuePairs.add(new BasicNameValuePair("sex", sex + ""));
		nameValuePairs.add(new BasicNameValuePair("qianming", qianmingstr));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_SETUSER_INFO, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.showErrorMessage(e, "");
			}

			@Override
			public void onComplete(String response) {
				CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (commonModel != null && commonModel.result == 1) {
					account.userName = nickNameStr;
					account.sex = sex;
					account.personDesc = qianmingstr;
					account.saveMeInfoToPreference();
					if (TextUtils.isEmpty(avatarPath)) {
						ToastUtil.makeShortText("设置成功");
						Intent intent = new Intent(MineFragment.RefreshInfoAction);
						HCApplicaton.getInstance().sendBroadcast(intent);
						dismissDialog();
						finish();
					} else {
						uploadImage();
					}
				} else {
					dismissDialog();
					ToastUtil.makeShortText("设置失败");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
			String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
			if (paths != null && paths.length <= 0) {
				return;
			}
			if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
				if (!TextUtils.isEmpty(paths[0])) {
					ActivityCropImage.openForResult(this, paths[0], 750, 750, true, kPhotoCropImageRequest);
					return;
				}
			} else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
				if (!TextUtils.isEmpty(paths[0])) {
					avatarPath = paths[0];
				}
				imageLoader.displayImage(ImageLoaderUtil.fromFile(avatarPath), avatar, ImageLoaderUtil.Options_Common_Disc_Pic);
			}
		} else if (requestCode == kPhotoCropImageRequest && resultCode == RESULT_OK) {
			avatarPath = data.getStringExtra(ActivityCropImage.kCropImagePath);
			imageLoader.displayImage(ImageLoaderUtil.fromFile(avatarPath), avatar, ImageLoaderUtil.Options_Common_Disc_Pic);
			return;
		} else if (requestCode == kNameRequest && resultCode == RESULT_OK) {
			nickName.setText(data.getStringExtra(ActivityModifyStr.kResult));
		} else if (requestCode == kQianMingRequest && resultCode == RESULT_OK) {
			qianMing.setText(data.getStringExtra(ActivityModifyStr.kResult));
		}
	}

	private void uploadImage() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		Request.doFileUploadRequest(this, nameValuePairs, new File(avatarPath), ServerConfig.URL_MODIFY_AVATAR, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("头像设置失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				ModifyAvatarModel modifyAvatarModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, ModifyAvatarModel.class);
				if (modifyAvatarModel != null && !TextUtils.isEmpty(modifyAvatarModel.result)) {
					account.avatar = modifyAvatarModel.result;
					account.saveMeInfoToPreference();
					ToastUtil.makeShortText("设置成功");
					Intent intent = new Intent(MineFragment.RefreshInfoAction);
					HCApplicaton.getInstance().sendBroadcast(intent);
					finish();
				} else {
					ToastUtil.makeShortText("头像设置失败");
				}
			}
		});
	}

	class ModifyAvatarModel {
		@SerializedName("message")
		public String message;
		@SerializedName("result")
		public String result;
	}
}
