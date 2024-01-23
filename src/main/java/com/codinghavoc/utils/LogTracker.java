package com.codinghavoc.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogTracker {
	
	private static String getTimeStamp(){
		String test = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss").format(Calendar.getInstance().getTime());
		return test;
	}
	
	public static void updateLog(String fcName, String message){
		File logFile = new File("fcRepo\\"+fcName+"\\actionLog.txt");
		String output;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try{
			fw = new FileWriter(logFile.getAbsolutePath(),true);
			bw = new BufferedWriter(fw);
			output = getTimeStamp() + "::\""+message+"\"";
			bw.write(output+"\n");
		} catch (IOException e){
			
		} finally{
			try{
				if(bw != null){
					bw.close();
				}
			} catch (IOException e){
				
			}
		}
	}
	
	public static void initLog(String fcName){
		updateLog(fcName, "############# "
				+ "Initiating FileCabinet " + fcName
				+ "#############");
	}
	
	public static void sessionStart(String fcName){
		updateLog(fcName,"############# "
				+ "Session opened "
				+ "#############");
	}
	
	public static void sessionClose(String fcName){
		updateLog(fcName,"############# "
				+ "Session closed "
				+ "#############");
	}
}
