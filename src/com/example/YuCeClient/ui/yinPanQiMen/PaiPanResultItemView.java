package com.example.YuCeClient.ui.yinPanQiMen;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;

/**
 * Created by sreay on 15-11-11.
 */
public class PaiPanResultItemView extends FrameLayout {
	private TextView leftOne;
	private TextView leftTwo;
	private TextView leftThree;
	private TextView rightOne;
	private TextView rightTwo;

	public PaiPanResultItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PaiPanResultItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PaiPanResultItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.paipan_result_item_view, this, true);
		leftOne = (TextView) findViewById(R.id.leftOne);
		leftTwo = (TextView) findViewById(R.id.leftTwo);
		leftThree = (TextView) findViewById(R.id.leftThree);
		rightOne = (TextView) findViewById(R.id.rightOne);
		rightTwo = (TextView) findViewById(R.id.rightTwo);
	}

	public void setData(String content) {
		if (TextUtils.isEmpty(content)) {
			leftOne.setVisibility(INVISIBLE);
			leftTwo.setVisibility(INVISIBLE);
			leftThree.setVisibility(INVISIBLE);
			rightOne.setVisibility(INVISIBLE);
			rightTwo.setVisibility(INVISIBLE);
		} else {
			String[] datas = content.trim().split(";");
			String one = datas[0];
			String[] two = datas[1].split("-");
			String[] three = datas[2].split("-");
			leftOne.setText(one);
			leftTwo.setText(two[0]);
			rightOne.setText(two[1]);
			leftThree.setText(three[0]);
			rightTwo.setText(three[1]);
		}
	}
}
