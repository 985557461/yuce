package com.example.YuCeClient.ui.account;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.ThirdPlantConstant;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.chat.chatuidemo.Constant;
import com.example.YuCeClient.chat.chatuidemo.db.UserDao;
import com.example.YuCeClient.chat.chatuidemo.domain.User;
import com.example.YuCeClient.chat.chatuidemo.utils.CommonUtils;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.ActivityMain;
import com.example.YuCeClient.util.Debug;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.meilishuo.gson.annotations.SerializedName;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyuPC on 2015/6/7.
 */
public class ActivityLogin extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private TextView register;
	private EditText phoneNumber;
	private EditText password;
	private TextView login;
	private TextView forgetPwd;
	private ImageView weiXinLogin;
	private ImageView qqLogin;
	private ImageView sinaLogin;

	private Account account;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityLogin.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		account = HCApplicaton.getInstance().getAccount();
		setContentView(R.layout.activity_login_hc);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		if (!TextUtils.isEmpty(account.userId)) {
			ActivityMain.open(this);
			this.finish();
		}
		back = (ImageView) findViewById(R.id.back);
		register = (TextView) findViewById(R.id.register);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		password = (EditText) findViewById(R.id.password);
		login = (TextView) findViewById(R.id.login);
		forgetPwd = (TextView) findViewById(R.id.forgetPwd);
		weiXinLogin = (ImageView) findViewById(R.id.weiXinLogin);
		qqLogin = (ImageView) findViewById(R.id.qqLogin);
		sinaLogin = (ImageView) findViewById(R.id.sinaLogin);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
		weiXinLogin.setOnClickListener(this);
		qqLogin.setOnClickListener(this);
		sinaLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backFL:
			finish();
			break;
		case R.id.register:
			ActivityRegister.open(this);
			break;
		case R.id.login:
			tryToLogin();
			break;
		case R.id.forgetPwd:
			break;
		case R.id.weiXinLogin:
			break;
		case R.id.qqLogin:
			break;
		case R.id.sinaLogin:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 新浪登录*
	 */
	/**
	 * SSO 授权认证实例
	 */
	private SsoHandler mSsoHandler;
	/**
	 * 登陆认证对应的listener
	 */
	private AuthListener mLoginListener = new AuthListener();

	private void sinaLogin() {
		AuthInfo mAuthInfo = HCApplicaton.getInstance().getAuthInfo();
		if (mSsoHandler == null && mAuthInfo != null) {
			mSsoHandler = new SsoHandler(this, mAuthInfo);
		}
		if (mSsoHandler != null) {
			mSsoHandler.authorize(mLoginListener);
		} else {
			Debug.debug("xiaoyu", "Please setWeiboAuthInfo(...) for first");
		}
	}

	/**
	 * 登入按钮的监听器，接收授权结果。
	 */
	private class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
			if (accessToken != null && accessToken.isSessionValid()) {
				accessToken.getToken();
				AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ToastUtil.makeShortText("登录失败");
		}

		@Override
		public void onCancel() {
			ToastUtil.makeShortText("取消登录");
		}
	}

	/**
	 * QQ登录*
	 */
	private void qqLogin() {
		Tencent tencent = HCApplicaton.getInstance().getTencent();
		tencent.login(ActivityLogin.this, "all", new BaseUiListener());
	}

	/**
	 * 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法，
	 * onComplete  onCancel onError
	 * 分别表示第三方登录成功，取消 ，错误。
	 */
	private class BaseUiListener implements IUiListener {
		public void onCancel() {
		}

		public void onComplete(Object response) {
			ToastUtil.makeShortText("登录成功");
			try {
				String openidString = ((JSONObject) response).getString("openid");
				String access_token = ((JSONObject) response).getString("access_token");
				String expires_in = ((JSONObject) response).getString("expires_in");
				/**根据这些信息，把信息传递给服务器，让服务器给你返回头像，昵称**/
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void onError(UiError error) {

		}
	}

	/**
	 * 微信登录*
	 */
	private void weiXinLogin() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		IWXAPI iwxapi = HCApplicaton.getInstance().getIWXAPI();
		if (iwxapi != null) {
			HCApplicaton.getInstance().getIWXAPI().sendReq(req);
		}
	}

	private WeChatLoginBroastCast mWeChatLoginBroastCast;

	private void registerBroastCast() {
		mWeChatLoginBroastCast = new WeChatLoginBroastCast();
		IntentFilter filter = new IntentFilter(ThirdPlantConstant.ACTION_lOGIN_WX_SUCCEED);
		this.registerReceiver(mWeChatLoginBroastCast, filter);
	}


	private void unRegisterBroastCast() {
		this.unregisterReceiver(mWeChatLoginBroastCast);
	}

	class WeChatLoginBroastCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String code = intent.getStringExtra("weixinlogin");
			getWxToken(code);
		}
	}

	public void getWxToken(String code) {
		showDialog();
		String url = ThirdPlantConstant.wxTokenUrl + "appid=" + ThirdPlantConstant.WX_APP_ID + "&" + "secret=" + ThirdPlantConstant.WX_App_secret + "&" + "code=" + code + "&" + "grant_type=authorization_code";
		Request.doRequestSpecial(this, url, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.showErrorMessage(e, "获取微信登陆Token失败");
			}

			@Override
			public void onComplete(String response) {
				loginWXThrid(response);
			}
		});
	}

	public void loginWXThrid(String response) {
		WeChatLoginModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, WeChatLoginModel.class);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wx_access_token", model.access_token));
		nameValuePairs.add(new BasicNameValuePair("open_id", model.openid));
		nameValuePairs.add(new BasicNameValuePair("refresh_token", model.refresh_token));
		nameValuePairs.add(new BasicNameValuePair("expires_in", model.expires_in));
		nameValuePairs.add(new BasicNameValuePair("connect_type", "1"));

		Request.doRequest(this, nameValuePairs, ServerConfig.URL_LOGIN_THRID, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.showErrorMessage(e, "微信登录失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				loginSuccess(response);
			}
		});
	}

	private void loginSuccess(String response) {
		/**服务器返回用户的信息，存储到本地再进行一些初始化的工作**/
		/**注册环信，登录环信**/
	}

	private void tryToLogin() {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		final String phoneNumberStr = phoneNumber.getText().toString();
		if (TextUtils.isEmpty(phoneNumberStr)) {
			ToastUtil.makeShortText("电话号码不能为空");
			return;
		}
		final String pwdStr = password.getText().toString();
		if (TextUtils.isEmpty(pwdStr)) {
			ToastUtil.makeShortText("密码不能为空");
			return;
		}
		showDialog("正在登录");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("phoneNum", phoneNumberStr));
		nameValuePairs.add(new BasicNameValuePair("password", pwdStr));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_LOGIN, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("登录失败");
			}

			@Override
			public void onComplete(String response) {
				LoginModel loginModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, LoginModel.class);
				if (loginModel != null && loginModel.result == 1) {
					account.phoneNumber = phoneNumberStr;
					account.password = pwdStr;
					account.userId = loginModel.user_id;
					account.avatar = loginModel.imageurl;
					account.caiNaCount = loginModel.cainashu;
					account.jinBiCount = loginModel.jinbi;
					account.level = loginModel.level;
					account.levelNickName = loginModel.levelname;
					account.sex = loginModel.sex;
					account.personDesc = loginModel.qianming;
					account.userType = loginModel.usertype;
					account.saveMeInfoToPreference();
					ToastUtil.makeShortText("登陆成功");
					startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
					finish();
					/**先不去登录环信**/
					//					toLoginChatServer();
				} else {
					dismissDialog();
					if (loginModel == null || TextUtils.isEmpty(loginModel.message)) {
						ToastUtil.makeShortText("登录失败");
					} else {
						ToastUtil.makeShortText(loginModel.message);
					}
				}
			}
		});
	}

	private void toLoginChatServer() {
		EMChatManager.getInstance().login(account.userId, account.password, new EMCallBack() {
			@Override
			public void onSuccess() {
				HCApplicaton.getInstance().setUserName(account.userId);
				HCApplicaton.getInstance().setPassword(account.password);

				try {
					EMChatManager.getInstance().loadAllLocalGroups();
					EMChatManager.getInstance().loadAllConversations();
					processContactsAndGroups();
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							HCApplicaton.getInstance().logout(null);
							Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
						HCApplicaton.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				dismissDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToastUtil.makeShortText("登录成功");
					}
				});
				startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
				finish();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				runOnUiThread(new Runnable() {
					public void run() {
						dismissDialog();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void processContactsAndGroups() throws EaseMobException {
		List<String> usernames = EMContactManager.getInstance().getContactUserNames();
		EMLog.d("roster", "contacts size: " + usernames.size());
		Map<String, User> userlist = new HashMap<String, User>();
		for (String username : usernames) {
			User user = new User();
			user.setUsername(username);
			setUserHearder(username, user);
			userlist.put(username, user);
		}

		User addFriends = new User();
		String addFriendStr = getString(R.string.add_friend);
		addFriends.setUsername(Constant.ADD_FRIEND);
		addFriends.setNick(addFriendStr);
		addFriends.setHeader("");
		userlist.put(Constant.ADD_FRIEND, addFriends);

		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getString(R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);

		User groupUser = new User();
		String strGroup = getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);

		User chatRoomItem = new User();
		String strChatRoom = getString(R.string.chat_room);
		chatRoomItem.setUsername(Constant.CHAT_ROOM);
		chatRoomItem.setNick(strChatRoom);
		chatRoomItem.setHeader("");
		userlist.put(Constant.CHAT_ROOM, chatRoomItem);

		HCApplicaton.getInstance().setContactList(userlist);
		System.out.println("----------------" + userlist.values().toString());
		UserDao dao = new UserDao(ActivityLogin.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);

		List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
		EMContactManager.getInstance().saveBlackList(blackList);

		EMChatManager.getInstance().fetchJoinedGroupsFromServer();
	}

	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
					.toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	class LoginModel {
		@SerializedName("message")
		public String message;
		@SerializedName("result")
		public int result;
		@SerializedName("nickname")
		public String nickname;
		@SerializedName("problem_num")
		public String problem_num;
		@SerializedName("news_num")
		public String news_num;
		@SerializedName("user_id")
		public String user_id;
		@SerializedName("imageurl")
		public String imageurl;
		@SerializedName("cainashu")
		public int cainashu;
		@SerializedName("jinbi")
		public int jinbi;
		@SerializedName("level")
		public String level;
		@SerializedName("levelname")
		public String levelname;
		@SerializedName("qianming")
		public String qianming;
		@SerializedName("sex")
		public int sex;
		@SerializedName("usertype")
		public int usertype;
	}
}
