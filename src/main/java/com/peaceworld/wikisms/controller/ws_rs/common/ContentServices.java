package com.peaceworld.wikisms.controller.ws_rs.common;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.Content;

@Stateless
@Path("/data")

public class ContentServices {
	
	@EJB
	private ContentDao contentDao;
	
	private static final String SERVER_EMPTY_CONTENT_LIST_MESSAGE="EMPTYCONTENTLIST";
	
	
	@POST
	@Path("/getcontentsbycategory")
	public String getContentsByCategory(@FormParam("maxContentId")long maxContentId,@FormParam("CategoryId")long catId, @FormParam("limit")int limit ,
			@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<Content> contentList=contentDao.getContentsByCategoryOriginal(maxContentId, catId, limit);
			
			if(contentList==null || contentList.size()<1)
				return SERVER_EMPTY_CONTENT_LIST_MESSAGE;
			
			String gson=Utility.toJson(contentList);
			String compressed=Utility.gzipCompress(gson);
			result= compressed;
		} catch (Exception e) {
			result= e.toString();
		}

		Utility.invalidateSession(httpResuest);
		return result;
	}
	
}
