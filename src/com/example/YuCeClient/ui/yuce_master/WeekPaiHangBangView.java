package com.example.YuCeClient.ui.yuce_master;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.RefreshListView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-4.
 */
public class WeekPaiHangBangView extends FrameLayout implements RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener{
	private RefreshListView listView;
	private YuCePeopleAdapter yuCePeopleAdapter;
	private List<YuCePeopleModel> yuCePeopleModels = new ArrayList<YuCePeopleModel>();
	private int p = 0;
	private final int SIZE = 20;

	public WeekPaiHangBangView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public WeekPaiHangBangView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WeekPaiHangBangView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.total_pai_hang_bang_view, this, true);

		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setCanRefresh(false);
		listView.setCanLoadMore(false);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		yuCePeopleAdapter = new YuCePeopleAdapter();
		listView.setAdapter(yuCePeopleAdapter);

		onRefresh();
	}

	@Override
	public void onLoadMore() {
		p++;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("type", "2"));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getContext(), nameValuePairs, ServerConfig.URL_DOCTOR_LIST, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				p--;
				ToastUtil.makeShortText("数据加载失败");
				listView.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onLoadMoreComplete();
				YuCePeopleListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, YuCePeopleListModel.class);
				if (model != null && model.result == 1) {
					if (model.doctorinfo != null && model.doctorinfo.size() > 0) {
						if (model.doctorinfo.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						yuCePeopleModels.addAll(model.doctorinfo);
						yuCePeopleAdapter.notifyDataSetChanged();
					}
				} else {
					p--;
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("type", "2"));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getContext(), nameValuePairs, ServerConfig.URL_DOCTOR_LIST, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("数据加载失败");
				listView.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onRefreshComplete();
				YuCePeopleListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, YuCePeopleListModel.class);
				if (model != null && model.result == 1) {
					if (model.doctorinfo != null && model.doctorinfo.size() > 0) {
						if (model.doctorinfo.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						yuCePeopleModels.clear();
						yuCePeopleModels.addAll(model.doctorinfo);
						yuCePeopleAdapter.notifyDataSetChanged();
					}
				} else {
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	class YuCePeopleAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return getLineCount();
		}

		public int getLineCount() {
			if (yuCePeopleModels.size() == 0) {
				return 0;
			} else {
				return yuCePeopleModels.size() / 2 + (yuCePeopleModels.size() % 2 == 0 ? 0 : 1);
			}
		}


		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int one = position * 2;
			int two = one + 1;
			YuCePeopleModel oneModel = yuCePeopleModels.get(one);
			YuCePeopleModel twoModel = null;
			if (two < yuCePeopleModels.size()) {
				twoModel = yuCePeopleModels.get(two);
			}
			YuCePeopleLineItemView yuCePeopleLineItemView = null;
			if (convertView == null) {
				yuCePeopleLineItemView = new YuCePeopleLineItemView(getContext());
			} else {
				yuCePeopleLineItemView = (YuCePeopleLineItemView) convertView;
			}
			yuCePeopleLineItemView.setData(oneModel, twoModel);
			return yuCePeopleLineItemView;
		}
	}
}
