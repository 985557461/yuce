package com.example.YuCeClient.ui.circle;

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
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class CircleNewItemView extends FrameLayout implements View.OnClickListener {
	private ImageView avatar;
	private TextView nickName;
	private TextView content;
	private ImageView mainImage;
	private TextView postTime;
	private LinearLayout collectionLL;
	private TextView collectionCount;
	private LinearLayout shareLL;
	private TextView shareCount;
	private LinearLayout likeLL;
	private TextView likeCount;
	private LinearLayout commentLL;
	private TextView commentCount;

	public CircleItemModel itemModel;
	private ImageLoader imageLoader;

	private CircleNewItemViewListener listener;

	public interface CircleNewItemViewListener {
		public void onCollectionClicked(CircleItemModel itemModel);

		public void onShareClicked(CircleItemModel itemModel);

		public void onLikeClicked(CircleItemModel itemModel);

		public void onCommentClicked(CircleItemModel itemModel);

		public void onDefaultClicked(CircleItemModel itemModel);
	}

	public CircleNewItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CircleNewItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CircleNewItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.circle_new_item_view, this, true);

		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		content = (TextView) findViewById(R.id.content);
		mainImage = (ImageView) findViewById(R.id.mainImage);
		postTime = (TextView) findViewById(R.id.postTime);
		collectionLL = (LinearLayout) findViewById(R.id.collectionLL);
		collectionCount = (TextView) findViewById(R.id.collectionCount);
		shareLL = (LinearLayout) findViewById(R.id.shareLL);
		shareCount = (TextView) findViewById(R.id.shareCount);
		likeLL = (LinearLayout) findViewById(R.id.likeLL);
		likeCount = (TextView) findViewById(R.id.likeCount);
		commentLL = (LinearLayout) findViewById(R.id.commentLL);
		commentCount = (TextView) findViewById(R.id.commentCount);

		collectionLL.setOnClickListener(this);
		shareLL.setOnClickListener(this);
		likeLL.setOnClickListener(this);
		commentLL.setOnClickListener(this);
		setOnClickListener(this);
	}

	public void setListener(CircleNewItemViewListener listener) {
		this.listener = listener;
	}

	public void setData(CircleItemModel itemModel) {
		this.itemModel = itemModel;
		if (itemModel == null) {
			return;
		}
		if (!TextUtils.isEmpty(itemModel.userAvatar)) {
			imageLoader.displayImage(itemModel.userAvatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(itemModel.userName)) {
			nickName.setText(itemModel.userName);
		} else {
			nickName.setText("无名小卒");
		}
		if (!TextUtils.isEmpty(itemModel.content)) {
			content.setText(itemModel.content);
		} else {
			content.setText("");
		}
		if (itemModel.postImages != null && itemModel.postImages.size() > 0) {
			if (!TextUtils.isEmpty(itemModel.postImages.get(0).postImageUrl)) {
				imageLoader.displayImage(itemModel.postImages.get(0).postImageUrl, mainImage, ImageLoaderUtil.Options_Common_memory_Pic);
			} else {
				imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.post_default), mainImage, ImageLoaderUtil.Options_Common_memory_Pic);
			}
		} else {
			imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.post_default), mainImage, ImageLoaderUtil.Options_Common_memory_Pic);
		}
		if (!TextUtils.isEmpty(itemModel.createTimeMs)) {
			postTime.setText(StringUtil.getDateToString2(itemModel.createTimeMs));
		} else {
			postTime.setText("");
		}
		collectionCount.setText(itemModel.collectionNum + "");
		shareCount.setText(itemModel.shareCount + "");
		likeCount.setText(itemModel.likeCount + "");
		commentCount.setText(itemModel.replyNum + "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.collectionLL:
			if (listener != null) {
				listener.onCollectionClicked(itemModel);
			}
			break;
		case R.id.shareLL:
			if (listener != null) {
				listener.onShareClicked(itemModel);
			}
			break;
		case R.id.likeLL:
			if (listener != null) {
				listener.onLikeClicked(itemModel);
			}
			break;
		case R.id.commentLL:
			if (listener != null) {
				listener.onCommentClicked(itemModel);
			}
			break;
		default:
			if (listener != null) {
				listener.onDefaultClicked(itemModel);
			}
			break;
		}
	}
}
