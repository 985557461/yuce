package com.example.YuCeClient.ui.circle.post;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.StringUtil;
import com.example.YuCeClient.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-7.
 */
public class PostCommentItemView extends FrameLayout {
	private ImageView avatar;
	private TextView nickName;
	private TextView time;
	private ImageView reply;
	private LinearLayout replyContainer;
	private TextView content;

	private ImageLoader imageLoader;
	private Account account;

	private CommentModel commentModel;
	private PostDescModel model;

	public PostCommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PostCommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PostCommentItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		account = HCApplicaton.getInstance().getAccount();
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.post_comment_item_view, this, true);

		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		time = (TextView) findViewById(R.id.time);
		reply = (ImageView) findViewById(R.id.reply);
		replyContainer = (LinearLayout) findViewById(R.id.replyContainer);
		content = (TextView) findViewById(R.id.content);

		reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(commentModel.replyPostId)) {
					ActivityPostReply.openForResult((Activity) getContext(), commentModel.replyPostId, commentModel.communityid, commentModel.userName, ActivityPostDetail.RequestReplyPostCode);
				}
			}
		});
	}

	public void setData(CommentModel commentModel) {
		this.commentModel = commentModel;
		if (commentModel == null) {
			return;
		}
		replyContainer.removeAllViews();
		getReply();
		if (!TextUtils.isEmpty(commentModel.userAvatar)) {
			imageLoader.displayImage(commentModel.userAvatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(commentModel.userName)) {
			nickName.setText(commentModel.userName);
		} else {
			nickName.setText("");
		}
		if (!TextUtils.isEmpty(commentModel.createTimeMs)) {
			time.setText(StringUtil.getTimeLineTime(commentModel.createTimeMs));
		} else {
			time.setText("");
		}
		if (!TextUtils.isEmpty(commentModel.content)) {
			content.setText(commentModel.content);
		} else {
			content.setText("");
		}
	}

	private void getReply() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", commentModel.replyPostId));
		nameValuePairs.add(new BasicNameValuePair("start_num", "0"));
		nameValuePairs.add(new BasicNameValuePair("limit", "2000"));
		Request.doRequest(getContext(), nameValuePairs, ServerConfig.URL_REPLY_DETAIL, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("数据获取失败");
			}

			@Override
			public void onComplete(String response) {
				model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostDescModel.class);
				if (model != null && model.result == 1) {
					if (model.replyPosts != null && model.replyPosts.size() > 0) {
						for (CommentModel commentModel1 : model.replyPosts) {
							PostReplyItemView itemView = new PostReplyItemView(getContext());
							itemView.setData(commentModel1);
							replyContainer.addView(itemView);
						}
					}
				} else {
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}
}
