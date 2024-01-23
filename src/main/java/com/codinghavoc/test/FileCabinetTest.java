package com.codinghavoc.test;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.codinghavoc.structs.*;
import com.codinghavoc.structs.arrays.DoubleArray;
import com.codinghavoc.structs.arrays.IntegerArray;
import com.codinghavoc.structs.arrays.StringArray;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.DoubleNode;
import com.codinghavoc.structs.nodes.IntegerNode;
import com.codinghavoc.structs.nodes.StringNode;
import com.codinghavoc.utils.ForeignLink;
import com.codinghavoc.utils.LogTracker;
import com.codinghavoc.utils.ParseCommand;
import com.codinghavoc.utils.RedirectInput;
import com.codinghavoc.utils.RelationshipItem;
import com.codinghavoc.utils.RelationshipStruct;

public class FileCabinetTest{
	//private static FileCabinet fc = new FileCabinet("test");
	
	private static final long MB = 1024L * 1024L;
	
	public static void testChangeToString(){
		BaseNode bn = RedirectInput.redirectInput("test", "42");
		System.out.println(bn.getClass());
		bn = new StringNode(bn.getnKey(),bn.getnValue().toString());
		System.out.println(bn.getClass());
	}
	
	private static long bytesToMB(long b){
		return b/MB;
	}
	
	public static void hashSetTest(){
//		HashSet<BaseNode> nodes = new HashSet<>();
		HashSet<Page> pages = new HashSet<>();
		StringNode sn1 = new StringNode("firstName","Sean");
		StringNode sn2 = new StringNode("lastName","Hayes");
		Page p1 = new Page();
		p1.addNodes(sn1);
		p1.addNodes(sn2);
		pages.add(p1);
		StringNode sn3 = new StringNode("firstName","Anne");
		StringNode sn4 = new StringNode("lastName","Hayes");
		p1 = new Page();
		p1.addNodes(sn3);
		p1.addNodes(sn4);
		pages.add(p1);
		StringNode sn5 = new StringNode("firstName","Alex");
		StringNode sn6 = new StringNode("lastName","Hayes");
		p1 = new Page();
		p1.addNodes(sn5);
		p1.addNodes(sn6);
		pages.add(p1);
		p1 = new Page();
		p1.addNodes(sn1);
		p1.addNodes(sn2);
		System.out.println(pages.size());
//		nodes.add(sn1);
//		nodes.add(sn2);
//		nodes.add(sn1);
//		System.out.println(nodes.size());
	}
	
	public static void sizeTest(){
		//modify to use multiple threads to simulate large workload
		long start = System.currentTimeMillis();
		Runtime runtime = Runtime.getRuntime();
		System.out.println("Before");
		System.out.println("Total: " + runtime.totalMemory() + " - Free: "+runtime.freeMemory());
		System.out.println("Max memory: " +runtime.maxMemory());
		long mem = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Bytes: "+mem);
		System.out.println("MB: "+bytesToMB(mem));
		double diff = mem/runtime.totalMemory();
		System.out.println("Using : " + mem + " out of " + runtime.totalMemory());
		Drawer d = new Drawer();
		d.setDrawerName("testDrawerName");
		for(int i = 0; i <1_000_000; i++){
			long check = runtime.totalMemory() - runtime.freeMemory();
			if(check < 1_500_000_000){
				Page p = new Page(d.idIncrement());
				p.addNodes(new StringNode("uselessText","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis consequat massa. Pellentesque vulputate ante a mauris laoreet consequat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec nec varius ante, a pulvinar augue. Proin sagittis tellus odio, id condimentum ligula efficitur vitae. Sed efficitur porta urna, quis vulputate velit consequat vel. Nulla congue blandit nisi, id sagittis arcu cursus sed. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed fermentum finibus nibh, vitae fringilla nibh viverra non. Curabitur in tellus elementum, lacinia ligula ac, eleifend justo. Phasellus finibus gravida dui, sed aliquam diam bibendum id. Maecenas nec euismod nulla. In hac habitasse platea dictumst. Praesent accumsan eleifend diam vulputate feugiat. Donec non cursus elit. Curabitur sed urna ut nulla posuere aliquam eget quis augue. "));
				p.addNodes(new IntegerNode("uselessPositiveInteger",Integer.MAX_VALUE));
				p.addNodes(new IntegerNode("uselessNegativeInteger",Integer.MIN_VALUE));
				p.addNodes(new DoubleNode("uselessPositiveDouble",Double.MAX_VALUE));
				p.addNodes(new DoubleNode("uselessNegativeDouble",Double.MIN_VALUE));
				d.addPage(p);
			} else {
				System.out.println(check);
				System.out.println("Out of space");
				System.out.println("Added " + d.getPages().size() + " pages");
				break;
			}
		}
		long stop = System.currentTimeMillis();
		System.out.println("Total: " + runtime.totalMemory() + " - Free: "+runtime.freeMemory());
		mem = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Bytes: "+mem);
		System.out.println("MB: "+bytesToMB(mem));
		diff = mem/runtime.totalMemory();
		System.out.println("Using : " + mem + " out of " + runtime.totalMemory());
		
		/*
		 * This program shows that creating 1 million pages with the data above will only consume
		 * 310 MB and take approx 6 seconds
		 */
		double timeDiff = ((double)stop - (double)start)/1000;
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Time to run: " + df.format(timeDiff) + " seconds");
	}
	
