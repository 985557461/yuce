package com.example.YuCeClient.ui.circle;

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
import com.example.YuCeClient.ui.circle.post.ActivityPostDetail;
import com.example.YuCeClient.ui.circle.post.ActivityPostQuestion;
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
 * Created by xiaoyu on 15-12-13.
 */
public class CircleNewFragment extends Fragment implements View.OnClickListener, RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener,
		CircleNewItemView.CircleNewItemViewListener {
	private ImageView avatar;
	private TextView faCirclePost;
	private RefreshListView listView;

	private CircleAdapter circleAdapter;
	private List<CircleItemModel> circleItemModels = new ArrayList<CircleItemModel>();
	private int p = 0;
	private final int SIZE = 20;

	private Account account;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.circle_new_fragment, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		account = HCApplicaton.getInstance().getAccount();
		imageLoader = HCApplicaton.getInstance().getImageLoader();
		avatar = (ImageView) view.findViewById(R.id.avatar);
		faCirclePost = (TextView) view.findViewById(R.id.faCirclePost);
		listView = (RefreshListView) view.findViewById(R.id.listView);

		faCirclePost.setOnClickListener(this);
		listView.setCanRefresh(true);
		listView.setCanLoadMore(false);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		circleAdapter = new CircleAdapter();
		listView.setAdapter(circleAdapter);

		/**初始化头像**/
		if (!TextUtils.isEmpty(account.avatar)) {
			imageLoader.displayImage(account.avatar, avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		} else {
			imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
		}

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CircleNewItemView itemView = (CircleNewItemView) view;
				if (itemView.itemModel != null) {
					ActivityPostDetail.open(getActivity(), itemView.itemModel.postId);
				}
			}
		});
		onRefresh();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.faCirclePost:
			Account account = HCApplicaton.getInstance().getAccount();
			if (TextUtils.isEmpty(account.userId)) {
				ActivityLogin.open(getActivity());
			} else {
				ActivityPostQuestion.open(getActivity());
			}
			break;
		}
	}

	@Override
	public void onRefresh() {
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid",""));
		nameValuePairs.add(new BasicNameValuePair("communityid", "1"));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getActivity(), nameValuePairs, ServerConfig.URL_ALL_COMMUNITY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				ToastUtil.makeShortText("数据加载失败");
				listView.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onRefreshComplete();
				PostListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostListModel.class);
				if (model != null && model.result == 1) {
					if (model.posts != null && model.posts.size() > 0) {
						if (model.posts.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						circleItemModels.clear();
						circleItemModels.addAll(model.posts);
						circleAdapter.notifyDataSetChanged();
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
		nameValuePairs.add(new BasicNameValuePair("userid",""));
		nameValuePairs.add(new BasicNameValuePair("communityid", "1"));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(getActivity(), nameValuePairs, ServerConfig.URL_ALL_COMMUNITY, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				p--;
				ToastUtil.makeShortText("数据加载失败");
				listView.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				listView.onLoadMoreComplete();
				PostListModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostListModel.class);
				if (model != null && model.result == 1) {
					if (model.posts != null && model.posts.size() > 0) {
						if (model.posts.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						circleItemModels.addAll(model.posts);
						circleAdapter.notifyDataSetChanged();
					}
				} else {
					p--;
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	@Override
	public void onCollectionClicked(CircleItemModel itemModel) {
		ToastUtil.makeShortText("收藏：" + itemModel.postId);
	}

	@Override
	public void onShareClicked(CircleItemModel itemModel) {
		ToastUtil.makeShortText("分享：" + itemModel.postId);
	}

	@Override
	public void onLikeClicked(CircleItemModel itemModel) {
		ToastUtil.makeShortText("喜欢：" + itemModel.postId);
	}

	@Override
	public void onCommentClicked(CircleItemModel itemModel) {
		ToastUtil.makeShortText("评论：" + itemModel.postId);
	}

	@Override
	public void onDefaultClicked(CircleItemModel itemModel) {
		if (!TextUtils.isEmpty(itemModel.postId)) {
			ActivityPostDetail.open(getActivity(), itemModel.postId);
		} else {
			ToastUtil.makeShortText("帖子的id为空");
		}
	}

	class CircleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return circleItemModels.size();
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
			CircleNewItemView itemView = null;
			if (convertView != null) {
				itemView = (CircleNewItemView) convertView;
			} else {
				itemView = new CircleNewItemView(getActivity());
			}
			itemView.setData(circleItemModels.get(position));
			itemView.setListener(CircleNewFragment.this);
			return itemView;
		}
	}
}
