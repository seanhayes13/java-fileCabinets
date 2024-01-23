package com.codinghavoc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.codinghavoc.gui.actionListeners.BuildRelSelect;
import com.codinghavoc.gui.actionListeners.EastButtonPanel;
import com.codinghavoc.gui.actionListeners.GUIActions;
import com.codinghavoc.gui.actionListeners.WestButtonPanel;
import com.codinghavoc.main.FileCabinetEngine;
import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Folder;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.arrays.*;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.FKLNode;
import com.codinghavoc.utils.ForeignLink;
import com.codinghavoc.utils.ParseCommand;
import com.codinghavoc.utils.ReadWrite;
import com.codinghavoc.utils.RelationshipItem;
import com.codinghavoc.utils.RelationshipStruct;

@SuppressWarnings("serial")
public class UserInterfaceView extends JFrame {

	protected FileCabinetEngine fce;
	private static final int DEFAULT_WIDTH = 900;
	private static final int DEFAULT_HEIGHT = 550;
	
	private ArrayList<JComponent> actionButtons = new ArrayList<>();
	private ArrayList<String> turnOnList;
	private RelationshipStruct rs;

	public DefaultTreeModel model;
	public DefaultTreeModel farSideModel;
	
//	private EastButtonPanel ebp;
//	private WestButtonPanel wbp;
//	private BuildRelSelect brs;
	private GUIActions ga;

	private JFrame frame;

	private JButton deleteButton;
	private JButton addFC;
	private JButton addDrawer;
	private JButton addFolder;
	private JButton addPage;
	private JButton addNode;
	private JButton addArray;
	private JButton buildRel;
	private JButton delArrayItem;
	private JButton saveBtn;
	private JButton farSideSelect;
	
	private JCheckBox saveAsString;

	private JLabel regInputLabel;
	private JLabel keyInputLabel;
	private JLabel valueInputLabel;
	private JLabel msgLabel;
	private JLabel nearListInputLabel;
	private JLabel farListInputLabel;
	private JLabel nearSideLinkLabel;
	//private JLabel farSideLinkLabel;

	private JPanel masterPanel;
	private JPanel inputPanel; //TODO replace with JToolBar
	private JPanel treePanel;
	private JPanel msgPanel;
	private JPanel btnPanel;
	private JPanel nearSideInfoPanel;
	private JPanel farSideInfoPanel;
	private JPanel farSideInputPanel;

	private JScrollPane scrollPane;
	private JScrollPane scrollPaneRight;

	/*
	 * TODO possibility of removing keyboard input from the main frame (this)
	 * and using popups to get information from user. Popups would start with 
	 * the Submit/Create button disabled then use validation of user input to
	 * decide whether to enable the button, allowing them to submit the request
	 */
	public JTextField input;
	public JTextField keyInput;
	public JTextField valueInput;
	public JTextField nearListInput;
	public JTextField farListInput;

	public JTree tree;
	public JTree farSideTree;

	private TreeNode root;
	private TreeNode farSideRoot;

	public FileCabinet fc;

	public UserInterfaceView() {
//		ebp = new EastButtonPanel(this);
//		wbp = new WestButtonPanel(this);
//		brs = new BuildRelSelect(this);
		ga = new GUIActions(this);
	}

	public UserInterfaceView(FileCabinetEngine e) {
//		ebp = new EastButtonPanel(this);
//		wbp = new WestButtonPanel(this);
//		brs = new BuildRelSelect(this);
		ga = new GUIActions(this);
		fce = e;
	}

