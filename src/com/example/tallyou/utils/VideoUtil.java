package com.example.tallyou.utils;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.view.SurfaceHolder;

public class VideoUtil implements OnCompletionListener {
	private File dir;
	private String VideoPath;

	public String getVideoPath() {
		return VideoPath;
	}

	public void setVideoPath(String videoPath) {
		VideoPath = videoPath;
	}

	private MediaRecorder recorder = null;
	private File myRecAudioFile;
	private boolean isVideo = false;
	private Camera mCamera;
	private MediaPlayer mediaPlayer;
	File defaultDir = Environment.getExternalStorageDirectory();
	String path = defaultDir.getAbsolutePath() + File.separator
			+ new File(FieldsUtil.VideoFile_S).getAbsolutePath()
			+ File.separator;

	public boolean getisVideo() {
		return isVideo;
	}

	@SuppressLint("NewApi")
	public void recorder(SurfaceHolder mSurfaceHolder, android.hardware.Camera c) {
		this.mCamera = c;
		try {
			if (recorder == null) {
				recorder = new MediaRecorder();
			}
			dir = new File(path);
			if (!dir.exists())
				dir.mkdir();
			myRecAudioFile = File.createTempFile("video", ".3gp", dir);// 创建临时文件
			setVideoPath(myRecAudioFile.getAbsolutePath().toString());
			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// 预览
			recorder.setCamera(c);
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 输出格式为3gp
			recorder.setVideoSize(FieldsUtil.VideoWidth, FieldsUtil.VideoHeight);// 视频尺寸
			//recorder.setOrientationHint(90);
			recorder.setVideoFrameRate(20);// 视频帧频率
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);// 视频编码
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频编码
			// recorder.setMaxDuration(10000);// 最大期限
			recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// 保存路径

			recorder.prepare();
			recorder.start();
			isVideo = true;
		} catch (IOException e) {
			isVideo = false;
			e.printStackTrace();
		}
	}

	public File getMyRecAudioFile() {
		return myRecAudioFile;
	}

	public void recorderStop() {
		try {
			if (recorder != null) {
				recorder.stop();
				recorder.reset();
				recorder.release();
				recorder = null;
				isVideo = false;
				System.out.println("=============");
			}

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

	public MediaPlayer mediaPlayerStart(SurfaceHolder mSurfaceHolder,
			String mediaPath) {
		try {
			if(mediaPlayer == null)
				mediaPlayer = new MediaPlayer();
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDisplay(mSurfaceHolder);
			mediaPlayer.setDataSource(mediaPath);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(final MediaPlayer mp) {

				}
			});
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			System.out.println("wrong");
		}
		return mediaPlayer;
	}

	public void meidaPlayerStop() {
		if (mediaPlayer.isPlaying()) {

			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	/*
	 * 监听视频是否播放完毕 (non-Javadoc)
	 * 
	 * @see
	 * android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media
	 * .MediaPlayer)
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		meidaPlayerStop();
	}
}
