package com.example.YuCeClient.chat.chatuidemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.*;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.YuCeClient.R;
import com.example.YuCeClient.ui.ActivityBase;

import java.util.Collections;
import java.util.List;

public class GroupBlacklistActivity extends ActivityBase {
	private ListView listView;
	private ProgressBar progressBar;
	private BlacklistAdapter adapter;
	private String groupId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_group_blacklist);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		listView = (ListView) findViewById(R.id.list);

		groupId = getIntent().getStringExtra("groupId");
		// 注册上下文菜单
		registerForContextMenu(listView);
		final String st1 = getResources().getString(R.string.get_failed_please_check);
		new Thread(new Runnable() {

			public void run() {
				try {
					List<String> blockedList = EMChatManager.getInstance().getBlockedGroupUsers(groupId);
					if(blockedList != null){
						Collections.sort(blockedList);
						adapter = new BlacklistAdapter(GroupBlacklistActivity.this, 1, blockedList);
						runOnUiThread(new Runnable() {
							public void run() {
								listView.setAdapter(adapter);
								progressBar.setVisibility(View.INVISIBLE);
							}
						});
					}
				} catch (EaseMobException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
		}).start();

	}

	@Override
	protected void getViews() {

	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.remove_from_blacklist, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.remove) {
			final String tobeRemoveUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 移出黑名单
			removeOutBlacklist(tobeRemoveUser);
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 移出黑民单
	 * 
	 * @param tobeRemoveUser
	 */
	void removeOutBlacklist(final String tobeRemoveUser) {
		final String st2 = getResources().getString(R.string.Removed_from_the_failure);
		try {
			// 移出黑民单
		    EMChatManager.getInstance().unblockGroupUser(groupId, tobeRemoveUser);
			adapter.remove(tobeRemoveUser);
		} catch (Exception e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	/**
	 * adapter
	 * 
	 */
	private class BlacklistAdapter extends ArrayAdapter<String> {

		public BlacklistAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.row_contact, null);
			}

			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(getItem(position));

			return convertView;
		}

	}
	
}
