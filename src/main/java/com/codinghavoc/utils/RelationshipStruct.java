package com.codinghavoc.utils;

public class RelationshipStruct {
	private RelationshipItem tgtRI;
	private RelationshipItem srcRI;
	
	public RelationshipStruct(){}
	
	//Both target and source Pages are in Folders
	public RelationshipStruct(RelationshipItem tgt, RelationshipItem src){
		tgtRI = tgt;
		srcRI = src;
	}
	
	public RelationshipItem getSrcRI() {
		return srcRI;
	}

	public RelationshipItem getTgtRI() {
		return tgtRI;
	}

	public void setSrcRI(RelationshipItem sr) {
		srcRI = sr;
	}

	public void setTgtRI(RelationshipItem tr) {
		tgtRI = tr;
	}

	@Override
	public String toString(){
		return tgtRI+"---"+srcRI;
	}
}
