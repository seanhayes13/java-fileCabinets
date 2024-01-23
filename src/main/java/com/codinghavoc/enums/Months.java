package com.codinghavoc.enums;

import java.util.ArrayList;

public enum Months {
	
	JANUARY("January","Jan",1),FEBRUARY("February","Feb",2),MARCH("March","Mar",3),
	APRIL("April","Apr",4),MAY("May","May",5),JUNE("June","Jun",6),
	JULY("July","Jul",7),AUGUST("August","Aug",8),SEPTEMBER("September","Sept",9),
	OCTOBER("October","Oct",10),NOVEMBER("November","Nov",11),DECEMBER("December","Dec",12);
	
	private String longName;
	private String shortName;
	private int numerical;
	
	Months(String l, String s, int n){
		longName = l;
		shortName = s;
		numerical = n;
	}
	
	public ArrayList<String> getShortNames(){
		ArrayList<String> result = new ArrayList<String>();
		for(Months m : Months.values()){
			result.add(m.shortName);
		}
		return result;
	}
	
	public ArrayList<String> getLongNames(){
		ArrayList<String> result = new ArrayList<String>();
		for(Months m : Months.values()){
			result.add(m.longName);
		}
		return result;
	}

	public String getLongName() {
		return longName;
	}

	public String getShortName() {
		return shortName;
	}

	public int getNumerical() {
		return numerical;
	}
}
