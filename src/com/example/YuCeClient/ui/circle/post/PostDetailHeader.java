package com.example.YuCeClient.ui.circle.post;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.circle.post.PostDescModel;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-11-6.
 */

/**
 * 帖子详情的头部*
 */
public class PostDetailHeader extends FrameLayout {
	private TextView title;
	private ImageView avatar;
	private TextView nickName;
	private TextView time;
	private TextView content;
	private ImageLoader imageLoader;

	public PostDescModel postDescModel;

	public PostDetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PostDetailHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PostDetailHeader(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.post_detail_header, this, true);

		title = (TextView) findViewById(R.id.title);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		time = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content);
	}

	public void setData(PostDescModel postDescModel) {
		this.postDescModel = postDescModel;
		if (postDescModel == null) {
			return;
		}
		if (!TextUtils.isEmpty(postDescModel.postTitle)) {
			title.setText(postDescModel.postTitle);
		} else {
			title.setText("");
		}
		if (!TextUtils.isEmpty(postDescModel.postFloorAvatar)) {
			imageLoader.displayImage(postDescModel.postFloorAvatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(postDescModel.postFloorName)) {
			nickName.setText(postDescModel.postFloorName);
		} else {
			nickName.setText("");
		}
		if (!TextUtils.isEmpty(postDescModel.createTimeMs)) {
			time.setText(StringUtil.getTimeLineTime(postDescModel.createTimeMs));
		} else {
			time.setText("");
		}
		if (!TextUtils.isEmpty(postDescModel.postContent)) {
			content.setText(postDescModel.postContent);
		} else {
			content.setText("");
		}
	}
}
