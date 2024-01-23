package com.codinghavoc.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.codinghavoc.gui.actionListeners.BuildRelSelect;
import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Folder;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.utils.ReadWrite;

@SuppressWarnings("serial")
/*
 * This frame is now part of the main frame and is obsolete
 */
public class BuildRelationshipPopup extends JFrame{
	
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 500;
	
	private UserInterfaceView uiv;

	public DefaultTreeModel model;

	private FileCabinet fc;
	
	private JButton choose;
	
	private JFrame frame;
	
	private JLabel farListInputLabel;
	
	private JPanel listInputPanel;
	
	private JScrollPane scrollPane;
	
	public JTextField farListInput;
	
	public JTree tree;
	
	private TreeNode root;

	public BuildRelationshipPopup(UserInterfaceView u){
		uiv = u;
	}
	
//	public BuildRelationshipPopup(FileCabinet f, UserInterfaceView u){
//		fc = f;
//		uiv = u;
//	}
	
	public void display(){
		choose = new JButton("Select target");
		//choose.addActionListener(new BuildRelSelect(uiv, this));
		
		buildTree();
		
		scrollPane = new JScrollPane(tree);
		listInputPanel = new JPanel();
		
		farListInputLabel = new JLabel("Far side list");
		farListInput = new JTextField(10);
		listInputPanel.add(farListInputLabel);
		listInputPanel.add(farListInput);
		
		frame = new JFrame("Build Relationship GUI");
		frame.add(listInputPanel,BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(choose, BorderLayout.SOUTH);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		frame.setVisible(true);
		
	}
	
	private void buildTree(){
		tree = new JTree(model);
		root = buildTempTree();
		model = new DefaultTreeModel(root);
		
		tree = new JTree(model);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	public TreeNode buildTempTree(){
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) uiv.tree.getLastSelectedPathComponent();
		TreeNode[] fullPath = selectedNode.getPath();
		DefaultMutableTreeNode fcNode = (DefaultMutableTreeNode) fullPath[1];
		String fcName = ((FileCabinet)fcNode.getUserObject()).getFcName();
		fc = buildFC(fcName);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(fc);

		DefaultMutableTreeNode newDrawer;
		DefaultMutableTreeNode newFolder;
		DefaultMutableTreeNode newPage;
		DefaultMutableTreeNode newBaseNode;
		for (Drawer d : fc.getDrawerList()) {
			newDrawer = new DefaultMutableTreeNode(d);
			if (d.getFolders() != null) {
				for (Folder f : d.getFolders()) {
					newFolder = new DefaultMutableTreeNode(f);
					if(f.getPages()!=null){
						for (Page p : f.getPages()) {
							newPage = new DefaultMutableTreeNode(p);
							if(p.getNodes()!=null){
								for (BaseNode bn : p.getNodes()) {
									newBaseNode = new DefaultMutableTreeNode(bn);
									newBaseNode.setAllowsChildren(false);
									newPage.add(newBaseNode);
								}
							}
							newFolder.add(newPage);
						}									
					}
					newDrawer.add(newFolder);
				}
			}
			if (d.getPages() != null) {
				for (Page p : d.getPages()) {
					newPage = new DefaultMutableTreeNode(p);
					if(p.getNodes()!=null){
						for (BaseNode bn : p.getNodes()) {
							newBaseNode = new DefaultMutableTreeNode(bn);
							newPage.add(newBaseNode);
						}
					}
					newDrawer.add(newPage);
				}
			}
			root.add(newDrawer);
		}
		return root;
	}
	
	private FileCabinet buildFC(String fc){
		FileCabinet result = new FileCabinet();
		result.setFCName(fc);
		File dir = new File("fcRepo\\"+fc);
		File[] files = dir.listFiles();
		for(File f : files){
			if (!f.getName().equals("actionLog.txt") && !f.getName().equals("drawerConfigs.xml")
					&& !f.getName().equals("fcConfig.xml")) {
				Drawer d = ReadWrite.readFromXml(fc, f.getName());
				result.addDrawer(d);
			}
		}
		return result;
	}
	
	public void closeFrame(){
		frame.dispose();
	}
}
