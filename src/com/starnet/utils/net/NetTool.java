package com.starnet.utils.net;

import com.starnet.infocollecter.NetStateBroadcastReceiver;
import com.starnet.interfaces.ConnectNetInterface;
import com.starnet.interfaces.LoadApiBackInterface;
import com.starnet.utils.LogUtil;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关辅助类
 * @author zmp
 *
 */
public class NetTool
{
	
	/**
	 * 上传数据
	 */
	public static void uploadData(String address,String data,Context context)
	{
		LoadDataBack loadDataBack = new LoadDataBack(context);
		ConnectNetBack connectNetBack = new ConnectNetBack(address,loadDataBack,data);
		
		if(isNetworkConnected(context))
		{
			//网络可用，上传数据
			LoadServerAPIHelp.loadServerApi(address, loadDataBack, data);
		}else 
		{
			//监听网络状态
			System.out.print("监听网络状态");
			NetStateBroadcastReceiver connectionReceiver = new NetStateBroadcastReceiver(connectNetBack);
			IntentFilter intentFilter = new IntentFilter(); 
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 
			context.registerReceiver(connectionReceiver, intentFilter);
		}
	}
	
	
	private static class LoadDataBack implements LoadApiBackInterface
	{
		private Context context;
		
		public LoadDataBack(Context context)
		{
			this.context = context;
		}
		
		@Override
		public void loadApiBackString(String jsonString) 
		{
//			LogUtil.showServerBackInfo(jsonString, context);
		}
	}
	
	private static class ConnectNetBack implements ConnectNetInterface
	{
		private String address;
		private LoadApiBackInterface loadApiBackInterface;
		private String data;
		public ConnectNetBack(String address,LoadApiBackInterface loadApiBackInterface,String data)
		{
			this.address = address;
			this.loadApiBackInterface = loadApiBackInterface;
			this.data = data;
		}
		
		@Override
		public void connectNet() 
		{
			//网络可用，上传数据
			System.out.println("监听到有可用的网络了");
			LoadServerAPIHelp.loadServerApi(address, loadApiBackInterface, data);
		}
	}
	
	
	/**
	 * 判断是否有网络连接
	 * @param context
	 * @return
	 */
	private static boolean isNetworkConnected(Context context) 
	{ 
		if (context != null)
		{ 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
			if (mNetworkInfo != null) 
			{ 
				return mNetworkInfo.isAvailable(); 
			} 
		} 
		
		return false; 
	}
	
	
	
}
