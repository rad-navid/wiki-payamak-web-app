package com.peaceworld.wikisms.controller.ws_rs.admin;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.dao.CategoryDao;
import com.peaceworld.wikisms.model.ContentCategory;

@Stateless
@Path("/admin/category")
public class ContentCategoryAdminServices {
	
	@EJB
	private CategoryDao categoryDao;
	
	@POST
	@Path("/create")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public String create(@FormParam("id")long id,@FormParam("newCategoryName")String newCategoryName,@FormParam("parentCategory")long parentCategory,
			@Context HttpServletRequest httpResuest)
	
	{
		String result=categoryDao.create(id, newCategoryName, parentCategory);
		Utility.invalidateSession(httpResuest);
		return result;
	}

	
	
}
