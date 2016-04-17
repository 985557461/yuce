package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.PicassoUtil;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 照片选择控件
 * 
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author yinxinya
 * @version 1.0
 * @created
 * @changeRecord
 */
public class PhotoChooseView extends GridView implements OnItemClickListener,
		DragGridView.OnChanageListener {
	private PhotoChooseAdapter mAdapter;
	private int mNumColumns;
	private int mHorizontalSpacing;
	private int mVerticalSpacing;
	private String r;

	public PhotoChooseView(Context context) {
		super(context);
		init();
	}

	public PhotoChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PhotoChooseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// Log.e("xyds", "onMeasure" + getMeasuredHeight());
		// if (mAdapter != null && mAdapter.getCount() == 1) {
		// mItemHeight = getMeasuredHeight();
		// }
	}

	@Override
	public void setNumColumns(int numColumns) {
		super.setNumColumns(numColumns);
		mNumColumns = numColumns;
	}

	@Override
	public void setHorizontalSpacing(int horizontalSpacing) {
		super.setHorizontalSpacing(horizontalSpacing);
		mHorizontalSpacing = horizontalSpacing;
	}

	@Override
	public void setVerticalSpacing(int verticalSpacing) {
		super.setVerticalSpacing(verticalSpacing);
		mVerticalSpacing = verticalSpacing;
	}

	private void init() {
		mAdapter = new PhotoChooseAdapter(getContext());

		setOnItemClickListener(PhotoChooseView.this);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				setAdapter(mAdapter);
			}
		}, 10);
		// setOnChangeListener(PhotoChooseView.this);
		// setDragResponseMS(500);

	}

	public void setR(String r) {
		this.r = r;
	}

	public PhotoChooseAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mAdapter.isMorePosition(position)) {
			if (getContext() instanceof PhotoChooseActicity) {
				PhotoChooseActicity photoChooseActicity = (PhotoChooseActicity) getContext();
				int maxCount = mAdapter.getMaxCount()- mAdapter.getPhotos().size();
				photoChooseActicity.showPicPhotoDialog(mAdapter.getPhotos(),maxCount);
			}
		} else {// 预览
			if (getContext() instanceof PhotoChooseActicity) {
				PhotoChooseActicity photoChooseActicity = (PhotoChooseActicity) getContext();
				List<String> sList = mAdapter.getPhotos();
				String[] paths = new String[sList.size()];
				sList.toArray(paths);
				if (paths != null && paths.length != 0) {
					ArrayList<PreviewAdapter.PreviewItem> items = new ArrayList<PreviewAdapter.PreviewItem>();
					for (String path : paths) {
						PreviewAdapter.PreviewItem item = new PreviewAdapter.PreviewItem();
						item.originPath = path;
						items.add(item);
					}
					photoChooseActicity.preView(position, items, r);
				}
			}
		}

	}

	public class PhotoChooseAdapter extends BaseAdapter {
		private ArrayList<String> mPaths = new ArrayList<String>();// 照片路径
		private Context mContext;
		private int mMax = 9;// 最多数量
		private int mHidePosition = -1;

		public PhotoChooseAdapter(Context context) {
			mContext = context;
		}

		public void setMaxCount(int max) {
			mMax = max;
		}

		public int getMaxCount() {
			return mMax;
		}

		/**
		 * 隐藏item项目（适配长按拖动功能）
		 * 
		 * @param position
		 */
		public void setHideItem(int position) {
			mHidePosition = position;
		}

		/**
		 * 获取当前选中图片
		 * 
		 * @return
		 */
		public ArrayList<String> getPhotos() {
			return mPaths;
		}

		public void setPhotos(ArrayList<String> photos) {
			if (photos != null) {
				mPaths = photos;
				notifyDataSetChanged();
			}

		}

		/**
		 * 清空数据
		 */
		public void clear() {
			mPaths.clear();
			notifyDataSetChanged();
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			// 本地图片有可能被删除 这里做过滤
			ArrayList<String> paths = new ArrayList<String>();
			for (int i = 0; i < mPaths.size(); i++) {
				String path = mPaths.get(i);
				File file = new File(path);
				if (path.startsWith("http")||file.exists()) {
					paths.add(path);
				}
			}
			mPaths.clear();
			mPaths = paths;
			if (getChildCount() > 0) {
				int itemHeight = getChildAt(0).getMeasuredHeight();
				if (itemHeight != -1) {// 由于gridview嵌套在scrollview里面 所以需要对高度进行计算
										// 保证能全部显示
					ViewGroup.LayoutParams layoutParams = getLayoutParams();
					layoutParams.height = itemHeight
							* ((getCount() - 1) / mNumColumns + 1)
							+ (mVerticalSpacing) * ((getCount() - 1)/getNumColumns());
					setLayoutParams(layoutParams);
				}
			}
		}

		/**
		 * 添加图片
		 *
		 * @param
		 */
		public void addPhoto(String[] paths) {
			if (paths == null) {
				return;
			}
			for (int i = 0; i < paths.length; i++) {
				if (!TextUtils.isEmpty(paths[i])) {
					mPaths.add(paths[i]);
				}
			}
			notifyDataSetChanged();
		}

		/**
		 * 交换位置
		 *
		 * @param form
		 * @param to
		 */
		public void swapItem(int form, int to) {
			String fromString = getItem(form);
			String toString = getItem(to);
			mPaths.remove(form);
			mPaths.add(form, toString);
			mPaths.remove(to);
			mPaths.add(to, fromString);
		}

		/**
		 * 添加图片
		 *
		 * @param path
		 */
		public void addPhoto(String path) {
			if (!TextUtils.isEmpty(path)) {
				mPaths.add(path);
			}
			notifyDataSetChanged();
		}

		/**
		 * 是否是添加更多图片选项
		 *
		 * @param position
		 * @return
		 */
		private boolean isMorePosition(int position) {
			if (position == getCount() - 1) {
				if (mPaths.size() < mMax) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 删除照片
		 */
		public void deletePhoto(String path) {
			if (!TextUtils.isEmpty(path)) {
				mPaths.remove(path);
			}
			notifyDataSetChanged();

		}

		@Override
		public int getCount() {
			int count = mPaths.size();
			if (count < mMax) {
				count++;
			} else if (count >= mMax) {
				count = mMax;
			}
			return count;
		}

		@Override
		public String getItem(int position) {
			return mPaths.get(position).toString();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = null;// 在低版本的系统 复用item会导致长按拖动功能失效（item项目消失
								// 所以这里不复用）而且多图选择器本身也只有8项 复用也没意义
			if (convertView == null) {
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				GridView gridView = (GridView) parent;
				int width = (gridView.getMeasuredWidth()- gridView.getPaddingLeft()
						- gridView.getPaddingRight() - mHorizontalSpacing* (mNumColumns - 1))/ mNumColumns;
				LayoutParams layoutParams = new LayoutParams(width, width);
				imageView.setLayoutParams(layoutParams);
				convertView = imageView;
			}
			ImageView imageView = (ImageView) convertView;
			if (isMorePosition(position)) {
				imageView.setImageResource(R.drawable.btn_add_photo);
			} else {
				RequestCreator requestCreator = PicassoUtil.getInstance(mContext).buildRequestCreator(getItem(position), -1,-1, null);
				requestCreator = requestCreator.resize(150, 150, true);
				requestCreator = requestCreator.centerCrop();
				requestCreator.into(imageView);
				imageView.setBackgroundColor(Color.TRANSPARENT);
			}
			if (mHidePosition == position) {
				convertView.setVisibility(View.INVISIBLE);
			} else {
				convertView.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}

	// @Override
	// boolean isInvalidPosition(int position) {
	// if (position == AbsListView.INVALID_POSITION
	// || mAdapter.isMorePosition(position)) {
	// return true;
	// }
	// return false;
	// }

	@Override
	public void onChange(int form, int to) {
		mAdapter.swapItem(form, to);
		mAdapter.setHideItem(to);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDrapStop() {
		mAdapter.setHideItem(-1);
		mAdapter.notifyDataSetChanged();
	}
}