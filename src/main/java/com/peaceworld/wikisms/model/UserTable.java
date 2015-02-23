package com.peaceworld.wikisms.model;

import java.io.Serializable;

import javax.persistence.*;


@Entity

@NamedQueries({
@NamedQuery(name="UserTable.CountAll" , query="SELECT COUNT(user) FROM UserTable user"),
@NamedQuery(name="UserTable.GetAll" , query="SELECT user FROM UserTable user"),
@NamedQuery(name="UserTable.GetAllRequested" , query="SELECT user FROM UserTable user WHERE user.userIdentifier IN (:Ids)"),
@NamedQuery(name="UserTable.GetByIdentifier" , query="SELECT user FROM UserTable user WHERE user.userIdentifier = :Identifier"),
@NamedQuery(name="UserTable.GetByEmail" , query="SELECT user FROM UserTable user WHERE user.email = :email")

})
public class UserTable implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public UserTable() {
		super();
	}
	
	@Id
	@GeneratedValue
	private int id;
	private boolean isMobieUser;
	private String Name;
	private String email;
	private long userIdentifier;
	
	//the shown name of the user ; may not be unique
	@Column(length=5000)
	private String username;
	private String password;
	private String deviceInfo;
	private int administrationLevel;
	private String gender;
	private int age;
	private long registrationTime;
	private long lastInteractionTime;
	
	
	// shows how much credit this user has bought 
	private int credit;
	
	// shows how much active is this user in general
	private int participation;

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public int getAdministrationLevel() {
		return administrationLevel;
	}

	public void setAdministrationLevel(int administrationLevel) {
		this.administrationLevel = administrationLevel;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getParticipation() {
		return participation;
	}

	public void setParticipation(int participation) {
		this.participation = participation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isMobieUser() {
		return isMobieUser;
	}

	public void setMobieUser(boolean isMobieUser) {
		this.isMobieUser = isMobieUser;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(long registrationTime) {
		this.registrationTime = registrationTime;
	}

	public long getLastInteractionTime() {
		return lastInteractionTime;
	}

	public void setLastInteractionTime(long lastInteractionTime) {
		this.lastInteractionTime = lastInteractionTime;
	}

	public long getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(long userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
	public String toString()
	{
		return "User Name: "+username+"\nUser Identifier: "+userIdentifier+"\r\nEmail: "+email+
				"\r\nPassword: "+password+"\r\nDevide Info: "+deviceInfo;
	}
   
	
}
