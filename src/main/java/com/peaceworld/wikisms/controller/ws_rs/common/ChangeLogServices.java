package com.peaceworld.wikisms.controller.ws_rs.common;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.dao.ChangeLogDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.CategoryChangeLog;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentChangeLog;
import com.peaceworld.wikisms.model.ContentDirectChange;
import com.peaceworld.wikisms.model.GeneralChangeLog;

@Stateless
@Path("/changelog")
public class ChangeLogServices {
	private static final String SERVER_EMPTY_LOG_LIST_MESSAGE="EMPTYLOGLIST";
	
	@EJB
	private ChangeLogDao changeLogDao;
	@EJB
	private ContentDao contentDao;
	
	@POST
	@Path("/changelog")
	public String getGeneralChangeLogs(@FormParam("IndexId")int indexId ,@FormParam("timestamp") long timeStamp,
			@FormParam("limit") int limit,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {

			ArrayList<GeneralChangeLog>logList=changeLogDao.GetGeneralChangeLogs(indexId,timeStamp,limit);
			if(logList==null || logList.size()<1)
				return SERVER_EMPTY_LOG_LIST_MESSAGE;
			
			String gson=Utility.toJson(logList);
			String compress=Utility.gzipCompress(gson);
			result= compress;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}

	@POST
	@Path("/ccnchangelog")
	public String getCcnChangeLogs(@FormParam("indexId") long indexId,@FormParam("timeStamp") long timeStamp,
			@FormParam("limit")int limit,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<CategoryChangeLog>ccnsLog=changeLogDao.GetCcnChangeLogs(indexId, timeStamp, limit);
			
			if(ccnsLog==null || ccnsLog.size()<1)
				return SERVER_EMPTY_LOG_LIST_MESSAGE;
			
			String gson=Utility.toJson(ccnsLog);
			String compress=Utility.gzipCompress(gson);
			result= compress;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	@POST
	@Path("/cnchangelog")
	public String getCnChangeLogs(@FormParam("indexId") long indexId,@FormParam("timeStamp") long timeStamp,
			@FormParam("limit")int limit,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<ContentChangeLog>cnsLog=changeLogDao.GetCnChangeLogs(indexId,timeStamp,limit);
			
			if(cnsLog==null || cnsLog.size()<1)
				return SERVER_EMPTY_LOG_LIST_MESSAGE;
			
			String gson=Utility.toJson(cnsLog);
			String compress=Utility.gzipCompress(gson);
			result= compress;
		} catch (Exception e) {
			result= e.toString();
		}
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	
	@POST
	@Path("/systemtime")
	public String getAllCNS(@Context HttpServletRequest httpResuest)
	{
		String result= System.currentTimeMillis()+"";
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	@POST
	@Path("/getlastcontentsstate")
	public String getLstContentsState(@FormParam("indexId") long indexId,@FormParam("lastChangeTime") long lastChangeTime,
			@FormParam("limit")int limit,@Context HttpServletRequest httpResuest)
	{
		String result="";
		try {
			ArrayList<Content>logList=contentDao.getLastContentsState(indexId, lastChangeTime, limit);
			
			if(logList==null || logList.size()<1)
			{
				result= SERVER_EMPTY_LOG_LIST_MESSAGE;
				Utility.invalidateSession(httpResuest);
				return result;
			}
			
			ArrayList<ContentDirectChange>changeLog=new ArrayList<ContentDirectChange>(logList.size());
			
			for(Content cnt:logList)
			{
				ContentDirectChange change=new ContentDirectChange();
				change.setId(cnt.getId());
				change.setLiked(cnt.getLikedCounter());
				change.setViewd(cnt.getViewedCounter());
				change.setTimeStamp(cnt.getLastChengedTime());
				changeLog.add(change);
			}
			String gson=Utility.toJson(changeLog);
			String compress=Utility.gzipCompress(gson);
			result= compress;
		} catch (Exception e) {
			result= e.toString();
		}
		
		
		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	
	@POST
	@Path("/setcontentsstate")
	public String setAllContentChenges(@FormParam("contentChangeLog") String contentChangeLog,@Context HttpServletRequest httpResuest)
	{
		String result="successful";
		try {
			String changeLogList=Utility.gzipUncompress(contentChangeLog);
			Gson gson=new Gson();
			
			List<ContentDirectChange> changeList = null;
			changeList = (List<ContentDirectChange>)gson.fromJson(changeLogList, new TypeToken<List<ContentDirectChange>>() {}.getType());

			boolean success=contentDao.setAllContentChenges(changeList);
			if(success)
				result= "successful";
			else
				result="failed";
			
		} catch (Exception e) {
			result= e.toString();
		}

		Utility.invalidateSession(httpResuest);
		return result;
	}
	
	@POST
	@Path("/likecontent")
	public String likeContent(@FormParam("contentId") long contentId,@Context HttpServletRequest httpResuest)
	{
		String result="successful";
		try {

			boolean success=contentDao.like(contentId);
			if(success)
				result= "successful";
			else
				result="failed";
			
		} catch (Exception e) {
			result= e.toString();
		}

		Utility.invalidateSession(httpResuest);
		return result;
	}
	@POST
	@Path("/dislikecontent")
	public String dislikeContent(@FormParam("contentId") long contentId,@Context HttpServletRequest httpResuest)
	{
		String result="successful";
		try {

			boolean success=contentDao.disLike(contentId);
			if(success)
				result= "successful";
			else
				result="failed";
			
		} catch (Exception e) {
			result= e.toString();
		}

		Utility.invalidateSession(httpResuest);
		return result;
	}
	
}
