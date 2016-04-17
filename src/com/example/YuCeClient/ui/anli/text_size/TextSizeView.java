package com.example.YuCeClient.ui.anli.text_size;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.widget.RefreshListView;

/**
 * Created by sreay on 15-11-7.
 */
public class TextSizeView extends FrameLayout {
	private TextView name;
	private TextView sex;
	private TextView testType;
	private RefreshListView questionList;
	private QuestionAdapter questionAdapter;

	public TextSizeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public TextSizeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TextSizeView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.text_size_view, this, true);

		name = (TextView) findViewById(R.id.name);
		sex = (TextView) findViewById(R.id.sex);
		testType = (TextView) findViewById(R.id.testType);
		questionList = (RefreshListView) findViewById(R.id.questionList);
		questionList.setCanRefresh(false);
		questionList.setCanLoadMore(false);
		questionAdapter = new QuestionAdapter();
		questionList.setAdapter(questionAdapter);
	}

	class QuestionAdapter extends BaseAdapter {

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
			QuestionItemView itemView = null;
			if (convertView != null) {
				itemView = (QuestionItemView) convertView;
			} else {
				itemView = new QuestionItemView(getContext());
			}
			return itemView;
		}
	}
}
