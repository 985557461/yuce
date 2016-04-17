package com.example.YuCeClient.pay;

import com.meilishuo.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 15-11-26.
 */

/**
 * 微信支付需要调用服务器协议，生成订单的信息的model*
 */
public class WeChatPayModel {
	@SerializedName("appid")
	public String appid;

	@SerializedName("partnerid")
	public String partnerid;

	@SerializedName("prepayid")
	public String prepayid;

	@SerializedName("noncestr")
	public String noncestr;

	@SerializedName("timestamp")
	public String timestamp;

	@SerializedName("PACKAGE")
	public String PACKAGE;

	@SerializedName("sign")
	public String sign;
}
