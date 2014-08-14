package com.example.tallyou.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tallyou.R;

/**
 * @author zhangpengzhan
 * 
 *         2014年3月7日 上午10:26:04
 */
public class RegistAlert extends Dialog {
	private LayoutInflater mLayoutInflater;

	protected RegistAlert(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mLayoutInflater = getLayoutInflater();
		View layout = mLayoutInflater.inflate(R.layout.activity_main_regist,
				null);
		// RegistAlert.this.b
	}

	protected RegistAlert(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public static class Builder {
		private Context mContext;
		private String mTitle;
		private String mMessage;
		private String mPositiveButtonText;
		private String mNegativeButtonText;
		private View mContentView;
		private DialogInterface.OnClickListener mPositiveButtonOnClickListener,
				mNegativeButtonOnClickListener;
		private OnClickListener mNegativeButtOnClickListener;

		public Builder(Context context) {
			this.mContext = context;
		}

		/**
		 * 设置对话框的显示的消息内容
		 * 
		 * @param message
		 * @return
		 */
		public Builder setMessage(String message) {
			this.mMessage = message;
			return this;
		}

		/**
		 * 设置对话框的消息从资源文件当中
		 * 
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message) {
			this.mMessage = (String) mContext.getText(message);
			return this;
		}

		/**
		 * 设置对话框的标题从资源文件中
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.mTitle = (String) mContext.getText(title);
			return this;
		}

		/**
		 * 设置对话框的标题
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		/**
		 * 设置自定义布局
		 * 
		 * @param view
		 * @return
		 */
		public Builder setContentView(View view) {
			this.mContentView = view;
			return this;
		}

		/**
		 * 设置确定按钮的文字和监听
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.mPositiveButtonText = (String) mContext
					.getText(positiveButtonText);
			this.mPositiveButtonOnClickListener = listener;
			return this;
		}

		/**
		 * 设置确定按钮的文字和监听
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.mPositiveButtonText = positiveButtonText;
			this.mPositiveButtonOnClickListener = listener;
			return this;
		}

		/**
		 * 设置取消按钮的文字和监听
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.mNegativeButtonText = (String) mContext
					.getText(negativeButtonText);
			this.mNegativeButtOnClickListener = listener;
			return this;
		}

		/**
		 * 设置取消按钮的文字和监听
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.mNegativeButtonText = negativeButtonText;
			this.mNegativeButtOnClickListener = listener;
			return this;
		}

		public RegistAlert create(View mLayout) {
			
			// 设置对话框的theme
			final RegistAlert mDialog = new RegistAlert(mContext,
					R.style.Dialog);
			mDialog.addContentView(mLayout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// 设置对话框的标题
			TextView mTitle1 = (TextView) mLayout
					.findViewById(R.id.regist_textView1);
			mTitle1.setText(mTitle);
			// 设置确定按钮
			if (mPositiveButtonText != null) {
				Button pButton = (Button) mLayout
						.findViewById(R.id.regist_button1);
				pButton.setText(mPositiveButtonText);
				if (mPositiveButtonOnClickListener != null) {
					pButton.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							mPositiveButtonOnClickListener.onClick(mDialog,
									BUTTON_POSITIVE);
						}

					});
				}

			} else {
				Button pButton = (Button) mLayout
						.findViewById(R.id.regist_button1);
				pButton.setVisibility(View.GONE);
			}
			// 设置取消按钮
			if (mNegativeButtonText != null) {
				Button pButton = (Button) mLayout
						.findViewById(R.id.regist_button2);
				pButton.setText(mNegativeButtonText);
				if (mNegativeButtOnClickListener != null) {
					pButton.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							mNegativeButtOnClickListener.onClick(mDialog,
									BUTTON_NEGATIVE);
						}

					});
				}

			} else {
				Button pButton = (Button) mLayout
						.findViewById(R.id.regist_button2);
				pButton.setVisibility(View.GONE);
			}

			// 设置对话框的内容消息
			if (mMessage != null) {
				TextView mContent = (TextView) mLayout
						.findViewById(R.id.regist_textView2);
				mContent.setText(mMessage);
			} else if (mContentView != null) {
				LinearLayout mLinearLayout = (LinearLayout) mLayout
						.findViewById(R.id.regist_linearlayout1);
				mLinearLayout.removeAllViews();
				mLinearLayout.addView(mContentView, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			mDialog.setContentView(mLayout);
			return mDialog;

		}
	}

}
