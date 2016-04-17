package com.example.YuCeClient.ui.circle.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.RefreshListView;

/**
 * Created by sreay on 15-11-7.
 */
public class PostSearchResultView extends FrameLayout implements View.OnClickListener {
	private LinearLayout allAnLiLL;
	private ImageView allAnLiImageView;
	private LinearLayout oneMonthLL;
	private ImageView oneMonthImageView;
	private LinearLayout threeMonthLL;
	private ImageView threeMonthImageView;
	private RefreshListView postList;
	private LinearLayout choseAllLL;
	private TextView delete;

	private PostAdapter postAdapter;
	private int currentIndex = 0;

	public PostSearchResultView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PostSearchResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PostSearchResultView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.post_search_result_view, this, true);

		allAnLiLL = (LinearLayout) findViewById(R.id.allAnLiLL);
		allAnLiImageView = (ImageView) findViewById(R.id.allAnLiImageView);
		oneMonthLL = (LinearLayout) findViewById(R.id.oneMonthLL);
		oneMonthImageView = (ImageView) findViewById(R.id.oneMonthImageView);
		threeMonthLL = (LinearLayout) findViewById(R.id.threeMonthLL);
		threeMonthImageView = (ImageView) findViewById(R.id.threeMonthImageView);
		postList = (RefreshListView) findViewById(R.id.postList);
		choseAllLL = (LinearLayout) findViewById(R.id.choseAllLL);
		delete = (TextView) findViewById(R.id.delete);

		allAnLiLL.setOnClickListener(this);
		oneMonthLL.setOnClickListener(this);
		threeMonthLL.setOnClickListener(this);
		choseAllLL.setOnClickListener(this);
		delete.setOnClickListener(this);

		postAdapter = new PostAdapter();
		postList.setCanRefresh(false);
		postList.setCanLoadMore(false);
		postList.setAdapter(postAdapter);

		changeState(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.allAnLiLL:
			changeState(0);
			break;
		case R.id.oneMonthLL:
			changeState(1);
			break;
		case R.id.threeMonthLL:
			changeState(2);
			break;
		case R.id.choseAllLL:
			ToastUtil.makeShortText("全选");
			break;
		case R.id.delete:
			ToastUtil.makeShortText("删除");
			break;
		}
	}

	private void changeState(int index) {
		if (currentIndex == index) {
			return;
		}
		switch (index) {
		case 0:
			allAnLiImageView.setSelected(true);
			oneMonthImageView.setSelected(false);
			threeMonthImageView.setSelected(false);
			currentIndex = 0;
			break;
		case 1:
			allAnLiImageView.setSelected(false);
			oneMonthImageView.setSelected(true);
			threeMonthImageView.setSelected(false);
			currentIndex = 1;
			break;
		case 2:
			allAnLiImageView.setSelected(false);
			oneMonthImageView.setSelected(false);
			threeMonthImageView.setSelected(true);
			currentIndex = 2;
			break;
		}
	}

	class PostAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PostItemView itemView = null;
			if (convertView != null) {
				itemView = (PostItemView) convertView;
			} else {
				itemView = new PostItemView(getContext());
			}
			return itemView;
		}
	}
}
