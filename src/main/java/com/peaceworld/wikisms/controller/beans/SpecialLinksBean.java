package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateful;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@RequestScoped
@Stateful
public class SpecialLinksBean extends ContentHandlerBean  implements Serializable{
	
	private static final long serialVersionUID = 763357223487326871L;


	public enum SpecialLink{MostViewed,MostLiked,MostResent,Random}
	
	private SpecialLink mMod;
	
	private ArrayList<LinkHelper> links;
	
	public SpecialLinksBean()
	{
		links=new ArrayList<SpecialLinksBean.LinkHelper>(3);
		links.add(new LinkHelper(SpecialLink.MostLiked, "پسندیده شده ترین ها"));
		links.add(new LinkHelper(SpecialLink.MostViewed, "بازدید شده ترین ها"));
		links.add(new LinkHelper(SpecialLink.MostResent, "جدید ترین ها"));
		links.add(new LinkHelper(SpecialLink.Random, "تصادفی"));
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
			case Random:
				contentList = contentDao.getRandomContent(firstResult,LIST_SIZE_LIMIT);
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

	public void load(String mod,int page) {
		
		setCurentLink(mod);
		if(mMod==null)
			mMod=SpecialLink.Random;
		reEvaluatePages();
		goToPage(page);
		
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
	public String getCurrentLinkLable() {
		for(LinkHelper lhp:links)
			if(lhp.linkEnum==mMod)
				return lhp.linkLable;
		return "";
	}

	private void setCurentLink(String linkEnumName) {
		
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
	
	public String getSpecialLinkAsUrl(String name,String code,int page)
	{
		 FacesContext ctx = FacesContext.getCurrentInstance();
	     String contextPath = ctx.getExternalContext().getRequestContextPath();
	     StringBuilder url = new StringBuilder(100);
	     url.append(getRootUrl(ctx));
	     url.append(contextPath);
	     url.append("/sms/");
	     url.append(name.replace("ها", "").trim().replace(" ", "_"));
	     url.append(" اس ام اس ها و پیامک ها".replace(" ", "_"));
	     url.append("?ln="+code);
	     url.append("&pid="+page);

		return url.toString();
		
	}
	
	public void indexPages()
	{
		super.indexPages();
		LinkHelper LHP = null;
		for(LinkHelper lhp:links)
		{
			if(lhp.linkEnum==mMod)
			{
				LHP=lhp;
				break;
			}
		}
		
		if(LHP==null)
			return;
		
		for(Page page:pages)
			page.setHref(getSpecialLinkAsUrl(LHP.linkLable, LHP.linkEnum.name(),page.getPageNumber()));
		
	}

	
}


