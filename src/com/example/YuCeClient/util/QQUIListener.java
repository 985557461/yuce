package com.example.YuCeClient.util;

import android.os.Handler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by xiaoyu on 15-11-25.
 */
public class QQUIListener implements IUiListener {
	private static Handler handler = new Handler();

	@Override
	public void onComplete(Object o) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				ToastUtil.makeShortText("分享成功");
			}
		});
	}

	@Override
	public void onError(UiError uiError) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				ToastUtil.makeShortText("分享失败");
			}
		});
	}

	@Override
	public void onCancel() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				ToastUtil.makeShortText("分享取消");
			}
		});
	}
}
