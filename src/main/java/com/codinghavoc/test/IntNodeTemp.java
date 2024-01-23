package com.codinghavoc.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="intNotTemp")
@XmlAccessorType(XmlAccessType.FIELD)
public class IntNodeTemp extends NodeTemp{
	@XmlElement
	private Integer nValue;
	
	public IntNodeTemp(){}
	public IntNodeTemp(String s, Integer i){
		nKey = s;
		nValue = i;
	}
	
	public Integer getnValue() {
		return nValue;
	}

	public void setnValue(Integer nValue) {
		this.nValue = nValue;
	}

	public String toString(){
		return nKey +": "+nValue;
	}
}
