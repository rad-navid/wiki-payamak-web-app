package com.peaceworld.wikisms.controller.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.peaceworld.wikisms.controller.ws_rs.common.UserServices;
import com.peaceworld.wikisms.dao.UserDao;
import com.peaceworld.wikisms.model.UserTable;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable{
	
	private static final long serialVersionUID = -5144154646135500927L;
	
	@EJB
	UserDao userDao;
	@EJB 
	MailBean mailBean;
	
	private String passwordVerify;
	private UserTable user;
	private boolean logedIn=false;
	private long userVerificationCode;
	
	public UserBean()
	{
		user=new UserTable();
		logedIn=false;
	}
	
	public String loginByEmail()
	{
		try {
			if (userDao != null) {
				String email = user.getEmail();
				UserTable tmpUser = userDao.getUserByEmail(email.trim());
				if(tmpUser!=null && user.getPassword().trim().compareTo(tmpUser.getPassword().trim())==0)
				{
					user=tmpUser;
					setLogedIn(true);
					return "pretty:userpanel";
				}
				else
				{
					FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("نام کاربری یا کلمه عبور اشتباه است","لطفاً مجدداً تلاش کنید"));
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	public String loginByuserIdentifier()
	{
		try {
			if (userDao != null) {
				long userId = user.getUserIdentifier();
				user = userDao.getUserByUserIdentifier(userId);
				if (user != null) {
					setLogedIn(true);
					return "pretty:userpanel";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	
	public String signup()
	{
		try {
			generateUserIdentifier();
			sendUserVerificationEmail();
			return "pretty:useractivation";
			
		} catch (Exception e) {
			return "";
		}

	}
	
	
	public String completeUserRegistration() {
		
		try {
			if(userVerificationCode==user.getUserIdentifier())
			{
				user.setRegistrationTime(System.currentTimeMillis());
				user.setMobieUser(false);
				boolean result=userDao.CreateUser(user);
				
				if(result)
				{
					setLogedIn(true);
					return "pretty:userpanel";
				}
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("کد رمز اشتباه است","لطفاً مجدداً تلاش کنید"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("خطا در ثبت‌نام","لطفاً بعداً مجدداً تلاش کنید"));
		}
		
		return "";
	}
	
	public String resetPasswordAndEmailToUser()
	{
		try {
			if (userDao != null) {
				String email = user.getEmail();
				UserTable tmpUser = userDao.getUserByEmail(email.trim());
				if(tmpUser!=null )
				{
					String newPass=generateNewPassword();
					boolean changePass=userDao.resetPassword(email.trim(),newPass.trim());
					boolean sentEmail=sendResetPassEmail(newPass);
					if(changePass && sentEmail)
						return "pretty:login";
				}
				else
				{
					FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("کاربری با این ایمیل ثبت نشده است","لطفاً مجدداً تلاش کنید"));
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "pretty:resetpass";
	}

	private String generateNewPassword() {
		String chars="123456789asdfghjklpoiuytrewqzxcvbnm";
		String newPass="";
		for(int i=0;i<10;i++)
			newPass+=chars.charAt( (int)(Math.random()*chars.length()) );
		return newPass;
	}
	
public boolean sendResetPassEmail(String newPass) {
		
		try {
			
			ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			InputStream is = context.getResourceAsStream("/resources/template/reset_pass_email.html");
			Scanner sc = new Scanner(is, "UTF-8");
			String response = "";
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				response += line;
				if (line.contains("id=\"newpass\""))
					response += newPass;

			}
			boolean emailSent = mailBean.generateAndSendEmail(user.getEmail(), response,"کلمه عبور جدید");
			if (emailSent) {
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("با موفقیت ارسال شد","برای تکمیل ثبت نام ایمیل خود را چک کنید"));
				return true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ایمیل ارسال نشد","بعداً مجدداً تلاش کنید"));
		return false;
	}

	public boolean sendUserVerificationEmail() {
		
		try {
			
			ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			InputStream is = context.getResourceAsStream("/resources/template/user_verification_email.html");
			Scanner sc = new Scanner(is, "UTF-8");
			String response = "";
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				response += line;
				if (line.contains("id=\"userIdentifier\""))
					response += user.getUserIdentifier();

			}
		//	FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("در حال ارسال ایمیل","لطفاً چند لحظه صبر کنید"));
			boolean emailSent = mailBean.generateAndSendEmail(user.getEmail(), response,"کد فعال سازی");
			if (emailSent) {
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage("با موفقیت ارسال شد","برای تکمیل ثبت نام ایمیل خود را چک کنید"));
				return true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ایمیل ارسال نشد","بعداً مجدداً تلاش کنید"));
		return false;
	}

	private void generateUserIdentifier()
	{
		long id=0;
		do{
			id=(long)(Math.random()*Math.pow(10, 15));
		}while((id+"").length()<15);
		user.setUserIdentifier(id);
	}


	public String getPasswordVerify() {
		return passwordVerify;
	}



	public UserTable getUser() {
		return user;
	}



	public boolean isLogedIn() {
		return logedIn;
	}



	public void setPasswordVerify(String passwordVerify) {
		this.passwordVerify = passwordVerify;
	}



	public void setUser(UserTable user) {
		this.user = user;
	}



	public void setLogedIn(boolean logedIn) {
		this.logedIn = logedIn;
	}

	public long getUserVerificationCode() {
		return userVerificationCode;
	}

	public void setUserVerificationCode(long userVerificationCode) {
		this.userVerificationCode = userVerificationCode;
	}


}