	public void display() {
		inputPanel = new JPanel();

		regInputLabel = new JLabel("Input");
		input = new JTextField(40);
		keyInputLabel = new JLabel("Key");
		keyInputLabel.setVisible(false);
		valueInputLabel = new JLabel("Value");
		valueInputLabel.setVisible(false);
		keyInput = new JTextField(15);
		keyInput.setVisible(false);
		valueInput = new JTextField(25);
		valueInput.setVisible(false);
		saveAsString = new JCheckBox("Save as plain text");
		saveAsString.setVisible(false);
		inputPanel.add(regInputLabel);
		inputPanel.add(input);
		inputPanel.add(keyInputLabel);
		inputPanel.add(keyInput);
		inputPanel.add(valueInputLabel);
		inputPanel.add(valueInput);
		inputPanel.add(saveAsString);
		
		msgLabel = new JLabel();
		msgPanel = new JPanel();
		msgPanel.add(msgLabel);

//		buildButtonWest();
//		buildButtonEast();
		buildBtnPanel();
		buildTree();
		
		//build nearSide
		nearSideInfoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints nearGbc = new GridBagConstraints();
		nearGbc.gridy = 0;
		nearGbc.insets = new Insets(0,20,0,10);
		nearGbc.fill = GridBagConstraints.HORIZONTAL;
		nearGbc.gridwidth = 1;
		nearSideLinkLabel = new JLabel("Active FileCabinet Repository");
		scrollPane = new JScrollPane(tree);
		Dimension dim = new Dimension();
		dim.setSize(((DEFAULT_WIDTH)/2) - 50, 250);
		scrollPane.setMinimumSize(dim);
		nearSideInfoPanel.add(nearSideLinkLabel, nearGbc);
		nearGbc.gridy++;
		nearGbc.insets = new Insets(0,20,12,10);
		nearSideInfoPanel.add(scrollPane,nearGbc);
		
		//build farSide
		farSideInfoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints farGbc = new GridBagConstraints();
		farGbc.gridy=0;
		farGbc.fill = GridBagConstraints.HORIZONTAL;
		farGbc.gridwidth = 1;
		farGbc.insets = new Insets(0,10,0,20);
		
		farSideInputPanel = new JPanel();
		farListInputLabel = new JLabel("");
		farListInput = new JTextField(20);
		farListInput.setEnabled(false);
		farListInput.setText("Disabled");
		farSideInputPanel.add(farListInputLabel);
		farSideInputPanel.add(farListInput);
		
		farSideInfoPanel.add(farSideInputPanel, farGbc);
		farGbc.gridy++;
		
		farSideTree = new JTree(farSideModel);
		scrollPaneRight = new JScrollPane(farSideTree);
		scrollPaneRight.setViewportView(farSideTree);
		scrollPaneRight.setMinimumSize(dim);
		scrollPaneRight.setEnabled(false);
		farSideInfoPanel.add(scrollPaneRight, farGbc);
		farGbc.gridy++;
		
		farSideSelect = new JButton("Set Relationship");
		farSideSelect.addActionListener(ga);
		farSideInfoPanel.add(farSideSelect,farGbc);
		
		//build treePanel to hold both nearSide and farSide InfoPanels
		treePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbcTree = new GridBagConstraints();
		gbcTree.gridx = 0;
		gbcTree.gridy = 0;
		gbcTree.fill = GridBagConstraints.HORIZONTAL;
		gbcTree.gridwidth = 1;
		
		treePanel.add(nearSideInfoPanel,gbcTree);
		gbcTree.gridx++;
		
		treePanel.add(farSideInfoPanel,gbcTree);

		frame = new JFrame("FileCabinet");
		masterPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 0;
		gbc.ipady = 20;
		gbc.gridwidth = 1;
		
		masterPanel.add(inputPanel, gbc);
		gbc.gridy++;
		
		masterPanel.add(treePanel, gbc);
		gbc.gridy++;
		
		masterPanel.add(msgPanel, gbc);
		gbc.gridy++;
		
		masterPanel.add(btnPanel, gbc);
		gbc.gridy++;
		frame.add(masterPanel);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 100);
		frame.setVisible(true);
	}
	
	public void repaintFrame(){
		masterPanel.repaint();
		frame.repaint();
	}

	public void buildTree() {
		tree = null;
		model = null;
		root = null;
		tree = new JTree(model);
		setRoot(populateTree());
		model = new DefaultTreeModel(getRoot());

		tree = new JTree(model);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			/*
			 * TODO Need to update this so that when the user has selected an element from a foreign link
			 * all buttons are disabled
			 */
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				//System.out.println("You've selected: " + selected.toString());
//				for(int i = 0; i < root.getChildCount(); i++){
//					for(int j = 0; j < root.getChildAt(i).getChildCount(); j++){
//						if(root.getChildAt(i).getChildAt(j).toString().contains("Drawer")){
//							System.out.println("Bingo");
//						}
//					}
//				}
				if (selected != null) {
					Object o = selected.getUserObject();
					if (o instanceof String) {
						turnOnList = new ArrayList<>(Arrays.asList("Add FileCabinet"));
						changeEnabledBtns(turnOnList);
					} else if (o instanceof FileCabinet) {
						fc = (FileCabinet) o;
						updateInput(false);
						turnOnList = new ArrayList<>(Arrays.asList("Add Drawer", "Save"));
						changeEnabledBtns(turnOnList);
					} else if (o instanceof Drawer) {
						updateInput(false);
						turnOnList = new ArrayList<>(Arrays.asList("Add Folder", "Add Page"));
						changeEnabledBtns(turnOnList);
					} else if (o instanceof Folder) {
						updateInput(false);
						turnOnList = new ArrayList<>(Arrays.asList("Add Folder", "Add Page"));
						changeEnabledBtns(turnOnList);
					} else if (o instanceof Page) {
						updateInput(true);
						turnOnList = new ArrayList<>(Arrays.asList("Add Node", "Build Relationship",
								"Add Array"));
						changeEnabledBtns(turnOnList);
					} else if (o instanceof BaseNode) {
						if(o instanceof StringArray 
								|| o instanceof IntegerArray 
								|| o instanceof DoubleArray){
							System.out.println(o.getClass());
							updateInput(true);
							turnOnList = new ArrayList<>(Arrays.asList("Add Array", "Delete Array Item"));
							changeEnabledBtns(turnOnList);
						} else {
							updateInput(false);
							turnOnList = new ArrayList<>();
							changeEnabledBtns(turnOnList);
						}
					} else {
						System.out.println("Did we forget to program an option?");
					}
				}
			}
		});
	}
	
	public void buildFarSideTree(){
		scrollPaneRight.setEnabled(true);
		nearSideLinkLabel.setEnabled(true);
		nearSideLinkLabel.setText("Near Side Link - Activated");
		//farSideTree = new JTree(farSideModel);
		farSideRoot = buildTempTree();
		farSideModel = new DefaultTreeModel(farSideRoot);
		
		farSideTree = new JTree(farSideModel);
		farSideTree.setEditable(false);
		farSideTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		farListInputLabel.setText("Far side list name:");
		farListInput.setEnabled(true);
		farListInput.setText(null);
		scrollPaneRight.setViewportView(farSideTree);
		scrollPaneRight.revalidate();
		scrollPaneRight.repaint();
	}
	
	public TreeNode buildTempTree(){
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
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
	
	private void buildBtnPanel(){
		btnPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 0;
		gbc.ipady = 20;
		gbc.gridwidth = 1;
		
		//currently have 10 buttons; setting up for 5 columns
		addFC = new JButton("Add FileCabinet");
		actionButtons.add(addFC);
		addFC.setEnabled(false);
		/*
		 * Each of these will need to follow a similar path to the ones above
		 * for the listener, or move everything to an actual event class, or
		 * several to break up code.
		 */
		addFC.addActionListener(ga);
		btnPanel.add(addFC, gbc);
		gbc.gridx++; //1

		addDrawer = new JButton("Add Drawer");
		actionButtons.add(addDrawer);
		addDrawer.setEnabled(false);
		addDrawer.addActionListener(ga);
		btnPanel.add(addDrawer, gbc);
		gbc.gridx++; //2

		addFolder = new JButton("Add Folder");
		actionButtons.add(addFolder);
		addFolder.setEnabled(false);
		addFolder.addActionListener(ga);
		btnPanel.add(addFolder, gbc);
		gbc.gridx++; //3

		addPage = new JButton("Add Page");
		actionButtons.add(addPage);
		addPage.setEnabled(false);
		addPage.addActionListener(ga);
		btnPanel.add(addPage, gbc);
		gbc.gridx++; //4

		addNode = new JButton("Add Node");
		actionButtons.add(addNode);
		addNode.setEnabled(false);
		addNode.addActionListener(ga);
		// need to get both the key for the new node and the value
		// Will likely require two input fields
		btnPanel.add(addNode, gbc);
		gbc.gridx++;

		// will only save the selected FileCabinet
		/*
		 * possibility of checking what level is currently selected and save either
		 * all FileCabinets, the selected FileCabinet, or the selected Drawer
		 * 
		 * Also, look into multiple save buttons, one to save everything (overkill
		 * if the user only makes one change in a very large set of FileCabinets)
		 * and one to only save the selected drawer 
		 */
		saveBtn = new JButton("Save");
		actionButtons.add(saveBtn);
		saveBtn.setEnabled(false);
		saveBtn.addActionListener(ga);
		btnPanel.add(saveBtn, gbc);
		gbc.gridx++;
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(ga);
		btnPanel.add(deleteButton, gbc);
		gbc.gridx = 0;
		gbc.gridy++; //1
		

		addArray = new JButton("Add Array");
		actionButtons.add(addArray);
		addArray.setEnabled(false);
		addArray.addActionListener(ga);
		btnPanel.add(addArray, gbc);
		gbc.gridx++; //1
		
		delArrayItem = new JButton("Delete Array Item");
		actionButtons.add(delArrayItem);
		delArrayItem.setEnabled(false);
		delArrayItem.addActionListener(ga);
		btnPanel.add(delArrayItem,gbc);
		gbc.gridx++; //2
		
		buildRel = new JButton("Build Relationship");
		actionButtons.add(buildRel);
		buildRel.setEnabled(false);
		buildRel.addActionListener(ga);
		btnPanel.add(buildRel, gbc);
		gbc.gridx++; //3
		
		//repeat here what is being done with the far side input
		//changet this to empty string and remove the setVisible line
		nearListInputLabel = new JLabel("");
		//nearListInputLabel.setVisible(false);
		btnPanel.add(nearListInputLabel,gbc);
		gbc.gridx++;
		
		//remove setvisible and replace with setEnable
		nearListInput = new JTextField(10);
		nearListInput.setEnabled(false);
		btnPanel.add(nearListInput,gbc);
		gbc.gridx++;
		
//		JButton test = new JButton("Test");
//		test.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				buildFarSideTree();
//			}
//			
//		});
//		btnPanel.add(test,gbc);
//		gbc.gridx++;
//		
//		JButton test2 = new JButton("Test2");
//		test2.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				nearSideLinkLabel.setText("Active FileCabinet Repository");
//				farListInputLabel.setText("");
//				scrollPaneRight.setViewportView(null);
//				farListInput.setText("Disabled");
//				farListInput.setEnabled(false);
//			}			
//		});
//		btnPanel.add(test2,gbc);
//		gbc.gridx++;

		/*
		 * Need to add another save button to save all FileCabinets. Look at the
		 * root, grab all of the immediate children (the FileCabinets) and run
		 * the save on each of them.
		 */
	}
	
	public TreeNode populateTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		File dir = new File("fcRepo");
		if(!dir.exists()){
			dir.mkdirs();
		}
		System.out.println(new File("fcRepo").exists() ? "True" : "False");
		File[] files = dir.listFiles();
		// Only display items that are folders, not individual files
		files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		if (files.length == 0 || files == null) {
			System.out.println("Either dir does not exist or is not a directory");
		} else {
			for (int i = 0; i < files.length; i++) {
				File filename = files[i];
				fc = new FileCabinet();
				fc.setFCName(filename.getName());
				DefaultMutableTreeNode newFcNode = new DefaultMutableTreeNode(fc);
				File[] fcFiles = filename.listFiles();
				for (File file : fcFiles) {
					DefaultMutableTreeNode newFolder;
					DefaultMutableTreeNode newPage;
//					DefaultMutableTreeNode newBaseNode;
					if (!file.getName().equals("actionLog.txt") && !file.getName().equals("drawerConfigs.xml")
							&& !file.getName().equals("fcConfig.xml")) {
						Date date = new Date();
						System.out.println("Reading " + file.getName() + " at " + new Timestamp(date.getTime()));
						Drawer d = ReadWrite.readFromXml(filename.getName(), file.getName());
						fc.addDrawer(d);
						DefaultMutableTreeNode newDrawer = buildDrawer(d);
//						DefaultMutableTreeNode newDrawer = new DefaultMutableTreeNode(d);
//						if (d.getFolders() != null) {
//							for (Folder f : d.getFolders()) {
//								newFolder = new DefaultMutableTreeNode(f);
//								if(f.getFolders()!=null){
//									for(Folder fInner : f.getFolders()){
//										loadSubFolders(newFolder, fInner);
//									}
//								}
//								//for pages directly attached to folders
//								if(f.getPages()!=null){
//									for (Page p : f.getPages()) {
//										newPage = buildPages(p);
//										newFolder.add(newPage);
//									}									
//								}
//								newDrawer.add(newFolder);
//							}
//						}
//						//for pages directly attached to drawer
//						if (d.getPages() != null) {
//							for (Page p : d.getPages()) {
//								newPage = buildPages(p);
//								newDrawer.add(newPage);
//							}
//						}
						newFcNode.add(newDrawer);
					}
				}
				fce.addFC(fc);
				root.add(newFcNode);
			}
		}
		return root;
	}
	
	public DefaultMutableTreeNode buildDrawer(Drawer d) {
		DefaultMutableTreeNode result = new DefaultMutableTreeNode(d);
		DefaultMutableTreeNode newFolder;
		DefaultMutableTreeNode newPage;
		if (d.getFolders() != null) {
			for (Folder f : d.getFolders()) {
				newFolder = new DefaultMutableTreeNode(f);
				if(f.getFolders()!=null){
					for(Folder fInner : f.getFolders()){
						loadSubFolders(newFolder, fInner);
					}
				}
				//for pages directly attached to folders
				if(f.getPages()!=null){
					for (Page p : f.getPages()) {
						newPage = buildPages(p);
						newFolder.add(newPage);
					}									
				}
				result.add(newFolder);
			}
		}
		//for pages directly attached to drawer
		if (d.getPages() != null) {
			for (Page p : d.getPages()) {
				newPage = buildPages(p);
				result.add(newPage);
			}
		}		
		return result;
	}
	
	/**
	 * Take a Page and iterate through the Nodes. Add non-FKL Nodes
	 * first followed by FKLNodes
	 * @param p The Page to process
	 * @return The Page and its Nodes processed for placing into the
	 * 		tree
	 */
	private DefaultMutableTreeNode buildPages(Page p){
		DefaultMutableTreeNode result = new DefaultMutableTreeNode(p);
		DefaultMutableTreeNode newBaseNode;
		if(p.getNodes()!=null){
			for (BaseNode bn : p.getNodes()) {
				if(!(bn instanceof FKLNode)){
					newBaseNode = new DefaultMutableTreeNode(bn);
					newBaseNode.setAllowsChildren(false);
					result.add(newBaseNode);
				}
			}
			for (BaseNode bn : p.getNodes()) {
				if(bn instanceof FKLNode){
					System.out.println("FKL Node - " + bn.getnKey());
					FKLNode fkl = (FKLNode)bn;
					if(fkl.getKeyList()!=null){
						DefaultMutableTreeNode linkName = buildLinks(fkl);
						result.add(linkName);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Take a FKLNode and build the section of the tree containing the
	 * details from the various pages it connects to
	 * @param fkl The FKLNode to be processed
	 * @return The FKLNode and the links within processed for placing into the
	 * 		tree
	 */
	private DefaultMutableTreeNode buildLinks(FKLNode fkl){
		DefaultMutableTreeNode result = new DefaultMutableTreeNode(fkl.getnKey());
		DefaultMutableTreeNode newBaseNode;
		for(ForeignLink fl : fkl.getKeyList()){
			//need to add a break of some sort between 
			DefaultMutableTreeNode pageBreak = new DefaultMutableTreeNode(fl.getDrawerName()+": "+fl.getPageID());
			Drawer tempDrawer = ReadWrite.readFromXml(fc.getFcName(), fl.getDrawerName()+".xml");
			Page tempPage = null;
			System.out.println(fl.getFolderName().length());
			if(fl.getFolderName()!=null && fl.getFolderName().length()>0){
				System.out.println(fc.getFcName()+": Folders");
				ArrayList<String> tempFolders = new ArrayList<>(ParseCommand.parseFolderInput(fl.getFolderName()));
				tempPage = tempDrawer.getFolder(tempFolders).getPage(fl.getPageID());
			} else {
				System.out.println(fc.getFcName()+": No folders");
				tempPage = tempDrawer.getPage(fl.getPageID());
			}
			for(BaseNode pIter : tempPage.getNodes()){
				if(!(pIter instanceof FKLNode)){
					newBaseNode = new DefaultMutableTreeNode(pIter);
					pageBreak.add(newBaseNode);
				}
			}
			result.add(pageBreak);
		}
		return result;
	}
	
	/**
	 * Recursive method for loading sub folders from the Drawer XML files
	 * into the tree
	 * @param parentFolder The TreeNode to add the elements to
	 * @param f
	 */
	private void loadSubFolders(DefaultMutableTreeNode parentFolder, Folder f){
		DefaultMutableTreeNode subFolder = new DefaultMutableTreeNode(f);
		if(f.getFolders()!=null){
			for(Folder fInner : f.getFolders()){
				loadSubFolders(subFolder, fInner);
			}
		}
		if(f.getPages()!=null){
			DefaultMutableTreeNode newPage;
			DefaultMutableTreeNode newBaseNode;
			for(Page p : f.getPages()){
				newPage = new DefaultMutableTreeNode(p);
				if(p.getNodes()!=null){
					for (BaseNode bn : p.getNodes()) {
						newBaseNode = new DefaultMutableTreeNode(bn);
						newBaseNode.setAllowsChildren(false);
						newPage.add(newBaseNode);
					}
				}
				subFolder.add(newPage);
			}
		}
		parentFolder.add(subFolder);
	}

	/*
	 * This may need to change
	 */
	private void updateInput(boolean node) {
		if (node) {
			regInputLabel.setVisible(false);
			input.setVisible(false);
			keyInputLabel.setVisible(true);
			valueInputLabel.setVisible(true);
			keyInput.setVisible(true);
			valueInput.setVisible(true);
			saveAsString.setVisible(true);
		} else {
			regInputLabel.setVisible(true);
			input.setVisible(true);
			keyInputLabel.setVisible(false);
			valueInputLabel.setVisible(false);
			keyInput.setVisible(false);
			valueInput.setVisible(false);
			saveAsString.setVisible(false);
		}
	}

	public void setFCE(FileCabinetEngine e) {
		fce = e;
	}
	
	private void changeEnabledBtns(ArrayList<String> valid){
		for(JComponent btn : actionButtons){
			if(valid.contains("Build Relationship")){
				nearListInputLabel.setText("Near side list name:");
				nearListInput.setEnabled(true);
			} else {
				nearListInputLabel.setText("");
				nearListInput.setEnabled(false);
			}
			if(valid.contains(((JButton)btn).getActionCommand())){
				btn.setEnabled(true);
			} else {
				btn.setEnabled(false);
			}
		}
	}
	
	public FileCabinetEngine getFCE(){
		return fce;
	}

	public void updateView(DefaultMutableTreeNode n, DefaultMutableTreeNode s) {
		model.insertNodeInto(n, s, s.getChildCount());
		// now display new node
		TreeNode[] nodes = model.getPathToRoot(n);
		TreePath path = new TreePath(nodes);
		tree.scrollPathToVisible(path);
	}

	public void updateView(DefaultMutableTreeNode n, DefaultMutableTreeNode s, int tgtLoc) {
		model.insertNodeInto(n, s, tgtLoc);
		// now display new node
		TreeNode[] nodes = model.getPathToRoot(n);
		TreePath path = new TreePath(nodes);
		tree.scrollPathToVisible(path);
	}
	
	public void updateMsg(String m){
		msgLabel.setText(m);
	}
	
	public void clearMsg(){
		msgLabel.setText("");
	}
	
	public void initRs(){
		rs = new RelationshipStruct();
	}
	
	public RelationshipStruct getRs(){
		return rs;
	}
	
	public void buildRs(DefaultMutableTreeNode tn, String listName, String tgt){
		RelationshipItem ri = new RelationshipItem();
		ri.setListName(listName);
		ArrayList<String> folders = new ArrayList<>();
		TreeNode[] fullPath = tn.getPath();
		for (TreeNode n : fullPath) {
			if (((DefaultMutableTreeNode) n).getUserObject() instanceof Drawer) {
				ri.setDrawerName(((Drawer) ((DefaultMutableTreeNode) n).getUserObject()).getDrawerName());
			} else if (((DefaultMutableTreeNode) n).getUserObject() instanceof Folder) {
				Folder temp = (Folder) ((DefaultMutableTreeNode) n).getUserObject();
				folders.add(temp.getFolderName());
			} else if(((DefaultMutableTreeNode) n).getUserObject() instanceof Page){
				ri.setPageID(((Page)((DefaultMutableTreeNode)n).getUserObject()).getId());
			}
		}
		if(folders.size()>0){
			String fldrStr = String.join("|", folders);
			ri.setFolderName(fldrStr);
		} else {
			ri.setDrawerName(null);
		}
		System.out.println(tgt+": "+ri.toString());
		if(tgt.equals("target")){
			//TODO System.out.println("Setting target RI");
			rs.setTgtRI(ri);
		} else if(tgt.equals("source")){
			//TODO System.out.println("Setting source RI");
			rs.setSrcRI(ri);
		}
	}
	
//	/**
//	 * 
//	 * @param tn Path to the target
//	 * @param newNode New Node to add to the tree
//	 */
//	public void setRelationship(TreeNode[] tn, DefaultMutableTreeNode newNode, boolean folder){
//		String fFCName = ((FileCabinet)((DefaultMutableTreeNode) tn[0]).getUserObject()).getFcName();
//		Drawer farD = (Drawer)((DefaultMutableTreeNode) tn[1]).getUserObject();
//		TreeModel tempRoot = tree.getModel();
//		boolean search = true;
//		int t = 0;
//		while(search){
//			int childCount = tempRoot.getChildCount(root);
//			for(int i = 0; i < childCount; i++){
//				if(((DefaultMutableTreeNode)tempRoot.getChild(root, i)).toString().equals(tn[t].toString())){
//					DefaultMutableTreeNode temp = (DefaultMutableTreeNode)tempRoot.getChild(root, i);
//					t++;
//					DefaultMutableTreeNode dest = getNode(tn, t, temp);
//					Page p = (Page)dest.getUserObject();
//					if(folder){
//						ArrayList<String> folders = new ArrayList<>();
//						for (TreeNode n : tn) { // iterate through the path
//							if (((DefaultMutableTreeNode) n).getUserObject() instanceof Folder) { //build the super folder list
//								Folder tempFolder = (Folder) ((DefaultMutableTreeNode) n).getUserObject();
//								folders.add(tempFolder.getFolderName());
//							}
//						}
//						farD.getFolder(folders).getPage(p.getId()).addNodes((FKLNode)newNode.getUserObject());
//					} else {
//						farD.getPage(p.getId()).addNodes((FKLNode)newNode.getUserObject());
//					}
//					updateView(newNode,dest);
//					search = false;
//					ReadWrite.writeToXml(fFCName, farD);
//					break;
//				}
//			}
//			search = false;
//		}
//	}
	
//	private DefaultMutableTreeNode getNode(TreeNode[] tn, int t, DefaultMutableTreeNode working){
//		int i = 0;
//		int workingCount = working.getChildCount();
//		DefaultMutableTreeNode result = null;
//		if(working.getUserObject() instanceof Page){
//			return working;
//		} else {
//			for(int j = 0; i < workingCount; j++){
//				if(working.getChildAt(j).toString().equals(tn[t].toString())){ //if we have a match, dig deeper
//					t++;
//					result = getNode(tn,t, (DefaultMutableTreeNode)working.getChildAt(j));
//					break;
//				}
//			}
//			return result;
//		}
//	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = null;
		this.root = root;
	}
	
	public void deleteNode(){
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		TreeNode temp = selectedNode.getParent();

		if (selectedNode != null && selectedNode.getParent() != null)
			model.removeNodeFromParent(selectedNode);
	}
	
	public void deleteNode(DefaultMutableTreeNode selectedNode){
		TreeNode temp = selectedNode.getParent();

		if (selectedNode != null && selectedNode.getParent() != null)
			model.removeNodeFromParent(selectedNode);
	}

//	private void buildButtonWest() {
//		btnPanelWest = new JPanel();
//		btnPanelWest.setLayout(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.ipadx = 0;
//		gbc.ipady = 20;
//		deleteButton = new JButton("Delete");
//		deleteButton.addActionListener(wbp);
//		btnPanelWest.add(deleteButton, gbc);
//		gbc.gridy++;
//
//		addFC = new JButton("Add FileCabinet");
//		actionButtons.add(addFC);
//		addFC.setEnabled(false);
//		/*
//		 * Each of these will need to follow a similar path to the ones above
//		 * for the listener, or move everything to an actual event class, or
//		 * several to break up code.
//		 */
//		addFC.addActionListener(wbp);
//		btnPanelWest.add(addFC, gbc);
//		gbc.gridy++;
//
//		addDrawer = new JButton("Add Drawer");
//		actionButtons.add(addDrawer);
//		addDrawer.setEnabled(false);
//		addDrawer.addActionListener(wbp);
//		btnPanelWest.add(addDrawer, gbc);
//		gbc.gridy++;
//
//		addFolder = new JButton("Add Folder");
//		actionButtons.add(addFolder);
//		addFolder.setEnabled(false);
//		addFolder.addActionListener(wbp);
//		btnPanelWest.add(addFolder, gbc);
//		gbc.gridy++;
//
//		addPage = new JButton("Add Page");
//		actionButtons.add(addPage);
//		addPage.setEnabled(false);
//		addPage.addActionListener(wbp);
//		btnPanelWest.add(addPage, gbc);
//		gbc.gridy++;
//
//		addNode = new JButton("Add Node");
//		actionButtons.add(addNode);
//		addNode.setEnabled(false);
//		addNode.addActionListener(wbp);
//		// need to get both the key for the new node and the value
//		// Will likely require two input fields
//		btnPanelWest.add(addNode, gbc);
//		gbc.gridy++;
//	}

//	private void buildButtonEast() {
//		btnPanelEast = new JPanel();
//		btnPanelEast.setLayout(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.ipadx = 0;
//		gbc.ipady = 20;
//
//		addArray = new JButton("Add Array");
//		actionButtons.add(addArray);
//		addArray.setEnabled(false);
//		addArray.addActionListener(ebp);
//		/*
//		 * Each of these will need to follow a similar path to the ones above
//		 * for the listener, or move everything to an actual event class, or
//		 * several to break up code.
//		 */
//		btnPanelEast.add(addArray, gbc);
//		gbc.gridy++;
//		
//		delArrayItem = new JButton("Delete Array Item");
//		actionButtons.add(delArrayItem);
//		delArrayItem.setEnabled(false);
//		delArrayItem.addActionListener(ebp);
//		btnPanelEast.add(delArrayItem,gbc);
//		gbc.gridy++;
//		
//		nearListInputLabel = new JLabel("Near Side List:");
//		nearListInputLabel.setVisible(false);
//		btnPanelEast.add(nearListInputLabel,gbc);
//		gbc.gridy++;
//		
//		nearListInput = new JTextField(10);
//		nearListInput.setVisible(false);
//		btnPanelEast.add(nearListInput,gbc);
//		gbc.gridy++;
//		
//		buildRel = new JButton("Build Relationship");
//		actionButtons.add(buildRel);
//		buildRel.setEnabled(false);
//		buildRel.addActionListener(ebp);
//		btnPanelEast.add(buildRel, gbc);
//		gbc.gridy++;
//
//		// will only save the selected FileCabinet
//		/*
//		 * possibility of checking what level is currently selected and save either
//		 * all FileCabinets, the selected FileCabinet, or the selected Drawer
//		 * 
//		 * Also, look into multiple save buttons, one to save everything (overkill
//		 * if the user only makes one change in a very large set of FileCabinets)
//		 * and one to only save the selected drawer 
//		 */
//		saveBtn = new JButton("Save");
//		actionButtons.add(saveBtn);
//		saveBtn.setEnabled(false);
//		saveBtn.addActionListener(ebp);
//		btnPanelEast.add(saveBtn, gbc);
//		gbc.gridy++;
//
//		/*
//		 * Need to add another save button to save all FileCabinets. Look at the
//		 * root, grab all of the immediate children (the FileCabinets) and run
//		 * the save on each of them.
//		 */
//	}
}
