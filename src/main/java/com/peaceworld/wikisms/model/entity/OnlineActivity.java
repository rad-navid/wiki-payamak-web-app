package com.peaceworld.wikisms.model.entity;

import com.peaceworld.wikisms.controller.Utility;

public class OnlineActivity {

	private String description,date;
	private long accepted,denied;
	
	public OnlineActivity(String description, long date, long accepted,
			long denied) {
		this.description = description;

		this.date = Utility.milisecondsToJalaliDateTime(date);
		this.accepted = accepted;
		this.denied = denied;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}

	public long getAccepted() {
		return accepted;
	}

	public long getDenied() {
		return denied;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setAccepted(long accepted) {
		this.accepted = accepted;
	}

	public void setDenied(long denied) {
		this.denied = denied;
	}

}
