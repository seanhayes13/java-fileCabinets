package com.codinghavoc.structs.nodes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="stringNode")
@XmlAccessorType(XmlAccessType.FIELD)

public class StringNode extends BaseNode implements Comparable<StringNode>{
	@XmlElement
	private String nValue;
	
	public StringNode(){}
	public StringNode(String k, String v){
		nKey = k;
		nValue = v;
	}

	public String getnValue() {
		return nValue;
	}

	public void setnValue(String nValue) {
		this.nValue = nValue;
	}

	@Override
	public String toString(){
		return nKey +": "+nValue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nValue == null) ? 0 : nValue.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StringNode other = (StringNode) obj;
		if (nValue == null) {
			if (other.nValue != null) {
				return false;
			}
		} else if (!nValue.equals(other.nValue)) {
			return false;
		}
		return true;
	}
	@Override
	public int compareTo(StringNode sn) {
		String s1 = this.getClass().getSimpleName();
		String s2 = sn.getClass().getSimpleName();
		return s1.compareTo(s2);
	}

}
