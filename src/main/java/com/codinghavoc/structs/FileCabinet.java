package com.codinghavoc.structs;

import java.io.File;
import java.util.ArrayList;

import com.codinghavoc.structs.arrays.StringArray;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.IntegerNode;
import com.codinghavoc.utils.LogTracker;
import com.codinghavoc.utils.ParseCommand;
import com.codinghavoc.utils.ReadWrite;
import com.codinghavoc.utils.WriteThread;

public class FileCabinet {
	private String fcName;
	/*
	 * TODO Consider refactoring drawers into a HashSet. See notes on Pages->nodes for
	 * more details 
	 */
	private ArrayList<Drawer> drawers;
	private StringArray drawerList;
	private Drawer drawerConfig;
	private Drawer activeDrawer;
	private String activeFolder; //Like with ForeignLinks, this will need to account for sub-Folders
	private Page activePage;
	private BaseNode activeNode;
	private boolean showTips = true;
	
	public FileCabinet(){
		drawers = new ArrayList<>();
		drawerList = new StringArray("drawerNames",new ArrayList<>());
		drawerConfig = new Drawer("drawerConfigs");
	}
	
	public FileCabinet(String fcn){
		drawers = new ArrayList<>();
		drawerList = new StringArray("drawerNames",new ArrayList<>());
		drawerConfig = new Drawer("drawerConfigs");
		fcName = fcn;
	    File f = new File("fcRepo//"+fcName);
//	    if(!f.exists()) f.mkdir();
	    if(!f.exists()){
	    	System.out.println("directory does not exist, yet...");
	    	f.mkdir();
	    	System.out.println("now it does...");
	    } else {
	    	System.out.println("directory already exists...");
	    }
//		File logFile = new File("fcRepo\\"+fcName+"\\actionLog.txt");
		LogTracker.initLog(fcName);
	}
	
	public void addDrawer(Drawer d){
		drawers.add(d);
		drawerList.add(d.getDrawerName());
	}

	public void clearActiveDrawer(){
		if(activeDrawer!=null){
			activeDrawer = null;
		}
	}

	public void clearActiveNode(){
		if(activeNode!=null){
			activeNode = null;
		}
	}

	public void clearActivePage(){
		if(activePage!=null){
			activePage = null;
		}
	}
	
	public void clearAllActiveFolder(){
		if(activeFolder!=null){
			activeFolder = null;
		}
	}

	public void clearOneActiveFolder(){
		//This will need to check if the size of temp is 0 after removing the last
		//If size is zero, set activeFolder to null
		if(activeFolder!=null){
			ArrayList<String> temp = ParseCommand.parseFolderInput(activeFolder);
			StringBuilder sb = new StringBuilder();
			temp.remove(temp.size()-1);
			if(temp.size()==0){
				activeFolder = null;
			} else {
				sb.append(temp.remove(0));
				if(temp.size()>=1){
					for(String s : temp){
						sb.append("|"+s);
					}
				}
				activeFolder = sb.toString();
			}
		} else {
			System.out.println("The activeFolder has already been cleared");
		}
	}

	/**
	 * Deletes a Drawer with the specified Drawer name. As with the other delete functions, confirming the action
	 * is done before this function is called.
	 * @param tgt The name of the Drawer to delete
	 * @return True if the Drawer was found and deleted; false if not
	 */
	public boolean deleteDrawer(String tgt){
		boolean confirm = false;
		int found = -1;
		if(drawers.size()==0){
			System.out.println("Nothing to delete...");
		} else {
			for(int i = 0; i < drawers.size(); i++){
				if(drawers.get(i).getDrawerName().equals(tgt)){
					found = i;
				}
			}
			confirm = new File("fcRepo\\"+fcName+"//"+drawers.get(found).getDrawerName()+".xml").delete();
			drawers.remove(found);
			updateConfigs();
			
		}
		System.out.println(tgt + " found at index " + found);
		return confirm;
	}
	
	/**
	 * Deletes a Page with the specified ID value. As with the other delete functions, confirming the action
	 * is done before this function is called.
	 * @param i The ID value of the Page that needs to be deleted.
	 * @return True if the Page was deleted, false if not
	 */
	public boolean deleteFolder(String s){
		boolean confirm = false;
		confirm = activeDrawer.deleteFolder(s);
		if(confirm){
			//saveFileCabinet();
			confirm = true;
		}
		return confirm;
	}
	
	/**
	 * Deletes a Node with a specific nKey. As with the other delete functions, confirming the action
	 * is done before this function is called.
	 * @param s The nKey value of the Node to delete
	 * @return Returns true if the Node was found and deleted; false if not
	 */
	public boolean deleteNode(String s){
		boolean confirm = false;
		confirm = activePage.deleteNodeByKey(s);
		return confirm;
	}
	
