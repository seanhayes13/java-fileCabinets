package com.codinghavoc.structs;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.*;

/*
 * Possibility of having stand alone Pages
 * Uses: 
 * ----Company information on stand alone Pages with regional offices stored in Folders
 * ----Teacher information on stand alone Pages with Courses taught in a term in each Folder
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drawer{
	/*
	 * TODO Consider refactoring folders into a HashSet. See notes on Pages->nodes for
	 * more details 
	 */
	@XmlElement//(name="drawerFolders")
	private ArrayList<Folder> folders;
	
	@XmlElement//(name="drawerPages")
	private ArrayList<Page> pages;
	
	@XmlElement//(name="idCount")
	private int idCount = 1;
	
	@XmlAttribute
	private String dName;

	public Drawer(){}
	
	public Drawer(Drawer d){
		folders = d.getFolders();
		pages = d.getPages();
		dName = d.getDrawerName();
	}
	
	public Drawer(String dn){
		dName = dn;
		folders = new ArrayList<>();
		pages = new ArrayList<>();
	}
	
	/**
	 * Add a new Page to the pages list
	 * @param p The Page to add to the pages list
	 */
	public void addFolder(Folder f){
		if(folders==null){
			folders = new ArrayList<Folder>();
		}
		folders.add(f);
	}
	
	/**
	 * Add a new Page to the pages list
	 * @param p The Page to add to the pages list
	 */
	public void addPage(Page p){
		//System.out.println("drawer");
		if(pages == null){
			pages = new ArrayList<Page>();
		}
		pages.add(p);
	}
	
	public boolean deleteFolder(String tgt){
		boolean confirm = false;
		int found = -1;
		if(folders.size()==0){
			System.out.println("Nothing to delete...");
		} else {
			for(int i = 0; i < folders.size(); i++){
				if(folders.get(i).getFolderName().equals(tgt)){
					found = i;
				}
			}
			folders.remove(found);
			confirm = true;
		}
		System.out.println(tgt + " found at index " + found);
		return confirm;
	}
	
	/**
	 * Takes in an integer representing the ID of the page that needs to be deleted. If no Page with that ID is found
	 * a false value is returned. If a Page with that ID is found, it is deleted, a message is displayed to the screen
	 * and a true value is returned. As with the deleteNodeByKey in the Page class, confirming whether to delete this
	 * object is done before this function is called.
	 * @param tgt The ID number of the page that is to be deleted.
	 * @return Returns true if the Page was deleted, false if not
	 */
	public boolean deletePage(int tgt){
		boolean confirm = false;
		int found = -1;
		if(pages.size()==0){
			System.out.println("Nothing to delete...");
		} else {
			for(int i = 0; i < pages.size(); i++){
				if(pages.get(i).getId()==(tgt)){
					found = i;
				}
			}
			pages.remove(found);
			confirm = true;
		}
		System.out.println(tgt + " found at index " + found);
		return confirm;
	}
	
	/**
	 * -WIP- Find all Pages with Nodes that match the criteria provided -WIP-
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return -WIP- Returns an ArrayList of Pages with Nodes that match the search criteria -WIP-
	 */
	public ArrayList<Folder> findManyFolders(String fTgt, Object tgt){
		ArrayList<Folder> result = new ArrayList<>();;
		//fill in later with search
		return result;
	}
	
	/**
	 * -WIP- Find all Pages with Nodes that match the criteria provided -WIP-
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return -WIP- Returns an ArrayList of Pages with Nodes that match the search criteria -WIP-
	 */
	public ArrayList<Page> findManyPages(String fTgt, Object tgt){
		ArrayList<Page> result = new ArrayList<>();;
		//fill in later with search
		return result;
	}
	
	/**
	 * -WIP- Finds the first Page with a Node that matches the criteria provided -WIP-
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return Returns the first Page with matching criteria, or a null object if no Page is found
	 */
	public Folder findOneDrawer(String fTgt, Object tgt){
		Folder result = null;
		//fill in later with search
		return result;
	}
	
	/**
	 * -WIP- Finds the first Page with a Node that matches the criteria provided -WIP-
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return Returns the first Page with matching criteria, or a null object if no Page is found
	 */
	public Page findOnePage(String fTgt, Object tgt){
		Page result = null;
		//fill in later with search
		return result;
	}
	
	public String getDrawerName(){
		return dName;
	}

	public Folder getFolder(ArrayList<String> tgt){
		ArrayList<String>working = new ArrayList<>(tgt);
		Folder result;
		if(working.size()==1){
			result = this.getFolder(working.remove(0));
		} else {
			String a = working.remove(0);
			Folder temp = this.getFolder(a);
			if(working.size()==0){
				result = temp;
			}
			result = getSubFolder(temp,working);
		}
		return result;
	}
	
	public Folder getFolder(String tgt){
		for(Folder f : folders){
			if(f.getFolderName().equals(tgt)){
				return f;
			}
		}
		return null;
	}
	
	public ArrayList<Folder> getFolders(){
		return folders;
	}
	
	public int getidCount(){
		return idCount;
	}
	
	public Page getPage(int i){
		for(Page p : pages){
			if(p.getId() == i){
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<Page> getPages() {
		return pages;
	}
	
	private Folder getSubFolder(Folder f, ArrayList<String> s){
		if(s.size()==1){
			return f.getSubFolder(s.remove(0));//problem
		} else {
			String t = s.remove(0);
			Folder temp = f.getSubFolder(t);
			if(s.size()==0){
				return temp;
			}
			return getSubFolder(temp, s);//problem
		}
	}
	
	public int idIncrement(){
		return idCount++;
	}
	
	public void setDrawerName(String d){
		System.out.println("Setting Drawer Name: "+ d);
		dName = d;
	}

	/**
	 * CAUTION: Only use this when loading configs from file. Any other use can cause problems
	 * @param i The number to set the idCount to
	 */
	public void setIDCount(int i){
		idCount = i;
	}

	public void setPages(ArrayList<Page> pages) {
		this.pages = pages;
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
		Drawer other = (Drawer) obj;
		if (dName == null) {
			if (other.dName != null) {
				return false;
			}
		} else if (!dName.equals(other.dName)) {
			return false;
		}
		if (folders == null) {
			if (other.folders != null) {
				return false;
			}
		} else if (!folders.equals(other.folders)) {
			return false;
		}
		if (idCount != other.idCount) {
			return false;
		}
		if (pages == null) {
			if (other.pages != null) {
				return false;
			}
		} else if (!pages.equals(other.pages)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dName == null) ? 0 : dName.hashCode());
		result = prime * result + ((folders == null) ? 0 : folders.hashCode());
		result = prime * result + idCount;
		result = prime * result + ((pages == null) ? 0 : pages.hashCode());
		return result;
	}
	
	@Override
	public String toString(){
		return "Drawer: "+dName;
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/