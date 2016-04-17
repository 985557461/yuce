package com.example.YuCeClient.ui.circle.post;

import com.meilishuo.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 2015/7/4.
 */
public class CommentModel {
    @SerializedName("communityid")
    public String communityid;

    @SerializedName("content")
    public String content;

    @SerializedName("createTimeMs")
    public String createTimeMs;

	@SerializedName("isPraise")
	public String isPraise;

	@SerializedName("likeNum")
	public String likeNum;

	@SerializedName("replyNum")
	public String replyNum;

	@SerializedName("replyPostId")
	public String replyPostId;

    @SerializedName("userAvatar")
    public String userAvatar;

	@SerializedName("userId")
	public String userId;

    @SerializedName("userName")
    public String userName;

	@SerializedName("userPhoneNum")
	public String userPhoneNum;
}
