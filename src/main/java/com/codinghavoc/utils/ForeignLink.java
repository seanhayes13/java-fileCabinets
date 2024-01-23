package com.codinghavoc.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ForeignLink {
	private String drawerName;
	private String folderName;
	private int pageID;
	
	public ForeignLink(){}
	
	public ForeignLink(String d, String f, int p){
		drawerName = d;
		folderName = f;
		pageID = p;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ForeignLink other = (ForeignLink) obj;
		//checking drawer
		if (drawerName == null) {
			if (other.drawerName != null)
				return false;
		} else if (!drawerName.equals(other.drawerName))
			return false;
		
		//checking folder
		if (folderName == null || folderName.equals("")) {
			if (other.folderName != null)
				return false;
		} else if (!folderName.equals(other.folderName))
			return false;
		
		//checking page
		if (pageID != other.pageID)
			return false;
		return true;
	}
	
	public String folderListToString(ArrayList<String> fl){
		return String.join("|", fl);
	}
	
	public ArrayList<String> folderStringToList(){
		return new ArrayList<>(Arrays.asList(folderName.split("|")));
	}
	
	public String getDrawerName() {
		return drawerName;
	}

	public String getFolderName() {
		if(folderName==null){
			return null;
		} else {
			return folderName;
		}
	}
	
	//Getters and Setters

	public int getPageID() {
		return pageID;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setPageID(int pageID) {
		this.pageID = pageID;
	}

	@Override
	public String toString(){
//		System.out.println("hmm:"+folderName.length());
		if(folderName == null){
			return "Drawer: "+drawerName+" --- Page: "+pageID;
		} else {
			return "Drawer: "+drawerName+" --- Folder: "+folderName+" --- Page: "+pageID;
		}
	}
}
