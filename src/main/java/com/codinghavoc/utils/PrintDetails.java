package com.codinghavoc.utils;

import java.util.ArrayList;
//import java.util.LinkedHashMap;

import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.structs.*;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.BooleanNode;
import com.codinghavoc.structs.nodes.FKLNode;
import com.codinghavoc.structs.nodes.IntegerNode;
import com.codinghavoc.structs.nodes.StringNode;

public class PrintDetails {
	private static UserInterfaceView uiv;
	private static String buffer = "->";
		
	public static void printDrawer(FileCabinet fc, Drawer d){
		sendToDisplay(buffer+d.getDrawerName()+":");
		increaseBuffer();//open 1
		ArrayList<Page> working = d.getPages();
		if(working!=null){
			for(Page p : working){
				printPage(fc,p);
			}
		}
		ArrayList<Folder>workingF = d.getFolders();
		if(workingF!=null){
			increaseBuffer();//open 2
			for(Folder f1 : workingF){
				printFolder(fc,f1);
			}
			decreaseBuffer();//close 2
		}
		decreaseBuffer();//close 1
	}
	
	public static void printFolder(FileCabinet fc, Folder f){
		sendToDisplay(buffer+f.getFolderName()+":");
		ArrayList<Page>workingP = f.getPages();
		increaseBuffer();// open 1
		if(workingP!=null){
			increaseBuffer();//open 2
			for(Page p : workingP){
				printPage(fc,p);
			}
			decreaseBuffer(); //close 2
		}
		ArrayList<Folder>workingF = f.getFolders();
		if(workingF!=null){
			increaseBuffer(); //open 3
			for(Folder f1 : workingF){
				printFolder(fc,f1);
			}
			decreaseBuffer(); //close 3
		}
		decreaseBuffer();//close 1
	}
	
	public static void printNode(FileCabinet fc, BaseNode n){
		if(n instanceof IntegerNode || n instanceof BooleanNode || n instanceof StringNode){
			sendToDisplay(buffer+n.toString());
		}
		/*
		 * TODO Need to add block to print array nodes
		 */
		if(n instanceof FKLNode){
			FKLNode fkl = (FKLNode)n;
			if(fkl.getKeyList().size()>0){
				for(ForeignLink fl : fkl.getKeyList()){
					/*
					 * TODO need to update this to include increasing buffer and displaying
					 * folder name if applicable
					 */
					StringBuilder sb = new StringBuilder();
					//sendToDisplay(buffer + fl.getDrawerName());
					sb.append(buffer+"Foreign Link -> Drawer: "+fl.getDrawerName());
					if(fl.getFolderName()!=null){
						//increaseBuffer();//folder open
						//sendToDisplay(buffer+fl.getFolderName());
						sb.append(" -> Folder: "+fl.getFolderName());
					}
					Drawer d = fc.getDrawer(fl.getDrawerName());
					Folder f;
					Page p;
					if(fl.getFolderName()!=null){
						f = d.getFolder(ParseCommand.parseFolderInput(fl.getFolderName()));
						p = f.getPage(fl.getPageID());
					} else {
						p = d.getPage(fl.getPageID());
					}
					//increaseBuffer();//page open
					//sendToDisplay(buffer+"Page: "+p.getId());
					sb.append(" -> Page: "+p.getId());
					sendToDisplay(sb.toString());
					increaseBuffer();//nodes open
					for(BaseNode n2 : p.getNodes()){
						if(!(n2 instanceof FKLNode)){
							sendToDisplay(buffer+n2);
						}
					}
					decreaseBuffer();//nodes close
					//decreaseBuffer();//page close
//					if(fl.getFolderName()!=null){
//						decreaseBuffer();//folder close
//					}
				}
			}	
		}
	}
	
	public static void printPage(FileCabinet fc, Page p){
		sendToDisplay(buffer+"Page: "+p.getId());
		increaseBuffer();
		ArrayList<BaseNode> working = p.getNodes();
		if(working!=null){
			for(BaseNode n : working){
				printNode(fc,n);
			}
		}
		decreaseBuffer();
	}
	
	private static void sendToDisplay(String s){
		if(uiv != null){
			//uiv.printResults.append(s+"\n");
		} else {
			System.out.println(s);
		}
	}
	
	//Constructor for command prompt mode
	public PrintDetails(){}
	
	//Constructor for GUI mode
	public PrintDetails(UserInterfaceView u){
		//System.out.println("Setting view");
		uiv = u;
	}
	
	public void printDetails(FileCabinet fc){
		//System.out.println("Starting print");
		if(fc.getActiveDrawer()==null){ //if nothing has been set to active
			ArrayList<Drawer> working = fc.getDrawerList();
			if(working!=null){
				for(Drawer d : working){
					printDrawer(fc,d);
				}
			}
		} else if(fc.getActivePage()==null){ //if there is an active drawer but no active page
			printDrawer(fc,fc.getActiveDrawer());
		} else if(fc.getActiveNode()==null){ //if there is an active drawer and page but no active node
			printPage(fc,fc.getActivePage());
		} else { //if there is an active drawer, page and node
			printNode(fc,fc.getActiveNode());
		}
	}
	
	private static void increaseBuffer(){
		buffer = new String("  ")+buffer;
	}
	
	private static void decreaseBuffer(){
		buffer = buffer.substring(2, buffer.length());
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/