package com.peaceworld.wikisms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Comments {
 
	@Id
	@GeneratedValue
	private int id;
	@Column(length=1000)
	private String comment;
	private long time;
	private long userIdentifier;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getUserIdentifier() {
		return userIdentifier;
	}
	public void setUserIdentifier(long userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
	
}
