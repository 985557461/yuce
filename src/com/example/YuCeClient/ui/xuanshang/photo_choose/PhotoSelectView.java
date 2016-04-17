package com.example.YuCeClient.ui.xuanshang.photo_choose;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.example.YuCeClient.R;
import com.example.YuCeClient.widget.FixWidthFrameLayout;

/**
 * Created by xiaoyu on 15-11-30.
 */

public class PhotoSelectView extends FrameLayout implements SquarePhotoView.DeleteListener {
	private LinearLayout container;
	private PhotoSelectAdapter adapter;

	/**
	 * 总行数*
	 */
	private int totalRow = 0;

	/**
	 * 每个孩子的宽度,高度等于宽度*
	 */
	private int childWidth = 0;

	/**
	 * footer点击之后的回调*
	 */
	private FooterClickListener footerClickListener;

	/**
	 * deleteListener*
	 */
	private PhotoDeleteListener photoDeleteListener;

	@Override
	public void onDeleteClicked(String path) {
		if (photoDeleteListener != null) {
			photoDeleteListener.onPhotoDeleteClicked(path);
		}
	}

	public interface FooterClickListener {
		public void onFooterClicked();
	}

	public interface PhotoDeleteListener {
		public void onPhotoDeleteClicked(String path);
	}

	public PhotoSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PhotoSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PhotoSelectView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.photo_choose_view, this, true);
		container = (LinearLayout) findViewById(R.id.container);
	}

	public void setFooterClickListener(FooterClickListener footerClickListener) {
		this.footerClickListener = footerClickListener;
	}

	public void setPhotoDeleteListener(PhotoDeleteListener listener) {
		this.photoDeleteListener = listener;
	}

	public void setAdapter(PhotoSelectAdapter adapter) {
		this.adapter = adapter;
		if (adapter == null) {
			return;
		}
		/**移除孩子**/
		container.removeAllViews();
		/**计算一共有多少行**/
		int partOne = (adapter.getTotalCount() + 1) / adapter.getColCount();
		int partTwo = (adapter.getTotalCount() + 1) % adapter.getColCount() == 0 ? 0 : 1;
		/**最后一个是选择图片的加号**/
		totalRow = partOne + partTwo;

		/**计算每个孩子的宽度**/
		childWidth = (int) ((adapter.getWidth() - (adapter.getColCount() - 1) * adapter.getHorMargin() - getPaddingLeft() - getPaddingRight()) * 1.0f / adapter.getColCount());

		/**初始化数据**/
		for (int i = 0; i < totalRow; i++) {
			LinearLayout row = new LinearLayout(getContext());
			row.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < adapter.getColCount(); j++) {
				int index = i * adapter.getColCount() + j;
				/**初始化孩子的LayoutParams**/
				LinearLayout.LayoutParams childParams = null;
				if (j != adapter.getColCount() - 1) {
					childParams = new LinearLayout.LayoutParams(childWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
					childParams.rightMargin = adapter.getHorMargin();
				} else {
					childParams = new LinearLayout.LayoutParams(childWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
				}
				if (index < adapter.getTotalCount() + 1) {
					/**看是不是最后一个**/
					if (index != adapter.getTotalCount()) {
						SquarePhotoView child = new SquarePhotoView(getContext());
						child.setDeleteListener(this);
						row.addView(child, childParams);
						adapter.setView(child, index);
					} else {
						PhotoSelectFooterView child = new PhotoSelectFooterView(getContext());
						row.addView(child, childParams);
					}
				}
			}
			if (i != totalRow - 1) {
				LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				rowParams.bottomMargin = adapter.getVerMargin();
				container.addView(row, rowParams);
			} else {
				LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				container.addView(row, rowParams);
			}
		}
	}

	/**
	 * 最后一个点击点击添加图片的View*
	 */
	private class PhotoSelectFooterView extends FixWidthFrameLayout implements OnClickListener {
		private LinearLayout containerLL;

		public PhotoSelectFooterView(Context context) {
			super(context);
			init(context);
		}

		private void init(Context context) {
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater.inflate(R.layout.photo_select_footer_view, this, true);

			containerLL = (LinearLayout) findViewById(R.id.containerLL);
			containerLL.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.containerLL) {
				if (footerClickListener != null) {
					footerClickListener.onFooterClicked();
				}
			}
		}
	}
}
