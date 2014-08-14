package com.example.tallyou.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallyou.R;
import com.example.tallyou.utils.FactoryUtil;
import com.example.tallyou.utils.FieldsUtil;
import com.example.tallyou.utils.UserInfo;
import com.example.tallyou.utils.XmppTool;

/**
 * @author zhangpengzhan
 * 
 *         2014��3��6�� ����10:00:10
 */
public class OnLineUserActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlineuser_mian);
		initOnLineUserView();
		setOnLineList();
	}

	private TextView mTextView, mTextView2;
	private ImageView mImageView;
	private ListView mListView;
	private LinearLayout mLinearLayout, mLinearLayout2, mLinearLayout3;
	public ArrayList<UserInfo> mArrayList = new ArrayList<UserInfo>();
	private String mUserID;

	/**
	 * ��ʼ���ؼ�
	 */
	private void initOnLineUserView() {
		mTextView = (TextView) this.findViewById(R.id.onlineuser_textView1);
		mTextView2 = (TextView) this.findViewById(R.id.onlineuser_textView2);
		mImageView = (ImageView) this.findViewById(R.id.onlineuser_imageView1);
		mListView = (ListView) this.findViewById(R.id.onlineuser_listView1);
		mLinearLayout = (LinearLayout) this
				.findViewById(R.id.onlineuser_linearlayout);
		mLinearLayout2 = (LinearLayout) this
				.findViewById(R.id.onlineuser_linearlayout2);
		mLinearLayout3 = (LinearLayout) this
				.findViewById(R.id.onlineuser_linearlayout3);

		// ��ʼ���ؼ�����
		mArrayList = FactoryUtil.getmUserInfoArrayList();
		if (mArrayList == null) {
			Toast.makeText(getApplicationContext(), "û���û�����",
					Toast.LENGTH_SHORT).show();
			return;
		} else {

		}
		mUserID = getIntent().getStringExtra(FieldsUtil.UserID);
		mTextView.setText(mUserID);
		mTextView2.setVisibility(View.GONE);
		mImageView.setBackgroundResource(R.drawable.ic_launcher);
	}

	private onLineListAdpater mLineListAdpater;

	/**
	 * �����б�
	 */
	private void setOnLineList() {
		mLineListAdpater = new onLineListAdpater();
		mListView.setAdapter(mLineListAdpater);
		mListView.setOnItemClickListener(new setOnLineListOnItemClick());
		mLineListAdpater.notifyDataSetChanged();

	}

	/**
	 * @author zhangpengzhan
	 *
	 * 2014��3��6��
	 * ����1:42:34
	 */
	class setOnLineListOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent mIntent = new Intent(OnLineUserActivity.this, FormClient.class);
			FactoryUtil.setmUserInfo(mArrayList.get(position));
			OnLineUserActivity.this.startActivityForResult(mIntent,FieldsUtil.CallBack_Value1);
			overridePendingTransition(R.anim.alpha_scale,R.anim.alpha_scale); 
		}

	}
	
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		
		
		
		
	}
	private boolean isPressed = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Log.d("�����ڰ����˳�����", "back"+ isPressed);
			
			if(isPressed){
				
				OnLineUserActivity.this.onDestroy();
				startActivity(new Intent(getBaseContext(), MainActivity.class));
				
			}else{
				Toast.makeText(getApplicationContext(), "�ٴΰ����˳�", Toast.LENGTH_SHORT).show();
				isPressed = true;
				Message msg = new Message();
				msg.what = 0;
				// �ӳ�3��֪ͨ�̸߳ı䰴�����µ�״̬
				mHandler.sendMessageDelayed(msg, FieldsUtil.mTime*1000);
			}
		}
		return true;
		
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		switch(msg.what){
		case 0:
			isPressed = false;
			break;
		}
		}
	};
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		XmppTool.closeConnection();
		this.finish();
	}

	/**
	 * @author zhangpengzhan
	 *
	 * 2014��3��6��
	 * ����1:33:03
	 */
	public class onLineListAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		HashMap<Integer, View> mHashMap = new HashMap<Integer, View>();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mHashMap.get(parent);
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.onlineuser_listitem, null);
				ImageView mImageView = (ImageView) convertView
						.findViewById(R.id.onlineuser_item_imageView1);
				mImageView.setBackgroundResource(R.drawable.ic_launcher);// ͷ��
				TextView mTextView = (TextView) convertView
						.findViewById(R.id.onlineuser_item_textView1);
				mTextView.setText(mArrayList.get(position).getName());// �û���
				TextView mTextView2 = (TextView) convertView
						.findViewById(R.id.onlineuser_item_textView2);
				mTextView2.setText(mArrayList.get(position).getStatus());// ״̬
				mHashMap.put(position, convertView);
			}

			return convertView;
		}

	}

}
