package com.example.tallyou.utils;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;

import android.util.Log;

public class XmppTool {

	private static XMPPConnection con = null;

	private static void openConnection() {
		try {
			// url、端口，也可以设置连接的服务器名字，地址，端口，用户。
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					FactoryUtil.getSavePrivateFolder(// 设置服务器地址，初始化地址为空
							FieldsUtil.WebAddress, FieldsUtil.mString), 5222);
			System.out.println("webaddress===="
					+ FactoryUtil.getSavePrivateFolder(// 设置服务器地址，初始化地址为空
							FieldsUtil.WebAddress, FieldsUtil.mString));
			connConfig.setReconnectionAllowed(true);
			connConfig
					.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			connConfig.setSASLAuthenticationEnabled(true);
			connConfig.setTruststorePath("/system/etc/security/cacerts.bks");
			connConfig.setTruststorePassword("changeit");
			connConfig.setTruststoreType("bks");

			con = new XMPPConnection(connConfig);
			con.connect();
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}
	}

	public static XMPPConnection getConnection() {
		if (con == null) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		if (con != null) {
			if (con.isConnected())
				con.disconnect();
			con = null;
		}
	}

	/**
	 * 获取最新用户列表
	 * 
	 * @param mRoster
	 */
	public static void upDataRoster() {
		Roster mRoster = getConnection().getRoster();
		if (FactoryUtil.getmUserInfoArrayList() != null)
			FactoryUtil.clearmUserInfoArrayList();
		Collection<RosterEntry> mCollection = mRoster.getEntries();
		for (RosterEntry mRosterEntry : mCollection) {
			UserInfo mUserInfo = new UserInfo();
			mUserInfo.setName(mRosterEntry.getUser().split("@")[0]);
			mUserInfo.setUser(mRosterEntry.getUser());
			mUserInfo.setGroup_Size(mRosterEntry.getGroups().size());
			Presence mPresence = mRoster.getPresence(mRosterEntry.getUser());
			mUserInfo.setStatus(mPresence.getStatus());
			mUserInfo.setFrom(mPresence.getFrom());
			if (!mRosterEntry.getUser().split("@")[0]
					.equals(FactoryUtil.getSavePrivateFolder(FieldsUtil.UserID,
							FieldsUtil.mString))) {
				FactoryUtil.addmUserInfoToArrayList(mUserInfo);
				System.out.println("==============");
			}
		}
	}

	/**
	 * 注册
	 * 
	 * @param account
	 *            注册帐号
	 * @param password
	 *            注册密码
	 * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
	 */
	public static String regist(String account, String password) {
		if (getConnection() == null)
			return "0";
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(getConnection().getServiceName());
		reg.setUsername(account);// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
		reg.setPassword(password);
		reg.addAttribute("android", "geolo_createUser_android");// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = getConnection().createPacketCollector(
				filter);
		getConnection().sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// Stop queuing results
		collector.cancel();// 停止请求results（是否成功的结果）
		if (result == null) {
			Log.e("RegistActivity", "No response from server.");
			return "0";
		} else if (result.getType() == IQ.Type.RESULT) {
			return "1";
		} else { // if (result.getType() == IQ.Type.ERROR)
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Log.e("RegistActivity", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return "2";
			} else {
				Log.e("RegistActivity", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return "3";
			}

		}

	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public static boolean changePassword(String pwd) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getAccountManager().changePassword(pwd);
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}

}
