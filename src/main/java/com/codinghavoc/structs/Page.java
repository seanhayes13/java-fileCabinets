package com.codinghavoc.structs;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

import com.codinghavoc.structs.nodes.BaseNode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Page{
	/*
	 * TODO Possibly consider changing from ArrayList to HashSet, eliminates the
	 * hassle of checking so that two nodes of the same type and value are added.
	 * Will probably replace with a check to see if the size of the HashSet 
	 * increases and return true or false to tell the user whether the node was 
	 * added or not.
	 */
	@XmlElement//(name="pageNodes")
	private ArrayList<BaseNode> nodes;
	
	@XmlAttribute
	private int id;

	public Page(){
		//System.out.println("Page default constructor");
		nodes = new ArrayList<>();
		}
	
	public Page(int i){
		nodes = new ArrayList<BaseNode>();
		id = i;
	}
	
	/**
	 * Add a new Node to the nodes list after check whether a Node with the same nKey already exists.
	 * @param n The new Node to add to the list.
	 * @return True if the new Node was added; false if not
	 */
	public boolean addNodes(BaseNode n){
		if(nodes == null){
			nodes = new ArrayList<BaseNode>();
		}
		for(BaseNode n1 : nodes){
			if(n1.getnKey().equals(n.getnKey())){
				//Display a message saying that node already exists
				System.out.println("fail add");
				return false;
			}
		}
		nodes.add(n);
		//System.out.println(id + " - add node - "+n.getnKey()+":"+n.getnValue().toString());
		return true;
	}
	
	/**
	 * Check if the current page has a Node with a given nKey
	 * @param field The name of the nKey to check for
	 * @return
	 */
	public boolean contain(String field){
		for(BaseNode n : nodes){
			if(n.getnKey().equals(field)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Searches through the nodes list for a Node with an nKey that matches the parameter. If found, that
	 * node will be deleted (confirming the delete action is handled by the FileCabinetEngine before
	 * calling this function.
	 * @param tgt The string value to search for.
	 * @return Returns true if a Node was found and deleted; false if no matching Node was found
	 */
	public boolean deleteNodeByKey(String tgt){
		boolean confirm = false;
		int found = -1;
		if(nodes.size()>0){
			for(int i = 0; i < nodes.size();i++){
				if(nodes.get(i).getnKey().equals(tgt)){
					found = i;					
				}
			}			
		}
		if(found > -1){
			nodes.remove(found);
			confirm = true;
		}
		return confirm;
	}
	
	public int getId(){
		return id;
	}
	
	public ArrayList<BaseNode> getNodes(){
		return nodes;
	}
	
	public BaseNode selectNode(String s){
		BaseNode result = null;
		for(BaseNode n : nodes){
			if(n.getnKey().equals(s)){
				result = n;
			}
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
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
		Page other = (Page) obj;
		if (id != other.id) {
			return false;
		}
		if (nodes == null) {
			if (other.nodes != null) {
				return false;
			}
		} else if (!nodes.equals(other.nodes)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		return "Page: "+id;
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/