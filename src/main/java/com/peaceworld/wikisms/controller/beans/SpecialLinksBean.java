package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateful;
import javax.faces.bean.ViewScoped;

@ViewScoped
@Stateful
public class SpecialLinksBean extends ContentHandlerBean  implements Serializable{
	
	private static final long serialVersionUID = 763357223487326871L;


	public enum SpecialLink{MostViewed,MostLiked,MostResent}
	
	private SpecialLink mMod=SpecialLink.MostLiked;
	
	private ArrayList<LinkHelper> links;
	
	public SpecialLinksBean()
	{
		links=new ArrayList<SpecialLinksBean.LinkHelper>(3);
		links.add(new LinkHelper(SpecialLink.MostLiked, "پسندیده شده ترین ها"));
		links.add(new LinkHelper(SpecialLink.MostViewed, "بازدید شده ترین ها"));
		links.add(new LinkHelper(SpecialLink.MostResent, "جدید ترین ها"));
		pageCounter=3;
	}

	@Override
	public void goToPage(int index) {
		try{
			int firstResult=(index-1)*LIST_SIZE_LIMIT;
			switch (mMod) {
			case MostLiked:
				contentList = contentDao.getMostLikedContent(firstResult,LIST_SIZE_LIMIT);
				break;
			case MostResent:
				contentList = contentDao.getMostRecentContent(firstResult,LIST_SIZE_LIMIT);
				break;
			case MostViewed:
				contentList = contentDao.getMostViewedContent(firstResult,LIST_SIZE_LIMIT);
				break;
			}
			
			if(contentList!=null && contentList.size()>0)
				currentPage=index;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void load() {
		
		if(mMod==null)
			return;
		reEvaluatePages();
		goToPage(1);
		
	}

	@Override
	protected void reEvaluatePages() {
		pageCounter=3;
		
	}
	
	public ArrayList<LinkHelper> getLinks() {
		return links;
	}
	
	
	public class LinkHelper{
		
		private SpecialLink linkEnum;
		private String linkEnumName;

		private String linkLable;
		
		public LinkHelper(SpecialLink linkEnum, String linkLable) {
			this.linkEnum = linkEnum;
			this.linkLable = linkLable;
			this.setLinkEnumName(linkEnum.name());
		}
		public SpecialLink getLinkEnum() {
			return linkEnum;
		}

		public String getLinkLable() {
			return linkLable;
		}
		public String getLinkEnumName() {
			return linkEnumName;
		}
		public void setLinkEnumName(String linkEnumName) {
			this.linkEnumName = linkEnumName;
		}

		
		
	}


	public SpecialLink getCurrentLink() {
		return mMod;
	}

	public void setCurentLink(String linkEnumName) {
		
		if(linkEnumName==null)
			return;
		
		try{
			mMod=SpecialLink.valueOf(linkEnumName);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}


	
}


