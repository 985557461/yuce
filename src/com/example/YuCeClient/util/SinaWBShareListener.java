package com.example.YuCeClient.util;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by xiaoyu on 15-11-25.
 */
public class SinaWBShareListener implements IWeiboHandler.Response {
	@Override
	public void onResponse(BaseResponse baseResponse) {
		switch (baseResponse.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			ToastUtil.makeShortText("分享成功");
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			ToastUtil.makeShortText("分享取消");
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			ToastUtil.makeShortText("分享失败");
			break;
		}
	}
}
