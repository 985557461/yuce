package com.example.YuCeClient.ui.yuce_master;

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
 * Created by xiaoyu on 15-12-7.
 */
public class DoctorCommentItemView extends FrameLayout {
	private ImageView avatar;
	private TextView nickName;
	private ImageView starImageView;
	private TextView content;
	private TextView time;

	public DoctorCommentItemModel itemModel;
	private ImageLoader imageLoader;

	public DoctorCommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public DoctorCommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DoctorCommentItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.doctor_comment_item_view, this, true);

		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		starImageView = (ImageView) findViewById(R.id.starImageView);
		content = (TextView) findViewById(R.id.content);
		time = (TextView) findViewById(R.id.time);
	}

	public void setData(DoctorCommentItemModel itemModel) {
		this.itemModel = itemModel;
		if (itemModel == null) {
			return;
		}
		if (!TextUtils.isEmpty(itemModel.userAvatar)) {
			imageLoader.displayImage(itemModel.userAvatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(itemModel.nickname)) {
			nickName.setText(itemModel.nickname);
		} else {
			nickName.setText("无名小卒");
		}
		if (itemModel.score == 1) {
			starImageView.setImageResource(R.drawable.icon_one_star);
		} else if (itemModel.score == 2) {
			starImageView.setImageResource(R.drawable.icon_two_star);
		} else if (itemModel.score == 3) {
			starImageView.setImageResource(R.drawable.icon_three_star);
		} else if (itemModel.score == 4) {
			starImageView.setImageResource(R.drawable.icon_four_star);
		} else {
			starImageView.setImageResource(R.drawable.icon_five_star);
		}
		if (!TextUtils.isEmpty(itemModel.content)) {
			content.setText(itemModel.content);
		} else {
			content.setText("");
		}

		if (!TextUtils.isEmpty(itemModel.date)) {
			time.setText(StringUtil.getDateToString2(itemModel.date));
		} else {
			time.setText("");
		}
	}
}
