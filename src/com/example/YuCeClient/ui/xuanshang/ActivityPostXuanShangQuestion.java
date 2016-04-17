package com.example.YuCeClient.ui.xuanshang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.xuanshang.photo_choose.PhotoSelectAdapter;
import com.example.YuCeClient.ui.xuanshang.photo_choose.PhotoSelectView;
import com.example.YuCeClient.ui.xuanshang.photo_choose.SquarePhotoView;
import com.example.YuCeClient.util.DisplayUtil;
import com.example.YuCeClient.util.IntentUtils;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.HCDatePickDialog;
import com.example.YuCeClient.widget.cropimage.ActivityCropImage;
import com.example.YuCeClient.widget.photo.PhotoActivity;
import com.example.YuCeClient.widget.photo.PhotoAlbumActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class ActivityPostXuanShangQuestion extends ActivityBase implements View.OnClickListener, PhotoSelectView.FooterClickListener, PhotoSelectView.PhotoDeleteListener, HCDatePickDialog.HCDatePickDialogListener {
	private ImageView back;
	private ImageView postQestion;
	private LinearLayout manLL;
	private LinearLayout womenLL;
	private EditText realNameEditText;
	private TextView birthTimeTextView;
	private EditText birthPlaceEditText;
	private EditText questionContentEditText;
	private TextView nextStep;

	/**
	 * picture about
	 * *
	 */
	private PhotoSelectView photoSelectView;
	private PhotoSelectAdapter adapter;
	private static final int kActivitySettingSelectPicRequest = 101;
	private static final int kPhotoCropImageRequest = 102;
	private String avatarPath = "";
	private List<String> pathsList = new ArrayList<String>();

	/**
	 * 基本信息相关*
	 */
	private int sex = 1;//0 女 1 男
	private String birthTime;

	private String currYear;
	private String currMonth;
	private String currDay;
	private String currHour;
	private String currMin;
	private int dateType;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityPostXuanShangQuestion.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_post_xuan_shang_question);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		back = (ImageView) findViewById(R.id.back);
		postQestion = (ImageView) findViewById(R.id.postQestion);
		manLL = (LinearLayout) findViewById(R.id.manLL);
		womenLL = (LinearLayout) findViewById(R.id.womenLL);
		birthTimeTextView = (TextView) findViewById(R.id.birthTimeTextView);
		realNameEditText = (EditText) findViewById(R.id.realNameEditText);
		birthPlaceEditText = (EditText) findViewById(R.id.birthPlaceEditText);
		questionContentEditText = (EditText) findViewById(R.id.questionContentEditText);
		photoSelectView = (PhotoSelectView) findViewById(R.id.photoSelectView);
		nextStep = (TextView) findViewById(R.id.nextStep);
	}

	@Override
	protected void initViews() {
		adapter = new MyPhotoSelectAdapter();
		photoSelectView.setAdapter(adapter);
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		manLL.setOnClickListener(this);
		womenLL.setOnClickListener(this);
		birthTimeTextView.setOnClickListener(this);
		photoSelectView.setFooterClickListener(this);
		photoSelectView.setPhotoDeleteListener(this);
		nextStep.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.birthTimeTextView:
			HCDatePickDialog.showDlg(this, this);
			break;
		case R.id.manLL:
			sex = 1;
			womenLL.setBackground(null);
			manLL.setBackgroundResource(R.drawable.sex_man_bg_shape);
			break;
		case R.id.womenLL:
			sex = 0;
			manLL.setBackground(null);
			womenLL.setBackgroundResource(R.drawable.sex_women_bg_shape);
			break;
		case R.id.nextStep:
			tryToNextStep();
			break;
		}
	}

	private void tryToNextStep() {
		String xingMing = realNameEditText.getText().toString();
		if (TextUtils.isEmpty(xingMing)) {
			ToastUtil.makeShortText("请输入姓名");
			return;
		}
		if (TextUtils.isEmpty(birthTime)) {
			ToastUtil.makeShortText("请输入生日");
			return;
		}
		String chuShengDi = birthPlaceEditText.getText().toString();
		if (TextUtils.isEmpty(chuShengDi)) {
			ToastUtil.makeShortText("请输入出生地");
			return;
		}
		String content = questionContentEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.makeShortText("请输入问题");
			return;
		}
		ActivityJinBi.open(this, xingMing, sex + "", birthTime, chuShengDi, content, pathsList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
			String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
			if (paths != null && paths.length <= 0) {
				return;
			}
			if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
				if (!TextUtils.isEmpty(paths[0])) {
					ActivityCropImage.openForResult(this, paths[0], 750, 750, true, kPhotoCropImageRequest);
					return;
				}
			} else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
				if (!TextUtils.isEmpty(paths[0])) {
					avatarPath = paths[0];
					pathsList.add(avatarPath);
					photoSelectView.setAdapter(adapter);
				}
			}
		} else if (requestCode == kPhotoCropImageRequest && resultCode == RESULT_OK) {
			avatarPath = data.getStringExtra(ActivityCropImage.kCropImagePath);
			pathsList.add(avatarPath);
			photoSelectView.setAdapter(adapter);
			return;
		}
	}

	@Override
	public void onFooterClicked() {
		Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, ActivityPostXuanShangQuestion.this);
		startActivityForResult(intent, kActivitySettingSelectPicRequest);
	}

	@Override
	public void onPhotoDeleteClicked(String path) {
		pathsList.remove(path);
		photoSelectView.setAdapter(adapter);
	}

	@Override
	public void onDataPicked(boolean isYangLi, String year, String month, String day, String hour, String min) {
		if (isYangLi) {
			dateType = 1;
		} else {
			dateType = 0;
		}
		currYear = year;
		currMonth = month;
		currDay = day;
		currHour = hour;
		currMin = min;
		//阴历 2015-11-11 23点5分
		StringBuilder sb = new StringBuilder();
		if (dateType == 1) {
			sb.append("阳历 ");
		} else {
			sb.append("阴历 ");
		}
		sb.append(currYear).append("-");
		sb.append(currMonth).append("-");
		sb.append(currDay).append(" ");
		sb.append(currHour).append("点");
		sb.append(currMin).append("分");
		birthTime = sb.toString();
		birthTimeTextView.setText(birthTime);
	}

	private class MyPhotoSelectAdapter extends PhotoSelectAdapter<SquarePhotoView> {

		@Override
		public int getWidth() {
			WindowManager wm = (WindowManager) ActivityPostXuanShangQuestion.this.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			return width - DisplayUtil.dip2px(ActivityPostXuanShangQuestion.this, 14);
		}

		@Override
		public int getTotalCount() {
			return pathsList.size();
		}

		@Override
		public int getColCount() {
			return 3;
		}

		@Override
		public int getHorMargin() {
			return 0;
		}

		@Override
		public int getVerMargin() {
			return DisplayUtil.dip2px(ActivityPostXuanShangQuestion.this, 10);
		}

		@Override
		public void setView(SquarePhotoView view, int position) {
			view.setData(pathsList.get(position));
		}
	}
}
