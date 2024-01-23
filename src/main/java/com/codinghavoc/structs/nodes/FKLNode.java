package com.codinghavoc.structs.nodes;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.codinghavoc.utils.ForeignLink;

@XmlRootElement(name="fklNode")
@XmlAccessorType(XmlAccessType.FIELD)

public class FKLNode extends BaseNode{
	
	@XmlElementWrapper(name="keyList")
	@XmlElement(name="keys")
	private ArrayList<ForeignLink> keyList;
	
	public FKLNode(){}
	
	public FKLNode(String k, String d, String f, int p){
		nKey = k;
		if(keyList==null){
			keyList = new ArrayList<>();
		}
		ForeignLink fl = new ForeignLink(d,f,p);
		keyList.add(fl);
		
	}
	
	public FKLNode(String k, ForeignLink fl){
		nKey = k;
		if(keyList==null){
			keyList = new ArrayList<>();
		}
		keyList.add(fl);
	}
	
	public boolean addLink(ForeignLink fl){
		if(keyList.contains(fl)){
			return false;
		} else {
			keyList.add(fl);
			return true;
		}
	}
	
	public boolean check(ForeignLink fl){
		if(keyList==null){
			return false;
		} else {
			for(ForeignLink flIter : keyList){
				if(flIter.equals(fl)){
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean check(String s, String f, int i){
		if(keyList==null){
			return false;
		} else {
			String tgt = s+":"+f+":"+i;
			if(keyList.contains(tgt)){
				return true;
			} else {
				return false;
			}
		}
	}
	
	public ArrayList<ForeignLink> getKeyList() {
		return keyList;
	}
	
	public void removeLink(ForeignLink fl){
		System.out.println("before: "+keyList.size());
		//System.out.println(this.toString());
		ForeignLink rem = null;
		for(ForeignLink fIter : keyList){
			if(fIter.equals(fl)){
				rem = fIter;
				break;
			}
		}
		if(rem != null){
			keyList.remove(rem);
		}
		//keyList.remove(fl);
		System.out.println("after: "+keyList.size());
		if(keyList.size()==0){
			keyList = null;
		}
	}
	
	public void setKeyList(ArrayList<ForeignLink> keyList) {
		this.keyList = keyList;
	}
	
	@Override
	public String toString(){
		ArrayList<String> temp = new ArrayList<>();
		for(ForeignLink key : keyList){
			temp.add(key.toString());
		}
		String result = String.join(", ", temp);
		return nKey +": "+result;
	}
}
