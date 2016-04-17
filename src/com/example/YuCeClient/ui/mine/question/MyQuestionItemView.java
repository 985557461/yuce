package com.example.YuCeClient.ui.mine.question;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.xuanshang.ActivityXuanShangDesc;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.StringUtil;
import com.example.YuCeClient.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-12-9.
 */
public class MyQuestionItemView extends FrameLayout {
	private TextView time;
	private ImageView mainImageView;
	private TextView title;
	private TextView content;

	public QuestionItemModel itemModel;
	private ImageLoader imageLoader;

	public MyQuestionItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.my_question_ite_view, this, true);

		time = (TextView) findViewById(R.id.time);
		mainImageView = (ImageView) findViewById(R.id.mainImageView);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(itemModel.postId)) {
					ActivityXuanShangDesc.open((Activity)getContext(),itemModel.postId);
				} else {
					ToastUtil.makeShortText("帖子id为空");
				}
			}
		});
	}

	public void setData(QuestionItemModel itemModel) {
		this.itemModel = itemModel;
		if (itemModel == null) {
			return;
		}
		if (itemModel.postImage != null && itemModel.postImage.size() > 0) {
			if (!TextUtils.isEmpty(itemModel.postImage.get(0).postImageUrl)) {
				imageLoader.displayImage(itemModel.postImage.get(0).postImageUrl, mainImageView, ImageLoaderUtil.Options_Common_memory_Pic);
			} else {
				imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.post_default), mainImageView, ImageLoaderUtil.Options_Common_memory_Pic);
			}
		} else {
			imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.post_default), mainImageView, ImageLoaderUtil.Options_Common_memory_Pic);
		}
		if (!TextUtils.isEmpty(itemModel.postTitle)) {
			title.setText(itemModel.postTitle);
		} else {
			title.setText("没有标题");
		}
		if (!TextUtils.isEmpty(itemModel.postContent)) {
			content.setText(itemModel.postContent);
		} else {
			content.setText("");
		}
		if (!TextUtils.isEmpty(itemModel.createTimeMs)) {
			time.setText(StringUtil.getDateToString2(itemModel.createTimeMs));
		} else {
			time.setText("");
		}
	}
}
