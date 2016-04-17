package com.example.YuCeClient.ui.account;

import com.meilishuo.gson.annotations.SerializedName;

/**
 * Created by sreay on 15-11-24.
 */
public class WeChatLoginModel {
	@SerializedName("access_token")
	public String access_token;

	@SerializedName("expires_in")
	public String expires_in;

	@SerializedName("refresh_token")
	public String refresh_token;

	@SerializedName("openid")
	public String openid;

	@SerializedName("scope")
	public String scope;
}
