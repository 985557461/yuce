package com.example.YuCeClient.ui.yuce_master;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.example.YuCeClient.R;

/**
 * Created by xiaoyu on 15-12-4.
 */
public class YuCePeopleLineItemView extends FrameLayout {
	private YuCePeopleItemView itemOne;
	private YuCePeopleItemView itemTwo;

	public YuCePeopleLineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public YuCePeopleLineItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public YuCePeopleLineItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.yuce_people_line_item_view, this, true);

		itemOne = (YuCePeopleItemView) findViewById(R.id.itemOne);
		itemTwo = (YuCePeopleItemView) findViewById(R.id.itemTwo);
	}

	public void setData(YuCePeopleModel model1, YuCePeopleModel model2) {
		if (model1 != null) {
			itemOne.setVisibility(View.VISIBLE);
			itemOne.setData(model1);
		} else {
			itemOne.setVisibility(View.INVISIBLE);
		}
		if (model2 != null) {
			itemTwo.setVisibility(View.VISIBLE);
			itemTwo.setData(model2);
		} else {
			itemTwo.setVisibility(View.INVISIBLE);
		}
	}
}
