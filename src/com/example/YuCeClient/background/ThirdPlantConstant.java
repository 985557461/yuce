package com.example.YuCeClient.background;

/**
 * Created by xiaoyu on 15-11-24.
 */
public class ThirdPlantConstant {
	/**微信的相关变量**/
	/**
	 * 微信登录*
	 */
	public static final String ACTION_lOGIN_WX_SUCCEED = "com.meilishuo.higo.action.ACTION_LOGIN_WX_SUCCEED";
	public static String WX_App_secret = "";
	public static final String WX_APP_ID = "";
	public static final String wxTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?";

	/**
	 * 腾讯的相关变量*
	 */
	public static String TX_App_secret = "";
	public static final String TX_APP_ID = "";

	/**
	 * 新浪微博的相关变量*
	 */
	public static String Sina_App_secret = "";
	public static final String Sina_APP_ID = "";
	public static final String REDIRECT_URL = "http://www.sina.com";
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";
}
