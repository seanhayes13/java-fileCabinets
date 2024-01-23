package com.codinghavoc.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TemplateInputPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel nKeyLabel;
	private JTextField nKeyInput;
	private JLabel nValueLabel;
	private JTextField nValueInput;
	private JLabel nodeSelLabel;
	private JTextField nodeSelInput;
	
	public void buildPanel(){
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		nKeyLabel = new JLabel("Node Key");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(nKeyLabel,gbc);
		nKeyInput = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(nKeyInput,gbc);
		nValueLabel = new JLabel("Node Value");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(nValueLabel,gbc);
		nValueInput = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(nValueInput,gbc);
		nodeSelLabel = new JLabel("Select Node Type");
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(nodeSelLabel,gbc);
		nodeSelInput = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(nodeSelInput,gbc);
	}
}