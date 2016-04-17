package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.PicassoUtil;
import com.squareup.picasso.RequestCreator;

/**
 * 
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author yinxinya
 * @version 1.0
 * @created
 * @changeRecord
 */
public class PhotoAlbumAdapter extends PhotoAdapter {

	public PhotoAlbumAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View convertView = (LinearLayout) View.inflate(context,
				R.layout.photoalbum_item, null);
		ViewHolder holder = new ViewHolder();
		holder.iv = (ImageView) convertView
				.findViewById(R.id.photoalbum_item_image);
		holder.tv = (TextView) convertView
				.findViewById(R.id.photoalbum_item_name);
		convertView.setTag(holder);
		return convertView;

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		PhotoAlbum photoAibum = getPhotoAlbum(cursor);
		holder.iv.setImageBitmap(null);
		RequestCreator requestCreator = PicassoUtil.getInstance(context)
				.buildRequestCreator(photoAibum.getPath(), -1, -1, null);
		requestCreator = requestCreator.resize(150, 150, true);
		requestCreator = requestCreator.centerCrop();
		requestCreator.into(holder.iv);
		holder.tv.setText(photoAibum.getName() + " ( " + photoAibum.getCount()
				+ " )");
	}

	@Override
	protected PhotoAlbum getPhotoAlbum(Cursor cursor) {
		PhotoAlbum photoAlbum = super.getPhotoAlbum(cursor);
		photoAlbum.setCount(getPhotoCount(photoAlbum.getDirId()));
		return photoAlbum;
	}

	/**
	 * 获取文件夹下图片个数
	 * 
	 * @param dirId
	 * @return
	 */
	public int getPhotoCount(long dirId) {
		String where = MediaStore.Images.Media.BUCKET_ID + " =  " + dirId;
		Cursor cursor = MediaStore.Images.Media
				.query(mContext.getContentResolver(),
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
						where, null);
		int count = 0;
		if (cursor != null) {
			count = cursor.getCount();
			cursor.close();
		}
		return count;
	}

	static class ViewHolder {
		ImageView iv;
		TextView tv;
	}
}
