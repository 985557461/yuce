package com.example.YuCeClient.ui.yinPanQiMen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.yinPanQiMen.model.PaiPanModel;
import com.example.YuCeClient.ui.yinPanQiMen.model.ResultItemModel;
import com.example.YuCeClient.util.DisplayUtil;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2015/10/17.
 */
public class ActivityPaiPanResultForCommon extends ActivityBase implements View.OnClickListener{
	private ImageView back;
	private TextView gongLi;
	private TextView nongLi;
	private TextView siZhu;
	private TextView jieQi;
	private TextView yueJiang;
	private TextView ziXuan;
	private TextView zhiFu;
	private TextView zhiShi;
	private TextView shiKongWang;
	private TextView maXing;
	private LinearLayout paiPanImageLL;

	public static final String kDataType = "data_type";
	public static final String kDataStr = "data_str";
	public static final String kType = "type";
	public static final String kXinShuType = "xinshu_type";
	public static final String kXinShuNum = "xinshu_num";

	/**0阴历 1阳历**/
	private String dataType;
	private String dataStr;
	private String type;
	private String xinShuType;
	private String xinShuNum;

	public static void open(Activity activity, String dataType, String dataStr, String type, String xinShuType, String xinShuNum) {
		Intent intent = new Intent(activity, ActivityPaiPanResultForCommon.class);
		intent.putExtra(kDataType, dataType);
		intent.putExtra(kDataStr, dataStr);
		intent.putExtra(kType, type);
		intent.putExtra(kXinShuType, xinShuType);
		intent.putExtra(kXinShuNum, xinShuNum);
		activity.startActivity(intent);
	}

	public static void open(Activity activity, String dataStr, String type, String xinShuType, String xinShuNum) {
		Intent intent = new Intent(activity, ActivityPaiPanResultForCommon.class);
		intent.putExtra(kDataType, "0");
		intent.putExtra(kDataStr, dataStr);
		intent.putExtra(kType, type);
		intent.putExtra(kXinShuType, xinShuType);
		intent.putExtra(kXinShuNum, xinShuNum);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getIntent() != null) {
			Intent intent = getIntent();
			dataType = intent.getStringExtra(kDataType);
			dataStr = intent.getStringExtra(kDataStr);
			type = intent.getStringExtra(kType);
			xinShuType = intent.getStringExtra(kXinShuType);
			xinShuNum = intent.getStringExtra(kXinShuNum);
		}
		setContentView(R.layout.activity_paipan_result_for_common);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		back = (ImageView) findViewById(R.id.back);
		gongLi = (TextView) findViewById(R.id.gongLi);
		nongLi = (TextView) findViewById(R.id.nongLi);
		siZhu = (TextView) findViewById(R.id.siZhu);
		jieQi = (TextView) findViewById(R.id.jieQi);
		yueJiang = (TextView) findViewById(R.id.yueJiang);
		ziXuan = (TextView) findViewById(R.id.ziXuan);
		zhiFu = (TextView) findViewById(R.id.zhiFu);
		zhiShi = (TextView) findViewById(R.id.zhiShi);
		shiKongWang = (TextView) findViewById(R.id.shiKongWang);
		maXing = (TextView) findViewById(R.id.maXing);
		paiPanImageLL = (LinearLayout) findViewById(R.id.paiPanImageLL);
	}

	@Override
	protected void initViews() {
		refreshData();
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
	}

	private void refreshData() {
		showDialog("请稍后");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("datetype", dataType));
		nameValuePairs.add(new BasicNameValuePair("datestr", dataStr));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("xinshu", xinShuType));
		nameValuePairs.add(new BasicNameValuePair("xinshunum", xinShuNum));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_GET_PAIPAN_DATA, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				PaiPanModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PaiPanModel.class);
				if (model != null) {
					if (!TextUtils.isEmpty(model.gongli)) {
						gongLi.setText("公历: " + model.gongli);
					}
					if (!TextUtils.isEmpty(model.nongli)) {
						nongLi.setText("农历: " + model.nongli);
					}
					if (!TextUtils.isEmpty(model.fourzhu)) {
						siZhu.setText("四柱: " + model.fourzhu);
					}
					if (!TextUtils.isEmpty(model.jieqi)) {
						jieQi.setText("节气: " + model.jieqi);
					}
					if (!TextUtils.isEmpty(model.yuejiang)) {
						yueJiang.setText("月将: " + model.yuejiang);
					}
					if (!TextUtils.isEmpty(model.zixuan)) {
						ziXuan.setText(model.zixuan);
					}
					if (!TextUtils.isEmpty(model.zhifu)) {
						zhiFu.setText("值符: " + model.zhifu);
					}
					if (!TextUtils.isEmpty(model.zhishi)) {
						zhiShi.setText("值使: " + model.zhishi);
					}
					if (!TextUtils.isEmpty(model.shikongwang)) {
						shiKongWang.setText("时空亡: " + model.shikongwang);
					}
					if (!TextUtils.isEmpty(model.maxing)) {
						maXing.setText("马星: " + model.maxing);
					}
					StringBuilder sb = new StringBuilder();
					if (model.paipanlist != null && model.paipanlist.size() > 0) {
						int count = model.paipanlist.size();
						for (int i = 0; i < count; i++) {
							ResultItemModel resultItemModel = model.paipanlist.get(i);
							if (i > 0) {
								sb.setLength(0);
								LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								textViewParams.bottomMargin = DisplayUtil.dip2px(ActivityPaiPanResultForCommon.this, 10);
								TextView textView = new TextView(ActivityPaiPanResultForCommon.this);
								sb.append("顺转").append(i).append("宫");
								textView.setGravity(Gravity.LEFT);
								textView.setTextSize(15);
								textView.setTextColor(getResources().getColor(R.color.common_black));
								textView.setText(sb.toString());
								paiPanImageLL.addView(textView, textViewParams);
							}
							PaiPanResultView view = new PaiPanResultView(ActivityPaiPanResultForCommon.this);
							view.setListener(new PaiPanResultView.PaiPanResultViewListener() {
								@Override
								public void onItemClicked(int index) {
									showDialog();
									List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair("type", index + ""));
									Request.doRequest(ActivityPaiPanResultForCommon.this, nameValuePairs, ServerConfig.URL_YUCE_XIANGYI, Request.GET, new Request.RequestListener() {
										@Override
										public void onException(Request.RequestException e) {
											dismissDialog();
											ToastUtil.makeShortText("获取详细信息失败");
										}

										@Override
										public void onComplete(String response) {
											dismissDialog();
											DetailModel detailModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, DetailModel.class);
											if (detailModel != null && !TextUtils.isEmpty(detailModel.xy)) {
												HCScrollViewDlg.showDlg(ActivityPaiPanResultForCommon.this, detailModel.xy);
											} else {
												ToastUtil.makeShortText("获取详细信息失败");
											}
										}
									});
								}
							});
							view.setData(resultItemModel);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							params.bottomMargin = DisplayUtil.dip2px(ActivityPaiPanResultForCommon.this, 20);
							paiPanImageLL.addView(view, params);
						}
					}
				} else {
					ToastUtil.makeShortText("失败");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.back:
			finish();
			break;
		}
	}

	/**
	 * 每个方格点击后详细内容的model**
	 */
	class DetailModel {
		@SerializedName("xy")
		public String xy;
	}

	@Override
	public void onBackPressed() {
		if (HCScrollViewDlg.hasDlg(this)) {
			HCScrollViewDlg.getDlgView(this).dismiss();
			return;
		}
		super.onBackPressed();
	}
}
