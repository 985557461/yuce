package com.example.YuCeClient.ui.xuanshang;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class XuanShangItemView extends FrameLayout {
	private ImageView caiNaImageView;
	private ImageView mainImage;
	private TextView content;
	private TextView jinBiCount;
	private TextView postTime;
	private ImageView avatar;
	private TextView nickName;
	private TextView commentCount;

	public XuanShangItemModel xuanShangItemModel;
	private ImageLoader imageLoader;

	public XuanShangItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public XuanShangItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XuanShangItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.xuan_shang_item_view, this, true);

		caiNaImageView = (ImageView) findViewById(R.id.caiNaImageView);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		content = (TextView) findViewById(R.id.content);
		mainImage = (ImageView) findViewById(R.id.mainImage);
		postTime = (TextView) findViewById(R.id.postTime);
		jinBiCount = (TextView) findViewById(R.id.jinBiCount);
		commentCount = (TextView) findViewById(R.id.commentCount);
	}

	public void setData(XuanShangItemModel xuanShangItemModel) {
		this.xuanShangItemModel = xuanShangItemModel;
		if (xuanShangItemModel == null) {
			return;
		}
		if (xuanShangItemModel.iscaina == 1) {
			caiNaImageView.setVisibility(VISIBLE);
		} else {
			caiNaImageView.setVisibility(INVISIBLE);
		}
		if (!TextUtils.isEmpty(xuanShangItemModel.userAvatar)) {
			imageLoader.displayImage(xuanShangItemModel.userAvatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(xuanShangItemModel.userName)) {
			nickName.setText(xuanShangItemModel.userName);
		} else {
			nickName.setText("无名小卒");
		}
		if (!TextUtils.isEmpty(xuanShangItemModel.content)) {
			content.setText(xuanShangItemModel.content);
		} else {
			content.setText("");
		}
		if (xuanShangItemModel.postImages != null && xuanShangItemModel.postImages.size() > 0) {
			if (!TextUtils.isEmpty(xuanShangItemModel.postImages.get(0).postImageUrl)) {
				imageLoader.displayImage(xuanShangItemModel.postImages.get(0).postImageUrl, mainImage, ImageLoaderUtil.Options_Common_memory_Pic);
			} else {
				imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.post_default), mainImage, ImageLoaderUtil.Options_Common_memory_Pic);
			}
		} else {
			imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.post_default), mainImage, ImageLoaderUtil.Options_Common_memory_Pic);
		}
		if (!TextUtils.isEmpty(xuanShangItemModel.createTimeMs)) {
			postTime.setText(StringUtil.getDateToString2(xuanShangItemModel.createTimeMs));
		} else {
			postTime.setText("");
		}
		jinBiCount.setText(xuanShangItemModel.jinbi + "");
		commentCount.setText(xuanShangItemModel.replyNum + "");
	}
}
