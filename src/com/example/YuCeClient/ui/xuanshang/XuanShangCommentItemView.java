package com.example.YuCeClient.ui.xuanshang;

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
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.circle.post.CommentModel;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-12-1.
 */
public class XuanShangCommentItemView extends FrameLayout implements View.OnClickListener {
	private ImageView avatar;
	private TextView nickName;
	private ImageView renZhengImageview;
	private TextView time;
	private TextView louTextView;
	private TextView content;
	private LinearLayout shangContainer;
	private TextView caiNa;
	private TextView zhuiWen;
	private TextView daShang;

	private CommentModel commentModel;
	private ImageLoader imageLoader;
	private XuanShangCommentItemListener listener;

	public interface XuanShangCommentItemListener {
		public void onCaiNaClicked(CommentModel commentModel);

		public void onDaShangClicked(CommentModel commentModel);

		public void onZhuiWenClicked(CommentModel commentModel);
	}

	public XuanShangCommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public XuanShangCommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XuanShangCommentItemView(Context context) {
		super(context);
		init(context);
	}

	public void setListener(XuanShangCommentItemListener listener) {
		this.listener = listener;
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.xin_yuan_comment_item_view, this, true);

		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		renZhengImageview = (ImageView) findViewById(R.id.renZhengImageview);
		time = (TextView) findViewById(R.id.time);
		louTextView = (TextView) findViewById(R.id.louTextView);
		content = (TextView) findViewById(R.id.content);
		shangContainer = (LinearLayout) findViewById(R.id.shangContainer);
		caiNa = (TextView) findViewById(R.id.caiNa);
		zhuiWen = (TextView) findViewById(R.id.zhuiWen);
		daShang = (TextView) findViewById(R.id.daShang);

		caiNa.setOnClickListener(this);
		zhuiWen.setOnClickListener(this);
		daShang.setOnClickListener(this);
	}

	public void setData(CommentModel commentModel, boolean isMyPost, boolean isCaina) {
		this.commentModel = commentModel;
		if (commentModel == null) {
			return;
		}
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
		if (isMyPost) {
			shangContainer.setVisibility(View.VISIBLE);
			if (isCaina) {
				caiNa.setVisibility(View.INVISIBLE);
			} else {
				caiNa.setVisibility(View.VISIBLE);
			}
		} else {
			shangContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.caiNa:
			if (listener != null) {
				listener.onCaiNaClicked(commentModel);
			}
			break;
		case R.id.daShang:
			if (listener != null) {
				listener.onDaShangClicked(commentModel);
			}
			break;
		case R.id.zhuiWen:
			if (listener != null) {
				listener.onZhuiWenClicked(commentModel);
			}
			break;
		}
	}
}
