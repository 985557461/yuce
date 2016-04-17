package com.example.YuCeClient.wxapi;

/**
 * Created by xiaoyu on 15-11-24.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.example.YuCeClient.background.ThirdPlantConstant;
import com.example.YuCeClient.util.Debug;
import com.example.YuCeClient.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;
	public static String ShareCompleteAction = "share_complete_action";
	public static String ShareFiledAction = "share_filed_action";
	public static String ShareCancelAction = "share_cancel_action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, ThirdPlantConstant.WX_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp baseResp) {
		if (baseResp instanceof SendAuth.Resp) {
			switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				String code = ((SendAuth.Resp) baseResp).code;
				Debug.debug("成功：   ===    code =   ”" + code);
				sendBroadcast(code);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				ToastUtil.makeShortText("您取消了授权");
				break;
			}
			finish();
		} else {
			switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ShareCompleteAction));
				ToastUtil.makeShortText("分享成功");
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ShareCancelAction));
				ToastUtil.makeShortText("分享取消");
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ShareFiledAction));
				ToastUtil.makeShortText("分享拒绝");
				break;
			}
			finish();
		}
	}

	private void sendBroadcast(String code) {
		Intent intent = new Intent();
		intent.putExtra("weixinlogin", code);
		intent.setAction(ThirdPlantConstant.ACTION_lOGIN_WX_SUCCEED);
		sendBroadcast(intent);
	}

	//	//	/**
	//	 * 处理微信发出的向第三方应用请求app message
	//	 * <p>
	//	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	//	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	//	 * 做点其他的事情，包括根本不打开任何页面
	//	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
		startActivity(iLaunchMyself);
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p/>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p/>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}
}
