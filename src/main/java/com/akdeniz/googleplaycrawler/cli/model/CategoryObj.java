package com.akdeniz.googleplaycrawler.cli.model;

/**
 * 
 * @author sandro
 *
 */
public class CategoryObj {
	private String id;
	private String name;
	
	public CategoryObj(String id,String name){
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "id : " + id + " , name : " + name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
