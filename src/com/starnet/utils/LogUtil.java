package com.starnet.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

 
public class LogUtil {
 
	
 
	public static void showServerBackInfo(String info,Context context)
	{
		 AlertDialog alertDialog = new AlertDialog.Builder(context).
					setMessage(info).
					setTitle("加载数据返回").
					setPositiveButton("确定", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
					}).create();
			alertDialog.show();
	}
	
	 
	public static void showTip(String text,Context context)
	{
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	
	
	
}
