package com.example.YuCeClient.ui.xuanshang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.Account;
import com.example.YuCeClient.background.CommonModel;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.ui.account.ActivityLogin;
import com.example.YuCeClient.ui.circle.Actions;
import com.example.YuCeClient.ui.circle.post.CommentModel;
import com.example.YuCeClient.ui.circle.post.PostDescModel;
import com.example.YuCeClient.ui.mine.ActivityChongZhi;
import com.example.YuCeClient.ui.mine.MineFragment;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.util.ToastUtil;
import com.example.YuCeClient.widget.HCAlertDlgNoTitle;
import com.example.YuCeClient.widget.HCDaShangConfirmAlert;
import com.example.YuCeClient.widget.HCDaShangInputConfirmAlert;
import com.example.YuCeClient.widget.RefreshListView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-12-1.
 */
public class ActivityXuanShangDesc extends ActivityBase implements View.OnClickListener,
		RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener, XuanShangCommentItemView.XuanShangCommentItemListener,
		XuanShangDescHeader.XuanShangDescHeaderListener {
	private XuanShangDescHeader xuanShangDescHeader;
	private ImageView back;
	private ImageView like;
	private RefreshListView listView;
	private TextView replyTextView;

	private int p = 0;
	private final int SIZE = 50;
	private List<CommentModel> commentModels = new ArrayList<CommentModel>();
	private XuanShangCommentAdapter commentAdapter;

	public static final String kPostId = "post_id";
	private String postId = "";

	private Account account;
	private XuanShangDetailListModel model;
	public static final int RequestReplyPostCode = 10001;

	/**
	 * 是否是自己发表的帖子*
	 */
	private boolean isMyPost = false;
	private boolean isCaiNa = false;

	public static void open(Activity activity, String postId) {
		Intent intent = new Intent(activity, ActivityXuanShangDesc.class);
		intent.putExtra(kPostId, postId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		postId = getIntent().getStringExtra(kPostId);
		setContentView(R.layout.activity_xuan_shang_desc);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		back = (ImageView) findViewById(R.id.back);
		like = (ImageView) findViewById(R.id.like);
		listView = (RefreshListView) findViewById(R.id.listView);
		replyTextView = (TextView) findViewById(R.id.replyTextView);
	}

	@Override
	protected void initViews() {
		account = HCApplicaton.getInstance().getAccount();
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		like.setOnClickListener(this);

		listView.setCanRefresh(false);
		listView.setCanLoadMore(false);
		listView.setOnLoadListener(this);
		replyTextView.setOnClickListener(this);

		onRefresh();
	}

	@Override
	public void onBackPressed() {
		if (HCAlertDlgNoTitle.isShowing(this)) {
			HCAlertDlgNoTitle.onBackPressed(this);
			return;
		}
		if (HCDaShangInputConfirmAlert.isShowing(this)) {
			HCDaShangInputConfirmAlert.onBackPressed(this);
			return;
		}
		super.onBackPressed();
	}

	private void initHeaderData(XuanShangDetailListModel postDescModel) {
		if (postDescModel != null) {
			int headerCount = listView.getHeaderViewsCount();
			if (headerCount > 0) {
				listView.removeHeaderView(xuanShangDescHeader);
			}
			if (postDescModel.iscaina == 1) {
				isCaiNa = true;
			} else {
				isCaiNa = false;
			}
			/**判断是不是自己发的帖子**/
			isMyPost = postDescModel.postFloorId.equals(account.userId);
			xuanShangDescHeader = new XuanShangDescHeader(this);
			xuanShangDescHeader.setData(postDescModel);
			xuanShangDescHeader.setListener(this);
			listView.addHeaderView(xuanShangDescHeader);
			commentAdapter = new XuanShangCommentAdapter();
			listView.setAdapter(commentAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.like:
			tryToLike();
			break;
		case R.id.replyTextView:
			onCommentClicked();
			break;
		}
	}

	private void tryToLike() {
		if (TextUtils.isEmpty(account.userId)) {
			ActivityLogin.open(this);
			return;
		}
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("topicid", postId));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUANSHANG_PRAISE, Request.POST, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("点赞失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				CommonModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (model != null && model.result == 1) {
					ToastUtil.makeShortText("点赞成功");
				} else {
					ToastUtil.makeShortText("点赞失败");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestReplyPostCode && resultCode == RESULT_OK) {
			onRefresh();
		}
	}

	@Override
	public void onRefresh() {
		showDialog();
		p = 0;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", postId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUAN_SHANG_TOPIC_DETAIL, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("数据获取失败");
				listView.onRefreshComplete();
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				listView.onRefreshComplete();
				model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, XuanShangDetailListModel.class);
				if (model != null && model.result == 1) {
					initHeaderData(model);
					if (model.replyPosts != null && model.replyPosts.size() > 0) {
						if (model.replyPosts.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						commentModels.clear();
						commentModels.addAll(model.replyPosts);
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
		if (!TextUtils.isEmpty(account.userId)) {
			nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		}
		nameValuePairs.add(new BasicNameValuePair("topicid", postId));
		nameValuePairs.add(new BasicNameValuePair("start_num", p + ""));
		nameValuePairs.add(new BasicNameValuePair("limit", SIZE + ""));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUAN_SHANG_TOPIC_DETAIL, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("数据获取失败");
				listView.onLoadMoreComplete();
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				listView.onLoadMoreComplete();
				PostDescModel model = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, PostDescModel.class);
				if (model != null && model.result == 1) {
					if (model.replyPosts != null && model.replyPosts.size() > 0) {
						if (model.replyPosts.size() < SIZE) {//说明没有了
							listView.setCanLoadMore(false);
						} else {
							listView.setCanLoadMore(true);
						}
						commentModels.addAll(model.replyPosts);
						commentAdapter.notifyDataSetChanged();
					}
				} else {
					ToastUtil.makeShortText("没有数据");
				}
			}
		});
	}

	@Override
	public void onCaiNaClicked(final CommentModel commentModel) {
		HCAlertDlgNoTitle.showDlg("提示", "是否采纳,确认后将无法修改。", this, new HCAlertDlgNoTitle.HCAlertDlgClickListener() {

			@Override
			public void onAlertDlgClicked(boolean isConfirm) {
				if (isConfirm) {
					tryToCaiNa(commentModel);
				}
			}
		}, true);
	}

	@Override
	public void onDaShangClicked(final CommentModel commentModel) {
		HCDaShangInputConfirmAlert.showDlg(this, new HCDaShangInputConfirmAlert.HCDaShangInputConfirmAlertListener() {
			@Override
			public void onSureClicked(boolean isConfirm, int jinbiCount) {
				if (isConfirm) {
					tryToDaShang(commentModel, jinbiCount + "");
				}
			}
		});
	}

	@Override
	public void onZhuiWenClicked(CommentModel commentModel) {
		if (TextUtils.isEmpty(account.userId)) {
			ActivityLogin.open(this);
		} else {
			if (model != null) {
				ActivityXuanShangReply.openForResult(this, commentModel.replyPostId, commentModel.communityid, commentModel.userName, RequestReplyPostCode);
			}
		}
	}

	private void tryToCaiNa(final CommentModel commentModel) {
		if (TextUtils.isEmpty(account.userId)) {
			ActivityLogin.open(this);
			return;
		}
		/**判断金币数量够不够**/
		final int modelJinbi = Integer.valueOf(model.jinbi);
		int myJinBi = account.jinBiCount;
		if (modelJinbi > myJinBi) {
			ToastUtil.makeShortText("金币不足，请充值");
			ActivityChongZhi.open(this);
			return;
		}
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("topicid", postId));
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("doctorid", commentModel.userId));
		nameValuePairs.add(new BasicNameValuePair("jinbi", model.jinbi));
		nameValuePairs.add(new BasicNameValuePair("replyid", commentModel.replyPostId));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUAN_SHANG_CAINA, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("采纳失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (commonModel != null && commonModel.result == 1) {
					ToastUtil.makeShortText("采纳成功");
					ActivityXuanShangComment.open(ActivityXuanShangDesc.this, postId, commentModel.userId);
					/**当前界面刷新**/
					onRefresh();
					/**通知刷新悬赏列表**/
					Intent intent1 = new Intent();
					intent1.setAction(Actions.ACTION_REFRESH_XUAN_SHANG_LIST);
					HCApplicaton.getInstance().sendBroadcast(intent1);
				} else {
					ToastUtil.makeShortText("采纳失败");
				}
			}
		});
	}

	private void tryToDaShang(CommentModel commentModel, String jinbi) {
		if (TextUtils.isEmpty(account.userId)) {
			ActivityLogin.open(this);
			return;
		}
		/**判断金币数量够不够**/
		final int modelJinbi = Integer.valueOf(jinbi);
		int myJinBi = account.jinBiCount;
		if (modelJinbi > myJinBi) {
			ToastUtil.makeShortText("金币不足，请充值");
			ActivityChongZhi.open(this);
			return;
		}
		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("topicid", commentModel.replyPostId));
		nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
		nameValuePairs.add(new BasicNameValuePair("doctorid", commentModel.userId));
		nameValuePairs.add(new BasicNameValuePair("jinbi", jinbi));
		Request.doRequest(this, nameValuePairs, ServerConfig.URL_XUAN_SHANG_DASHANG, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				dismissDialog();
				ToastUtil.makeShortText("打赏失败");
			}

			@Override
			public void onComplete(String response) {
				dismissDialog();
				CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
				if (commonModel != null && commonModel.result == 1) {
					/**本地用户的金币减去**/
					account.jinBiCount = account.jinBiCount - modelJinbi;
					account.saveMeInfoToPreference();
					Intent intent = new Intent();
					intent.setAction(MineFragment.RefreshJinBiAction);
					HCApplicaton.getInstance().sendBroadcast(intent);
					ToastUtil.makeShortText("打赏成功");
				} else {
					ToastUtil.makeShortText("打赏失败");
				}
			}
		});
	}

	@Override
	public void onCommentClicked() {
		if (TextUtils.isEmpty(account.userId)) {
			ActivityLogin.open(this);
		} else {
			if (model != null) {
				ActivityXuanShangReply.openForResult(this, postId, model.communityid, null, RequestReplyPostCode);
			}
		}
	}

	private class XuanShangCommentAdapter extends BaseAdapter {

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
			XuanShangCommentItemView itemView = null;
			if (itemView == null) {
				itemView = new XuanShangCommentItemView(ActivityXuanShangDesc.this);
			} else {
				itemView = (XuanShangCommentItemView) convertView;
			}
			itemView.setData(commentModels.get(position), isMyPost, isCaiNa);
			itemView.setListener(ActivityXuanShangDesc.this);
			return itemView;
		}
	}
}
