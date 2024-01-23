package com.codinghavoc.test;

import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.Page;
import com.codinghavoc.structs.nodes.BaseNode;

/*Replicate and create a similar class to read in from Xml files. Will
 * first need to get the names of all of the drawers for that FileCabinet
 * (great time to use a StringArray)
 */

public class ThreadTest implements Runnable{
	private Drawer drawer;
	
	@Override
	public void run() {
		//Replace code with writeToXml code from FileCabinet
		//Remove writeToXml code from FileCabinet
		System.out.println("Running: " + drawer.getDrawerName());
		for(Page p : drawer.getPages()){
			for(BaseNode bn : p.getNodes()){
				System.out.println(bn);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Finished: " + drawer.getDrawerName());
	}
	
	public ThreadTest(Drawer d){
		drawer = d;
	}
}
