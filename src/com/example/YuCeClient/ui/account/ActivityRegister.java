package com.example.YuCeClient.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
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
 * Created by xiaoyuPC on 2015/6/7.
 */
public class ActivityRegister extends ActivityBase implements View.OnClickListener {

	private Account account;
	/**
	 * init views
	 * *
	 */
	private ImageView back;
	private ImageView avatar;
	private EditText nickName;
	private EditText phoneNumber;
	private TextView getVerCode;
	private EditText verificationCode;
	private EditText password;
	private TextView registerNow;
	/**
	 * modify avatar about
	 * *
	 */
	private static final int kActivitySettingSelectPicRequest = 101;
	private static final int kPhotoCropImageRequest = 102;
	private String avatarPath = "";
	private ImageLoader imageLoader;


	private static final int cuteDownTime = 60;//60s
	private int tempCuteDownTime = cuteDownTime;
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if (what == 1) {
				if (msg.arg1 >= 0) {
					getVerCode.setText(msg.arg1 + "s");
					Message message = Message.obtain();
					message.what = 1;
					message.arg1 = --tempCuteDownTime;
					myHandler.sendMessageDelayed(message, 1000);
				} else {
					getVerCode.setClickable(true);
					getVerCode.setText("获取验证码");
				}
			}
		}
	};


	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityRegister.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register_hc);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		registerNow = (TextView) findViewById(R.id.registerNow);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (EditText) findViewById(R.id.nickName);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		getVerCode = (TextView) findViewById(R.id.getVerCode);
		verificationCode = (EditText) findViewById(R.id.verificationCode);
		password = (EditText) findViewById(R.id.password);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		registerNow.setOnClickListener(this);
		getVerCode.setOnClickListener(this);
		avatar.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.registerNow:
			tryToRegister();
			break;
		case R.id.getVerCode:
			tryToGetVerCode();
			break;
		case R.id.avatar:
			Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, this);
			startActivityForResult(intent, kActivitySettingSelectPicRequest);
			break;
		}
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
					/**注册环信**/
					tryToRegisterChatServer();
				} else {
					ToastUtil.makeShortText("头像设置失败");
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		myHandler.removeMessages(1);
		super.onDestroy();
	}

	private void tryToRegister() {
		final String nickNameStr = nickName.getText().toString();
		if (TextUtils.isEmpty(nickNameStr)) {
			ToastUtil.makeShortText("昵称不能为空");
			return;
		}
		final String phoneNumberStr = phoneNumber.getText().toString();
		if (TextUtils.isEmpty(phoneNumberStr)) {
			ToastUtil.makeShortText("电话号码不能为空");
			return;
		}
		if (!CommonUtil.isPhoneNumberValid(phoneNumberStr)) {
			ToastUtil.makeShortText("电话号码格式不对");
			return;
		}

		String verCodeStr = verificationCode.getText().toString();
		if (TextUtils.isEmpty(verCodeStr)) {
			ToastUtil.makeShortText("验证码不能为空");
			return;
		}
		final String pwdStr = password.getText().toString();
		if (TextUtils.isEmpty(pwdStr)) {
			ToastUtil.makeShortText("密码不能为空");
			return;
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("nickname", nickNameStr));
		nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumberStr));
		nameValuePairs.add(new BasicNameValuePair("password", pwdStr));
		nameValuePairs.add(new BasicNameValuePair("activationNumber", verCodeStr));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_REGISTER, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.showErrorMessage(e, "");
			}

			@Override
			public void onComplete(String response) {
				RegisterModel registerModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, RegisterModel.class);
				if (registerModel != null && registerModel.result == 1) {
					account.phoneNumber = phoneNumberStr;
					account.password = pwdStr;
					account.userName = nickNameStr;
					account.userId = registerModel.userId;
					account.saveMeInfoToPreference();
					/**没有头像直接注册环信**/
					if (TextUtils.isEmpty(avatarPath)) {
						/**先不去注册环信**/
						ToastUtil.makeShortText("注册成功");
						finish();
						//						tryToRegisterChatServer();
					} else {
						uploadImage();
					}
				} else {
					if (registerModel == null || TextUtils.isEmpty(registerModel.message)) {
						ToastUtil.makeShortText("注册失败");
					} else {
						ToastUtil.makeShortText(registerModel.message);
					}
				}
			}
		});
	}

	private void tryToRegisterChatServer() {
		final String st7 = getResources().getString(R.string.network_anomalies);
		final String st8 = getResources().getString(R.string.User_already_exists);
		final String st9 = getResources().getString(R.string.registration_failed_without_permission);
		final String st10 = getResources().getString(R.string.Registration_failed);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMChatManager.getInstance().createAccountOnServer(account.userId, account.password);
					runOnUiThread(new Runnable() {
						public void run() {
							HCApplicaton.getInstance().setUserName(account.userId);
							ToastUtil.makeShortText("注册成功");
							finish();
						}
					});
				} catch (final EaseMobException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							int errorCode = e.getErrorCode();
							if (errorCode == EMError.NONETWORK_ERROR) {
								Toast.makeText(getApplicationContext(), st7, Toast.LENGTH_SHORT).show();
							} else if (errorCode == EMError.USER_ALREADY_EXISTS) {
								Toast.makeText(getApplicationContext(), st8, Toast.LENGTH_SHORT).show();
							} else if (errorCode == EMError.UNAUTHORIZED) {
								Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(), st10 + e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		}).start();
	}

	private void tryToGetVerCode() {
		String phoneNumberStr = phoneNumber.getText().toString();
		if (TextUtils.isEmpty(phoneNumberStr)) {
			ToastUtil.makeShortText("电话号码不能为空");
			return;
		}
		if (!CommonUtil.isPhoneNumberValid(phoneNumberStr)) {
			ToastUtil.makeShortText("电话号码不正确");
			return;
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumberStr));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_GET_VER_CODE, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("获取验证码失败");
			}

			@Override
			public void onComplete(String response) {
				CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (commonModel != null && commonModel.result == 1) {
					ToastUtil.makeShortText("验证码已经发送");
					getVerCode.setClickable(false);
					Message message = Message.obtain();
					message.what = 1;
					message.arg1 = tempCuteDownTime;
					myHandler.sendMessage(message);
				} else {
					ToastUtil.makeShortText("获取验证码失败");
				}
			}
		});
	}

	class RegisterModel {
		@SerializedName("message")
		public String message;
		@SerializedName("result")
		public int result;
		@SerializedName("userId")
		public String userId;
	}

	class ModifyAvatarModel {
		@SerializedName("message")
		public String message;
		@SerializedName("result")
		public String result;
	}
}
