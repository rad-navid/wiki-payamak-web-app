package com.peaceworld.wikisms.controller.ws_rs.admin;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.peaceworld.wikisms.controller.CryptoMessage;
import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentChangeLog;
import com.peaceworld.wikisms.model.ContentNotification.ACTION;

@Stateless
@Path("/admin/data")
public class ContentAdminServices {
	
	@PersistenceContext(name="wiki")
	EntityManager em;
	
	
	@Path("/approvecontents")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public String approveContents(@FormParam("contentList")String contentList)
	{
		int effected=0;
		try {
			CryptoMessage crypto = new CryptoMessage();
			String list[]=contentList.split(";");
			if(list==null)
				return effected+"";
				for(int i=0;i<list.length;i++)
				{
					if(list[i]!=null && list[i].length()>0)
					{
						long id=-1;
						try{
							id=Long.parseLong(list[i]);
						}
						catch(Exception e)
						{
							continue;
						}
						Content content=em.find(Content.class, id);
						content.setApproved(true);
						String encrypted=crypto.encrypt(content.getPlainText());
						content.setEncriptedText(encrypted);
						content.setPlainText(Utility.removeUselessCharacters(content.getPlainText()));
						em.persist(content);
						
						createNewContentChangeLog(content);
						
						effected++;
					}
				}
				
			em.flush();
			return effected+"";
				
			} catch (Exception e) {
				return e.toString();
			}
		
	}

	private void createNewContentChangeLog(Content content) {
		
		ContentChangeLog cLog = new ContentChangeLog();
		cLog.setContentId(content.getId());
		cLog.setMetaInfo(content.getEncriptedText());
		cLog.setComment(content.getContentTag());
		//cLog.setOperand1(0);
		//cLog.setOperand2(0);
		cLog.setCreatorUser(content.getCreatorUser());
		cLog.setAction(ACTION.CONTENT_CREATE.toString());
		cLog.setTimeStamp(System.currentTimeMillis());
		em.persist(cLog);
		
	}

	@Path("/deletecontents")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public String deleteContents(@FormParam("contentList")String contentList)
	{
		int effected=0;
		try {
			String list[]=contentList.split(";");
			if(list==null)
				return effected+"";
				for(int i=0;i<list.length;i++)
				{
					if(list[i]!=null && list[i].length()>0)
					{
						long id=-1;
						try{
							id=Long.parseLong(list[i]);
						}
						catch(Exception e)
						{
							continue;
						}
						Content content=em.find(Content.class, id);
						em.remove(content);
						effected++;
					}
				}
				
			em.flush();
			return effected+"";
				
			} catch (Exception e) {
				return e.toString();
			}
		
	}
	
	@Path("/unapprovedcontents")
	@POST
	public String getUnApprovedContents(@FormParam("indexId")long indexId, @FormParam("limit")int limit) {
		long t1=System.currentTimeMillis();
		
		try{
			ArrayList<Content> contentList=(ArrayList<Content>)em.createNamedQuery("Content.GetAllUnApprovedContentesById")
					.setParameter("cntId", indexId).setMaxResults(limit).getResultList();
			String gson=Utility.toJson(contentList);
			String compressed=Utility.gzipCompress(gson);
			return compressed;
		}catch(Exception ex)
		{
			return ex.toString();
		}
		
	}
}
