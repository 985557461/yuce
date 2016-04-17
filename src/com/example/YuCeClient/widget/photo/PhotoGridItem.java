package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;

//此处一定要继承FrameLayout，xml中的rootView也要是FrameLayout，不知道原因，有待验证
public class PhotoGridItem extends FrameLayout implements Checkable {
	private Context mContext;
	private ImageView mImageView;
	private CheckBox mCheckBox;
	private int space;

	public PhotoGridItem(Context context) {
		this(context, null, 0);
	}

	public PhotoGridItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PhotoGridItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.photoalbum_gridview_item, this,true);
		mImageView = (ImageView) findViewById(R.id.photo_img_view);
		mCheckBox = (CheckBox) findViewById(R.id.photo_select);
		space = getResources().getDimensionPixelSize(R.dimen.photo_activity_space);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		WindowManager wm = (WindowManager) HCApplicaton.getInstance().getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();
		int rowPhotos = 4;
		int itemWidth = (screenWidth-(rowPhotos+1)*space)/rowPhotos;
		setMeasuredDimension(itemWidth,itemWidth);
		measureChildren(MeasureSpec.makeMeasureSpec(itemWidth,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(itemWidth,MeasureSpec.EXACTLY));
	}

	@Override
	public void setChecked(boolean checked) {
		mCheckBox.setChecked(checked);
	}

	@Override
	public boolean isChecked() {
		return mCheckBox.isChecked();
	}

	public void showCheck() {
		mCheckBox.setVisibility(View.VISIBLE);
	}

	public void hideCheck() {
		mCheckBox.setVisibility(View.GONE);
	}

	@Override
	public void toggle() {
		setChecked(!isChecked());
		if(isChecked()){
			showCheck();
		}else{
			hideCheck();
		}
	}

	public void setImgResID(int id) {
		if (mImageView != null) {
			mImageView.setBackgroundResource(id);
		}
	}

	public void setBitmap(Bitmap bit) {
		if (mImageView != null) {
			mImageView.setImageBitmap(bit);
		}
	}

}
