package com.codinghavoc.structs.arrays;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.IntegerNode;

@XmlRootElement(name="integerArray")
@XmlAccessorType(XmlAccessType.FIELD)

public class IntegerArray extends BaseNode{
	@XmlElement
	private ArrayList<Integer> nValue;
	
	public IntegerArray(){}
	
	public IntegerArray(String k, Integer v){
		nValue = new ArrayList<Integer>();
		nValue.add(v);
		nKey = k;
	}
	
	public IntegerArray(String k, ArrayList<Integer> v){
		nValue = v;
		nKey = k;
	}
	
	public IntegerArray(IntegerNode in){
		nKey = in.getnKey();
		nValue = new ArrayList<Integer>();
		nValue.add(in.getnValue());
	}
	
	public IntegerArray(IntegerArray ia){
		nValue = ia.getnValue();
		nKey = ia.getnKey();
	}
	
	public ArrayList<Integer> getnValue(){
		return nValue;
	}
	
	public void setnValue(ArrayList<Integer> v){
		nValue = v;
	}
	
	public boolean add(Integer v){
		if(nValue.contains(v)){
			return false;
		} else {
			nValue.add(v);
			return true;
		}
	}
	
	public boolean remove(Integer v){
		if(nValue.contains(v)){
			nValue.remove(v);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString(){
		String result = nValue.toString();
		StringBuilder sb = new StringBuilder();
		sb.append(nKey + ": ");
		sb.append(result.substring(1, result.length()-1));
		return sb.toString();
	}
}
