package com.example.YuCeClient.background;

import android.text.TextUtils;
import com.example.YuCeClient.background.config.CommonPreference;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaoyuPC on 2015/6/7.
 */
public class Account {
	public static final String kUsrInfo = "key_user_info";
	public static final String kPhoneNumber = "key_phone_number";
	public static final String kPassword = "key_password";
	public static final String kUserId = "key_user_id";
	public static final String kAvatar = "key_avatar";
	public static final String kUserName = "key_user_name";
	public static final String kJinBiCount = "key_jinbi_count";
	public static final String kPersonDesc = "key_person_desc";
	public static final String kLevel = "key_level";
	public static final String kLevelNickName = "key_level_nickname";
	public static final String kCaiNaCount = "key_caina_count";
	public static final String kSex = "sex";
	public static final String kUserType = "user_type";

	public String phoneNumber;
	public String password;
	public String userId;
	public String avatar;
	public String userName;
	public int jinBiCount;
	public String personDesc;
	public String level;
	public String levelNickName;
	public int caiNaCount;
	public int sex;
	public int userType;//1预测师  0普通用户

	// 用户选择照片时需要记住用户的偏好的文件夹的key
	public static String kLastSelectDir = "lastSelectDir";
	public String lastSelectDir;

	public static Account loadAccount() {
		String userInfo = CommonPreference.getStringValue(kUsrInfo, "");
		Account account = new Account();
		if (TextUtils.isEmpty(userInfo)) {
			return account;
		}
		try {
			JSONObject user = new JSONObject(userInfo);
			account.phoneNumber = user.optString(kPhoneNumber);
			account.password = user.optString(kPassword);
			account.userId = user.optString(kUserId);
			account.avatar = user.optString(kAvatar);
			account.userName = user.optString(kUserName);
			account.jinBiCount = user.optInt(kJinBiCount);
			account.personDesc = user.optString(kPersonDesc);
			account.level = user.optString(kLevel);
			account.levelNickName = user.optString(kLevelNickName);
			account.caiNaCount = user.optInt(kCaiNaCount);
			account.sex = user.optInt(kSex, 1);
			account.userType = user.optInt(kUserType,0);
			account.lastSelectDir = user.optString(kLastSelectDir);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return account;
	}

	public void saveMeInfoToPreference() {
		JSONObject info = new JSONObject();
		try {
			info.put(kPhoneNumber, phoneNumber);
			info.put(kPassword, password);
			info.put(kUserId, userId);
			info.put(kAvatar, avatar);
			info.put(kUserName, userName);
			info.put(kJinBiCount, jinBiCount);
			info.put(kPersonDesc, personDesc);
			info.put(kLevel, level);
			info.put(kLevelNickName, levelNickName);
			info.put(kCaiNaCount, caiNaCount);
			info.put(kSex, sex);
			info.put(kUserType,userType);
			info.put(kLastSelectDir, lastSelectDir);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		CommonPreference.setStringValue(kUsrInfo, info.toString());
	}

	public void clearMeInfo() {
		phoneNumber = "";
		password = "";
		userId = "";
		avatar = "";
		userName = "";
		lastSelectDir = "";
		jinBiCount = 0;
		personDesc = "";
		level = "";
		levelNickName = "";
		caiNaCount = 0;
		sex = 1;
		userType = 0;
		saveMeInfoToPreference();
	}
}
