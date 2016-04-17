package com.example.YuCeClient.ui;

import android.os.Bundle;
import android.os.Handler;
import com.example.YuCeClient.R;

/**
 * Created by xiaoyuPC on 2015/6/7.
 */
public class ActivitySplash extends ActivityBase {
	private long sleepTime = 2000;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_splash);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void getViews() {
	}

	@Override
	protected void initViews() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ActivityMain.open(ActivitySplash.this);
				finish();
			}
		}, sleepTime);
	}

	@Override
	protected void setListeners() {

	}
}
