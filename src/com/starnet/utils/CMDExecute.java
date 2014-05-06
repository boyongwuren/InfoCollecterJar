package com.starnet.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 执行操作系统cmd命令
 * @author zmp
 *
 */
public class CMDExecute 
{


	public synchronized String run(String[]cmd,String workdirectory)
	{
		String result = "";
		try 
		{
            ProcessBuilder builder = new ProcessBuilder(cmd);
            if(workdirectory != null)
            {
            	builder.directory(new File(workdirectory));
            	builder.redirectErrorStream(true);
            	Process process = builder.start();
            	InputStream inputStream = process.getInputStream();
            	BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf8")); 
            	String row;
        	    while((row = br.readLine())!=null)
        	    {
        	      result += row;
        	    }
        	    inputStream.close();
        	    br.close();
            }
		} catch (Exception e)
		{
			result = cmd.toString()+" not get";
		}
		return result;
	}
	

}
