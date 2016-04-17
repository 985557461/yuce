package com.example.YuCeClient.widget.cropimage;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.example.YuCeClient.R;

/**
 * 头像图片选择框的浮层
 *
 * @author Administrator
 */
public class FloatDrawable extends Drawable {

	private Context mContext;
	private Drawable mCropPointDrawable;
	private boolean isLockPic = false;

	private int mBorderWidth = 1;
	private Paint mLinePaint = new Paint();
	private Paint mTransprentPaint = new Paint();

	public FloatDrawable(Context context) {
		super();
		this.mContext = context;
		init();
	}

	private void init() {
		mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, mContext.getResources().getDisplayMetrics());
		mCropPointDrawable = mContext.getResources().getDrawable(R.drawable.clip_point);
		mLinePaint.setStrokeWidth(1F);
		mLinePaint.setStyle(Paint.Style.FILL);
		mLinePaint.setAntiAlias(true);
		mLinePaint.setColor(Color.WHITE);

		mTransprentPaint.setStrokeWidth(mBorderWidth);
		mTransprentPaint.setStyle(Paint.Style.STROKE);
		mTransprentPaint.setAntiAlias(true);
		mTransprentPaint.setColor(Color.WHITE);
	}

	public int getCirleWidth() {
		return mCropPointDrawable.getIntrinsicWidth();
	}

	public int getCirleHeight() {
		return mCropPointDrawable.getIntrinsicHeight();
	}

	public void setLockFlag(boolean lock) {
		this.isLockPic = lock;
	}

	@Override
	public void draw(Canvas canvas) {
		int left = getBounds().left;
		int top = getBounds().top;
		int right = getBounds().right;
		int bottom = getBounds().bottom;

		Rect mRect = new Rect(left, top, right, bottom);
		//方框
		if (isLockPic) {
			canvas.drawRect(mRect, mLinePaint);
		} else {
			canvas.drawRect(mRect, mTransprentPaint);
		}

		//		//左上
		//		mCropPointDrawable.setBounds(left, top, left+mCropPointDrawable.getIntrinsicWidth(), top+mCropPointDrawable.getIntrinsicHeight());
		//		mCropPointDrawable.draw(canvas);
		//
		//		//右上
		//		mCropPointDrawable.setBounds(right-mCropPointDrawable.getIntrinsicWidth(), top, right, top+mCropPointDrawable.getIntrinsicHeight());
		//		mCropPointDrawable.draw(canvas);
		//
		//		//左下
		//		mCropPointDrawable.setBounds(left, bottom-mCropPointDrawable.getIntrinsicHeight(), left+mCropPointDrawable.getIntrinsicWidth(), bottom);
		//		mCropPointDrawable.draw(canvas);
		//
		//		//右下
		//		mCropPointDrawable.setBounds(right-mCropPointDrawable.getIntrinsicWidth(), bottom-mCropPointDrawable.getIntrinsicHeight(), right, bottom);
		//		mCropPointDrawable.draw(canvas);

	}

	@Override
	public void setBounds(Rect bounds) {
		super.setBounds(new Rect(bounds.left, bounds.top, bounds.right, bounds.bottom));
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}
}
