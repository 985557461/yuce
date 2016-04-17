/**
 *
 */
package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.util.BitmapUtil;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanyuyong
 */
public class PreviewAdapter extends PagerAdapter {
	private Context mContext;
	private ImageLoader imageLoader = HCApplicaton.getInstance().getImageLoader();

	public PreviewAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	private List<PreviewItem> items = new ArrayList<PreviewItem>();

	public static class PreviewItem implements android.os.Parcelable {
		public String originPath;
		public String filterPath;
		public boolean isSelected = true;
		public long id;

		public static final Creator<PreviewItem> CREATOR = new Creator<PreviewItem>() {

			@Override
			public PreviewItem createFromParcel(Parcel source) {
				PreviewItem item = new PreviewItem();
				item.originPath = source.readString();
				item.filterPath = source.readString();
				item.isSelected = source.readInt() == 1;
				item.id = source.readLong();
				return item;
			}

			@Override
			public PreviewItem[] newArray(int size) {
				return new PreviewItem[size];
			}

		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(originPath);
			dest.writeString(filterPath);
			dest.writeInt(isSelected ? 1 : 0);
			dest.writeLong(id);
		}

		@Override
		public String toString() {
			return "Item :  originPath=" + originPath + "  ,  filterPath="
					+ filterPath + " , selected=" + isSelected;
		}
	}

	public List<PreviewItem> getItems() {
		return items;
	}

	public void setItems(List<PreviewItem> items) {
		this.items = items;
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(mContext, R.layout.preview_item, null);
		final PreviewItem item = items.get(position);
		view.setTag(item);
		PhotoView iv = (PhotoView) view.findViewById(R.id.img_view);
		String path = TextUtils.isEmpty(item.filterPath) ? item.originPath : item.filterPath;
		setImg(iv, path);
		container.addView(view);
		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	public void setImg(ImageView view, String path) {
		if (path.startsWith("http")) {
			imageLoader.displayImage(path, view, ImageLoaderUtil.Options_Common_Disc_Pic);
		} else {
			int degrees = BitmapUtil.readPictureDegree(path);
			Bitmap bitmap = BitmapUtil.loadBitmap(path, 1600);
			if (degrees != 0) {
				bitmap = BitmapUtil.rotate(bitmap, degrees);
				if (bitmap == null) {
					ToastUtil.makeShortText(mContext.getString(R.string.picLoadField));
					return;
				}
			}
			view.setImageBitmap(bitmap);
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
