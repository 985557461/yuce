package com.example.YuCeClient.ui.yinPanQiMen;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.yinPanQiMen.model.ResultItemModel;

/**
 * Created by sreay on 15-11-11.
 */
public class PaiPanResultView extends FrameLayout implements View.OnClickListener {
	/**
	 * 第一行*
	 */
	private TextView rowOneItemOne;
	private TextView rowOneItemTwo;
	private TextView rowOneItemThreeTop;
	private TextView rowOneItemThreeBottom;
	private TextView rowOneItemFour;
	private TextView rowOneItemFive;

	/**
	 * 第二行*
	 */
	private TextView rowTwoItemOneTop;
	private TextView rowTwoItemOneBottom;
	private PaiPanResultItemView rowTwoItemTwo;
	private PaiPanResultItemView rowTwoItemThree;
	private PaiPanResultItemView rowTwoItemFour;
	private TextView rowTwoItemFiveTop;
	private TextView rowTwoItemFiveBottom;

	/**
	 * 第三行*
	 */
	private TextView rowThreeItemOneTop;
	private TextView rowThreeItemOneBottom;
	private PaiPanResultItemView rowThreeItemTwo;
	private PaiPanResultItemView rowThreeItemThree;
	private PaiPanResultItemView rowThreeItemFour;
	private TextView rowThreeItemFiveTop;
	private TextView rowThreeItemFiveBottom;

	/**
	 * 第四行*
	 */
	private TextView rowFourItemOneTop;
	private TextView rowFourItemOneBottom;
	private PaiPanResultItemView rowFourItemTwo;
	private PaiPanResultItemView rowFourItemThree;
	private PaiPanResultItemView rowFourItemFour;
	private TextView rowFourItemFiveTop;
	private TextView rowFourItemFiveBottom;

	/**
	 * 第5行*
	 */
	private TextView rowFiveItemOne;
	private TextView rowFiveItemTwo;
	private TextView rowFiveItemThreeTop;
	private TextView rowFiveItemThreeBottom;
	private TextView rowFiveItemFour;
	private TextView rowFiveItemFive;

	private PaiPanResultViewListener listener;

	private ResultItemModel resultItemModel;

	public interface PaiPanResultViewListener {
		public void onItemClicked(int index);
	}

	public PaiPanResultView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public PaiPanResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PaiPanResultView(Context context) {
		super(context);
		init(context);
	}

	public void setListener(PaiPanResultViewListener listener) {
		this.listener = listener;
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.paipan_result_view, this, true);

		/**第一行**/
		rowOneItemOne = (TextView) findViewById(R.id.rowOneItemOne);
		rowOneItemTwo = (TextView) findViewById(R.id.rowOneItemTwo);
		rowOneItemThreeTop = (TextView) findViewById(R.id.rowOneItemThreeTop);
		rowOneItemThreeBottom = (TextView) findViewById(R.id.rowOneItemThreeBottom);
		rowOneItemFour = (TextView) findViewById(R.id.rowOneItemFour);
		rowOneItemFive = (TextView) findViewById(R.id.rowOneItemFive);

		/**第二行**/
		rowTwoItemOneTop = (TextView) findViewById(R.id.rowTwoItemOneTop);
		rowTwoItemOneBottom = (TextView) findViewById(R.id.rowTwoItemOneBottom);
		rowTwoItemTwo = (PaiPanResultItemView) findViewById(R.id.rowTwoItemTwo);
		rowTwoItemThree = (PaiPanResultItemView) findViewById(R.id.rowTwoItemThree);
		rowTwoItemFour = (PaiPanResultItemView) findViewById(R.id.rowTwoItemFour);
		rowTwoItemFiveTop = (TextView) findViewById(R.id.rowTwoItemFiveTop);
		rowTwoItemFiveBottom = (TextView) findViewById(R.id.rowTwoItemFiveBottom);

