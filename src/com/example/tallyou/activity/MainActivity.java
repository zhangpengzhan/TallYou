package com.example.tallyou.activity;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallyou.R;
import com.example.tallyou.utils.CustomDialog;
import com.example.tallyou.utils.FactoryUtil;
import com.example.tallyou.utils.FieldsUtil;
import com.example.tallyou.utils.XmppTool;
import com.example.tallyou.utils.view.RegistAlert;

/**
 * @author zhangpengzhan
 * 
 *         2014年3月5日 下午11:51:51
 */
public class MainActivity extends Activity {
	public static MainActivity mMainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMainActivity = this;
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private LinearLayout mLinearLayout, mLinearLayout2, mLinearLayout3,
			mLinearLayout4, mLinearLayout5, mLinearLayout6;// 布局对象
	private ImageView mImageView1;// 头像图画
	private Button btn1, btn2, btn3;// 登陆、注册
	private CheckBox mCheckBox, mCheckBox2;// 记住密码
	private EditText mEditText, mEditText2, mEditText3;// 账号和密码输入框
	public final static int mLoading = 0x0001, mCancel = 0x0002,
			mRegister = 0x0003, mAutoLoading = 0x0004;
	private ProgressBar pb1;
	private CustomDialog mCustomDialog;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case mLoading:
				mLinearLayout.setVisibility(View.GONE);
				mLinearLayout2.setVisibility(View.GONE);
				mLinearLayout3.setVisibility(View.GONE);
				mLinearLayout4.setVisibility(View.GONE);
				mLinearLayout5.setVisibility(View.GONE);
				mLinearLayout6.setVisibility(View.VISIBLE);
				pb1.setVisibility(View.VISIBLE);
				Loading();
				break;
			case mCancel:
				Toast.makeText(mMainActivity, "登陆失败", Toast.LENGTH_SHORT)
						.show();
				mLinearLayout.setVisibility(View.VISIBLE);
				mLinearLayout2.setVisibility(View.VISIBLE);
				mLinearLayout3.setVisibility(View.VISIBLE);
				mLinearLayout4.setVisibility(View.VISIBLE);
				mLinearLayout5.setVisibility(View.VISIBLE);
				mLinearLayout6.setVisibility(View.GONE);
				pb1.setVisibility(View.GONE);
				XmppTool.closeConnection();
				break;
			case mRegister:// 注册
				mCustomDialog = new CustomDialog(mMainActivity,
						FieldsUtil.CUSTOM_DIALOG);
				mCustomDialog.onCreateDialog().show();
				break;
			case mAutoLoading:// 自动登录
				mHandler.sendEmptyMessage(mLoading);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void initView() {
		mLinearLayout = (LinearLayout) this.findViewById(R.id.main_linear);// 账号输入框
		mLinearLayout2 = (LinearLayout) this
				.findViewById(R.id.main_linearLayout1);// 密码输入框
		mLinearLayout3 = (LinearLayout) this
				.findViewById(R.id.main_linearlayout2);// 按钮
		mLinearLayout4 = (LinearLayout) this
				.findViewById(R.id.main_linearlayout3);// 记住密码
		mLinearLayout5 = (LinearLayout) this
				.findViewById(R.id.main_linearLayout4);// 服务器地址
		mLinearLayout6 = (LinearLayout) this
				.findViewById(R.id.main_linearlayout8);// 服务器地址

		mImageView1 = (ImageView) this.findViewById(R.id.main_image);// 头像图片
		mCheckBox = (CheckBox) this.findViewById(R.id.main_checkBox1);// 记住密码
		mCheckBox2 = (CheckBox) this.findViewById(R.id.main_checkBox2);// 自动登录
		mEditText = (EditText) this.findViewById(R.id.main_editText1);// 账号
		mEditText2 = (EditText) this.findViewById(R.id.main_editText2);// 密码
		mEditText3 = (EditText) this.findViewById(R.id.main_editText4);// 服务器地址
		btn1 = (Button) this.findViewById(R.id.main_button1);// loading
		btn2 = (Button) this.findViewById(R.id.main_button2);// 注册
		btn3 = (Button) this.findViewById(R.id.main_btncancel);// 取消
		pb1 = (ProgressBar) this.findViewById(R.id.main_progressBar1);
		// 添加点击监听
		mCheckBox.setOnClickListener(new setViewOnClick());
		mCheckBox2.setOnClickListener(new setViewOnClick());
		btn1.setOnClickListener(new setViewOnClick());
		btn2.setOnClickListener(new setViewOnClick());
		btn3.setOnClickListener(new setViewOnClick());
		// 初始化属性
		mCheckBox.setChecked(Boolean.valueOf(FactoryUtil.getSavePrivateFolder(
				FieldsUtil.isRembered_PW, FieldsUtil.mBoolean)));
		mCheckBox2.setChecked(Boolean.valueOf(FactoryUtil.getSavePrivateFolder(
				FieldsUtil.isAutoLoading, FieldsUtil.mBoolean)));
		mEditText.setText(FactoryUtil.getSavePrivateFolder(FieldsUtil.UserID,
				FieldsUtil.mString));
		mEditText3.setText(FactoryUtil.getSavePrivateFolder(// 设置服务器地址，初始化地址为空
				FieldsUtil.WebAddress, FieldsUtil.mString));
		if (mCheckBox.isChecked()) {
			mEditText2.setText(FactoryUtil.getSavePrivateFolder(
					FieldsUtil.UserPW, FieldsUtil.mString));
		}
		if (mCheckBox2.isChecked()
				&& !mEditText3.getText().toString().equals("")) {
			mHandler.sendEmptyMessage(mAutoLoading);
		}

	}

