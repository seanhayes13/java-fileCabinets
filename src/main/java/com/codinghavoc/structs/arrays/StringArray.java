package com.codinghavoc.structs.arrays;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.StringNode;

@XmlRootElement(name="stringArray")
@XmlAccessorType(XmlAccessType.FIELD)

public class StringArray extends BaseNode{
	@XmlElement
	private ArrayList<String> nValue;
	
	public StringArray(){}
	
	public StringArray(String k, String v){
		nValue = new ArrayList<String>();
		nValue.add(v);
		nKey = k;
	}
	
	public StringArray(String k, ArrayList<String> v){
		nValue = v;
		nKey = k;
	}
	
	public StringArray(StringNode sn){
		nKey = sn.getnKey();
		nValue = new ArrayList<String>();
		nValue.add(sn.getnValue());
	}
	
	public StringArray(StringArray sa){
		nValue = sa.getnValue();
		nKey = sa.getnKey();
	}
	
	public ArrayList<String> getnValue(){
		return nValue;
	}
	
	public void setnValue(ArrayList<String> v){
		nValue = v;
	}
	
	public boolean add(String v){
		if(nValue.contains(v)){
			return false;
		} else {
			nValue.add(v);
			return true;
		}
	}
	
	public boolean remove(String v){
		if(nValue.contains(v)){
			nValue.remove(v);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString(){
		return nKey+": " + String.join(", ", nValue);
	}
}
