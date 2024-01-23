package com.codinghavoc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.codinghavoc.main.FileCabinetEngine;

public class UserInterfaceAction implements ActionListener{
	@SuppressWarnings("unused")
	private FileCabinetEngine fce;
	@SuppressWarnings("unused")
	private UserInterfaceView uiv;
	
	public UserInterfaceAction(FileCabinetEngine e, UserInterfaceView v){
		fce = e;
		uiv = v;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		//Get text from cmdInput and send to run method in fce
//		String cmd = uiv.cmdDropDown.getSelectedItem().toString();
//		String param = uiv.paramInput.getText();
//		uiv.cmds.append("Command: "+cmd+"\n");
//		uiv.cmds.append("Parameter: "+param+"\n");
//		fce.setActionInput(uiv.paramInput.getText());
//		uiv.paramInput.setText("");
//		fce.run(cmd);
	}
}
