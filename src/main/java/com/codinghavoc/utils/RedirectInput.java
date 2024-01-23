package com.codinghavoc.utils;

import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.BooleanNode;
import com.codinghavoc.structs.nodes.DoubleNode;
import com.codinghavoc.structs.nodes.IntegerNode;
import com.codinghavoc.structs.nodes.StringNode;

public class RedirectInput {
	
	private static boolean isDbl(String s){
		try{
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
	
	private static boolean isInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
	
	public static BaseNode redirectInput(String k, String s){
		BaseNode result = null;
		if(s.equals("true")){
			result = new BooleanNode(k,true);
		} else if(s.equals("false")){
			result = new BooleanNode(k,false);
		} else if(isInt(s)){
			result = new IntegerNode(k,Integer.parseInt(s));
		} else if(isDbl(s)){
			result = new DoubleNode(k, Double.parseDouble(s));
		} else {
			result = new StringNode(k,s);
		}
		return result;
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/
