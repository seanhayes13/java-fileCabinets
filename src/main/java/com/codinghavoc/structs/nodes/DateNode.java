package com.codinghavoc.structs.nodes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.codinghavoc.enums.Months;

@XmlRootElement(name="dateNode")
@XmlAccessorType(XmlAccessType.FIELD)

public class DateNode extends BaseNode{
	private String type;
	private int year;
	private Months month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	
	public DateNode(){}
	
	//Date
	public DateNode(String t, int y, Months m, int d){
		type = t;
		year = y;
		month = m;
		day = d;
	}
	
	//Time
	public DateNode(String t, int h, int m, int s){
		type = t;
		hour = h;
		minute = m;
		second = s;
	}
	
	//DateTime
	public DateNode(String t, int y, Months mo, int d, int h, int mi, int s){
		type = t;
		year = y;
		month = mo;
		day = d;
		hour = h;
		minute = mi;
		second = s;
	}
	
	public String toString(){
		if(type.equals("date")){
			return day + " "+ month.getLongName() + " year";
		} else if(type.equals("time")){
			return hour+":"+minute+":"+second;
		} else {
			return hour+":"+minute+":"+second +" "+ day + " "+ month.getLongName() + " year";
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}
}
