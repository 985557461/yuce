package com.example.YuCeClient.ui.circle;

import com.example.YuCeClient.ui.xuanshang.ImageInfoModel;
import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-6.
 */
public class CircleItemModel {
	//发布的时间,毫秒数
	@SerializedName("createTimeMs")
	public String createTimeMs;
	//标题
	@SerializedName("postTitle")
	public String postTitle;
	//内容
	@SerializedName("content")
	public String content;
	//回复数
	@SerializedName("replyNum")
	public int replyNum;
	//用户ID
	@SerializedName("userId")
	public String userId;
	//昵称
	@SerializedName("userName")
	public String userName;
	//头像
	@SerializedName("userAvatar")
	public String userAvatar;
	//用户手机号
	@SerializedName("userPhoneNum")
	public String userPhoneNum;

	@SerializedName("type")
	public String type;

	//帖子id
	@SerializedName("postId")
	public String postId;

	@SerializedName("postImages")
	public List<ImageInfoModel> postImages;

	/**
	 * 下面这三个字段是我自己加的，需要后台返回*
	 */
	@SerializedName("collectionNum")
	public int collectionNum;

	@SerializedName("shareCount")
	public int shareCount;

	@SerializedName("likeCount")
	public int likeCount;
	/**
	 * 自己加的字段结束*
	 */
}
