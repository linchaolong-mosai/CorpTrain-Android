package com.mosai.corporatetraining.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.mosai.corporatetraining.entity.CurrentCtUserResponse;
import com.mosai.corporatetraining.entity.UserInfoResponse;
import com.mosai.corporatetraining.util.Utils;

/**
 * 用户本地缓存数据
 * 
 * @author Rays 2016年1月7日
 * 
 */
public class UserPF {
	private static final String DATABASE_NAME = "USER";
	public static final String APP_VERSION = "APP_VERSION";
	/** 默认值为false */
	public static final String PASSWORD = "PASSWORD";
	public static final String IS_LOGIN = "IS_LOGIN";
	public static final String USER_NAME = "USER_NAME";
	public static final String USER_ID = "USER_ID";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String TIME_ZONE = "TIME_ZONE";
	public static final String AVATAR = "AVATAR";
	public static final String SCREEN_NAME = "SCREEN_NAME";
	public static final String TOKEN = "TOKEN";
	public static final String API_TOKEN = "API_TOKEN";
	public static final String COUNTRY_CODE = "COUNTRY_CODE";
	public static final String IS_SYSTEM_CHECKED = "IS_SYSTEM_CHECKED";
	public static final String IS_ADMIN = "IS_ADMIN";
	public static final String IS_CT_ADMIN = "IS_CT_ADMIN";
	public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String RECORDING_DEFAULT = "RECORDING_DEFAULT";
	public static final String ACCOUNT_SN = "ACCOUNT_SN";
	public static final String UPLOAD_LIMIT = "UPLOAD_LIMIT";
	public static final String ATTENDEES_LIMIT = "ATTENDEES_LIMIT";
	public static final String AVATAR_URL = "AVATAR_URL";
	/** 默认值为-1 */
	public static final String LOGO = "LOGO";

    public static final String CT_USER_ID = "CT_USER_ID";
    public static final String CT_ROLE = "CT_ROLE";
    public static final String CT_COMPANY_ID = "CT_COMPANY_ID";
    public static final String CT_COMPANY_NAME = "CT_COMPANY_NAME";
    public static final String CT_COMPANY_VALID = "CT_COMPANY_VALID";
    public static final String PHONE = "PHONE";
    public static final String USER_STATE = "USER_STATE";
    public static final String CT_GROUPS = "CT_GROUPS";

	private SharedPreferences sharedPreferences;
    private static final UserPF INSTANCE = new UserPF();

	private UserPF() {

	}

	public static UserPF getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		sharedPreferences = context.getSharedPreferences(DATABASE_NAME,
				Context.MODE_PRIVATE);
	}
	
	public void putString(String key, String value) {
		sharedPreferences.edit().putString(key, value).commit();
	}
	public String getString(String key, String defValue) {
		return sharedPreferences.getString(key, defValue);
	}
	
	public void putBoolean(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	public boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}
	
	public void putFloat(String key, float value) {
		sharedPreferences.edit().putFloat(key, value).commit();
	}
	public float getFloat(String key, float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}
	
	public void putInt(String key, int value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}
	public int getInt(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}
	
	public void putLong(String key, long value) {
		sharedPreferences.edit().putLong(key, value).commit();
	}
	public long getLong(String key, long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}
	
	/**
	 * 保存用户信息
	 * @param userInfoResponse
	 */
	public void saveUserInfo(UserInfoResponse userInfoResponse) {
		putString(PASSWORD,userInfoResponse.password);
		putBoolean(IS_LOGIN, true);
		putBoolean(IS_ADMIN, userInfoResponse.isAdmin);
		putBoolean(IS_CT_ADMIN, userInfoResponse.isCtAdmin);
		putBoolean(IS_SYSTEM_CHECKED, userInfoResponse.isSystemChecked);
		putString(USER_NAME, userInfoResponse.name);
		putString(USER_EMAIL, userInfoResponse.email);
		putString(API_TOKEN, userInfoResponse.apiToken);
		putString(AVATAR, userInfoResponse.avatar);
		putString(COUNTRY_CODE, userInfoResponse.countryCode);
		putString(SCREEN_NAME, userInfoResponse.screenName);
		putString(TIME_ZONE, userInfoResponse.timeZone);
		putString(TOKEN, userInfoResponse.token);
		putInt(USER_ID, userInfoResponse.userId);
		putInt(ACCOUNT_SN, userInfoResponse.accountSn);
		putInt(ACCOUNT_TYPE, userInfoResponse.accountType);
		putInt(ATTENDEES_LIMIT, userInfoResponse.attendeesLimit);
		putInt(LOGO, userInfoResponse.logo);
		putInt(RECORDING_DEFAULT, userInfoResponse.recordingDefault);
		putInt(UPLOAD_LIMIT, userInfoResponse.uploadLimit);
		putString(AVATAR_URL, Utils.getAvatar(userInfoResponse.userId));
	}
	public String getAvatarUrl(){
		return getString(AVATAR_URL,"");
	}
    public void  saveCtUser(CurrentCtUserResponse currentCtUserResponse) {
        putString(CT_USER_ID, currentCtUserResponse.CtUser.ctUserId);
        putString(CT_COMPANY_ID, currentCtUserResponse.CtUser.ctCompanyId);
        putString(CT_COMPANY_NAME, currentCtUserResponse.CtUser.ctCompanyName);
        putInt(CT_ROLE, currentCtUserResponse.CtUser.ctRole);
        putBoolean(CT_COMPANY_VALID, currentCtUserResponse.CtUser.valid);
        putString(PHONE, currentCtUserResponse.CtUser.phone);
        putInt(USER_STATE, currentCtUserResponse.CtUser.userState);
        putString(CT_GROUPS, JSON.toJSONString(currentCtUserResponse.CtUser.groups));
    }

}