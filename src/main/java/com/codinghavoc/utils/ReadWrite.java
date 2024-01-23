package com.codinghavoc.utils;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.codinghavoc.structs.Drawer;
import com.codinghavoc.structs.nodes.StringNode;

public class ReadWrite {
	
	//Reading fcConfigs from xml
	public static StringNode readFcConfigFromXml(String fcName){
		try{
			JAXBContext context = JAXBContext.newInstance(StringNode.class);
			Unmarshaller un = context.createUnmarshaller();
			StringNode s = (StringNode) un.unmarshal(new File("fcRepo\\"+fcName+"\\fcConfig.xml"));
			return s;
		} catch (JAXBException e){
			e.printStackTrace();
		}
		return null;
	}
	
	//Reading Drawers from xml
	public static Drawer readFromXml(String fcName, String tgt){
		try{
			JAXBContext context = JAXBContext.newInstance(Drawer.class);
			Unmarshaller un = context.createUnmarshaller();
			Drawer d = (Drawer) un.unmarshal(new File("fcRepo\\"+fcName+"\\"+tgt));
			//Might relook whether to use this particular command
			LogTracker.updateLog(fcName, "Drawer - " + d.getDrawerName() + " unmarshalled...");
			Date date = new Date();
			System.out.println("Finished reading " + d.getDrawerName() + " at " + new Timestamp(date.getTime()));
			return d;
		} catch (JAXBException e){
			e.printStackTrace();
		}
		return null;
	}
	
	//Writing fcConfigs to xml
	public static void writeFcConfigToXml(String fcName, StringNode n){
		try{
			JAXBContext context = JAXBContext.newInstance(StringNode.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(n, new File("fcRepo\\"+fcName+"//fcConfig.xml"));
		} catch (JAXBException e){
			e.printStackTrace();
		}
	}

	//Writing Drawers to xml
	public static void writeToXml(String fcName, Drawer d){
		try{
			JAXBContext context = JAXBContext.newInstance(Drawer.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(d, new File("fcRepo\\"+fcName+"//"+d.getDrawerName()+".xml"));
			Date date = new Date();
			System.out.println("Finished writing " + d.getDrawerName() + " at " + new Timestamp(date.getTime()));
		} catch (JAXBException e){
			e.printStackTrace();
		}
	}

}
