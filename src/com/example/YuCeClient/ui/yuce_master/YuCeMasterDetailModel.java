package com.example.YuCeClient.ui.yuce_master;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 15-12-7.
 */
public class YuCeMasterDetailModel {
	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public int result;

	@SerializedName("doctorinfo")
	public List<MasterModel> doctorinfo;

	public static class MasterModel{
		@SerializedName("brief")
		public String brief;

		@SerializedName("cainalv")
		public String cainalv;

		@SerializedName("cainashu")
		public int cainashu;

		@SerializedName("doctor_avatar")
		public String doctor_avatar;

		@SerializedName("doctor_name")
		public String doctor_name;

		@SerializedName("doctor_title")
		public String doctor_title;

		@SerializedName("doctorid")
		public String doctorid;

		@SerializedName("isrecomm")
		public String isrecomm;

		@SerializedName("jinbi")
		public int jinbi;
	}
}
