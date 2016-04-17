package com.example.YuCeClient.ui.circle.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-7.
 */
public class ActivityPostSearch extends ActivityBase implements View.OnClickListener {
	private FrameLayout backFL;
	private TextView shiYe;
	private TextView ganQing;
	private TextView caiYun;
	private TextView jianKang;
	private TextView other;
	private ViewPager viewPager;
	private PostPagerAdapter postPagerAdapter;

	private List<PostSearchResultView> views = new ArrayList<PostSearchResultView>();

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityPostSearch.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.actvitiy_post_search);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		backFL = (FrameLayout) findViewById(R.id.backFL);
		shiYe = (TextView) findViewById(R.id.shiYe);
		ganQing = (TextView) findViewById(R.id.ganQing);
		caiYun = (TextView) findViewById(R.id.caiYun);
		jianKang = (TextView) findViewById(R.id.jianKang);
		other = (TextView) findViewById(R.id.other);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	@Override
	protected void initViews() {
		shiYe.setSelected(true);
		for (int i = 0; i < 5; i++) {
			views.add(new PostSearchResultView(this));
		}
		postPagerAdapter = new PostPagerAdapter();
		viewPager.setAdapter(postPagerAdapter);
		viewPager.setOnPageChangeListener(new PostPagerChangedListener());
	}

	@Override
	protected void setListeners() {
		backFL.setOnClickListener(this);
		shiYe.setOnClickListener(this);
		ganQing.setOnClickListener(this);
		caiYun.setOnClickListener(this);
		jianKang.setOnClickListener(this);
		other.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backFL:
			finish();
			break;
		case R.id.shiYe:
			shiYe.setSelected(true);
			ganQing.setSelected(false);
			caiYun.setSelected(false);
			jianKang.setSelected(false);
			other.setSelected(false);
			viewPager.setCurrentItem(0);
			break;
		case R.id.ganQing:
			shiYe.setSelected(false);
			ganQing.setSelected(true);
			caiYun.setSelected(false);
			jianKang.setSelected(false);
			other.setSelected(false);
			viewPager.setCurrentItem(1);
			break;
		case R.id.caiYun:
			shiYe.setSelected(false);
			ganQing.setSelected(false);
			caiYun.setSelected(true);
			jianKang.setSelected(false);
			other.setSelected(false);
			viewPager.setCurrentItem(2);
			break;
		case R.id.jianKang:
			shiYe.setSelected(false);
			ganQing.setSelected(false);
			caiYun.setSelected(false);
			jianKang.setSelected(true);
			other.setSelected(false);
			viewPager.setCurrentItem(3);
			break;
		case R.id.other:
			shiYe.setSelected(false);
			ganQing.setSelected(false);
			caiYun.setSelected(false);
			jianKang.setSelected(false);
			other.setSelected(true);
			viewPager.setCurrentItem(4);
			break;
		}
	}

	class PostPagerChangedListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int i, float v, int i2) {

		}

		@Override
		public void onPageSelected(int i) {
			switch (i) {
			case 0:
				shiYe.setSelected(true);
				ganQing.setSelected(false);
				caiYun.setSelected(false);
				jianKang.setSelected(false);
				other.setSelected(false);
				break;
			case 1:
				shiYe.setSelected(false);
				ganQing.setSelected(true);
				caiYun.setSelected(false);
				jianKang.setSelected(false);
				other.setSelected(false);
				break;
			case 2:
				shiYe.setSelected(false);
				ganQing.setSelected(false);
				caiYun.setSelected(true);
				jianKang.setSelected(false);
				other.setSelected(false);
				break;
			case 3:
				shiYe.setSelected(false);
				ganQing.setSelected(false);
				caiYun.setSelected(false);
				jianKang.setSelected(true);
				other.setSelected(false);
				break;
			case 4:
				shiYe.setSelected(false);
				ganQing.setSelected(false);
				caiYun.setSelected(false);
				jianKang.setSelected(false);
				other.setSelected(true);
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int i) {

		}
	}

	class PostPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			return views.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}
	}

}
