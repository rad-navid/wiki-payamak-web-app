package com.peaceworld.wikisms.controller.manager;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.CategoryChangeLog;
import com.peaceworld.wikisms.model.CategoryNotification;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;
import com.peaceworld.wikisms.model.GeneralChangeLog;

@Stateless
public class CategoryManager {

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
		Query query = em.createNamedQuery("CategoryNotification.GetDeadCCNs")
				.setParameter("sent", threshold)
				.setParameter("lastSentTime", deadTime);
		ArrayList<CategoryNotification> CCNList = (ArrayList<CategoryNotification>) query
				.getResultList();

		for (int i = 0; i < CCNList.size(); i++) {

			CategoryNotification ccn = CCNList.get(i);
			try {
				if (ccn.getAccepted() > ccn.getDenied()) {
					classifyNotifications(ccn);
					registerInCategoryChangeLog(ccn);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			GeneralChangeLog newChangeLog=new GeneralChangeLog(ccn.getId(), "CategoryNotification",
					GeneralChangeLog.ChangeLogActions.CATEGORY_NOTIFICATION_REMOVED.toString(), System.currentTimeMillis());
			em.persist(newChangeLog);
			
			em.remove(ccn);
		}
		
		em.flush();

	}

	private void registerInCategoryChangeLog(CategoryNotification ccn) {

		CategoryChangeLog ccLog = new CategoryChangeLog();
		ccLog.setMetadata(ccn.getMetadata());
		ccLog.setOperand1(ccn.getOperand1());
		ccLog.setOperand2(ccn.getOperand2());
		ccLog.setCreatorUser(ccn.getCreatorUser());
		ccLog.setAction(ccn.getAction());
		ccLog.setTimeStamp(System.currentTimeMillis());
		em.persist(ccLog);
	}

	private void classifyNotifications(CategoryNotification ccnotif) {

		CategoryNotification.ACTION action = CategoryNotification.ACTION
				.valueOf(ccnotif.getAction());
		switch (action) {
		case CATEGORY_CHANGED:
			notification_action_CATEGORY_CHANGED(ccnotif);
			break;
		case CATEGORY_CREATE:
			notification_action_CATEGORY_CREATE(ccnotif);
			break;
		case CATEGORY_DELETE:
			notification_action_CATEGORY_DELETE(ccnotif);
			break;
		case CATEGORY_MERGE:
			notification_action_CATEGORY_MERGE(ccnotif);
			break;
		case CATEGORY_MOVE:
			notification_action_CATEGORY_MOVE(ccnotif);
			break;

		default:
			break;
		}

	}

	private void notification_action_CATEGORY_MOVE(CategoryNotification ccnotif) {

		ContentCategory movedc = em.find(ContentCategory.class,
				ccnotif.getOperand1());
		ContentCategory newParent = em.find(ContentCategory.class,
				ccnotif.getOperand2());
		movedc.setParentCategory(newParent.getId());
		em.flush();

	}

	private void notification_action_CATEGORY_MERGE(CategoryNotification ccnotif) {

		ContentCategory main = em.find(ContentCategory.class,
				ccnotif.getOperand1());
		ContentCategory beReplaces = em.find(ContentCategory.class,
				ccnotif.getOperand2());
		ArrayList<Content> contentList = (ArrayList<Content>) em
				.createNamedQuery("Content.GetAllByCategory")
				.setParameter("categoryId", "%" + beReplaces.getId() + "%")
				.getResultList();
		for (Content c : contentList) {
			String newTag = c.getContentTag().replace(beReplaces.getId() + "",
					"")
					+ main.getId() + ";";
			c.setContentTag(newTag);
		}

		main.setName(ccnotif.getMetadata());
		em.remove(beReplaces);
		em.flush();
	}

	private void notification_action_CATEGORY_DELETE(
			CategoryNotification ccnotif) {

	}

	private void notification_action_CATEGORY_CREATE(
			CategoryNotification ccnotif) {

		ContentCategory parent = em.find(ContentCategory.class,
				ccnotif.getOperand2());
		ContentCategory cc = new ContentCategory();
		cc.setId(ccnotif.getOperand1());
		if (ccnotif.getOperand2() == 0)
			cc.setParentCategory(0);
		else
			cc.setParentCategory(parent.getId());
		cc.setName(ccnotif.getMetadata());
		em.persist(cc);
		em.flush();
	}

	private void notification_action_CATEGORY_CHANGED(
			CategoryNotification ccnotif) {
		ContentCategory cc = em.find(ContentCategory.class,
				ccnotif.getOperand1());
		cc.setName(ccnotif.getMetadata());

	}

}
