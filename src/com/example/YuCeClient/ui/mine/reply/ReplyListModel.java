package com.example.YuCeClient.ui.mine.reply;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class ReplyListModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("myReply")
	public List<ReplyItemModel> myReply;
}
