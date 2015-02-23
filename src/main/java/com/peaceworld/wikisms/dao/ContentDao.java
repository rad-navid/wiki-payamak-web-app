package com.peaceworld.wikisms.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentDirectChange;

@Stateless
public class ContentDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	
	public ArrayList<Content> getContentsByCategoryOriginal(long maxContentId,long catId,int limit)
	{
		try {
			ArrayList<Content> contentList=(ArrayList<Content>)
					em.createNamedQuery("Content.GetContentsByCategory").setParameter("cntId", maxContentId)
					.setParameter("likequery", "%;"+catId+";%").setMaxResults(limit).getResultList();
			return contentList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Content> getContentsByCategoryOriginal(long catId,int firstResult, int maxResult)
	{
		try {
			ArrayList<Content> contentList=(ArrayList<Content>)
					em.createNamedQuery("Content.GetContentsByCategory").setParameter("cntId", 0L)
					.setParameter("likequery", "%;"+catId+";%").setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
			return contentList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public long getContentsCountByCategory(long catId)
	{
		try {
			long counter=(Long)
					em.createNamedQuery("Content.GetContentsCountByCategory")
					.setParameter("likequery", "%;"+catId+";%").getSingleResult();
			return counter;
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public long getSearchContentCount(String queryParam)
	{
		try {
			String query="SELECT COUNT(*) FROM Content where plainText REGEXP \'[[:<:]]"+queryParam+"[[:>:]]\'";
			System.out.println(query);
			BigInteger result=(BigInteger) em.createNativeQuery(query).getResultList().get(0);
			int counter=result.intValue();
			System.out.println(counter);
			return counter;
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public ArrayList<Content> getSearchContent(String queryParam ,int firstResult, int maxResult)
	{
		try {
			
			String query="SELECT * FROM Content where plainText REGEXP \'[[:<:]]"+queryParam+"[[:>:]]\'";
			ArrayList<Content> contentList=(ArrayList<Content>)
					em.createNativeQuery(query,Content.class).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
			return contentList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Content> getMostRecentContent(int firstResult, int maxResult)
	{
		try {
			ArrayList<Content> contentList=(ArrayList<Content>)
					em.createNamedQuery("Content.GetMostRecent").setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
			return contentList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Content> getMostLikedContent(int firstResult, int maxResult)
	{
		try {
			ArrayList<Content> contentList=(ArrayList<Content>)
					em.createNamedQuery("Content.GetMostLiked").setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
			return contentList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Content> getMostViewedContent(int firstResult, int maxResult)
	{
		try {
			ArrayList<Content> contentList=(ArrayList<Content>)
					em.createNamedQuery("Content.GetMostViewed").setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
			return contentList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public ArrayList<Content> getUserContents(long userIdentifier, int limit)
	{
		try {
			ArrayList<Content> contentList=	(ArrayList<Content>) em.createNamedQuery("Content.GetUserContentes").setParameter("userIdentifier", userIdentifier).setMaxResults(limit).getResultList();
			return contentList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean createUnverifiedNewContent(String content,String tag,long userIdentifier)
	{
		try{
			Content c=new Content();
			c.setApproved(false);
			c.setAdministrationLevel(0);
			c.setPlainText(content);
			c.setContentTag(tag);
			c.setCreatorUser(userIdentifier);
			c.setInsertionDateTime(System.currentTimeMillis());
			em.persist(c);
			em.flush();
			return true;
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public void like(long contentId) {
		try {
			Content c=em.find(Content.class, contentId);
			c.setLikedCounter(c.getLikedCounter()+1);
			em.persist(c);
			em.flush();
		} catch (Exception e) {
		}
	}

	public void disLike(long contentId) {
		try {
			Content c=em.find(Content.class, contentId);
			c.setLikedCounter(c.getLikedCounter()-1);
			em.persist(c);
			em.flush();
		} catch (Exception e) {
		}
		
	}
	
	public ArrayList<Content> getLastContentsState(long indexId, long lastChangeTime,int limit)
	{
		try {
			
			ArrayList<Content>logList=(ArrayList<Content>)
					em.createNamedQuery("Content.GetChangeLogById").
					setParameter("cntId", indexId).setParameter("lastChangeTime", lastChangeTime)
					.setMaxResults(limit).getResultList();
			return logList;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean setAllContentChenges(List<ContentDirectChange> changeList)
	{
		
		try {
			for(ContentDirectChange change:changeList)
			{
				Content cnt=em.find(Content.class, change.getId());
				if(cnt!=null)
				{
					if(change.getLiked()>0)
						cnt.setLikedCounter(cnt.getLikedCounter()+1);
					if(change.getViewd()>0)
						cnt.setViewedCounter(cnt.getViewedCounter()+1);
					cnt.setLastChengedTime(System.currentTimeMillis());
				}
			}
			em.flush();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
