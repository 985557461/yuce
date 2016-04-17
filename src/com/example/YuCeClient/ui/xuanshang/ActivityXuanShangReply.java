package com.example.YuCeClient.ui.xuanshang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.xuanshang.photo_choose.PhotoSelectAdapter;
import com.example.YuCeClient.ui.xuanshang.photo_choose.PhotoSelectView;
import com.example.YuCeClient.ui.xuanshang.photo_choose.SquarePhotoView;
import com.example.YuCeClient.util.DisplayUtil;
import com.example.YuCeClient.util.IntentUtils;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.cropimage.ActivityCropImage;
import com.example.YuCeClient.widget.photo.PhotoActivity;
import com.example.YuCeClient.widget.photo.PhotoAlbumActivity;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-22.
 */
public class ActivityXuanShangReply extends ActivityBase implements View.OnClickListener, PhotoSelectView.FooterClickListener, PhotoSelectView.PhotoDeleteListener {
	private FrameLayout backFL;
	private EditText replyContent;
	private TextView submit;

	/**
	 * picture about
	 * *
	 */
	private PhotoSelectView photoSelectView;
	private PhotoSelectAdapter adapter;
	private static final int kActivitySettingSelectPicRequest = 101;
	private static final int kPhotoCropImageRequest = 102;
	private String avatarPath = "";
	private List<String> pathsList = new ArrayList<String>();
	private List<String> serverPaths = new ArrayList<String>();

	/**
	 * 图片上传的索引*
	 */
	private int pathIndex;

	private Account account;

	public static final String kToppicId = "top_id";
	public static final String kCommunityId = "community_id";
	public static final String kReplyName = "reply_name";

	private String topicId;
	private String communityId;
	private String replyName;

	public static void openForResult(Activity activity, String topId, String communityId, String replyName, int requestCode) {
		Intent intent = new Intent(activity, ActivityXuanShangReply.class);
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
		setContentView(R.layout.activity_xuan_shang_reply);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		backFL = (FrameLayout) findViewById(R.id.backFL);
		replyContent = (EditText) findViewById(R.id.replyContent);
		submit = (TextView) findViewById(R.id.submit);
		photoSelectView = (PhotoSelectView) findViewById(R.id.photoSelectView);
	}

	@Override
	protected void initViews() {
		adapter = new MyPhotoSelectAdapter();
		photoSelectView.setAdapter(adapter);
		if (TextUtils.isEmpty(replyName)) {
			replyContent.setHint("说点什么呢");
		} else {
			replyContent.setHint("回复 " + replyName + " :");
		}
	}

	@Override
	protected void setListeners() {
		backFL.setOnClickListener(this);
		submit.setOnClickListener(this);
		photoSelectView.setFooterClickListener(this);
		photoSelectView.setPhotoDeleteListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.submit) {
			final String content = replyContent.getText().toString();
			if (TextUtils.isEmpty(content)) {
				ToastUtil.makeShortText("回复内容不能为空");
				return;
			}
			showDialog();
			/**先判断需不需要上传图片**/
			if (pathsList != null && pathsList.size() > 0) {
				serverPaths.clear();
				pathIndex = 0;
				uploadImage(pathIndex);
			} else {
				tryToReply();
			}
		} else if (v.getId() == R.id.backFL) {
			finish();
		}
	}

	private void uploadImage(int index) {
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Request.doFileUploadRequest(this, nameValuePairs, new File(pathsList.get(index)), ServerConfig.URL_UPLOAD_IMAGE, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("图片上传失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				UploadImageModel uploadImageModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, UploadImageModel.class);
				if (uploadImageModel != null && !TextUtils.isEmpty(uploadImageModel.result)) {
					serverPaths.add(uploadImageModel.result);
					pathIndex++;
					if (pathIndex > pathsList.size() - 1) {
						tryToReply();
					} else {
						uploadImage(pathIndex);
					}
					ToastUtil.makeShortText("图片上传成功");
				} else {
					ToastUtil.makeShortText("图片上传失败");
				}
			}
		});
	}

	class UploadImageModel {
		@SerializedName("message")
		public String message;
		@SerializedName("result")
		public String result;
	}

	public void tryToReply() {
		final String content = replyContent.getText().toString();
		JSONObject contentJson = new JSONObject();
		try {
			contentJson.put("type", "text");
			contentJson.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(contentJson);
		for (int i = 0; i < serverPaths.size(); i++) {
			JSONObject imageJson = new JSONObject();
			try {
				imageJson.put("type", "image");
				imageJson.put("content", serverPaths.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonArray.put(imageJson);
		}
		String jsonContent = jsonArray.toString();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", topicId));
		nameValuePairs.add(new BasicNameValuePair("communityid", communityId));
		nameValuePairs.add(new BasicNameValuePair("content", jsonContent));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUAN_SHANG_REPLY_TOPIC, Request.POST, new Request.RequestListener() {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
			String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
			if (paths != null && paths.length <= 0) {
				return;
			}
			if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
				if (!TextUtils.isEmpty(paths[0])) {
					ActivityCropImage.openForResult(this, paths[0], 750, 750, true, kPhotoCropImageRequest);
					return;
				}
			} else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
				if (!TextUtils.isEmpty(paths[0])) {
					avatarPath = paths[0];
					pathsList.add(avatarPath);
					photoSelectView.setAdapter(adapter);
				}
			}
		} else if (requestCode == kPhotoCropImageRequest && resultCode == RESULT_OK) {
			avatarPath = data.getStringExtra(ActivityCropImage.kCropImagePath);
			pathsList.add(avatarPath);
			photoSelectView.setAdapter(adapter);
			return;
		}
	}

	@Override
	public void onFooterClicked() {
		Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, ActivityXuanShangReply.this);
		startActivityForResult(intent, kActivitySettingSelectPicRequest);
	}

	@Override
	public void onPhotoDeleteClicked(String path) {
		pathsList.remove(path);
		photoSelectView.setAdapter(adapter);
	}

	private class MyPhotoSelectAdapter extends PhotoSelectAdapter<SquarePhotoView> {

		@Override
		public int getWidth() {
			WindowManager wm = (WindowManager) ActivityXuanShangReply.this.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			return width - DisplayUtil.dip2px(ActivityXuanShangReply.this, 14);
		}

		@Override
		public int getTotalCount() {
			return pathsList.size();
		}

		@Override
		public int getColCount() {
			return 3;
		}

		@Override
		public int getHorMargin() {
			return 0;
		}

		@Override
		public int getVerMargin() {
			return DisplayUtil.dip2px(ActivityXuanShangReply.this, 10);
		}

		@Override
		public void setView(SquarePhotoView view, int position) {
			view.setData(pathsList.get(position));
		}
	}

	class ReplyModel {
		@SerializedName("message")
		public String message;

		@SerializedName("result")
		public int result;
	}
}
