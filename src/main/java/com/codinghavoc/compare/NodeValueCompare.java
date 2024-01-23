package com.codinghavoc.compare;

import java.util.ArrayList;
import java.util.Arrays;

import com.codinghavoc.enums.CompareResults;
import com.codinghavoc.structs.*;
import com.codinghavoc.structs.nodes.BaseNode;
import com.codinghavoc.structs.nodes.BooleanNode;
import com.codinghavoc.structs.nodes.DoubleNode;
import com.codinghavoc.structs.nodes.IntegerNode;
import com.codinghavoc.structs.nodes.StringNode;
import com.codinghavoc.utils.PrintDetails;
import com.codinghavoc.utils.RedirectInput;
import com.codinghavoc.utils.UserInput;

public class NodeValueCompare{
	
	private static CompareResults compareNodes(BaseNode n1, BaseNode n2){
		if(n1.getClass().toString().equals(n2.getClass().toString())){
			if((n1 instanceof IntegerNode || n1 instanceof DoubleNode) && (n2 instanceof IntegerNode || n2 instanceof DoubleNode)){
				DoubleNode d1 = (DoubleNode)n1;
				DoubleNode d2 = (DoubleNode)n2;
				if(d1.getnValue() < d2.getnValue()){
					//System.out.println(n1.getnValue()+" comes before "+n2.getnValue());
					return CompareResults.LOWER;
				} else if (d1.getnValue() > d2.getnValue()){
					//System.out.println(n1.getnValue()+" comes after "+n2.getnValue());
					return CompareResults.HIGHER;
				} else {
					//System.out.println("These values are equal");
					return CompareResults.EQUAL;
				}
			}
			if (n1 instanceof StringNode && n2 instanceof StringNode){
				StringNode s1 = (StringNode)n1;
				StringNode s2 = (StringNode)n2;
				int sortIndex = s1.getnValue().compareTo(s2.getnValue());
				if(sortIndex < 0){
					//System.out.println(n1.getnValue().toString()+" comes before "+n2.getnValue().toString());
					return CompareResults.LOWER;
				} else if (sortIndex > 0){
					//System.out.println(n1.getnValue().toString()+" comes after "+n2.getnValue().toString());
					return CompareResults.HIGHER;
				} else {
					//System.out.println("These values are equal");
					return CompareResults.EQUAL;
				}
			}
			if(n1 instanceof BooleanNode && n2 instanceof BooleanNode){
				BooleanNode b1 = (BooleanNode)n1;
				BooleanNode b2 = (BooleanNode)n2;
				int check1 = b1.isnValue() ? 1 : 0;
				int check2 = b2.isnValue() ? 1 : 0;
				if(check1 > check2){
					//System.out.println("n1 comes first");
					return CompareResults.LOWER; //this will change depending on how the sorting for true/false works
				} else if(check1 < check2){
					//System.out.println("n2 comes first");
					return CompareResults.HIGHER; //this will change depending on how the sorting for true/false works
				} else {
					System.out.println("They are equal");
					return CompareResults.EQUAL;
				}
			} else {
				//System.out.println("WTF");
				return CompareResults.UNKNOWN;
			}
		}
		//System.out.println("Different classes, cannot sort");
		return CompareResults.DIFF_CLASS;
	}
	
	public static void filter(FileCabinet fc, ArrayList<Page> d){
		boolean print = false;
		//ArrayList<Page> result = null;
		ArrayList<Page> working = new ArrayList<Page>();
		ArrayList<String> actions = new ArrayList<String>(Arrays.asList("sortasc","sortdesc","whereeq","wherene","wheregt","wheregte","wherelt","wherelte","exit"));
		String action = "placeholder";
		System.out.println("Select from the list below");
		for(int i = 0; i < actions.size(); i++){
			System.out.print("-:"+actions.get(i)+"\t");
			if(i>0 && i%3==0) System.out.println();
		}
		while(!actions.contains(action)){
			System.out.println("\nWhich filter would you like to apply?\nfilter>>>");
			action = UserInput.getString();
			if(!actions.contains(action)){
				System.out.println("Please select from the list");
			}
		}
		//Will need to be relooked with new structure
		switch(action){
		case "sortasc":
			//working = sortAsc(d);
			working = sort(d, "asc");
			print = true;
			break;
		case "sortdesc":
			//sortdesc
			working = sort(d, "desc");
			print = true;
			break;
		case "whereeq":
			//whereeq
			working = where(d,"eq");
			print=true;
			break;
		case "wherene":
			//wherene
			working = where(d,"ne");
			print = true;
			break;
		case "wheregt":
			//wheregt
			working = where(d,"gt");
			print = true;
			break;
		case "wheregte":
			//wheregte
			working = where(d,"gte");
			print = true;
			break;
		case "wherelt":
			//wherelt
			working = where(d,"lt");
			print = true;
			break;
		case "wherelte":
			//wherelte
			working = where(d,"lte");
			print = true;
			break;
		default:
			break;
		}
		//Will need to be relooked with new structure
		if(print){
			for(Page p : working){
				PrintDetails.printPage(fc, p);
			}
		}
	}

