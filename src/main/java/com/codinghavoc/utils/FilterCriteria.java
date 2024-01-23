package com.codinghavoc.utils;

public class FilterCriteria {
	private String filterType;
	private String tgtKey;
	private String tgtValue;
	
	public FilterCriteria(){}
	
	//Used for sorting
	public FilterCriteria(String f, String k){
		filterType = f;
		tgtKey = k;
	}
	
	//Used for where
	public FilterCriteria(String f, String k, String v){
		filterType = f;
		tgtKey = k;
		tgtValue = v;
	}
	
	//Getters and Setters

	public String getFilterType() {
		return filterType;
	}

	public String getTgtKey() {
		return tgtKey;
	}

	public String getTgtValue() {
		return tgtValue;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public void setTgtKey(String tgtKey) {
		this.tgtKey = tgtKey;
	}

	public void setTgtValue(String tgtValue) {
		this.tgtValue = tgtValue;
	}

}
