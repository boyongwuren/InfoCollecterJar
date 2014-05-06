package com.starnet.interfaces;


/**
 * 加载服务端数据返回的结果
 * @author zmp
 *
 */
public interface LoadApiBackInterface
{
	/**
	 * 加载数据返回
	 * @param jsonString 返回的数据
	 */
   public void loadApiBackString(String jsonString);
}
