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
 * Created by xiaoyu on 15-12-7.
 */
public class CommentDetailView extends FrameLayout implements RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener {
	private RefreshListView listView;

	private CommentAdapter commentAdapter;
	private List<DoctorCommentItemModel> commentModels = new ArrayList<DoctorCommentItemModel>();
	private int p = 0;
	private final int SIZE = 20;

	/**
	 * 评论的类型*
	 */
	private int type = 0;
	private String doctorId;

	public CommentDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CommentDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CommentDetailView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.comment_detail_view, this, true);

		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setCanRefresh(true);
		listView.setCanLoadMore(false);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		commentAdapter = new CommentAdapter();
		listView.setAdapter(commentAdapter);
	}

	public void setData(int type, String doctorId) {
		this.type = type;
		this.doctorId = doctorId;
		onRefresh();
	}

	@Override
	public void onRefresh() {
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		nameValuePairs.add(new BasicNameValuePair("doctorid", doctorId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getContext(), nameValuePairs, ServerConfig.URL_EVALUATE_HISTORY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("数据获取失败");
				listView.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onRefreshComplete();
				DoctorCommentModelList model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, DoctorCommentModelList.class);
				if (model != null && model.result == 1) {
					if (model.evaluates != null && model.evaluates.size() > 0) {
						if (model.evaluates.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						commentModels.clear();
						commentModels.addAll(model.evaluates);
						commentAdapter.notifyDataSetChanged();
					}
				} else {
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	@Override
	public void onLoadMore() {
		p++;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		nameValuePairs.add(new BasicNameValuePair("doctorid", doctorId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getContext(), nameValuePairs, ServerConfig.URL_EVALUATE_HISTORY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				p--;
				ToastUtil.makeShortText("数据加载失败");
				listView.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onLoadMoreComplete();
				DoctorCommentModelList model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, DoctorCommentModelList.class);
				if (model != null && model.result == 1) {
					if (model.evaluates != null && model.evaluates.size() > 0) {
						if (model.evaluates.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						commentModels.addAll(model.evaluates);
						commentAdapter.notifyDataSetChanged();
					}
				} else {
					p--;
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	class CommentAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return commentModels.size();
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
			DoctorCommentItemView itemView = null;
			if (convertView == null) {
				itemView = new DoctorCommentItemView(getContext());
			} else {
				itemView = (DoctorCommentItemView) convertView;
			}
			itemView.setData(commentModels.get(position));
			return itemView;
		}
	}
}
