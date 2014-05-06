package com.starnet.utils;

import java.util.ArrayList;


import com.starnet.constant.WebJsonKey;
import com.starnet.vo.ContactVo;

public class JsonTool {

	/**
	 * 插入字符串
	 * @param json
	 * @param key
	 * @param value
	 */
	public static void appendJsonItem(StringBuffer infoStr,String key,String value)
	{
		infoStr.append("\"");
		infoStr.append(key);
		infoStr.append("\"");
		infoStr.append(":");
		infoStr.append("\"");
		infoStr.append(value);
		infoStr.append("\"");
		infoStr.append(",");
	}

	/**插入数值
	 * @param json
	 * @param key
	 * @param value
	 */
	public static void appendJsonItem(StringBuffer infoStr,String key,int value)
	{
		infoStr.append("\"");
		infoStr.append(key);
		infoStr.append("\"");
		infoStr.append(":");
		infoStr.append(value);
		infoStr.append(",");
	}

	/**插入布尔值
	 * @param json
	 * @param key
	 * @param value
	 */
	public static void appendJsonItem(StringBuffer infoStr,String key,Boolean value)
	{
		infoStr.append("\"");
		infoStr.append(key);
		infoStr.append("\"");
		infoStr.append(":");
		if(value == true)
		{
			infoStr.append(1);
		}else {
			infoStr.append(0);
		}
		infoStr.append(",");
	}

	/**插入布尔值
	 * @param json
	 * @param key
	 * @param value
	 */
	public static void appendJsonItem(StringBuffer infoStr,String key,ArrayList<ContactVo> vos)
	{
		infoStr.append("\"");
		infoStr.append(key);
		infoStr.append("\"");
		infoStr.append(":");
		
		infoStr.append("[");
		
		int len = vos.size();
		ContactVo vo;
		for (int i = 0; i < len; i++) 
		{
			vo = vos.get(i);
			infoStr.append("{");
			appendJsonItem(infoStr, WebJsonKey.phoneID, vo.phoneId+"");
			appendJsonItem(infoStr, WebJsonKey.phoneName, vo.phoneName+"");
			appendJsonItem(infoStr, WebJsonKey.phoneNumber, vo.phoneNum+"");
			infoStr.deleteCharAt(infoStr.length()-1);
			infoStr.append("}");
			infoStr.append(",");
		}

		infoStr.deleteCharAt(infoStr.length()-1);

		infoStr.append("]");
		
		infoStr.append(",");
	}

}
