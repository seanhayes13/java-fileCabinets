package com.codinghavoc.utils;

public class RelationshipItem {
	private String drawerName;
	private String folderName;
	private int pageID;
	private String listName;
	
	public RelationshipItem(){}
	
	public RelationshipItem(String d, String f, int p, String l){
		drawerName = d;
		folderName = f;
		pageID = p;
		listName = l;
	}
	
	public String getDrawerName() {
		return drawerName;
	}

	public String getFolderName() {
		return folderName;
	}

	public String getListName() {
		return listName;
	}

	public int getPageID() {
		return pageID;
	}

	public void setDrawerName(String dn) {
		drawerName = dn;
	}

	public void setFolderName(String fn) {
		folderName = fn;
	}

	public void setListName(String ln) {
		listName = ln;
	}

	public void setPageID(int pi) {
		pageID = pi;
	}

	public String toString(){
		if(folderName == null){
			return drawerName+"::"+pageID+":"+listName;
		} else {
			return drawerName+":"+folderName+":"+pageID+":"+listName;
		}
	}
}
