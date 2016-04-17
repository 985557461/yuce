package com.example.YuCeClient.ui.circle.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreay on 15-11-22.
 */
public class ActivityPostReply extends ActivityBase implements View.OnClickListener {
	private EditText replyContent;
	private TextView submit;

	private Account account;

	public static final String kToppicId = "top_id";
	public static final String kCommunityId = "community_id";
	public static final String kReplyName = "reply_name";

	private String topicId;
	private String communityId;
	private String replyName;

	public static void openForResult(Activity activity, String topId, String communityId, String replyName, int requestCode) {
		Intent intent = new Intent(activity, ActivityPostReply.class);
		intent.putExtra(kToppicId, topId);
		intent.putExtra(kCommunityId, communityId);
		intent.putExtra(kReplyName, replyName);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		topicId = getIntent().getStringExtra(kToppicId);
		communityId = getIntent().getStringExtra(kCommunityId);
		replyName = getIntent().getStringExtra(kReplyName);
		setContentView(R.layout.activity_post_reply);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		replyContent = (EditText) findViewById(R.id.replyContent);
		submit = (TextView) findViewById(R.id.submit);
	}

	@Override
	protected void initViews() {
		if (TextUtils.isEmpty(replyName)) {
			replyContent.setHint("说点什么呢");
		} else {
			replyContent.setHint("回复 " + replyName + " :");
		}
	}

	@Override
	protected void setListeners() {
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.submit) {
			tryToReply();
		}
	}

	public void tryToReply() {
		showDialog();
		final String content = replyContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.makeShortText("回复内容不能为空");
			return;
		}
		JSONObject contentJson = new JSONObject();
		try {
			contentJson.put("type", "text");
			contentJson.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(contentJson);
		String jsonContent = jsonArray.toString();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", topicId));
		nameValuePairs.add(new BasicNameValuePair("communityid", communityId));
		nameValuePairs.add(new BasicNameValuePair("content", jsonContent));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_REPLY_TOPIC, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("回复失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				ReplyModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, ReplyModel.class);
				if (model != null && model.result == 1) {
					setResult(RESULT_OK);
					finish();
				} else {
					ToastUtil.makeShortText("评论失败");
				}
			}
		});
	}

	class ReplyModel {
		@SerializedName("message")
		public String message;

		@SerializedName("result")
		public int result;
	}
}
