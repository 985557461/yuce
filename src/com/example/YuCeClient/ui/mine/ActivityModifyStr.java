package com.example.YuCeClient.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.ToastUtil;

/**
 * Created by xiaoyu on 2015/7/11.
 */
public class ActivityModifyStr extends ActivityBase implements View.OnClickListener {
	private ImageView back;
	private EditText nickName;
	private TextView sure;

	public static final String kStr = "str";
	public static final String kResult = "result";
	private String str;

	public static void open(Activity activity, String str, int requestCode) {
		Intent intent = new Intent(activity, ActivityModifyStr.class);
		intent.putExtra(kStr, str);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		str = getIntent().getStringExtra(kStr);
		setContentView(R.layout.activity_modify_str);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		back = (ImageView) findViewById(R.id.back);
		sure = (TextView) findViewById(R.id.sure);
		nickName = (EditText) findViewById(R.id.nickName);
	}

	@Override
	protected void initViews() {
		if (!TextUtils.isEmpty(str)) {
			nickName.setText(str);
		}
	}

	@Override
	protected void setListeners() {
		back.setOnClickListener(this);
		sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.back) {
			finish();
		} else if (view.getId() == R.id.sure) {
			String desc = nickName.getText().toString();
			if (TextUtils.isEmpty(desc)) {
				ToastUtil.makeShortText("请输入内容");
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(kResult, desc);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
