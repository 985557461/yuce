package com.example.YuCeClient.ui.yuce_master;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaoyu on 15-12-4.
 */
public class YuCePeopleItemView extends FrameLayout {
	private ImageView imageView;
	private ImageView levelIcon;
	private TextView nickName;
	private TextView totalMoney;
	private TextView goodPriseLv;
	private TextView caiNaCount;

	private YuCePeopleModel yuCePeopleModel;
	private ImageLoader imageLoader;

	public YuCePeopleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public YuCePeopleItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public YuCePeopleItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.yuce_people_item_view, this, true);

		imageView = (ImageView) findViewById(R.id.imageView);
		levelIcon = (ImageView) findViewById(R.id.levelIcon);
		nickName = (TextView) findViewById(R.id.nickName);
		totalMoney = (TextView) findViewById(R.id.totalMoney);
		goodPriseLv = (TextView) findViewById(R.id.goodPriseLv);
		caiNaCount = (TextView) findViewById(R.id.caiNaCount);

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(yuCePeopleModel.doctorid)) {
					ActivityYuCeMasterDetail.open((Activity) getContext(), yuCePeopleModel.doctorid);
				} else {
					ToastUtil.makeShortText("预测师id为空");
				}
			}
		});
	}

	public void setData(YuCePeopleModel peopleModel) {
		this.yuCePeopleModel = peopleModel;
		if (peopleModel == null) {
			return;
		}
		if (!TextUtils.isEmpty(peopleModel.doctor_avatar)) {
			imageLoader.displayImage(peopleModel.doctor_avatar, imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if(!TextUtils.isEmpty(peopleModel.doctor_name)){
			nickName.setText("昵称: "+peopleModel.doctor_name);
		}else{
			nickName.setText("昵称: 无名小卒");
		}
		totalMoney.setText("总收入: " + peopleModel.jinbi + "金币");
		if (!TextUtils.isEmpty(peopleModel.cainalv)) {
			goodPriseLv.setText(peopleModel.cainalv + "%");
		} else {
			goodPriseLv.setText("0%");
		}
		caiNaCount.setText(peopleModel.cainashu + "");
	}
}
