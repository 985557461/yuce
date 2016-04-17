package com.example.YuCeClient.ui.mine.ti_xian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.background.db.TableTiXian;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.mine.MineFragment;
import com.example.YuCeClient.util.DisplayUtil;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-19.
 */
public class ActivityTiXian extends ActivityBase implements View.OnClickListener, TiXianItemView.TiXianItemViewListener {
	private ImageView back;
	private TextView tiXianTextView;
	private LinearLayout tixianContainer;
	private TextView addWeiXinZhangHu;
	private TextView addZhiFuBaoZhangHu;
	private TextView addBankCardZhangHu;

	private Account account;

	private TiXianItemView destView;
	private TiXianModel destModel;
	private int moneyCount = 0;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityTiXian.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tixian);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		back = (ImageView) findViewById(R.id.back);
		tiXianTextView = (TextView) findViewById(R.id.tiXianTextView);
		tixianContainer = (LinearLayout) findViewById(R.id.tixianContainer);
		addWeiXinZhangHu = (TextView) findViewById(R.id.addWeiXinZhangHu);
		addZhiFuBaoZhangHu = (TextView) findViewById(R.id.addZhiFuBaoZhangHu);
		addBankCardZhangHu = (TextView) findViewById(R.id.addBankCardZhangHu);
	}

	@Override
	protected void initViews() {
		List<TiXianModel> tiXianModels = TableTiXian.getTiXianModels(account.userId);
		for (TiXianModel tiXianModel : tiXianModels) {
			TiXianItemView itemView = new TiXianItemView(this);
			itemView.setData(tiXianModel, this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.bottomMargin = DisplayUtil.dip2px(this, 20);
			tixianContainer.addView(itemView, params);
		}
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		tiXianTextView.setOnClickListener(this);
		addWeiXinZhangHu.setOnClickListener(this);
		addZhiFuBaoZhangHu.setOnClickListener(this);
		addBankCardZhangHu.setOnClickListener(this);
	}

	private void tryToTiXian() {
		if (destModel == null) {
			ToastUtil.makeShortText("请选择提现方式");
			return;
		}
		if (moneyCount <= 0) {
			ToastUtil.makeShortText("请输入金额");
			return;
		}
		if (moneyCount > account.jinBiCount) {
			ToastUtil.makeShortText("无法提取超出数量的金币");
			return;
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("money", moneyCount + ""));
		nameValuePairs.add(new BasicNameValuePair("tixiantype", destModel.tixiantype));
		nameValuePairs.add(new BasicNameValuePair("cardnum", destModel.cardnum));
		nameValuePairs.add(new BasicNameValuePair("cardaddress", destModel.cardaddress));
		nameValuePairs.add(new BasicNameValuePair("realname", destModel.realname));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_TIXIAN, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("提现失败");
			}

			@Override
			public void onComplete(String response) {
				CommonModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (model != null && model.result == 1) {
					ToastUtil.makeShortText("提现成功，请耐心等待几个工作日");
					account.jinBiCount = account.jinBiCount - moneyCount;
					account.saveMeInfoToPreference();

					Intent intent = new Intent();
					intent.setAction(MineFragment.RefreshJinBiAction);
					HCApplicaton.getInstance().sendBroadcast(intent);

					finish();
				} else {
					ToastUtil.makeShortText("提现失败");
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.tiXianTextView:
			tryToTiXian();
			break;
		case R.id.addWeiXinZhangHu: {
			HCAddWeoXinCardDialog.showDlg(this, new HCAddWeoXinCardDialog.HCAddBankDlgClickListener() {
				@Override
				public void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount) {
					TiXianModel tiXianModel = new TiXianModel();
					tiXianModel.tixiantype = tixiantype;
					tiXianModel.cardaddress = cardaddress;
					tiXianModel.cardnum = cardnum;
					tiXianModel.realname = realname;
					ActivityTiXian.this.moneyCount = moneyCount;

					boolean insert = TableTiXian.addPayItem(tiXianModel.tixiantype, tiXianModel.cardnum, tiXianModel.cardaddress, tiXianModel.realname);
					if (insert) {
						TiXianItemView itemView = new TiXianItemView(ActivityTiXian.this);
						itemView.setData(tiXianModel, ActivityTiXian.this);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						params.bottomMargin = DisplayUtil.dip2px(ActivityTiXian.this, 20);
						tixianContainer.addView(itemView, params);
						refreshContainer(itemView);
					} else {
						ToastUtil.makeShortText("已经存在该账号");
					}
				}
			});
		}
		break;
		case R.id.addZhiFuBaoZhangHu: {
			HCAddZhiFuBaoCardDialog.showDlg(this, new HCAddZhiFuBaoCardDialog.HCAddBankDlgClickListener() {
				@Override
				public void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount) {
					TiXianModel tiXianModel = new TiXianModel();
					tiXianModel.tixiantype = tixiantype;
					tiXianModel.cardaddress = cardaddress;
					tiXianModel.cardnum = cardnum;
					tiXianModel.realname = realname;
					ActivityTiXian.this.moneyCount = moneyCount;

					boolean insert = TableTiXian.addPayItem(tiXianModel.tixiantype, tiXianModel.cardnum, tiXianModel.cardaddress, tiXianModel.realname);
					if (insert) {
						TiXianItemView itemView = new TiXianItemView(ActivityTiXian.this);
						itemView.setData(tiXianModel, ActivityTiXian.this);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						params.bottomMargin = DisplayUtil.dip2px(ActivityTiXian.this, 20);
						tixianContainer.addView(itemView, params);
						refreshContainer(itemView);
					} else {
						ToastUtil.makeShortText("已经存在该账号");
					}
				}
			});
		}
		break;
		case R.id.addBankCardZhangHu: {
			HCAddBankCardDialog.showDlg(this, new HCAddBankCardDialog.HCAddBankDlgClickListener() {
				@Override
				public void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount) {
					TiXianModel tiXianModel = new TiXianModel();
					tiXianModel.tixiantype = tixiantype;
					tiXianModel.cardaddress = cardaddress;
					tiXianModel.cardnum = cardnum;
					tiXianModel.realname = realname;
					ActivityTiXian.this.moneyCount = moneyCount;

					boolean insert = TableTiXian.addPayItem(tiXianModel.tixiantype, tiXianModel.cardnum, tiXianModel.cardaddress, tiXianModel.realname);
					if (insert) {
						TiXianItemView itemView = new TiXianItemView(ActivityTiXian.this);
						itemView.setData(tiXianModel, ActivityTiXian.this);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						params.bottomMargin = DisplayUtil.dip2px(ActivityTiXian.this, 20);
						tixianContainer.addView(itemView, params);
						refreshContainer(itemView);
					} else {
						ToastUtil.makeShortText("已经存在该账号");
					}
				}
			});
		}
		break;
		}
	}

	@Override
	public void onBackPressed() {
		if (HCAddBankCardDialog.isShowing(this)) {
			HCAddBankCardDialog.onBackPressed(this);
			return;
		}
		if (HCAddWeoXinCardDialog.isShowing(this)) {
			HCAddWeoXinCardDialog.onBackPressed(this);
			return;
		}
		if (HCAddZhiFuBaoCardDialog.isShowing(this)) {
			HCAddZhiFuBaoCardDialog.onBackPressed(this);
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClickView(TiXianItemView itemView) {
		refreshContainer(itemView);
		if (destModel.tixiantype.equals("0")) {
			HCAddZhiFuBaoCardDialog dialog = HCAddZhiFuBaoCardDialog.showDlg(this, new HCAddZhiFuBaoCardDialog.HCAddBankDlgClickListener() {
				@Override
				public void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount) {
					ActivityTiXian.this.moneyCount = moneyCount;
				}
			});
			dialog.setData(destModel);
		} else if (destModel.tixiantype.equals("1")) {
			HCAddWeoXinCardDialog dialog = HCAddWeoXinCardDialog.showDlg(this, new HCAddWeoXinCardDialog.HCAddBankDlgClickListener() {
				@Override
				public void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount) {
					ActivityTiXian.this.moneyCount = moneyCount;
				}
			});
			dialog.setData(destModel);
		} else if (destModel.tixiantype.equals("2")) {
			HCAddBankCardDialog dialog = HCAddBankCardDialog.showDlg(this, new HCAddBankCardDialog.HCAddBankDlgClickListener() {
				@Override
				public void onSureBtnClicked(String tixiantype, String cardaddress, String cardnum, String realname, int moneyCount) {
					ActivityTiXian.this.moneyCount = moneyCount;
				}
			});
			dialog.setData(destModel);
		}
	}

	private void refreshContainer(TiXianItemView itemView) {
		destView = itemView;
		destModel = destView.tiXianModel;
		int childCount = tixianContainer.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TiXianItemView itemView1 = (TiXianItemView) tixianContainer.getChildAt(i);
			if (itemView1 == itemView) {
				itemView1.setChoose(true);
			} else {
				itemView1.setChoose(false);
			}
		}
	}
}
