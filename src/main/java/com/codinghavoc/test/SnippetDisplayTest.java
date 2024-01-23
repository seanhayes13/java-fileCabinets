package com.codinghavoc.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.codinghavoc.structs.nodes.SnippetNode;

public class SnippetDisplayTest {
	private SnippetNode tgt;
	
	public SnippetDisplayTest(SnippetNode sn){
		tgt = sn;
		display();
	}
	
	public void display(){
		JPanel pane = new JPanel(new BorderLayout());
		JPanel displayPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JTextArea lineDisplay = new JTextArea(25,4);
		JTextArea codeDisplay = new JTextArea(25,100);
		int tabSize = codeDisplay.getTabSize();
		tabSize = 2;
		codeDisplay.setTabSize(tabSize);
		int lineCount = 1;
		for(String s : tgt.getnValue()){
			lineDisplay.append(String.format("%8s", String.valueOf(lineCount))+"\n");
			codeDisplay.append(s+"\n");
			lineCount++;
		}
		/*
		 * https://stackoverflow.com/questions/6379255/how-to-scroll-two-jtextpanes
		 */
		JScrollPane lineScrollPane = new JScrollPane(lineDisplay, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane codeScrollPane = new JScrollPane(codeDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		lineScrollPane.getVerticalScrollBar().setModel(codeScrollPane.getVerticalScrollBar().getModel());
		displayPane.add(lineScrollPane, gbc);
		gbc.gridy++;
		displayPane.add(codeScrollPane,gbc);
		
		pane.add(displayPane, BorderLayout.CENTER);
		
		JFrame frame = new JFrame("SnippetInputTest");
		frame.add(pane);
		frame.pack();
		frame.setLocation(300, 100);
		frame.setVisible(true);
	}
}
