package com.codinghavoc.gui.actionListeners;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Folder;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.FKLNode;
import com.codinghavoc.utils.ForeignLink;
import com.codinghavoc.utils.ParseCommand;
import com.codinghavoc.utils.ReadWrite;
import com.codinghavoc.utils.RelationshipItem;

public class GUIActions implements ActionListener{
	private UserInterfaceView uiv;
	
	public GUIActions(UserInterfaceView u){
		uiv = u;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		if(action.equals("Set Relationship")){
			DefaultMutableTreeNode brpSelectedNode = (DefaultMutableTreeNode) uiv.farSideTree.getLastSelectedPathComponent();
			DefaultMutableTreeNode uivSelectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			String farList = "";
			if(!uiv.farListInput.getText().equals("")){
				farList = uiv.farListInput.getText();
			} else {
				farList = "noName";
			}
			uiv.buildRs(brpSelectedNode, farList,"target");
			System.out.println("printing full rs");
			System.out.println(uiv.getRs().toString());
			RelationshipItem tgt = uiv.getRs().getTgtRI();
			RelationshipItem src = uiv.getRs().getSrcRI();
			TreeNode[] uivTn = uivSelectedNode.getPath();
			String nFCName = ((FileCabinet)((DefaultMutableTreeNode) uivTn[1]).getUserObject()).getFcName();
			String fFCName = ((FileCabinet)((DefaultMutableTreeNode) brpSelectedNode.getRoot()).getUserObject()).getFcName();
			System.out.println("near fc name: "+nFCName);
			System.out.println("far fc name: "+fFCName);
			Drawer nearD = (Drawer)((DefaultMutableTreeNode) uivTn[2]).getUserObject();
			FKLNode nearFKL = null;
			boolean nearSet = false;
			boolean nearFolderCheck = false;
			ArrayList<String> temp3 = new ArrayList<>();
			//check if the drawer/folder/page in question has an fklnode with the same name
			ForeignLink fl = new ForeignLink(tgt.getDrawerName(),tgt.getFolderName(),tgt.getPageID());
			if(src.getFolderName()==null || src.getFolderName().equals("")){ //adding directly to source drawer
				System.out.println("1-A");
				if(nearD.getPage(src.getPageID()).contain(src.getListName())){ //if an node with the same name exists
					System.out.println("1-A-1");
					((FKLNode)(nearD.getPage(src.getPageID()).selectNode(src.getListName()))).addLink(fl);
					nearSet = true;
					System.out.println("near side already exists");
				}
			} else { //adding to folder in source drawer
				System.out.println("1-B");
				nearFolderCheck = true;
				ArrayList<String> temp = new ArrayList<>(ParseCommand.parseFolderInput(src.getFolderName()));
				ArrayList<String> temp2 = new ArrayList<>(temp);
				temp3 = new ArrayList<>(temp);
				if(nearD.getFolder(temp).getPage(src.getPageID()).contain(src.getListName())
						&& nearD.getFolder(temp).getPage(src.getPageID()).selectNode(src.getListName()) instanceof FKLNode){ //if the near side has an fkl node
					System.out.println("1-B-1");
					((FKLNode)(nearD.getFolder(temp2).getPage(tgt.getPageID()).selectNode(src.getListName()))).addLink(fl);
					nearSet = true;
					System.out.println("near side already exists");
				}
			}
			/*
			 * This method and the opposite method for the farside will need to be looked at
			 * to make sure that the filecabinet, drawer and/or folder are properly updated
			 */
			if(!nearSet){ //No fkl node exists, create new node
				System.out.println("1-C");
				nearFKL = new FKLNode(src.getListName(),fl);
				DefaultMutableTreeNode newUIVNode = new DefaultMutableTreeNode(nearFKL);
				Page p = (Page)uivSelectedNode.getUserObject();
				if(nearFolderCheck){
					System.out.println("alpha");
					nearD.getFolder(temp3).getPage(p.getId()).addNodes((FKLNode)newUIVNode.getUserObject());
				} else {
					System.out.println("bravo");
					nearD.getPage(p.getId()).addNodes((FKLNode)newUIVNode.getUserObject());
				}
				uiv.updateView(newUIVNode, uivSelectedNode);
				System.out.println("nearFKL: "+nearFKL.toString());
			}
			TreeNode[] brpTn = brpSelectedNode.getPath();
			Drawer farD = (Drawer)((DefaultMutableTreeNode) brpTn[1]).getUserObject();
			boolean sameDrawer = false;
			if(nearD.getDrawerName().equals(farD.getDrawerName())){
				//set farD equal to nearD
				farD = new Drawer(nearD);
				sameDrawer = true;
				//don't save yet, wait until after making changes to the 'farD'
			} else {
				//save nearD since the two Drawers are different
				ReadWrite.writeToXml(nFCName, nearD);
			}
			FKLNode farFKL = null;
			boolean farSet = false;
			boolean farFolderCheck = false;
			//check if the drawer/folder/page in question has an fklnode with the same name
			fl = new ForeignLink(src.getDrawerName(),src.getFolderName(),src.getPageID());
			if(tgt.getFolderName()==null || tgt.getFolderName().equals("")){ //adding directly to drawer on far side
				System.out.println("2-A");
				if(farD.getPage(tgt.getPageID()).contain(farList) 
						&& farD.getPage(tgt.getPageID()).selectNode(farList) instanceof FKLNode){ //if an node with the same name exists
					System.out.println("2-A-1");
					((FKLNode)(farD.getPage(tgt.getPageID()).selectNode(farList))).addLink(fl);
					farSet = true;
					System.out.println("far side already exists");
				}
			} else { //add to folder within drawer on far side
				System.out.println("2-B");
				farFolderCheck = true;
				ArrayList<String> temp = new ArrayList<>(ParseCommand.parseFolderInput(tgt.getFolderName()));
				ArrayList<String> temp2 = new ArrayList<>(temp);
				temp3 = new ArrayList<>(temp);
				if(farD.getFolder(temp).getPage(tgt.getPageID()).contain(tgt.getListName())){ //check if far side folder has a node
					System.out.println("2-B-1");
					((FKLNode)(farD.getFolder(temp2).getPage(tgt.getPageID()).selectNode(farList))).addLink(fl);
					farSet = true;
					System.out.println("far side already exists");
				}
			}
			if(!farSet){ // create node from scratch
				System.out.println("2-C");
				farFKL = new FKLNode(farList,fl);
				DefaultMutableTreeNode newBRPNode = new DefaultMutableTreeNode(farFKL);
				TreeNode[] tn = brpSelectedNode.getPath();
				if(sameDrawer){
					if(farFolderCheck){
						System.out.println("alpha");
						farD.getFolder(temp3).getPage(src.getPageID()).addNodes((FKLNode)newBRPNode.getUserObject());
					} else {
						System.out.println("bravo");
						farD.getPage(src.getPageID()).addNodes((FKLNode)newBRPNode.getUserObject());
					}
					DefaultMutableTreeNode placeholder = setRelationship(tn, newBRPNode,farFolderCheck);
					uiv.updateView(newBRPNode, placeholder);
					ReadWrite.writeToXml(nFCName, farD);
				} else {
					DefaultMutableTreeNode dest = setRelationship(tn, newBRPNode,farFolderCheck);
					uiv.updateView(newBRPNode,dest);
				}
				System.out.println("farFKL: "+farFKL.toString());
			} else {
				ReadWrite.writeToXml(nFCName, farD);
			}
			//brp.closeFrame();
			//need to save each drawer after updating
		}
		// Delete
		if (action.equals("Delete")) {
			// Pop up to confirm delete
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			TreeNode[] fullPath = selectedNode.getPath();
			Object o = selectedNode.getUserObject();
			uiv.getFCE().setActiveFC(getFC(fullPath).getFcName());
			Drawer d = getDrawer(fullPath);
			uiv.getFCE().getActiveFileCabinet().setActiveDrawer(d);
			ArrayList<String> folders = getFolderList(fullPath);
			if(folders != null){
				uiv.getFCE().getActiveFileCabinet().setActiveFolder(String.join("|", folders));
			} /*else {
				uiv.getFCE().getActiveFileCabinet().setActiveFolder(null);
			}*/
			Page p = getPage(fullPath);
			uiv.getFCE().getActiveFileCabinet().setActivePage(p);
			if(o instanceof Drawer){
				uiv.getFCE().execute("deletedrawer");
				uiv.clearMsg();
				uiv.updateMsg("Drawer deleted");
			} else if (o instanceof Folder){
				uiv.getFCE().execute("deletefolder");
				uiv.clearMsg();
				uiv.updateMsg("Folder deleted");
			} else if (o instanceof Page){
				uiv.getFCE().execute("deletepage");
				uiv.clearMsg();
				uiv.updateMsg("Page deleted");
			} else if (o instanceof BaseNode){				
				uiv.getFCE().execute("deletenodebykey");
				uiv.clearMsg();
				uiv.updateMsg("Node deleted");
			}
			ReadWrite.writeToXml(uiv.getFCE().getActiveFileCabinet().getFcName(),d);
//			uiv.setRoot(null);
//			uiv.buildTree();
//			uiv.model.reload();
//			uiv.tree.repaint();
//			uiv.repaint();
//			uiv.repaintFrame();
			
			
			/*
			 * Add logic here to update the drawer
			 * 
			 * Will need to determine when and where to update drawer
			 * if a ForeignLink is deleted
			 */
		}
		// Add FileCabinet
		if (action.equals("Add FileCabinet")) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;
			if(!uiv.input.getText().isEmpty()){
				uiv.getFCE().setActionInput(uiv.input.getText());
				uiv.getFCE().execute("newfc");
			}

		}
		// Add Drawer
		if (action.equals("Add Drawer")) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;
			if(!uiv.input.getText().isEmpty()){
				String fcName = ((FileCabinet)selectedNode.getUserObject()).getFcName();
				uiv.getFCE().setActiveFC(fcName);
				uiv.getFCE().setActionInput(uiv.input.getText());
				uiv.getFCE().execute("newdrawer");
			}
		}
		// Add Folder
		//TODO Need to debug this to see if this method is really needed or if I can adjust the
		//the FCE methods
		if (action.equals("Add Folder")) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;
			Object o = selectedNode.getUserObject();
			TreeNode[] fullPath = selectedNode.getPath();
			Drawer d = getDrawer(fullPath);
			ArrayList<String> folders = getFolderList(fullPath);
			uiv.fc.setActiveDrawer(d);
			//TODO there is a problem with folders
			if(folders != null){
			if (folders.size() > 0 ) { //if there are super folders
				StringBuilder sb = new StringBuilder();
				sb.append(folders.get(0));
				if (folders.size() > 1) {
					for (int i = 1; i < folders.size(); i++) {
						sb.append("|" + folders.get(i));
					}
				}
				uiv.fc.setActiveFolder(sb.toString());
			}
		 } //else no super folders, adding directly to drawer
			DefaultMutableTreeNode newNode = null;
			String name = uiv.input.getText();
			Folder newFolder = new Folder(name);
			/*
			 * Again, need to check that a folder at the selected level with that name
			 * does not already exist
			 */
			if (o instanceof Folder) { //if the currently selected object is a folder
//				if (uiv.fc.getActiveFolder() != null) { //if
//					uiv.fc.getActiveDrawer().getFolder(folders).addFolder(newFolder);
//				} else {
//					uiv.fc.getActiveDrawer().addFolder(newFolder);
//				}
				uiv.fc.getActiveDrawer().getFolder(folders).addFolder(newFolder);
				newNode = new DefaultMutableTreeNode(newFolder);
			} else { // Adding this folder directly to a Drawer
				uiv.fc.getActiveDrawer().addFolder(newFolder);
				newNode = new DefaultMutableTreeNode(newFolder);
			}
			uiv.updateView(newNode, selectedNode);
		}
		// Add Page
		if (action.equals("Add Page")) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;
			Object o = selectedNode.getUserObject();
			TreeNode[] fullPath = selectedNode.getPath();
			Drawer d = getDrawer(fullPath);
			ArrayList<String> folders = getFolderList(fullPath);
