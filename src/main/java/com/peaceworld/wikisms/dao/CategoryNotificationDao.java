package com.peaceworld.wikisms.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.CategoryNotification;

@Stateless
public class CategoryNotificationDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	public ArrayList<CategoryNotification> getAllByCreatorUser(long userIdentifier, int limit)
	{
		try{
			ArrayList<CategoryNotification>ccns=(ArrayList<CategoryNotification>)
				em.createNamedQuery("CategoryNotification.GetAllByCreator").setParameter("creatorUser", userIdentifier).setMaxResults(limit).getResultList();
			return ccns;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<CategoryNotification> nativeQuery1(long identifier, String exclude, int limit)
	{
		try{
			// exclude: (1,2,3,4,5)
			String query="SELECT * FROM CategoryNotification ccn WHERE ( ccn.creatorUser != "+identifier+" ) AND ( ccn.id  NOT IN "+exclude+" )  ORDER BY ccn.sent DESC LIMIT "+ limit;
			ArrayList<CategoryNotification>ccns=(ArrayList<CategoryNotification>)em.createNativeQuery(query, CategoryNotification.class).getResultList();
			return ccns;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList<Long> getAllAvailable(String ccnIds)
	{
		try{
			String queryString = "SELECT id FROM CategoryNotification WHERE id IN "+ccnIds+" ";
			ArrayList<Long>ccns=(ArrayList<Long>)em.createNativeQuery(queryString).getResultList();
			return ccns;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public boolean addAll(List<CategoryNotification> ccns)
	{
		//escape all new notifications
		if(1==1)
			return true;
		try{
			for(CategoryNotification ccn:ccns)
			{
				ccn.setLastSentTime(System.currentTimeMillis());
				em.persist(ccn);
			}
			em.flush();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateStates(String ccnsstate)
	{
		
		try {
			Query query= em.createNamedQuery("CategoryNotification.GetById");
			
			// ccnsstatus: id1:true;id2:false;id3:true;...
			String ccns[]=ccnsstate.split(";");
			for(int i=0;i<ccns.length;i++)
			{
				String part[]=ccns[i].split(":");
				
				if(part==null || part.length<2 || part[0].length()<1 ||part[1].length()<1)
					continue;
				try{
					long id=Long.parseLong(part[0]);
					boolean state= part[1].trim().equalsIgnoreCase("true");
					CategoryNotification ccn=(CategoryNotification)query.setParameter("ccnId", id).getSingleResult();
					if(state)
						ccn.setAccepted(ccn.getAccepted()+1);
					else
						ccn.setDenied(ccn.getDenied()+1);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
			em.flush();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
