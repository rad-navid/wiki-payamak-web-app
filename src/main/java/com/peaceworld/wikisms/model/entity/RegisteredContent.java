package com.peaceworld.wikisms.model.entity;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.model.Content;

public class RegisteredContent {
	
	private String content,date;
	private int liked,viewd;


	public RegisteredContent(Content content) {
		
		this.content=content.getPlainText();
		this.date=Utility.milisecondsToJalaliDateTime(content.getInsertionDateTime());
		this.liked=content.getLikedCounter();
		this.viewd=content.getViewedCounter();
	}


	public String getContent() {
		return content;
	}


	public String getDate() {
		return date;
	}


	public int getLiked() {
		return liked;
	}


	public int getViewd() {
		return viewd;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public void setLiked(int liked) {
		this.liked = liked;
	}


	public void setViewd(int viewd) {
		this.viewd = viewd;
	}

}
