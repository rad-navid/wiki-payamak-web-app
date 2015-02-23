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
import com.peaceworld.wikisms.dao.CategoryNotificationDao;
import com.peaceworld.wikisms.model.CategoryNotification;

@Stateless
@Path("/ccn")
public class CategoryNotificationServices {
	
	@EJB
	CategoryManager categoryManager;
	@EJB
	ContentManager contentManager;
	@EJB
	private CategoryNotificationDao ccnDao;
	

	@POST
	@Path("/getallccns")
	public String getAllCCNS(@FormParam("exclude")String exclude, @FormParam("identifier")long identifier, @FormParam("limit") int limit,
			@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<CategoryNotification>ccns=ccnDao.nativeQuery1(identifier,exclude,limit);
			for(CategoryNotification ccn:ccns)
			{
				ccn.setLastSentTime(System.currentTimeMillis());
				ccn.setSent(ccn.getSent()+1);
			}
			String gson=Utility.toJson(ccns);
			
			result= gson;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	@POST
	@Path("/addccns")
	public String addNewCategoryNotificationes(@FormParam("ccns")String receivedCCNs,
			@Context HttpServletRequest httpResuest)
	
	{
		String result="";
		try {
			Gson gson=new Gson();
			List<CategoryNotification> ccns = null;
			ccns = 	(List<CategoryNotification>)gson.fromJson(receivedCCNs, new TypeToken<List<CategoryNotification>>() {}.getType());

			boolean success=ccnDao.addAll(ccns);
			if(success)
				result = "successful";
			else
				result="failed";
			
		} catch (Exception e) {
			result = e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	@POST
	@Path("/getallavailable")
	public String getAllAvailable(@FormParam("ccns")String ccnIds,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<Long>ccns=ccnDao.getAllAvailable(ccnIds);
			String gson=Utility.toJson(ccns);
			result= gson;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	@POST
	@Path("/updateccns")
	public String updateState(@FormParam("ccnsstate")String ccnsstate,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			boolean success=ccnDao.updateStates(ccnsstate);
			categoryManager.verifyRequests();
			contentManager.verifyRequests();
			
			if(success)
				result= "successful";
			else
				result="failed";
			
		} catch (Exception e) {
			e.printStackTrace();
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	


	
}
