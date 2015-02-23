package com.peaceworld.wikisms.controller.ws_rs.common;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.model.Comments;

@Stateless
@Path("/comments")
public class CommentsServices {
	 
	@PersistenceContext(name="wiki")
	EntityManager em;
	
	@Path("/new")
	@POST
	public String CreateUser(@FormParam("userIdentifier")long UserIdentifier,@FormParam("comments")String comments,
			@Context HttpServletRequest httpResuest)
	{
		String result="successful";
		try {
			Comments c=new Comments();
			c.setComment(comments);
			c.setUserIdentifier(UserIdentifier);
			c.setTime(System.currentTimeMillis());
			em.persist(c);
			
			result= "successful";
			
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	
	}
}

