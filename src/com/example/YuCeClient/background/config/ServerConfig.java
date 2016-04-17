package com.example.YuCeClient.background.config;

/**
 * Created by sreay on 14-8-18.
 */
public class ServerConfig {
	// 测试环境
	public static String BASE_URL_TEST = "http://182.92.227.113/";
	// 正式环境
	public static String BASE_URL_OFFICAL = "http://182.92.227.113/";

	public static String BASE_URL = BASE_URL_OFFICAL;

	public static void initUrl(boolean boo) {
		if (boo) {
			ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
		} else {
			ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
		}
	}

	public static final String URL_GET_PAIPAN_DATA = "qimen/api/yuce/qimenpaipan.do";
	public static final String URL_YUCE_XIANGYI = "qimen/api/yuce/xiangyi.do";
	public static final String URL_YUCE_XMPAIPAN = "qimen/api/yuce/xmpaipan.do";
	public static final String URL_ALL_COMMUNITY = "qimen/api/account/queryTopic.do";
	public static final String URL_PUB_TOPIC = "qimen/api/account/pubTopic.do";
	public static final String URL_REGISTER = "qimen/api/accounts/activate.do";
	public static final String URL_GET_VER_CODE = "qimen/api/accounts/register.do";
	public static final String URL_LOGIN = "qimen/api/accounts/login.do";
	public static final String URL_TOPIC_DETAIL = "qimen/api/account/topicDetail.do";
	public static final String URL_REPLY_TOPIC = "qimen/api/account/replyTopic.do";
	public static final String URL_REPLY_DETAIL = "qimen/api/account/replyDetail.do";
	public static final String URL_MODIFY_AVATAR = "qimen/api/accounts/setImage2.do";
	public static final String URL_LOGIN_THRID = "xxxx";
	public static final String URL_WECHAT_PAY = "xxxx";
	public static final String URL_YINLIAN_PAY = "xxxxx";
	public static final String URL_XUAN_SHANG_LIST = "qimen/api/xuanshang/queryTopic.do";
	public static final String URL_PUB_XUAN_SHANG_TOPIC = "qimen/api/xuanshang/pubTopic.do";
	public static final String URL_UPLOAD_IMAGE = "qimen/api/files/upload.do";
	public static final String URL_XUAN_SHANG_TOPIC_DETAIL = "qimen/api/xuanshang/topicDetail.do";
	public static final String URL_XUAN_SHANG_REPLY_TOPIC = "qimen/api/xuanshang/replyTopic.do";
	public static final String URL_XUAN_SHANG_CAINA = "qimen/api/xuanshang/caina.do";
	public static final String URL_XUAN_SHANG_DASHANG = "qimen/api/xuanshang/dashang.do";
	public static final String URL_DOCTOR_LIST = "qimen/api/accounts/doctorlist.do";
	public static final String URL_APP_PRAISE = "qimen/api/export/appraise.do";
	public static final String URL_EVALUATE_HISTORY = "qimen/api/export/evaluate_history.do";
	public static final String URL_DOCTOR_DETAIL = "qimen/api/accounts/docdetail.do";
	public static final String URL_XUANSHANG_MY_TOPIC = "qimen/api/xuanshang/myTopic.do";
	public static final String URL_XUANSHANG_MY_REPLY = "qimen/api/xuanshang/myReply.do";
	public static final String URL_SETUSER_INFO = "qimen/api/accounts/setUserinfo.do";
	public static final String URL_XUANSHANG_PRAISE = "qimen/api/xuanshang/praise.do";
	public static final String URL_MASTER_COMMENTS = "qimen/api/export/evaluate_list.do";
	public static final String URL_ACCOUNTS_GET_JINBI = "qimen/api/accounts/getJinbi.do";
	public static final String URL_MASTER_SHENQING = "qimen/api/accounts/shenqing.do";
	public static final String URL_ADD_JINBI = "qimen/api/account/chongzhi.do";
	public static final String URL_TIXIAN = "qimen/api/account/tixian.do";
}
