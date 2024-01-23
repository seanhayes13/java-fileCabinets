package com.codinghavoc.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.codinghavoc.gui.BuildRelationshipPopup;
import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.FileCabinet;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.utils.ReadWrite;

public class EastButtonPanel implements ActionListener{
	private UserInterfaceView uiv;
	private BuildRelationshipPopup brp;
	
	public EastButtonPanel(){
		
	}
	
	public EastButtonPanel(UserInterfaceView v){
		uiv = v;
	}
	
	public void setView(UserInterfaceView v){
		uiv = v;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		uiv.clearMsg();
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
}