	public static void numberTest(){
		BaseNode bn = null;
		String test1 = "192.0";
		//String test2 = "3.1";
		boolean set = false;
		try {
			try{
				Integer i = Integer.parseInt(test1);
				bn = new IntegerNode("testI",i);
				System.out.println("a - works");
				set = true;
			} catch (NumberFormatException e){
				System.out.println(e.getMessage());
				System.out.println("a - no");
			}
			if(!set){
				try{
					Double d = Double.parseDouble(test1);
					bn = new DoubleNode("testD",d);
					System.out.println("b - works");
				} catch (NumberFormatException e){
					System.out.println(e.getMessage());
					System.out.println("b - no");
				}
			}
		} catch (NumberFormatException e){
			System.out.println("Message - " + e.getMessage());
			System.out.println("a - " + test1);
		}
		if(bn == null){
			bn = new StringNode("testS",test1);
		}
		System.out.println(bn + "\n" + bn.getClass());
	}
	
	public static void testTree(){
		FileCabinet fc = new FileCabinet();
		fc.setFCName("treeTest");
		Drawer d = new Drawer("testDrawer");
		
		//init folder
		Folder f = new Folder("family");
		//init page
		Page p = new Page(f.idIncrement());
		p.addNodes(new StringNode("firstName","Sean"));
		p.addNodes(new StringNode("lastName","Hayes"));
		f.addPage(p);
		
		p = new Page(f.idIncrement());
		p.addNodes(new StringNode("firstName","Anne"));
		p.addNodes(new StringNode("lastName","Hayes"));
		f.addPage(p);
		
		p = new Page(f.idIncrement());
		p.addNodes(new StringNode("firstName","Alex"));
		p.addNodes(new StringNode("lastName","Hayes"));
		f.addPage(p);
		d.addFolder(f);
		
		//reset folder
		f = new Folder("parents");
		p = new Page(f.idIncrement());
		p.addNodes(new StringNode("firstName","Mark"));
		p.addNodes(new StringNode("lastName","Hayes"));
		f.addPage(p);
		p = new Page(f.idIncrement());
		p.addNodes(new StringNode("firstName","Julie"));
		p.addNodes(new StringNode("lastName","Hayes"));
		f.addPage(p);
		d.addFolder(f);
		
		//reset folder
		f = new Folder("siblings");
		p = new Page(f.idIncrement());
		p.addNodes(new StringNode("firstName","Tori"));
		p.addNodes(new StringNode("lastName","Hayes"));
		f.addPage(p);
		d.addFolder(f);
		fc.addDrawer(d);
		//PrintDetails.printDrawer(fc, d);
		TreeTest tt = new TreeTest(fc);
		tt.display();
	}
	
