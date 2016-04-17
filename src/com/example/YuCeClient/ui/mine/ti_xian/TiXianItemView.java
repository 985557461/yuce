package com.example.YuCeClient.ui.mine.ti_xian;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;

/**
 * Created by xiaoyu on 15-12-20.
 */
public class TiXianItemView extends FrameLayout {
	private ImageView selectImageView;
	private TextView cardAddress;
	private TextView nameAndCardNum;

	public TiXianModel tiXianModel;
	private TiXianItemViewListener listener;

	public interface TiXianItemViewListener{
		public void onClickView(TiXianItemView itemView);
	}

	public TiXianItemView(Context context) {
		super(context);
		init(context);
	}

	public TiXianItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TiXianItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tixian_item_view, this, true);

		selectImageView = (ImageView) findViewById(R.id.selectImageView);
		cardAddress = (TextView) findViewById(R.id.cardAddress);
		nameAndCardNum = (TextView) findViewById(R.id.nameAndCardNum);

		setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClickView(TiXianItemView.this);
				}
			}
		});
	}

	public void setChoose(boolean choose){
		selectImageView.setSelected(choose);
	}

	public void setData(TiXianModel tiXianModel,TiXianItemViewListener listener) {
		this.tiXianModel = tiXianModel;
		this.listener = listener;
		if (tiXianModel == null) {
			return;
		}
		selectImageView.setSelected(false);
		cardAddress.setText(tiXianModel.cardaddress);
		nameAndCardNum.setText(tiXianModel.realname + "  " + tiXianModel.cardnum);
	}
}
