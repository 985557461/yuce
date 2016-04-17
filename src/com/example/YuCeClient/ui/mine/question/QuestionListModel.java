package com.example.YuCeClient.ui.mine.question;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class QuestionListModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("myQuestion")
	public List<QuestionItemModel> myQuestion;
}
