package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.peaceworld.wikisms.controller.beans.ContentHandlerBean.Page;
import com.peaceworld.wikisms.controller.beans.SpecialLinksBean.LinkHelper;
import com.peaceworld.wikisms.dao.CategoryDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;

@RequestScoped
@Stateful
public class CategoryExplorerBean extends ContentHandlerBean implements Serializable{
	
	private static final long serialVersionUID = 6958715060041482702L;
	
	@EJB
	CategoryDao categoryDao;
	@EJB
	ContentDao contentDao;
	
	private ContentCategory currentCategory;
	private ArrayList<ContentCategory>catgoryList;
	private ArrayList<ContentCategory>allCatgoriesList;


	public CategoryExplorerBean() {
		super();
		currentCategory=null;
	}
	
	public void baseLoad()
	{
		try {
			load(0, 1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void load(long catId,int page)
	{
		try {
			currentCategory=categoryDao.getCategoryById(catId);
			load(page);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void load(int page)
	{
		try {
			loadCategories();
			reEvaluatePages();
			if (currentCategory != null && currentCategory.getId() > 0) {
				goToPage(page);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	
	private void loadCategories() {

		try{
			allCatgoriesList=categoryDao.getAll();
			long categoryId= currentCategory==null?0 : currentCategory.getId();
			ArrayList<ContentCategory> tmpList=categoryDao.getSubCategoriesByParentId(categoryId);
			if(tmpList!=null && tmpList.size()>0)
				catgoryList=tmpList;
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	public void goToPrePage()
	{
		if(currentPage>1)
			goToPage(currentPage-1);
	}
	
	public void goToNextPage()
	{
		goToPage(currentPage+1);
		
	}
	
	public void goToPage(int index)
	{
		try{
			long categoryId= currentCategory==null?0 : currentCategory.getId();
			int firstResult=(index-1)*LIST_SIZE_LIMIT;
			contentList= contentDao.getContentsByCategoryOriginal(categoryId,firstResult, LIST_SIZE_LIMIT);
			if(contentList!=null && contentList.size()>0)
				currentPage=index;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	protected void reEvaluatePages()
	{
		long contentCounter=-1;
		
		if(currentCategory!=null && ( contentCounter = contentDao.getContentsCountByCategory(currentCategory.getId())) > 0 )
		{
			pageCounter=(int)(Math.ceil(contentCounter*1.0d/LIST_SIZE_LIMIT));
		}
		else
			pageCounter=0;
	}
	
	public void deepOutCategory()
	{
		if(currentCategory!=null)
		{
			currentCategory=categoryDao.getCategoryById(currentCategory.getParentCategory());
			load(1);
		}
	}
	
	public ContentCategory getParentCategory(long catId)
	{
		try {
			ContentCategory cat = categoryDao.getCategoryById(catId);
			if (cat != null)
				return categoryDao.getCategoryById(cat.getParentCategory());
			else
				return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public ContentCategory deepInCategory(long categoryId)
	{
		currentCategory=categoryDao.getCategoryById(categoryId);
		load(1);
		return currentCategory;
	}
	
	public String getCategoryNameAsUrl(String name,long catId,int page)
	{
		 FacesContext ctx = FacesContext.getCurrentInstance();
	     String contextPath = ctx.getExternalContext().getRequestContextPath();
	     StringBuilder url = new StringBuilder(100);
	     url.append(getRootUrl(ctx));
	     url.append(contextPath);
	     url.append("/sms/");
	     String categoryName= name==null ?"گروه اصلی": name.trim();
	     url.append("اس ام اس ها و پیامک های ".replace(" ", "_"));
	     url.append(categoryName.replace(" ", "_"));
	     url.append("?cid="+catId);
	     url.append("&pid="+page);

		return url.toString();
		
	}
	
	public void indexPages()
	{
		super.indexPages();
		
		for(Page page:pages)
			page.setHref(getCategoryNameAsUrl(currentCategory.getName(), currentCategory.getId(), page.getPageNumber()));
	}

	public ArrayList<Content> getContentList() {
		return contentList;
	}



	public ArrayList<ContentCategory> getCatgoryList() {
		return catgoryList;
	}
	public ArrayList<ContentCategory> getAllCatgoriesList() {
		return allCatgoriesList;
	}



	public String currentCategoryName() {
		if(currentCategory!=null)
			return currentCategory.getName();
		else return "";
	}
	
	public String currentCategoryPathName() {
		String pathName="";
		ContentCategory tmpCategory=currentCategory;
		while(tmpCategory!=null)
		{
			pathName+=tmpCategory.getName()+" ";
			tmpCategory=categoryDao.getCategoryById(tmpCategory.getParentCategory());
		}
		return pathName;
	}
	

	public int getCurrentPage() {
		return currentPage;
	}



	public ContentCategory getCurrentCategory() {
		return currentCategory;
	}



	public ArrayList<Page> getPages() {
		return pages;
	}



	public void setCurrentCategory(Object object) {
		currentCategory=null;
	}
	
	
	

}


