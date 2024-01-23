package com.codinghavoc.structs.arrays;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.DoubleNode;

@XmlRootElement(name="doubleArray")
@XmlAccessorType(XmlAccessType.FIELD)

public class DoubleArray extends BaseNode{
	@XmlElement
	private ArrayList<Double> nValue;
	
	public DoubleArray(){}
	
	public DoubleArray(String k, Double v){
		nValue = new ArrayList<Double>();
		nValue.add(v);
		nKey = k;
	}
	
	public DoubleArray(String k, ArrayList<Double> v){
		nValue = v;
		nKey = k;
	}
	
	public DoubleArray(DoubleNode dn){
		nKey = dn.getnKey();
		nValue = new ArrayList<Double>();
		nValue.add(dn.getnValue());
	}
	
	public DoubleArray(DoubleArray da){
		nValue = da.getnValue();
		nKey = da.getnKey();
	}
	
	public ArrayList<Double> getnValue(){
		return nValue;
	}
	
	public void setnValue(ArrayList<Double> v){
		nValue = v;
	}
	
	public boolean add(Double v){
		if(nValue.contains(v)){
			return false;
		} else {
			nValue.add(v);
			return true;
		}
	}
	
	public boolean remove(Double v){
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