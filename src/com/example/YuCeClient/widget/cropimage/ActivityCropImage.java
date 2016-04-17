package com.example.YuCeClient.widget.cropimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.PathManager;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.BitmapUtil;
import com.example.YuCeClient.util.FileUtil;
import com.example.YuCeClient.util.ToastUtil;

/**
 * Created by xiaoyu on 14-9-15.
 */
public class ActivityCropImage extends ActivityBase implements View.OnClickListener {
	private TextView cancel;
	private TextView finish;
	private ImageView lockImage;
	private CropImageView cropImageView;
	private BitmapDrawable drawable;
	private Bitmap bitmap;

	public static String kImagePath = "image_path";
	public String imagePath = "";

	public static String kCropImagePath = "cropImagePath";
	public static String kShowLockImageButton = "show_lock_image_button";
	private int cropWidth = 0;
	private int cropHeight = 0;
	public static final String kCropWidth = "crop_width";
	public static final String kCropHeight = "crop_height";
	private boolean showLockImageButton = true;

	public static void openForResult(Activity activity, String imagePath, int cropWidth, int cropHeight, boolean showLockImageButton, int requestCode) {
		Intent intent = new Intent(activity, ActivityCropImage.class);
		intent.putExtra(kImagePath, imagePath);
		intent.putExtra(kShowLockImageButton, showLockImageButton);
		intent.putExtra(kCropWidth, cropWidth);
		intent.putExtra(kCropHeight, cropHeight);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void openForResult(Fragment framgment, String imagePath, int cropWidth, int cropHeight, boolean showLockImageButton, int requestCode) {
		Intent intent = new Intent(framgment.getActivity(), ActivityCropImage.class);
		intent.putExtra(kImagePath, imagePath);
		intent.putExtra(kShowLockImageButton, showLockImageButton);
		intent.putExtra(kCropWidth, cropWidth);
		intent.putExtra(kCropHeight, cropHeight);
		framgment.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_crop_image);
		if (getIntent() != null) {
			imagePath = getIntent().getStringExtra(kImagePath);
			showLockImageButton = getIntent().getBooleanExtra(kShowLockImageButton, true);
			cropWidth = getIntent().getIntExtra(kCropWidth, 750);
			cropHeight = getIntent().getIntExtra(kCropHeight, 750);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		cancel = (TextView) findViewById(R.id.cancel);
		finish = (TextView) findViewById(R.id.finish);
		lockImage = (ImageView) findViewById(R.id.lockImage);
		cropImageView = (CropImageView) findViewById(R.id.cropImg);
	}

	@Override
	protected void initViews() {
		if (showLockImageButton) {
			lockImage.setVisibility(View.VISIBLE);
		} else {
			lockImage.setVisibility(View.INVISIBLE);
		}
		int degrees = BitmapUtil.readPictureDegree(imagePath);
		//		Debug.debug("bitmap degrees---:"+degrees);
		bitmap = BitmapUtil.loadBitmap(imagePath, 1600);
		if (bitmap == null) {
			ToastUtil.makeShortText(getResources().getString(R.string.picLoadField));
			return;
		}
		if (degrees != 0) {
			bitmap = BitmapUtil.rotate(bitmap, degrees);
			if (bitmap == null) {
				ToastUtil.makeShortText(getResources().getString(R.string.picLoadField));
				return;
			}
		}
		drawable = new BitmapDrawable(bitmap);
		cropImageView.setDrawable(drawable, cropWidth, cropHeight);
	}

	@Override
	protected void setListeners() {
		cancel.setOnClickListener(this);
		finish.setOnClickListener(this);
		lockImage.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		if (drawable != null) {
			if (drawable.getBitmap() != null && !drawable.getBitmap().isRecycled())
				drawable.getBitmap().recycle();
		}
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.lockImage:
			cropImageView.lockPic(!cropImageView.getLockFlag());
			break;
		case R.id.finish: {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String path = PathManager.getCropPhotoPath().getAbsolutePath();
					FileUtil.writeImage(cropImageView.getCropImage(), path, 100);
					Intent mIntent = new Intent();
					mIntent.putExtra(kCropImagePath, path);
					ActivityCropImage.this.setResult(RESULT_OK, mIntent);
					ActivityCropImage.this.finish();
				}
			}).start();
		}
		break;
		}
	}
}
