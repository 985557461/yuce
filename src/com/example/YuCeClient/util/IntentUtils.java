package com.example.YuCeClient.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.YuCeClient.widget.photo.ActivityCapture;
import com.example.YuCeClient.widget.photo.PhotoAlbumActivity;

import java.io.File;
import java.util.ArrayList;

public class IntentUtils {
	/**
	 * 系统相机拍照
	 *
	 * @return
	 */
	public static Intent goToCameraIntent(String path) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(new File(path));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		return intent;
	}

	/**
	 * 拍照，自定义相机界面
	 */
	public static Intent goToCameraIntent(Activity activity, int cropWidth, int cropHeight) {
		Intent intent = new Intent(activity, ActivityCapture.class);
		intent.putExtra(ActivityCapture.kCropWidth, cropWidth);
		intent.putExtra(ActivityCapture.kCropHeight, cropHeight);
		return intent;
	}

	/**
	 * 从图库选择图片
	 * maxCount －1 从系统相册选择
	 * name 在图片选择界面，可以定义右下角的按钮的名字
	 * showCamera 是否显示照相机按钮
	 *
	 * @return
	 */
	public static Intent goToAlbumIntent(ArrayList<String> selectPaths, int maxCount, String rightBottomName,
			boolean showCamera, Activity activity) {
		if (maxCount == -1) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			intent.putExtra("return-data", false);
			return intent;
		}
		Intent intent = new Intent(activity, PhotoAlbumActivity.class);
		intent.putExtra(PhotoAlbumActivity.Key_SelectPaths, selectPaths);
		intent.putExtra(PhotoAlbumActivity.Key_Right_Bottom_Button_Name, rightBottomName);
		intent.putExtra(PhotoAlbumActivity.Key_Show_Camera, showCamera);
		intent.putExtra(PhotoAlbumActivity.Key_Max_Count, maxCount);
		return intent;
	}
}
