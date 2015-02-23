package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.controller.ws_rs.common.UserServices;
import com.peaceworld.wikisms.dao.CommentDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.dao.UserDao;
import com.peaceworld.wikisms.model.UserTable;
import com.peaceworld.wikisms.model.entity.OnlineActivity;
import com.peaceworld.wikisms.model.entity.RegisteredContent;

@ManagedBean
@SessionScoped
public class UserPanelBean implements Serializable {
	
	private static final long serialVersionUID = 6064609277232286919L;
	
	@EJB
	UserDao userDao;
	@EJB
	CommentDao commentDao;
	@EJB
	ContentDao contentDao;
	@EJB 
	ReportManager reportManager;
	
	private enum Mod{NONE,CHANGE_PASSWORD,USER_STATUS,CONTACT_US,NEW_SMS,ONLINE_ACTIVITIES,REGISTERED_CONTENTS}
	
	private String passwordVerify, newPass,oldPass,newEmailAddress;
	private String emailUsContent,newSmsContent;
	
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	private UserTable user;
	private boolean logedIn;
	
	private Mod mMode=Mod.USER_STATUS;
	
	private ArrayList<OnlineActivity> onlineActivities;
	private ArrayList<RegisteredContent> registeredContents;


	@PostConstruct
	public void firstGreating()
	{
		try{
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setMaxInactiveInterval(1*60*60);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		user=userBean.getUser();
		FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(" عزیز "+user.getUsername()," به ویکی پیامک خوش آمدید "));
	}
	
	public void newSmsMod(){
		mMode=Mod.NEW_SMS;
	}
	public void contactUsMod(){
		mMode=Mod.CONTACT_US;
	}
	public void changePasswordMod(){
		mMode=Mod.CHANGE_PASSWORD;
	}
	public void userStatusMod(){
		mMode=Mod.USER_STATUS;
	}
	public void onlineActivityMod(){
		onlineActivities=reportManager.getAllOnlineActivities(userBean.getUser(),100);
		mMode=Mod.ONLINE_ACTIVITIES;
	}
	public void registeredContentsMod(){
		mMode=Mod.REGISTERED_CONTENTS;
		registeredContents=reportManager.getUserContents(user, 100);
	}

	
	public boolean render(int section)
	{
		
		switch(section)
		{
		case 1:
			if(mMode==Mod.NEW_SMS)
				return true;
			else return false;
		case 2:
			if(mMode==Mod.CHANGE_PASSWORD)
				return true;
			else return false;
		case 3:
			if(mMode==Mod.ONLINE_ACTIVITIES)
				return true;
			else return false;
		case 4:
			if(mMode==Mod.USER_STATUS)
				return true;
			else return false;
		case 5:
			if(mMode==Mod.REGISTERED_CONTENTS)
				return true;
			else return false;
		case 6:
			if(mMode==Mod.CONTACT_US)
				return true;
			else return false;
			
		default:
			return false;
			
		}
	}
	
	public void changePassword()
	{
		if(user.getPassword().trim().compareTo(oldPass.trim())==0)
		{
			boolean result=userDao.resetPassword(user.getEmail(), newPass.trim());
			if(result)
			{
				user=userDao.getUserByEmail(user.getEmail());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("تغییرات با موفقیت اعمال شدند",""));
				mMode=Mod.USER_STATUS;
				return;
			}
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("کلمه عبور اشتباه است","لطفاً مجدداً تلاش کنید"));
		}
		FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("اعمال تغییرات نا موفق بود","لطفاً بعداً مجدداً تلاش کنید"));
	}
	
	public void sendEmail()
	{
		
		if(commentDao!=null)
		{
			String plainMailContent=Utility.HtmlToPlainText(emailUsContent);
			boolean result=commentDao.CreateNewCommnet(plainMailContent, user.getUserIdentifier());
			if(result)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("با موفقیت ارسال شد","پیشاپیش از تماس شما متشکریم"));
				mMode=Mod.USER_STATUS;
				emailUsContent="";
				return;
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ارسال ناموفق بود","لطفاً بعداً مجدداً تلاش کنید"));
		
	}
	public String sendEmailAndReturnToHome()
	{
		sendEmail();
		return "pretty:index";
	}
	
	public void sendNewSms()
	{
		if(contentDao!=null)
		{
			String plainSmsContent=Utility.HtmlToPlainText(newSmsContent);
			boolean result=contentDao.createUnverifiedNewContent(plainSmsContent, "", user.getUserIdentifier());
			if(result)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("با موفقیت ارسال شد","پیشاپیش از همکاری شما متشکریم"));
				mMode=Mod.USER_STATUS;
				newSmsContent="";
				return;
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ارسال ناموفق بود","لطفاً بعداً مجدداً تلاش کنید"));
	}
	
	public String logout()
	{
		try{
			FacesContext facesContext=FacesContext.getCurrentInstance(); 
			ExternalContext externalContext=facesContext.getExternalContext();
			externalContext.invalidateSession();
			onlineActivities.clear();
			onlineActivities=null;
		}
		catch(Exception e)
		{
			
		}
		return "pretty:login";
	}
	
	public String showUserAccessLevel()
	{
		switch(user.getAdministrationLevel())
		{
			case 0:
				return "کاربر عادی";
			
		}
		return "تعریف نشده";
	}
	public String showUserAccessLevelDesc()
	{
		switch(user.getAdministrationLevel())
		{
			case 0:
				return "شما با این سطح دسترسی میتوانید همه پیامک ها را مشاهده کنید، پیامک های جدید ارسال کنید. ";
			
		}
		return "تعریف نشده";
	}
	
	public String showUserRegistrationTime()
	{
		return Utility.milisecondsToJalaliDateTime(user.getRegistrationTime());
	}
	
	
	public boolean isLogedIn() {
		return logedIn;
	}

	public void setLogedIn(boolean logedIn) {
		this.logedIn = logedIn;
	}
	

	public String getPasswordVerify() {
		return passwordVerify;
	}

	public void setPasswordVerify(String passwordVerify) {
		this.passwordVerify = passwordVerify;
	}


	public Mod getmMode() {
		return mMode;
	}
	public void setmMode(Mod mod) {
		mMode=mod;
	}

	public ArrayList<OnlineActivity> getOnlineActivities() {
		return onlineActivities;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public String getNewEmailAddress() {
		return newEmailAddress;
	}

	public String getEmailUsContent() {
		return emailUsContent;
	}

	public String getNewSmsContent() {
		return newSmsContent;
	}

	public void setNewEmailAddress(String newEmailAddress) {
		this.newEmailAddress = newEmailAddress;
	}

	public void setEmailUsContent(String emailUsContent) {
		this.emailUsContent = emailUsContent;
	}

	public void setNewSmsContent(String newSmsContent) {
		this.newSmsContent = newSmsContent;
	}

	public UserTable getUser() {
		return userBean.getUser();
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public void setUser(UserTable user) {
		this.user = user;
	}

	public ArrayList<RegisteredContent> getRegisteredContents() {
		return registeredContents;
	}


}
