package com.peaceworld.wikisms.dao;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.Comments;

@Stateless
public class CommentDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	public boolean CreateNewCommnet(String comment,long userIdentifier)
	{
		try {
			Comments c = new Comments();
			c.setComment(comment);
			c.setUserIdentifier(userIdentifier);
			c.setTime(System.currentTimeMillis());
			em.persist(c);
			em.flush();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
