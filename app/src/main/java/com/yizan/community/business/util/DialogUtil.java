package com.yizan.community.business.util;



import com.yizan.community.business.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogUtil {

	
	/**
	 * 提示框
	 */
	
	private static AlertDialog customDialog = null;
	
	/**
	 * 单按钮提示框
	 * @param context
	 * @param message
	 * @param buttonText
	 * @param isCancel
	 * @param dialogListener
	 */
	public static synchronized void onlyDialog(Context context, String message, String buttonText, boolean isCancel,final OnonlyDialogListener dialogListener){
		if (customDialog != null && customDialog.isShowing())
		{
			return;
		}
		
		customDialog = new AlertDialog.Builder(context).create();
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		customDialog.show();
		
		customDialog.getWindow().setContentView(R.layout.only_dialog_layout);
		customDialog.setCancelable(isCancel);
		View view = customDialog.getWindow().getDecorView();
		TextView messageTextView = (TextView) view.findViewById(R.id.messageTextView);
		Button btn_onlyButton = (Button) view.findViewById(R.id.btn_only_cancel);
		if (null != message)
			messageTextView.setText(message);
		if (buttonText == null || buttonText.equals(""))
		{
			btn_onlyButton.setText(context.getString(R.string.sure));
		} else
		{
			btn_onlyButton.setText(buttonText);
		}
		
		//点击确定
				btn_onlyButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (customDialog != null)
						{
							customDialog.dismiss();
							if (null != dialogListener)
								dialogListener.ononlyClick();
						}
					}
				});
	}
	
	//单按钮事件监听
	public interface OnonlyDialogListener
	{
		public void ononlyClick();
	}
}