		/**第三行**/
		rowThreeItemOneTop = (TextView) findViewById(R.id.rowThreeItemOneTop);
		rowThreeItemOneBottom = (TextView) findViewById(R.id.rowThreeItemOneBottom);
		rowThreeItemTwo = (PaiPanResultItemView) findViewById(R.id.rowThreeItemTwo);
		rowThreeItemThree = (PaiPanResultItemView) findViewById(R.id.rowThreeItemThree);
		rowThreeItemFour = (PaiPanResultItemView) findViewById(R.id.rowThreeItemFour);
		rowThreeItemFiveTop = (TextView) findViewById(R.id.rowThreeItemFiveTop);
		rowThreeItemFiveBottom = (TextView) findViewById(R.id.rowThreeItemFiveBottom);

		/**第四行**/
		rowFourItemOneTop = (TextView) findViewById(R.id.rowFourItemOneTop);
		rowFourItemOneBottom = (TextView) findViewById(R.id.rowFourItemOneBottom);
		rowFourItemTwo = (PaiPanResultItemView) findViewById(R.id.rowFourItemTwo);
		rowFourItemThree = (PaiPanResultItemView) findViewById(R.id.rowFourItemThree);
		rowFourItemFour = (PaiPanResultItemView) findViewById(R.id.rowFourItemFour);
		rowFourItemFiveTop = (TextView) findViewById(R.id.rowFourItemFiveTop);
		rowFourItemFiveBottom = (TextView) findViewById(R.id.rowFourItemFiveBottom);

		/**第五行**/
		rowFiveItemOne = (TextView) findViewById(R.id.rowFiveItemOne);
		rowFiveItemTwo = (TextView) findViewById(R.id.rowFiveItemTwo);
		rowFiveItemThreeTop = (TextView) findViewById(R.id.rowFiveItemThreeTop);
		rowFiveItemThreeBottom = (TextView) findViewById(R.id.rowFiveItemThreeBottom);
		rowFiveItemFour = (TextView) findViewById(R.id.rowFiveItemFour);
		rowFiveItemFive = (TextView) findViewById(R.id.rowFiveItemFive);

