package com.example.YuCeClient.ui.xuanshang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-7.
 */
public class ActivityXuanShangComment extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private RelativeLayout fiveStarRL;
	private ImageView selectImageOne;
	private RelativeLayout fourStarRL;
	private ImageView selectImageTwo;
	private RelativeLayout threeStarRL;
	private ImageView selectImageThree;
	private RelativeLayout twoStarRL;
	private ImageView selectImageFour;
	private RelativeLayout oneStarRL;
	private ImageView selectImageFive;
	private EditText content;
	private TextView commit;

	private Account account;

	/**
	 * 星星的数量*
	 */
	private int starCount = 0;

	public static final String kProblemId = "problem_id";
	public static final String kDoctorid = "doctor_id";
	private String problemId;
	private String doctorId;

	public static void open(Activity activity, String problemId, String doctorId) {
		Intent intent = new Intent(activity, ActivityXuanShangComment.class);
		intent.putExtra(kProblemId, problemId);
		intent.putExtra(kDoctorid, doctorId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		problemId = getIntent().getStringExtra(kProblemId);
		doctorId = getIntent().getStringExtra(kDoctorid);
		setContentView(R.layout.activity_xuanshang_comment);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		fiveStarRL = (RelativeLayout) findViewById(R.id.fiveStarRL);
		selectImageOne = (ImageView) findViewById(R.id.selectImageOne);
		fourStarRL = (RelativeLayout) findViewById(R.id.fourStarRL);
		selectImageTwo = (ImageView) findViewById(R.id.selectImageTwo);
		threeStarRL = (RelativeLayout) findViewById(R.id.threeStarRL);
		selectImageThree = (ImageView) findViewById(R.id.selectImageThree);
		twoStarRL = (RelativeLayout) findViewById(R.id.twoStarRL);
		selectImageFour = (ImageView) findViewById(R.id.selectImageFour);
		oneStarRL = (RelativeLayout) findViewById(R.id.oneStarRL);
		selectImageFive = (ImageView) findViewById(R.id.selectImageFive);
		content = (EditText) findViewById(R.id.content);
		commit = (TextView) findViewById(R.id.commit);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		fiveStarRL.setOnClickListener(this);
		fourStarRL.setOnClickListener(this);
		threeStarRL.setOnClickListener(this);
		twoStarRL.setOnClickListener(this);
		oneStarRL.setOnClickListener(this);
		commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.fiveStarRL:
			changeState(0);
			break;
		case R.id.fourStarRL:
			changeState(1);
			break;
		case R.id.threeStarRL:
			changeState(2);
			break;
		case R.id.twoStarRL:
			changeState(3);
			break;
		case R.id.oneStarRL:
			changeState(4);
			break;
		case R.id.commit:
			tryToComment();
			break;
		}
	}

	private void changeState(int index) {
		switch (index) {
		case 0:
			selectImageOne.setSelected(true);
			selectImageTwo.setSelected(false);
			selectImageThree.setSelected(false);
			selectImageFour.setSelected(false);
			selectImageFive.setSelected(false);
			starCount = 5;
			break;
		case 1:
			selectImageOne.setSelected(false);
			selectImageTwo.setSelected(true);
			selectImageThree.setSelected(false);
			selectImageFour.setSelected(false);
			selectImageFive.setSelected(false);
			starCount = 4;
			break;
		case 2:
			selectImageOne.setSelected(false);
			selectImageTwo.setSelected(false);
			selectImageThree.setSelected(true);
			selectImageFour.setSelected(false);
			selectImageFive.setSelected(false);
			starCount = 3;
			break;
		case 3:
			selectImageOne.setSelected(false);
			selectImageTwo.setSelected(false);
			selectImageThree.setSelected(false);
			selectImageFour.setSelected(true);
			selectImageFive.setSelected(false);
			starCount = 2;
			break;
		case 4:
			selectImageOne.setSelected(false);
			selectImageTwo.setSelected(false);
			selectImageThree.setSelected(false);
			selectImageFour.setSelected(false);
			selectImageFive.setSelected(true);
			starCount = 1;
			break;
		}
	}

	private void tryToComment() {
		if (starCount == 0) {
			ToastUtil.makeShortText("请选择满意程度");
			return;
		}
		String contentStr = content.getText().toString();
		if (TextUtils.isEmpty(contentStr)) {
			ToastUtil.makeShortText("请输入评论内容");
			return;
		}

		if (TextUtils.isEmpty(account.userId)) {
			ActivityLogin.open(this);
			return;
		}
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("problemId", problemId));
		nameValuePairs.add(new BasicNameValuePair("doctorid", doctorId));
		nameValuePairs.add(new BasicNameValuePair("userId", account.userId));
		nameValuePairs.add(new BasicNameValuePair("score", starCount + ""));
		nameValuePairs.add(new BasicNameValuePair("evaluate", contentStr));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_APP_PRAISE, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("评价失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				CommonModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (model != null && model.result == 1) {
					ToastUtil.makeShortText("评论成功");
					finish();
				} else {
					ToastUtil.makeShortText("评价失败");
				}
			}
		});
	}
}
