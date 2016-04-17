package com.example.YuCeClient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.xuanshang.photo_choose.PhotoSelectAdapter;
import com.example.YuCeClient.ui.xuanshang.photo_choose.PhotoSelectView;
import com.example.YuCeClient.ui.xuanshang.photo_choose.SquarePhotoView;
import com.example.YuCeClient.util.DisplayUtil;
import com.example.YuCeClient.util.IntentUtils;
import com.example.YuCeClient.widget.cropimage.ActivityCropImage;
import com.example.YuCeClient.widget.photo.PhotoActivity;
import com.example.YuCeClient.widget.photo.PhotoAlbumActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreay on 15-11-11.
 */
public class ActivityTest extends ActivityBase implements PhotoSelectView.FooterClickListener {
	private TextView button;
	private PhotoSelectView photoSelectView;
	private PhotoSelectAdapter adapter;

	/**
	 * modify avatar about
	 * *
	 */
	private static final int kActivitySettingSelectPicRequest = 101;
	private static final int kPhotoCropImageRequest = 102;
	private String avatarPath = "";
	private List<String> pathsList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_test);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		button = (TextView) findViewById(R.id.button);
		photoSelectView = (PhotoSelectView) findViewById(R.id.photoSelectView);
		photoSelectView.setFooterClickListener(this);
	}

	@Override
	protected void initViews() {
	}

	@Override
	protected void setListeners() {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter = new MyPhotoSelectAdapter();
				photoSelectView.setAdapter(adapter);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
			String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
			if (paths != null && paths.length <= 0) {
				return;
			}
			if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
				if (!TextUtils.isEmpty(paths[0])) {
					ActivityCropImage.openForResult(this, paths[0], 750, 750, true, kPhotoCropImageRequest);
					return;
				}
			} else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
				if (!TextUtils.isEmpty(paths[0])) {
					avatarPath = paths[0];
					pathsList.add(avatarPath);
					photoSelectView.setAdapter(adapter);
				}
			}
		} else if (requestCode == kPhotoCropImageRequest && resultCode == RESULT_OK) {
			avatarPath = data.getStringExtra(ActivityCropImage.kCropImagePath);
			pathsList.add(avatarPath);
			photoSelectView.setAdapter(adapter);
			return;
		}
	}

	@Override
	public void onFooterClicked() {
		Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, ActivityTest.this);
		startActivityForResult(intent, kActivitySettingSelectPicRequest);
	}

	private class MyPhotoSelectAdapter extends PhotoSelectAdapter<SquarePhotoView> {
		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public int getTotalCount() {
			return pathsList.size();
		}

		@Override
		public int getColCount() {
			return 3;
		}

		@Override
		public int getHorMargin() {
			return DisplayUtil.dip2px(ActivityTest.this, 10);
		}

		@Override
		public int getVerMargin() {
			return DisplayUtil.dip2px(ActivityTest.this, 10);
		}

		@Override
		public void setView(SquarePhotoView view, int position) {
			view.setData(pathsList.get(position));
		}
	}
}
