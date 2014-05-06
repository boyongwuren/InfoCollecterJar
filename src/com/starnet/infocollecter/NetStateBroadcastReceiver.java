package com.starnet.infocollecter;

import com.starnet.interfaces.ConnectNetInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 监听网络的状态变化
 * @author zmp
 *
 */
public class NetStateBroadcastReceiver extends BroadcastReceiver
{
	private ConnectNetInterface connectNetInterface;
	
	public NetStateBroadcastReceiver(ConnectNetInterface connectNetInterface)
	{
		this.connectNetInterface = connectNetInterface;
	}
	
	

	@Override
	public void onReceive(Context arg0, Intent arg1) 
	{
		ConnectivityManager connectMgr = (ConnectivityManager) arg0.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) 
		{ 
			//连上网络了
			this.connectNetInterface.connectNet();
			this.connectNetInterface = null;
			arg0.unregisterReceiver(this);
			System.out.println("移除了网络监听？？？");
		} 

	}

}
