package com.example.YuCeClient.ui.xuanshang;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.circle.Actions;
import com.example.YuCeClient.ui.mine.ActivityChongZhi;
import com.example.YuCeClient.ui.mine.MineFragment;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class ActivityJinBi extends ActivityBase implements View.OnClickListener {
	public static final String kXingMing = "xing_ming";
	public static final String kXingBie = "xing_bie";
	public static final String kChuSheng = "chu_sheng";
	public static final String kChuShengDi = "chu_sheng_di";
	public static final String kTitle = "title";
	public static final String kContent = "content";

	private String xingMing;
	private String xingBie;
	private String chuSheng;
	private String chuShengDi;
	private String title;
	private String content;
	private static List<String> destPaths;
	private List<String> paths;
	private List<String> serverPaths = new ArrayList<String>();

	/**
	 * views*
	 */
	private ImageView back;
	private TextView jinbiCount;
	private TextView chongZhi;
	private LinearLayout fiveJinBiLL;
	private LinearLayout tenJinBiLL;
	private LinearLayout twentyJinBiLL;
	private LinearLayout fiftyJinBiLL;
	private LinearLayout oneHundreJinBiLL;
	private LinearLayout threeHundreJinBiLL;
	private EditText jinbiEditText;
	private TextView commit;

	/**
	 * 金币数量*
	 */
	private int selectJinBiCount = 0;
	private Account account;

	/**
	 * 图片上传的索引*
	 */
	private int pathIndex;

	private static Activity destActivity;

	public static void open(Activity activity, String xingMing, String xingbBie,
			String chuSheng, String chuShengDi, String content, List<String> paths) {
		Intent intent = new Intent(activity, ActivityJinBi.class);
		intent.putExtra(kXingMing, xingMing);
		intent.putExtra(kXingBie, xingbBie);
		intent.putExtra(kChuSheng, chuSheng);
		intent.putExtra(kChuShengDi, chuShengDi);
		intent.putExtra(kContent, content);
		destPaths = paths;
		destActivity = activity;
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		xingMing = intent.getStringExtra(kXingMing);
		xingBie = intent.getStringExtra(kXingBie);
		chuSheng = intent.getStringExtra(kChuSheng);
		chuShengDi = intent.getStringExtra(kChuShengDi);
		content = intent.getStringExtra(kContent);
		this.paths = destPaths;
		destPaths = null;
		setContentView(R.layout.activity_jinbi);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		jinbiCount = (TextView) findViewById(R.id.jinbiCount);
		chongZhi = (TextView) findViewById(R.id.chongZhi);
		fiveJinBiLL = (LinearLayout) findViewById(R.id.fiveJinBiLL);
		tenJinBiLL = (LinearLayout) findViewById(R.id.tenJinBiLL);
		twentyJinBiLL = (LinearLayout) findViewById(R.id.twentyJinBiLL);
		fiftyJinBiLL = (LinearLayout) findViewById(R.id.fiftyJinBiLL);
		oneHundreJinBiLL = (LinearLayout) findViewById(R.id.oneHundreJinBiLL);
		threeHundreJinBiLL = (LinearLayout) findViewById(R.id.threeHundreJinBiLL);
		jinbiEditText = (EditText) findViewById(R.id.jinbiEditText);
		commit = (TextView) findViewById(R.id.commit);
	}

	@Override
	protected void initViews() {
		jinbiCount.setText(account.jinBiCount + "");
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		chongZhi.setOnClickListener(this);
		fiveJinBiLL.setOnClickListener(this);
		tenJinBiLL.setOnClickListener(this);
		twentyJinBiLL.setOnClickListener(this);
		fiftyJinBiLL.setOnClickListener(this);
		oneHundreJinBiLL.setOnClickListener(this);
		threeHundreJinBiLL.setOnClickListener(this);
		commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.chongZhi:
			ActivityChongZhi.open(this);
			break;
		case R.id.fiveJinBiLL:
			selectJinBiCount = 5;
			changedState(0);
			break;
		case R.id.tenJinBiLL:
			selectJinBiCount = 10;
			changedState(1);
			break;
		case R.id.twentyJinBiLL:
			selectJinBiCount = 20;
			changedState(2);
			break;
		case R.id.fiftyJinBiLL:
			selectJinBiCount = 50;
			changedState(3);
			break;
		case R.id.oneHundreJinBiLL:
			selectJinBiCount = 100;
			changedState(4);
			break;
		case R.id.threeHundreJinBiLL:
			selectJinBiCount = 300;
			changedState(5);
			break;
		case R.id.commit:
			tryToCommitQuestion();
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void changedState(int index) {
		switch (index) {
		case 0:
			fiveJinBiLL.setBackgroundResource(R.drawable.red_stroken_transparent_bg_shape);
			tenJinBiLL.setBackground(null);
			twentyJinBiLL.setBackground(null);
			fiftyJinBiLL.setBackground(null);
			oneHundreJinBiLL.setBackground(null);
			threeHundreJinBiLL.setBackground(null);
			break;
		case 1:
			fiveJinBiLL.setBackground(null);
			tenJinBiLL.setBackgroundResource(R.drawable.red_stroken_transparent_bg_shape);
			twentyJinBiLL.setBackground(null);
			fiftyJinBiLL.setBackground(null);
			oneHundreJinBiLL.setBackground(null);
			threeHundreJinBiLL.setBackground(null);
			break;
		case 2:
			fiveJinBiLL.setBackground(null);
			tenJinBiLL.setBackground(null);
			twentyJinBiLL.setBackgroundResource(R.drawable.red_stroken_transparent_bg_shape);
			fiftyJinBiLL.setBackground(null);
			oneHundreJinBiLL.setBackground(null);
			threeHundreJinBiLL.setBackground(null);
			break;
		case 3:
			fiveJinBiLL.setBackground(null);
			tenJinBiLL.setBackground(null);
			twentyJinBiLL.setBackground(null);
			fiftyJinBiLL.setBackgroundResource(R.drawable.red_stroken_transparent_bg_shape);
			oneHundreJinBiLL.setBackground(null);
			threeHundreJinBiLL.setBackground(null);
			break;
		case 4:
			fiveJinBiLL.setBackground(null);
			tenJinBiLL.setBackground(null);
			twentyJinBiLL.setBackground(null);
			fiftyJinBiLL.setBackground(null);
			oneHundreJinBiLL.setBackgroundResource(R.drawable.red_stroken_transparent_bg_shape);
			threeHundreJinBiLL.setBackground(null);
			break;
		case 5:
			fiveJinBiLL.setBackground(null);
			tenJinBiLL.setBackground(null);
			twentyJinBiLL.setBackground(null);
			fiftyJinBiLL.setBackground(null);
			oneHundreJinBiLL.setBackground(null);
			threeHundreJinBiLL.setBackgroundResource(R.drawable.red_stroken_transparent_bg_shape);
			break;
		}
	}

	/**
	 * 提交到服务器*
	 */
	private void tryToCommitQuestion() {
		/**如果填写了数量，以填写的数量为准**/
		String jinBiCountStr = jinbiEditText.getText().toString();
		if (!TextUtils.isEmpty(jinBiCountStr)) {
			selectJinBiCount = Integer.parseInt(jinBiCountStr);
			if (selectJinBiCount <= 300) {
				ToastUtil.makeShortText("请输入大于300的金币数量");
				return;
			}
		} else {
			if (selectJinBiCount == 0) {
				ToastUtil.makeShortText("请选择悬赏金额");
				return;
			}
		}
		if (selectJinBiCount > account.jinBiCount) {
			ToastUtil.makeShortText("金币不足，请充值");
			ActivityChongZhi.open(this);
			return;
		}
		showDialog();

		/**先判断需不需要上传图片**/
		if (paths != null && paths.size() > 0) {
			serverPaths.clear();
			pathIndex = 0;
			uploadImage(pathIndex);
		} else {
			realCommitToServer();
		}
	}

	private void uploadImage(int index) {
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Request.doFileUploadRequest(this, nameValuePairs, new File(paths.get(index)), ServerConfig.URL_UPLOAD_IMAGE, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("图片上传失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				UploadImageModel uploadImageModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, UploadImageModel.class);
				if (uploadImageModel != null && !TextUtils.isEmpty(uploadImageModel.result)) {
					serverPaths.add(uploadImageModel.result);
					pathIndex++;
					if (pathIndex > paths.size() - 1) {
						realCommitToServer();
					} else {
						uploadImage(pathIndex);
					}
				} else {
					ToastUtil.makeShortText("图片上传失败");
				}
			}
		});
	}

	class UploadImageModel {
		@SerializedName("message")
		public String message;
		@SerializedName("result")
		public String result;
	}

	private void realCommitToServer() {
		JSONObject contentJson = new JSONObject();
		try {
			contentJson.put("type", "text");
			contentJson.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(contentJson);
		for (int i = 0; i < serverPaths.size(); i++) {
			JSONObject imageJson = new JSONObject();
			try {
				imageJson.put("type", "image");
				imageJson.put("content", serverPaths.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonArray.put(imageJson);
		}
		String jsonContent = jsonArray.toString();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("xingming", xingMing));
		nameValuePairs.add(new BasicNameValuePair("xingbie", xingBie));
		nameValuePairs.add(new BasicNameValuePair("chusheng", chuSheng));
		nameValuePairs.add(new BasicNameValuePair("chushengdi", chuShengDi));
		nameValuePairs.add(new BasicNameValuePair("jinbi", selectJinBiCount + ""));
		nameValuePairs.add(new BasicNameValuePair("title", ""));
		nameValuePairs.add(new BasicNameValuePair("content", jsonContent));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_PUB_XUAN_SHANG_TOPIC, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("发表失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				ToastUtil.makeShortText("发表成功");
				Intent intent = new Intent();
				intent.setAction(Actions.ACTION_REFRESH_XUAN_SHANG_LIST);
				sendBroadcast(intent);
				/**本地用户的金币减去**/
				account.jinBiCount = account.jinBiCount - selectJinBiCount;
				account.saveMeInfoToPreference();
				Intent intent1 = new Intent();
				intent1.setAction(MineFragment.RefreshJinBiAction);
				HCApplicaton.getInstance().sendBroadcast(intent1);
				destActivity.finish();
				destActivity = null;
				finish();
			}
		});
	}
}
