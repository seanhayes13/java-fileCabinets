package com.codinghavoc.utils;

import java.util.ArrayList;
import java.util.Arrays;

import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.DateNode;

public class ParseCommand {
	private static boolean checkLength(String s){
		if(s.substring(0, s.indexOf(":")).trim().length()>0 ||
				s.substring(s.indexOf(":")+1).trim().toString().length()>0){
			return true;
		} else {
			return false;
		}
	}
	
	private static int checkParseBuildRel(String s){
		int check = 1;
		for(int i = 0; i < s.length();i++){
			if(s.charAt(i)==':'){
				check++;
			}
		}
		String [] working = s.split(":");
		for(String s2 : working){
			if(s2.trim().length()>0){
				check--;
			}
		}
		return check;
	}
	
	public static ArrayList<RelationshipStruct> parseBuildRel(String input, String activeDrawer, String activeFolder, int activePage){
		ArrayList<RelationshipStruct> result = new ArrayList<RelationshipStruct>();
		String[] working = null;
		if(input.indexOf(";")==-1){
			working = input.split(":");
			if(working.length==5 && checkParseBuildRel(input)==0){
				try{
					RelationshipStruct rs = processArray(working, activeDrawer, activeFolder, activePage);
					result.add(rs);
				} catch (NumberFormatException nfe){
					System.out.println("Provide a number for the Page ID");
				}
			} else {
				System.out.println("Incorrect format: Missing parameter");
			}
		} else {
			String[] workingMultiple = input.split(";");
			for(String s1 : workingMultiple){
				working = s1.split(":");
				if(working.length==5 && checkParseBuildRel(input)==0){
					try{
						RelationshipStruct rs = processArray(working, activeDrawer, activeFolder, activePage);
						result.add(rs);
					} catch (NumberFormatException nfe){
						System.out.println("Provide a number for the Page ID");
					}
				} else {
					System.out.println("Incorrect format: Missing parameter");
				}
			}
		}
		return result;
	}
	
	public static ArrayList<BaseNode> parseCmd(){
		ArrayList<BaseNode> result = new ArrayList<BaseNode>();
		String cmd = UserInput.getString();
		if(cmd.indexOf(";")==-1){
			if(cmd.indexOf(":")>0 && checkLength(cmd)){
				result.add(processInput(cmd));
			} else {
				System.out.println("Incorrect format");
			}
		} else {
			String[] params2 = cmd.split(";");
			for(String s : params2){
				if(s.indexOf(":")>0 && checkLength(s)){
					result.add(processInput(s));
				} else {
					System.out.println("Incorrect format");
				}
			}
		}
		return result;
	}
	
	public static DateNode parseDate(String t, String i){
		DateNode result = new DateNode();
		
		return result;
	}
	
	public static ArrayList<String> parseFolderInput(String s){
		if(s == null || s.indexOf("|")==-1){
			ArrayList<String> temp = new ArrayList<String>(); 
			temp.add(s);
			return temp;
		} else {
			return new ArrayList<String>(Arrays.asList(s.split("\\|")));
		}
	}
	
	private static RelationshipStruct processArray(String[] working, String activeDrawer, String activeFolder, int activePage){
		RelationshipStruct rs = new RelationshipStruct();
		RelationshipItem ri = new RelationshipItem();
		ri.setDrawerName(working[0]);
		ri.setFolderName(working[1]);
		ri.setPageID(Integer.parseInt(working[2]));
		ri.setListName(working[3]);
		rs.setTgtRI(ri);
		ri = new RelationshipItem();
		ri.setDrawerName(activeDrawer);
		ri.setFolderName(activeFolder);
		ri.setPageID(activePage);
		ri.setListName(working[4]);
		rs.setSrcRI(ri);
		return rs;
	}
	
	private static BaseNode processInput(String s){
		BaseNode result = null;
		String key = s.substring(0, s.indexOf(":")).trim();
		result = RedirectInput.redirectInput(key,s.substring(s.indexOf(":")+1).trim());
		return result;
	}
	
	public static boolean validateDateInput(String input){
		String[] working = input.split(":");
		if(working.length!=3 || working.length!=6){
			return false;
		} else {
			for(String s : working){
				try{
					Integer.parseInt(s);
				} catch (NumberFormatException nfe){
					return false;
				}
			}
			return true;
		}
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/