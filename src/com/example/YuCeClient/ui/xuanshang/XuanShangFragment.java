package com.example.YuCeClient.ui.xuanshang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.util.ImageLoaderUtil;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.RefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-29.
 */
public class XuanShangFragment extends Fragment implements View.OnClickListener, RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener {
	private ImageView avatar;
	private ImageView faXuanShangPost;
	private RefreshListView listView;

	private XuanShangAdapter xuanShangAdapter;
	private List<XuanShangItemModel> xuanShangItemModels = new ArrayList<XuanShangItemModel>();
	private int p = 0;
	private final int SIZE = 20;

	private Account account;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.xuan_shang_fragment, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		account = HCApplicaton.getInstance().getAccount();
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		avatar = (ImageView) view.findViewById(R.id.avatar);
		faXuanShangPost = (ImageView) view.findViewById(R.id.faXuanShangPost);
		listView = (RefreshListView) view.findViewById(R.id.listView);

		faXuanShangPost.setOnClickListener(this);
		listView.setCanRefresh(true);
		listView.setCanLoadMore(false);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				XuanShangItemView itemView = (XuanShangItemView) view;
				if (!TextUtils.isEmpty(itemView.xuanShangItemModel.postId)) {
					ActivityXuanShangDesc.open(getActivity(), itemView.xuanShangItemModel.postId);
				} else {
					ToastUtil.makeShortText("帖子的id为空");
				}
			}
		});

		xuanShangAdapter = new XuanShangAdapter();
		listView.setAdapter(xuanShangAdapter);

		/**初始化头像**/
		if (!TextUtils.isEmpty(account.avatar)) {
			imageLoader.displayImage(account.avatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}
		onRefresh();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.faXuanShangPost:
			Account account = HCApplicaton.getInstance().getAccount();
			if (TextUtils.isEmpty(account.userId)) {
				ActivityLogin.open(getActivity());
			} else {
				ActivityPostXuanShangQuestion.open(getActivity());
			}
			break;
		}
	}

	@Override
	public void onRefresh() {
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getActivity(), nameValuePairs, ServerConfig.URL_XUAN_SHANG_LIST, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("数据加载失败");
				listView.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onRefreshComplete();
				XuanShangListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, XuanShangListModel.class);
				if (model != null && model.result == 1) {
					if (model.posts != null && model.posts.size() > 0) {
						if (model.posts.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						xuanShangItemModels.clear();
						xuanShangItemModels.addAll(model.posts);
						xuanShangAdapter.notifyDataSetChanged();
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
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getActivity(), nameValuePairs, ServerConfig.URL_XUAN_SHANG_LIST, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				p--;
				ToastUtil.makeShortText("数据加载失败");
				listView.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onLoadMoreComplete();
				XuanShangListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, XuanShangListModel.class);
				if (model != null && model.result == 1) {
					if (model.posts != null && model.posts.size() > 0) {
						if (model.posts.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						xuanShangItemModels.addAll(model.posts);
						xuanShangAdapter.notifyDataSetChanged();
					}
				} else {
					p--;
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	class XuanShangAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return xuanShangItemModels.size();
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
			XuanShangItemView itemView = null;
			if (convertView != null) {
				itemView = (XuanShangItemView) convertView;
			} else {
				itemView = new XuanShangItemView(getActivity());
			}
			itemView.setData(xuanShangItemModels.get(position));
			return itemView;
		}
	}
}
