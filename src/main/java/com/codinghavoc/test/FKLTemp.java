package com.codinghavoc.test;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fklNodeTemp")
@XmlAccessorType(XmlAccessType.FIELD)
public class FKLTemp extends NodeTemp{
	
	@XmlElementWrapper(name="keyList")
	@XmlElement(name="keys")
	private ArrayList<String> keyList;
	
	public FKLTemp(){}
	
	public FKLTemp(String t, String s, int i){
		nKey = t;
		keyList = new ArrayList<String>();
		keyList.add(s +":"+i);
	}
	public boolean addLink(String s, int i){
		String tgt = s+":"+i;
		if(keyList.contains(tgt)){
			return false;
		} else {
			keyList.add(tgt);
			return true;
		}
	}
	
	public ArrayList<String> getKeyList() {
		return keyList;
	}
	public void setKeyList(ArrayList<String> keyList) {
		this.keyList = keyList;
	}
	@Override
	public String toString(){
		return keyList.toString();
	}
}
