package com.codinghavoc.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlSeeAlso({IntNodeTemp.class,FKLTemp.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class NodeTemp {
	@XmlElement
	protected String nKey;

	public String getnKey() {
		return nKey;
	}

	public void setnKey(String n) {
		nKey = n;
	}

}