//			for (TreeNode n : fullPath) {
//				if (((DefaultMutableTreeNode) n).getUserObject() instanceof Drawer) {
//					d = (Drawer) ((DefaultMutableTreeNode) n).getUserObject();
//				} else if (((DefaultMutableTreeNode) n).getUserObject() instanceof Folder) {
//					Folder temp = (Folder) ((DefaultMutableTreeNode) n).getUserObject();
//					folders.add(temp.getFolderName());
//				}
//			}
			uiv.fc.setActiveDrawer(d);
			DefaultMutableTreeNode newNode = null;
			if (folders.size() > 0) { //if adding page to a folder
				StringBuilder sb = new StringBuilder();
				sb.append(folders.get(0));
				if (folders.size() > 1) {
					for (int i = 1; i < folders.size(); i++) {
						sb.append("|" + folders.get(i));
					}
				}
				uiv.fc.setActiveFolder(sb.toString());
				
				//TODO Need to relook this section, if folders is greater than 0, that means the
				//page is being added to a folder
				if (o instanceof Folder) {
					ArrayList<String> temp = ParseCommand.parseFolderInput(uiv.fc.getActiveFolder());
//					ArrayList<String> temp2 = new ArrayList<String>(temp);
					Page p = new Page(uiv.fc.getActiveDrawer().getFolder(temp).idIncrement());
					uiv.fc.getActiveDrawer().getFolder(temp).addPage(p);
					newNode = new DefaultMutableTreeNode(p);
				} else { // Drawer - this section should be irrelevant
					int id = uiv.fc.getDrawer(d.getDrawerName()).idIncrement();
					Page p = new Page(id);
					uiv.fc.getActiveDrawer().addPage(p);
					newNode = new DefaultMutableTreeNode(p);
				}
			} else { //adding page to drawer
				int id = uiv.fc.getActiveDrawer().idIncrement();
				Page p = new Page(id);
				uiv.fc.getActiveDrawer().addPage(p);
				newNode = new DefaultMutableTreeNode(p);				
			}
			uiv.updateView(newNode, selectedNode);
		}
		
		// Add Node
		if (action.equals("Add Node")) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			TreeNode[] fullPath = selectedNode.getPath();
			String fcName = ((FileCabinet) ((DefaultMutableTreeNode) fullPath[1]).getUserObject()).getFcName();
			uiv.getFCE().setActiveFC(fcName);
			uiv.getFCE().getActiveFileCabinet().setActivePage((Page)selectedNode.getUserObject());
			uiv.getFCE().execute("newnode");
		}
		
		if(action.equals("Add Array")){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			TreeNode[] path = selectedNode.getPath();
			String fcName = ((FileCabinet)((DefaultMutableTreeNode)path[1]).getUserObject()).getFcName();
			System.out.println("fcName: "+ fcName);
			uiv.getFCE().setActiveFC(fcName);
			//System.out.println("Setting activeFC: " + uiv.getFCE().getActiveFileCabinet().getFcName());
			Object o = selectedNode.getUserObject();
			//if a Page is selected, create and add a new array node
			if(o instanceof Page){
				uiv.getFCE().execute("newarray");	
			} else { //if an array node is selected, get input and attempt to add to the array node
				BaseNode bn = (BaseNode)selectedNode.getUserObject();
				uiv.getFCE().getActiveFileCabinet().setActiveNode(bn);
				uiv.getFCE().execute("addtoarray");
			}
		}
		
		if(action.equals("Convert to Array")){
			uiv.getFCE().execute("convertoarray");
		}
		
		if(action.equals("Build Relationship")){
			uiv.buildFarSideTree();
			uiv.initRs();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			uiv.buildRs(selectedNode, uiv.nearListInput.getText(), "source");
		}
		
		if(action.equals("Delete Array Item")){
			uiv.getFCE().setActionInput(uiv.valueInput.getText());
			uiv.getFCE().execute("deletefromarray");
		}
		
		if(action.equals("Save")){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
			//System.out.println("-->"+((FileCabinet)selectedNode.getUserObject()).getFcName());
			for(int i = 0; i < selectedNode.getChildCount();i++){
				DefaultMutableTreeNode temp = (DefaultMutableTreeNode) selectedNode.getChildAt(i);
				Drawer d = (Drawer) temp.getUserObject();
				System.out.println("---->"+d.getDrawerName());
				ReadWrite.writeToXml(((FileCabinet)selectedNode.getUserObject()).getFcName(), d);
			}
		}
		
	}
	
	/**
	 * 
	 * @param tn Path to the target
	 * @param newNode New Node to add to the tree
	 */
	private DefaultMutableTreeNode setRelationship(TreeNode[] tn, DefaultMutableTreeNode newNode, boolean folder){
		DefaultMutableTreeNode result = null;
		String fFCName = ((FileCabinet)((DefaultMutableTreeNode) tn[0]).getUserObject()).getFcName();
		Drawer farD = (Drawer)((DefaultMutableTreeNode) tn[1]).getUserObject();
		TreeModel tempRoot = uiv.tree.getModel();
		boolean search = true;
		int t = 0;
		while(search){
			int childCount = tempRoot.getChildCount(uiv.getRoot());
			for(int i = 0; i < childCount; i++){
				if(((DefaultMutableTreeNode)tempRoot.getChild(uiv.getRoot(), i)).toString().equals(tn[t].toString())){
					DefaultMutableTreeNode temp = (DefaultMutableTreeNode)tempRoot.getChild(uiv.getRoot(), i);
					t++;
					//DefaultMutableTreeNode dest = getNode(tn, t, temp);
					result = getNode(tn, t, temp);
					Page p = (Page)result.getUserObject();
					if(folder){
						ArrayList<String> folders = new ArrayList<>();
						for (TreeNode n : tn) { // iterate through the path
							if (((DefaultMutableTreeNode) n).getUserObject() instanceof Folder) { //build the super folder list
								Folder tempFolder = (Folder) ((DefaultMutableTreeNode) n).getUserObject();
								folders.add(tempFolder.getFolderName());
							}
						}
						farD.getFolder(folders).getPage(p.getId()).addNodes((FKLNode)newNode.getUserObject());
					} else {
						farD.getPage(p.getId()).addNodes((FKLNode)newNode.getUserObject());
					}
					//uiv.updateView(newNode,dest);
					search = false;
					ReadWrite.writeToXml(fFCName, farD);
					break;
				}
			}
			search = false;
		}
		return result;
	}
	
	private DefaultMutableTreeNode getNode(TreeNode[] tn, int t, DefaultMutableTreeNode working){
		int i = 0;
		int workingCount = working.getChildCount();
		DefaultMutableTreeNode result = null;
		if(working.getUserObject() instanceof Page){
			return working;
		} else {
			for(int j = 0; i < workingCount; j++){
				if(working.getChildAt(j).toString().equals(tn[t].toString())){ //if we have a match, dig deeper
					t++;
					result = getNode(tn,t, (DefaultMutableTreeNode)working.getChildAt(j));
					break;
				}
			}
			return result;
		}
	}
	
	private FileCabinet getFC(TreeNode[] tn){
		FileCabinet fc = null;
		for (TreeNode n : tn) {
			if (((DefaultMutableTreeNode) n).getUserObject() instanceof FileCabinet) {
				fc = (FileCabinet) ((DefaultMutableTreeNode) n).getUserObject();
				return fc;
			}
		}
		return fc;
	}
	
	private Drawer getDrawer(TreeNode[] tn){
		Drawer d = null;
		for (TreeNode n : tn) {
			if (((DefaultMutableTreeNode) n).getUserObject() instanceof Drawer) {
				d = (Drawer) ((DefaultMutableTreeNode) n).getUserObject();
				return d;
			}
		}
		return d;
	}
	
	private ArrayList<String> getFolderList(TreeNode[] tn){
		ArrayList<String> folders = null;
		for (TreeNode n : tn) {
			if (((DefaultMutableTreeNode) n).getUserObject() instanceof Folder) {
				if(folders == null) folders = new ArrayList<>();
				Folder temp = (Folder) ((DefaultMutableTreeNode) n).getUserObject();
				folders.add(temp.getFolderName());
			}
		}
		return folders;
	}
	
	private Page getPage(TreeNode[] tn){
		Page p = null;
		for (TreeNode n : tn) {
			if (((DefaultMutableTreeNode) n).getUserObject() instanceof Page) {
				p = (Page)((DefaultMutableTreeNode)n).getUserObject();
			}
		}
		return p;
	}
}
