package com.starnet.utils.net;

import com.starnet.constant.HandlerConst;
import com.starnet.interfaces.LoadApiBackInterface;
import android.os.Handler;
import android.os.Message;

/**
 * 工具类，用作加载服务端接口的统一入口
 * @author zmp
 */
public class LoadServerAPIHelp 
{
 
	/**
	 * 调用这个函数
	 * 可以启动线程加载服务端对应的接口
	 * @param serverApiUrl
	 * @param loadApiBackInterface 加载完成的回调
	 */
	public static void loadServerApi(String serverApiUrl,LoadApiBackInterface loadApiBackInterface,String paramString)
	{
		new LoadServerThread(serverApiUrl, new LoadDataHandler(loadApiBackInterface),paramString).start();
	}
	
	
	/**
	 * 处理加载服务端接口完成的操作
	 * @author zmp
	 *
	 */
	private static class LoadDataHandler extends Handler
	{
		private LoadApiBackInterface loadApiBackInterface;
		
		
		public LoadDataHandler(LoadApiBackInterface loadApiBackInterface)
		{
			this.loadApiBackInterface = loadApiBackInterface;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) 
			{
				case HandlerConst.LOAD_OK:
					if(loadApiBackInterface != null)
					{
						loadApiBackInterface.loadApiBackString(msg.getData().getString(HandlerConst.LOAD_FILE_KEY));
					}
					break;
					
				case HandlerConst.LOAD_FALL:
					break;
					
				default:
					break;
			}
		}
	}
	
	
	

}
