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
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.widget.custom_viewgroup.IViewContainerItem;
import com.example.YuCeClient.widget.custom_viewgroup.MeasureModel;
import com.example.YuCeClient.widget.custom_viewgroup.SuperViewContainer;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by xiaoyu on 15-12-1.
 */
public class XuanShangDescHeader extends FrameLayout implements View.OnClickListener {
	private ImageView caiNaImageView;
	private ImageView avatar;
	private TextView name;
	private TextView sex;
	private TextView jinBiCount;
	private TextView birthDay;
	private TextView birthPlace;
	private TextView content;
	private SuperViewContainer imageContainer;
	private LinearLayout commentLL;
	private TextView commentCount;
	private TextView readCount;

	public XuanShangDetailListModel postDescModel;
	private ImageLoader imageLoader;

	private XuanShangDescHeaderListener listener;

	public interface XuanShangDescHeaderListener {
		public void onCommentClicked();
	}

	public XuanShangDescHeader(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public XuanShangDescHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XuanShangDescHeader(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.xuan_shang_desc_header, this, true);

		caiNaImageView = (ImageView) findViewById(R.id.caiNaImageView);
		avatar = (ImageView) findViewById(R.id.avatar);
		name = (TextView) findViewById(R.id.name);
		sex = (TextView) findViewById(R.id.sex);
		jinBiCount = (TextView) findViewById(R.id.jinBiCount);
		birthDay = (TextView) findViewById(R.id.birthDay);
		birthPlace = (TextView) findViewById(R.id.birthPlace);
		content = (TextView) findViewById(R.id.content);
		imageContainer = (SuperViewContainer) findViewById(R.id.imageContainer);
		commentLL = (LinearLayout) findViewById(R.id.commentLL);
		commentCount = (TextView) findViewById(R.id.commentCount);
		readCount = (TextView) findViewById(R.id.readCount);

		commentLL.setOnClickListener(this);
	}

	public void setListener(XuanShangDescHeaderListener listener) {
		this.listener = listener;
	}

	public void setData(XuanShangDetailListModel postDescModel) {
		this.postDescModel = postDescModel;
		if (postDescModel == null) {
			return;
		}
		if (postDescModel.iscaina == 1) {
			caiNaImageView.setVisibility(View.VISIBLE);
		} else {
			caiNaImageView.setVisibility(View.INVISIBLE);
		}
		if (!TextUtils.isEmpty(postDescModel.xingming)) {
			name.setText("姓名: " + postDescModel.xingming);
		} else {
			name.setText("");
		}
		if (!TextUtils.isEmpty(postDescModel.postFloorAvatar)) {
			imageLoader.displayImage(postDescModel.postFloorAvatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage(ImageLoaderUtil.fromDrawable(R.drawable.default_avatar), avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(postDescModel.xingbie) && postDescModel.xingbie.equals("1")) {
			sex.setText("性别: 男");
		} else {
			sex.setText("性别: 女");
		}
		jinBiCount.setText(postDescModel.jinbi + "");
		if (!TextUtils.isEmpty(postDescModel.chusheng)) {
			birthDay.setText("出生年月: " + postDescModel.chusheng);
		} else {
			birthDay.setText("");
		}
		if (!TextUtils.isEmpty(postDescModel.chushengdi)) {
			birthPlace.setText("出生地点: " + postDescModel.chushengdi);
		} else {
			birthPlace.setText("");
		}
		if (!TextUtils.isEmpty(postDescModel.postContent)) {
			content.setText(postDescModel.postContent);
		} else {
			content.setText("");
		}
		commentCount.setText(postDescModel.replyNum + "");
		if (postDescModel.postImages != null && postDescModel.postImages.size() > 0) {
			List<ImageInfoModel> imagePaths = postDescModel.postImages;
			for (ImageInfoModel imageInfoModel : imagePaths) {
				MyImageView myImageView = new MyImageView(getContext());
				myImageView.setData(imageInfoModel.postImageUrl);
				imageContainer.addView(myImageView);
			}
		} else {
			imageContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.commentLL) {
			if (listener != null) {
				listener.onCommentClicked();
			}
		}
	}

	class MyImageView extends FrameLayout implements IViewContainerItem {
		/**
		 * MeasureModel*
		 */
		public MeasureModel widthMeasureModel = new MeasureModel(MeasureModel.Pattern.WEIGHT, 1);
		public MeasureModel heightMeasureModel = new MeasureModel(MeasureModel.Pattern.WEIGHT, 1);
		public int multiple = 1;

		private ImageView imageView;

		public MyImageView(Context context) {
			super(context);
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater.inflate(R.layout.my_imageview, this, true);

			imageView = (ImageView) findViewById(R.id.imageView);
		}

		public void setData(String path) {
			if (!TextUtils.isEmpty(path)) {
				HCApplicaton.getInstance().getImageLoader().displayImage(path, imageView, ImageLoaderUtil.Options_Common_Disc_Pic);
			} else {
				HCApplicaton.getInstance().getImageLoader().displayImage("", imageView, ImageLoaderUtil.Options_Common_Disc_Pic);
			}
		}

		@Override
		public MeasureModel getWidthMeasureModel() {
			return widthMeasureModel;
		}

		@Override
		public MeasureModel getHeightMeasureMoldel() {
			return heightMeasureModel;
		}

		@Override
		public int getMultiple() {
			return multiple;
		}
	}
}
