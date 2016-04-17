package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.PicassoUtil;
import com.squareup.picasso.RequestCreator;

import java.util.LinkedHashMap;

public class PhotoAdapter extends CursorAdapter {
	private LinkedHashMap<Long, String> mSelectList = new LinkedHashMap<Long, String>();
	protected Context mContext;

	public PhotoAdapter(Context context, Cursor c) {
		super(context, c, true);
		mContext = context;
	}

	public void setSelectList(LinkedHashMap<Long, String> hashMap) {
		mSelectList.clear();
		if (hashMap != null) {
			mSelectList = hashMap;
		}
	}

	// /**
	// * 获取缩略图路径
	// *
	// * @param context
	// * @param photoId
	// */
	// private Uri getThumbnail(final Context context, final long photoId) {
	// Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
	// uri = uri.buildUpon()
	// .appendQueryParameter("orig_id", String.valueOf(photoId))
	// .build();
	// try {
	// context.getContentResolver().openInputStream(uri);
	// } catch (FileNotFoundException e) {
	// }
	// return uri;
	// }

	public void selectPhoto(Long photoId, String path) {
		mSelectList.put(photoId, path);
	}

	public void unSelectPhoto(Long photoId) {
		mSelectList.remove(photoId);
	}

	public boolean isSelectPhoto(Long photoId) {
		return mSelectList.containsKey(photoId);

	}

	public int getSelectPhotoCount() {
		return mSelectList.size();
	}

	public LinkedHashMap<Long, String> getSelectPhoto() {
		return mSelectList;
	}

	@Override
	public PhotoAlbum getItem(int position) {
		Cursor cursor = (Cursor) super.getItem(position);
		return getPhotoAlbum(cursor);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		PhotoGridItem item = new PhotoGridItem(context);
		item.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return item;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		PhotoGridItem item = (PhotoGridItem) view;
		PhotoAlbum photoItem = getPhotoAlbum(cursor);
		ImageView imageView = (ImageView) item.findViewById(R.id.photo_img_view);
		imageView.setImageBitmap(null);
		RequestCreator requestCreator = PicassoUtil.getInstance(context).buildRequestCreator(photoItem.getPath(), -1, -1, null);
		requestCreator = requestCreator.resize(100, 100, true);
		requestCreator = requestCreator.centerCrop();
		requestCreator.into(imageView);
		boolean flag = isSelectPhoto(photoItem.getPhotoID());
		item.setChecked(flag);
		if (flag) {
			item.showCheck();
		} else {
			item.hideCheck();
		}
	}

	protected PhotoAlbum getPhotoAlbum(Cursor cursor) {
		PhotoAlbum photoAlbum = new PhotoAlbum();
		long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
		photoAlbum.setPhotoID(id);
		photoAlbum.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
		photoAlbum.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
		photoAlbum.setDirId(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)));
		return photoAlbum;
	}

}
