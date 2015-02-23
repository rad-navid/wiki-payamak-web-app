package com.peaceworld.wikisms.controller.manager;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.controller.CryptoMessage;
import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;
import com.peaceworld.wikisms.model.ContentChangeLog;
import com.peaceworld.wikisms.model.ContentNotification;
import com.peaceworld.wikisms.model.ContentNotification.ACTION;
import com.peaceworld.wikisms.model.GeneralChangeLog;

@Stateless
public class ContentManager {

	public final long TenDay = 864000000L;

	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;

	public void verifyRequests() {
		long users = (Long) em.createNamedQuery("UserTable.CountAll")
				.getSingleResult();
		long ccns = (Long) em.createNamedQuery("CategoryNotification.CountAll")
				.getSingleResult();
		long cns = (Long) em.createNamedQuery("ContentNotification.CountAll")
				.getSingleResult();
		if ((ccns + cns) == 0)
			return;
		long threshold = (long) (5.0 * users / (ccns + cns));
		threshold = threshold > users ? users : threshold;
		threshold = threshold < 1 ? 1 : threshold;

		long currentTime = System.currentTimeMillis();
		long deadTime = currentTime - TenDay;
		Query query = em.createNamedQuery("ContentNotification.GetDeadCNs")
				.setParameter("sent", threshold)
				.setParameter("lastSentTime", deadTime);
		ArrayList<ContentNotification> CNList = (ArrayList<ContentNotification>) query
				.getResultList();
		for (int i = 0; i < CNList.size(); i++) {
			ContentNotification cn = CNList.get(i);
			try {
				if (cn.getAccepted() > cn.getDenied()) {
					classifyNotifications(cn);
					registerInCategoryChangeLog(cn);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			GeneralChangeLog newChangeLog=new GeneralChangeLog(cn.getId(), "ContentNotification",
					GeneralChangeLog.ChangeLogActions.CONTENT_NOTIFICATION_REMOVED.toString(), System.currentTimeMillis());
			em.persist(newChangeLog);
			
			em.remove(cn);
		}
		em.flush();

	}

	private void registerInCategoryChangeLog(ContentNotification cn) {
		
		ContentNotification.ACTION action = ContentNotification.ACTION.valueOf(cn.getAction());
		// they should be checked manually by admin ...
		if(action== ACTION.CONTENT_CREATE)
			return;
		

		ContentChangeLog cLog = new ContentChangeLog();
		cLog.setContentId(cn.getContentId());
		cLog.setMetaInfo(cn.getMetaInfo());
		cLog.setComment(cn.getComment());
		cLog.setOperand1(cn.getOperand1());
		cLog.setOperand2(cn.getOperand2());
		cLog.setCreatorUser(cn.getCreatorUser());
		cLog.setAction(cn.getAction());
		cLog.setTimeStamp(System.currentTimeMillis());
		em.persist(cLog);
	}

	private void classifyNotifications(ContentNotification cnotif) {

		ContentNotification.ACTION action = ContentNotification.ACTION
				.valueOf(cnotif.getAction());
		switch (action) {
		case CONTENT_DELETE:
			notification_action_CONTENT_DELETE(cnotif);
			break;
		case CONTENT_EDIT:
			notification_action_CONTENT_EDIT(cnotif);
			break;
		case CONTENT_TAG_ADDED:
			notification_action_CONTENT_TAG_ADDED(cnotif);
			break;
		case CONTENT_TAG_CHANGED:
			notification_action_CONTENT_TAG_CHANGED(cnotif);
			break;
		case CONTENT_CREATE:
			notification_action_CONTENT_CREATE(cnotif);
			break;

		default:
			break;
		}

	}

	private void notification_action_CONTENT_CREATE(ContentNotification cnotif) {
		
		long parentId;
		ContentCategory parent = em.find(ContentCategory.class,
				cnotif.getOperand1());
		if (parent != null)
			parentId = parent.getId();
		else {
			parent = em.find(ContentCategory.class, cnotif.getOperand2());
			if (parent != null)
				parentId = parent.getId();
			else
				parentId = 0;
		}
		
		Content c = new Content();
		try{
			String plainText=cnotif.getMetaInfo();
			c.setPlainText(plainText);
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		if(parentId!=0)
			c.setContentTag(";"+parentId + ";");
		c.setCreatorUser(cnotif.getCreatorUser());
		c.setInsertionDateTime(System.currentTimeMillis());
		c.setApproved(false);
		c.setAdministrationLevel(0);
		em.persist(c);
		em.flush();

	}

	private void notification_action_CONTENT_TAG_CHANGED(
			ContentNotification cnotif) {

		Content c = em.find(Content.class, cnotif.getContentId());
		ContentCategory oldCategory = em.find(ContentCategory.class,
				cnotif.getOperand1());
		ContentCategory newCategory = em.find(ContentCategory.class,
				cnotif.getOperand2());
		String newTag = c.getContentTag().replace(";"+oldCategory.getId() + ";", ";")
				.replace(";"+newCategory.getId() + ";", ";")
				+newCategory.getId() + ";";
		c.setContentTag(newTag);

	}

	private void notification_action_CONTENT_TAG_ADDED(
			ContentNotification cnotif) {

		Content c = em.find(Content.class, cnotif.getContentId());
		ContentCategory newCategory = em.find(ContentCategory.class,
				cnotif.getOperand1());
		String newTag = c.getContentTag().replace(";"+newCategory.getId() + ";", ";")+ newCategory.getId() + ";";
		c.setContentTag(newTag);

	}

	private void notification_action_CONTENT_EDIT(ContentNotification cnotif) {
		
		try{
			CryptoMessage crypto=new CryptoMessage();
			Content c = em.find(Content.class, cnotif.getContentId());
			String encryptedValue=crypto.encrypt(cnotif.getMetaInfo());
			c.setEncriptedText(encryptedValue);
			c.setPlainText(Utility.removeUselessCharacters(cnotif.getMetaInfo()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}

	private void notification_action_CONTENT_DELETE(ContentNotification cnotif) {
		Content c = em.find(Content.class, cnotif.getContentId());
		em.remove(c);

	}

}
