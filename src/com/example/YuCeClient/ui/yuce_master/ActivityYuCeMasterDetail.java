package com.example.YuCeClient.ui.yuce_master;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-7.
 */
public class ActivityYuCeMasterDetail extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private ImageView avatar;
	private TextView nickName;
	private TextView level;
	private TextView goodComment;
	private TextView accountId;
	private TextView accountNickName;
	private TextView accountQianMing;
	private TextView caiNaCount;
	private TextView tiWenCount;
	private TextView replyCount;

	public static final String kDoctorId = "doctor_id";
	private String doctorId;

	private ImageLoader imageLoader;

	public static void open(Activity activity, String doctorId) {
		Intent intent = new Intent(activity, ActivityYuCeMasterDetail.class);
		intent.putExtra(kDoctorId, doctorId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		doctorId = getIntent().getStringExtra(kDoctorId);
		setContentView(R.layout.activity_yuce_master_detail);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		back = (ImageView) findViewById(R.id.back);
		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
		level = (TextView) findViewById(R.id.level);
		goodComment = (TextView) findViewById(R.id.goodComment);
		accountId = (TextView) findViewById(R.id.accountId);
		accountNickName = (TextView) findViewById(R.id.accountNickName);
		accountQianMing = (TextView) findViewById(R.id.accountQianMing);
		caiNaCount = (TextView) findViewById(R.id.caiNaCount);
		tiWenCount = (TextView) findViewById(R.id.tiWenCount);
		replyCount = (TextView) findViewById(R.id.replyCount);
	}

	@Override
	protected void initViews() {
		refreshData();
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		goodComment.setOnClickListener(this);
	}

	private void refreshData() {
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("doctorId", doctorId));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_DOCTOR_DETAIL, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("数据请求失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				YuCeMasterDetailModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, YuCeMasterDetailModel.class);
				if (model != null && model.result == 1 && model.doctorinfo != null && model.doctorinfo.size() > 0) {
					initData(model);
				} else {
					ToastUtil.makeShortText("数据请求失败");
				}
			}
		});
	}

	private void initData(YuCeMasterDetailModel model) {
		YuCeMasterDetailModel.MasterModel masterModel = model.doctorinfo.get(0);
		if (!TextUtils.isEmpty(masterModel.doctor_avatar)) {
			imageLoader.displayImage(masterModel.doctor_avatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		if (!TextUtils.isEmpty(masterModel.doctor_name)) {
			nickName.setText(masterModel.doctor_name);
		} else {
			nickName.setText("无名小卒");
		}
		if (!TextUtils.isEmpty(masterModel.doctor_title)) {
			level.setText(masterModel.doctor_title);
		} else {
			level.setText("无级别");
		}
		if (!TextUtils.isEmpty(masterModel.cainalv)) {
			goodComment.setText("用户好评率：" + masterModel.cainalv + "%");
		} else {
			goodComment.setText("用户好评率：0%");
		}
		if (!TextUtils.isEmpty(masterModel.doctorid)) {
			accountId.setText("他的账号：" + masterModel.doctorid);
		} else {
			accountId.setText("他的账号：");
		}
		if (!TextUtils.isEmpty(masterModel.doctor_title)) {
			accountNickName.setText("他的称谓：" + masterModel.doctor_title);
		} else {
			accountNickName.setText("他的称谓：");
		}
		if(!TextUtils.isEmpty(masterModel.brief)){
			accountQianMing.setText("他的签名: "+masterModel.brief);
		}else{
			accountQianMing.setText("他的签名: 无");
		}
		caiNaCount.setText("被采纳数：" + masterModel.cainashu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.goodComment:
			if (!TextUtils.isEmpty(doctorId)) {
				ActivityCommentDetail.open(this, doctorId);
			} else {
				ToastUtil.makeShortText("预测师id为空");
			}
			break;
		}
	}
}
