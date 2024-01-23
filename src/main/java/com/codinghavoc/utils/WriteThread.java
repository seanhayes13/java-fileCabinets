package com.codinghavoc.utils;

import com.codinghavoc.structs.Drawer;

public class WriteThread {
	private static String fcName;
	
	public static void execute(String s, Drawer d){
		final Drawer drawer = d;
		class Write implements Runnable{
			
			@Override
			public void run() {
				//System.out.println("Running: " + drawer.getDrawerName());
				LogTracker.updateLog(s,"Saving: " + d.getDrawerName());
				ReadWrite.writeToXml(fcName, drawer);
				LogTracker.updateLog(s, "Finished saving: " + d.getDrawerName());
				//System.out.println("Finished: " + drawer.getDrawerName());
			}
			
//			Write(Drawer d){
//				drawer = d;
//			}
		}
		Thread t;
		fcName = s;
		t = new Thread(new Write());
		//System.out.println("Starting: " + d.getDrawerName());
		LogTracker.updateLog(s, "Starting save: " + d.getDrawerName());
		t.start();
	}
}
