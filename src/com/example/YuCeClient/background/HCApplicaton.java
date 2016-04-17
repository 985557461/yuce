package com.example.YuCeClient.background;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.bmob.pay.tool.BmobPay;
import com.easemob.EMCallBack;
import com.example.YuCeClient.background.db.DBHelper;
import com.example.YuCeClient.chat.chatuidemo.DemoHXSDKHelper;
import com.example.YuCeClient.chat.chatuidemo.domain.User;
import com.meilishuo.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.util.Map;

/**
 * Created by xiaoyuPC on 2015/6/5.
 */
public class HCApplicaton extends Application {
	private static HCApplicaton mApplication;
	public static boolean android_show_filter = false;
	private ImageLoader imageLoader;
	private Account account;
	private Gson gson;
	private SQLiteDatabase db;

	// login user name
	public final String PREF_USERNAME = "username";
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	/**
	 * 微信*
	 */
	private static IWXAPI api;

	/**
	 * 腾讯*
	 */
	private static Tencent mTencent;

	/**新浪**/
	/**
	 * 授权认证所需要的信息
	 */
	private static AuthInfo mAuthInfo;

	/**
	 * 微博微博分享接口实例
	 */
	private static IWeiboShareAPI mWeiboShareAPI = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		BmobPay.init(this, "837d6fed8e43c510a45fb310eb4efe20");
		initImageLoader(getApplicationContext());

		/**
		 * this function will initialize the HuanXin SDK
		 *
		 * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
		 *
		 * 环信初始化SDK帮助函数
		 * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
		 *
		 * for example:
		 * 例子：
		 *
		 * public class DemoHXSDKHelper extends HXSDKHelper
		 *
		 * HXHelper = new DemoHXSDKHelper();
		 * if(HXHelper.onInit(context)){
		 *     // do HuanXin related work
		 * }
		 */
		hxSDKHelper.onInit(mApplication);

		//注册微信
		api = WXAPIFactory.createWXAPI(this, ThirdPlantConstant.WX_APP_ID, true);
		api.registerApp(ThirdPlantConstant.WX_APP_ID);
		/**初始化腾讯**/
		mTencent = Tencent.createInstance(ThirdPlantConstant.TX_APP_ID, this);
		/**初始化新浪**/
		// 创建授权认证信息
		mAuthInfo = new AuthInfo(this, ThirdPlantConstant.Sina_APP_ID, ThirdPlantConstant.REDIRECT_URL, ThirdPlantConstant.SCOPE);
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ThirdPlantConstant.Sina_APP_ID);

		// 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
		// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
		// NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
		mWeiboShareAPI.registerApp();
	}

	public SQLiteDatabase getDB() {
		if (db == null) {
			synchronized (DBHelper.class) {
				if (db == null) {
					DBHelper dbHelper = new DBHelper(this, "suanming");
					db = dbHelper.getWritableDatabase();
				}
			}
		}
		return db;
	}


	public static HCApplicaton getInstance() {
		return mApplication;
	}

	public static IWXAPI getIWXAPI() {
		return api;
	}

	public static Tencent getTencent() {
		return mTencent;
	}

	public static AuthInfo getAuthInfo() {
		return mAuthInfo;
	}

	public static IWeiboShareAPI getIWeiboShareAPI() {
		return mWeiboShareAPI;
	}

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}

	public Account getAccount() {
		if (account == null) {
			account = Account.loadAccount();
		}
		return account;
	}

	public Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	public ImageLoader getImageLoader() {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
		}
		return imageLoader;
	}

	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).discCache(new UnlimitedDiscCache(PathManager.getImageLoaderCacheDir()))// 自定义缓存路径
				.tasksProcessingOrder(QueueProcessingType.LIFO).memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13).writeDebugLogs() // Remove for release app
				.threadPoolSize(3).imageDownloader(new BaseImageDownloader(context)).memoryCache(new WeakMemoryCache()).diskCacheSize(40 * 1024 * 1024).defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.build();
		ImageLoader.getInstance().init(config);
	}
}
