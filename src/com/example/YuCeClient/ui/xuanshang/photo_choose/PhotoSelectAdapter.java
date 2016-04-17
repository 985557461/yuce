package com.example.YuCeClient.ui.xuanshang.photo_choose;

/**
 * Created by xiaoyu on 15-11-30.
 */
public abstract class PhotoSelectAdapter<T> {

	/**控件的总的宽度**/
	public abstract int getWidth();
	/**
	 * 总共有多少条数据*
	 */
	public abstract int getTotalCount();

	/**
	 * 每一行现实几个*
	 */
	public abstract int getColCount();

	/**
	 * 水平间距*
	 */
	public abstract int getHorMargin();

	/**
	 * 竖直间距*
	 */
	public abstract int getVerMargin();

	/**
	 * getView自己去做转换吧*
	 */
	public abstract void setView(T view, int position);
}
