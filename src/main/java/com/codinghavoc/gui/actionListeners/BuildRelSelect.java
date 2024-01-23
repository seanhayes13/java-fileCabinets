package com.codinghavoc.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import com.codinghavoc.gui.BuildRelationshipPopup;
import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Folder;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.FKLNode;
import com.codinghavoc.utils.ForeignLink;
import com.codinghavoc.utils.ParseCommand;
import com.codinghavoc.utils.ReadWrite;
import com.codinghavoc.utils.RelationshipItem;

public class BuildRelSelect implements ActionListener{
	private UserInterfaceView uiv;
	//private BuildRelationshipPopup brp;
	
	public BuildRelSelect(){
		
	}
	
//	public BuildRelSelect(UserInterfaceView v, BuildRelationshipPopup b){
//		uiv = v;
//		brp = b;
//	}
	
	public BuildRelSelect(UserInterfaceView v){
		uiv = v;
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
	}
	
	/**
	 * 
	 * @param tn Path to the target
	 * @param newNode New Node to add to the tree
	 */
	public DefaultMutableTreeNode setRelationship(TreeNode[] tn, DefaultMutableTreeNode newNode, boolean folder){
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
}