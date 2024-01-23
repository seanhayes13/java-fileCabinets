package com.codinghavoc.test;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SpinnerTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame("SwingTest");
		JPanel pane = new JPanel();
		SpinnerModel spinModel = new SpinnerNumberModel(1,1,100,1);
		JSpinner spinner = new JSpinner(spinModel);
		pane.add(spinner);
		frame.add(pane);
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(300, 100);
		frame.setVisible(true);
	}

}
