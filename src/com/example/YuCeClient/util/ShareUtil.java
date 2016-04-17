package com.example.YuCeClient.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.account.AccessTokenKeeper;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import java.util.ArrayList;

/**
 * Created by xiaoyu on 15-11-25.
 */
public class ShareUtil {
	/**
	 * 分享到朋友圈(文字，图片)*
	 */
	public void shareToWechatCircle(String title, String content, Bitmap bitmap, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}
		Bitmap thumBmp = null;
		if (bitmap != null) {
			thumBmp = BitmapUtil.rotateAndScale(bitmap, 0, 100, false);
		} else {
			thumBmp = BitmapFactory.decodeResource(HCApplicaton.getInstance().getResources(), R.drawable.ic_launcher);
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = content;
		msg.setThumbImage(thumBmp);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到朋友圈(文字，图片)*
	 */
	public void shareToWechatCircle(String title, String content, ImageView imageView, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}

		Bitmap bitmap = ((BitmapDrawable) (imageView.getDrawable())).getBitmap();
		Bitmap thumbBmp = BitmapUtil.rotateAndScale(bitmap, 0, 100, false);

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = content;
		msg.setThumbImage(thumbBmp);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到朋友圈(文字，图片)*
	 */
	public void shareToWechatCircle(String title, String content, int resId, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}

		Bitmap bitmap = BitmapFactory.decodeResource(HCApplicaton.getInstance().getResources(), resId);

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = content;
		msg.setThumbImage(bitmap);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到朋友圈(图片)*
	 */
	public void shareToWechatCircle(Bitmap bitmap) {
		WXImageObject imageObject = new WXImageObject(bitmap);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imageObject;

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到好友(文字，图片)*
	 */
	public void shareToWechatFriend(String title, String content, Bitmap bitmap, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}
		Bitmap thumBmp = null;
		if (bitmap != null) {
			thumBmp = BitmapUtil.rotateAndScale(bitmap, 0, 100, false);
		} else {
			thumBmp = BitmapFactory.decodeResource(HCApplicaton.getInstance().getResources(), R.drawable.ic_launcher);
		}

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = content;
		msg.setThumbImage(thumBmp);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到好友(文字，图片)*
	 */
	public void shareToWechatFriend(String title, String content, ImageView imageView, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}

		Bitmap bitmap = ((BitmapDrawable) (imageView.getDrawable())).getBitmap();
		Bitmap thumbBmp = BitmapUtil.rotateAndScale(bitmap, 0, 100, false);

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = content;
		msg.setThumbImage(thumbBmp);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到好友(文字，图片)*
	 */
	public void shareToWechatFriend(String title, String content, int resId, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}

		Bitmap bitmap = BitmapFactory.decodeResource(HCApplicaton.getInstance().getResources(), resId);

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = content;
		msg.setThumbImage(bitmap);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * 分享到好友(图片)*
	 */
	public void shareToWechatFriend(Bitmap bitmap) {
		WXImageObject imageObject = new WXImageObject();
		imageObject.imageData = BitmapUtil.bitmapToBytes(bitmap);

		WXMediaMessage msg = new WXMediaMessage(imageObject);
		msg.setThumbImage(BitmapUtil.rotateAndScale(bitmap, 0, 150, false));

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		HCApplicaton.getInstance().getIWXAPI().sendReq(req);
	}

	/**
	 * QQ相关分享*
	 */
	public void shareToQQZoneBitmapID(Activity activity, String title, String content, int resID, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		ArrayList<String> urls = new ArrayList<String>();
		params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, urls);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "算命大师");
		HCApplicaton.getInstance().getTencent().shareToQzone(activity, params, new QQUIListener());
	}

	public void shareToQQZoneUrl(Activity activity, String title, String content, String imgUrl, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		ArrayList<String> urls = new ArrayList<String>();
		if (!TextUtils.isEmpty(imgUrl.trim())) {
			urls.add(imgUrl);
		}
		params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, urls);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "算命大师");
		HCApplicaton.getInstance().getTencent().shareToQzone(activity, params, new QQUIListener());
	}

	public void shareToQQBitmapID(Activity activity, String title, String content, int resID, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "算命大师");
		HCApplicaton.getInstance().getTencent().shareToQQ(activity, params, new QQUIListener());
	}

	public void shareToQQUrl(Activity activity, String title, String content, String imgUrl, String shareUrl) {
		if (TextUtils.isEmpty(title)) {
			title = "算命大师";
		}
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		if (!TextUtils.isEmpty(imgUrl.trim())) {
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
		}
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "算命大师");
		HCApplicaton.getInstance().getTencent().shareToQQ(activity, params, new QQUIListener());
	}

	/**
	 * 新浪微博分享*
	 */
	public void shareToSina(final Activity activity, String content, Bitmap bitmap, String shareUrl) {
		IWeiboShareAPI mWeiboShareAPI = HCApplicaton.getInstance().getIWeiboShareAPI();
		/**看有没有安装客户端**/
		if (mWeiboShareAPI.isWeiboAppInstalled()) {
			if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
				int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
				if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
					sendMultiMessage(activity, content, bitmap, shareUrl);
				} else {
					ToastUtil.makeShortText("当前新浪微博客户端版本不支持这种分享方式，请更新客户端");
				}
			} else {
				ToastUtil.makeShortText("请安装客户端或者安装官方版本的客户端");
			}
		}
	}

	private void sendMultiMessage(final Activity activity, String content, Bitmap bitmap, String shareUrl) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		TextObject textObject = new TextObject();
		textObject.text = content;
		weiboMessage.textObject = textObject;
		ImageObject imageObject = new ImageObject();
		//设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		imageObject.setImageObject(bitmap);
		weiboMessage.imageObject = imageObject;
		// 网页
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = "算命大师";
		mediaObject.description = "算命大师";
		Bitmap webBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
		// 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		mediaObject.setThumbImage(webBitmap);
		mediaObject.actionUrl = shareUrl;
		mediaObject.defaultText = "算命大师";
		weiboMessage.mediaObject = mediaObject;
		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		IWeiboShareAPI mWeiboShareAPI = HCApplicaton.getInstance().getIWeiboShareAPI();
		if (mWeiboShareAPI.isWeiboAppInstalled()) {
			mWeiboShareAPI.sendRequest(activity, request);
		} else {
			AuthInfo authInfo = HCApplicaton.getInstance().getAuthInfo();
			Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
			String token = "";
			if (accessToken != null) {
				token = accessToken.getToken();
			}
			mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

				@Override
				public void onWeiboException(WeiboException arg0) {
				}

				@Override
				public void onComplete(Bundle bundle) {
					Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
					AccessTokenKeeper.writeAccessToken(activity, newToken);
				}

				@Override
				public void onCancel() {
				}
			});
		}
	}
}
