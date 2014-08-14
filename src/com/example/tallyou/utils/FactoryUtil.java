package com.example.tallyou.utils;

import java.util.ArrayList;

import com.example.tallyou.MainApplication;

import android.preference.PreferenceManager;

/**
 * @author zhangpengzhan
 * 
 *         2014年3月6日 上午12:39:47
 */
public class FactoryUtil {
	/**
	 * 保存数据到私有文件夹的xml文件当中
	 * 
	 * @param mKey
	 * @param mVaule
	 */
	public static void setSavePrivateFolder(String mKey, String mVaule) {

		PreferenceManager
				.getDefaultSharedPreferences(MainApplication.mainApplication)
				.edit().putString(mKey, mVaule).commit();
	}

	/**
	 * 
	 * 获取在私有文件夹中存储的数据
	 * 
	 * @param mKey
	 * @param mType
	 * @return
	 */
	public static String getSavePrivateFolder(String mKey, String mType) {

		String mValue = PreferenceManager.getDefaultSharedPreferences(
				MainApplication.mainApplication).getString(mKey, "");
		if (mValue.equals("")) {
			if (mType.equals(FieldsUtil.mString)) {
				return "";
			} else if (mType.equals(FieldsUtil.mBoolean)) {
				return "false";
			} else if (mType.equals(FieldsUtil.mInt)) {
				return "0";
			}
		}
		return mValue;
	}

	/**
	 * 在线user 信息
	 */
	private static ArrayList<UserInfo> mUserInfoArrayList = new ArrayList<UserInfo>();

	public static ArrayList<UserInfo> getmUserInfoArrayList() {
		return mUserInfoArrayList;
	}

	public static void addmUserInfoToArrayList(UserInfo mUserInfo) {

		mUserInfoArrayList.add(mUserInfo);
	}

	public static void clearmUserInfoArrayList() {
		mUserInfoArrayList.clear();
	}
	
	/**
	 * 将要聊天的用户属性信息
	 */
	private static UserInfo mUserInfo = new UserInfo();
	

	public static UserInfo getmUserInfo() {
		return mUserInfo;
	}

	public static void setmUserInfo(UserInfo mUserInfo) {
		FactoryUtil.mUserInfo = mUserInfo;
	}

}
