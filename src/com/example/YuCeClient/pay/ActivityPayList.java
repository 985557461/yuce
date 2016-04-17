package com.example.YuCeClient.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.alipay.sdk.app.PayTask;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sreay on 15-11-25.
 */

/**
 * 支付方式选择Activity*
 */
public class ActivityPayList extends ActivityBase implements View.OnClickListener {
	private TextView zhiFuBaoPay;
	private TextView weiXinPay;
	private TextView yinLianPay;

	/**
	 * 支付宝相关*
	 */
	// 商户PID
	public static final String ZHIFUBAO_PARTNER = "";
	// 商户收款账号
	public static final String ZHIFUBAO_SELLER = "";
	// 商户私钥，pkcs8格式
	public static final String ZHIFUBAO_RSA_PRIVATE = "";
	// 支付宝公钥
	public static final String ZHIFUBAO_RSA_PUBLIC = "";
	private static final int ZHIFUBAO_SDK_PAY_FLAG = 1;
	private static final int ZHIFUBAO_SDK_CHECK_FLAG = 2;

	public static final String kGoodsName = "goods_name";
	public static final String kGoodsDesc = "goods_desc";
	public static final String kGoodsPrice = "goods_price";
	private String goodsName;
	private String goodsDesc;
	private String goodsPrice;

	public static void open(Activity activity, String goodsName, String goodsDesc, String goodsPrice) {
		Intent intent = new Intent(activity, ActivityPayList.class);
		intent.putExtra(kGoodsName, goodsName);
		intent.putExtra(kGoodsDesc, goodsDesc);
		intent.putExtra(kGoodsPrice, goodsPrice);
		activity.startActivity(intent);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ZHIFUBAO_SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					ToastUtil.makeShortText("支付成功");
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.makeShortText("支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						ToastUtil.makeShortText("支付失败");
					}
				}
				break;
			}
			case ZHIFUBAO_SDK_CHECK_FLAG: {
				ToastUtil.makeShortText("检查结果为：" + msg.obj);
				break;
			}
			default:
				break;
			}
		}

		;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		goodsName = getIntent().getStringExtra(kGoodsName);
		goodsDesc = getIntent().getStringExtra(kGoodsDesc);
		goodsPrice = getIntent().getStringExtra(kGoodsPrice);

		setContentView(R.layout.activity_pay_list);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		zhiFuBaoPay = (TextView) findViewById(R.id.zhiFuBaoPay);
		weiXinPay = (TextView) findViewById(R.id.weiXinPay);
		yinLianPay = (TextView) findViewById(R.id.yinLianPay);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		zhiFuBaoPay.setOnClickListener(this);
		weiXinPay.setOnClickListener(this);
		yinLianPay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zhiFuBaoPay:
			break;
		case R.id.weiXinPay:
			break;
		case R.id.yinLianPay:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}
		//支付完成,处理自己的业务逻辑!
	}

	/**
	 * 银联支付*
	 */
	private String mMode = "00";//设置测试模式:01为测试 00为正式环境

	private void yinLianPay() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_YINLIAN_PAY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("获取支付数据失败");
			}

			@Override
			public void onComplete(String response) {
				/**获取订单号码**/
				String tn = "";
				UPPayAssistEx.startPayByJAR(ActivityPayList.this, PayActivity.class, null, null, tn, mMode);
			}
		});
	}

	/**
	 * 微信支付*
	 */
	private void weChatPay() {
		final IWXAPI api = HCApplicaton.getInstance().getIWXAPI();
		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (isPaySupported) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			Request.doRequest(this, nameValuePairs, ServerConfig.URL_WECHAT_PAY, Request.GET, new Request.RequestListener() {
				@Override
				public void onException(Request.RequestException e) {
					ToastUtil.makeShortText("支付数据获取失败");
				}

				@Override
				public void onComplete(String response) {
					WeChatPayModel payModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, WeChatPayModel.class);
					/***组装数据去微信支付**/
					PayReq req = new PayReq();
					req.appId = payModel.appid;
					req.partnerId = payModel.partnerid;
					req.prepayId = payModel.prepayid;
					req.nonceStr = payModel.noncestr;
					req.timeStamp = payModel.timestamp;
					req.packageValue = payModel.PACKAGE;
					req.sign = payModel.sign;
					api.sendReq(req);
				}
			});
		} else {
			ToastUtil.makeShortText("请安装微信或升级微信版本才可以使用微信支付");
		}
	}

	/**
	 * 支付宝支付*
	 */
	private void zhiFuBaoPay() {
		if (TextUtils.isEmpty(ZHIFUBAO_PARTNER) || TextUtils.isEmpty(ZHIFUBAO_RSA_PRIVATE)
				|| TextUtils.isEmpty(ZHIFUBAO_SELLER)) {
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | ZHIFUBAO_RSA_PRIVATE| ZHIFUBAO_SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialoginterface, int i) {
									finish();
								}
							}).show();
			return;
		}
		// 订单
		String orderInfo = getZhiFuBaoOrderInfo(goodsName, goodsDesc, goodsPrice);
		// 对订单做RSA 签名
		String sign = zhiFuBaoSign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getZhiFuBaoSignType();
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ActivityPayList.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = ZHIFUBAO_SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 */
	public String getZhiFuBaoOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + ZHIFUBAO_PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + ZHIFUBAO_SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getZhiFuBaoOutTradeNo() + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 */
	public String getZhiFuBaoOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content 待签名订单信息
	 */
	public String zhiFuBaoSign(String content) {
		return SignUtils.sign(content, ZHIFUBAO_RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 */
	public String getZhiFuBaoSignType() {
		return "sign_type=\"RSA\"";
	}
}
