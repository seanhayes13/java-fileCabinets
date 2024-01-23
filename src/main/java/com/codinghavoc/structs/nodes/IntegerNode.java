package com.codinghavoc.structs.nodes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="integerNode")
@XmlAccessorType(XmlAccessType.FIELD)

public class IntegerNode extends BaseNode{
	@XmlElement
	private Integer nValue;
	
	public IntegerNode(){}
	public IntegerNode(String k, Integer v){
		nKey = k;
		nValue = v;
	}

	public Integer getnValue() {
		return nValue;
	}

	public void setnValue(Integer nValue) {
		this.nValue = nValue;
	}

	@Override
	public String toString(){
		return nKey +": "+nValue;
	}
}
