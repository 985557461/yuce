package com.example.YuCeClient.wxapi;

import android.content.Intent;
import android.os.Bundle;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by xiaoyu on 15-11-26.
 */
public class WXPayEntryActivity extends ActivityBase implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_wechat_pay_result);
		super.onCreate(savedInstanceState);
		api = HCApplicaton.getInstance().getIWXAPI();
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void getViews() {

	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp baseResp) {
		if (baseResp instanceof PayResp) {
			switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				ToastUtil.makeShortText("支付取消");
				break;
			case BaseResp.ErrCode.ERR_OK:
				ToastUtil.makeShortText("支付成功");
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				ToastUtil.makeShortText("支付失败");
				break;
			}
		}
		finish();
	}
}
