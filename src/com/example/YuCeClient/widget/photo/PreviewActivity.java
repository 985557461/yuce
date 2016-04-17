package com.example.YuCeClient.widget.photo;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.photoview.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 照片预览界面
 */

public class PreviewActivity extends ActivityBase implements OnClickListener {
	private HackyViewPager mPage;
	private PreviewAdapter mAdapter;
	private View toFilter;
	private View back_btn, ok_btn, del_btn;
	private ImageView check_box;
	private boolean isCurrentSelected = true;
	private TextView title;
	private TextView photo_count;

	public static final int MODE_SELECT = 0;
	public static final int MODE_DEL = 1;
	public static final int FLAG_OK = 0;
	public static final int FLAG_BACK = 1;

	private int mode = MODE_SELECT;

	private final static int REQUEST_FOR_FILTER = 100;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.preview_layout);
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {

	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {

	}

	private void initView() {
		mPage = (HackyViewPager) findViewById(R.id.vPager);
		mPage.setOffscreenPageLimit(1);
		mAdapter = new PreviewAdapter(this);
		title = (TextView) findViewById(R.id.title);
		photo_count = (TextView) findViewById(R.id.photo_count);
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", MODE_SELECT);
		int pos = intent.getIntExtra("position", 0);

		List<PreviewAdapter.PreviewItem> items = intent.getParcelableArrayListExtra("items");
		mAdapter.setItems(items);
		mPage.setAdapter(mAdapter);
		if (pos >= 0 && pos < mAdapter.getCount()) {
			mPage.setCurrentItem(pos);
		}
		mPage.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				if (mAdapter.getItems() != null && pos < mAdapter.getItems().size()) {
					PreviewAdapter.PreviewItem item = mAdapter.getItems().get(pos);
					if (item != null) {
						if(item.isSelected){
							check_box.setImageResource(R.drawable.icon_selected_round);
							isCurrentSelected = true;
						}else{
							check_box.setImageResource(R.drawable.icon_normal_round);
							isCurrentSelected = false;
						}
					}
				}

				if (mode == MODE_DEL) {
					title.setText((pos + 1)
							+ "/"
							+ (mAdapter.getItems() != null ? mAdapter
							.getItems().size() : 0));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		toFilter = findViewById(R.id.btn_filter);
		toFilter.setOnClickListener(this);
		toFilter.setVisibility((HCApplicaton.android_show_filter && Build.VERSION.SDK_INT >= 8) ? View.VISIBLE
				: View.GONE);

		check_box = (ImageView) findViewById(R.id.check_box);
		check_box.setOnClickListener(this);

		back_btn = findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		ok_btn = findViewById(R.id.btn_ok);
		ok_btn.setOnClickListener(this);

		del_btn = findViewById(R.id.btn_delete);
		del_btn.setOnClickListener(this);

		check_box.setVisibility(mode == MODE_SELECT ? View.VISIBLE : View.GONE);
		del_btn.setVisibility(mode == MODE_DEL ? View.VISIBLE : View.GONE);
		ok_btn.setVisibility(mode == MODE_SELECT ? View.VISIBLE : View.GONE);

		if (mode == MODE_DEL) {
			title.setText((pos + 1) + "/" + (items != null ? items.size() : 0));
		} else {
			title.setText(R.string.preview_title);
		}

		changeSelectedNum();
	}

	private void toFilter() {
		int pos = mPage.getCurrentItem();
		PreviewAdapter.PreviewItem item = mAdapter.getItems().get(pos);

		String path = TextUtils.isEmpty(item.filterPath) ? item.originPath : item.filterPath;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_FOR_FILTER:
				Uri imageUri = data.getData();
				if (imageUri != null) {
					PreviewAdapter.PreviewItem item = mAdapter.getItems().get(
							mPage.getCurrentItem());
					String path = getImagePath(imageUri);
					item.filterPath = path;
					View view = mPage.findViewWithTag(item);
					ImageView iv = (ImageView) view.findViewById(R.id.img_view);
					if (iv != null) {
						mAdapter.setImg(iv, item.filterPath);
					}
				}
				break;
			}
		}
	}

	/*
	 * 获取图片本地路径
	 * 
	 * @param uri
	 * 
	 * @return
	 */
	public String getImagePath(Uri uri) {
		String path = null;
		if (uri == null) {
			return path;
		}
		if ("content".equals(uri.getScheme())) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			cursor.moveToFirst();
			String document_id = cursor.getString(0);
			document_id = document_id
					.substring(document_id.lastIndexOf(":") + 1);
			cursor.close();

			cursor = getContentResolver()
					.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							null, MediaStore.Images.Media._ID + " = ? ",
							new String[] { document_id }, null);
			cursor.moveToFirst();
			path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
		} else if ("file".equals(uri.getScheme())) {
			path = uri.getPath();
		}
		return path;
	}

	private void deletDialog(final int pos) {
		Builder builder = new Builder(this);
		builder.setTitle(getString(R.string.deletePic));
		builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAdapter.getItems().remove(pos);
				mAdapter.notifyDataSetChanged();

				if (mAdapter.getItems().size() == 0) {
					back(FLAG_BACK);
				} else {
					title.setText((mPage.getCurrentItem() + 1)
							+ "/"
							+ (mAdapter.getItems() != null ? mAdapter
							.getItems().size() : 0));
				}
			}
		});
		builder.create().show();
	}

	@Override
	public void onBackPressed() {
		back(FLAG_BACK);
	}

	private void back(int flag) {
		if (mAdapter != null && mAdapter.getItems() != null) {
			int num = 0;
			for (PreviewAdapter.PreviewItem item : mAdapter.getItems()) {
				if (item.isSelected) {
					num++;
				}
			}
			if (num == 0) {
				ToastUtil.makeShortText(getString(R.string.choseAtLeastPic));
				return;
			}
		}
		Intent data = new Intent();
		data.putParcelableArrayListExtra("items",
				(ArrayList<? extends Parcelable>) mAdapter.getItems());
		data.putExtra("flag", flag);
		setResult(RESULT_OK, data);
		finish();
	}

	private void changeSelectedNum() {
		if (mode == MODE_DEL) {
			photo_count.setVisibility(View.GONE);
			return;
		}
		if (mAdapter != null && mAdapter.getItems() != null) {
			int num = 0;
			for (PreviewAdapter.PreviewItem item : mAdapter.getItems()) {
				if (item.isSelected) {
					num++;
				}
			}
			if (num == 0) {
				photo_count.setVisibility(View.GONE);
			} else {
				photo_count.setText(String.valueOf(num));
				photo_count.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_filter:
			toFilter();
			break;

		case R.id.back_btn:
			back(FLAG_BACK);
			break;

		case R.id.btn_delete:
			int postion = mPage.getCurrentItem();
			if (mAdapter.getItems() != null
					&& postion < mAdapter.getItems().size()) {
				deletDialog(postion);
			}
			break;

		case R.id.btn_ok:
			back(FLAG_OK);
			break;

		case R.id.check_box:
			int pos = mPage.getCurrentItem();
			if (mAdapter.getItems() != null && pos < mAdapter.getItems().size()) {
				PreviewAdapter.PreviewItem item = mAdapter.getItems().get(pos);
				if (item != null) {
					if(isCurrentSelected){
						isCurrentSelected = false;
						check_box.setImageResource(R.drawable.icon_normal_round);
					}else{
						isCurrentSelected = true;
						check_box.setImageResource(R.drawable.icon_selected_round);
					}
					item.isSelected = isCurrentSelected;
				}
			}
			changeSelectedNum();
			break;
		}
	}
}
