package com.example.YuCeClient.ui.yuce_master;

import com.meilishuo.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 15-12-7.
 */
public class DoctorCommentItemModel {
	@SerializedName("content")
	public String content;

	@SerializedName("date")
	public String date;

	@SerializedName("nickname")
	public String nickname;

	@SerializedName("phoneNum")
	public String phoneNum;

	@SerializedName("score")
	public int score;

	@SerializedName("userAvatar")
	public String userAvatar;

	@SerializedName("userid")
	public String userid;
}
