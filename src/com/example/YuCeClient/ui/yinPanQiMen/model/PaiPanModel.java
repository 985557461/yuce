package com.example.YuCeClient.ui.yinPanQiMen.model;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2015/10/17.
 */
public class PaiPanModel {
	@SerializedName("xming")
	public String xming;

    @SerializedName("maxing")
    public String maxing;

    @SerializedName("zhifu")
    public String zhifu;

    @SerializedName("jieqi")
    public String jieqi;

    @SerializedName("zixuan")
    public String zixuan;

    @SerializedName("yuejiang")
    public String yuejiang;

    @SerializedName("nongli")
    public String nongli;

    @SerializedName("zhishi")
    public String zhishi;

    @SerializedName("shikongwang")
    public String shikongwang;

    @SerializedName("fourzhu")
    public String fourzhu;

    @SerializedName("gongli")
    public String gongli;

	@SerializedName("paipanlist")
	public List<ResultItemModel> paipanlist;
}
