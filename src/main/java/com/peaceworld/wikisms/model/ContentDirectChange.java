package com.peaceworld.wikisms.model;


public class ContentDirectChange {

	/** this field is the effected content id
	 * every single record indicate a content is viewed referenced by id
	*/ 
	long id;
	
	// if changed to 1 means this content is liked
	int liked=0,viewd=0;
	
	long timeStamp=0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLiked() {
		return liked;
	}

	public void setLiked(int liked) {
		this.liked = liked;
	}

	public int getViewd() {
		return viewd;
	}

	public void setViewd(int viewd) {
		this.viewd = viewd;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	

}
