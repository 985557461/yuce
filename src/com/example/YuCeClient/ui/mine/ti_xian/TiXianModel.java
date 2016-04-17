package com.example.YuCeClient.ui.mine.ti_xian;

/**
 * Created by xiaoyu on 15-12-20.
 */

/**
 * 从数据库里面组装的model*
 */
public class TiXianModel {
	/**
	 * 支付宝0，微信1，银行卡2*
	 */
	public String tixiantype;
	/**
	 * 支付宝账号，微信账号，银行卡账号*
	 */
	public String cardnum;
	/**
	 * 银行卡（例如建设银行，招商银行）*
	 */
	public String cardaddress;
	/**
	 * 支付宝昵称，微信昵称，银行卡姓名*
	 */
	public String realname;
}
