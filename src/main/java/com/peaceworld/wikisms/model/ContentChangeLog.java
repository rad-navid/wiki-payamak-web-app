package com.peaceworld.wikisms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity

@NamedQueries({

@NamedQuery(name="ContentChangeLog.GetAll", query="SELECT clog FROM ContentChangeLog clog"),

@NamedQuery(name="ContentChangeLog.GetById", query="SELECT clog FROM ContentChangeLog clog WHERE clog.id = :id"),
@NamedQuery(name="ContentChangeLog.GetByIdAndTimeStamp", query="SELECT clog FROM ContentChangeLog clog WHERE clog.id> :Id AND clog.timeStamp > :timeStamp")

})

public class ContentChangeLog {

	@Id
	@GeneratedValue
	private long id;
	private long contentId;
	private String action;
	@Column(length=10000)
	private String  metaInfo;
	private long operand1;
	private long operand2;
	@Column(length=5000)
	private String comment;
	private long creatorUser;
	private long timeStamp;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMetaInfo() {
		return metaInfo;
	}
	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}
	public long getOperand1() {
		return operand1;
	}
	public void setOperand1(long operand1) {
		this.operand1 = operand1;
	}
	public long getOperand2() {
		return operand2;
	}
	public void setOperand2(long operand2) {
		this.operand2 = operand2;
	}
	public long getCreatorUser() {
		return creatorUser;
	}
	public void setCreatorUser(long creatorUser) {
		this.creatorUser = creatorUser;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
