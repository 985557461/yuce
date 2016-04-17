package com.example.YuCeClient.util;

import com.example.YuCeClient.widget.RefreshListView;

/**
 * Created by sreay on 14-11-7.
 */
public class ShowFooterUtil {
	public static boolean showFooter(int total, int pageSize, int currentPage, RefreshListView listView) {
		if (listView == null) {
			return false;
		}
		if (total == 0) {
			listView.setCanLoadMore(false);
			return false;
		}
		int pre = total / pageSize;
		int post = (total % pageSize) > 0 ? 1 : 0;
		int totalPage = pre + post;
		if (totalPage > currentPage) {
			listView.setCanLoadMore(true);
			return true;
		} else {
			listView.setCanLoadMore(false);
			return false;
		}
	}

	public static boolean showFooter(int total, int pageSize, int currentPage) {
		if (total == 0) {
			return false;
		}
		int pre = total / pageSize;
		int post = (total % pageSize) > 0 ? 1 : 0;
		int totalPage = pre + post;
		if (totalPage > currentPage) {
			return true;
		} else {
			return false;
		}
	}
}
