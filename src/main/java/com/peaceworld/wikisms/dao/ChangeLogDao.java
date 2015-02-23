package com.peaceworld.wikisms.dao;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.CategoryChangeLog;
import com.peaceworld.wikisms.model.ContentChangeLog;
import com.peaceworld.wikisms.model.GeneralChangeLog;

@Stateless
public class ChangeLogDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	public ArrayList<GeneralChangeLog> GetGeneralChangeLogs(int indexId, long timeStamp, int limit)
	{
		try {

			ArrayList<GeneralChangeLog>logList=(ArrayList<GeneralChangeLog>)
					em.createNamedQuery("GeneralChangeLog.GetAllByTimeStamp").setParameter("Id", indexId).
					setParameter("TimeStamp", timeStamp).setMaxResults(limit).getResultList();
			return logList;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public ArrayList<CategoryChangeLog> GetCcnChangeLogs(long indexId, long timeStamp, int limit)
	{
		try {

			ArrayList<CategoryChangeLog>ccnsLog=(ArrayList<CategoryChangeLog>)
					em.createNamedQuery("CategoryChangeLog.GetByIdAndTimeStamp").
					setParameter("Id", indexId).setParameter("timeStamp", timeStamp).setMaxResults(limit).getResultList();
			return ccnsLog;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ContentChangeLog> GetCnChangeLogs(long indexId, long timeStamp, int limit)
	{
		try {

			ArrayList<ContentChangeLog>cnsLog=(ArrayList<ContentChangeLog>)
					em.createNamedQuery("ContentChangeLog.GetByIdAndTimeStamp").setParameter("Id", indexId).
					setParameter("timeStamp", timeStamp).setMaxResults(limit).getResultList();
			return cnsLog;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}


	public boolean addGeneralLog(GeneralChangeLog newChangeLog) {

		try{
			em.persist(newChangeLog);
			em.flush();
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
}
