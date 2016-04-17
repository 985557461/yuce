package com.example.YuCeClient.ui.anli.text_size;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;

/**
 * Created by xiaoyu on 15-11-7.
 */
public class QuestionItemView extends FrameLayout {
	private TextView title;
	private TextView content;

	public QuestionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public QuestionItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public QuestionItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.question_item_view, this, true);

		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
	}
}