	/**
	 * Deletes a Page with the specified ID value. As with the other delete functions, confirming the action
	 * is done before this function is called.
	 * @param i The ID value of the Page that needs to be deleted.
	 * @return True if the Page was deleted, false if not
	 */
	public boolean deletePage(int i){
		boolean confirm = false;
		confirm = activeDrawer.deletePage(i);
		if(confirm){
			//saveFileCabinet();
			confirm = true;
		}
		return confirm;
	}
	
	/*
	 * TODO This will need major rework to search through the Pages in each Drawer 
	 * and the Pages in each Folder in each Drawer
	 */
	/**
	 * Search for Pages with Nodes with matching nKey and nValue values.
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return Returns a list of matching Pages. Returns an empty list if none are found
	 */
	//This will be a problem and need to be reworked
	public ArrayList<Page> findMany(String fTgt, Object tgt){
		ArrayList<Page> result = new ArrayList<>();
		ArrayList<Page> working;
		if(activeDrawer != null){
			if(activeFolder == null){
				working = activeDrawer.getPages();
			} else {
				working = activeDrawer.getFolder(ParseCommand.parseFolderInput(activeFolder)).getPages();
			}
			for(Page p : working){
				for(BaseNode n : p.getNodes()){
					if(n.getnKey().equals(fTgt) && n.getnValue().toString().equals(tgt)){
						result.add(p);
					}
				}
			}
		}
		return result;
	}
	
	/*
	 * TODO This will need major rework to search through the Pages in each Drawer 
	 * and the Pages in each Folder in each Drawer
	 */
	/**
	 * Finds the first Page with a matching Node, regardless if there are other pages with similar Nodes.
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return Return a matching page, if found. Returns a null object if not
	 */
	public Page findOne(String fTgt, Object tgt){
		Page result = null;
		if(activeDrawer != null){
			for(Page p : activeDrawer.getPages()){
				for(BaseNode n : p.getNodes()){
					if(n.getnKey().equals(fTgt) && n.equals(tgt)){
						return p;
					}
				}
			}
		}
		//fill in later with search
		return result;
	}
	
	/*
	 * TODO This will need major rework to search through the Pages in each Drawer 
	 * and the Pages in each Folder in each Drawer
	 */
	/**
	 * Search by nKey only; used when updating arrays
	 * @param fTgt Field Target: which field are you looking for
	 * @return Return a matching Node, if found. Returns a null object if not
	 */
	public BaseNode findOneNode(String fTgt){
		BaseNode result = null;
		for(BaseNode n : activePage.getNodes()){
			if(n.getnKey().equals(fTgt)){
				result = n;
				return result;
			}
		}
		return result;
	}
	
	/*
	 * TODO This will need major rework to search through the Pages in each Drawer 
	 * and the Pages in each Folder in each Drawer
	 */
	/**
	 * Like the Page equivalent, this returns the first node that matches the criteria.
	 * @param fTgt Field Target: which field are you looking for
	 * @param tgt Target: what value are you looking for
	 * @return Return a matching Node, if found. Returns a null object if not
	 */
	//This will be a problem and need to be reworked
	public BaseNode findOneNode(String fTgt, Object tgt){
		BaseNode result = null;
		for(BaseNode n : activePage.getNodes()){
			if(n.getnKey().equals(fTgt)&&n.getnValue().toString().equals(tgt)){
				return n;
			}
		}
		return result;
	}
	
	public Drawer getActiveDrawer(){
		return activeDrawer;
	}
	
	public String getActiveFolder(){
		return activeFolder;
	}
	
	public BaseNode getActiveNode(){
		return activeNode;
	}
	
	public Page getActivePage(){
		return activePage;
	}
	
	public Drawer getDrawer(String s){
		for(Drawer d : drawers){
			if(d.getDrawerName().equals(s)) return d;
		}
		return null;
	}
	
	public Drawer getDrawerConfig() {
		return drawerConfig;
	}
	
	public ArrayList<Drawer> getDrawerList(){
		return drawers;
	}
	
	public ArrayList<String> getDrawerNames(){
		ArrayList<String> result = new ArrayList<String>();
		for(Page p : drawerConfig.getPages()){
			for(BaseNode i : p.getNodes()){
				result.add(i.getnKey().toString());				
			}
		}
		return result;
	}
	
	public int getDrawerSize(){
		return drawers.size();
	}
	
	public String getFcName() {
		return fcName;
	}
	
	public boolean isShowTips() {
		return showTips;
	}

	public void loadAllDrawers(){
		for(Page p : drawerConfig.getPages()){
			for(BaseNode i : p.getNodes()){
				Drawer d = ReadWrite.readFromXml(fcName, i.getnKey()+".xml");
				System.out.println("FileCabinet::Loading Drawer: "+ d.getDrawerName());
				LogTracker.updateLog(fcName,"FileCabinet::Loading Drawer: "+ d.getDrawerName());
				drawers.add(d);
			}
		}
	}
	
