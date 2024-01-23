package com.codinghavoc.test;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="pageTemp")
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTemp {
	@XmlElementWrapper(name="nodes")
	@XmlElement(name="node")
	private ArrayList<NodeTemp> nodes;
	
	@XmlAttribute
	private int id;
	
	public PageTemp(){
	}

	public PageTemp(int i) {
		id = i;
		nodes = new ArrayList<NodeTemp>();
	}
	
	public void addNode(NodeTemp n){
		nodes.add(n);
	}
	
	public int getId() {
		return id;
	}

	public ArrayList<NodeTemp> getNodes() {
		return nodes;
	}

	public void setId(int i) {
		id = i;
	}

	public void setNodes(ArrayList<NodeTemp> nodes) {
		this.nodes = nodes;
	}
}
