package com.example.YuCeClient.ui.mine.question;


import com.example.YuCeClient.ui.xuanshang.ImageInfoModel;
import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class QuestionItemModel {
	@SerializedName("communityId")
	public String communityId;

	@SerializedName("communityName")
	public String communityName;

	@SerializedName("createTimeMs")
	public String createTimeMs;

	@SerializedName("postContent")
	public String postContent;

	@SerializedName("postId")
	public String postId;

	@SerializedName("postImage")
	public List<ImageInfoModel> postImage;

	@SerializedName("postTitle")
	public String postTitle;
}
