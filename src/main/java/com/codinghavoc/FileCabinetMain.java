package com.codinghavoc;

import com.codinghavoc.gui.UserInterfaceView;
import com.codinghavoc.main.FileCabinetEngine;

import static com.codinghavoc.test.FileCabinetTest.*;

//import com.fileCabinets.test.FileCabinetTest;
//import com.fileCabinets.compare.NodeValueCompare;
//import com.fileCabinets.structs.*;
//import com.fileCabinets.test.FileCabinetTest;

/**
 * version 0.2024.01.23.1234
 * @author Sean Hayes
 *
 */

public class FileCabinetMain {
	public static void cmdPrompt(){
		FileCabinetEngine fce = new FileCabinetEngine();
		fce.run();
	}
	
	public static void main(String[] args){
//		testChangeToString();
//		hashSetTest();
//		numberTest();
//		testStream();
//		testLogTracker();
//		sizeTest();
		// if(args[0].equals("gui")){
		// 	System.out.println("User Interface Mode");
			userInterface();
		// } else {
		// 	System.out.println("Command Prompt Mode");
		// 	cmdPrompt();
		// }
	}
	
	public static void userInterface(){
		FileCabinetEngine fce = new FileCabinetEngine();
		UserInterfaceView uiv = new UserInterfaceView();
		fce.setUIV(uiv);
		uiv.setFCE(fce);
		//UserInterfaceAction uia = new UserInterfaceAction(fce, uiv);
		uiv.display();
	}
}

/*
 * To do list:
 * - Build addToArray in FileCabinetEngine to add more elements to array 
 * 		Nodes; will need to verify that new user input matches the array
 * 		type, example and exceptions:
 * 		- can't put a String into an Integer array
 * 		- Integers can be converted to Doubles
 * 		- Integers and Doubles can be converted to String
 * - Modify public struct member functions that return void to return bool. Make
 * 		a different log entry based on the return value. Example, when adding a
 *  	new Node to a Page: if the add returns true, the new Node was added to
 *  	the Page, if false, the new Node was not added because another Node with
 *  	the same nKey already exists.
 * - Make use of the Stream.collect(Collectors.joining(", ")) to put arrays into a
 * 		String for comparison or printing
 * - Look at the summarizing section in Chapter 1.8 of the Core Java for ways to
 * 		perform various math operations
 * - Modify the format of the logs so that they can be parsed into strings, these 
 * 		strings can then be used to rebuild a FileCabinet (will need a separate
 * 		utility to perform this function)
 * - Pop up all around to display a problem (adding a folder to an existing folder
 * 		or drawer if another entity exists with the same name); a node already exists
 * 		with that key
 * - Develop a 'refresh' method on the UserInterfaceView to reflect changes made
 * - Develop methods to load all FileCabinets in the repo or one at a time
 * - Work with buildRelationshipPopup to refresh the tree after adding a link to
 * 		an existing FKLNode
 */