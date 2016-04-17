package com.example.YuCeClient.ui.circle;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sreay on 15-11-19.
 */
public class PostListModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("posts")
	public List<CircleItemModel> posts;
}
