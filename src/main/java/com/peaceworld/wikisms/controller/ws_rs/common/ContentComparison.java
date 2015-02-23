package com.peaceworld.wikisms.controller.ws_rs.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.peaceworld.wikisms.controller.CryptoMessage;
import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.model.Content;

@Stateless
@Path("/user")

public class ContentComparison {
	
	@PersistenceContext(name="wiki")
	EntityManager em;
	
	
	@Path("/contentcomparison")
	@POST
	public String checkForPreExistence(@FormParam("content")String content,@Context HttpServletRequest httpResuest) {
		long t1=System.currentTimeMillis();
		
		try{
			ArrayList<Content> allContents=(ArrayList<Content>)em.createNamedQuery("Content.GetAllContentes").getResultList();
			HashSet<String>list1=getKeyWords(content);
			for(Content c:allContents)
			{
				try{
					String content2=c.getPlainText();
					HashSet<String>list2=getKeyWords(content2);
					double value=Utility.getSimilarity(list1, list2);
					if(value>.6)
					{
						Utility.invalidateSession(httpResuest);
						return (System.currentTimeMillis()-t1)+";True";
					}
				}
				catch(Exception ex)
				{
					continue;
				}
				
			}
		}catch(Exception ex2)
		{
			Utility.invalidateSession(httpResuest);
			return (System.currentTimeMillis()-t1)+";False";
		}
		
		Utility.invalidateSession(httpResuest);
		return (System.currentTimeMillis()-t1)+";False";
		
	}
	

	private static HashSet<String> getKeyWords(String content)
	{
		content=Utility.removeUselessCharacters(content);
		String tokens[]=content.split("\\s+");
		HashSet<String>mapKeys=new HashSet<String>(tokens.length);
		for(int i=0;i<tokens.length;i++)
		{
			String token=tokens[i].replace("\u202b", "").trim();
			if(token.length()>0)
				mapKeys.add(token.trim());
		}
		return mapKeys;
	}

}
