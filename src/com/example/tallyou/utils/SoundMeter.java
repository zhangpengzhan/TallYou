package com.example.tallyou.utils;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	private String mVoicePath = null;

	public String getmVoicePath() {
		return mVoicePath;
	}

	public void setmVoicePath(String mVoicePath) {
		this.mVoicePath = mVoicePath;
	}

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (!new File(android.os.Environment.getExternalStorageDirectory()
				+ FieldsUtil.VoiceFile_S).exists())
			new File(android.os.Environment.getExternalStorageDirectory()
					+ FieldsUtil.VoiceFile_S).mkdir();

		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(android.os.Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ FieldsUtil.VoiceFile_S + File.separator + name);
			setmVoicePath(android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ FieldsUtil.VoiceFile_S
					+ File.separator + name);
			try {
				mRecorder.prepare();
				mRecorder.start();

				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			try {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			} catch (IllegalStateException e) {
				System.out.println("illegalstateexception========" + e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null) {

			return (mRecorder.getMaxAmplitude() / 100);
		} else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
