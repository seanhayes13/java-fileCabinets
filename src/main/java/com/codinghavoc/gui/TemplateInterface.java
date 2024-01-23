package com.codinghavoc.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/*
 * This is working to the point that the frame appears, and the user
 * can input 1 to 10 templates (actually need to correct the input
 * panel to remove value, or keep and use it as a bulk input method).
 */

public class TemplateInterface extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 300;
	
	private ArrayList<TemplateInputPanel> fields = new ArrayList<>();
	private int count = 1;
	
	private JFrame frame;
	
	private JPanel mainPane;
	private JPanel inputPane;
	private JPanel btnPane;
	
	private JButton btn;
	
	public void display(){
		frame = new JFrame("Template Builder");
		
		btnPane = new JPanel();
		btn = new JButton("Add more");
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(count < fields.size()){
					fields.get(count).setEnabled(true);
					fields.get(count).setVisible(true);
					count++;
					//frame.pack();
				} else {
					System.out.println("No more to add");
				}
			}
			
		});
		btnPane.add(btn);

		inputPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		TemplateInputPanel tip;
		for(int i = 0; i < 10; i++){
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.insets = new Insets(5,5,5,5);
			tip = new TemplateInputPanel();
			tip.buildPanel();
			fields.add(tip);
			System.out.println("Adding to pane");
			if(i==0){
				tip.setVisible(true);
				tip.setEnabled(true);
			} else {
				tip.setVisible(false);
				tip.setEnabled(false);
			}
			inputPane.add(tip,gbc);
		}
		
		JScrollPane scroll = new JScrollPane(inputPane);
		
		
		mainPane = new JPanel(new BorderLayout());
		mainPane.add(btnPane, BorderLayout.NORTH);
		mainPane.add(scroll, BorderLayout.CENTER);
		
		frame.add(mainPane);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
