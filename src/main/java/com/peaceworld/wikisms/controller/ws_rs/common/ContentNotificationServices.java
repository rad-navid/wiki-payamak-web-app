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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.controller.manager.CategoryManager;
import com.peaceworld.wikisms.controller.manager.ContentManager;
import com.peaceworld.wikisms.dao.ContentNotificationDao;
import com.peaceworld.wikisms.model.ContentNotification;

@Stateless
@Path("/cn")
public class ContentNotificationServices {
	
	@EJB
	CategoryManager categoryManager;
	@EJB
	ContentManager contentManager;
	@EJB
	private ContentNotificationDao cnDao;
	
	@POST
	@Path("/getallcns")
	public String getAllCNs(@FormParam("exclude")String exclude, @FormParam("identifier")long identifier,
			@FormParam("limit") int limit,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<ContentNotification>cns=cnDao.nativeQuery1(identifier,exclude,limit);
			String gson=Utility.toJson(cns);
			result= gson;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	@POST
	@Path("/getallcnsbycreator")
	public String getAllByCreator(@FormParam("exclude")String exclude,@FormParam("creatorsId")String creatorsId,
			@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<ContentNotification>cns=cnDao.nativeQuery2(creatorsId,exclude);
			String gson=Utility.toJson(cns);
			
			result= gson;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	@POST
	@Path("/addcns")
	public String addNewContentNotifications(@FormParam("cns")String receivedCNs,@Context HttpServletRequest httpResuest)
	
	{
		String result="";
		try {
			Gson gson=new Gson();
			List<ContentNotification> cns = null;
			cns = (List<ContentNotification>)gson.fromJson(receivedCNs, new TypeToken<List<ContentNotification>>() {}.getType());

			boolean success=cnDao.addAll(cns);
			if(success)
				result= "successful";
			else
				result="failed";
			
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	@POST
	@Path("/getallavailable")
	public String getAllAvailable(@FormParam("cns")String cnIds,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<Long>ccns=cnDao.getAllAvailable(cnIds);
			String gson=Utility.toJson(ccns);
			result= gson;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	@POST
	@Path("/updatecns")
	public String updateState(@FormParam("cnsstate")String cnsstate,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			boolean success=cnDao.updateState(cnsstate);
			categoryManager.verifyRequests();
			contentManager.verifyRequests();
			
			if(success)
				result= "successful";
			else 
				result= "failed";
			
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
}
