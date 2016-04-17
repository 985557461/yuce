package com.example.YuCeClient.ui.yuce_master;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class YuCePeopleListModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("doctorinfo")
	public List<YuCePeopleModel> doctorinfo;
}
