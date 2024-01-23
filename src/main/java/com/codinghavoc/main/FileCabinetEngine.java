package com.codinghavoc.main;

import java.io.File;
import java.io.FileFilter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.codinghavoc.compare.NodeValueCompare;
import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.structs.*;
import com.codinghavoc.structs.arrays.DoubleArray;
import com.codinghavoc.structs.arrays.IntegerArray;
import com.codinghavoc.structs.arrays.StringArray;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.DateNode;
import com.codinghavoc.structs.nodes.DoubleNode;
import com.codinghavoc.structs.nodes.FKLNode;
import com.codinghavoc.structs.nodes.IntegerNode;
import com.codinghavoc.structs.nodes.StringNode;
import com.codinghavoc.utils.*;

public class FileCabinetEngine{
	//The uiv will be null if working from command prompt mode
	private UserInterfaceView uiv;
	private PrintDetails pd;
	private ArrayList<FileCabinet> cabinets;
	private FileCabinet activeFC;
	private String prompt = ">>>";
	private Set<String> keyList = CommandList.getCmds();
	private ArrayList<String> inputList = null;
	private String actionInput;
	
	public FileCabinetEngine(){
		pd = new PrintDetails();
		cabinets = new ArrayList<>();
	}
	
	public FileCabinetEngine(UserInterfaceView u){
		uiv = u;
		pd = new PrintDetails(u);
		cabinets = new ArrayList<>();
	}
	
	public ArrayList<FileCabinet> getCabinets(){
		return cabinets;
	}
	
