package com.example.YuCeClient.ui.circle.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.RefreshListView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-6.
 */

/**
 * 帖子的详细内容*
 */
public class ActivityPostDetail extends ActivityBase implements View.OnClickListener, RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener {
	private FrameLayout backFL;
	private TextView reply;
	private RefreshListView commentList;
	private PostDetailHeader postDetailHeader;

	private int p = 0;
	private final int SIZE = 50;
	private List<CommentModel> commentModels = new ArrayList<CommentModel>();
	private CommentAdapter commentAdapter;

	public static final String kPostId = "post_id";
	private String postId = "";

	private Account account;
	private PostDescModel model;
	public static final int RequestReplyPostCode = 10001;

	public static void open(Activity activity, String postId) {
		Intent intent = new Intent(activity, ActivityPostDetail.class);
		intent.putExtra(kPostId, postId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		postId = getIntent().getStringExtra(kPostId);
		setContentView(R.layout.activity_circle_detail);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		backFL = (FrameLayout) findViewById(R.id.backFL);
		reply = (TextView) findViewById(R.id.reply);
		commentList = (RefreshListView) findViewById(R.id.commentList);
	}

	@Override
	protected void initViews() {
		commentList.setCanRefresh(false);
		commentList.setCanLoadMore(false);
		onRefresh();
	}

	private void initHeaderData(PostDescModel postDescModel) {
		if (postDescModel != null) {
			int headerCount = commentList.getHeaderViewsCount();
			if (headerCount > 0) {
				commentList.removeHeaderView(postDetailHeader);
			}
			postDetailHeader = new PostDetailHeader(this);
			postDetailHeader.setData(postDescModel);
			commentList.addHeaderView(postDetailHeader);
			commentAdapter = new CommentAdapter();
			commentList.setAdapter(commentAdapter);
		}
	}

	@Override
	protected void setListeners() {
		backFL.setOnClickListener(this);
		reply.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backFL:
			finish();
			break;
		case R.id.reply:
			if (TextUtils.isEmpty(account.userId)) {
				ActivityLogin.open(this);
			} else {
				if (model != null) {
					ActivityPostReply.openForResult(this, postId, model.communityid, null, RequestReplyPostCode);
				}
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestReplyPostCode && resultCode == RESULT_OK) {
			onRefresh();
		}
	}

	@Override
	public void onRefresh() {
		showDialog();
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", postId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_TOPIC_DETAIL, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("数据获取失败");
				commentList.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				commentList.onRefreshComplete();
				model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostDescModel.class);
				if (model != null && model.result == 1) {
					initHeaderData(model);
					if (model.replyPosts != null && model.replyPosts.size() > 0) {
						if (model.replyPosts.size() < SIZE) {//说明没有了
							commentList.setCanLoadMore(false);
						} else {
							commentList.setCanLoadMore(true);
						}
						commentModels.clear();
						commentModels.addAll(model.replyPosts);
						commentAdapter.notifyDataSetChanged();
					}
				} else {
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	@Override
	public void onLoadMore() {
		p++;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", postId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_TOPIC_DETAIL, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("数据获取失败");
				commentList.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				commentList.onLoadMoreComplete();
				PostDescModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostDescModel.class);
				if (model != null && model.result == 1) {
					if (model.replyPosts != null && model.replyPosts.size() > 0) {
						if (model.replyPosts.size() < SIZE) {//说明没有了
							commentList.setCanLoadMore(false);
						} else {
							commentList.setCanLoadMore(true);
						}
						commentModels.addAll(model.replyPosts);
						commentAdapter.notifyDataSetChanged();
					}
				} else {
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	class CommentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return commentModels.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PostCommentItemView itemView = null;
			if (convertView != null) {
				itemView = (PostCommentItemView) convertView;
			} else {
				itemView = new PostCommentItemView(ActivityPostDetail.this);
			}
			itemView.setData(commentModels.get(position));
			return itemView;
		}
	}
}