	public static void testStream(){
		Stream<Drawer> drawerResult = null;
		FileCabinet fc = new FileCabinet();
		fc.addDrawer(new Drawer("movies"));
		fc.addDrawer(new Drawer("actors"));
		fc.addDrawer(new Drawer("tvShows"));
		fc.addDrawer(new Drawer("authors"));
		fc.addDrawer(new Drawer("books"));
		fc.addDrawer(new Drawer("musicGroups"));
		fc.addDrawer(new Drawer("albums"));
		drawerResult = fc.getDrawerList().stream();
		Drawer result = fc.getDrawerList().stream().filter(d ->d.getDrawerName().startsWith("a")).findFirst().orElse(null);
		if(result!=null){
			System.out.println("Found: "+result.getDrawerName());
		} else {
			System.out.println("Found nothing");
		}
//		Stream<BaseNode> result = null;
//		Stream<String> result = null;
//		StringNode sN = new StringNode("firstName","Sean");
//		IntegerNode iN = new IntegerNode("ultimateAnswer",42);
//		DoubleNode dN = new DoubleNode("fiveK",3.1);
//		ArrayList<BaseNode> working = new ArrayList<>();
//		working.add(sN);
//		working.add(iN);
//		working.add(dN);
//		result = working.stream();
//		BaseNode[] temp = result.filter(bn->bn.getnKey().startsWith("f")).toArray(BaseNode[]::new);
//		int count = 0;
//		for(BaseNode s : temp){
//			count++;
//			System.out.println(count +": "+ s.toString());
//		}
//		ArrayList<String> working = new ArrayList<>();
//		working.add(sN.toString());
//		working.add(iN.toString());
//		working.add(dN.toString());
//		result = working.stream();
		//System.out.println(result.count());
		//result.peek(e -> System.out.println("Printing: "+e)).toArray();
	}
	
	public static void testLogTracker(){
		String action = "newnode";
		StringNode sn = new StringNode("test","first");
		String input = sn.getnKey() +":"+sn.getnValue();
		String message = action+"_"+input;
		LogTracker.updateLog("test",message);
		sn = new StringNode("test2","second");
		input = sn.getnKey() +":"+sn.getnValue();
		message = action+"_"+input;
		LogTracker.updateLog("test",message);
	}
	
