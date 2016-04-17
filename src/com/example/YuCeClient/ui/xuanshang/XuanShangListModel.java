package com.example.YuCeClient.ui.xuanshang;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class XuanShangListModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("posts")
	public List<XuanShangItemModel> posts;
}
