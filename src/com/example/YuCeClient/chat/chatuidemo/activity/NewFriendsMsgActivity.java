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

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;
import com.example.YuCeClient.chat.chatuidemo.Constant;
import com.example.YuCeClient.chat.chatuidemo.adapter.NewFriendsMsgAdapter;
import com.example.YuCeClient.chat.chatuidemo.db.InviteMessgeDao;
import com.example.YuCeClient.chat.chatuidemo.domain.InviteMessage;
import com.example.YuCeClient.ui.ActivityBase;

import java.util.List;

/**
 * 申请与通知
 *
 */
public class NewFriendsMsgActivity extends ActivityBase {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);

		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		//设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		HCApplicaton.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
		
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

	public void back(View view) {
		finish();
	}
	
	
}
