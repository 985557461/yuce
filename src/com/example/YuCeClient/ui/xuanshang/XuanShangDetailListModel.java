package com.example.YuCeClient.ui.xuanshang;

import com.example.YuCeClient.ui.circle.post.CommentModel;
import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-12-2.
 */
public class XuanShangDetailListModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("chusheng")
	public String chusheng;

	@SerializedName("chushengdi")
	public String chushengdi;

	@SerializedName("communityid")
	public String communityid;

	@SerializedName("createTimeMs")
	public String createTimeMs;

	@SerializedName("iscaina")
	public int iscaina;//是否被采纳

	@SerializedName("jinbi")
	public String jinbi;

	@SerializedName("postContent")
	public String postContent;

	@SerializedName("postFloorAvatar")
	public String postFloorAvatar;

	@SerializedName("postFloorId")
	public String postFloorId;

	@SerializedName("postFloorName")
	public String postFloorName;

	@SerializedName("postFloorPhoneNum")
	public String postFloorPhoneNum;

	@SerializedName("postHtmlContent")
	public String postHtmlContent;

	@SerializedName("postImages")
	public List<ImageInfoModel> postImages;

	@SerializedName("postTitle")
	public String postTitle;

	@SerializedName("replyNum")
	public int replyNum;

	@SerializedName("readcount")
	public int readcount;

	@SerializedName("replyPosts")
	public List<CommentModel> replyPosts;

	@SerializedName("shareimage")
	public String shareimage;

	@SerializedName("shareurl")
	public String shareurl;

	@SerializedName("xingbie")
	public String xingbie;

	@SerializedName("xingming")
	public String xingming;
}
