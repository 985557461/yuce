package com.example.YuCeClient.ui.mine.reply;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
public class MyReplyItemView extends FrameLayout {
	private TextView time;
	private ImageView avatar;
	private TextView nickName;
	private TextView title;
	private TextView content;

	public ReplyItemModel itemModel;
	private ImageLoader imageLoader;

	public MyReplyItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.my_reply_item_view, this, true);

		time = (TextView) findViewById(R.id.time);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(itemModel.postId)) {
					ActivityXuanShangDesc.open((Activity) getContext(), itemModel.postId);
				} else {
					ToastUtil.makeShortText("帖子id为空");
				}
			}
		});
	}

	public void setData(ReplyItemModel itemModel) {
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
		if (!TextUtils.isEmpty(itemModel.postTitle)) {
			title.setText(itemModel.postTitle);
		} else {
			title.setText("没有标题");
		}
		if (!TextUtils.isEmpty(itemModel.replyContent)) {
			content.setText(itemModel.replyContent);
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
