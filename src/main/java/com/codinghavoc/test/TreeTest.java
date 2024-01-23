package com.codinghavoc.test;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Folder;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.utils.ParseCommand;

@SuppressWarnings("serial")
public class TreeTest extends JFrame {
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 500;	

	private DefaultTreeModel model;

	private JFrame frame;
	
	private JButton addSiblingButton;
	private JButton addChildButton;
	private JButton testBtn;
	private JButton deleteButton;
	private JButton addFC;
	private JButton addDrawer;
	private JButton addFolder;
	private JButton addPage;
	private JButton addNode;
	private JButton addArray;
	private JButton saveBtn;
	
	private JLabel regInputLabel;
	private JLabel keyInputLabel;
	private JLabel valueInputLabel;
	
	private JPanel inputPanel;
	private JPanel btnPanelWest;
	private JPanel btnPanelEast;
	
	private JScrollPane scrollPane;
	
	private JTextField input;
	private JTextField keyInput;
	private JTextField valueInput;
	
	private JTree tree;
	
	private TreeNode root;

	private FileCabinet fc;

	public TreeTest(FileCabinet fc) {
		this.fc = fc;
	}
	
	public void display(){
		inputPanel = new JPanel();
		
		regInputLabel = new JLabel("Input");
		keyInputLabel = new JLabel("Key");
		keyInputLabel.setVisible(false);
		valueInputLabel = new JLabel("Value");
		valueInputLabel.setVisible(false);
		input = new JTextField(40);
		keyInput = new JTextField(15);
		keyInput.setVisible(false);
		valueInput = new JTextField(25);
		valueInput.setVisible(false);
		inputPanel.add(regInputLabel);
		inputPanel.add(input);
		inputPanel.add(keyInputLabel);
		inputPanel.add(keyInput);
		inputPanel.add(valueInputLabel);
		inputPanel.add(valueInput);

		buildButtonWest();
		buildButtonEast();
		buildTree();
		
		scrollPane = new JScrollPane(tree);
		
		frame = new JFrame("Test FileCabinet Tree GUI");
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(btnPanelWest, BorderLayout.WEST);
		frame.add(btnPanelEast, BorderLayout.EAST);
		frame.add(inputPanel, BorderLayout.NORTH);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void buildTree(){
		tree = new JTree(model);
		root = populateTree();
		model = new DefaultTreeModel(root);
		
		tree = new JTree(model);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode selected = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(selected!=null){
					Object o = selected.getUserObject();
					if (o instanceof FileCabinet) {
						fc = (FileCabinet)o;
						updateInput(false);
						addDrawer.setEnabled(true);
						addFolder.setEnabled(false);
						addPage.setEnabled(false);
						addNode.setEnabled(false);
					} else if (o instanceof Drawer) {
						updateInput(false);
						addDrawer.setEnabled(false);
						addFolder.setEnabled(true);
						addPage.setEnabled(true);
						addNode.setEnabled(false);
					} else if (o instanceof Folder) {
						updateInput(false);
						addDrawer.setEnabled(false);
						addFolder.setEnabled(true);
						addPage.setEnabled(true);
						addNode.setEnabled(false);
					} else if (o instanceof Page) {
						updateInput(true);
						addDrawer.setEnabled(false);
						addFolder.setEnabled(false);
						addPage.setEnabled(false);
						addNode.setEnabled(true);
					} else if (o instanceof BaseNode) {
						updateInput(false);
						addDrawer.setEnabled(false);
						addFolder.setEnabled(false);
						addPage.setEnabled(false);
						addNode.setEnabled(false);
					}
				}
			}
		});
	}
	
	private void buildButtonWest(){
		btnPanelWest = new JPanel();
		btnPanelWest.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 0;
		gbc.ipady = 20;
		
		addSiblingButton = new JButton("Add Sibling");
		addSiblingButton.addActionListener(event -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;

			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();

			if (parent == null)
				return;

			// This is where I would try to create the new struct, setting the
			// result as the new DefaultMutableTreeNode

			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");

			int selectedIndex = parent.getIndex(selectedNode);
			model.insertNodeInto(newNode, parent, selectedIndex + 1);

			// now display new node

			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
		});
		btnPanelWest.add(addSiblingButton, gbc);
		gbc.gridy++;

		addChildButton = new JButton("Add Child");
		addChildButton.addActionListener(event -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;

			// This is where I would try to create the new struct, setting the
			// result as the new DefaultMutableTreeNode and adding to tree.

			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");
			model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

			// now display new node

			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
		});
		btnPanelWest.add(addChildButton, gbc);
		gbc.gridy++;

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(event -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			
			if (selectedNode != null && selectedNode.getParent() != null)
				model.removeNodeFromParent(selectedNode);
			
		});
		btnPanelWest.add(deleteButton, gbc);
		gbc.gridy++;

		testBtn = new JButton("Test");
		testBtn.addActionListener(event -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			Object o = node.getUserObject();
			// System.out.println(o.getClass());
			if (o instanceof FileCabinet) {
				System.out.println("FileCabinet: " + ((FileCabinet) o).getFcName());
			} else if (o instanceof Drawer) {
				System.out.println("Drawer: " + ((Drawer) o).getDrawerName());
			} else if (o instanceof Folder) {
				System.out.println("Folder: " + ((Folder) o).getFolderName());
			} else if (o instanceof Page) {
				System.out.println("Page: " + ((Page) o).getId());
			} else if (o instanceof BaseNode) {
				BaseNode bn = (BaseNode) o;
				System.out.println("Node: " + bn.getnKey() + ": " + bn.getnValue());
			} else {
				System.out.println("Something else...");
			}
		});
		btnPanelWest.add(testBtn, gbc);
		gbc.gridy++;
		

		addFC = new JButton("Add FileCabinet");
		/*
		 * Each of these will need to follow a similar path to the ones above for the listener,
		 * or move everything to an actual event class, or several to break up code.
		 */
		addFC.addActionListener(event ->{
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;
//			Object o = selectedNode.getUserObject();
			FileCabinet newFC = new FileCabinet(input.getText());
			fc = newFC;
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFC);
			model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
			// now display new node
			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
	 	});
		btnPanelWest.add(addFC, gbc);
		gbc.gridy++;
		
		
		addDrawer = new JButton("Add Drawer");
		addDrawer.addActionListener(event -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (selectedNode == null)
				return;
//			Object o = selectedNode.getUserObject();
			// System.out.println(o.getClass());
			String name = input.getText();
			Drawer d = new Drawer(name);
			fc.addDrawer(d);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(d);
			model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

			// now display new node

			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
		});
		btnPanelWest.add(addDrawer, gbc);
		gbc.gridy++;


		addFolder = new JButton("Add Folder");
		addFolder.addActionListener(event -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			
			if (selectedNode == null)
				return;
			Object o = selectedNode.getUserObject();
			Drawer d = null;
			ArrayList<String> folders = new ArrayList<>();
			TreeNode[] fullPath = selectedNode.getPath();
			for(TreeNode n : fullPath){
				if(((DefaultMutableTreeNode) n).getUserObject() instanceof Drawer){
					d = (Drawer)((DefaultMutableTreeNode) n).getUserObject();
				} else if(((DefaultMutableTreeNode) n).getUserObject() instanceof Folder){
					Folder temp = (Folder)((DefaultMutableTreeNode) n).getUserObject();
					folders.add(temp.getFolderName());
				}
			}
			fc.setActiveDrawer(d);
			if(folders.size()>0){
				StringBuilder sb = new StringBuilder();
				sb.append(folders.get(0));
				if(folders.size()>1){
					for(int i = 1; i < folders.size(); i++){
						sb.append("|"+folders.get(i));
					}
				}
				//System.out.println(sb.toString());
				fc.setActiveFolder(sb.toString());
			}
			/*
			 * Need to get the Drawer that the selected Folder is in (set as
			 * activeDrawer), any Folders between the home Drawer and the 
			 * selected Folder (set activeFolder)
			 */

			DefaultMutableTreeNode newNode = null;
			String name = input.getText();
			Folder newFolder = new Folder(name);
			if(o instanceof Folder){
				if(fc.getActiveFolder()!=null){
					fc.getActiveDrawer().getFolder(folders).addFolder(newFolder);
				} else {
					fc.getActiveDrawer().addFolder(newFolder);
				}
				newNode = new DefaultMutableTreeNode(newFolder);
			} else { //Drawer
				fc.getActiveDrawer().addFolder(newFolder);
				newNode = new DefaultMutableTreeNode(newFolder);
			}
//			for(Drawer dIter : fc.getDrawerList()){
//				System.out.println(dIter.getDrawerName());
//				if(dIter.getFolders()!=null){
//					for(Folder fIter : dIter.getFolders()){
//						System.out.println(fIter.getFolderName());
//					}
//				}
//			}
			model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

			// now display new node

			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
		});
		btnPanelWest.add(addFolder, gbc);
		gbc.gridy++;


		addPage = new JButton("Add Page");
		addPage.addActionListener(event -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			
			if (selectedNode == null)
				return;
			Object o = selectedNode.getUserObject();
			Drawer d = null;
			ArrayList<String> folders = new ArrayList<>();
			TreeNode[] fullPath = selectedNode.getPath();
			for(TreeNode n : fullPath){
				if(((DefaultMutableTreeNode) n).getUserObject() instanceof Drawer){
					d = (Drawer)((DefaultMutableTreeNode) n).getUserObject();
				} else if(((DefaultMutableTreeNode) n).getUserObject() instanceof Folder){
					Folder temp = (Folder)((DefaultMutableTreeNode) n).getUserObject();
					folders.add(temp.getFolderName());
				}
			}
			fc.setActiveDrawer(d);
			if(folders.size()>0){
				StringBuilder sb = new StringBuilder();
				sb.append(folders.get(0));
				if(folders.size()>1){
					for(int i = 1; i < folders.size(); i++){
						sb.append("|"+folders.get(i));
					}
				}
				fc.setActiveFolder(sb.toString());
			}
			System.out.println("hmmm: " + fc.getActiveFolder());

			DefaultMutableTreeNode newNode = null;

			if(o instanceof Folder){
				System.out.println("alpha - " +fc.getActiveDrawer().getDrawerName());
				System.out.println("bravo - " + fc.getActiveFolder());
				ArrayList<String> temp = ParseCommand.parseFolderInput(fc.getActiveFolder());
				ArrayList<String> temp2 = new ArrayList<String>(temp);
				System.out.println("charlie - " + temp.size());
				Page p = new Page(fc.getActiveDrawer().getFolder(temp).idIncrement());
				fc.getActiveDrawer().getFolder(temp2).addPage(p);
				newNode = new DefaultMutableTreeNode(p);
			} else { //Drawer
				int id = fc.getDrawer(d.getDrawerName()).idIncrement();
				Page p = new Page(id);
				fc.getActiveDrawer().addPage(p);
				newNode = new DefaultMutableTreeNode(p);
			}
			model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

			// now display new node

			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
		});
		btnPanelWest.add(addPage, gbc);
		gbc.gridy++;


		addNode = new JButton("Add Node");
		addNode.addActionListener(event -> {
		});
		//need to get both the key for the new node and the value
		//Will likely require two input fields
		btnPanelWest.add(addNode, gbc);
		gbc.gridy++;
	}
	
	private void buildButtonEast(){
		btnPanelEast = new JPanel();
		btnPanelEast.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 0;
		gbc.ipady = 20;

		addArray = new JButton("Add Array");
		/*
		 * Each of these will need to follow a similar path to the ones above for the listener,
		 * or move everything to an actual event class, or several to break up code.
		 */
		btnPanelEast.add(addArray, gbc);
		gbc.gridy++;	

		saveBtn = new JButton("Save");
		saveBtn.addActionListener(event->{
			fc.saveFileCabinet();
		});
		btnPanelEast.add(saveBtn, gbc);
		gbc.gridy++;		
	}

	public TreeNode populateTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
