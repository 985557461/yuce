package com.example.YuCeClient.ui.circle.post;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2015/7/4.
 */
public class PostDescModel {
	@SerializedName("communityid")
	public String communityid;

	@SerializedName("createTimeMs")
	public String createTimeMs;

	@SerializedName("message")
	public String message;

	@SerializedName("postTitle")
	public String postTitle;

	@SerializedName("postContent")
	public String postContent;

	@SerializedName("postFloorAvatar")
	public String postFloorAvatar;

	/**楼主id**/
	@SerializedName("postFloorId")
	public String postFloorId;

	@SerializedName("postFloorName")
	public String postFloorName;

	@SerializedName("postFloorPhoneNum")
	public String postFloorPhoneNum;

	@SerializedName("postHtmlContent")
	public String postHtmlContent;

	@SerializedName("postImages")
	public List<String> postImages;

    @SerializedName("replyNum")
    public String replyNum;

    @SerializedName("replyPosts")
    public List<CommentModel> replyPosts;

    @SerializedName("result")
    public int result;

	@SerializedName("shareimage")
	public String shareimage;

	@SerializedName("shareurl")
	public String shareurl;
}
