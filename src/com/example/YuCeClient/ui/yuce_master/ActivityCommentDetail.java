package com.example.YuCeClient.ui.yuce_master;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;

/**
 * Created by xiaoyu on 15-12-7.
 */
public class ActivityCommentDetail extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private TextView allComment;
	private TextView goodComment;
	private TextView middleComment;
	private TextView badComment;

	private CommentDetailView allCommentDetailView;
	private CommentDetailView goodCommentDetailView;
	private CommentDetailView middleCommentDetailView;
	private CommentDetailView badCommentDetailView;

	public static final String kDoctorId = "doctor_id";
	private String doctorId;

	public static void open(Activity activity,String doctorId) {
		Intent intent = new Intent(activity, ActivityCommentDetail.class);
		intent.putExtra(kDoctorId,doctorId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		doctorId = getIntent().getStringExtra(kDoctorId);
		setContentView(R.layout.activity_comment_detail);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		back = (ImageView) findViewById(R.id.back);
		allComment = (TextView) findViewById(R.id.allComment);
		goodComment = (TextView) findViewById(R.id.goodComment);
		middleComment = (TextView) findViewById(R.id.middleComment);
		badComment = (TextView) findViewById(R.id.badComment);

		allCommentDetailView = (CommentDetailView) findViewById(R.id.allCommentDetailView);
		goodCommentDetailView = (CommentDetailView) findViewById(R.id.goodCommentDetailView);
		middleCommentDetailView = (CommentDetailView) findViewById(R.id.middleCommentDetailView);
		badCommentDetailView = (CommentDetailView) findViewById(R.id.badCommentDetailView);
	}

	@Override
	protected void initViews() {
		allCommentDetailView.setData(1, doctorId);
		goodCommentDetailView.setData(2, doctorId);
		middleCommentDetailView.setData(3, doctorId);
		badCommentDetailView.setData(4, doctorId);
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		allComment.setOnClickListener(this);
		goodComment.setOnClickListener(this);
		middleComment.setOnClickListener(this);
		badComment.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.allComment:
			changeState(0);
			break;
		case R.id.goodComment:
			changeState(1);
			break;
		case R.id.middleComment:
			changeState(2);
			break;
		case R.id.badComment:
			changeState(3);
			break;
		}
	}

	private void changeState(int index) {
		switch (index) {
		case 0:
			allComment.setTextColor(Color.parseColor("#ffffff"));
			goodComment.setTextColor(Color.parseColor("#9a1f22"));
			middleComment.setTextColor(Color.parseColor("#9a1f22"));
			badComment.setTextColor(Color.parseColor("#9a1f22"));

			allComment.setBackgroundResource(R.drawable.red_stroken_red_bg_left_round_shape);
			goodComment.setBackgroundResource(R.drawable.red_stroken_white_bg_center_shape);
			middleComment.setBackgroundResource(R.drawable.red_stroken_white_bg_center_shape);
			badComment.setBackgroundResource(R.drawable.red_stroken_white_bg_right_round_shape);

			allCommentDetailView.setVisibility(View.VISIBLE);
			goodCommentDetailView.setVisibility(View.INVISIBLE);
			middleCommentDetailView.setVisibility(View.INVISIBLE);
			badCommentDetailView.setVisibility(View.INVISIBLE);
			break;
		case 1:
			allComment.setTextColor(Color.parseColor("#9a1f22"));
			goodComment.setTextColor(Color.parseColor("#ffffff"));
			middleComment.setTextColor(Color.parseColor("#9a1f22"));
			badComment.setTextColor(Color.parseColor("#9a1f22"));

			allComment.setBackgroundResource(R.drawable.red_stroken_white_bg_left_round_shape);
			goodComment.setBackgroundResource(R.drawable.red_stroken_red_bg_center_shape);
			middleComment.setBackgroundResource(R.drawable.red_stroken_white_bg_center_shape);
			badComment.setBackgroundResource(R.drawable.red_stroken_white_bg_right_round_shape);

			allCommentDetailView.setVisibility(View.INVISIBLE);
			goodCommentDetailView.setVisibility(View.VISIBLE);
			middleCommentDetailView.setVisibility(View.INVISIBLE);
			badCommentDetailView.setVisibility(View.INVISIBLE);
			break;
		case 2:
			allComment.setTextColor(Color.parseColor("#9a1f22"));
			goodComment.setTextColor(Color.parseColor("#9a1f22"));
			middleComment.setTextColor(Color.parseColor("#ffffff"));
			badComment.setTextColor(Color.parseColor("#9a1f22"));

			allComment.setBackgroundResource(R.drawable.red_stroken_white_bg_left_round_shape);
			goodComment.setBackgroundResource(R.drawable.red_stroken_white_bg_center_shape);
			middleComment.setBackgroundResource(R.drawable.red_stroken_red_bg_center_shape);
			badComment.setBackgroundResource(R.drawable.red_stroken_white_bg_right_round_shape);

			allCommentDetailView.setVisibility(View.INVISIBLE);
			goodCommentDetailView.setVisibility(View.INVISIBLE);
			middleCommentDetailView.setVisibility(View.VISIBLE);
			badCommentDetailView.setVisibility(View.INVISIBLE);
			break;
		case 3:
			allComment.setTextColor(Color.parseColor("#9a1f22"));
			goodComment.setTextColor(Color.parseColor("#9a1f22"));
			middleComment.setTextColor(Color.parseColor("#9a1f22"));
			badComment.setTextColor(Color.parseColor("#ffffff"));

			allComment.setBackgroundResource(R.drawable.red_stroken_white_bg_left_round_shape);
			goodComment.setBackgroundResource(R.drawable.red_stroken_white_bg_center_shape);
			middleComment.setBackgroundResource(R.drawable.red_stroken_white_bg_center_shape);
			badComment.setBackgroundResource(R.drawable.red_stroken_red_bg_right_round_shape);

			allCommentDetailView.setVisibility(View.INVISIBLE);
			goodCommentDetailView.setVisibility(View.INVISIBLE);
			middleCommentDetailView.setVisibility(View.VISIBLE);
			badCommentDetailView.setVisibility(View.INVISIBLE);
			break;
		}
	}
}