	public static void testDateFormat(){
		String test = new SimpleDateFormat("yyyy-MM-dd_:_HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(test);
	}
	
	public static void testThread(){
		String[] drawerNames = {"alpha","bravo","charlie","delta","echo","foxtrot"};
		FileCabinet fc = new FileCabinet();
		Drawer tempDrawer;
		Page tempPage;
		StringNode sN;
		
		class Write implements Runnable{
			private Drawer drawer;
			
			@Override
			public void run() {
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
			
			Write(Drawer d){
				drawer = d;
			}
		}
		
		int max_count = 5;
		for(String s : drawerNames){
			tempDrawer = new Drawer("test");
			tempPage = new Page(tempDrawer.idIncrement());
			for(int i = 0; i < max_count; i++){
				sN = new StringNode(s+":"+i,s+":"+i);
				tempPage.addNodes(sN);
			}
			tempDrawer.addPage(tempPage);
			fc.addDrawer(tempDrawer);
			max_count+=3;
		}
		Thread t;
		for(Drawer d : fc.getDrawerList()){
			t = new Thread(new Write(d));
			System.out.println("Starting: " + d.getDrawerName());
			t.start();
		}
	}
	
	public static void testHashEquals(){
		StringNode s1 = new StringNode("alpha","albatross");
		StringNode s2 = new StringNode("bravo","brown bear");
		StringNode s3 = new StringNode("alpha","albatross");
		if(s1.equals(s2)){
			System.out.println("first - yep");
		} else {
			System.out.println("first - nope");
		}
		if(s1.equals(s3)){
			System.out.println("second - yep");
		} else {
			System.out.println("second - nope");
		}
	}
	
	public static void hashEquals(){
		Drawer d1 = new Drawer("test");
		Page p = new Page(d1.idIncrement());
		d1.addPage(p);
		Drawer d2 = new Drawer("drawerA");
		p = new Page(d2.idIncrement());
		d2.addPage(p);
		System.out.println(d1.hashCode());
		System.out.println(d2.hashCode());
		if(d1.equals(d2)){
			System.out.println("Same");
		} else {
			System.out.println("Nope");
		}
	}
	
	public static void testCopyNode(){
		FileCabinet fc = new FileCabinet();
		Drawer d = new Drawer("testDrawer");
		Page p = new Page(1);
		StringNode sn = new StringNode("test","page");
		p.addNodes(sn);
		d.addPage(p);
		fc.addDrawer(d);
		fc.setActiveNode(sn);
		for(BaseNode bn : p.getNodes()){
			System.out.println(bn.toString());
		}
		System.out.println("########################");
		p.addNodes(RedirectInput.redirectInput("copiedNode",fc.getActiveNode().getnValue().toString()));
		for(BaseNode bn : p.getNodes()){
			System.out.println(bn.toString());
		}
	}
	
	public static void testCopyDrawer(){
		FileCabinet fc = new FileCabinet();
		Drawer d = new Drawer("testDrawer");
		Page p = new Page(1);
		StringNode sn = new StringNode("test","page");
		p.addNodes(sn);
		d.addPage(p);
		fc.addDrawer(d);
		for(Drawer dIter : fc.getDrawerList()){
			System.out.println(dIter.getDrawerName());
		}
		System.out.println("########################");
		Drawer d2 = new Drawer(fc.getDrawer("testDrawer"));
		d2.setDrawerName("copiedDrawer");
		fc.addDrawer(d2);
		for(Drawer dIter : fc.getDrawerList()){
			System.out.println(dIter.getDrawerName());
		}
	}
	
	public static void testArrayNodes(){
		IntegerArray ia = new IntegerArray("testIA",42);
		ia.add(23);
		for(Integer i : ia.getnValue()){
			System.out.println(i);
		}
		DoubleNode dn = new DoubleNode("testDA",3.1);
		DoubleArray da = new DoubleArray(dn);
		da.add(6.2);
		for(Double d:da.getnValue()){
			System.out.println(d);
		}
		ArrayList<String> sl = new ArrayList<>();
		sl.add("alpha");
		sl.add("bravo");
		StringArray sa = new StringArray("testSA",sl);
		for(String s:sa.getnValue()){
			System.out.println(s);
		}
	}
	
	public static void addFolder(FileCabinet fc, String s){
		Folder f = new Folder(s);
		if(fc.getActiveFolder()==null){
			fc.getActiveDrawer().addFolder(f);
			fc.setActiveFolder(s);
		} else {
			Folder tgt;
			String tgtFldr = fc.getActiveFolder();
			ArrayList<String> temp = ParseCommand.parseFolderInput(tgtFldr);
			if(temp.size()>1){
				tgt = fc.getActiveDrawer().getFolder(temp);				
			} else {
				String tempName = temp.get(0);
				tgt = fc.getActiveDrawer().getFolder(tempName);
			}			
			System.out.println("Adding "+f.getFolderName()+" to "+tgt.getFolderName());
			tgt.addFolder(f);
			tgtFldr = tgtFldr+"|"+s;
			fc.setActiveFolder(tgtFldr);
		}
		System.out.println("ActiveFolder: "+fc.getActiveFolder());
		
		for(Folder fIter : fc.getActiveDrawer().getFolders()){
			System.out.println("Folder: "+fIter.getFolderName());
			checkPages(fIter);
			checkSubFolder(fIter);
		}
	}
	
	public static void addPageToActiveFolder(){
		FileCabinet fc = new FileCabinet();
		Drawer d = new Drawer("sample");
		Folder f = new Folder("folder");
		Page p = new Page(1);
		StringNode s = new StringNode("firstName","Sean");
		p.addNodes(s);
		d.addFolder(f);
		fc.addDrawer(d);
		fc.setActiveDrawer(d);
		fc.setActiveFolder(f.getFolderName());
		fc.getActiveDrawer().getFolder(fc.getActiveFolder()).addPage(p);
		for(Folder fIter : fc.getActiveDrawer().getFolders()){
			for(Page pIter : fIter.getPages()){
				for(BaseNode bn : pIter.getNodes()){
					System.out.println(bn.toString());
				}
			}
		}
	}
	
	public static void checkPages(Folder f){
		if(f.getPages()!=null){
			for(Page p : f.getPages()){
				System.out.println("->Page: "+p.getId());
			}
		}
	}
	
	public static void checkSubFolder(Folder f){
		if(f.getFolders() != null){
			for(Folder sfIter : f.getFolders()){
				System.out.println("Sub-Folder: "+sfIter.getFolderName());
				checkSubFolder(sfIter);
			}
		}		
	}
	
	public static void fifo(){
		ArrayList<String> list = new ArrayList<>();
		String input = "first|second|third|fourth|fifth";
		String[] list2 = input.split("\\|");
		for(String s : list2){
			list.add(s);
		}
		while(!list.isEmpty()){
			System.out.println(list.get(0));
			list.remove(0);
		}
	}
	
	public static void folderTest(){
		Drawer d = new Drawer("sample");
		
		Folder f = new Folder("folder1");
		Folder sf = new Folder("subFolder1");
		Page sfp = new Page(8);
		sfp.addNodes(new StringNode("middleMan","Monkey in the Middle"));
		sf.addPage(sfp);
		Folder ssf = new Folder("subSubFolder1");
		Page ssfp = new Page(9);
		ssfp.addNodes(new StringNode("target","Found you!"));
		ssf.addPage(ssfp);
		f.addFolder(sf);
		f.getFolders().get(0).addFolder(ssf);
		Page p = new Page(1);
		p.addNodes(new StringNode("firstName","Sean"));
		f.addPage(p);
		d.addFolder(f);
		
		Folder f2 = new Folder("folder2");
		f2.addPage(new Page(2));
		f2.addPage(new Page(3));
		d.addFolder(f2);
		
		Folder f3 = new Folder("folder3");
		f3.addPage(new Page(4));
		f3.addPage(new Page(5));
		f3.addPage(new Page(6));
		d.addFolder(f3);
		
		Page p2 = new Page(7);
		p2.addNodes(new StringNode("firstName","Anne"));
		d.addPage(p2);
//		
//		fc.addDrawer(d);
//		fc.saveFileCabinet();
//		for(Page pIter : d.getPages()){
//			System.out.println("Page: "+pIter.getId());
//		}
//		for(Folder fIter : d.getFolders()){
//			System.out.println("Folder: "+fIter.getFolderName());
//			checkPages(fIter);
//			checkSubFolder(fIter);
//		}
//		
//		Drawer d2 = fc.readFromXml("sample");
//		System.out.println("And after reading in");
//		for(Page pIter : d2.getPages()){
//			System.out.println("Page: "+pIter.getId());
//		}
//		for(Folder fIter : d2.getFolders()){
//			System.out.println("Folder: "+fIter.getFolderName());
//			checkPages(fIter);
//			checkSubFolder(fIter);
//		}
		ArrayList<String> fldrList = ParseCommand.parseFolderInput("folder1|subFolder1|subSubFolder");
		Folder check = d.getFolder(fldrList);
		if(check!=null){
			for(Page pIter : check.getPages()){
				System.out.println(pIter.getId());
				for(BaseNode bn : pIter.getNodes()){
					System.out.println(bn.toString());
				}
			}
		} else {
			System.out.println("No Folder found");
		}
//		ArrayList<String> fldrList2 = ParseCommand.parseFolderInput("folder1|subFolder1");
//		Folder check2 = d.getFolder(fldrList2);
//		for(Page pIter : check2.getPages()){
//			System.out.println(pIter.getId());
//			for(BaseNode bn : pIter.getNodes()){
//				System.out.println(bn.toString());
//			}
//		}
	}
	
	private static void jaxbObjectToXML(PageTemp p){
		try{
			JAXBContext context = JAXBContext.newInstance(PageTemp.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(p, new File("test.xml"));
		} catch (JAXBException e){
			e.printStackTrace();
		}
	}

	//Moved to Drawer
//	public static Folder getFolder(Drawer d, ArrayList<String> tgt){
//		if(tgt.size()==1){
//			return d.getFolder(tgt.remove(0));
//		} else {
//			Folder temp = d.getFolder(tgt.remove(0));
//			return getSubFolder(temp,tgt);
//		}
//	}
//	
//	private static Folder getSubFolder(Folder f, ArrayList<String> s){
//		if(s.size()==1){
//			return f.getSubFolder(s.remove(0));
//		} else {
//			Folder temp = f.getSubFolder(s.remove(0));
//			return getSubFolder(temp, s);
//		}
//	}
	
	private static PageTemp jaxbXmlToObject(){
		try{
			JAXBContext context = JAXBContext.newInstance(PageTemp.class);
			Unmarshaller un = context.createUnmarshaller();
			PageTemp d = (PageTemp) un.unmarshal(new File("test.xml"));
			return d;
		} catch (JAXBException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void joinTest(){
		ArrayList<String> temp = ParseCommand.parseFolderInput("folder1|subFolder1|subSubFolder");
		StringBuilder sb = new StringBuilder();
		temp.remove(temp.size()-1);
		sb.append(temp.remove(0));
		for(String s : temp){
			sb.append("|"+s);
		}
		System.out.println(sb.toString());
	}
	
	public static void testAddFolder(){
		FileCabinet fc = new FileCabinet("testAddFolder");
		Drawer d = new Drawer("sample2");
		fc.addDrawer(d);
		fc.setActiveDrawer(d);
		addFolder(fc, "folder1");
		addFolder(fc, "folder2");
		addFolder(fc, "folder3");
		fc.saveFileCabinet();
	}
	
	public static void testAddToLHM(){
		LinkedHashMap<String,String> lhm = new LinkedHashMap<>();
		lhm.put("test", "actors:1");
		for(Map.Entry<String, String> e : lhm.entrySet()){
			System.out.println(e.getKey()+": "+e.getValue());
		}
	}
	
	public static void testRelationshipStruct(){
		RelationshipStruct rs = new RelationshipStruct();
		RelationshipItem ri = new RelationshipItem("farDrawer",null,1,"farList");
		rs.setTgtRI(ri);
		ri = new RelationshipItem("nearDrawer",null,1,"nearList");
		rs.setSrcRI(ri);
		System.out.println(rs);
		ForeignLink fl = new ForeignLink(rs.getTgtRI().getDrawerName(), rs.getTgtRI().getFolderName(), rs.getTgtRI().getPageID());
		System.out.println(fl);
	}
	
	//http://www.journaldev.com/1234/jaxb-example-tutorial
	//https://www.javatpoint.com/jaxb-unmarshalling-example
	public static void xmlTest(){
		//FileCabinet fc = null;
		PageTemp p = new PageTemp(1);
		IntNodeTemp iNode = new IntNodeTemp("age",33);
		FKLTemp fklNode = new FKLTemp("classes","courses",1);
		fklNode.addLink("courses", 2);
		p.addNode(iNode);
		p.addNode(fklNode);
		jaxbObjectToXML(p);
		
		PageTemp newP = jaxbXmlToObject();
		for(NodeTemp n : newP.getNodes()){
			System.out.println(n.toString()+"___"+n.getClass().toString());
		}
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/