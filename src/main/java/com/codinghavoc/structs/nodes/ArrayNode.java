package com.codinghavoc.structs.nodes;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="arrayNode")
@XmlAccessorType(XmlAccessType.FIELD)

/*
 * After trying to build this in C++ and seeing what the final results were, I'm thinking
 * about breaking this into separate types and not allow the use of mixing datatypes in a
 * single array.
 */
public class ArrayNode extends BaseNode{
	private ArrayList<Object> arrayItems;
	
	public ArrayNode(){}
	
	public ArrayNode(String k){
		nKey = k;
		arrayItems = new ArrayList<>();
	}
	
	public ArrayNode(String k, Object o){
		this(k);
		if(arrayItems == null){
			arrayItems = new ArrayList<Object>();
		}
		arrayItems.add(o);
	}
	
	public boolean addObject(Object o){
		if(!arrayItems.contains(o)){
			arrayItems.add(o);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean contains(Object o){
		if(arrayItems.contains(o)) return true;
		else return false;
	}

	public ArrayList<Object> getArrayItems() {
		return arrayItems;
	}
	
	public boolean removeItem(Object o){
		if(arrayItems.contains(o)){
			arrayItems.remove(o);
			return true;
		} else {
			return false;
		}
	}

	public void setArrayItems(ArrayList<Object> arrayItems) {
		this.arrayItems = arrayItems;
	}

	@Override
	public String toString(){
		return nKey+": "+arrayItems.toString();
	}
}
