package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.peaceworld.wikisms.controller.Utility;
import com.peaceworld.wikisms.controller.beans.ContentHandlerBean.Page;
import com.peaceworld.wikisms.controller.beans.SpecialLinksBean.LinkHelper;
import com.peaceworld.wikisms.controller.beans.SpecialLinksBean.SpecialLink;
import com.peaceworld.wikisms.dao.CommentDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;

@RequestScoped
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
	private String search,contactUsValue,cname,linkName;
	private int page=1;
	private long categoryId=-1;
	
	@PostConstruct
	private void loadAfterConstruct() {
		
		extractRequestParams();
		try {
			
			if (categoryId>=0 && page>0) 
				mMod=MOD.NORMAL;
			else if(linkName!=null && linkName.length()>0)
			{
				mMod=MOD.SPECIAL_LINKS;
				categoryExplorerBean.baseLoad();
			}
			else
			{
				categoryExplorerBean.baseLoad();
			}

			load();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public String seoContent(String content)
	{
		String header="";
		switch(mMod)
		{
		case NORMAL:
			if(categoryExplorerBean.getCurrentCategory()==null)
				header="";
			else
				header=categoryExplorerBean.currentCategoryPathName();
			break;
		case SEARCH:
			header=search;
			break;
		case SPECIAL_LINKS:
			if(specialLinksBean.getCurrentLink()==SpecialLink.Random)
				header="";
			else
				header=specialLinksBean.getCurrentLinkLable();
			break;

		}
		String tokens[]=header.split("\\s+");
		String contentWords[]=content.split("\\s+");
		if(tokens!=null && contentWords!=null)
		{
			for(String word:contentWords)
			{
				for(String token:tokens)
				{
					if(word.length()>2 && token.length()>2 && word.trim().contains(token.trim()))
						content=content.replace(word, "<strong>"+word+"</strong>");
				}
			}
		}
		return content;
	}
	
	private void extractRequestParams() {
		try {
			
			HttpServletRequest req = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			try {
				cname = req.getParameter("cn");
			} catch (Exception ex) {}
			try {
				page = Integer.parseInt(req.getParameter("pid"));
			} catch (Exception ex) {}
			try {
				categoryId = Long.parseLong(req.getParameter("cid"));
			} catch (Exception ex) {}
			try {
				linkName = req.getParameter("ln");
			} catch (Exception ex) {}
			
		} catch (Exception ex) {
			
		}

	}

	private void load()
	{
	
		switch(mMod)
		{
		case NORMAL:
			categoryExplorerBean.load(categoryId, page);
			break;
		case SEARCH:
			break;
		case SPECIAL_LINKS:
			specialLinksBean.load(linkName, page);
			break;

		}
		
		
	}
	
	public void searchMod()
	{
		mMod=MOD.SEARCH;
		searchHandlerBean.search(search);
		
	}
	
	public void indexPages()
	{
		switch (mMod) {
		case NORMAL:
			categoryExplorerBean.indexPages();
			return;
		case SEARCH:
			searchHandlerBean.indexPages();
			return;
		case SPECIAL_LINKS:
			specialLinksBean.indexPages();
		}

	}
	
	public static String getContentDate(long milisec) {
		return Utility.milisecondsToJalaliDateTime(milisec);
	}
	
	public int getCurrentPage() {
		
		switch(mMod)
		{
			case NORMAL:
				return categoryExplorerBean.getCurrentPage();
			case SEARCH:
				return searchHandlerBean.getCurrentPage();
			case SPECIAL_LINKS:
				return specialLinksBean.getCurrentPage();
			
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
			case SPECIAL_LINKS:
				return specialLinksBean.getPages();
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
	public String getCategoryNameAsUrl(String name,long catId,int page)
	{
		return categoryExplorerBean.getCategoryNameAsUrl(name, catId, page);
	}
	public String getSpecialLinkAsUrl(String name,String code, int page)
	{
		return specialLinksBean.getSpecialLinkAsUrl(name, code, page);
	}
	
	public String getMoveBackUrl(long catId)
	{
		 FacesContext ctx = FacesContext.getCurrentInstance();
	     String contextPath = ctx.getExternalContext().getRequestContextPath();
	     StringBuilder url = new StringBuilder(100);
	     url.append(categoryExplorerBean.getRootUrl(ctx));
	     url.append(contextPath);
	     url.append("/sms/");
	     ContentCategory parent=categoryExplorerBean.getParentCategory(catId);
	     if(parent!=null)
	     {
	    	 url.append(parent.getName().replace(" ", "_"));
		     url.append("?cid="+parent.getId());
	     }
	     else
	     {
	    	 url.append("گروه اصلی");
		     url.append("?cid=0");
	     }
	    
	     url.append("&pid="+1);

		return url.toString();
		
	}

	public ArrayList<ContentCategory> getCatgoryList() {
		
		return categoryExplorerBean.getCatgoryList();
	}
	public ArrayList<ContentCategory> getAllCatgoryList() {
		
		return categoryExplorerBean.getAllCatgoriesList();
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
	
	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public void fakeRequest()
	{
		
	}

	public String getContactUsValue() {
		return contactUsValue;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public String getPageTitle()
	{
		switch(mMod)
		{
		case NORMAL:
			if(categoryExplorerBean.getCurrentCategory()==null)
				return "های مختلف";
			else
				return categoryExplorerBean.getCurrentCategory().getName();
		case SEARCH:
			return search;
		case SPECIAL_LINKS:
			if(specialLinksBean.getCurrentLink()==SpecialLink.Random)
				return "های تصادفی";
			else
				return specialLinksBean.getCurrentLinkLable();
		}
		return "های مختلف";
	}
	
	public String getMetaPageTitle()
	{
		String homePageTitle="جدید ترین و به روز ترین پیامک ها | اس ام اس ها";
		String title="اس ام اس و پیامک های جدید و به روز";
		String header="";
		switch(mMod)
		{
		case NORMAL:
			if(categoryExplorerBean.getCurrentCategory()==null)
				return homePageTitle;
			else
				header=categoryExplorerBean.getCurrentCategory().getName();
			break;
		case SEARCH:
			header=search;
			break;
		case SPECIAL_LINKS:
			if(specialLinksBean.getCurrentLink()==SpecialLink.Random)
				return homePageTitle;
			else
				header=specialLinksBean.getCurrentLinkLable();
			break;

		}
		return header+" | "+title;
	}
	
	public String getMetaDescription()
	{
		String mainDesc="جدید ترین و به روز ترین اس ام اس ها ، پیامک های منتخب و زیبا، جملات حکیمانه از مشاهیر و بزرگان ، داستان ها و حکایت های کوتاه و بلند. ";
		String homePageDesc="و کلی مطالب  جالب .";
		String description="  با موضوع : ";
		switch(mMod)
		{
		case NORMAL:
			if(categoryExplorerBean.getCurrentCategory()==null)
				return mainDesc+homePageDesc;
			else
				description+=categoryExplorerBean.currentCategoryPathName();
			break;
		case SEARCH:
			description+=search;
			break;
		case SPECIAL_LINKS:
			if(specialLinksBean.getCurrentLink()==SpecialLink.Random)
				return mainDesc+homePageDesc;
			else
				description+=specialLinksBean.getCurrentLinkLable();
			break;

		}
		return mainDesc+description+"";
	}
	public String getMetaKeyWords()
	{
		String keywords="اس ام اس"+","+"پیامک"+","+"جمله"+",";
		switch(mMod)
		{
		case NORMAL:
			keywords+=categoryExplorerBean.currentCategoryName();
			break;
		case SEARCH:
			keywords+=search;
			break;
		case SPECIAL_LINKS:
			keywords+=specialLinksBean.getCurrentLinkLable();
			break;

		}
		return keywords;
	}
	
}


