package com.peaceworld.wikisms.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.ContentNotification;

@Stateless
public class ContentNotificationDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	
	public ArrayList<ContentNotification> getAllByCreatorUser(long userIdentifier, int limit)
	{
		try{
			ArrayList<ContentNotification>cns=(ArrayList<ContentNotification>)
					em.createNamedQuery("ContentNotification.GetAllByCreatorUser").setParameter("creatorUser", userIdentifier).setMaxResults(limit).getResultList();
			return cns;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ContentNotification> nativeQuery1(long identifier, String exclude, int limit)
	{
		try{
			// exclude: (1,2,3,4,5)
			String query="SELECT * FROM ContentNotification cn WHERE ( cn.creatorUser != "+identifier+" ) AND ( cn.id  NOT IN "+exclude+" )  ORDER BY cn.sent DESC LIMIT "+ limit;
			ArrayList<ContentNotification>cns=(ArrayList<ContentNotification>)em.createNativeQuery(query, ContentNotification.class).getResultList();
			for(ContentNotification cn:cns)
			{
				cn.setLastSentTime(System.currentTimeMillis());
				cn.setSent(cn.getSent()+1);
			}
				
			em.flush();
			return cns;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ContentNotification> nativeQuery2(String creatorsId, String exclude)
	{
		try{
			// exclude: (1,2,3,4,5)
			String query="SELECT * FROM ContentNotification cn WHERE ( cn.creatorUser IN "+creatorsId+" ) AND ( cn.id  NOT IN "+exclude+" ) ";
			ArrayList<ContentNotification>cns=(ArrayList<ContentNotification>)em.createNativeQuery(query, ContentNotification.class).getResultList();
			
			for(ContentNotification cn:cns)
			{
				cn.setLastSentTime(System.currentTimeMillis());
				cn.setSent(cn.getSent()+1);
			}
			em.flush();
			return cns;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean addAll(List<ContentNotification> cns)
	{
		try{
			for(ContentNotification cn:cns)
			{
				//escape all new notifications except CONTENT_CREATE
				if(cn.getAction().equalsIgnoreCase(ContentNotification.ACTION.CONTENT_CREATE.name()))
				{
					cn.setLastSentTime(System.currentTimeMillis());
					em.persist(cn);
				}
			}
			
			em.flush();
			return true;
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Long> getAllAvailable(String cnIds)
	{
		try {
			String queryString = "SELECT id FROM ContentNotification WHERE id IN "+cnIds+" ";
			ArrayList<Long>ccns=(ArrayList<Long>)em.createNativeQuery(queryString).getResultList();
			return ccns;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public boolean updateState(String cnsstate)
	{
		
		try {
			Query query= em.createNamedQuery("ContentNotification.GetById");
			
			// ccnsstatus: id1:true;id2:false;id3:true;...
			String cns[]=cnsstate.split(";");
			for(int i=0;i<cns.length;i++)
			{
				String part[]=cns[i].split(":");
				
				if(part==null || part.length<2 || part[0].length()<1 ||part[1].length()<1)
					continue;
				try{
					long id=Long.parseLong(part[0]);
					boolean state= part[1].trim().equalsIgnoreCase("true");
					ContentNotification cn=(ContentNotification)query.setParameter("cnId", id).getSingleResult();
					if(state)
						cn.setAccepted(cn.getAccepted()+1);
					else
						cn.setDenied(cn.getDenied()+1);
				}
				catch(Exception ex){
					
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