	/**
	 * Load a FileCabinet configuration setting into the program using the String parameter to determine 
	 * which FileCabinet to load.
	 * @param input The name of the FileCabinet that is being loaded into the program
	 */
	public void loadConfigs(String input){
		fcName=input;
		drawerConfig = new Drawer("drawerConfigs");
		if(new File("fcRepo\\"+fcName+"\\drawerConfigs.xml").exists()){
			System.out.println("Found drawer configuration settings...\nLoading drawer configuration settings...");
			LogTracker.updateLog(fcName, "Found drawer configuration settings...Loading drawer configuration settings...");
			drawerConfig = ReadWrite.readFromXml(fcName,"drawerConfigs.xml");
//			for(Page p : drawerConfig.getPages()){
//				for(BaseNode bn : p.getNodes()){
//					System.out.println(bn.getnKey()+":"+bn.getnValue());
//				}
//			}
		} else {
			System.out.println("No drawer configuration setting file found...");
		}
	}
	
	/**
	 * Checks if there is already a Drawer by that name, if not, adds that Drawer to the list. Possible Obsolete Function
	 * @param dn The name of the new Drawer to create
	 */
//	public void newDrawer(String dn){
//		Drawer temp = new Drawer(dn);
//		boolean check = true;
//		for(Drawer d : drawers){
//			if(d.getDrawerName().equals(dn)){
//				check = false;
//			}
//		}
//		if(check){
//			System.out.println("Adding new drawer to file cabinet...");
//			drawers.add(temp);
//		} else {
//			System.out.println("A drawer with that name already exists...");
//		}
//		
//	}
	
	/**
	 * Runs the updateConfigs function, and, if there is an activeDrawer, saves that Drawer to JSON.
	 */
	public void saveFileCabinet(){
		updateConfigs();
		for(Drawer d : drawers){
			//writeToXml(d);
			WriteThread.execute(fcName, d);
		}
		//}
	}
	
	/**
	 * Searches through the activeDrawer for a Page with a matching ID.
	 * @param i The ID of the Page that is being searched
	 * @param dn The name of the Drawer that is being search through
	 * @return Returns a Page with a matching ID in the Drawer specified. Returns null if not found
	 */
	public Page selectPageByID(int i, String dn){
		Page result = null;
		boolean checkDrawer = true;
		if(activeFolder!=null){
			checkDrawer = false;
			for(Page p : activeDrawer.getFolder(activeFolder).getPages()){
				if(p.getId() == i){
					result = p;
				}
			}
		}
		if(checkDrawer){
			if(activeDrawer!=null){
				for(Page p : activeDrawer.getPages()){
					if(p.getId() == i){
						result = p;
					}
				}
			}
		}
		return result;
	}
	
	public void setActiveDrawer(Drawer activeDrawer) {
		this.activeDrawer = activeDrawer;
	}
	
	/**
	 * If the drawer is found, set that drawer as the active drawer and return true; false if not
	 * @param dn The name of the Drawer that needs to be set as the activeDrawer
	 * @return True if the drawer is found, false if not
	 */
	public boolean setActiveDrawer(String dn){
		activeDrawer = null;
		for(Drawer d : drawers){
			if(d.getDrawerName().equals(dn)){
				activeDrawer = d;
				return true;
			}
		}
		return false;
	}
	
	public boolean setActiveFolder(String fn){
		//activeFolder = null;
		ArrayList<String> temp = ParseCommand.parseFolderInput(fn);
		Folder check = activeDrawer.getFolder(temp);
		if(check!=null){
			activeFolder = fn;
			return true;
		} else{
			return false;
		}
	}
	
	public void setActiveNode(BaseNode n){
		activeNode = n;
	}
	
	public void setActivePage(Page aP) {
		this.activePage = aP;
	}

	public void setFCName(String fcN){
		fcName = fcN;
	}
	
	public void setShowTips(boolean showTips) {
		this.showTips = showTips;
	}
	
	public void turnOffTips(){
		showTips = false;
	}

	public void turnOnTips(){
		showTips = true;
	}

	/**
	 * Updates just the configurations of each Drawer: the Drawer name and the current ID count
	 */
	public void updateConfigs(){
		if(drawers != null && drawers.size()>0){
			drawerConfig = new Drawer("drawerConfigs");
			for(Drawer d : drawers){
				Page p = new Page(drawerConfig.getidCount());
				p.addNodes(new IntegerNode(d.getDrawerName(),d.getidCount()));
				drawerConfig.addPage(p);
			}
			WriteThread.execute(fcName, drawerConfig);
		}
	}
	
	@Override
	public String toString(){
		return "FileCabinet: "+fcName;
	}
}