package com.example.YuCeClient.ui.yinPanQiMen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.HCDatePickDialog;
import com.example.YuCeClient.widget.HCPopListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xiaoyu on 2015/7/1.
 */
public class PaiPanFragment extends Fragment implements View.OnClickListener, HCDatePickDialog.HCDatePickDialogListener {
	private CheckBox radioTianMenDiHu;
	private CheckBox radioYiXingHuanDou;
	private CheckBox radioYinPanKePan;
	private TextView timeTextView;
	private TextView yearTextView;
	private TextView monthTextView;
	private TextView dayTextView;
	private TextView hourTextView;
	private TextView minTextView;
	private RelativeLayout ziXuanJuRL;
	private TextView ziXuanJuTextView;
	private EditText nameEditText;
	private CheckBox liuNianCheckBox;
	private LinearLayout liuNianLL;
	private TextView liuNianTextView;
	private TextView nameQiMenPan;
	private TextView resetTime;
	private TextView beginPaiPan;

	/**
	 * yinli or yangli
	 * *
	 */
	private int dateType = 1;

	/**
	 * xinshu type
	 * *
	 */
	private int xinShuType = -1;
	private String xinShuNum = "";

	/**
	 * 是否选中天门地户等*
	 */
	private boolean choseOne = false;
	private boolean choseTwo = false;
	private boolean choseThree = false;

	/**
	 * 是否选中流年*
	 */
	private boolean isChoseLiuNian = false;

	private String currYear;
	private String currMonth;
	private String currDay;
	private String currHour;
	private String currMin;

