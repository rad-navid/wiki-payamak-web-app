package com.peaceworld.wikisms.controller.beans;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.peaceworld.wikisms.controller.manager.ChangeLogToMassageHandler;
import com.peaceworld.wikisms.dao.CategoryDao;
import com.peaceworld.wikisms.dao.CategoryNotificationDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.dao.ContentNotificationDao;
import com.peaceworld.wikisms.model.CategoryNotification;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentNotification;
import com.peaceworld.wikisms.model.UserTable;
import com.peaceworld.wikisms.model.entity.OnlineActivity;
import com.peaceworld.wikisms.model.entity.RegisteredContent;

@Stateless
public class ReportManager {
	
	@EJB
	private CategoryNotificationDao ccnDao;
	@EJB
	private ContentNotificationDao cnDao;
	@EJB
	private CategoryDao categoryDao;
	@EJB
	private ContentDao contentDao;
	@EJB
	ChangeLogToMassageHandler changeLogToMassageHandler;
	
	
	
	
	
	public ArrayList<OnlineActivity> getAllOnlineActivities(UserTable user, int limit)
	{
		
		try {
			ArrayList<OnlineActivity>onlineActivities=new ArrayList<OnlineActivity>(100);
			ArrayList<CategoryNotification>ccns = ccnDao.getAllByCreatorUser(user.getUserIdentifier(),limit);
			for(CategoryNotification ccn:ccns)
				onlineActivities.add(new OnlineActivity(changeLogToMassageHandler.getMessage(ccn), ccn.getLastSentTime(), ccn.getAccepted(), ccn.getDenied()));
		
			ArrayList<ContentNotification>cns = cnDao.getAllByCreatorUser(user.getUserIdentifier(), limit);
			for(ContentNotification cn:cns)
				onlineActivities.add(new OnlineActivity(changeLogToMassageHandler.getMessage(cn), cn.getLastSentTime(), cn.getAccepted(), cn.getDenied()));
				
			return onlineActivities;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	
	public ArrayList<RegisteredContent> getUserContents(UserTable user, int limit)
	{
		
		try {
			ArrayList<Content> contentList=contentDao.getUserContents(user.getUserIdentifier(), limit);
			if(contentList==null || contentList.size()<=0)
				return null;
			
			ArrayList<RegisteredContent>registeredContentList=new ArrayList<RegisteredContent>(contentList.size());
			for(Content content:contentList)
				registeredContentList.add(new RegisteredContent(content));
			return registeredContentList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
}
