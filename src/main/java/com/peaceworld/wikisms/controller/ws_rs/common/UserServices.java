package com.peaceworld.wikisms.controller.ws_rs.common;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.dao.ChangeLogDao;
import com.peaceworld.wikisms.dao.UserDao;
import com.peaceworld.wikisms.model.GeneralChangeLog;
import com.peaceworld.wikisms.model.UserTable;

@Stateless
@Path("/user")
public class UserServices {
	 
	@EJB
	private UserDao userDao;
	@EJB
	private ChangeLogDao changeLogDao;

	@Path("/create")
	@POST
	public String CreateUser(@FormParam("UserIdentifier")long UserIdentifier,@FormParam("UserName")String UserName,
			@FormParam("DeviceInfo")String deviceInfo,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			UserTable user=userDao.getUserByUserIdentifier(UserIdentifier);
			if(user==null)
			{	
				user=new UserTable();
				user.setAdministrationLevel(0);
				user.setCredit(0);
				user.setDeviceInfo(deviceInfo);
				user.setUserIdentifier(UserIdentifier);
				user.setUsername(UserName);
				user.setMobieUser(true);
				user.setRegistrationTime(System.currentTimeMillis());
				userDao.CreateUser(user);
			}
			else
			{
				user.setDeviceInfo(deviceInfo);
				user.setUsername(UserName);
				GeneralChangeLog newChangeLog=new GeneralChangeLog(user.getId(), "UserTable",
						GeneralChangeLog.ChangeLogActions.USER_USERNAME_UPDATED.toString(), System.currentTimeMillis());
			
				changeLogDao.addGeneralLog(newChangeLog);
			}
			
			result= "successful";
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	
	}
	
	@Path("/getall")
	@POST
	public String getAllUsers(@FormParam("RequestIds")String requestIds,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			List<Long>idList=new ArrayList<Long>(100);
			String ids[]=requestIds.split(",");
			for(int i=0;i<ids.length;i++)
			{
				if(ids[i]==null ||ids[i]=="")
					continue;
				try{
					idList.add(Long.parseLong(ids[i]));
				}catch(Exception exx){};
			}
			if(idList!=null && idList.size()>0)
			{
				ArrayList<UserTable>userList=userDao.getUsersById(idList);
				for(UserTable user:userList)
				{
					result+=user.getUserIdentifier()+":"+user.getUsername()+";";
				}
			}
			String compressed=Utility.gzipCompress(result);
			result= compressed;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	
	}
	
	
}