	private HCPopListView popListView;
	private List<String> list = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.paipan_fragment, container, false);
		initViews(view);
		return view;
	}

	private void initViews(final View view) {
		radioTianMenDiHu = (CheckBox) view.findViewById(R.id.radioTianMenDiHu);
		radioYiXingHuanDou = (CheckBox) view.findViewById(R.id.radioYiXingHuanDou);
		radioYinPanKePan = (CheckBox) view.findViewById(R.id.radioYinPanKePan);
		liuNianCheckBox = (CheckBox) view.findViewById(R.id.liuNianCheckBox);

		radioTianMenDiHu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				choseOne = isChecked;
			}
		});

		radioYiXingHuanDou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				choseTwo = isChecked;
			}
		});

		radioYinPanKePan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				choseThree = isChecked;
			}
		});

		liuNianCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isChoseLiuNian = isChecked;
			}
		});

		timeTextView = (TextView) view.findViewById(R.id.timeTextView);
		yearTextView = (TextView) view.findViewById(R.id.yearTextView);
		monthTextView = (TextView) view.findViewById(R.id.monthTextView);
		dayTextView = (TextView) view.findViewById(R.id.dayTextView);
		hourTextView = (TextView) view.findViewById(R.id.hourTextView);
		minTextView = (TextView) view.findViewById(R.id.minTextView);
		ziXuanJuRL = (RelativeLayout) view.findViewById(R.id.ziXuanJuRL);
		ziXuanJuTextView = (TextView) view.findViewById(R.id.ziXuanJuTextView);
		nameEditText = (EditText) view.findViewById(R.id.nameEditText);
		liuNianLL = (LinearLayout) view.findViewById(R.id.liuNianLL);
		liuNianTextView = (TextView) view.findViewById(R.id.liuNianTextView);
		nameQiMenPan = (TextView) view.findViewById(R.id.nameQiMenPan);
		resetTime = (TextView) view.findViewById(R.id.resetTime);
		beginPaiPan = (TextView) view.findViewById(R.id.beginPaiPan);

		timeTextView.setOnClickListener(this);
		ziXuanJuRL.setOnClickListener(this);
		liuNianLL.setOnClickListener(this);
		nameQiMenPan.setOnClickListener(this);
		resetTime.setOnClickListener(this);
		beginPaiPan.setOnClickListener(this);
		resetTime();
	}

	private void resetTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(calendar.DAY_OF_MONTH);
		int hour = calendar.get(calendar.HOUR_OF_DAY);
		int min = calendar.get(calendar.MINUTE);
		currYear = year + "";
		currMonth = month + "";
		currDay = day + "";
		currHour = hour + "";
		currMin = min + "";
		yearTextView.setText(year + "年");
		monthTextView.setText(month + "月");
		dayTextView.setText(day + "日");
		hourTextView.setText(hour + "时");
		minTextView.setText(min + "分");

		liuNianTextView.setText(year + "年");
	}

	private void initXinShu() {
		list.clear();
		list.add("无");
		list.add("阴9");
		list.add("阴8");
		list.add("阴7");
		list.add("阴6");
		list.add("阴5");
		list.add("阴4");
		list.add("阴3");
		list.add("阴2");
		list.add("阴1");
		list.add("阳2");
		list.add("阳3");
		list.add("阳4");
		list.add("阳5");
		list.add("阳6");
		list.add("阳7");
		list.add("阳8");
		list.add("阳9");
	}

	private void initLiuNian() {
		list.clear();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 1990; i < 2200; i++) {
			stringBuilder.append(i).append("年");
			list.add(stringBuilder.toString());
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.timeTextView:
			HCDatePickDialog.showDlg(getActivity(), this);
			break;
		case R.id.ziXuanJuRL:
			initXinShu();
			popListView = new HCPopListView(getActivity(), "请选择", "取消", list, new HCPopListView.HCPopListViewListener() {
				@Override
				public void onItemClicked(int index, String content) {
					ziXuanJuTextView.setText(content);
					if (content.equals("无")) {
						xinShuNum = "";
						xinShuType = -1;
					} else if (content.equals("阴9") || content.equals("阴8") || content.equals("阴7") || content.equals("阴6") || content.equals("阴5") || content.equals("阴4") || content.equals("阴3") || content.equals("阴2") || content.equals("阴1")) {
						xinShuType = 0;
						xinShuNum = content.replace("阴", "").trim();
					} else {
						xinShuType = 1;
						xinShuNum = content.replace("阳", "").trim();
					}
				}

				@Override
				public void onCancelClicked() {

				}
			});
			popListView.show();
			break;
		case R.id.liuNianLL:
			initLiuNian();
			popListView = new HCPopListView(getActivity(), "请选择", "取消", list, new HCPopListView.HCPopListViewListener() {
				@Override
				public void onItemClicked(int index, String content) {
					liuNianTextView.setText(content);
				}

				@Override
				public void onCancelClicked() {

				}
			});
			break;
		case R.id.nameQiMenPan:
			tryToPaiPanForName();
			break;
		case R.id.resetTime:
			resetTime();
			break;
		case R.id.beginPaiPan:
			tryToPaiPanForCommon();
			break;
		}
	}

	private void tryToPaiPanForCommon() {
		StringBuffer typeBuffer = new StringBuffer();
		if (choseOne) {
			typeBuffer.append("0,");
		}
		if (choseTwo) {
			typeBuffer.append("1,");
		}
		if (choseThree) {
			typeBuffer.append("2,");
		}
		if (!choseOne && !choseTwo && !choseThree) {
			typeBuffer.append("-1,");
		}
		String type = typeBuffer.toString().substring(0, typeBuffer.toString().length() - 1);
		StringBuilder sb = new StringBuilder();
		sb.append(currYear);
		sb.append("-");
		sb.append(currMonth);
		sb.append("-");
		sb.append(currDay);
		sb.append(" ");
		sb.append(currHour);
		sb.append(":");
		sb.append(currMin);
		sb.append(":00");
		ActivityPaiPanResultForCommon.open(getActivity(), dateType + "", sb.toString(), type, xinShuType + "", xinShuNum);
	}

	private void tryToPaiPanForName() {
		StringBuilder sb = new StringBuilder();
		sb.append(currYear);
		sb.append("-");
		sb.append(currMonth);
		sb.append("-");
		sb.append(currDay);
		sb.append(" ");
		sb.append(currHour);
		sb.append(":");
		sb.append(currMin);
		sb.append(":00");
		/**姓名**/
		String name = nameEditText.getText().toString();
		if (TextUtils.isEmpty(name)) {
			ToastUtil.makeShortText("请输入名字");
			return;
		}
		/**是否选择流年**/
		String liuNian = "0";
		if (isChoseLiuNian) {
			liuNian = "1";
		} else {
			liuNian = "0";
		}
		/**年份**/
		String nianFen = liuNianTextView.getText().toString().replace("年", "").trim();
		ActivityPaiPanResultForName.open(getActivity(), sb.toString(), name, liuNian, nianFen);
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
		yearTextView.setText(year + "年");
		monthTextView.setText(month + "月");
		dayTextView.setText(day + "日");
		hourTextView.setText(hour + "时");
		minTextView.setText(min + "分");
	}
}
