package com.example.tallyou.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tallyou.R;
import com.example.tallyou.utils.view.RegistAlert;

/**
 * @author zhangpengzhan
 * 
 *         2014年3月7日 下午2:16:09
 */
public class CustomDialog {
	private Context mContext;
	private int mDialogId;
	private final int mProgressed_Visible = 0x003a, mProgressed_Gone = 0x003b,
			mResult_Ok = 0x003c, mEorr = 0x003d;
	ProgressBar pb;
	EditText mEditText;
	EditText mEditText2;
	EditText mEditText3;
	View mLayout;

	public CustomDialog(Context context, int dialogId) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mDialogId = dialogId;
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = mInflater.inflate(R.layout.activity_main_regist, null);
		pb = (ProgressBar) mLayout.findViewById(R.id.regist_progressBar1);
		mEditText = (EditText) mLayout.findViewById(R.id.regist_editText1);// user
																			// name
		mEditText2 = (EditText) mLayout.findViewById(R.id.regist_editText2);// pw1
		mEditText3 = (EditText) mLayout.findViewById(R.id.regist_editText3);// pw2
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case mProgressed_Visible:
				pb.setVisibility(View.VISIBLE);
				break;
			case mProgressed_Gone:
				pb.setVisibility(View.GONE);
				break;
			case mResult_Ok:
				Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
				break;
			case mEorr:
				Toast.makeText(mContext, "注册失败，请检查网络或者服务器设置",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	public Dialog onCreateDialog() {
		return onCreateDialog(mDialogId);
	}

	String userName = null, pw1 = null, pw2 = null;

	private Dialog onCreateDialog(int dialogId) {
		Dialog dialog = null;
		switch (dialogId) {
		case FieldsUtil.CUSTOM_DIALOG:
			RegistAlert.Builder customBuilder = new RegistAlert.Builder(
					mContext);
			customBuilder
					.setTitle("账号注册")
					.setMessage("创建新账号")
					.setNegativeButton("取    消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("注    册",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									userName = mEditText.getText().toString();
									pw1 = mEditText2.getText().toString();
									pw2 = mEditText3.getText().toString();
									if (userName.equals("") || null == userName) {
										Toast.makeText(mContext, "用户名不能为空",
												Toast.LENGTH_SHORT).show();
										return;
									}
									if (!pw1.equals(pw2) || null == pw1
											|| null == pw2 || pw1.equals("")
											|| pw2.equals("")) {
										Toast.makeText(mContext, "密码输入有误",
												Toast.LENGTH_SHORT).show();
										return;
									}
									mHandler.sendEmptyMessage(mProgressed_Visible);
									new Thread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											// mHandler.sendEmptyMessage(mProgressed_Visible);
											String mResult = XmppTool.regist(
													userName, pw1);
											System.out.println("mResult====="
													+ mResult);
											if (mResult.equals("1")) {
												mHandler.sendEmptyMessage(mResult_Ok);

											}
											if (!mResult.equals("1")) {
												mHandler.sendEmptyMessage(mEorr);
												mHandler.sendEmptyMessage(mProgressed_Gone);

												return;
											}
										}
									}).start();
									dialog.dismiss();
								}
							});

			dialog = customBuilder.create(mLayout);
			break;
		case FieldsUtil.DEFAULT_DIALOG:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
			alertBuilder
					.setTitle("Default title")
					.setMessage("Default body")
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			dialog = alertBuilder.create();
			break;
		}
		return dialog;
	}
}
