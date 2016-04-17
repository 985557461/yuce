package com.example.YuCeClient.util;

import android.graphics.Bitmap;
import android.net.Uri;
import com.example.YuCeClient.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * Created by sreay on 14-8-21.
 */
public class ImageLoaderUtil {
	public static DisplayImageOptions Options_Common_Disc_Pic = new DisplayImageOptions.Builder().
			imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true).resetViewBeforeLoading(true).cacheOnDisc(true).build();

	public static DisplayImageOptions Options_Common_memory_Pic = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).build();

	public static DisplayImageOptions Options_Memory_Rect_Avatar =
			new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT).
					bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true).
					showImageOnLoading(R.drawable.icon_default_user).showImageOnFail(R.drawable.icon_default_user).
					showImageForEmptyUri(R.drawable.icon_default_user).cacheInMemory(true).
					cacheOnDisk(true).build();

	public static String fromFile(String path) {
		if (path == null) {
			return null;
		}
		return Uri.fromFile(new File(path)).toString();
	}

	public static String fromHttp(String path) {
		if (path == null) {
			return null;
		}
		return Uri.parse(path).toString();
	}

	public static String fromDrawable(int resId) {
		if (resId == 0) {
			return null;
		}
		return "drawable://" + resId;
	}
}
