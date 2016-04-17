package com.example.YuCeClient.ui.yuce_master;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.background.config.ServerConfig;
import com.example.YuCeClient.util.Debug;
import com.example.YuCeClient.util.Request;
import com.example.YuCeClient.widget.vertical_viewpager.PagerAdapter;
import com.example.YuCeClient.widget.vertical_viewpager.VerticalViewPager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 15-11-7.
 */
public class YuCeMasterFragment extends Fragment implements View.OnClickListener {
	private TextView totalBang;
	private TextView weekBang;
	private VerticalViewPager verticalViewPager;
	private VerticalPagerAdapter verticalPagerAdapter;
	private List<VerticalItemView> verticalItemViews = new ArrayList<VerticalItemView>();
	private TotalPaiHangBangView totalPaiHangBang;
	private WeekPaiHangBangView weekPaiHangBang;

	private List<MasterCommentItemModel> masterCommentListModels = new ArrayList<MasterCommentItemModel>();
	private int index = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			index++;
			if (index > masterCommentListModels.size() - 1) {
				index = 0;
			}
			verticalViewPager.setCurrentItem(index,true);
			handler.sendEmptyMessageDelayed(1,3000);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.yuce_master_fragment, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		totalBang = (TextView) view.findViewById(R.id.totalBang);
		weekBang = (TextView) view.findViewById(R.id.weekBang);
		verticalViewPager = (VerticalViewPager) view.findViewById(R.id.verticalViewPager);
		totalPaiHangBang = (TotalPaiHangBangView) view.findViewById(R.id.totalPaiHangBang);
		weekPaiHangBang = (WeekPaiHangBangView) view.findViewById(R.id.weekPaiHangBang);

		totalBang.setOnClickListener(this);
		weekBang.setOnClickListener(this);

		getComments();
	}

	public void refreshData() {
		if (totalPaiHangBang != null) {
			totalPaiHangBang.onRefresh();
		}
		if (weekPaiHangBang != null) {
			weekPaiHangBang.onRefresh();
		}
	}

	private void getComments() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("start_num", "0"));
		nameValuePairs.add(new BasicNameValuePair("limit", "10"));
		Request.doRequest(getActivity(), nameValuePairs, ServerConfig.URL_MASTER_COMMENTS, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {
				gunDongComment();
			}

			@Override
			public void onComplete(String response) {
				MasterCommentListModel listModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, MasterCommentListModel.class);
				if (listModel != null && listModel.evaluates != null && listModel.evaluates.size() > 0) {
					masterCommentListModels.addAll(listModel.evaluates);
				}
				gunDongComment();
			}
		});
	}

	private void gunDongComment() {
		if (masterCommentListModels.size() == 0) {
			verticalViewPager.setVisibility(View.GONE);
		} else {
			for (MasterCommentItemModel itemModel : masterCommentListModels) {
				VerticalItemView itemView = new VerticalItemView(getActivity());
				String nickName = itemModel.nickname;
				if (TextUtils.isEmpty(nickName)) {
					nickName = "";
				}
				String content = itemModel.content;
				if (TextUtils.isEmpty(content)) {
					content = "";
				}
				itemView.setContent(nickName + ":" + content);
				verticalItemViews.add(itemView);
			}
			verticalPagerAdapter = new VerticalPagerAdapter();
			verticalViewPager.setVisibility(View.VISIBLE);
			verticalViewPager.setAdapter(verticalPagerAdapter);
			verticalViewPager.setCurrentItem(0, true);
			handler.sendEmptyMessageDelayed(1, 3000);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.totalBang:
			totalBang.setTextColor(Color.parseColor("#9a1f22"));
			weekBang.setTextColor(Color.parseColor("#ffffff"));
			totalBang.setBackgroundResource(R.drawable.white_stroken_white_bg_left_round_shape);
			weekBang.setBackgroundResource(R.drawable.white_stroken_red_bg_right_round_shape);
			totalPaiHangBang.setVisibility(View.VISIBLE);
			weekPaiHangBang.setVisibility(View.INVISIBLE);
			break;
		case R.id.weekBang:
			totalBang.setTextColor(Color.parseColor("#ffffff"));
			weekBang.setTextColor(Color.parseColor("#9a1f22"));
			totalBang.setBackgroundResource(R.drawable.white_stroken_red_bg_left_round_shape);
			weekBang.setBackgroundResource(R.drawable.white_stroken_white_bg_right_round_shape);
			totalPaiHangBang.setVisibility(View.INVISIBLE);
			weekPaiHangBang.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 广告的pagerAdapter*
	 */
	private class VerticalPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return masterCommentListModels.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(verticalItemViews.get(position));
			return verticalItemViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(verticalItemViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	private class VerticalItemView extends FrameLayout {
		private TextView broadDesc;

		public VerticalItemView(Context context) {
			super(context);
			init(context);
		}

		private void init(Context context) {
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater.inflate(R.layout.vertical_item_view, this, true);

			broadDesc = (TextView) findViewById(R.id.broadDesc);
		}

		public void setContent(String content) {
			if (!TextUtils.isEmpty(content)) {
				broadDesc.setText(content);
			} else {
				broadDesc.setText("");
			}
		}
	}
}
