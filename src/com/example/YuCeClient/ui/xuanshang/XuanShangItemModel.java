package com.example.YuCeClient.ui.xuanshang;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class XuanShangItemModel {
	@SerializedName("content")
	public String content;

	@SerializedName("createTimeMs")
	public String createTimeMs;

	@SerializedName("iscaina")
	public int iscaina;//是否被采纳

	@SerializedName("jinbi")
	public int jinbi;//金币

	@SerializedName("postId")
	public String postId;

	@SerializedName("postImages")
	public List<ImageInfoModel> postImages;

	@SerializedName("postTitle")
	public String postTitle;

	@SerializedName("replyNum")
	public int replyNum;

	@SerializedName("type")
	public String type;

	@SerializedName("userAvatar")
	public String userAvatar;

	@SerializedName("userId")
	public String userId;

	@SerializedName("userName")
	public String userName;

	@SerializedName("userPhoneNum")
	public String userPhoneNum;

}
