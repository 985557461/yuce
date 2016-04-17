package com.example.YuCeClient.widget.cropimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//底图缩放，浮层不变
public class CropImageView extends View {
	//单点触摸
	private float oldX = 0;
	private float oldY = 0;

	private float oldx_0 = 0;
	private float oldy_0 = 0;

	private float oldx_1 = 0;
	private float oldy_1 = 0;

	//状态
	private final int STATUS_Touch_SINGLE = 1;
	private final int STATUS_TOUCH_MULTI_START = 2;
	private final int STATUS_TOUCH_MULTI_TOUCHING = 3;

	private int mStatus = STATUS_Touch_SINGLE;

	//默认的裁剪图片宽度与高度
	private final int defaultCropWidth = 100;
	private final int defaultCropHeight = 100;
	private int cropWidth = defaultCropWidth;
	private int cropHeight = defaultCropHeight;

	protected float oriRationWH = 0;//原始宽高比率
	protected final float maxZoomOut = 5.0f;//最大扩大到多少倍
	protected final float minZoomIn = 0.333333f;//最小缩小到多少倍

	protected Drawable mDrawable;//原图
	protected FloatDrawable mFloatDrawable;//浮层
	protected Rect mDrawableSrc = new Rect();//图片在屏幕中显示的原始rect
	protected Rect mDrawableDst = new Rect();//图片在屏幕中操作后显示的rect
	protected Rect mDrawableFloat = new Rect();//浮层选择框，就是头像选择框
	protected boolean isFrist = true;

	protected Context mContext;

	protected boolean isLockPic = false;
	protected Rect mLockRect = new Rect();//图片锁定时的rect