//		DefaultMutableTreeNode newFC = new DefaultMutableTreeNode(fc);
//
//		DefaultMutableTreeNode newDrawer;
//		DefaultMutableTreeNode newFolder;
//		DefaultMutableTreeNode newPage;
//		DefaultMutableTreeNode newBaseNode;
//		for (Drawer d : fc.getDrawerList()) {
//			newDrawer = new DefaultMutableTreeNode(d);
//			if (d.getFolders() != null) {
//				for (Folder f : d.getFolders()) {
//					newFolder = new DefaultMutableTreeNode(f);
//					for (Page p : f.getPages()) {
//						newPage = new DefaultMutableTreeNode(p);
//						for (BaseNode bn : p.getNodes()) {
//							newBaseNode = new DefaultMutableTreeNode(bn);
//							newBaseNode.setAllowsChildren(false);
//							newPage.add(newBaseNode);
//						}
//						newFolder.add(newPage);
//					}
//					newDrawer.add(newFolder);
//				}
//			} else {
//				System.out.println("No folders");
//			}
//			if (d.getPages() != null) {
//				for (Page p : d.getPages()) {
//					newPage = new DefaultMutableTreeNode(p);
//					for (BaseNode bn : p.getNodes()) {
//						newBaseNode = new DefaultMutableTreeNode(bn);
//						newPage.add(newBaseNode);
//					}
//					newDrawer.add(newPage);
//				}
//			} else {
//				System.out.println("No pages");
//			}
//			newFC.add(newDrawer);
//		}
//		root.add(newFC);
		return root;
	}
	
	private void updateInput(boolean node){
		if(node){
			regInputLabel.setVisible(false);
			input.setVisible(false);
			keyInputLabel.setVisible(true);
			valueInputLabel.setVisible(true);
			keyInput.setVisible(true);
			valueInput.setVisible(true);			
		} else {
			regInputLabel.setVisible(true);
			input.setVisible(true);
			keyInputLabel.setVisible(false);
			valueInputLabel.setVisible(false);
			keyInput.setVisible(false);
			valueInput.setVisible(false);			
		}
	}
}
