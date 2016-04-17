package com.example.YuCeClient.ui.xuanshang.photo_choose;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.widget.FixWidthFrameLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-11-30.
 */
public class SquarePhotoView extends FixWidthFrameLayout {
	private ImageView imageView;
	private ImageView delete;

	private ImageLoader imageLoader;
	private String path;

	/**
	 * 删除按钮监听*
	 */
	private DeleteListener deleteListener;

	public interface DeleteListener {
		public void onDeleteClicked(String path);
	}

	public SquarePhotoView(Context context) {
		super(context);
		init(context);
	}

	public SquarePhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SquarePhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.square_photo_view, this, true);
		imageView = (ImageView) findViewById(R.id.imageView);
		delete = (ImageView) findViewById(R.id.delete);

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (deleteListener != null) {
					deleteListener.onDeleteClicked(path);
				}
			}
		});
	}

	public void setDeleteListener(DeleteListener listener) {
		this.deleteListener = listener;
	}

	public void setData(String path) {
		this.path = path;
		if (!TextUtils.isEmpty(path)) {
			imageLoader.displayImage(ImageLoaderUtil.fromFile(path), imageView, ImageLoaderUtil.Options_Common_Disc_Pic);
		} else {
			imageLoader.displayImage("", imageView, ImageLoaderUtil.Options_Common_Disc_Pic);
		}
	}
}
