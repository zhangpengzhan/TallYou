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
 *         2014��3��5�� ����11:51:51
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
			mLinearLayout4, mLinearLayout5, mLinearLayout6;// ���ֶ���
	private ImageView mImageView1;// ͷ��ͼ��
	private Button btn1, btn2, btn3;// ��½��ע��
	private CheckBox mCheckBox, mCheckBox2;// ��ס����
	private EditText mEditText, mEditText2, mEditText3;// �˺ź����������
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
				Toast.makeText(mMainActivity, "��½ʧ��", Toast.LENGTH_SHORT)
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
			case mRegister:// ע��
				mCustomDialog = new CustomDialog(mMainActivity,
						FieldsUtil.CUSTOM_DIALOG);
				mCustomDialog.onCreateDialog().show();
				break;
			case mAutoLoading:// �Զ���¼
				mHandler.sendEmptyMessage(mLoading);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void initView() {
		mLinearLayout = (LinearLayout) this.findViewById(R.id.main_linear);// �˺������
		mLinearLayout2 = (LinearLayout) this
				.findViewById(R.id.main_linearLayout1);// ���������
		mLinearLayout3 = (LinearLayout) this
				.findViewById(R.id.main_linearlayout2);// ��ť
		mLinearLayout4 = (LinearLayout) this
				.findViewById(R.id.main_linearlayout3);// ��ס����
		mLinearLayout5 = (LinearLayout) this
				.findViewById(R.id.main_linearLayout4);// ��������ַ
		mLinearLayout6 = (LinearLayout) this
				.findViewById(R.id.main_linearlayout8);// ��������ַ

		mImageView1 = (ImageView) this.findViewById(R.id.main_image);// ͷ��ͼƬ
		mCheckBox = (CheckBox) this.findViewById(R.id.main_checkBox1);// ��ס����
		mCheckBox2 = (CheckBox) this.findViewById(R.id.main_checkBox2);// �Զ���¼
		mEditText = (EditText) this.findViewById(R.id.main_editText1);// �˺�
		mEditText2 = (EditText) this.findViewById(R.id.main_editText2);// ����
		mEditText3 = (EditText) this.findViewById(R.id.main_editText4);// ��������ַ
		btn1 = (Button) this.findViewById(R.id.main_button1);// loading
		btn2 = (Button) this.findViewById(R.id.main_button2);// ע��
		btn3 = (Button) this.findViewById(R.id.main_btncancel);// ȡ��
		pb1 = (ProgressBar) this.findViewById(R.id.main_progressBar1);
		// ���ӵ������
		mCheckBox.setOnClickListener(new setViewOnClick());
		mCheckBox2.setOnClickListener(new setViewOnClick());
		btn1.setOnClickListener(new setViewOnClick());
		btn2.setOnClickListener(new setViewOnClick());
		btn3.setOnClickListener(new setViewOnClick());
		// ��ʼ������
		mCheckBox.setChecked(Boolean.valueOf(FactoryUtil.getSavePrivateFolder(
				FieldsUtil.isRembered_PW, FieldsUtil.mBoolean)));
		mCheckBox2.setChecked(Boolean.valueOf(FactoryUtil.getSavePrivateFolder(
				FieldsUtil.isAutoLoading, FieldsUtil.mBoolean)));
		mEditText.setText(FactoryUtil.getSavePrivateFolder(FieldsUtil.UserID,
				FieldsUtil.mString));
		mEditText3.setText(FactoryUtil.getSavePrivateFolder(// ���÷�������ַ����ʼ����ַΪ��
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
			case R.id.main_checkBox1: // ��ס����
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
			case R.id.main_button2:// ע��
				mHandler.sendEmptyMessage(mRegister);
				break;
			case R.id.main_checkBox2:// �Զ���¼
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
		// ȡ��������û�������ͷ�������ַ
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
						// ����
						XmppTool.getConnection().login(USERID, PWD);
						// Log.i("XMPPClient", "Logged in as " +
						// XmppTool.getConnection().getUser());

						// ״̬
						Presence presence = new Presence(
								Presence.Type.available);
						XmppTool.getConnection().sendPacket(presence);
						System.out.println("list-servicelist==="
								+ XmppTool.getConnection().getRoster());
						// ��ȡ�û��б�
						XmppTool.upDataRoster();
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								OnLineUserActivity.class);
						intent.putExtra(FieldsUtil.UserID, USERID);
						startActivity(intent);
						MainActivity.this.finish();
						  /** 
			             * ��������������Ĺٷ��������£��汾��2.0��ʼŶ 
			             * Call immediately after one of the flavors of startActivity(Intent) or finish()  
			             * to specify an explicit transition animation to perform next. 
			             * �ù��߲鵽����Ϊ�� 
			             *   ��startActivity(Intent)��finish()֮��֮����ú󣬻�������һ��ָ��������������XML�ļ���ִ�� 
			             *   ��һ��Activity  
			             *  
			             * ���������Ƕ�����������������Ľ���,�ڴ�֮ǰС��Ҳ�����±��˽��ģ� 
			             * ��ʵ�Ǵ��ģ����ٷ��Ľ��Ϳ϶�û��������Ӣ����ù��߲��� 
			             * С��һֱ��˵�ģ���Ӣ����ã����ܲ����һ��Ҳ�ܲ�õ� 
			             * 1.enterAnim  A resource ID of the animation resource  
			             *              to use for the incoming activity. Use 0 for no animation. 
			             * 2.exitAnim   A resource ID of the animation resource  
			             *              to use for the outgoing activity. Use 0 for no animation. 
			             * һ�����붯��  һ��������Դ������Ŀ��Activity ������Ļʱ�Ķ������˴�д0�����޶��� 
			             * �����˳�����  һ��������Դ�����ڵ�ǰActivity �˳���Ļʱ�Ķ������˴�д0�����޶��� 
			             *  
			             * ���Ŀ�ꡢ��ǰ��ô���⣿���磺startActivity( A����ǰ��--> B��Ŀ�꣩) ��finish()һ���� 
			             * �����������һ��Ϊ0���ͱ�ʾA�˳�ʱ�޶���...һ���Ѳ������������Ȼ�����͸����� 
			             * overridePendingTransition(R.anim.zoom_enter, 0);   
			             * ��������������Ŀ�ꡢ��ǰActivity��Ӧ��ϵ��Ч���·���ɫͼ��ʾ 
			             */ 
			          //  overridePendingTransition(R.anim.alpha_scale,0); 
			         
			     
			     
			    /** �м�����׿�Դ��Ķ���Ч������ҿ��԰����� overridePendingTransition�������¿���Ч�� 
			     *  ʵ�ֵ��뵭����Ч�� 
			        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);     
			        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
			          
			        �������һ����Ч�� 
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
							Toast.makeText(getApplicationContext(), "��½�쳣",
									Toast.LENGTH_SHORT).show();
						}
					});
					
				}
			}
		});
		t.start();
	}

}