	//Will need to be relooked with new structure
	private static ArrayList<Page> sort(ArrayList<Page> i, String order){
		ArrayList<Page> result = new ArrayList<Page>();
		ArrayList<Page> working = new ArrayList<Page>();
		Page pageLeft = null;
		Page pageRight = null;
		Page tempPage = null;
		BaseNode n1 = null;
		BaseNode n2 = null;
		CompareResults testResults = null;
		int pos = 0;
		int tgtpos = 0;
		System.out.println("Enter the name of the field you want to sort by\nfilter->sort"+order+">>>");
		String field = UserInput.getString();
		for(Page p1 : i){
			if(p1.contain(field)){
				working.add(p1);
			}
		}
		while(working.size()>0){
			if(working.size()==1){
				result.add(working.get(0));
				working.remove(0);
			} else {
				if(tempPage == null){
					pageLeft = working.get(pos);
					pos++;
					pageRight = working.get(pos);
					n1 = pageLeft.selectNode(field);
					n2 = pageRight.selectNode(field);
					testResults = compareNodes(n1,n2);
					if(order.equals("asc")){
						if(testResults == CompareResults.LOWER){
							tempPage = pageLeft;
							tgtpos = 0;
						} else {
							tempPage = pageRight;
							tgtpos = 1;
						}						
					}
					if(order.equals("desc")){
						if(testResults == CompareResults.HIGHER){
							tempPage = pageLeft;
							tgtpos = 0;
						} else {
							tempPage = pageRight;
							tgtpos = 1;
						}						
					}
				} else {
					n1 = tempPage.selectNode(field);
					n2 = working.get(pos).selectNode(field);
					testResults = compareNodes(n1,n2);
					if(order.equals("asc")){
						if(testResults == CompareResults.LOWER){
							pos++;
						} else {
							tempPage = working.get(pos);
							tgtpos = pos;
							pos++;
						}
					}
					if(order.equals("desc")){
						if(testResults == CompareResults.HIGHER){
							pos++;
						} else {
							tempPage = working.get(pos);
							tgtpos = pos;
							pos++;
						}						
					}
				}
				if(pos==working.size()){
					pos = 0;
					result.add(tempPage);
					tempPage = null;
					working.remove(tgtpos);
				}				
			}
		}
		return result;
	}

	//Will need to be relooked with new structure
	private static ArrayList<Page> where(ArrayList<Page> i, String comp){
		ArrayList<Page> result = new ArrayList<Page>();
		ArrayList<Page> working = new ArrayList<Page>();
		CompareResults testResults = null;
		BaseNode n1 = null;
		System.out.println("Enter the name of the field you want to sort by\nfilter->where"+comp+">>>");
		String field = UserInput.getString();
		System.out.println("Enter the criteria\n>>>");
		BaseNode tgt = RedirectInput.redirectInput(field, UserInput.getString());
		for(Page p1 : i){
			if(p1.contain(field)){
				working.add(p1);
			}
		}
		for(Page p : working){
			n1 = p.selectNode(field);
			testResults = compareNodes(n1,tgt);
			//switch statement to take different actions
			switch(comp){
			case "eq":
				if(testResults == CompareResults.EQUAL){
					result.add(p);
				}
				break;
			case "ne":
				if(testResults != CompareResults.EQUAL){
					result.add(p);
				}
				break;
			case "lt":
				if(testResults == CompareResults.LOWER){
					result.add(p);
				}
				break;
			case "lte":
				if(testResults == CompareResults.LOWER || testResults == CompareResults.EQUAL){
					result.add(p);
				}
				break;
			case "gt":
				if(testResults == CompareResults.HIGHER){
					result.add(p);
				}
				break;
			case "gte":
				if(testResults == CompareResults.HIGHER || testResults == CompareResults.EQUAL){
					result.add(p);					
				}
				break;
			}
		}
		return result;
	}
}


/*All code below this line is obsolete, but keeping it around for reference*/