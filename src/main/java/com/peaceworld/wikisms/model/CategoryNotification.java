package com.peaceworld.wikisms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;

/**
 * Entity implementation class for Entity: CategoryNotification
 *
 */
@XmlRootElement
@Entity

@NamedQueries({

@NamedQuery(name="CategoryNotification.GetAll", query="SELECT ccn FROM CategoryNotification ccn"),
@NamedQuery(name="CategoryNotification.GetById", query="SELECT ccn FROM CategoryNotification ccn WHERE ccn.id = :ccnId"),
@NamedQuery(name="CategoryNotification.GetDeadCCNs", query="SELECT ccn FROM CategoryNotification ccn WHERE  ( ccn.accepted + ccn.denied ) >= :sent OR ccn.lastSentTime <= :lastSentTime"),
@NamedQuery(name="CategoryNotification.CountAll" , query="SELECT COUNT(ccn) FROM CategoryNotification ccn"),
@NamedQuery(name="CategoryNotification.GetAllIds" , query="SELECT ccn.id FROM CategoryNotification ccn WHERE ccn.id IN :idSet"),
@NamedQuery(name="CategoryNotification.GetAllByCreator", query="SELECT ccn FROM CategoryNotification ccn WHERE ccn.creatorUser = :creatorUser")
})


public class CategoryNotification  {

	public static enum ACTION 
	{CATEGORY_CREATE,CATEGORY_DELETE,CATEGORY_CHANGED,CATEGORY_MERGE,CATEGORY_MOVE}

	public CategoryNotification() {
		super();
	}
	
	/**
	 * Auto Generated global unique id  
	 */
	
	@Id
	private long id;
	
	/**
	 * action CATEGORY_CREATE : new category Auto generated global unique id 
	 * action CATEGORY_MOVE: id of the category to be moved
	 * action CATEGORY_MERGE: id of the first category
	 * action CATEGORY_CHANGED : id of the category to be changed
	 * action CATEGORY_DELETE : id of the category to be deleted
	 */
	private long operand1;
	
	/**
	 * action CATEGORY_CREATE : new category parent id
	 * action CATEGORY_MOVE: id of the moved category parent
	 * action CATEGORY_MERGE: id of the second category
	 */
	private long operand2;
	
	/**
	 *  action CATEGORY_CREATE : new category name
	 *  action CATEGORY_MERGE: name of the new category
	 *  action CATEGORY_CHANGED : the new name of the changed category
	 */
	@Column(length=10000)
	private String metadata;
	private String action;
	private long creatorUser;
	
	private int administrationLevel;
	private long lastSentTime;
	private long accepted;
	private long denied;
	private long sent;
	
	@Column(length=5000)
	private String comment;

		
	
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


	public int getAdministrationLevel() {
		return administrationLevel;
	}



	public void setAdministrationLevel(int administrationLevel) {
		this.administrationLevel = administrationLevel;
	}



	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}



	public long getAccepted() {
		return accepted;
	}



	public void setAccepted(long accepted) {
		this.accepted = accepted;
	}



	public long getDenied() {
		return denied;
	}



	public void setDenied(long denied) {
		this.denied = denied;
	}



	public long getLastSentTime() {
		return lastSentTime;
	}



	public void setLastSentTime(long lastSentTime) {
		this.lastSentTime = lastSentTime;
	}



	public long getSent() {
		return sent;
	}



	public void setSent(long sent) {
		this.sent = sent;
	}



	public long getCreatorUser() {
		return creatorUser;
	}



	public void setCreatorUser(long creatorUser) {
		this.creatorUser = creatorUser;
	}



	public String toString()
	{
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	
}
