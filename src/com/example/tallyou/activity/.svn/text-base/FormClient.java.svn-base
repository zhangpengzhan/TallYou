package com.example.tallyou.activity;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallyou.R;
import com.example.tallyou.utils.FactoryUtil;
import com.example.tallyou.utils.FieldsUtil;
import com.example.tallyou.utils.SoundMeter;
import com.example.tallyou.utils.TimeRender;
import com.example.tallyou.utils.UserInfo;
import com.example.tallyou.utils.VideoUtil;
import com.example.tallyou.utils.XmppTool;
import com.example.tallyou.utils.service.VoiceService;

@SuppressLint("NewApi")
public class FormClient extends Activity {

	private MyAdapter adapter;
	private List<Msg> listMsg = new ArrayList<Msg>();
	private String pUSERID;
	private EditText msgText;
	private ProgressBar pb;
	private UserInfo mUserInfo;
	private String affixpath;
	public FormClient formClient;

	public String getAffixpath() {
		return affixpath;
	}

	public void setAffixpath(String affixpath) {
		this.affixpath = affixpath;
	}

	public class Msg {
		String userid;
		String msg;
		String date;
		String from;
		String path;

		public Msg(String userid, String msg, String date, String from,
				String path) {
			this.userid = userid;
			this.msg = msg;
			this.date = date;
			this.from = from;
			this.path = path;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formclient);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 禁止屏幕休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		formClient = this;
		initView();
		initSurfaceView();
		mUserInfo = new UserInfo();
		mUserInfo = FactoryUtil.getmUserInfo();
		// 获取Intent传过来的用户名
		this.pUSERID = getIntent().getStringExtra("USERID");
		this.setTitle(pUSERID);
		ListView listview = (ListView) findViewById(R.id.formclient_listview);
		listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		this.adapter = new MyAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new mListOnClickLitener());
		// 获取文本信息
		this.msgText = (EditText) findViewById(R.id.formclient_text);
		this.pb = (ProgressBar) findViewById(R.id.formclient_pb);

		// 消息监听
		ChatManager cm = XmppTool.getConnection().getChatManager();
		// 发送消息给yinghan-pc服务器的小王（获取自己的服务器，和好友）
		final Chat newchat = cm.createChat(mUserInfo.getUser(), null);

		cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) {
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat2, Message message) {
						// 收到来自yinghan-pc服务器小王的消息（获取自己的服务器，和好友）
						if (message.getFrom().contains(mUserInfo.getUser())) {
							// 获取用户、消息、时间、IN
							String[] args = new String[] { mUserInfo.getName(),
									message.getBody(), TimeRender.getDate(1),
									"IN", "" };

							// 在handler里取出来显示消息
							android.os.Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = args;
							msg.sendToTarget();
						} else {
							// message.getFrom().cantatins(获取列表上的用户，组，管理消息);
						}

					}
				});
			}
		});

		// 附件
		Button btattach = (Button) findViewById(R.id.formclient_btattach);
		btattach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FormClient.this, FormFiles.class);
				startActivityForResult(intent, 2);
			}
		});
		// 发送消息
		Button btsend = (Button) findViewById(R.id.formclient_btsend);
		btsend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取text文本
				String msg = msgText.getText().toString();

				if (msg.length() > 0) {
					// 发送消息
					listMsg.add(new Msg(pUSERID, msg, TimeRender.getDate(1),
							"OUT", ""));
					// 刷新适配器
					adapter.notifyDataSetChanged();

					try {
						// 发送消息给xiaowang
						newchat.sendMessage(msg);

					} catch (XMPPException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(FormClient.this, "请输入信息", Toast.LENGTH_SHORT)
							.show();
				}
				// 清空text
				msgText.setText("");
			}
		});

		Button btn_sendvideo = (Button) this
				.findViewById(R.id.formclient_btsend);
		btn_sendvideo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		// 接受文件
		FileTransferManager fileTransferManager = new FileTransferManager(
				XmppTool.getConnection());
		fileTransferManager
				.addFileTransferListener(new RecFileTransferListener());
	}

	/**
	 * 消息列表点击监听
	 * 
	 * @author zhangpengzhan
	 * 
	 *         2014年3月11日 下午1:51:11
	 */
	String mMediaPath = "";

	class mListOnClickLitener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			mMediaPath = listMsg.get(position).path;
			System.out.println("mMediaPath===========" + mMediaPath);
			setAffixpath(mMediaPath);
			if (!mMediaPath.equals(""))
				if (mMediaPath.indexOf(FieldsUtil.VoiceSuffix) != -1) {
					System.out.println("Voice================");
					Intent mIntent = new Intent(formClient, VoiceService.class);
					mIntent.putExtra(FieldsUtil.VoicePath, getAffixpath());
					formClient.startService(mIntent);
				} else if (mMediaPath.indexOf(FieldsUtil.VideoSuffix) != -1) {
					System.out.println("Video=============");
					mHandler.sendEmptyMessage(2);
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MediaPlayer mediaPlayer = mVideoUtil
									.mediaPlayerStart(mSurfaceHolder,
											mMediaPath);
							mediaPlayer
									.setOnCompletionListener(new OnCompletionListener() {

										public void onCompletion(MediaPlayer mp) {
											// TODO Auto-generated method stub
											// 视频播放停止
											mVideoUtil.meidaPlayerStop();
											mHandler.sendEmptyMessage(1);
										}
									});
							mediaPlayer.start();
						}
					}).start();

				}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/** 初始化摄像头 */
	private void InitCamera() {
		try {
			mCamera = Camera.open();
			mCamera.stopPreview();
			mCamera.setDisplayOrientation(90); // 设置横行录制
			mCamera.unlock();
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			if (mCamera != null) {
				mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private RelativeLayout mBottom;
	private TextView mBtnRcd;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:// 录音时间太短
				break;
			case 1:// 视频播放结束
				surfaceLinearLayout.setVisibility(View.GONE);
				break;
			case 2:// 视频播放开始
				mProgressBar.setVisibility(View.GONE);
				surfaceLinearLayout.setVisibility(View.VISIBLE);
				break;
			case 3:// 录像结束formclient_videolinearLayout
				surfaceLinearLayout.setVisibility(View.GONE);
				// 发送消息
				listMsg.add(new Msg(pUSERID, "影像消息", TimeRender.getDate(1),
						"OUT", mVideoUtil.getVideoPath().toString()));
				System.out.println("Video add message========"
						+ mVideoUtil.getVideoPath().toString());
				// 刷新适配器
				adapter.notifyDataSetChanged();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						sendFile(mVideoUtil.getVideoPath());
					}
				}).start();
				break;
			case 4:// 录像开始
				mProgressBar.setVisibility(View.GONE);
				surfaceLinearLayout.setVisibility(View.VISIBLE);

				break;
			default:
				break;
			}
		}
	};
	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
			voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private VideoUtil mVideoUtil;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private long startVoiceT, endVoiceT;
	private String voiceName;
	private SurfaceView mSurfaceview = null; // SurfaceView对象：(视图组件)视频显示
	private SurfaceHolder mSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
	private Camera mCamera = null; // Camera对象，相机预览
	private ImageButton right_btn;
	private Button left_btn;
	private boolean isSaveVideo = false;
	private LinearLayout surfaceLinearLayout;
	private ProgressBar mProgressBar;

	/**
	 * 摄像预览框
	 */
	private void initSurfaceView() {
		mProgressBar = (ProgressBar) findViewById(R.id.formclient_progressBar1);
		surfaceLinearLayout = (LinearLayout) findViewById(R.id.formclient_videolinearLayout);
		mSurfaceview = (SurfaceView) findViewById(R.id.camera_preview);
		mSurfaceHolder = mSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象

		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置显示器类型，setType必须设置

		right_btn = (ImageButton) findViewById(R.id.right_btn);
		right_btn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out
							.println("this right button down========upupupupupupup");
					isSaveVideo = true;
					mHandler.sendEmptyMessage(4);
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							InitCamera();
							mVideoUtil.recorder(mSurfaceHolder, mCamera);
							mProgressBar.setVisibility(View.GONE);
						}
					}, 200);

					break;
				case MotionEvent.ACTION_UP:
					System.out
							.println("this right button down========downdowndowndown");

					mVideoUtil.recorderStop();
					mHandler.sendEmptyMessage(3);
					isSaveVideo = false;
					break;
				}
				return true;
			}
		});

	}

	/**
	 * 语音控件
	 */
	public void initView() {
		mBtnRcd = (TextView) findViewById(R.id.btn_rcd);// 按住说话控件
		mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);// 文字输入框 和发送按钮
		chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);// 语音按钮
		volume = (ImageView) this.findViewById(R.id.volume);// 提示声音大小
		rcChat_popup = this.findViewById(R.id.rcChat_popup);// 音量提示界面
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		del_re = (LinearLayout) this.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter();
		mVideoUtil = new VideoUtil();
		// 语音文字切换按钮
		chatting_mode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.out.println("chatting mode btn=====");
				if (btn_vocie) {
					mBtnRcd.setVisibility(View.GONE);
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn
							.setImageResource(R.anim.chatting_setmode_msg_btn);

				} else {
					mBtnRcd.setVisibility(View.VISIBLE);
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn
							.setImageResource(R.anim.chatting_setmode_voice_btn);
					btn_vocie = true;
				}
			}
		});
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// 按下语音录制按钮时返回false执行父类OnTouch
				return false;
			}
		});
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
			return false;
		}

		if (btn_vocie) {
			System.out.println("1");
			int[] location = new int[2];
			mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
			int btn_rc_Y = location[1];
			int btn_rc_X = location[0];
			int[] del_location = new int[2];
			del_re.getLocationInWindow(del_location);
			int del_Y = del_location[1];
			int del_x = del_location[0];
			if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				// System.out.println("2");
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
					System.out.println("3");
					mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// 松开手势时执行录制完成
				// System.out.println("4");
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					stop();
					flag = 1;

					String voicePath = android.os.Environment
							.getExternalStorageDirectory().toString()
							+ FieldsUtil.VoiceFile_S + voiceName;
					if (new File(voicePath).exists())
						new File(voicePath).delete();

				} else {

					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					endVoiceT = System.currentTimeMillis();
					flag = 1;
					int time = (int) ((endVoiceT - startVoiceT) / 1000);
					System.out.println("time=========" + time);
					if (time < 1) {
						isShosrt = true;
						voice_rcd_hint_loading.setVisibility(View.GONE);
						voice_rcd_hint_rcding.setVisibility(View.GONE);
						voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
						Toast.makeText(formClient, "时间太短", Toast.LENGTH_SHORT)
								.show();
						mHandler.postDelayed(new Runnable() {
							public void run() {
								voice_rcd_hint_tooshort
										.setVisibility(View.GONE);
								rcChat_popup.setVisibility(View.GONE);
								isShosrt = false;
							}
						}, 500);
						// 发送消息
						listMsg.add(new Msg(pUSERID, "语音消息", TimeRender
								.getDate(1), "OUT", android.os.Environment
								.getExternalStorageDirectory().toString()
								+ FieldsUtil.VoiceFile_S + voiceName));

						// 刷新适配器
						adapter.notifyDataSetChanged();
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								sendFile(android.os.Environment
										.getExternalStorageDirectory()
										.toString()
										+ FieldsUtil.VoiceFile_S + voiceName);
							}
						}).start();
						return false;
					}
					/*
					 * ChatMsgEntity entity = new ChatMsgEntity();
					 * entity.setDate(getDate()); entity.setName("高富帅");
					 * entity.setMsgType(false); entity.setTime(time + "\"");
					 * entity.setText(voiceName); mDataArrays.add(entity);
					 * mAdapter.notifyDataSetChanged();
					 * mListView.setSelection(mListView.getCount() - 1);
					 */
					rcChat_popup.setVisibility(View.GONE);

				}
			}
			if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
				// System.out.println("5");
				Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc2);
				img1.setVisibility(View.GONE);
				del_re.setVisibility(View.VISIBLE);
				del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
					sc_img1.startAnimation(mLitteAnimation);
					sc_img1.startAnimation(mBigAnimation);
				}
			} else {

				img1.setVisibility(View.VISIBLE);
				del_re.setVisibility(View.GONE);
				del_re.setBackgroundResource(0);
			}
		}
		return super.onTouchEvent(event);

	};

	String filepath;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 发送附件
		if (requestCode == 2 && resultCode == 2 && data != null) {

			filepath = data.getStringExtra("filepath");

			if (filepath.length() > 0) {
				System.out.println(this + "filePath====" + filepath);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						sendFile(filepath);
					}
				}).start();

			}
		}
	}

	private void sendFile(String filepath) {
		ServiceDiscoveryManager sdm = ServiceDiscoveryManager
				.getInstanceFor(XmppTool.getConnection());
		if (sdm == null)
			sdm = new ServiceDiscoveryManager(XmppTool.getConnection());
		sdm.addFeature("http://jabber.org/protocol/disco#info");
		sdm.addFeature("jabber:iq:privacy");
		final FileTransferManager fileTransferManager = new FileTransferManager(
				XmppTool.getConnection());
		FileTransferNegotiator
				.setServiceEnabled(XmppTool.getConnection(), true);
		// 发送给yinghan-pc服务器，xiaowang（获取自己的服务器，和好友）
		final OutgoingFileTransfer fileTransfer = fileTransferManager
				.createOutgoingFileTransfer(mUserInfo.getUser()
						+ FieldsUtil.Spark);

		final File file = new File(filepath);
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("1");
		try {
			fileTransfer.sendFile(file, /* file.getName(), in.available(), */
					"Sending");
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("2");
		} catch (Exception e) {
			handler.sendEmptyMessage(9);
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(500L);
						System.out.println("3");
						Status status = fileTransfer.getStatus();
						System.out.println("4");
						if ((status == FileTransfer.Status.error)
								|| (status == FileTransfer.Status.cancelled)
								|| (status == FileTransfer.Status.refused)) {
							/*
							 * System.out.println(this + "==4===" +
							 * fileTransfer.getError());
							 */
							handler.sendEmptyMessage(4);
							break;
						} else if (status == FileTransfer.Status.complete) {
							handler.sendEmptyMessage(8);
							handler.sendEmptyMessage(4);
							break;
						}

						else if (status == FileTransfer.Status.negotiating_transfer) {
							// ..

						} else if (status == FileTransfer.Status.negotiated) {
							// ..

						} else if (status == FileTransfer.Status.initial) {
							// ..

						} else if (status == FileTransfer.Status.negotiating_stream) {
							// ..

						} else if (status == FileTransfer.Status.in_progress) {
							System.out.println(this + "==2===" + status);
							// 进度条显示
							handler.sendEmptyMessage(2);

							long p = fileTransfer.getBytesSent() * 100L
									/ fileTransfer.getFileSize();

							android.os.Message message = handler
									.obtainMessage();
							message.arg1 = Math.round((float) p);
							message.what = 3;
							message.sendToTarget();

						}
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(9);

					e.printStackTrace();
				}
			}
		}).start();
	}

	private FileTransferRequest request;
	private File file;
	boolean isAutoReceiver = false;

	class RecFileTransferListener implements FileTransferListener {
		@Override
		public void fileTransferRequest(FileTransferRequest prequest) {
			// 接受附件
			/*
			 * System.out.println("The file received from: " +
			 * prequest.getRequestor());
			 */
			String mFileName = prequest.getFileName();
			isAutoReceiver = false;
			file = new File(android.os.Environment
					.getExternalStorageDirectory().toString()
					+ FieldsUtil.AccessoryFile_R + mFileName);
			request = prequest;
			for (String str : FieldsUtil.FilterStrings) {
				if (mFileName.indexOf(str) != -1) {
					// 程序的发送的多媒体附件
					isAutoReceiver = true;
					break;
				}
			}
			if (isAutoReceiver)// 自动接收
				ReceiverFile(request, file);
			else
				handler.sendEmptyMessage(5);
		}
	}

	public boolean isSendMessage = false;

	public void ReceiverFile(FileTransferRequest mRequest, File mFile) {
		isSendMessage = false;
		if (isAutoReceiver) {
			isSendMessage = isAutoReceiver;
			isAutoReceiver = false;
		}
		final IncomingFileTransfer infiletransfer = request.accept();
		try {
			infiletransfer.recieveFile(file);
		} catch (XMPPException e) {

			handler.sendEmptyMessage(7);
			e.printStackTrace();
		}

		handler.sendEmptyMessage(2);

		Timer timer = new Timer();
		TimerTask updateProgessBar = new TimerTask() {
			public void run() {
				if ((infiletransfer.getAmountWritten() >= request.getFileSize())
						|| (infiletransfer.getStatus() == FileTransfer.Status.error)
						|| (infiletransfer.getStatus() == FileTransfer.Status.refused)
						|| (infiletransfer.getStatus() == FileTransfer.Status.cancelled)
						|| (infiletransfer.getStatus() == FileTransfer.Status.complete)) {
					if (isSendMessage) {
						// 获取用户、消息、时间、IN
						String[] args = new String[] { mUserInfo.getName(),
								"点击播放", TimeRender.getDate(1), "IN", "" };

						// 在handler里取出来显示消息
						android.os.Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.obj = args;
						msg.sendToTarget();
					}
					cancel();
					handler.sendEmptyMessage(4);
				} else {
					long p = infiletransfer.getAmountWritten() * 100L
							/ infiletransfer.getFileSize();

					android.os.Message message = handler.obtainMessage();
					message.arg1 = Math.round((float) p);
					message.what = 3;
					message.sendToTarget();

				}
			}
		};
		timer.scheduleAtFixedRate(updateProgessBar, 10L, 10L);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				// 获取消息并显示
				String[] args = (String[]) msg.obj;
				listMsg.add(new Msg(args[0], args[1], args[2], args[3], args[4]));
				// 刷新适配器
				adapter.notifyDataSetChanged();
				break;
			case 2:
				// 附件进度条
				if (pb.getVisibility() == View.GONE) {
					pb.setMax(100);
					pb.setProgress(1);
					pb.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				pb.setProgress(msg.arg1);
				break;
			case 4:
				pb.setVisibility(View.GONE);
				break;
			case 5:

				// 提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FormClient.this);

				builder.setTitle("附件：")
						.setCancelable(false)
						.setMessage("是否接收文件：" + file.getName() + "?")
						.setPositiveButton("接受",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										ReceiverFile(request, file);
										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										request.reject();
										dialog.cancel();
									}
								}).show();
				break;
			case 6:// 接收成功
				Toast.makeText(FormClient.this, "接收完成!", Toast.LENGTH_SHORT)
						.show();
				break;
			case 7:// 接收失败
				Toast.makeText(FormClient.this, "接收失败!", Toast.LENGTH_SHORT)
						.show();
				break;
			case 8:// 发送成功

				Toast.makeText(FormClient.this, "发送成功!", Toast.LENGTH_SHORT)
						.show();

				break;
			case 9:// 发送失败

				Toast.makeText(FormClient.this, "发送失败!", Toast.LENGTH_SHORT)
						.show();

				break;
			default:
				break;
			}
		};
	};

	// 退出
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		/*
		 * XmppTool.closeConnection(); System.exit(0);
		 * android.os.Process.killProcess(android.os.Process .myPid());
		 */
		this.finish();
	}

	class MyAdapter extends BaseAdapter {

		private Context cxt;
		private LayoutInflater inflater;

		public MyAdapter(FormClient formClient) {
			this.cxt = formClient;
		}

		@Override
		public int getCount() {
			return listMsg.size();
		}

		@Override
		public Object getItem(int position) {
			return listMsg.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 显示消息的布局：内容、背景、用户、时间
			this.inflater = (LayoutInflater) this.cxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// IN,OUT的图片
			if (listMsg.get(position).from.equals("IN")) {
				convertView = this.inflater.inflate(
						R.layout.formclient_chat_in, null);
			} else {
				convertView = this.inflater.inflate(
						R.layout.formclient_chat_out, null);
			}

			TextView useridView = (TextView) convertView
					.findViewById(R.id.formclient_row_userid);
			TextView dateView = (TextView) convertView
					.findViewById(R.id.formclient_row_date);
			TextView msgView = (TextView) convertView
					.findViewById(R.id.formclient_row_msg);

			useridView.setText(listMsg.get(position).userid);
			dateView.setText(listMsg.get(position).date);
			msgView.setText(listMsg.get(position).msg);

			return convertView;
		}
	}

	private static final int POLL_INTERVAL = 300;
	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			System.out.println("amp=============" + amp);
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {
		System.out.println("updatadisplay==============" + signalEMA);
		switch ((int) (signalEMA)) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
			volume.setImageResource(R.drawable.amp2);
			break;
		case 3:
			volume.setImageResource(R.drawable.amp3);

			break;
		case 4:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 5:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 6:
			volume.setImageResource(R.drawable.amp6);
			break;
		case 7:

		case 8:

		case 9:

		case 10:

		case 11:

		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	public void chat_back() {
		formClient.onDestroy();
		startActivity(new Intent(formClient, OnLineUserActivity.class));
	}

}