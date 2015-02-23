package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.controller.beans.ContentHandlerBean.Page;
import com.peaceworld.wikisms.controller.beans.SpecialLinksBean.LinkHelper;
import com.peaceworld.wikisms.controller.beans.SpecialLinksBean.SpecialLink;
import com.peaceworld.wikisms.dao.CommentDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;

@ViewScoped
@ManagedBean
public class ContentBrowserBean implements Serializable{

	private static final long serialVersionUID = -5975616184813162380L;
	private enum MOD {SEARCH,NORMAL,SPECIAL_LINKS};
	
	@EJB
	CategoryExplorerBean categoryExplorerBean;
	@EJB
	SearchHandlerBean searchHandlerBean;
	@EJB
	SpecialLinksBean specialLinksBean;
	@EJB
	CommentDao commentDao;
	@EJB 
	ContentDao contentDao;
	
	private MOD mMod=MOD.SPECIAL_LINKS;
	private String search,contactUsValue;
	
	@PostConstruct
	private void loadAfterConstruct()
	{
		try{
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setMaxInactiveInterval(10*60);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		load();
	}
	
	private void load()
	{
		switch(mMod)
		{
		case NORMAL:
			categoryExplorerBean.load();
			break;
		case SEARCH:
			break;
		case SPECIAL_LINKS:
			specialLinksBean.load();
			break;

		}
		
		
	}
	
	public void searchMod()
	{
		mMod=MOD.SEARCH;
		load();
		searchHandlerBean.search(search);
		
	}
	public void normalMod()
	{
		neutralizeAllModes();
		mMod=MOD.NORMAL;
		load();
	}
	
	public void setLink(String linkEnumName)
	{
		neutralizeAllModes();
		mMod=MOD.SPECIAL_LINKS;
		specialLinksBean.setCurentLink(linkEnumName);
		load();
		
	}
	
	public void deepInCategory(long categoryId)
	{
		normalMod();
		categoryExplorerBean.deepInCategory(categoryId);
	}
	
	private void neutralizeAllModes()
	{
		specialLinksBean.setCurentLink(null);
		categoryExplorerBean.setCurrentCategory(null);
	}
	
	public void deepOutCategory()
	{
		normalMod();
		categoryExplorerBean.deepOutCategory();
	}
	
	public void goToPage(int index)
	{
		switch(mMod)
		{
		case NORMAL:
			categoryExplorerBean.goToPage(index);
			return;
		case SEARCH:
			searchHandlerBean.goToPage(index);
			return;
		case SPECIAL_LINKS:
			specialLinksBean.goToPage(index);
		}
		
	}
	
	public void indexPages()
	{
		switch(mMod)
		{
			case NORMAL:
				categoryExplorerBean.indexPages();
				return;
			case SEARCH:
				searchHandlerBean.indexPages();
				return;
		}
		
	}
	
	public static String getContentDate(long milisec) {
		return Utility.milisecondsToJalaliDateTime(milisec);
	}
	
	public void likeContent(long contentId)
	{
		if(contentDao!=null)
			contentDao.like(contentId);
	}
	public void disLikeContent(long contentId)
	{
		if(contentDao!=null)
			contentDao.disLike(contentId);
	}

	
	public String currentCategoryName() {
		return categoryExplorerBean.currentCategoryName();
	}
	
	public int getCurrentPage() {
		
		switch(mMod)
		{
			case NORMAL:
				return categoryExplorerBean.getCurrentPage();
			case SEARCH:
				return searchHandlerBean.getCurrentPage();
		}
		return 0;
		
	}
	public ContentCategory getCurrentCategory() {
		return categoryExplorerBean.getCurrentCategory();
	}
	public SpecialLink getCurrentLink() {
		return specialLinksBean.getCurrentLink();
	}
	public ArrayList<Page> getPages() {
		switch(mMod)
		{
			case NORMAL:
				return categoryExplorerBean.getPages();
			case SEARCH:
				return searchHandlerBean.getPages();
		}
		return null;
		
	}
	public ArrayList<Content> getContentList() {
		switch(mMod)
		{
			case NORMAL:
				return categoryExplorerBean.getContentList();
			case SEARCH:
				return searchHandlerBean.getContentList();
			case SPECIAL_LINKS:
				return specialLinksBean.getContentList();
		}
		return null;
		
	}
	public ArrayList<ContentCategory> getCatgoryList() {
		
		return categoryExplorerBean.getCatgoryList();
	}
	
	public ArrayList<LinkHelper> getSpecialLinks() {
		
		return specialLinksBean.getLinks();
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void contactUs()
	{
		if(commentDao!=null)
		{
			String plainMailContent=Utility.HtmlToPlainText(contactUsValue);
			boolean result=commentDao.CreateNewCommnet(plainMailContent, 0);
			if(result)
				contactUsValue="";
		}
	}
	
	public void fakeRequest()
	{
		
	}

	public String getContactUsValue() {
		return contactUsValue;
	}

	public void setContactUsValue(String contactUsValue) {
		this.contactUsValue = contactUsValue;
	}
	
	public String testContent()
	{
		ArrayList<Content>list= contentDao.getUserContents(1919, 100);
		String result="";
		for(Content c:list)
			result+=c.getPlainText()+"\r\n\r\n";
		return result;
	}
	
	
}


