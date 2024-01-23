package com.codinghavoc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//
public class UserInput {
	public static boolean confirmChange(){
		System.out.println("##############################\nWARNING: This action is final and cannot be undone."
				+ "\nType CONFIRM to proceed\n##############################");
		if(getString().equals("CONFIRM")){
			return true;
		} else {
			return false;
		}
	}
	
	public static int convertOrChange(){
		String input = "";
		while(!input.equals("CONVERT") || !input.equals("CHANGE") || !input.equals("QUIT")){
			System.out.println("A Node with that key already exists.\nChoose an option from below"
					+ "- CONVERT - the existing Node into an Array\n"
					+ "- CHANGE - the value of the existing Node\n"
					+ "- QUIT - to cancel this operation");
		}
		if(input.equalsIgnoreCase("convert")) return 1;
		else if(input.equalsIgnoreCase("change")) return 0;
		else return -1;
	}

	public static int getInt(){
		int result=0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			result = Integer.parseInt(br.readLine());
		} catch (IOException e) {
			System.out.println("IOException Error");
		}
		return result;
	}
	
	public static Integer getInteger(){
		Integer result = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			result = Integer.parseInt(br.readLine());
		} catch (IOException e) {
			System.out.println("IOException Error");
		}
		return result;
	}
	
	//This method will 
	public static String getString(){
		String result="";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			result = br.readLine();
		} catch (IOException e) {
			System.out.println("IOException Error");
		}
		return result;
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/