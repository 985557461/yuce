/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.YuCeClient.chat.chatuidemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.*;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.example.YuCeClient.R;
import com.example.YuCeClient.chat.chatuidemo.utils.UserUtils;
import com.example.YuCeClient.chat.chatuidemo.widget.ExpandGridView;
import com.example.YuCeClient.ui.ActivityBase;

import java.util.List;

public class ChatRoomDetailsActivity extends ActivityBase implements OnClickListener {
	private static final String TAG = "ChatRoomDetailsActivity";
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_CLEAR_ALL_HISTORY = 3;

	String longClickUsername = null;

	private ExpandGridView userGridview;
	private String roomId;
	private ProgressBar loadingPB;
	private Button exitBtn;
	private Button deleteBtn;
	private EMChatRoom room;
	private GridAdapter adapter;
	private int referenceWidth;
	private int referenceHeight;
	private ProgressDialog progressDialog;

	public static ChatRoomDetailsActivity instance;
	
	String st = "";
	// 清空�?有聊天记�?
	private RelativeLayout clearAllHistory;
	private RelativeLayout blacklistLayout;
	private RelativeLayout changeGroupNameLayout;
	
	private RelativeLayout blockGroupMsgLayout;
	private RelativeLayout showChatRoomIdLayout;
	private TextView chatRoomIdTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_details);
		instance = this;
		st = getResources().getString(R.string.people);
		clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
		userGridview = (ExpandGridView) findViewById(R.id.gridview);
		userGridview.setVisibility(View.GONE);
		loadingPB = (ProgressBar) findViewById(R.id.progressBar);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
		blacklistLayout = (RelativeLayout) findViewById(R.id.rl_blacklist);
		changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);

		blockGroupMsgLayout = (RelativeLayout)findViewById(R.id.rl_switch_block_groupmsg);
		showChatRoomIdLayout = (RelativeLayout)findViewById(R.id.rl_group_id);
		chatRoomIdTextView = (TextView)findViewById(R.id.tv_group_id);

		Drawable referenceDrawable = getResources().getDrawable(R.drawable.smiley_add_btn);
		referenceWidth = referenceDrawable.getIntrinsicWidth();
		referenceHeight = referenceDrawable.getIntrinsicHeight();

		 // 获取传过来的groupid
		 roomId = getIntent().getStringExtra("roomId");
		 
		 showChatRoomIdLayout.setVisibility(View.VISIBLE);
		 chatRoomIdTextView.setText("������id��"+roomId);
		 
		 room = EMChatManager.getInstance().getChatRoom(roomId);

		exitBtn.setVisibility(View.GONE);
		deleteBtn.setVisibility(View.GONE);
		blacklistLayout.setVisibility(View.GONE);
		changeGroupNameLayout.setVisibility(View.GONE);
		blockGroupMsgLayout.setVisibility(View.GONE);
		
		// 如果自己是群主，显示解散按钮
		if (EMChatManager.getInstance().getCurrentUser().equals(room.getOwner())) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.GONE);
		}
		
		((TextView) findViewById(R.id.group_name)).setText(room.getName() + "(" + room.getAffiliationsCount() + st);
		adapter = new GridAdapter(this, R.layout.grid, room.getMembers());
		userGridview.setAdapter(adapter);
		
		updateRoom();


		// 设置OnTouchListener
		userGridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		clearAllHistory.setOnClickListener(this);
		blacklistLayout.setOnClickListener(this);
		changeGroupNameLayout.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.being_added);
		String st2 = getResources().getString(R.string.is_quit_the_group_chat);
		String st3 = getResources().getString(R.string.chatting_is_dissolution);
		String st4 = getResources().getString(R.string.are_empty_group_of_news);
		String st5 = getResources().getString(R.string.is_modify_the_group_name);
		final String st6 = getResources().getString(R.string.Modify_the_group_name_successful);
		final String st7 = getResources().getString(R.string.change_the_group_name_failed_please);
		String st8 = getResources().getString(R.string.Are_moving_to_blacklist);
		final String st9 = getResources().getString(R.string.failed_to_move_into);
		
		final String stsuccess = getResources().getString(R.string.Move_into_blacklist_success);
		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(ChatRoomDetailsActivity.this);
				progressDialog.setMessage(st1);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			switch (requestCode) {
			case REQUEST_CODE_EXIT: // �?出群
				progressDialog.setMessage(st2);
				progressDialog.show();
				exitGrop();
				break;
			case REQUEST_CODE_EXIT_DELETE: // 解散�?
				progressDialog.setMessage(st3);
				progressDialog.show();
				deleteGrop();
				break;
			case REQUEST_CODE_CLEAR_ALL_HISTORY:
				// 清空此群聊的聊天记录
				progressDialog.setMessage(st4);
				progressDialog.show();
				clearGroupHistory();
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 点击�?出群组按�?
	 * 
	 * @param view
	 */
	public void exitGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);

	}

	/**
	 * 点击解散群组按钮
	 * 
	 * @param view
	 */
	public void exitDeleteGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
				REQUEST_CODE_EXIT_DELETE);

	}

	/**
	 * 清空群聊天记�?
	 */
	public void clearGroupHistory() {
		EMChatManager.getInstance().clearConversation(room.getId());
		progressDialog.dismiss();
	}

	/**
	 * �?出群�?
	 * 
	 * @param
	 */
	private void exitGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMChatManager.getInstance().leaveChatRoom(roomId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							if(ChatActivity.activityInstance != null)
							    ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "�?出聊天室失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	protected void updateRoom() {
		new Thread(new Runnable() {
			public void run() {
				try {
					final EMChatRoom returnRoom = EMChatManager.getInstance().fetchChatRoomFromServer(roomId);

					runOnUiThread(new Runnable() {
						public void run() {
							((TextView) findViewById(R.id.group_name)).setText(returnRoom.getName() + "(" + returnRoom.getAffiliationsCount()
									+ "�?)");
							loadingPB.setVisibility(View.INVISIBLE);
							adapter.notifyDataSetChanged();
							if (EMChatManager.getInstance().getCurrentUser().equals(returnRoom.getOwner())) {
								// 显示解散按钮
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.GONE);
							} else {
								// 显示�?出按�?
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.GONE);

							}
						}
					});

				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							loadingPB.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 解散群组
	 * 
	 * @param
	 */
	private void deleteGrop() {
		final String st5 = getResources().getString(R.string.Dissolve_group_chat_tofail);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMChatManager.getInstance().dismissGroup(roomId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							if(ChatActivity.activityInstance != null)
							    ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), st5 + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_all_history: // 清空聊天记录
			String st9 = getResources().getString(R.string.sure_to_empty_this);
			Intent intent = new Intent(ChatRoomDetailsActivity.this, AlertDialog.class);
			intent.putExtra("cancel", true);
			intent.putExtra("titleIsCancel", true);
			intent.putExtra("msg", st9);
			startActivityForResult(intent, REQUEST_CODE_CLEAR_ALL_HISTORY);
			break;

		default:
			break;
		}

	}

	/**
	 * 群组成员gridadapter
	 * 
	 * @author admin_new
	 * 
	 */
	private class GridAdapter extends ArrayAdapter<String> {

		private int res;
		public boolean isInDeleteMode;
		private List<String> objects;

		public GridAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			this.objects = objects;
			res = textViewResourceId;
			isInDeleteMode = false;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
		    ViewHolder holder = null;
			if (convertView == null) {
			    holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(res, null);
				holder.imageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
				holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
				holder.badgeDeleteView = (ImageView) convertView.findViewById(R.id.badge_delete);
				convertView.setTag(holder);
			}else{
			    holder = (ViewHolder) convertView.getTag();
			}
			final LinearLayout button = (LinearLayout) convertView.findViewById(R.id.button_avatar);
			// �?后一个item，减人按�?
			if (position == getCount() - 1) {
			    holder.textView.setText("");
				// 设置成删除按�?
			    holder.imageView.setImageResource(R.drawable.smiley_minus_btn);
//				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_minus_btn, 0, 0);
				// 如果不是创建者或者没有相应权限，不提供加减人按钮
				if (!room.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
					// if current user is not room admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else { // 显示删除按钮
					if (isInDeleteMode) {
						// 正处于删除模式下，隐藏删除按�?
						convertView.setVisibility(View.INVISIBLE);
					} else {
						// 正常模式
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					final String st10 = getResources().getString(R.string.The_delete_button_is_clicked);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							EMLog.d(TAG, st10);
							isInDeleteMode = true;
							notifyDataSetChanged();
						}
					});
				}
			} else if (position == getCount() - 2) { // 添加群组成员按钮
			    holder.textView.setText("");
			    holder.imageView.setImageResource(R.drawable.smiley_add_btn);
//				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_add_btn, 0, 0);
				// 如果不是创建者或者没有相应权�?
				if (!room.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
					// if current user is not room admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else {
					// 正处于删除模式下,隐藏添加按钮
					if (isInDeleteMode) {
						convertView.setVisibility(View.INVISIBLE);
					} else {
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					final String st11 = getResources().getString(R.string.Add_a_button_was_clicked);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							EMLog.d(TAG, st11);
							// 进入选人页面
//							startActivityForResult(
//									(new Intent(ChatRoomDetailsActivity.this, GroupPickContactsActivity.class).putExtra("groupId", groupId)),
//									REQUEST_CODE_ADD_USER);
						}
					});
				}
			} else { // 普�?�item，显示群组成�?
				final String username = getItem(position);
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
//				Drawable avatar = getResources().getDrawable(R.drawable.default_avatar);
//				avatar.setBounds(0, 0, referenceWidth, referenceHeight);
//				button.setCompoundDrawables(null, avatar, null, null);
				holder.textView.setText(username);
				UserUtils.setUserAvatar(getContext(), username, holder.imageView);
				// demo群组成员的头像都用默认头像，�?由开发�?�自己去设置头像
				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图�?
					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				final String st12 = getResources().getString(R.string.not_delete_myself);
				final String st13 = getResources().getString(R.string.Are_removed);
				final String st14 = getResources().getString(R.string.Delete_failed);
				final String st15 = getResources().getString(R.string.confirm_the_members);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							// 如果是删除自己，return
							if (EMChatManager.getInstance().getCurrentUser().equals(username)) {
								startActivity(new Intent(ChatRoomDetailsActivity.this, AlertDialog.class).putExtra("msg", st12));
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								Toast.makeText(getApplicationContext(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
								return;
							}
							EMLog.d("room", "remove user from room:" + username);
							deleteMembersFromGroup(username);
						} else {
							// 正常情况下点击user，可以进入用户详情或者聊天页面等�?
							// startActivity(new
							// Intent(GroupDetailsActivity.this,
							// ChatActivity.class).putExtra("userId",
							// user.getUsername()));

						}
					}

					/**
					 * 删除群成�?
					 * 
					 * @param username
					 */
					protected void deleteMembersFromGroup(final String username) {
						final ProgressDialog deleteDialog = new ProgressDialog(ChatRoomDetailsActivity.this);
						deleteDialog.setMessage(st13);
						deleteDialog.setCanceledOnTouchOutside(false);
						deleteDialog.show();
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									// 删除被�?�中的成�?
									EMChatManager.getInstance().removeUserFromGroup(roomId, username);
									isInDeleteMode = false;
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											deleteDialog.dismiss();
											notifyDataSetChanged();
											((TextView) findViewById(R.id.group_name)).setText(room.getName() + "("
													+ room.getAffiliationsCount() + st);
										}
									});
								} catch (final Exception e) {
									deleteDialog.dismiss();
									runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(getApplicationContext(), st14 + e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									});
								}

							}
						}).start();
					}
				});

			}
			return convertView;
		}

		@Override
		public int getCount() {
			return super.getCount() + 2;
		}
	}


	public void back(View view) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
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

	private static class ViewHolder{
	    ImageView imageView;
	    TextView textView;
	    ImageView badgeDeleteView;
	}

}
