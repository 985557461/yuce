package com.example.YuCeClient.ui.circle.post;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;

/**
 * Created by sreay on 15-11-22.
 */
public class PostReplyItemView extends FrameLayout {
	private TextView replyContent;
	private TextView replyNickName;

	public CommentModel commentModel;

	public PostReplyItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PostReplyItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PostReplyItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.post_reply_item_view, this, true);

		replyContent = (TextView) findViewById(R.id.replyContent);
		replyNickName = (TextView) findViewById(R.id.replyNickName);
	}

	public void setData(CommentModel commentModel) {
		this.commentModel = commentModel;
		if (commentModel == null) {
			return;
		}
		if (!TextUtils.isEmpty(commentModel.userName)) {
			replyNickName.setText(commentModel.userName);
		} else {
			replyNickName.setText("");
		}
		if (!TextUtils.isEmpty(commentModel.content)) {
			replyContent.setText(commentModel.content);
		} else {
			replyContent.setText("");
		}
	}
}
