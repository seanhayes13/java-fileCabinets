package com.codinghavoc.structs.nodes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="boolNode")
@XmlAccessorType(XmlAccessType.FIELD)

public class BooleanNode  extends BaseNode{
	@XmlElement
	private boolean nValue;
	
	public BooleanNode(){}
	
	public BooleanNode(String k, boolean v){
		nKey = k;
		nValue = v;
	}

	@Override
	public String getnValue(){
		if(nValue){
			return "true";
		} else {
			return "false";
		}
	}

	public boolean isnValue() {
		return nValue;
	}
	
	public void setnValue(boolean nValue) {
		this.nValue = nValue;
	}
	
	@Override
	public String toString(){
		if(nValue){
			return nKey+": true";
		} else {
			return nKey+": false";
		}
	}
}