	//Done
	public boolean addFC(FileCabinet nfc){
		if(contains(nfc.getFcName())){
			cabinets.add(nfc);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean contains(String s){
		for(FileCabinet fcIter : cabinets){
			if(fcIter.getFcName().equals(s)){
				return true;
			}
		}
		return true;
	}
	
	/*
	 * TODO Working to check this versus westbuttonpanel version to see if they can be merged
	 */
	private void addFolder(String s){
		Folder f = new Folder(s);
		if(activeFC.getActiveFolder()==null){
			activeFC.getActiveDrawer().addFolder(f);
			activeFC.setActiveFolder(s);
		} else {
			Folder tgt;
			String tgtFldr = activeFC.getActiveFolder();
			ArrayList<String> temp = ParseCommand.parseFolderInput(tgtFldr);
			if(temp.size()>1){
				tgt = activeFC.getActiveDrawer().getFolder(temp);				
			} else {
				String tempName = temp.get(0);
				tgt = activeFC.getActiveDrawer().getFolder(tempName);
			}			
			tgt.addFolder(f);
			tgtFldr = tgtFldr+"|"+s;
			activeFC.setActiveFolder(tgtFldr);
		}
	}
	
	//Local method
	private boolean checkArray(BaseNode bn){
		if(bn instanceof StringArray
					|| bn instanceof IntegerArray
					|| bn instanceof DoubleArray){
			return true;
		} else return false;
	}
	
	/*
	 * Updated: step 1
	 */
	private void addToArray(){
		boolean check = false;
		String input = "";
		BaseNode tgt = null;
		DefaultMutableTreeNode selectedNode = null;
		if(uiv !=null){
			selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			Object o = selectedNode.getUserObject();
			if(checkArray((BaseNode)o)){
				input = uiv.valueInput.getText();
				tgt = (BaseNode)o;
				check = true;
			}
		} else {
			if(checkArray(activeFC.getActiveNode())){
				System.out.println("Enter the new value to add: \n"+prompt);
				input = UserInput.getString();
				tgt = activeFC.getActiveNode();
				check = true;
			}
		}
		if(check){
			if(tgt instanceof IntegerArray){
				try{
					Integer i = Integer.parseInt(input);
					((IntegerArray)tgt).add(i);
				} catch (NumberFormatException e){
					uiv.updateMsg("Invalid format, please try again");
				}
			} else if( tgt instanceof DoubleArray){
				try{
					Double d = Double.parseDouble(input);
					((DoubleArray)tgt).add(d);
				} catch (NumberFormatException e){
					uiv.updateMsg("Invalid format, please try again");
				}
			} else {
				((StringArray)tgt).add(input);
			}
			if(uiv!=null){
				uiv.model.reload(selectedNode);
			}
		}
	}
	
	/**
	 * This function builds the relationships between pages in the same or different drawers
	 */
	//Really need to look at this to make sure everything is still working right
	/*
	 * 22 Aug 2017 - Not going to mess with integrating this to use the GUI at this point. Both
	 * sets of code are working, do not want to risk breaking it. Will come back to this block and
	 * the related block in the GUI later.
	 */
	private void buildRelationship(){
		//filler for key on far-side list if not using a farside list: 1b61958c83d51a0b4a58295d9e272a3a
		checkNullPage();
		if(activeFC.isShowTips()){
			sendToDisplay("Use the following format: far-side-drawer:far-side-page-ID:near-side-list:far-side-list*");
			//0
			sendToDisplay("far-side-drawer - The name of the drawer that has the page you are linking to");
			//1
			sendToDisplay("far-side-folder - The Folder path from the Drawer to the page you are linking to");
			//2
			sendToDisplay("far-side-page-ID - The Page ID of the page you are linking to");
			//3
			sendToDisplay("far-side-list - The name of the list on the page you are linking to; OPTIONAL, if not used do not include any text");
			//4
			sendToDisplay("near-side-list - The name of the list on the selected Page");
			
			sendToDisplay("Enter the relationship using the format above:"+prompt);
		} else {
			sendToDisplay("Enter the relationship:"+prompt);
		}
		String bRCmd = UserInput.getString();
		if(bRCmd.equals("QUIT")) return;
		ArrayList<RelationshipStruct> working = ParseCommand.parseBuildRel(bRCmd, activeFC.getActiveDrawer().getDrawerName(), activeFC.getActiveFolder(), activeFC.getActivePage().getId()); 
		for(RelationshipStruct r : working){
			RelationshipItem tgt = r.getTgtRI();
			RelationshipItem src = r.getSrcRI();
			if(activeFC.getDrawer(tgt.getDrawerName())!=null 
					&& activeFC.getDrawer(tgt.getDrawerName()).getFolder(ParseCommand.parseFolderInput(tgt.getFolderName())) != null 
					&& activeFC.getDrawer(tgt.getDrawerName()).getFolder(ParseCommand.parseFolderInput(tgt.getFolderName())).getPage(tgt.getPageID())!=null){
				System.out.println("Start build...");
				//set list in near side
				boolean nearSet = false;
				ArrayList<BaseNode> nearList;
				if(activeFC.getActiveFolder()==null){
					nearList = activeFC.getActivePage().getNodes();
				} else {
					nearList = activeFC.getActiveDrawer().getFolder(ParseCommand.parseFolderInput(src.getFolderName())).getPage(src.getPageID()).getNodes();
				}
				if(nearList != null){
					for(BaseNode temp : nearList){
						//if key and node type are equal
						if(temp.getnKey().equals(src.getListName()) && temp instanceof FKLNode){
							ForeignLink fl = new ForeignLink(tgt.getDrawerName(), tgt.getFolderName(), tgt.getPageID());
							((FKLNode)temp).addLink(fl);
							nearSet = true;
						}
						//if node type is not fkl
						if(temp.getnKey().equals(src.getListName()) && (!(temp instanceof FKLNode))){
							sendToDisplay("Found a Node with that key but the Node is not an FKLNode");
							nearSet = true;
						}
					}
				}
				if(nearSet==false){
					FKLNode fklNode = new FKLNode(src.getListName(),tgt.getDrawerName(), tgt.getFolderName(), tgt.getPageID());
					activeFC.getActivePage().addNodes(fklNode);
				}
				//set far side list name
				String farSideName = null;
				if(tgt.getListName()==null){
					farSideName = "1b61958c83d51a0b4a58295d9e272a3a";
				} else {
					farSideName = tgt.getListName();
				}
				//set list in far side
				//farside list name is not being set properly
				boolean farSet = false;
				ArrayList<BaseNode> farList;
				if(tgt.getFolderName()==null){
					farList = activeFC.getDrawer(tgt.getDrawerName()).getPage(tgt.getPageID()).getNodes();
				} else {
					farList = activeFC.getDrawer(tgt.getDrawerName()).getFolder(ParseCommand.parseFolderInput(tgt.getFolderName())).getPage(tgt.getPageID()).getNodes();
				}
				if(farList!=null){
					for(BaseNode temp : farList){
						if(temp.getnKey().equals(farSideName) && temp instanceof FKLNode){
							ForeignLink fl = new ForeignLink(src.getDrawerName(),src.getFolderName(), src.getPageID());
							((FKLNode)temp).addLink(fl);
							farSet = true;
						}
						if(temp.getnKey().equals(farSideName) && (!(temp instanceof FKLNode))){
							sendToDisplay("Found a Node with that key but the Node is not an FKLNode");
							farSet = true;
						}
					}
				}
				if(farSet==false){
					FKLNode fklNode = new FKLNode(farSideName, src.getDrawerName(), src.getFolderName(), src.getPageID());
					activeFC.getDrawer(tgt.getDrawerName()).getFolder(ParseCommand.parseFolderInput(tgt.getFolderName())).getPage(tgt.getPageID()).addNodes(fklNode);
				}
				System.out.println("Build finished");
			} else {
				System.out.println("Build failed");
			}
			saveFC();
		}
	}

	//command prompt only command
	private void checkNullDrawer(){
		while(activeFC.getActiveDrawer()==null){
			sendToDisplay("Please select an active Drawer");
			selectDrawer();
		}
	}

	//command prompt only command
	private void checkNullFileCab(){
		while(activeFC == null){
			sendToDisplay("Please select an active File Cabinet");
			loadFC();
		}
	}

	//command prompt only command
	private void checkNullFolder(){
		//Since Pages can be attached to Drawers and Folders, this may be unnecessary
		//add check to see if the new element is going inside a folder or being attached at the current level
		while(activeFC.getActiveFolder()==null){
			sendToDisplay("Please select an active Folder");
			selectFolder();
		}
	}

	//command prompt only command
	private void checkNullNode(){
		checkNullPage();
		while(activeFC.getActiveNode()==null){
			sendToDisplay("Please select an active Node");
			selectNode();
		}
	}

	//command prompt only command
	private void checkNullPage(){
		//
		checkNullDrawer();
		sendToDisplay("Please select an active Drawer");
		while(activeFC.getActivePage()==null){
			selectPage();
		}
	}
	
	/**
	 * Clear out the activeDrawer, including activePage and activeNode, and reset the prompt 
	 */
	//command prompt only command
	private void clearActiveDrawer(){
		activeFC.clearActiveNode();
		activeFC.clearActivePage();
		activeFC.clearActiveDrawer();
		prompt = activeFC.getFcName()+">>>";		
	}
	
	/**
	 * Clears out the activeFileCabinet, including equivalent Drawer, Page, and Node, and resets the prompt
	 */
	//command prompt only command
	private void clearActiveFileCabinet(){
		activeFC.clearActiveNode();
		activeFC.clearActivePage();
		activeFC.clearActiveDrawer();
		activeFC = null;
		prompt = ">>>";
	}
	
	/**
	 * Clears out the activeNode and resets the prompt
	 */
	//command prompt only command
	private void clearActiveNode(){
		activeFC.clearActiveNode();
		prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActiveFolder()+":"+activeFC.getActivePage().getId()+">>>";
	}
	
	/**
	 * Clears out the activePage and resets the prompt
	 */
	//command prompt only command
	private void clearActivePage(){
		System.out.println("Clearing active page...");
		activeFC.clearActiveNode();
		activeFC.clearActivePage();
		if(activeFC.getActiveFolder()!=null){
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActiveFolder()+">>>";
		} else {
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+">>>";
		}
	}
	
	//command prompt only command
	private void clearAllFolders(){
		activeFC.clearActiveNode();
		activeFC.clearActivePage();
		activeFC.clearAllActiveFolder();
		prompt = activeFC.getActiveDrawer().getDrawerName()+">>>";
	}

	//command prompt only command
	private void clearOneFolder(){
		activeFC.clearActiveNode();
		activeFC.clearActivePage();
		activeFC.clearOneActiveFolder();
		if(activeFC.getActiveFolder()!=null){
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActiveFolder()+">>>";
		} else {
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+">>>";
		}
	}
	
	/*
	 * Replace the UserInput section with selecting a different button on the GUI
	 * Updated in GUI actions
	 */
	private void convertToArray(){
		checkNullNode();
		BaseNode working = activeFC.getActiveNode();
		if(working instanceof IntegerNode 
				|| working instanceof DoubleNode 
				|| working instanceof StringNode){
			BaseNode bn;
			if(uiv==null){
				String tempKey = activeFC.getActiveNode().getnKey();
				if(activeFC.getActiveNode() instanceof IntegerNode){
					bn = new IntegerArray((IntegerNode)activeFC.getActiveNode());
				} else if(activeFC.getActiveNode() instanceof DoubleNode){
					bn = new DoubleArray((DoubleNode) activeFC.getActiveNode());
				} else {
					bn = new StringArray((StringNode)activeFC.getActiveNode());
				}
				activeFC.getActivePage().deleteNodeByKey(tempKey);
				activeFC.getActivePage().addNodes(bn);
				
			} else {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
				Object o = selectedNode.getUserObject();
				boolean check = false;
				bn = null;
				if(o instanceof StringNode){
					bn = new StringArray();
					bn.setnKey(((StringNode)o).getnKey());
					((StringArray)bn).add(((StringNode)o).getnValue());
					check = true;
				}
				if (o instanceof IntegerNode){
					bn = new StringArray();
					bn.setnKey(((IntegerNode)o).getnKey());
					((IntegerArray)bn).add(((IntegerNode)o).getnValue());
					check = true;
				}
				if (o instanceof DoubleNode){
					bn = new DoubleArray();
					bn.setnKey(((DoubleNode)o).getnKey());
					((DoubleArray)bn).add(((DoubleNode)o).getnValue());
					check = true;
				}
				if(check){
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(bn);
					uiv.updateView(newNode, selectedNode);
					if (selectedNode != null && selectedNode.getParent() != null)
						uiv.model.removeNodeFromParent(selectedNode);
				} else {
					uiv.clearMsg();
					uiv.updateMsg("That node cannot be converted to an array");
				}
			}		
		} else {
			System.out.println("The active Node cannot be converted to an array at this time.");
		}
	}
	
	//TODO need to test
	private void copyDrawer(){
		//Coming soon
		checkNullDrawer();
		boolean check = false;
		String input;
		while(check == false){
			System.out.printf("Enter the name of the new Drawer:\n%s",prompt);
			input = UserInput.getString();
			if(activeFC.getDrawerNames().contains(input)){
				System.out.println("A Drawer with that name already exists, please provide a different name");
			} else {
				check = true;
				Drawer d = new Drawer(activeFC.getActiveDrawer());
				d.setDrawerName(input);
				activeFC.addDrawer(d);
			}
		}
	}
	
	//TODO
	private void copyFolder(){
		//Coming soon
		checkNullFolder();
		System.out.printf("Enter the name of the new Folder:\n%s", prompt);
		String input = UserInput.getString();
		if(input.equals("QUIT"))return;
	}
	
	//TODO
	private void copyNode(){
		String input;
		checkNullNode();
		System.out.printf("Enter the name of the new Node \n>>>");
		input = UserInput.getString();
		if(input.equals("QUIT")) return;
		if(!activeFC.getActivePage().contain(input)){
			activeFC.getActivePage().addNodes(RedirectInput.redirectInput(input,activeFC.getActiveNode().getnValue().toString()));
		} else {
			System.out.println("A node with that key already exists");
		}
	}
	
	//TODO
	private void copyPage(){
		//Coming soon
		checkNullPage();
	}
	
	/**
	 * Deletes a Folder from the activeFolder's parent
	 */
	//TODO Will need to check each page to determine if an FKLNode is present, if so, delete that page
	private void deleteFolder(){
		//create a separate instance of the Folder to be deleted
		Folder f = activeFC.getActiveDrawer().getFolder(ParseCommand.parseFolderInput(activeFC.getActiveFolder()));
		//check for subfolders
		//if any of the folders have pages, run deletePageRep
		if(f.getFolders()!=null){
			for(Folder fIter : f.getFolders()){
				checkFolder(fIter);
			}			
		}
		//check if the folder has any pages, if so, run deletePageRep
		if(f.getPages()!=null){
			for(Page pIter : f.getPages()){
				for(BaseNode bn : pIter.getNodes()){
					if(bn instanceof FKLNode){
						//need to figure out how I am going to work with adding folder levels
						ForeignLink tgtFl = new ForeignLink(activeFC.getActiveDrawer().getDrawerName(), null ,pIter.getId());
						deletePageRep(tgtFl);
					}
				}
			}
		}
		if(uiv!=null)uiv.deleteNode();
		/*
		 * Three situations to consider:
		 * 1 - the folder being deleted is directly attached to the drawer
		 * 2 - the folder being deleted is at the end of the folder string,
		 * 		example, folder path: alpha|bravo|charlie and we are deleting
		 * 		charlie
		 */
		activeFC.clearOneActiveFolder();
		if(activeFC.getActiveFolder()==null){
			//Scenario 1 - delete folder from drawer
			activeFC.getActiveDrawer().deleteFolder(f.getFolderName());
		} else {
			//Scenario 2 - delete folder from folder
			Folder temp = activeFC.getActiveDrawer().getFolder(ParseCommand.parseFolderInput(activeFC.getActiveFolder()));
			temp.deleteFolder(f);
		}
		/*
		 * Options:
		 * 1. Auto save drawer
		 * 2. Wait for user to save FileCabinet
		 * 3. Modify to allow user to save at drawer level
		 */
		ReadWrite.writeToXml(activeFC.getFcName(),activeFC.getActiveDrawer());
	}
	
	/**
	 * Deletes a Drawer from the activeFileCabinet
	 */
	//TODO
	private void deleteDrawer(){
		if(uiv==null){
			checkNullFileCab();
			String input;
			System.out.printf("Enter the name of the drawer you want to delete:\n%s",prompt);
			input = UserInput.getString();
			if(input.equals("QUIT")) return;
			if(UserInput.confirmChange()){
				//get the drawer from the activeFC
				Drawer d = activeFC.getDrawer(input);
				checkDrawer(d);
				activeFC.deleteDrawer(input);
			} else {
				System.out.println("Delete was not confirmed, nothing has been deleted");
			}
		} else {
			Drawer d = activeFC.getActiveDrawer();
			activeFC.clearActiveDrawer();
			checkDrawer(d);
			activeFC.deleteDrawer(d.getDrawerName());
			uiv.deleteNode();
		}
		//FileCabinet is already deleting the drawer
	}
	
	private void checkDrawer(Drawer d){
		//check for folders and subfolders
		if(d.getFolders()!=null){
			//Check folders, great place for a recursive method to dig through sub folders
			//if any of the folders have pages with FKLNodes, run deletePageRep
			for(Folder fIter : d.getFolders()){
				checkFolder(fIter);
			}
		}
		if(d.getPages()!=null){
			//check if the drawer has any pages with FKLNodes, if so, run deletePageRep
			for(Page pIter : d.getPages()){
				for(BaseNode bn : pIter.getNodes()){
					if(bn instanceof FKLNode){
						//setting the folderName section of this FL to null since we are working from the Drawer
						ForeignLink tgtFl = new ForeignLink(d.getDrawerName(), null ,pIter.getId());
						deletePageRep(tgtFl);
					}
				}
			}
		}
	}
	
	private void checkFolder(Folder f){
		if(f.getFolders()!=null){
			for(Folder fIter : f.getFolders()){
				checkFolder(fIter);
			}
		}
		if(f.getPages()!=null){
			for(Page pIter : f.getPages()){
				for(BaseNode bn : pIter.getNodes()){
					if(bn instanceof FKLNode){
						//need to figure out how I am going to work with adding folder levels
						ForeignLink tgtFl = new ForeignLink(activeFC.getActiveDrawer().getDrawerName(), null ,pIter.getId());
						deletePageRep(tgtFl);
					}
				}
			}
		}
	}
	
	//Updated in GUI actions
	private void deleteFromArray(){
		if(activeFC.getActiveNode() instanceof StringArray
				|| activeFC.getActiveNode() instanceof IntegerArray
				|| activeFC.getActiveNode() instanceof DoubleArray){
			String input;
			if(uiv==null){
				System.out.println("Enter the new value to remove: \n"+prompt);
				input = getAction();
			} else {
				input = actionInput;
			}
			boolean removed = false;
			if(activeFC.getActiveNode() instanceof IntegerArray){
				Integer i = Integer.parseInt(input);
				if(uiv == null){
					removed = ((IntegerArray)activeFC.getActiveNode()).remove(i);
				} else {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
					removed = ((IntegerArray)selectedNode.getUserObject()).remove(i);
				}
			} else if( activeFC.getActiveNode() instanceof DoubleArray){
				Double d = Double.parseDouble(input);
				if(uiv==null){
					removed = ((DoubleArray)activeFC.getActiveNode()).remove(d);
				} else {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
					removed = ((DoubleArray)selectedNode.getUserObject()).remove(d);
				}
			} else {
				if(uiv==null){
					removed = ((StringArray)activeFC.getActiveNode()).remove(input);
				} else {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
					removed = ((StringArray)selectedNode.getUserObject()).remove(input);
				}
			}
			if(uiv==null){
				if(removed){
					System.out.println("Removal successful");
				} else {
					System.out.println("Something went wrong...");
				}				
			} else {
				if(removed){
					uiv.updateMsg("Removal successful");
				} else {
					uiv.updateMsg("Something went wrong...");
				}
			}
		} else {
			System.out.println("Array node not selected");
		}
	}
	
	/*
	 * Delete a Node from the activePage by selecting the nKey of the Node to delete
	 * Status - Working
	 */
	private void deleteNodeByKey(){
		if(uiv==null){
			checkNullPage();
			String input;
			System.out.printf("Enter the field name of the node you want to delete:\n%s",prompt);
			input = UserInput.getString();
			if(input.equals("QUIT")) return;
			if(UserInput.confirmChange()){
				//if the node is an FKLNode, need to run deleteRelationship
				if(activeFC.getActiveNode()!=null && activeFC.getActiveNode().getnKey().equals(input)) clearActiveNode();
				if(activeFC.deleteNode(input)){
					System.out.println("Node deleted");
				} else {
					System.out.println("Node not found, nothing deleted.");
				}
			}else{
				System.out.println("Delete was not confirmed, nothing has been deleted");
			}
		} else {
			BaseNode tgt = (BaseNode)((DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent()).getUserObject();
			if(tgt instanceof FKLNode){
				System.out.println("Hold it there cowboy");
			} else {
				activeFC.getActivePage().deleteNodeByKey(tgt.getnKey());
				uiv.clearMsg();
				uiv.updateMsg("Should have worked");
				uiv.deleteNode();
			}
		}
		/*
		 * Options:
		 * 1. Auto save drawer
		 * 2. Wait for user to save FileCabinet
		 * 3. Modify to allow user to save at drawer/folder level
		 */
		ReadWrite.writeToXml(activeFC.getFcName(),activeFC.getActiveDrawer());
	}
	
	/*
	 * GUI is working with pages attached to drawers
	 */
	private void deletePage(){
		//No drawer selected
		if(activeFC.getActiveDrawer()==null){
			System.out.println("Please select a Drawer first.");
			return;
		}
		//Selected drawer does not have any pages and there are no folders to check
		if(activeFC.getActiveDrawer().getPages()==null && activeFC.getActiveFolder()==null){
			System.out.println("This drawer does not have any pages to delete and there are no folders to check.");
			return;
		}
		//Selected folder does not have any pages to delete
		if(activeFC.getActiveFolder()!=null && activeFC.getActiveDrawer().getFolder(ParseCommand.parseFolderInput(activeFC.getActiveFolder())).getPages()==null){
			System.out.println("This folder does not have any pages to delete.");
			return;
		}
		if(uiv==null){ //first work with command prompt version
			//At this point there is an active drawer with either pages or folders with pages
			//get the page number to delete
			System.out.printf("Enter the PageID for the page you want to delete (type -1 to quit):\n%s",prompt);
			int tgt = UserInput.getInt();
			if(tgt == -1)return;
			if(UserInput.confirmChange()){
				ForeignLink tgtFl = new ForeignLink(activeFC.getActiveDrawer().getDrawerName(), activeFC.getActiveFolder(),tgt);
				deletePageRep(tgtFl);
				//if no FKLNodes are found delete the page
				activeFC.getActiveDrawer().deletePage(tgt);
			}
		} else { //now gui version
			int nearTgt = ((Page)((DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent()).getUserObject()).getId();
			//create the foreign link, based on the selected node, that we are looking for
			ForeignLink tgtFl = new ForeignLink(activeFC.getActiveDrawer().getDrawerName(), activeFC.getActiveFolder(), nearTgt);
			deletePageRep(tgtFl);
			//if no FKLNodes are found delete the page
			if(activeFC.getActiveFolder()==null){
				activeFC.getActiveDrawer().deletePage(nearTgt);
			} else {
				activeFC.getActiveDrawer().getFolder(ParseCommand.parseFolderInput(activeFC.getActiveFolder())).deletePage(nearTgt);
			}
			uiv.clearMsg();
			uiv.updateMsg("Should have worked");
			uiv.deleteNode();
		}
	}
	
	/**
	 * 
	 * @param tgtFl The ForeignLink for the Page being deleted
	 */
	private void deletePageRep(ForeignLink tgtFl){
		//
		Page p = null;
		if(activeFC.getActiveFolder()!=null){
			p = activeFC.getDrawer(tgtFl.getDrawerName()).getFolder(ParseCommand.parseFolderInput(tgtFl.getFolderName())).getPage(tgtFl.getPageID());
		} else {
			p = activeFC.getDrawer(tgtFl.getDrawerName()).getPage(tgtFl.getPageID());
		}
		//check each node for an FKLNode
		for(BaseNode bn : p.getNodes()){
			if(bn instanceof FKLNode){
				//if an FKLNode is found will need to use each of the links in that node, 
				//open those pages' FKLNodes and remove the reference
				//to the page being deleted. After that the selected Page can be deleted.
				for(ForeignLink fl : ((FKLNode)bn).getKeyList()){
					Page pIter = null;
					if(activeFC.getActiveFolder()!=null){
						System.out.println("We have folders");
						pIter = activeFC.getDrawer(fl.getDrawerName()).getFolder(ParseCommand.parseFolderInput(fl.getFolderName())).getPage(fl.getPageID());
					} else {
						System.out.println("We don't have folders");
						pIter = activeFC.getDrawer(fl.getDrawerName()).getPage(fl.getPageID());
					}

					ArrayList<String> deleteThese = new ArrayList<>();
					for(BaseNode bnIter : pIter.getNodes()){
						if(bnIter instanceof FKLNode){
							if(((FKLNode) bnIter).check(tgtFl)){
								((FKLNode) bnIter).removeLink(tgtFl);
								System.out.println("Removed Link");
								if(bnIter.getnValue()==null){
									System.out.println("COPPERHEAD - Null nValue");
									deleteThese.add(bnIter.getnKey());
								}
							} else {
								System.out.println("Not Found");
							}
						}
					}
					for(String s : deleteThese){
						pIter.deleteNodeByKey(s);
					}
					//create a temp drawer based on the new information
					Drawer tempD = activeFC.getDrawer(fl.getDrawerName());
					//find the location of the drawer in the tree
					int fcTgt = 0;
					int drTgt = 0;
					for(int i = 0; i < uiv.getRoot().getChildCount(); i++){
						for(int j = 0; j < uiv.getRoot().getChildAt(i).getChildCount(); j++){
							if(uiv.getRoot().getChildAt(i).getChildAt(j).toString().equals("Drawer: "+tempD.getDrawerName())){
								fcTgt = i;
								drTgt = j;
							}
						}
					}
					//remove the original drawer
					uiv.model.removeNodeFromParent((MutableTreeNode)uiv.getRoot().getChildAt(fcTgt).getChildAt(drTgt));
					//write the drawer to file
					Date date = new Date();
					System.out.println("Saving "+ tempD.getDrawerName() + " at " + new Timestamp(date.getTime()));
					ReadWrite.writeToXml(activeFC.getFcName(), tempD);
					//create a new drawer and read from the file
					Drawer updatedDrawer = ReadWrite.readFromXml(activeFC.getFcName(), tempD.getDrawerName()+".xml");
					//create a defaultmutabletreenode and build it using the build drawer from uiv
					DefaultMutableTreeNode updatedNode = uiv.buildDrawer(updatedDrawer);
					//add that new node to the tree at the original's index
					DefaultMutableTreeNode parent = ((DefaultMutableTreeNode)uiv.getRoot().getChildAt(fcTgt)); 
					uiv.model.insertNodeInto(updatedNode, parent, drTgt);
				}				
			}
		}
	}
	
	/*
	 * TODO Will need to be relooked with new structure
	 * This function will not be used in the new GUI, replaced by the Delete button
	 */
//	private void deleteRelationship(String dN, String fN, int pI){
//		ForeignLink tgtLnk = new ForeignLink(dN,fN,pI);
//		for(Drawer d : activeFC.getDrawerList()){
//			for(Page p : d.getPages()){
//				for(BaseNode n : p.getNodes()){
//					if(n.getnValue() instanceof FKLNode){
//						if(((FKLNode)n).check(tgtLnk)){
//							((FKLNode)n).removeLink(tgtLnk);
//						}
//					}
//					if(((FKLNode)n).getKeyList()==null){
//						activeFC.deleteNode(n.getnKey());
//					}
//				}
//			}
//		}
//		saveFC();
//	}
	
	/**
	 * Selects which method to call based on the String passed to it from one of the run methods
	 * @param s
	 */
	public void execute(String s){
		if(keyList.contains(s)){
			switch(s){
			case "addtoarray":
				addToArray();
				break;
			case "backup":
				//fc.backUpFC();
				break;
			case "buildrelationship":
				buildRelationship();
				break;
			case "clearallfolders":
				clearAllFolders();
				break;
			case "cleardrawer":
				clearActiveDrawer();
				break;
			case "clearfc":
				clearActiveFileCabinet();
				break;
			case "clearnode":
				clearActiveNode();
				break;
			case "clearonefolder":
				clearOneFolder();
				break;
			case "clearpage":
				clearActivePage();
				break;
			case "convertoarray":
				convertToArray();
				break;
			case "copydrawer":
				copyDrawer();
				break;
			case "copyfolder":
				copyFolder();
				break;
			case "copynode":
				copyNode();
				break;
			case "copypage":
				copyPage();
				break;
			case "deletedrawer":
				deleteDrawer();
				break;
			case "deletefolder":
				deleteFolder();
				break;
			case "deletefromarray":
				deleteFromArray();
				break;
			case "deletenodebykey":
				deleteNodeByKey();
				break;
			case "deletepage":
				deletePage();
				break;
//			case "deleterelationship":
//				deleteRelationship("students",1);
//				break;
			case "exit":
				break;
			case "filter":
				ArrayList<Page> srchTgt;
				if(activeFC.getActiveFolder()!=null){
					srchTgt = activeFC.getActiveDrawer().getFolder(ParseCommand.parseFolderInput(activeFC.getActiveFolder())).getPages();
				} else {
					srchTgt = activeFC.getActiveDrawer().getPages();
				}
				NodeValueCompare.filter(activeFC, srchTgt);
				break;
			case "findall":
				findAll();
				break;
			case "findmany":
				//Will need to be relooked with new structure
				//findPage("many");
				break;
			case "findpage":
				//Will need to be relooked with new structure
				//findPage("one");
				break;
			case "help":
				CommandList.printList();
				break;
			case "listdrawers":
				listDrawers();
				break;
			case "listfc": //sh
				listfc();
				break;
			case "loadalldrawers":
				loadAllDrawers();
				break;
			/*case "loaddrawer":
				loadDrawer();
				break;*/
			case "loadfc":
				System.out.println("1");
				loadFC();
				break;
			case "newarray":
				newArray();
				break;
			case "newdrawer":
				newDrawer();
				break;
			case "newfc":
				newfc();
				break;
			case "newfolder":
				newFolder();
				break;
			case "newnode":
				newNode();
				break;
			case "newpage":
				newPage();
				break;
			case "save":
				saveFC();
				break;
			case "selectnode":
				selectNode();
				break;
			case "selectdrawer":
				selectDrawer();
				break;
			case "selectfolder":
				selectFolder();
				break;
			case "selectpage":
				selectPage();
				break;
			case "turnofftips":
				activeFC.turnOffTips();
				break;
			case "turnontips":
				activeFC.turnOnTips();
				break;
			case "updatenode":
				updateNode();
				break;
//			case "writetofile":
//				fc.writeToFile();
//				break;
			}
		} else {
			System.out.println("That is not a valid command, try again...");
		}
		
	}
	
	/*
	 * command prompt only method
	 */
	private void findAll(){
		pd.printDetails(activeFC);
	}
	
	/**
	 * Search for Page or Pages that contain a particular Node. This has not been updated
	 * to search through ForeignKeyList objects or arrays. Potentially obsolete after the
	 * introduction of the filter.
	 * @param limit Sets whether to return one or many pages.
	 */
	//TODO Will need to be relooked with new structure
	@SuppressWarnings("unused")
	private void findPage(String limit){
		String input ="";
		String tgt = "";
		while(input.length()<=0 && !input.equals("QUIT")){
			System.out.println("Enter the name of the field/n"+prompt);
			input = getAction();			
		}
//		System.out.println("Enter the name of the field/n"+prompt);
//		input = getAction();
//		if(input.length()>0){
//			chkKey = true;
//		}
		System.out.println("Enter the value to search for/n"+prompt);
		tgt = getAction();
		while(tgt.length()>0 && !tgt.equals("QUIT")){
			System.out.println("Enter the value to search for/n"+prompt);
			tgt = getAction();
		}
		if(limit.equals("one")){
			Page result = null;
			result = activeFC.findOne(input, tgt);
			if(result!=null){
				PrintDetails.printPage(activeFC, result);
			} else {
				System.out.println("No match found");
			}
		} else {
			ArrayList<Page> result = new ArrayList<Page>();
			result = activeFC.findMany(input, tgt);
			if(result.size()>0){
				for(Page p : result){
					PrintDetails.printPage(activeFC, p);
				}
			} else {
				System.out.println("No match found");
			}
		}
	}
	
	/**
	 * If the user has entered a batch command pop the first command from the start of the list,
	 * if the no batch command is being used or the batch command list is empty, prompts the user
	 * for the next command.
	 * @return The next action to execute
	 */
	//background command
	private String getAction(){
		String result = "";
		if(inputList != null && !inputList.isEmpty()){
			result = inputList.get(0);
			inputList.remove(0);
			if(inputList.isEmpty()){
				inputList = null;
			}
		} else {
			result = UserInput.getString();
		}
		return result;
	}
	
	//background command
	@SuppressWarnings("unused")
	private String getActionInput() {
		return actionInput;
	}

	/*
	 * This function will not be used in the new GUI, for obvious reasons
	 */
	private void listDrawers(){
		for(Drawer d : activeFC.getDrawerList()){
			System.out.println("--"+d.getDrawerName());
		}
	}
	
	/**
	 * List all of the FileCabinets in the current directory
	 */
	/*
	 * This function will not be used in the new GUI, for obvious reasons
	 */
	private void listfc(){
		File dir = new File("root");
		File[] files = dir.listFiles();
		//Only display items that are folders, not individual files
		files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});   
		if (files.length == 0) {
			System.out.println("Either dir does not exist or is not a directory");
		} else {
			for (int i = 0; i< files.length; i++) {
				File filename = files[i];
				System.out.println("-->"+filename.getName());
			}
	   }
	}
	
	/**
	 * Loads all drawers currently assigned to the FileCabinet into the program.
	 */

	/*
	 * This function will not be used in the new GUI, for obvious reasons
	 */
	private void loadAllDrawers(){
		activeFC.loadAllDrawers();
	}
	
	/**
	 * Currently disabled until the save can be properly updated to only update the save files
	 * of Drawers that have been loaded into the program. 
	 */
	/*
	 * This function will not be used in the new GUI, for obvious reasons
	 */
	@SuppressWarnings("unused")
	private void loadDrawer(){
		String input;
		System.out.printf("Enter the name of the drawer you want to work with:\n%s",prompt);
		input = getAction().toLowerCase();
		File f = new File(activeFC.getFcName()+"\\"+input+".xml");
		if(f.exists()){
			System.out.println("Found Drawer configs, loading now...");
			Drawer d = ReadWrite.readFromXml(activeFC.getFcName(),input);
			System.out.println("Would you like to set this Drawer as the active Drawer? (y/n)\n"+prompt);
			String resp = UserInput.getString().toLowerCase();
			if(resp.equals("y")){
				activeFC.setActiveDrawer(new Drawer(d));
				prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+">>>";
			}
		} else {
			System.out.println("That drawer does not exist");
		}
	}
	
	/**
	 * Prompt user for the name of the FileCabinet to work with. If the target FileCabinet does not exist,
	 * ask the user if they want to create the FileCabinet.
	 */
	/*
	 * TODO Need to update the GUI side of this to get input from the user, check if that FileCabinet
	 * is in the cabinets list, set that one as active, if not, load from files 
	 */
	private void loadFC(){
		String input;
		sendToDisplay("Enter the name of the database you want to work with:\n"+prompt);
		input = getAction();
		//check if a folder exists
		File f = new File("fcRepo//"+input);
		if(f.exists()){
			LogTracker.sessionStart(input);
			sendToDisplay("Found file, uploading configuration settings...");
			activeFC = new FileCabinet();
			activeFC.loadConfigs(input);
			addFC(activeFC);
		} else {
			sendToDisplay("Did not find that File Cabinet.\nWould you like to create it now? (y/n)"+prompt);
			String resp = UserInput.getString().toLowerCase();
			if(resp.equals("y")){
				System.out.println("Creating now...");
				activeFC = new FileCabinet(input);
				addFC(activeFC);
			}
		}
		prompt = activeFC.getFcName()+prompt;
		loadAllDrawers();
	}

	/*
	 * TODO Updated in GUI, needs testing
	 */
	private void newArray(){
		BaseNode bn = null;
		String arrayKey = "";
		String arrayInput = "";
		DefaultMutableTreeNode selectedNode = null;
		if(uiv!=null){
			selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			arrayKey = uiv.keyInput.getText();
			arrayInput = uiv.valueInput.getText();
		} else {
			System.out.printf("Enter the name of the array you want to create:%s\n",">>>");
			arrayKey = UserInput.getString();
			System.out.printf("Enter the first value to add to the array:%s\n", ">>>");
			arrayInput = UserInput.getString();
		}
		/*
		 * Batch entry will need to be handled differently to return one of two ArrayLists, one of Strings or one
		 * of Integers
		 */
		boolean set = false;
		try {
			try{
				Integer i = Integer.parseInt(arrayInput);
				bn = new IntegerArray(arrayKey,i);
				set = true;
			} catch (NumberFormatException e){
				//Do nothing
			}
			if(!set){
				try{
					Double d = Double.parseDouble(arrayInput);
					bn = new DoubleArray(arrayKey,d);
				} catch (NumberFormatException e){
					//Do nothing
				}
			}
		} catch (NumberFormatException e){
			//Do nothing
		}
		if(bn == null){
			bn = new StringArray(arrayKey,arrayInput);
		}
		if(uiv!=null){
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(bn);
			uiv.updateView(newNode, selectedNode);
		} else {
			activeFC.getActivePage().addNodes(bn);
			saveFC();			
		}
	}
	
	//TODO This whole thing needs to be tested after DateNode is more fully developed.
	@SuppressWarnings("unused")
	private void newDateNode(){
		ArrayList<String> validTypes = new ArrayList<String>(Arrays.asList("date","datetime","time"));
		checkNullPage();
		checkNullFolder();
		boolean selectVerify = false;
		String selectType = "";
		while (selectVerify = false){
			System.out.printf("Please select one of the following:\n- date - Only store day, month, and year"
				+ "\n- datetime - Store hours, minutes, seconds, day, month, year"
				+ "\n- time - Only store hour, minutes, seconds");
			selectType = UserInput.getString().toLowerCase();
			if(validTypes.contains(selectType)) selectVerify = true;
		}
		String input = "";
		String inputFormat="";
		if(selectType.equals("date")){
			inputFormat = "NOTE - For month use the numerical equivalent (1 for January, 6 for June):\n"
					+"day:month:year";
		} else if (selectType.equals("time")){
			inputFormat = "hour:minute:second";
		} else {
			inputFormat = "NOTE - For month use the numerical equivalent (1 for January, 6 for June):\n"
					+"hour:minute:second:day:month:year";
		}
		System.out.println("Enter the "+ selectType + " using the following format, "
				+ inputFormat);
		input = UserInput.getString();
		if(ParseCommand.validateDateInput(input)){
			DateNode dn = ParseCommand.parseDate(selectType, input);
			activeFC.getActivePage().addNodes(dn);
		} else {
			System.out.println("The format you provided was incorrect, please try again");
		}
	}
	
	/**
	 * Create a new Drawer in the current FileCabinet and set as active
	 */
	/*
	 * Updated in GUI, needs testing
	 */
	private void newDrawer(){
		if(activeFC != null){
			String input;
			if(uiv==null){
				System.out.printf("Enter the name of the drawer you want to create:\n%s",prompt);
				input = getAction();
			} else {
				input = actionInput;
			}
			//check if that drawer already exists
			Drawer d = new Drawer(input);
			activeFC.addDrawer(d);
			saveFC();
			activeFC.setActiveDrawer(input);
			if(uiv==null){
				prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+">>>";
			} else {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(d);
				uiv.updateView(newNode, selectedNode);
			}
		} else {
			System.out.println("No FileCabinet has been loaded. Please load a FileCabinet");
		}
	}
	
	/**
	 * Create a new FileCabinet and set as active
	 */
	/*
	 * Updated in GUI, needs testing
	 */
	private void newfc(){
		System.out.println("What is the name of the new FileCabinet?"+prompt);
		String newfcName;
		if(uiv==null){
			newfcName = getAction();
		} else {
			newfcName = actionInput;
		}
		System.out.println("newfcName: "+newfcName);;
		if(new File(newfcName).exists()){
			System.out.println("That FileCabinet already exists...");
		} else {
			FileCabinet tempfc = new FileCabinet(newfcName);
			cabinets.add(tempfc);
			LogTracker.sessionStart(tempfc.getFcName());
			tempfc.saveFileCabinet();
			activeFC = tempfc;
			prompt = activeFC.getFcName()+prompt;
			if(uiv!=null){
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(tempfc);
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
				uiv.updateView(newNode, selectedNode);
			}
		}
	}
	
	//Move method to move a page from one drawer and/or folder to another
	//This will also have to take into account FKLs


	/*
	 * This function will not be used in the new GUI, replaced by button
	 */
	private void newFolder(){
		checkNullDrawer();
		String input = "";
		System.out.printf("Enter the name of the folder you want to create:\n%s", prompt);
		input = getAction();
		addFolder(input);
		saveFC();
		activeFC.setActiveFolder(input);
		prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActiveFolder()+">>>";
	}
	
	/**
	 * Create a new Node in the activePage. Capable of handling batch commands.
	 */
	/*
	 * This function will not be used in the new GUI, replaced by button
	 */
	private void newNode(){
		if(uiv==null){
			checkNullPage();
			System.out.println("Enter the name of the key and the value to store seperated\n"
					 + "by a : (colon), and each set of nodes seperated by a ; (semicolon)\n"+prompt);
			ArrayList<BaseNode> temp = ParseCommand.parseCmd();
			if(temp.size()>0){
				for(BaseNode n : temp){
					String tgt = activeFC.getActiveNode().getnKey().toString();
					BaseNode bn = RedirectInput.redirectInput(tgt, n.getnValue().toString());
					if(bn instanceof IntegerNode || bn instanceof DoubleNode){
						System.out.println("Do you want to save this value as plain text?(y/n)");
						if(UserInput.getString().toLowerCase().equals("y")){
							bn = new StringNode(bn.getnKey(), bn.getnValue().toString());
						}
					}
					activeFC.setActiveNode(bn);
					activeFC.getActivePage().addNodes(n);
				}
			}			
		} else {
			String newNodeKey = uiv.keyInput.getText();
			String newNodeValue = uiv.valueInput.getText();
			BaseNode bn = RedirectInput.redirectInput(newNodeKey, newNodeValue);
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			activeFC.setActivePage((Page)selectedNode.getUserObject());
			//Page p = (Page)selectedNode.getUserObject();
			activeFC.getActivePage().addNodes(bn);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(bn);
			uiv.updateView(newNode, selectedNode);
		}
		
		System.out.println("Saving...");
		saveFC();
	}
	
	/**
	 * Create a new Page in the activeDrawer if there is no activeFolder
	 */
	private void newPage(){
		if(activeFC != null && activeFC.getActiveFolder() != null){
			ArrayList<String> temp = ParseCommand.parseFolderInput(activeFC.getActiveFolder());
			ArrayList<String> temp2 = new ArrayList<String>(temp);
			Page p = new Page(activeFC.getActiveDrawer().getFolder(temp).idIncrement());
			activeFC.getActiveDrawer().getFolder(temp2).addPage(p);
			saveFC();
			activeFC.setActivePage(p);
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActiveFolder()+":"+activeFC.getActivePage().getId()+">>>";
		} else if(activeFC != null && activeFC.getActiveDrawer() !=null){
			Page p = new Page(activeFC.getActiveDrawer().idIncrement());
			activeFC.getActiveDrawer().addPage(p);
			saveFC();
			activeFC.setActivePage(p);
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+":"+activeFC.getActivePage().getId()+">>>";
		} else {
			System.out.println("Please set an active FileCabinet and/or Drawer to add a page.");
		}
	}
	
	/**
	 * The main method of the FileCabinetEngine, gets input from the user and calls the appropriate
	 * method. Capable of handling batch instructions.
	 */
	public void run(){
		sendToDisplay("Enter a command. Type exit to exit the program or help for a list of commands");
		String input="";
		String action = "";
		while(!action.equals("exit")){
			sendToDisplay(prompt);
			if(inputList==null){
				input = UserInput.getString();
			}
			if(input.indexOf(":")==-1){
				action = input;
			} else if(inputList!=null){
				action = inputList.get(0);
				inputList.remove(0);
				if(inputList.isEmpty()){
					inputList = null;
				}
			} else {
				inputList = new ArrayList<String>();
				String[] temp = input.split(":");
				for(String s : temp){
					inputList.add(s);
				}
				action = inputList.get(0);
				inputList.remove(0);
				if(inputList.isEmpty()){
					inputList = null;
				}
			}
			execute(action);
		}
		System.out.println("Exit complete...");
		LogTracker.sessionClose(activeFC.getFcName());
	}
	
	/**
	 * Main method of the FileCabinetEngine when running from GUI interface
	 * @param s
	 */
	public void run(String s){
		String action = "";
		if(s.indexOf(":")==-1){
			action = s;
			//uiv.cmds.append(action+"\n");
			execute(action);
		} else if(inputList!=null){
			while(inputList!=null){
				action = inputList.get(0);
				inputList.remove(0);
				if(inputList.isEmpty()){
					inputList = null;
				}
				//uiv.cmds.append(action+"\n");
				execute(action);
			}
		} else {
			inputList = new ArrayList<String>();
			String[] temp = s.split(":");
			for(String s1 : temp){
				inputList.add(s1);
			}
			while(inputList!=null){
				action = inputList.get(0);
				inputList.remove(0);
				if(inputList.isEmpty()){
					inputList = null;
				}
				//uiv.cmds.append(action+"\n");
				execute(action);
			}
		}
		LogTracker.updateLog(activeFC.getFcName(), "#############");
		LogTracker.updateLog(activeFC.getFcName(), "Session closed");
		LogTracker.updateLog(activeFC.getFcName(), "#############");
	}
	
	/**
	 * Save the current FileCabinet
	 */
	/*
	 * This function will not be used in the new GUI, replaced by button
	 */
	private void saveFC(){
		activeFC.saveFileCabinet();
	}
	
	/**
	 * Select a new Drawer to work with 
	 */
	/*
	 * This function will not be used in the new GUI, replaced by user action
	 */
	private void selectDrawer(){
		boolean check = false;
		String input = "";
		sendToDisplay("Enter the name of the Drawer:\n"+prompt);
		input = getAction();
		check = activeFC.setActiveDrawer(input);
		if(check){
			sendToDisplay("Drawer found, setting as activeDrawer");
			clearActivePage();
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+">>>";
		} else {
			sendToDisplay("That drawer either has not been loaded or does not exist");
		}
	}

	/*
	 * This function will not be used in the new GUI, replaced by user action
	 */
	private void selectFolder(){
		boolean check = false;
		checkNullDrawer();
		String input = "";
		sendToDisplay("Enter the name of the Folder (NOTE: Enter full path for subfolders with each Folder name"
				+ "seperated by a pipe (|):\n"+prompt);
		input = getAction();
		check = activeFC.setActiveFolder(input);
		if(check){
			sendToDisplay("Folder found, setting as activeFolder");
			clearActivePage();
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActiveFolder()+">>>";
		} else {
			sendToDisplay("That Folder either has not been loaded or does not exist");
		}
	}
	
	/**
	 * Select a new Node to work with 
	 */
	//Will need to be relooked with new structure
	/*
	 * This function will not be used in the new GUI, replaced by user action
	 */
	private void selectNode(){
		checkNullPage();
		BaseNode result = null;
		String input ="";
		while(input.length()>0){
			sendToDisplay("Enter the name of the field\n>>>");
			input = UserInput.getString();
		}
		result = activeFC.findOneNode(input);
		if(result!=null){
			clearActiveNode();
			activeFC.setActiveNode(result);
			prompt = activeFC.getFcName()+"->"+activeFC.getActiveDrawer().getDrawerName()+"->"+activeFC.getActivePage().getId()+"->"+activeFC.getActiveNode().getnKey().toString()+">>>";
		} else {
			sendToDisplay("No match found");
		}		
	}
	
	/**
	 * Select a new Page to work with
	 */
	/*
	 * TODO Need to update to check if the active level is Drawer or Folder, search for the target
	 * Page in the proper location, then update the prompt accordingly
	 */
	private void selectPage(){
		boolean activeFolder = false;
		if(activeFC.getActiveFolder()!=null){
			checkNullFolder();
			activeFolder = true;
		} else {
			checkNullDrawer();
		}
		int tgt = 0;
		while(tgt == 0){
			sendToDisplay("Enter the Page ID for the Page you want to select\n"+prompt);
			try {
				tgt = Integer.parseInt(getAction());
			} catch (NumberFormatException e) {
				sendToDisplay("Please enter a valid number input");
			}			
		}
		Page temp = activeFC.selectPageByID(tgt, activeFC.getActiveDrawer().getDrawerName());
		if(temp == null){
			System.out.println("wtf");
		} else {
			clearActivePage();
			activeFC.setActivePage(temp);
			prompt = prompt+"->"+activeFC.getActivePage().getId()+">>>";			
		}
	}
	
	private void sendToDisplay(String s){
		if(uiv == null){
			System.out.println(s);
		}		
	}
	
	public void setActionInput(String actionInput) {
		this.actionInput = actionInput;
	}
	
	@SuppressWarnings("unused")
	private void setInputList(String s){
		inputList = new ArrayList<String>();
		String[] temp = s.split(":");
		for(String s1 : temp){
			inputList.add(s1);
		}		
	}
	
	public void setUIV(UserInterfaceView v){
		pd = new PrintDetails(v);
		uiv = v;
	}
	
	/**
	 * Change the nValue of a node
	 */
	private void updateNode(){
		if(activeFC.getActiveNode() != null){
			findAll();
			sendToDisplay("Enter the new value:/n"+prompt);
			String input = UserInput.getString();
			if(UserInput.confirmChange()){
				String tgt = activeFC.getActiveNode().getnKey().toString();
				activeFC.setActiveNode(RedirectInput.redirectInput(tgt, input));
				saveFC();
				findAll();
			} else {
				sendToDisplay("No changes made");
			}
		}
	}
	
	public void setActiveFC(String fcName){
		System.out.println(cabinets.size());
		for(FileCabinet fc : cabinets){
			if(fc.getFcName().equals(fcName)){
				System.out.println("Found");
				activeFC = fc;
				System.out.println("Set");
			}
		}
	}
	
	public FileCabinet getActiveFileCabinet(){
		return activeFC;
	}
}

/*All code below this line is obsolete, but keeping it around for reference*/
//delete the old node from the tree
//DefaultMutableTreeNode tgtNode = ((DefaultMutableTreeNode)uiv.getRoot().getChildAt(fcTgt).getChildAt(drTgt));
//((DefaultMutableTreeNode)uiv.getRoot().getChildAt(fcTgt)).remove(drTgt);
//System.out.println(tgtNode);
//uiv.deleteNode(tgtNode);
//read the updated drawer back into program from file
//Drawer updatedDrawer = ReadWrite.readFromXml(activeFC.getFcName(), tempD.getDrawerName()+".xml");
//load the updated drawer back into the tree at the same location
//((DefaultMutableTreeNode)uiv.getRoot().getChildAt(fcTgt)).insert(new DefaultMutableTreeNode(updatedDrawer), drTgt);