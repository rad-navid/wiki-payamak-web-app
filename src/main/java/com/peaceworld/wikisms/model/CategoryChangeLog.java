package com.peaceworld.wikisms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: CategoryNotification
 *
 */
@XmlRootElement
@Entity

@NamedQueries({

@NamedQuery(name="CategoryChangeLog.GetAll", query="SELECT cclog FROM CategoryChangeLog cclog"),
@NamedQuery(name="CategoryChangeLog.GetById", query="SELECT cclog FROM CategoryChangeLog cclog WHERE cclog.id = :id"),
@NamedQuery(name="CategoryChangeLog.GetByIdAndTimeStamp", query="SELECT cclog FROM CategoryChangeLog cclog WHERE cclog.id> :Id AND cclog.timeStamp > :timeStamp")

})


public class CategoryChangeLog  {

	public CategoryChangeLog() {
		super();
	}
	
	/**
	 * Auto Generated global unique id  
	 */
	
	@Id
	@GeneratedValue
	private long id;
	private long operand1;
	private long operand2;
	@Column(length=10000)
	private String metadata;
	private String action;
	private long creatorUser;	
	private long timeStamp;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
		
	
	
}
