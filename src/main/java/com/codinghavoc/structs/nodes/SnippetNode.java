package com.codinghavoc.structs.nodes;

public class SnippetNode extends BaseNode{
	private String[] nValue;
	
	public SnippetNode(String k, String v){
		nKey = k;
		nValue = v.split("\\r?\\n");
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String s : nValue){
			sb.append(s + "\n");
		}
		return sb.toString();
	}
	
	public String[] getnValue(){
		return nValue;
	}
}
