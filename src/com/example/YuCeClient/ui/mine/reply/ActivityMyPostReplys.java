package com.example.YuCeClient.ui.mine.reply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.RefreshListView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-9.
 */
public class ActivityMyPostReplys extends ActivityBase implements View.OnClickListener,
		RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener {
	private FrameLayout backFL;
	private RefreshListView listView;
	private MyReplyAdapter myQuestionAdapter;
	private List<ReplyItemModel> replyItemModels = new ArrayList<ReplyItemModel>();
	private int p = 0;
	private final int SIZE = 20;

	private Account account;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityMyPostReplys.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mypost_replys);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplicaton.getInstance().getAccount();
		backFL = (FrameLayout) findViewById(R.id.backFL);
		listView = (RefreshListView) findViewById(R.id.listView);
	}

	@Override
	protected void initViews() {
		listView.setCanRefresh(true);
		listView.setCanLoadMore(false);

		myQuestionAdapter = new MyReplyAdapter();
		listView.setAdapter(myQuestionAdapter);
	}

	@Override
	protected void setListeners() {
		backFL.setOnClickListener(this);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		onRefresh();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backFL) {
			finish();
		}
	}

	@Override
	public void onRefresh() {
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUANSHANG_MY_REPLY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("数据加载失败");
				listView.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onRefreshComplete();
				ReplyListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, ReplyListModel.class);
				if (model != null && model.result == 1) {
					if (model.myReply != null && model.myReply.size() > 0) {
						if (model.myReply.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						replyItemModels.clear();
						replyItemModels.addAll(model.myReply);
						myQuestionAdapter.notifyDataSetChanged();
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
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUANSHANG_MY_REPLY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				p--;
				ToastUtil.makeShortText("数据加载失败");
				listView.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onLoadMoreComplete();
				ReplyListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, ReplyListModel.class);
				if (model != null && model.result == 1) {
					if (model.myReply != null && model.myReply.size() > 0) {
						if (model.myReply.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						replyItemModels.addAll(model.myReply);
						myQuestionAdapter.notifyDataSetChanged();
					}
				} else {
					p--;
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	class MyReplyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return replyItemModels.size();
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
			MyReplyItemView itemView = null;
			if (convertView != null) {
				itemView = (MyReplyItemView) convertView;
			} else {
				itemView = new MyReplyItemView(ActivityMyPostReplys.this);
			}
			itemView.setData(replyItemModels.get(position));
			return itemView;
		}
	}
}
