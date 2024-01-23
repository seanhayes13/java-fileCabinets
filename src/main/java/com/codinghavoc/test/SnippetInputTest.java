package com.codinghavoc.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.codinghavoc.structs.nodes.SnippetNode;

public class SnippetInputTest {
	public static void main(String[] args){
		display();
	}
	
	public static void display(){
		JPanel pane = new JPanel(new BorderLayout());
		JTextArea textfield = new JTextArea(25,50);
		int tabSize = textfield.getTabSize();
		tabSize = 2;
		textfield.setTabSize(tabSize);
		/*
		 * https://stackoverflow.com/questions/6379255/how-to-scroll-two-jtextpanes
		 */
		JScrollPane scrollPane = new JScrollPane(textfield);
		JButton submit = new JButton("Submit text");
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SnippetNode sn = new SnippetNode("test",textfield.getText());
				new SnippetDisplayTest(sn);
//				System.out.println("\n######################## --- New Run --- ########################\n");
//				SnippetNode sn = new SnippetNode("test",textfield.getText());
//				System.out.println(sn.toString());
//				System.out.println("\n######################## --- End of Run --- ########################\n");
//				String[] results = textfield.getText().split("\\r?\\n");
//				System.out.println("\n######################## --- New Run --- ########################\n");
//				int lineCount = 1;
//				for(String s : results){
//					System.out.println(lineCount + ": " + s);
//					lineCount++;
//				}
//				System.out.println("\n######################## --- End of Run --- ########################\n");
			}
			
		});
		pane.add(scrollPane, BorderLayout.CENTER);
		pane.add(submit, BorderLayout.SOUTH);
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK),"Snippet Input");
		title.setBorder(new EmptyBorder(10,10,10,10));
		pane.setBorder(title);
		
		JFrame frame = new JFrame("SnippetInputTest");
		frame.add(pane);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(300, 100);
		frame.setVisible(true);
	}
}
