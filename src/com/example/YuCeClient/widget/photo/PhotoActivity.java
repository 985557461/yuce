package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.IntentUtils;
import com.example.YuCeClient.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author yinxinya
 * @version 1.0
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @created
 * @changeRecord
 */
// 单个目录中照片的展示的Activity
public class PhotoActivity extends ActivityBase implements OnClickListener {
	private ImageView headLeft;
	private TextView headTitle;
	private ImageView headRight;
	private LoadingWrapperLayout loadingWrapperLayout;
	private GridView gv;
	private TextView photo_count;// 当前选中个数
	private TextView photo_ok;// 保存按钮
	private TextView photo_preview;// 预览按钮

	private PhotoAlbum album;
	private PhotoAdapter adapter;

	/**
	 * intent data
	 * *
	 */
	private int mMaxCount = 1;// 最多选择照片的张数
	private String rightBottomBtnName = "";// 右下角按钮的名字
	private boolean showCamera = false; // 是否显示照相机按钮

	private static final int REQUEST_PREVIEW = 200;
	private ArrayList<String> mSelectList = new ArrayList<String>();

	/**
	 * 单张照片的来源
	 * *
	 */
	public static final String kWhereFrom = "where_from";
	public static final String kFromCamera = "from_camera";
	public static final String kFromAlbum = "from_album";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getDataFromIntent(getIntent());
		setContentView(R.layout.activity_photoalbum_gridview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		headLeft = (ImageView) findViewById(R.id.tv_head_left);
		headTitle = (TextView) findViewById(R.id.tv_head_title);
		headRight = (ImageView) findViewById(R.id.right_image);
		photo_count = (TextView) findViewById(R.id.photo_count);
		photo_preview = (TextView) findViewById(R.id.photo_preview);
		photo_ok = (TextView) findViewById(R.id.photo_ok);
		loadingWrapperLayout = (LoadingWrapperLayout) findViewById(R.id.photo_wrapper);
		gv = (GridView) findViewById(R.id.photo_gridview);
	}

