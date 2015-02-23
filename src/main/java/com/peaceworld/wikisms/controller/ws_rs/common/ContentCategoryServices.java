package com.peaceworld.wikisms.controller.ws_rs.common;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.dao.CategoryDao;
import com.peaceworld.wikisms.model.ContentCategory;

@Stateless
@Path("/category")
public class ContentCategoryServices {
	
	@EJB
	private CategoryDao categoryDao;
	
	@POST
	@Path("/getallencrypted")
	public String getAllEncrypted(@Context HttpServletRequest httpResuest)
	
	{
		String result="";
		try {
			ArrayList<ContentCategory> categories= categoryDao.getAll();
			String gson=Utility.toJson(categories);
			String compressed=Utility.gzipCompress(gson);
			result= compressed;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	@POST
	@Path("/getalloriginal")
	public String getAllUncompressed(@Context HttpServletRequest httpResuest)
	
	{
		String result="";
		try {
			ArrayList<ContentCategory> categories= categoryDao.getAll();
			String gson=Utility.toJson(categories);
			result= gson;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
}
