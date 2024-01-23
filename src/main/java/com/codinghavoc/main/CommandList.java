package com.codinghavoc.main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//look into transforming this into an enum
public class CommandList {
	private static LinkedHashMap<String,String> cmdList = new LinkedHashMap<>();
	
	public static void buildList(){
		cmdList.put("addtoarray", "Add an element to the current array");
		cmdList.put("addfolder","Add a new Folder to the existing active Drawer or Folder");//addfolder
		cmdList.put("backup","Make a full backup of the existing database");
		cmdList.put("buildrelationship","Establish a relationship between two pages. This includes one-to-one, one-to-many, and many-to-many");
		cmdList.put("clearallfolders", "Clear all active Folders and return to Drawer");
		cmdList.put("cleardrawer","Clear the FileCabinet's active drawer and work from the FileCabinet level.");//clearactivedrawer, tested, works
		cmdList.put("clearfc","Clears the active FileCabinet in preparation of loading a new FileCabinet");//clearactivefilecabinet
		cmdList.put("clearnode","Clear the active node."); //clearnode
		cmdList.put("clearonefolder","Clear only the lowest active Folder, go up one level");
		cmdList.put("clearpage","Clears the active Page before loading a new page. A new page can be added without running this command.");//clearactivepage
		cmdList.put("converttoarray","Convert the active Node, if it is a valid type, into an array Node");//converttoarray
		cmdList.put("copynode","Clone an existing Node");//copynode
		cmdList.put("deletefromarray","Delete a node within a NodeArray");//deletearraymember
		cmdList.put("deletefolder", "Delete a folder from the current Drawer or Folder");
		cmdList.put("deletedrawer","Delete one entire Drawer. User will be prompted for the name of the drawer to delete, and must confirm to delete the drawer, if found");//deletedrawer
		cmdList.put("deletenodebykey","Delete a node within a page with a particular key");//deletenodebykey
		cmdList.put("deletepage","Delete one page and all of the nodes on that page. User must confirm that they want to delete the page.");//deletepage
		//cmdList.put("deleterelationship","Test for deleting a relationship");//test only
		cmdList.put("exit","Exit the program"); //exit
		cmdList.put("filter","Run one of many sorting or where filters on the active drawer");
		cmdList.put("findall","Returns all elements in the currently active level");//findall
		cmdList.put("findmany","Returns the all nodes that match the criteria provided");//findmany
		cmdList.put("findpage","Returns the first page in a drawer with a node that matches the criteria provided");//findone
		cmdList.put("help","Display a list of known commands"); //help
		cmdList.put("listfc","Display a list of all FileCabinets in the current directory");//listfc
		cmdList.put("listdrawers","List all of the drawers associated with the current FileCabinet");//listdrawers
		//cmdList.put("loadalldrawers","This will load all drawers into the FileCabinet but will not set one as the active Drawer");//loadalldrawers
		//disabling loaddrawer, conflict with saving doesn't save all of the drawer configs
		//cmdList.put("loaddrawer","This will load one Drawer into the FileCabinet and prompt to set as the active Drawer"));//loadonedrawer
		cmdList.put("loadfc","Load file cabinet configurations to the program"); //loadfc
		cmdList.put("newarray", "Add a new Array Node to the Page. Acceptable types are Integer, Double, and String");
		cmdList.put("newdrawer","Create and add a new drawer to the active FileCabinet.");//newdrawer
		cmdList.put("newfc","Create a new FileCabinet with the option of setting the new FileCabinet as the active FileCabinet");//newfc
		cmdList.put("newfolder","Create a new Folder in the existing active Drawer or Folder");
		cmdList.put("newnode","Insert a new node onto the current page. A page must be selected."); //newnode
		cmdList.put("newpage","Set a page as the active page by providing the Page ID");//newpage
		cmdList.put("save","Save");
		cmdList.put("selectdrawer","Set a drawer as the active drawer by providing the drawer name");//selectpage
		cmdList.put("selectfolder","Set a Folder or sub-Folder as the active Folder");
		cmdList.put("selectnode","Select a single node from a page to work with directly");//selectnode
		cmdList.put("selectpage","Set a page as the active page by providing the Page ID");//selectpage
		cmdList.put("turnofftips","Turn off help tips for this FileCabinet");
		cmdList.put("turnontips","Turn on help tips for this FileCabinet");
		cmdList.put("updatearraymember","Change the nValue for a node in an array");//updatearraymember
		cmdList.put("updatenode","Change the nValue for the active Node");//updatenode
	}
	
	public static Set<String> getCmds(){
		buildList();
		return cmdList.keySet();
	}
	
	public static void printList(){
		buildList();
		for(Map.Entry<String,String> entry : cmdList.entrySet()){
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/