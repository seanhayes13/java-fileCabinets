package com.codinghavoc.structs.nodes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.codinghavoc.structs.arrays.DoubleArray;
import com.codinghavoc.structs.arrays.IntegerArray;
import com.codinghavoc.structs.arrays.StringArray;

@XmlTransient
@XmlSeeAlso({IntegerNode.class,StringNode.class,BooleanNode.class,
	FKLNode.class,DoubleNode.class, StringArray.class, IntegerArray.class,
	DoubleArray.class})
@XmlAccessorType(XmlAccessType.FIELD)

public abstract class BaseNode {
	@XmlElement
	protected String nKey;
	public String getnKey() {
		return nKey;
	}

	public Object getnValue(){
		return null;
	}
	
	public void setnKey(String n) {
		nKey = n;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nKey == null) ? 0 : nKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseNode other = (BaseNode) obj;
		if (nKey == null) {
			if (other.nKey != null) {
				return false;
			}
		} else if (!nKey.equals(other.nKey)) {
			return false;
		}
		return true;
	}
}