	public CropImageView(Context context) {
		super(context);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);

	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		this.mContext = context;
		try {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				this.setLayerType(LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mFloatDrawable = new FloatDrawable(context);
	}

	public void setDrawable(Drawable mDrawable, int cropWidth, int cropHeight) {
		this.mDrawable = mDrawable;
		this.cropWidth = cropWidth;
		this.cropHeight = cropHeight;
		this.isFrist = true;
		invalidate();
	}

	public void lockPic(boolean lock) {
		this.isLockPic = lock;
		if (isLockPic) {
			mFloatDrawable.setLockFlag(true);
			calculateCenterPosition();
		} else {
			mFloatDrawable.setLockFlag(false);
		}
		invalidate();
	}

	private void calculateCenterPosition() {
		int width = mDrawable.getIntrinsicWidth();//得到原始图片的宽度
		int height = mDrawable.getIntrinsicHeight();
		int left;
		int top;
		int right;
		int bottom;
		int realWidth;
		int realHeight;
		int screenWidth;
		int screenHeight;
		if (height > width) {//图片是竖直的
			realHeight = mDrawableFloat.height();
			realWidth = realHeight * width / height;
		} else {//图片是水平的
			realWidth = mDrawableFloat.width();
			realHeight = realWidth * height / width;
		}
		screenWidth = getWidth();
		screenHeight = getHeight();
		left = (screenWidth - realWidth) / 2;
		top = (screenHeight - realHeight) / 2;
		right = left + realWidth;
		bottom = top + realHeight;
		mLockRect.set(left, top, right, bottom);
	}

	public boolean getLockFlag() {
		return isLockPic;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isLockPic) {
			return true;
		}
		if (event.getPointerCount() > 1) {
			if (mStatus == STATUS_Touch_SINGLE) {
				mStatus = STATUS_TOUCH_MULTI_START;

				oldx_0 = event.getX(0);
				oldy_0 = event.getY(0);

				oldx_1 = event.getX(1);
				oldy_1 = event.getY(1);
			} else if (mStatus == STATUS_TOUCH_MULTI_START) {
				mStatus = STATUS_TOUCH_MULTI_TOUCHING;
			}
		} else {
			if (mStatus == STATUS_TOUCH_MULTI_START || mStatus == STATUS_TOUCH_MULTI_TOUCHING) {
				oldx_0 = 0;
				oldy_0 = 0;

				oldx_1 = 0;
				oldy_1 = 0;

				oldX = event.getX();
				oldY = event.getY();
			}

			mStatus = STATUS_Touch_SINGLE;
		}

		//Log.v("count currentTouch"+currentTouch, "-------");

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//Log.v("count ACTION_DOWN", "-------");
			oldX = event.getX();
			oldY = event.getY();
			break;

		case MotionEvent.ACTION_UP:
			//Log.v("count ACTION_UP", "-------");
			checkBounds();
			break;

		case MotionEvent.ACTION_POINTER_1_DOWN:
			//Log.v("count ACTION_POINTER_1_DOWN", "-------");
			break;

		case MotionEvent.ACTION_POINTER_UP:
			//Log.v("count ACTION_POINTER_UP", "-------");
			break;

		case MotionEvent.ACTION_MOVE:
			//Log.v("count ACTION_MOVE", "-------");
			if (mStatus == STATUS_TOUCH_MULTI_TOUCHING) {
				float newx_0 = event.getX(0);
				float newy_0 = event.getY(0);

				float newx_1 = event.getX(1);
				float newy_1 = event.getY(1);

				float oldWidth = Math.abs(oldx_1 - oldx_0);
				float oldHeight = Math.abs(oldy_1 - oldy_0);

				float newWidth = Math.abs(newx_1 - newx_0);
				float newHeight = Math.abs(newy_1 - newy_0);

				//以移动距离大的方向为准
				boolean isDependHeight = Math.abs(newHeight - oldHeight) > Math.abs(newWidth - oldWidth);
				//以最大的缩放
				float ration = isDependHeight ? ((float) newHeight / (float) oldHeight) : ((float) newWidth / (float) oldWidth);
				int centerX = mDrawableDst.centerX();
				int centerY = mDrawableDst.centerY();
				//destRect的新的宽高（维持原始缩放比）
				int _newWidth = (int) (mDrawableDst.width() * ration);
				int _newHeight = (int) ((float) _newWidth / oriRationWH);

				//规定的一个最大最小的缩放比
				float tmpZoomRation = (float) _newWidth / (float) mDrawableSrc.width();
				if (tmpZoomRation >= maxZoomOut) {
					_newWidth = (int) (maxZoomOut * mDrawableSrc.width());
					_newHeight = (int) ((float) _newWidth / oriRationWH);
				} else if (tmpZoomRation <= minZoomIn) {
					_newWidth = (int) (minZoomIn * mDrawableSrc.width());
					_newHeight = (int) ((float) _newWidth / oriRationWH);
				}

				mDrawableDst.set(centerX - _newWidth / 2, centerY - _newHeight / 2, centerX + _newWidth / 2, centerY + _newHeight / 2);
				invalidate();

				oldx_0 = newx_0;
				oldy_0 = newy_0;

				oldx_1 = newx_1;
				oldy_1 = newy_1;
			} else if (mStatus == STATUS_Touch_SINGLE) {
				int dx = (int) (event.getX() - oldX);
				int dy = (int) (event.getY() - oldY);

				oldX = event.getX();
				oldY = event.getY();

				//todo
				checkBorder(dx, dy);
			}
			break;
		}
		return true;
	}

	//图片吸附在框内
	private void checkBorder(int dx, int dy) {
		//图宽度比屏幕宽度小的时候
		if (mDrawableDst.width() < getWidth()) {
			//手指水平移动
			if (mDrawableDst.left <= 0 && dx < 0) {
				dx = 0;
			}
			if (mDrawableDst.right >= getWidth() && dx > 0) {
				dx = 0;
			}
		} else {//图宽度比屏幕宽度大的时候
			//手指水平移动
			if (mDrawableDst.left >= 0 && dx > 0) {
				dx = 0;
			}
			if (mDrawableDst.right <= getWidth() && dx < 0) {
				dx = 0;
			}
		}
		//图高度比截图高度小的时候
		if (mDrawableDst.height() < mFloatDrawable.getBounds().height()) {
			//手指竖直移动
			if (mDrawableDst.top <= mFloatDrawable.getBounds().top && dy < 0) {
				dy = 0;
			}
			if (mDrawableDst.bottom >= mFloatDrawable.getBounds().bottom && dy > 0) {
				dy = 0;
			}
		} else {//图高度比截图高度大的时候
			//手指竖直移动
			if (mDrawableDst.top >= mFloatDrawable.getBounds().top && dy > 0) {
				dy = 0;
			}
			if (mDrawableDst.bottom <= mFloatDrawable.getBounds().bottom && dy < 0) {
				dy = 0;
			}
		}
		if (!(dx == 0 && dy == 0)) {
			mDrawableDst.offset(dx, dy);
			invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//		super.onDraw(canvas);
		if (mDrawable == null) {
			return; // couldn't resolve the URI
		}

		if (mDrawable.getIntrinsicWidth() == 0 || mDrawable.getIntrinsicHeight() == 0) {
			return;     // nothing to draw (empty bounds)
		}

		configureBounds();
		mFloatDrawable.draw(canvas);
		mDrawable.draw(canvas);
		canvas.save();
		canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
		canvas.drawColor(Color.parseColor("#80000000"));
		canvas.restore();
	}


	protected void configureBounds() {
		if (isFrist) {
			//原始图片的宽高
			oriRationWH = ((float) mDrawable.getIntrinsicWidth()) / ((float) mDrawable.getIntrinsicHeight());

			//计算图片在屏幕中显示的宽高
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth() * scale + 0.5f));
			int h = (int) (w / oriRationWH);

			//计算图片所在的rect的位置
			int left = (getWidth() - w) / 2;
			int top = (getHeight() - h) / 2;
			int right = left + w;
			int bottom = top + h;

			mDrawableSrc.set(left, top, right, bottom);
			mDrawableDst.set(mDrawableSrc);

			//框的宽度充满屏幕的宽度，高度去计算
			int floatWidth = getWidth();
			int floatHeight = cropHeight * floatWidth / cropWidth;

			//浮动框在屏幕中的位置
			int floatLeft = (getWidth() - floatWidth) / 2;
			int floatTop = (getHeight() - floatHeight) / 2;
			mDrawableFloat.set(floatLeft, floatTop, floatLeft + floatWidth, floatTop + floatHeight);

			isFrist = false;
		}

		//为原图设置大小
		if (!isLockPic) {
			mDrawable.setBounds(mDrawableDst);
		} else {
			mDrawable.setBounds(mLockRect);
		}
		//设置剪裁框的大小
		mFloatDrawable.setBounds(mDrawableFloat);
	}

	protected void checkBounds() {
		int newLeft = mDrawableDst.left;
		int newTop = mDrawableDst.top;

		boolean isChange = false;
		//图的宽度大于剪裁宽度
		if (mDrawableDst.width() > getWidth()) {
			if (mDrawableDst.left > 0) {
				newLeft = 0;
				isChange = true;
			}
			if (mDrawableDst.right < getWidth()) {
				newLeft = getWidth() - mDrawableDst.width();
				isChange = true;
			}
		} else {//图的宽度小于剪裁宽度
			if (mDrawableDst.left < 0) {
				newLeft = 0;
				isChange = true;
			}
			if (mDrawableDst.right > getWidth()) {
				newLeft = getWidth() - mDrawableDst.width();
				isChange = true;
			}
		}
		//图的高度大于剪裁高度
		if (mDrawableDst.height() > mFloatDrawable.getBounds().height()) {
			if (mDrawableDst.top > mFloatDrawable.getBounds().top) {
				newTop = mFloatDrawable.getBounds().top;
				isChange = true;
			}

			if (mDrawableDst.bottom < mFloatDrawable.getBounds().bottom) {
				newTop = mFloatDrawable.getBounds().bottom - mDrawableDst.height();
				isChange = true;
			}
		} else {//图的高度小于剪裁高度
			if (mDrawableDst.top < mFloatDrawable.getBounds().top) {
				newTop = mFloatDrawable.getBounds().top;
				isChange = true;
			}
			if (mDrawableDst.bottom > mFloatDrawable.getBounds().bottom) {
				newTop = mFloatDrawable.getBounds().bottom - mDrawableDst.height();
				isChange = true;
			}
		}
		mDrawableDst.offsetTo(newLeft, newTop);
		if (isChange) {
			invalidate();
		}
	}

	public Bitmap getCropImage() {
		Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(tmpBitmap);
		canvas.drawColor(Color.parseColor("#ffffffff"));
		mDrawable.draw(canvas);

		Matrix matrix = new Matrix();
		float scale = 1.0f;
		if (!isLockPic) {
			scale = (float) (mDrawableSrc.width()) / (float) (mDrawableDst.width());
		} else {
			scale = (float) (mDrawableSrc.width()) / (float) (mLockRect.width());
		}
		matrix.postScale(scale, scale);

		Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left, mDrawableFloat.top, mDrawableFloat.width(), mDrawableFloat.height(), matrix, true);
		if (ret != null && ret != tmpBitmap) {
			tmpBitmap.recycle();
			tmpBitmap = ret;
		}
		//修改图片大小
		Bitmap newRet = Bitmap.createScaledBitmap(tmpBitmap, cropWidth, cropHeight, false);
		if (newRet != null && newRet != tmpBitmap) {
			tmpBitmap.recycle();
			tmpBitmap = newRet;
		}
		return tmpBitmap;
	}
}