	@Override
	protected void initViews() {
		headLeft.setImageResource(R.drawable.icon_black_close);
		headTitle.setText(getString(R.string.selectpic));
		if (showCamera) {
			headRight.setVisibility(View.VISIBLE);
			headRight.setImageResource(R.drawable.icon_select_photo_camera);
		} else {
			headRight.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(rightBottomBtnName)) {
			photo_ok.setText(rightBottomBtnName);
		}
		loadingWrapperLayout.showLoadingLayer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Cursor cursor = getCursor(getApplication());
				PhotoActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadingWrapperLayout.hideLoadingLayer();
						adapter = new PhotoAdapter(PhotoActivity.this, cursor);
						gv.setAdapter(adapter);
						gv.setOnItemClickListener(gvItemClickListener);
						setSelectCount(adapter.getSelectPhotoCount());
					}
				});
			}
		}).start();
	}

	@Override
	protected void setListeners() {
		headLeft.setOnClickListener(this);
		headRight.setOnClickListener(this);
		photo_preview.setOnClickListener(this);
		photo_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_image:
			Intent intent = IntentUtils.goToCameraIntent(this, 750, 750);
			startActivityForResult(intent, PhotoAlbumActivity.kCameraActivityRequestCode);
			break;
		case R.id.tv_head_left:
			finish();
			break;
		case R.id.photo_ok:
			onSelect(getSelect(), kFromAlbum);
			break;
		case R.id.photo_preview:
			LinkedHashMap<Long, String> hashMap = adapter.getSelectPhoto();
			if (hashMap != null && !hashMap.isEmpty()) {
				ArrayList<PreviewAdapter.PreviewItem> items = new ArrayList<PreviewAdapter.PreviewItem>();
				Iterator<Long> iterator = hashMap.keySet().iterator();
				while (iterator.hasNext()) {
					PreviewAdapter.PreviewItem item = new PreviewAdapter.PreviewItem();
					item.id = iterator.next();
					item.originPath = hashMap.get(item.id);
					items.add(item);
				}
				Intent preview_intent = new Intent(this, PreviewActivity.class);
				preview_intent.putParcelableArrayListExtra("items", items);
				startActivityForResult(preview_intent, REQUEST_PREVIEW);
			}
			break;
		default:
			break;
		}
	}

	private void getDataFromIntent(Intent intent) {
		if (getIntent() != null) {
			album = (PhotoAlbum) intent.getExtras().get(PhotoAlbumActivity.Key_Album);
			mMaxCount = intent.getIntExtra(PhotoAlbumActivity.Key_Max_Count, 1);
			rightBottomBtnName = intent.getStringExtra(PhotoAlbumActivity.Key_Right_Bottom_Button_Name);
			mSelectList = intent.getStringArrayListExtra(PhotoAlbumActivity.Key_SelectPaths);
			showCamera = intent.getBooleanExtra(PhotoAlbumActivity.Key_Show_Camera, true);
		}
	}

	// 设置获取图片的字段信息
	private static final String[] STORE_IMAGES = { MediaStore.Images.Media.DISPLAY_NAME, // 显示的名称
			MediaStore.Images.Media.LATITUDE, // 维度
			MediaStore.Images.Media.LONGITUDE, // 经度
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
			MediaStore.Images.Media.DATA // 路径
	};

	/**
	 * 获取图片信息
	 *
	 * @param context
	 * @return
	 */
	private Cursor getCursor(Context context) {
		String where = MediaStore.Images.Media.BUCKET_ID + " = " + album.getDirId();
		Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, where, MediaStore.Images.Media.DATE_ADDED + " DESC");
		return cursor;
	}

	/**
	 * 相册图片已经被删除
	 *
	 * @param path
	 */
	private void notifyPhotoDeleted(String path) {
		ToastUtil.makeText(getApplicationContext(), R.string.photo_file_delete, Toast.LENGTH_SHORT).show();
		Uri data = Uri.parse("file:///" + path);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
	}

	private OnItemClickListener gvItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			PhotoAlbum photoItem = adapter.getItem(position);
			if (!TextUtils.isEmpty(photoItem.getPath())) {
				File file = new File(photoItem.getPath());
				if (!file.exists()) {
					notifyPhotoDeleted(photoItem.getPath());
					return;
				}
			} else {
				notifyPhotoDeleted(photoItem.getPath());
				return;
			}
			PhotoGridItem photoGridItem = (PhotoGridItem) view;
			boolean checked = adapter.isSelectPhoto(photoItem.getPhotoID());
			if (checked) {
				adapter.unSelectPhoto(photoItem.getPhotoID());
				photoGridItem.toggle();
			} else {
				// 如果是单选操作
				if (mMaxCount == 1) {
					Set<Long> photoIds = adapter.getSelectPhoto().keySet();
					for (Long photoId : photoIds) {
						adapter.unSelectPhoto(photoId);
						adapter.notifyDataSetChanged();
						break;
					}
					adapter.selectPhoto(photoItem.getPhotoID(), photoItem.getPath());
					photoGridItem.toggle();
				} else {
					if (adapter.getSelectPhotoCount() == mMaxCount) {
						String limit = String.format(getString(R.string.photo_choose_limit), mMaxCount);
						ToastUtil.makeText(getApplicationContext(), limit, Toast.LENGTH_SHORT).show();
						return;
					} else {
						adapter.selectPhoto(photoItem.getPhotoID(), photoItem.getPath());
						photoGridItem.toggle();
					}
				}
			}
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.checkshake);
			setSelectCount(adapter.getSelectPhotoCount());
			if (adapter.getSelectPhotoCount() != 0) {
				photo_count.startAnimation(animation);
				if (!checked) {
					view = photoGridItem.findViewById(R.id.photo_select);
					view.startAnimation(animation);
				}
			}
		}
	};

	/**
	 * 设置选中个数
	 *
	 * @param count
	 */
	private void setSelectCount(int count) {
		if (count == 0) {
			photo_count.setVisibility(View.GONE);
		} else {
			photo_count.setVisibility(View.VISIBLE);
		}
		photo_count.setText(count + "");
	}

	private String[] getSelect() {
		LinkedHashMap<Long, String> hashMap = adapter.getSelectPhoto();
		String[] paths = new String[hashMap.size()];
		Iterator<Long> iterator = hashMap.keySet().iterator();
		int n = 0;
		while (iterator.hasNext()) {
			paths[n] = hashMap.get(iterator.next());
			n++;
		}
		return paths;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_PREVIEW:
				ArrayList<PreviewAdapter.PreviewItem> items = data.getParcelableArrayListExtra("items");
				LinkedHashMap<Long, String> hashMap = new LinkedHashMap<Long, String>();
				if (items != null && !items.isEmpty()) {
					for (PreviewAdapter.PreviewItem item : items) {
						if (item.isSelected) {
							String path = item.filterPath;
							if (TextUtils.isEmpty(path)) {
								path = item.originPath;
							}
							hashMap.put(item.id, path);
						}
					}
				}
				if (data.getIntExtra("flag", 0) == 0) {// 点击完成
					adapter.setSelectList(hashMap);
					adapter.notifyDataSetChanged();
					onSelect(getSelect(), kFromAlbum);
				} else {// 点击返回
					adapter.setSelectList(hashMap);
					adapter.notifyDataSetChanged();
					setSelectCount(adapter.getSelectPhotoCount());
				}
				break;
			case PhotoAlbumActivity.kCameraActivityRequestCode:
				String path = data.getStringExtra(ActivityCapture.kPhotoPath);
				onSelect(new String[] { path }, kFromCamera);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 选定图片
	 *
	 * @param paths
	 */
	private void onSelect(String[] paths, String from) {
		if (paths == null || paths.length <= 0) {
			ToastUtil.makeShortText(getString(R.string.chosePic));
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(kWhereFrom, from);
		intent.putExtra(PhotoAlbumActivity.Key_SelectPaths, paths);
		setResult(RESULT_OK, intent);
		finish();
	}
}