		rowTwoItemTwo.setOnClickListener(this);
		rowTwoItemThree.setOnClickListener(this);
		rowTwoItemFour.setOnClickListener(this);
		rowThreeItemTwo.setOnClickListener(this);
		rowThreeItemFour.setOnClickListener(this);
		rowFourItemTwo.setOnClickListener(this);
		rowFourItemThree.setOnClickListener(this);
		rowFourItemFour.setOnClickListener(this);
	}

	public void setData(ResultItemModel resultItemModel) {
		this.resultItemModel = resultItemModel;
		/**勾选了天地门户**/
		if (!TextUtils.isEmpty(resultItemModel.tiandi1)) {
			rowOneItemTwo.setText(resultItemModel.tiandi1.trim());
		} else {
			rowOneItemTwo.setVisibility(INVISIBLE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi2)) {
			rowOneItemThreeTop.setText(resultItemModel.tiandi2.trim());
		} else {
			rowOneItemThreeTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan1)) {
			rowOneItemThreeBottom.setText(resultItemModel.angan1.trim());
		} else {
			rowOneItemThreeBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi3)) {
			rowOneItemFour.setText(resultItemModel.tiandi3.trim());
		} else {
			rowOneItemFour.setVisibility(INVISIBLE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi4)) {
			rowTwoItemOneTop.setText(resultItemModel.tiandi4.trim());
		} else {
			rowTwoItemOneTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan2)) {
			rowTwoItemOneBottom.setText(resultItemModel.angan2.trim());
		} else {
			rowTwoItemOneBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi5)) {
			rowTwoItemFiveTop.setText(resultItemModel.tiandi5.trim());
		} else {
			rowTwoItemFiveTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan3)) {
			rowTwoItemFiveBottom.setText(resultItemModel.angan3.trim());
		} else {
			rowTwoItemFiveBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi6)) {
			rowThreeItemOneTop.setText(resultItemModel.tiandi6.trim());
		} else {
			rowThreeItemOneTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan4)) {
			rowThreeItemOneBottom.setText(resultItemModel.angan4.trim());
		} else {
			rowThreeItemOneBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi7)) {
			rowThreeItemFiveTop.setText(resultItemModel.tiandi7.trim());
		} else {
			rowThreeItemFiveTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan5)) {
			rowThreeItemFiveBottom.setText(resultItemModel.angan5.trim());
		} else {
			rowThreeItemFiveBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi8)) {
			rowFourItemOneTop.setText(resultItemModel.tiandi8.trim());
		} else {
			rowFourItemOneTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan6)) {
			rowFourItemOneBottom.setText(resultItemModel.angan6.trim());
		} else {
			rowFourItemOneBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi9)) {
			rowFourItemFiveTop.setText(resultItemModel.tiandi9.trim());
		} else {
			rowFourItemFiveTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan7)) {
			rowFourItemFiveBottom.setText(resultItemModel.angan7.trim());
		} else {
			rowFourItemFiveBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi10)) {
			rowFiveItemTwo.setText(resultItemModel.tiandi10.trim());
		} else {
			rowFiveItemTwo.setText("");
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi11)) {
			rowFiveItemThreeTop.setText(resultItemModel.tiandi11.trim());
		} else {
			rowFiveItemThreeTop.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.angan8)) {
			rowFiveItemThreeBottom.setText(resultItemModel.angan8.trim());
		} else {
			rowFiveItemThreeBottom.setVisibility(GONE);
		}
		if (!TextUtils.isEmpty(resultItemModel.tiandi12)) {
			rowFiveItemFour.setText(resultItemModel.tiandi12.trim());
		} else {
			rowFiveItemFour.setText("");
		}

		/**判断“马”的位置**/
		if (TextUtils.isEmpty(resultItemModel.maxing1)) {
			rowOneItemOne.setVisibility(INVISIBLE);
		} else {
			rowOneItemOne.setText(resultItemModel.maxing1.trim());
		}

		if (TextUtils.isEmpty(resultItemModel.maxing2)) {
			rowOneItemFive.setVisibility(INVISIBLE);
		} else {
			rowOneItemFive.setText(resultItemModel.maxing2.trim());
		}

		if (TextUtils.isEmpty(resultItemModel.maxing3)) {
			rowFiveItemOne.setVisibility(INVISIBLE);
		} else {
			rowFiveItemOne.setText(resultItemModel.maxing3.trim());
		}

		if (TextUtils.isEmpty(resultItemModel.maxing4)) {
			rowFiveItemFive.setVisibility(INVISIBLE);
		} else {
			rowFiveItemFive.setText(resultItemModel.maxing4.trim());
		}

		/**填充方格内的内容**/
		rowTwoItemTwo.setData(resultItemModel.fangge1);
		rowTwoItemThree.setData(resultItemModel.fangge2);
		rowTwoItemFour.setData(resultItemModel.fangge3);

		rowThreeItemTwo.setData(resultItemModel.fangge4);
		rowThreeItemThree.setData(resultItemModel.fangge5);
		rowThreeItemFour.setData(resultItemModel.fangge6);

		rowFourItemTwo.setData(resultItemModel.fangge7);
		rowFourItemThree.setData(resultItemModel.fangge8);
		rowFourItemFour.setData(resultItemModel.fangge9);
	}


	@Override
	public void onClick(View v) {
		int index = 0;
		switch (v.getId()) {
		case R.id.rowTwoItemTwo:
			index = 1;
			break;
		case R.id.rowTwoItemThree:
			index = 2;
			break;
		case R.id.rowTwoItemFour:
			index = 3;
			break;
		case R.id.rowThreeItemTwo:
			index = 4;
			break;
		case R.id.rowThreeItemThree:
			index = 5;
			break;
		case R.id.rowThreeItemFour:
			index = 6;
			break;
		case R.id.rowFourItemTwo:
			index = 7;
			break;
		case R.id.rowFourItemThree:
			index = 8;
			break;
		case R.id.rowFourItemFour:
			index = 9;
			break;
		}
		if (listener != null) {
			listener.onItemClicked(index);
		}
	}
}
