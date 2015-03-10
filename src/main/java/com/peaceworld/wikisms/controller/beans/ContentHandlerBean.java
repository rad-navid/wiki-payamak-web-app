package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.Content;

@RequestScoped
@ManagedBean
public abstract class ContentHandlerBean implements Serializable{

	private static final long serialVersionUID = -2063945080926166804L;
	protected static final int LIST_SIZE_LIMIT=50;
	protected static final int MAX_SHOWN_PAGES=5;
	
	@EJB
	protected ContentDao contentDao;
	
	protected int currentPage,pageCounter;
	protected ArrayList<Page>pages=new ArrayList<Page>(10);
	protected ArrayList<Content>contentList;
	
	public ContentHandlerBean()
	{
		currentPage=1;
		pageCounter=0;
	}
	
	protected abstract void goToPage(int index);
	protected abstract void reEvaluatePages();
	
	public String getRootUrl(FacesContext ctx) {
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();

        StringBuilder url = new StringBuilder(100);
        String scheme = request.getScheme();

        int port = request.getServerPort();

        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());

        if (port > 0 && ((scheme.equalsIgnoreCase("http") && port != 80) || (scheme.equalsIgnoreCase("https") && port != 443))) {
            url.append(':');
            url.append(port);
        }

        return url.toString();
    }
	
	public void indexPages()
	{
		pages.clear();
		int maxRightPages=pageCounter-currentPage;
		int maxLeftPages=currentPage-1;
		int counter=0;
		if(pageCounter>1)
		{
			int pageObj=currentPage;
			pages.add(new Page( pageObj+"", pageObj));
		}
		
		counter++;
		int index=1;
		while(counter<MAX_SHOWN_PAGES && (maxLeftPages>0 || maxRightPages>0))
		{
			if(maxLeftPages>0)
			{
				int pageObj=currentPage-index;
				pages.add(new Page( pageObj+"", pageObj));
				maxLeftPages--;
				counter++;
			}
			if(maxRightPages>0)
			{
				int pageObj=currentPage+index;
				pages.add(new Page( pageObj+"", pageObj));
				maxRightPages--;
				counter++;
			}
			index++;
		}
		
		Collections.sort(pages, new Comparator<Page>() {

			@Override
			public int compare(Page o1, Page o2) {
				if(o1.pageNumber < o2.pageNumber)
					return -1;
				else if(o1.pageNumber == o2.pageNumber)
					return 0;
				else return 1;
			}
		});
		
		if(currentPage<pageCounter)
			pages.add(pages.size(),new Page( "بعدی", currentPage+1));
		if(currentPage>1)
			pages.add(0,new Page( "قبلی", currentPage-1));
		if(maxLeftPages>0)
			pages.add(1,new Page( "...", 1));
		if(maxRightPages>0)
			pages.add(pages.size()-1,new Page( "...", pageCounter));
		
		
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public ArrayList<Page> getPages() {
		return pages;
	}
	
	public ArrayList<Content> getContentList() {
		return contentList;
	}
	
	
	public class Page{
		
		private String lable;
		private int pageNumber;
		private String href;
		
		public Page(String lable, int pageNumber) {
			this.lable = lable;
			this.pageNumber = pageNumber;
		}

		public String getLable() {
			return lable;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}
	}
	
}


