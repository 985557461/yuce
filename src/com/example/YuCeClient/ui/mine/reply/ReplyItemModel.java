package com.example.YuCeClient.ui.mine.reply;


import com.meilishuo.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class ReplyItemModel {
	@SerializedName("communityId")
	public String communityId;

	@SerializedName("communityName")
	public String communityName;

	@SerializedName("createTimeMs")
	public String createTimeMs;

	@SerializedName("replyContent")
	public String replyContent;

	@SerializedName("postId")
	public String postId;

	@SerializedName("replyid")
	public String replyid;

	@SerializedName("postTitle")
	public String postTitle;

	@SerializedName("userAvatar")
	public String userAvatar;

	@SerializedName("userId")
	public String userId;
	@SerializedName("userName")
	public String userName;
	@SerializedName("userPhoneNum")
	public String userPhoneNum;
}
