package com.example.YuCeClient.ui.yuce_master;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-12-16.
 */
public class MasterCommentListModel {
	@SerializedName("evaluates")
	public List<MasterCommentItemModel> evaluates;
	@SerializedName("message")
	public String message;
	@SerializedName("result")
	public int result;
}
