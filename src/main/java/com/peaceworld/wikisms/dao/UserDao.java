package com.peaceworld.wikisms.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.GeneralChangeLog;
import com.peaceworld.wikisms.model.UserTable;

@Stateless
public class UserDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	public UserTable getUserByUserIdentifier(long userIdentifier)
	{
		UserTable user=null;
		try{
			user=em.createNamedQuery("UserTable.GetByIdentifier",UserTable.class).setParameter("Identifier", userIdentifier).getSingleResult();
		}catch(Exception ex)
		{		
			
		}
		return user;
	}

	public UserTable getUserByEmail(String email) {
		UserTable user=null;
		try{
			List<UserTable>userList=(List<UserTable>)em.createNamedQuery("UserTable.GetByEmail").
				setParameter("email", email).getResultList();
			if(userList.size()>1)
				System.out.println("Non Unique User: "+email);
			if(userList!=null)
				return userList.get(0);
		}catch(Exception ex)
		{		
			ex.printStackTrace();
		}
		return null;
	}
	
	public boolean CreateUser(UserTable newUser)
	{
		try {
			if(newUser!=null)
			{
				em.persist(newUser);
				em.flush();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return false;
	}


	public boolean resetPassword(String email, String newPass) {
		
		try{
			UserTable user=getUserByEmail(email);
			if(user!=null)
			{
				user.setPassword(newPass);
				em.persist(user);
				em.flush();
			}
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
		
	}
	
	public ArrayList<UserTable> getUsersById(List<Long> idList)
	{
		try{
			ArrayList<UserTable>userList=(ArrayList<UserTable>)
					em.createNamedQuery("UserTable.GetAllRequested").setParameter("Ids", idList).getResultList();
			return userList;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		return null;
	}
}
