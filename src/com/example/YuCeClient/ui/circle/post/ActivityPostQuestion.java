package com.example.YuCeClient.ui.circle.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.circle.Actions;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.HCPopListView;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2015/7/4.
 */
public class ActivityPostQuestion extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private TextView postQuestion;
	private EditText title;
	private EditText question;

	/**
	 * account
	 * *
	 */
	private Account account;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityPostQuestion.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_post_question);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		postQuestion = (TextView) findViewById(R.id.postQuestion);
		title = (EditText) findViewById(R.id.title);
		question = (EditText) findViewById(R.id.question);
	}

	@Override
	protected void initViews() {
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		postQuestion.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.postQuestion:
			toPostQuestion();
			break;
		}
	}

	private void toPostQuestion() {
		String titleStr = title.getText().toString();
		if (TextUtils.isEmpty(titleStr)) {
			ToastUtil.makeShortText("输入帖子标题");
			return;
		}
		String content = question.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.makeShortText("输入帖子内容");
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
		showDialog("正在发表");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("communityid",  "1"));
		nameValuePairs.add(new BasicNameValuePair("title", titleStr));
		nameValuePairs.add(new BasicNameValuePair("content", jsonContent));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_PUB_TOPIC, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("发表失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				PostResultModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostResultModel.class);
				if (model != null && model.result == 1) {
					ToastUtil.makeShortText("发表成功");
					Intent intent = new Intent();
					intent.setAction(Actions.ACTION_REFRESH_POST_LIST);
					sendBroadcast(intent);
					finish();
				} else {
					ToastUtil.makeShortText("发表失败");
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (HCPopListView.isShowing(this)) {
			HCPopListView.onBackPressed(this);
			return;
		}
		super.onBackPressed();
	}

	class PostResultModel {
		@SerializedName("message")
		public String message;

		@SerializedName("result")
		public int result;

		@SerializedName("topicid")
		public String topicid;
	}
}
