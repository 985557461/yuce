package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.ActivityBase;

import java.util.ArrayList;

/**
 * @author yinxinya
 * @version 1.0
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @created
 * @changeRecord
 */
//照片的目录选择
public class PhotoAlbumActivity extends ActivityBase implements OnClickListener {
	private ImageView headLeft;
	private TextView headTitle;
	private LoadingWrapperLayout loadingWrapperLayout;
	private GridView albumGV;
	private PhotoAlbumAdapter mAlbumAdapter;
	private Account account;
	/**
	 * intent param
	 * *
	 */
	public static final String Key_SelectPaths = "select_paths";
	public static final String Key_Right_Bottom_Button_Name = "right_bottom_name";
	public static final String Key_Show_Camera = "show_camera";
	public static final String Key_Max_Count = "max_count";
	public static final String Key_Album = "album";
	private String rightBottomBtnName = "";
	private int mMaxCount = 1;
	private boolean showCamera;
	private ArrayList<String> mSelectList = new ArrayList<String>();

	/**
	 * request code
	 * *
	 */
	public static final int kPhotoActivityRequestCode = 10001;
	public static final int kCameraActivityRequestCode = 10002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		account = HCApplicaton.getInstance().getAccount();
		getDataFromIntent(getIntent());
		setContentView(R.layout.activity_photoalbum);
		super.onCreate(savedInstanceState);
	}

	private void getDataFromIntent(Intent intent) {
		if (getIntent() != null) {
			mMaxCount = intent.getIntExtra(Key_Max_Count, 1);
			rightBottomBtnName = intent.getStringExtra(Key_Right_Bottom_Button_Name);
			mSelectList = intent.getStringArrayListExtra(Key_SelectPaths);
			showCamera = intent.getBooleanExtra(Key_Show_Camera, true);
		}
	}

	@Override
	protected void getViews() {
		headLeft = (ImageView) findViewById(R.id.tv_head_left);
		headTitle = (TextView) findViewById(R.id.tv_head_title);
		loadingWrapperLayout = (LoadingWrapperLayout) findViewById(R.id.photo_album_wrapper);
		loadingWrapperLayout.showLoadingLayer();
		albumGV = (GridView) findViewById(R.id.album_gridview);
	}

	@Override
	protected void initViews() {
		headTitle.setText(R.string.photo);
		String dirId = account.lastSelectDir;
		if (!TextUtils.isEmpty(dirId)) {
			PhotoAlbum photoAlbum = existDir(this, dirId);
			if (photoAlbum != null) {
				Intent intent = new Intent(PhotoAlbumActivity.this, PhotoActivity.class);
				intent.putExtra(Key_Album, photoAlbum);
				intent.putExtra(Key_Max_Count, mMaxCount);
				intent.putExtra(Key_SelectPaths, mSelectList);
				intent.putExtra(Key_Show_Camera, showCamera);
				//可以自定义右下角的按钮的名字
				if (!TextUtils.isEmpty(rightBottomBtnName)) {
					intent.putExtra(Key_Right_Bottom_Button_Name, rightBottomBtnName);
				}
				startActivityForResult(intent, kPhotoActivityRequestCode);// 记住用户偏好 下次默认打开该文件夹
			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Cursor cursor = getCursor(getApplication());
				PhotoAlbumActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadingWrapperLayout.hideLoadingLayer();
						if (cursor == null || cursor.getCount() == 0) {
							findViewById(R.id.photo_empty).setVisibility(View.VISIBLE);
						} else {
							findViewById(R.id.photo_empty).setVisibility(View.GONE);
							mAlbumAdapter = new PhotoAlbumAdapter(getApplicationContext(), cursor);
							albumGV.setAdapter(mAlbumAdapter);
							albumGV.setOnItemClickListener(albumClickListener);
						}
					}
				});
			}
		}).start();
	}

	@Override
	protected void setListeners() {
		headLeft.setOnClickListener(this);
	}

	// 设置获取图片的字段信息
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // 显示的名称
			MediaStore.Images.Media.LATITUDE, // 维度
			MediaStore.Images.Media.LONGITUDE, // 经度
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
			MediaStore.Images.Media.DATA // 路径
	};

	/**
	 * 是否存在文件夹
	 *
	 * @param
	 * @return
	 */
	private PhotoAlbum existDir(Context context, String dirId) {
		String selection = MediaStore.Images.Media.BUCKET_ID + " = " + dirId + " ) group by ( " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
		Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				selection, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
		if (cursor != null && cursor.moveToNext()) {
			PhotoAlbum photoAlbum = new PhotoAlbum();
			long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
			photoAlbum.setPhotoID(id);
			photoAlbum.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
			photoAlbum.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
			photoAlbum.setDirId(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)));
			return photoAlbum;
		}
		return null;
	}

	/**
	 * 获取文件分类信息（按照文件夹名称分类）
	 *
	 * @param context
	 * @return
	 */
	private Cursor getCursor(Context context) {
		String selection = " 1=1 ) group by ( " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
		Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				selection, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
		return cursor;
	}

	/**
	 * 相册点击事件
	 */
	OnItemClickListener albumClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(PhotoAlbumActivity.this, PhotoActivity.class);
			PhotoAlbum photoAlbum = mAlbumAdapter.getItem(position);
			intent.putExtra(Key_Album, photoAlbum);
			intent.putExtra(Key_Max_Count, mMaxCount);
			intent.putExtra(Key_SelectPaths, mSelectList);
			intent.putExtra(Key_Show_Camera, showCamera);
			//可以自定义右下角的按钮的名字
			if (!TextUtils.isEmpty(rightBottomBtnName)) {
				intent.putExtra(Key_Right_Bottom_Button_Name, rightBottomBtnName);
			}
			startActivityForResult(intent, kPhotoActivityRequestCode);// 记住用户偏好 下次默认打开该文件夹
			account.lastSelectDir = photoAlbum.getDirId() + "";
			account.saveMeInfoToPreference();
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == kPhotoActivityRequestCode) {
			if (mSelectList == null) {
				mSelectList = new ArrayList<String>();
			}
			String[] paths = data.getStringArrayExtra(Key_SelectPaths);
			if (paths != null) {
				for (int i = 0; i < paths.length; i++) {
					mSelectList.add(paths[i]);
				}
			}
			paths = new String[mSelectList.size()];
			mSelectList.toArray(paths);
			data.putExtra(Key_SelectPaths, paths);
			setResult(resultCode, data);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_head_left:
			finish();
			break;
		case R.id.tv_head_right:
			break;
		}
	}
}
