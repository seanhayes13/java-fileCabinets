package com.codinghavoc.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Folder;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.utils.ParseCommand;

/*
 * I guess this and the EastButtonPanel should have their names adjusted since there are no
 * East and West button panels anymore.
 */
public class WestButtonPanel implements ActionListener {
	private UserInterfaceView uiv;

	public WestButtonPanel() {

	}

	public WestButtonPanel(UserInterfaceView v) {
		uiv = v;
	}

	public void setView(UserInterfaceView v) {
		uiv = v;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
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
			if(folders.size()>0){
				uiv.getFCE().getActiveFileCabinet().setActiveFolder(String.join("|", folders));
			} else {
				uiv.getFCE().getActiveFileCabinet().setActiveFolder(null);
			}
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
			} else if (o instanceof BaseNode){				
				uiv.getFCE().execute("deletenodebykey");
				uiv.clearMsg();
				uiv.updateMsg("Page deleted");
			}
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
			if (folders.size() > 0) { //if there are super folders
				StringBuilder sb = new StringBuilder();
				sb.append(folders.get(0));
				if (folders.size() > 1) {
					for (int i = 1; i < folders.size(); i++) {
						sb.append("|" + folders.get(i));
					}
				}
				uiv.fc.setActiveFolder(sb.toString());
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
		ArrayList<String> folders = new ArrayList<>();
		for (TreeNode n : tn) {
			if (((DefaultMutableTreeNode) n).getUserObject() instanceof Folder) {
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
