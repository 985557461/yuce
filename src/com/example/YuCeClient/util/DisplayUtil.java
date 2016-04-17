package com.example.YuCeClient.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DisplayUtil {
	/**
	 * ��pxֵת��Ϊdip��dpֵ����֤�ߴ��С����
	 *
	 * @param pxValue
	 * @param scale
	 *            ��DisplayMetrics��������density��
	 * @return
	 */
	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��dip��dpֵת��Ϊpxֵ����֤�ߴ��С����
	 *
	 * @param dipValue
	 * @param scale
	 *            ��DisplayMetrics��������density��
	 * @return
	 */
	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	public static int dipToPixels(Context context, int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return (int) px;
	}

	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}

	/**
	 * ��pxֵת��Ϊspֵ����֤���ִ�С����
	 *
	 * @param pxValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * ��spֵת��Ϊpxֵ����֤���ִ�С����
	 *
	 * @param spValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * ����ͼƬ��߻�ȡ�ȱ����Ŀ��
	 *
	 * @param nowWidth
	 * @param bmpWidth
	 * @param bmpHeight
	 */
	public static int getReSizeHeight(int nowWidth, int bmpWidth, int bmpHeight) {
		return (int) (nowWidth * (bmpHeight * 1.0 / bmpWidth));
	}

	public static int getReSizeWidth(int nowHeight, int bmpWidth, int bmpHeight) {
		return (int) (nowHeight * (bmpWidth * 1.0 / bmpHeight));
	}

	/*
	 * ���ؼ���
	 */
	public static void hideSolftInput(Context context, View v) {
		if (v == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	// ���������
	public static void openSoftInput(Context context, View v) {
		if (v == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		imm.showSoftInput(v, 0);
		// imm.showSoftInputFromInputMethod(v.getWindowToken(), 0);
	}

	/**
	 * ����������ñ�������paddingʧЧ����
	 *
	 * @param view
	 *            ��Ҫ�������ñ�����view
	 * @param resid
	 *            ������Դid
	 */
	public static void setBackgroundKeepPadding(View view, int resid) {
		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int left = view.getPaddingLeft();
		view.setBackgroundResource(resid);
		view.setPadding(left, top, right, bottom);
	}

	/**
	 * ����������ñ�������paddingʧЧ����
	 *
	 * @param view
	 *            ��Ҫ�������ñ�����view
	 * @param drawable
	 *            ����ͼƬ
	 */
	@SuppressLint("NewApi")
	public static void setBackgroundKeepPadding(View view, Drawable drawable) {
		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int left = view.getPaddingLeft();
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
		view.setPadding(left, top, right, bottom);
	}

	/**
	 * ��ȡ.9ͼ��paddingֵ
	 *
	 * @param context
	 * @param drawalbeId
	 * @return
	 */
	public static Rect getNinePatchPading(Context context, int drawalbeId) {
		Rect rect = new Rect();
		Drawable drawable = context.getResources().getDrawable(drawalbeId);
		if (drawable instanceof NinePatchDrawable) {
			NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) drawable;
			ninePatchDrawable.getPadding(rect);
		}
		return rect;

	}

	/**
	 * ���ֵĸߣ�������������
	 * @param fontSize
	 * @return
	 */
	public static float getFontHeight(float fontSize)
	{
	    Paint paint = new Paint();
	    paint.setTextSize(fontSize);
	    FontMetrics fm = paint.getFontMetrics();
	    return fm.bottom - fm.top ;
	}

	/**
	 * ���ֵĸߣ���������������
	 * @param fontSize
	 * @return
	 */
	public static float getFontHeightOnlyText(float fontSize)  
	{  
	    Paint paint = new Paint();  
	    paint.setTextSize(fontSize);  
	    FontMetrics fm = paint.getFontMetrics();  
	    return fm.descent - fm.ascent ;  
	}
	
}