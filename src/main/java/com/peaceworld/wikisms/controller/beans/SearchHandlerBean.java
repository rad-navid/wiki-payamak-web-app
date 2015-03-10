package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.faces.bean.RequestScoped;

@RequestScoped
@Stateful
public class SearchHandlerBean extends ContentHandlerBean  implements Serializable{

	private static final long serialVersionUID = 5165518320162595356L;
	private String query="";

	@Override
	public void goToPage(int index) {
		try{
			int firstResult=(index-1)*LIST_SIZE_LIMIT;
			contentList= contentDao.getSearchContent(query,firstResult, LIST_SIZE_LIMIT);
			if(contentList!=null && contentList.size()>0)
				currentPage=index;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void search(String query) {
		this.query=query;
		reEvaluatePages();
		goToPage(1);
		
	}

	@Override
	protected void reEvaluatePages() {
		
		long contentCounter = -1;

		if ( (contentCounter = contentDao.getSearchContentCount(query)) > 0) {
			pageCounter = (int) (Math.ceil(contentCounter * 1.0d/ LIST_SIZE_LIMIT));
		} else
			pageCounter = 0;
	}
	
	
	
}


