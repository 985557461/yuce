package com.example.YuCeClient.ui.circle.search;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.YuCeClient.R;

/**
 * Created by sreay on 15-11-7.
 */
public class PostItemView extends FrameLayout implements View.OnClickListener {
	private ImageView choseImageView;
	private FrameLayout editFL;
	private ImageView editImageView;
	private FrameLayout lookFL;
	private ImageView lookImageView;
	private FrameLayout deleteFL;
	private ImageView deleteImageView;

	private boolean isChosed = false;

	public PostItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PostItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PostItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.post_item_view, this, true);

		choseImageView = (ImageView) findViewById(R.id.choseImageView);
		editFL = (FrameLayout) findViewById(R.id.editFL);
		editImageView = (ImageView) findViewById(R.id.editImageView);
		lookFL = (FrameLayout) findViewById(R.id.lookFL);
		lookImageView = (ImageView) findViewById(R.id.lookImageView);
		deleteFL = (FrameLayout) findViewById(R.id.deleteFL);
		deleteImageView = (ImageView) findViewById(R.id.deleteImageView);

		choseImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choseImageView:
			if (isChosed) {
				isChosed = false;
				choseImageView.setBackgroundColor(Color.parseColor("#e8e8e8"));
			} else {
				isChosed = true;
				choseImageView.setBackgroundColor(Color.parseColor("#666666"));
			}
			break;
		}
	}
}
