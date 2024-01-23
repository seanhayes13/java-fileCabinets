package com.codinghavoc.structs.nodes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="doubleNode")
@XmlAccessorType(XmlAccessType.FIELD)

public class DoubleNode extends BaseNode{
	@XmlElement
	private Double nValue;
	
	public DoubleNode(){}
	public DoubleNode(String k, Double v){
		nKey = k;
		nValue = v;
	}

	public Double getnValue() {
		return nValue;
	}

	public void setnValue(Double nValue) {
		this.nValue = nValue;
	}

	@Override
	public String toString(){
		return nKey +": "+nValue;
	}

}
