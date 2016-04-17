package com.example.YuCeClient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by sreay on 14-9-3.
 */
public class FixWidthFrameLayout extends FrameLayout {

	public FixWidthFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FixWidthFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FixWidthFrameLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthModel = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightModel = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = 0;
		int height = 0;

		if (widthModel == MeasureSpec.EXACTLY || heightModel == MeasureSpec.EXACTLY) {
			if (widthModel == MeasureSpec.EXACTLY && heightModel == MeasureSpec.EXACTLY) {
				//ȡ�ϴ�ĳ���
				if (widthSize > heightSize) {
					width = height = widthSize;
				} else {
					width = height = heightSize;
				}
			} else if (widthModel == MeasureSpec.EXACTLY) {
				width = height = widthSize;
			} else {
				width = height = heightSize;
			}
		} else {
			//ָ��Ĭ�ϵĴ�С
			width = height = widthSize;
		}
		setMeasuredDimension(width,height);
		measureChildren(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
	}
}