	private class setViewOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.main_checkBox1: // 记住密码
				/* if (mCheckBox.isChecked()) { */
				/*
				 * FactoryUtil.setSavePrivateFolder(FieldsUtil.isRembered_PW,
				 * String.valueOf(!mCheckBox.isChecked())); } else {
				 */
				FactoryUtil.setSavePrivateFolder(FieldsUtil.isRembered_PW,
						String.valueOf(mCheckBox.isChecked()));
				/* } */
				mCheckBox.setChecked(mCheckBox.isChecked());
				break;
			case R.id.main_button1:// loading
				mHandler.sendEmptyMessage(mLoading);

				break;
			case R.id.main_button2:// 注册
				mHandler.sendEmptyMessage(mRegister);
				break;
			case R.id.main_checkBox2:// 自动登录
				FactoryUtil.setSavePrivateFolder(FieldsUtil.isAutoLoading,
						String.valueOf(mCheckBox2.isChecked()));
				break;
			case R.id.main_btncancel:
				XmppTool.closeConnection();
				mHandler.sendEmptyMessage(mCancel);
				break;
			}
		}

	}

	public void Loading() {
		// 取得填入的用户和密码和服务器地址
		final String USERID = mEditText.getText().toString();
		FactoryUtil.setSavePrivateFolder(FieldsUtil.UserID, USERID);
		final String WEBADDRESS = mEditText3.getText().toString();
		if (!WEBADDRESS.equals(FactoryUtil.getSavePrivateFolder(
				FieldsUtil.WebAddress, FieldsUtil.mString)))
			FactoryUtil.setSavePrivateFolder(FieldsUtil.WebAddress, WEBADDRESS);
		final String PWD = mEditText2.getText().toString();
		if (mCheckBox.isChecked()
				&& !PWD.equals(FactoryUtil.getSavePrivateFolder(
						FieldsUtil.UserPW, FieldsUtil.mString))) {
			FactoryUtil.setSavePrivateFolder(FieldsUtil.UserPW, PWD);
		}
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					try {
						// 连接
						XmppTool.getConnection().login(USERID, PWD);
						// Log.i("XMPPClient", "Logged in as " +
						// XmppTool.getConnection().getUser());

						// 状态
						Presence presence = new Presence(
								Presence.Type.available);
						XmppTool.getConnection().sendPacket(presence);
						System.out.println("list-servicelist==="
								+ XmppTool.getConnection().getRoster());
						// 获取用户列表
						XmppTool.upDataRoster();
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								OnLineUserActivity.class);
						intent.putExtra(FieldsUtil.UserID, USERID);
						startActivity(intent);
						MainActivity.this.finish();
						  /** 
			             * 对下面这个方法的官方解释如下，版本从2.0后开始哦 
			             * Call immediately after one of the flavors of startActivity(Intent) or finish()  
			             * to specify an explicit transition animation to perform next. 
			             * 用工具查到解释为： 
			             *   在startActivity(Intent)或finish()之法之后调用后，会立即用一个指定的描述动画的XML文件来执行 
			             *   下一个Activity  
			             *  
			             * 下面两句是对这个方法两个参数的解释,在此之前小马也看了下别人讲的， 
			             * 其实是错的，看官方的解释肯定没错，不懂英语的用工具查下 
			             * 小马一直都说的，我英语很烂，我能查的你一定也能查得到 
			             * 1.enterAnim  A resource ID of the animation resource  
			             *              to use for the incoming activity. Use 0 for no animation. 
			             * 2.exitAnim   A resource ID of the animation resource  
			             *              to use for the outgoing activity. Use 0 for no animation. 
			             * 一：进入动画  一个动画资源，用于目标Activity 进入屏幕时的动画，此处写0代表无动画 
			             * 二：退出动画  一个动画资源，用于当前Activity 退出屏幕时的动画，此处写0代表无动画 
			             *  
			             * 这个目标、当前怎么理解？比如：startActivity( A（当前）--> B（目标）) 《finish()一样》 
			             * 下面参数中有一个为0，就表示A退出时无动画...一定把参数搞清楚，不然动画就搞晕了 
			             * overridePendingTransition(R.anim.zoom_enter, 0);   
			             * 方法两个参数与目标、当前Activity对应关系如效果下方绿色图所示 
			             */ 
			          //  overridePendingTransition(R.anim.alpha_scale,0); 
			         
			     
			     
			    /** 列几个安卓自带的动画效果，大家可以把上面 overridePendingTransition参数改下看看效果 
			     *  实现淡入淡出的效果 
			        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);     
			        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
			          
			        由左向右滑入的效果 
			        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);     
			        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right); 
			     */ 
						
						
					} catch (XMPPException e) {

						mHandler.sendEmptyMessage(mCancel);

					}
				} catch (Exception e) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "登陆异常",
									Toast.LENGTH_SHORT).show();
						}
					});
					
				}
			}
		});
		t.start();
	}

}
