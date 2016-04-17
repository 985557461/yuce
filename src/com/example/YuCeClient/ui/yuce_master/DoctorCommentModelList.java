package com.example.YuCeClient.ui.yuce_master;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-12-7.
 */
public class DoctorCommentModelList {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("evaluates")
	public List<DoctorCommentItemModel> evaluates;
}
