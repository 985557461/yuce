package com.example.YuCeClient.ui.anli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.YuCeClient.R;

/**
 * Created by xiaoyu on 15-11-7.
 */
/**现在这个没有用到,废弃**/
public class AnLiFragment extends Fragment implements View.OnClickListener {
	private TextView close;
	private TextView complete;
	private TextView picture;
	private TextView textSize;
	private TextView biaoZhu;
	private TextView share;
	private TextView suiXi;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.anli_fragment, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		close = (TextView) view.findViewById(R.id.close);
		complete = (TextView) view.findViewById(R.id.complete);
		picture = (TextView) view.findViewById(R.id.picture);
		textSize = (TextView) view.findViewById(R.id.textSize);
		biaoZhu = (TextView) view.findViewById(R.id.biaoZhu);
		share = (TextView) view.findViewById(R.id.share);
		suiXi = (TextView) view.findViewById(R.id.suiXi);

		close.setOnClickListener(this);
		complete.setOnClickListener(this);
		picture.setOnClickListener(this);
		textSize.setOnClickListener(this);
		biaoZhu.setOnClickListener(this);
		share.setOnClickListener(this);
		suiXi.setOnClickListener(this);

		textSize.setSelected(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			break;
		case R.id.complete:
			break;
		case R.id.picture:
			picture.setSelected(true);
			textSize.setSelected(false);
			biaoZhu.setSelected(false);
			share.setSelected(false);
			suiXi.setSelected(false);
			break;
		case R.id.textSize:
			picture.setSelected(false);
			textSize.setSelected(true);
			biaoZhu.setSelected(false);
			share.setSelected(false);
			suiXi.setSelected(false);
			break;
		case R.id.biaoZhu:
			picture.setSelected(false);
			textSize.setSelected(false);
			biaoZhu.setSelected(true);
			share.setSelected(false);
			suiXi.setSelected(false);
			break;
		case R.id.share:
			picture.setSelected(false);
			textSize.setSelected(false);
			biaoZhu.setSelected(false);
			share.setSelected(true);
			suiXi.setSelected(false);
			break;
		case R.id.suiXi:
			picture.setSelected(false);
			textSize.setSelected(false);
			biaoZhu.setSelected(false);
			share.setSelected(false);
			suiXi.setSelected(true);
			break;
		}
	}
}
