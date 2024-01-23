package com.codinghavoc.structs;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.*;

/*
 * Before moving forward with this idea, some things to take into consideration:
 * 1. This will require a lot more coding and recoding that I probably realize right now
 * 2. Foreign relationships will have to be heavily looked at with the almost certainty of
 * 		adding a special class just to handle saving information (much easier now with XML)
 * 3. Provides depth for multiple tiered storage which will need to be addressed when printing
 * 
 * Examples if using Folders
 * 1. Employees working at different locations (employees drawer with folders for Broomfield,
 * 		Phoenix, Tulsa)
 * 2. College courses by term (have a CS101 page in several folders for each quarter all in
 * 		one courses drawer)
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Folder {
	@XmlElement//(name="folderPages")
	private ArrayList<Page> pages;

	/*
	 * TODO Consider refactoring folders into a HashSet. See notes on Pages->nodes for
	 * more details 
	 */
	@XmlElement//(name="subFolders")
	private ArrayList<Folder> folders;

	@XmlAttribute//(name="folderName")
	private String folderName;
	
	@XmlElement
	private int idCount = 1;
	
	public Folder(){}
	
	public Folder(String fn){
		folderName = fn;
	}
	
	public Folder(Folder f){
		if(f.getPages()!=null){
			pages = new ArrayList<Page>();
			pages = f.pages;
		}
		if(f.getFolders()!=null){
			folders = new ArrayList<Folder>();
			folders = f.folders;
		}
		folderName = f.folderName;
	}
	
	public void addFolder(Folder f){
		if(folders == null){
			folders = new ArrayList<Folder>();
		}
		folders.add(f);
	}
	
	/**
	 * Add a new Page to the pages list
	 * @param p The Page to add to the pages list
	 */
	public void addPage(Page p){
		//System.out.println("folder");
		if(pages == null){
			pages = new ArrayList<Page>();
		}
		pages.add(p);
	}
	
	public void deleteFolder(Folder tgt){
		folders.remove(tgt);
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
	public ArrayList<Page> findMany(String fTgt, Object tgt){
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
	public Page findOne(String fTgt, Object tgt){
		Page result = null;
		//fill in later with search
		return result;
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public ArrayList<Folder> getFolders() {
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
	
	public Folder getSubFolder(String s){
		//System.out.println("juliet - "+s);
		for(Folder t : folders){
			//System.out.println("kilo - "+t.folderName +":"+s);
			if(t.folderName.equals(s)){
				return t;
			}
		} return null;
	}
	
	//Getters and Setters

	public int idIncrement(){
		return idCount++;
	}

	public void setFolderName(String f) {
		folderName = f;
	}

	public void setFolders(ArrayList<Folder> f) {
		folders = f;
	}

	/**
	 * CAUTION: Only use this when loading configs from file. Any other use can cause problems
	 * @param i The number to set the idCount to
	 */
	public void setIDCount(int i){
		idCount = i;
	}

	public void setPages(ArrayList<Page> p) {
		pages = p;
	}
	
	@Override
	public String toString(){
		return "Folder: "+folderName;
	}
}
