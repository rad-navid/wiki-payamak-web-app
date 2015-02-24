package com.peaceworld.wikisms.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: MessagesTable
 *
 */
@XmlRootElement
@Entity

@NamedQueries({

@NamedQuery(name="Content.GetAllContentes", query="SELECT cnt FROM Content cnt"),
@NamedQuery(name="Content.GetAllUnApprovedContentesById", query="SELECT cnt FROM Content cnt WHERE cnt.approved=FALSE AND cnt.id > :cntId "),
@NamedQuery(name="Content.GetAllByCategory", query="SELECT cnt FROM Content cnt WHERE cnt.contentTag LIKE :categoryId"),
@NamedQuery(name="Content.GetChangeLogById", query="SELECT cnt FROM Content cnt WHERE cnt.approved=TRUE AND cnt.id > :cntId  AND cnt.lastChengedTime > :lastChangeTime"),
@NamedQuery(name="Content.GetContentsByCategory", query="SELECT cnt FROM Content cnt WHERE cnt.approved=TRUE  AND  cnt.id > :cntId  AND cnt.contentTag LIKE :likequery"),
@NamedQuery(name="Content.GetContentsCountByCategory", query="SELECT COUNT(cnt) FROM Content cnt WHERE cnt.approved=TRUE  AND  cnt.contentTag LIKE :likequery"),
@NamedQuery(name="Content.GetMostLiked", query="SELECT cnt FROM Content cnt WHERE cnt.approved=TRUE ORDER BY cnt.likedCounter DESC"),
@NamedQuery(name="Content.GetMostViewed", query="SELECT cnt FROM Content cnt WHERE cnt.approved=TRUE ORDER BY cnt.viewedCounter DESC"),
@NamedQuery(name="Content.GetMostRecent", query="SELECT cnt FROM Content cnt WHERE cnt.approved=TRUE ORDER BY cnt.insertionDateTime DESC"),
@NamedQuery(name="Content.GetUserContentes", query="SELECT cnt FROM Content cnt WHERE cnt.creatorUser= :userIdentifier")

})


public class Content implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Content() {
		super();
	}
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(length=10000)
	private String encriptedText;
	@Column(length=10000)
	private String plainText;
	
	private int administrationLevel;
	
	@Column(length=2000)
	private String contentTag;

	private long insertionDateTime;
	private long lastChengedTime;
	
	private int likedCounter;
	private int viewedCounter;
	
	private boolean approved;
	private long creatorUser;
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getAdministrationLevel() {
		return administrationLevel;
	}
	public void setAdministrationLevel(int administrationLevel) {
		this.administrationLevel = administrationLevel;
	}
	public String getContentTag() {
		return contentTag;
	}
	public void setContentTag(String contentTag) {
		this.contentTag = contentTag;
	}
	
	public int getLikedCounter() {
		return likedCounter;
	}
	public void setLikedCounter(int likedCounter) {
		this.likedCounter = likedCounter;
	}
	public int getViewedCounter() {
		return viewedCounter;
	}
	public void setViewedCounter(int viewedCounter) {
		this.viewedCounter = viewedCounter;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public long getInsertionDateTime() {
		return insertionDateTime;
	}
	public void setInsertionDateTime(long insertionDateTime) {
		this.insertionDateTime = insertionDateTime;
	}
	public long getCreatorUser() {
		return creatorUser;
	}
	public void setCreatorUser(long creatorUser) {
		this.creatorUser = creatorUser;
	}
	public long getLastChengedTime() {
		return lastChengedTime;
	}
	public void setLastChengedTime(long lastChengedTime) {
		this.lastChengedTime = lastChengedTime;
	}
	public String getEncriptedText() {
		return encriptedText;
	}
	public void setEncriptedText(String encriptedText) {
		this.encriptedText = encriptedText;
	}
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	
		
}
