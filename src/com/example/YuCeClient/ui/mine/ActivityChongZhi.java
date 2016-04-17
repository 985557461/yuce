package com.example.YuCeClient.ui.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.HCChongZhiInputConfirmAlert;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-6.
 */
public class ActivityChongZhi extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private TextView nickName;
	private TextView jinBiCount;
	private TextView zhifubao;
	private TextView weixin;
	private TextView yinlian;
	private TextView duanxin;

	private Account account;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityChongZhi.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_chongzhi);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		nickName = (TextView) findViewById(R.id.nickName);
		jinBiCount = (TextView) findViewById(R.id.jinBiCount);
		zhifubao = (TextView) findViewById(R.id.zhifubao);
		weixin = (TextView) findViewById(R.id.weixin);
		yinlian = (TextView) findViewById(R.id.yinlian);
		duanxin = (TextView) findViewById(R.id.duanxin);
	}

	@Override
	protected void initViews() {
		if (!TextUtils.isEmpty(account.userName)) {
			nickName.setText(account.userName);
		} else {
			nickName.setText("无名小卒");
		}
		jinBiCount.setText(account.jinBiCount + "");
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		zhifubao.setOnClickListener(this);
		weixin.setOnClickListener(this);
		yinlian.setOnClickListener(this);
		duanxin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.zhifubao:
			showInputDialogForAli();
			break;
		case R.id.weixin:
			showInputDialogForWeiXin();
			break;
		case R.id.yinlian:
			//不做
			break;
		case R.id.duanxin:
			//不做
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (HCChongZhiInputConfirmAlert.isShowing(this)) {
			HCChongZhiInputConfirmAlert.onBackPressed(this);
			return;
		}
		super.onBackPressed();
	}

	private void showInputDialogForAli() {
		HCChongZhiInputConfirmAlert.showDlg(this, new HCChongZhiInputConfirmAlert.HCChongZhiInputConfirmAlertListener() {
			@Override
			public void onSureClicked(boolean isConfirm, int count) {
				if (isConfirm) {
					payByAli(count);
				}
			}
		});
	}

	private void showInputDialogForWeiXin() {
		HCChongZhiInputConfirmAlert.showDlg(this, new HCChongZhiInputConfirmAlert.HCChongZhiInputConfirmAlertListener() {
			@Override
			public void onSureClicked(boolean isConfirm, int count) {
				if (isConfirm) {
					payByWeiXin(count);
				}
			}
		});
	}

	private void addMyJinBi(final int count) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("money", count + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_ADD_JINBI, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("金币增加失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (commonModel != null && commonModel.result == 1) {
					/**本地用户的金币减去**/
					account.jinBiCount = account.jinBiCount + count;
					account.saveMeInfoToPreference();
					jinBiCount.setText(account.jinBiCount + "");
					Intent intent = new Intent();
					intent.setAction(MineFragment.RefreshJinBiAction);
					HCApplicaton.getInstance().sendBroadcast(intent);
				} else {
					ToastUtil.makeShortText("金币增加失败");
				}
			}
		});
	}


	private void payByAli(final int count) {
		new BmobPay(ActivityChongZhi.this).pay(count, "算命圈金币", new PayListener() {

			@Override
			public void orderId(String s) {

			}

			@Override
			public void succeed() {
				ToastUtil.makeShortText("支付成功!");
				addMyJinBi(count);
			}

			@Override
			public void fail(int i, String s) {
				ToastUtil.makeShortText("支付中断!");
			}

			@Override
			public void unknow() {
				ToastUtil.makeShortText("支付结果未知,请稍后手动查询");
			}
		});
	}

	private void payByWeiXin(final int count) {
		new BmobPay(ActivityChongZhi.this).payByWX(count, "算命圈金币", new PayListener() {

			@Override
			public void orderId(String s) {

			}

			@Override
			public void succeed() {
				ToastUtil.makeShortText("支付成功!");
				addMyJinBi(count);
			}

			@Override
			public void fail(int code, String s) {
				// code为-3意味着没有安装BmobPlugin插件
				if (code == -3) {
					new AlertDialog.Builder(ActivityChongZhi.this).setMessage(
							"监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
							.setPositiveButton("安装",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											installBmobPayPlugin("BmobPayPlugin.apk");
										}
									})
							.setNegativeButton("支付宝支付",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											payByAli(1);
										}
									}).create().show();
				} else {
					Toast.makeText(ActivityChongZhi.this, "支付中断!", Toast.LENGTH_SHORT).show();
				}
				ToastUtil.makeShortText("支付中断!");
			}

			@Override
			public void unknow() {
				ToastUtil.makeShortText("支付结果未知,请稍后手动查询");
			}
		});
	}

	void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
