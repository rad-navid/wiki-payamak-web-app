package com.peaceworld.wikisms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;

@XmlRootElement
@Entity

@NamedQueries({

@NamedQuery(name="ContentNotification.GetAll", query="SELECT cn FROM ContentNotification cn"),
@NamedQuery(name="ContentNotification.GetById", query="SELECT cn FROM ContentNotification cn WHERE cn.id = :cnId"),
@NamedQuery(name="ContentNotification.GetByCreatorUser", query="SELECT cn FROM ContentNotification cn WHERE cn.creatorUser = :cnCreatorUser"),
@NamedQuery(name="ContentNotification.GetDeadCNs", query="SELECT cn FROM ContentNotification cn WHERE ( cn.accepted + cn.denied)  >= :sent OR cn.lastSentTime < :lastSentTime"),
@NamedQuery(name="ContentNotification.CountAll" , query="SELECT COUNT(cn) FROM ContentNotification cn"),
@NamedQuery(name="ContentNotification.GetAllIds" , query="SELECT cn.id FROM ContentNotification cn WHERE cn.id IN :idSet"),
@NamedQuery(name="ContentNotification.GetAllByCreatorUser", query="SELECT cn FROM ContentNotification cn WHERE cn.creatorUser = :creatorUser"),
})

public class ContentNotification {

	public static enum ACTION 
		{	CONTENT_CREATE,CONTENT_DELETE,CONTENT_EDIT,
				CONTENT_TAG_ADDED,CONTENT_TAG_CHANGED}
	   
	@Id
	private long id;
	/**
	 * id of the content to be affected
	 */
	private long contentId;
	private String action;
	/**
	 * action CONTENT_CREATE : new content value
	 * action CONTENT_EDIT : edited content value
	 */
	@Column(length=10000)
	private String  metaInfo;
	
	/**
	 * action CONTENT_TAG_CHANGED : id of the category to be changed
	 * action CONTENT_CATEGORY_ADDED : id of the category to be added
	 * action CONTENT_CREATE : id of the parent category (main and probably unaccepted parent)
	 */
	private long operand1;
	
	/**
	 * action CONTENT_TAG_CHANGED : id of the category to be replaced
	 * action CONTENT_CREATE : id of the parent category (first accepted parent)
	 * 
	 */
	private long operand2;
	
	private int administrationLevel;
	private long lastSentTime;
	private long accepted;
	private long denied;
	private long sent;
	
	@Column(length=5000)
	private String comment;
	private long creatorUser;

	
		
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
