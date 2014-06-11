package com.akdeniz.googleplaycrawler.cli.model;

import java.text.DateFormat;

/**
 * 评论实体
 * @author sandro
 *
 */
public class ReviewModel {

	private String authorName;
	
	private String title;
	
	private String comment;
	
	private int starRating;
	
	private long time;
	
	public ReviewModel(String authorName,String title,String comment,int starRating,long time){
		this.authorName = authorName;
		this.title = title;
		this.comment = comment;
		this.starRating = starRating;
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n");
//		sb.append("authorName : " + this.authorName);
		sb.append("starRating : " + starRating);
		sb.append(" ,title : " + this.title);
		sb.append(" ,time : " + DateFormat.getDateInstance(DateFormat.DEFAULT).format(time));
		sb.append("\n");
		sb.append("comment : " + comment);
		
		return sb.toString();	
	}

	public int getStarRating() {
		return starRating;
	}
}
