package com.peaceworld.wikisms.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.bean.ViewScoped;

import com.peaceworld.wikisms.dao.CategoryDao;
import com.peaceworld.wikisms.dao.ContentDao;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;

@ViewScoped
@Stateful
public class CategoryExplorerBean extends ContentHandlerBean implements Serializable{
	
	private static final long serialVersionUID = 6958715060041482702L;
	
	@EJB
	CategoryDao categoryDao;
	@EJB
	ContentDao contentDao;
	
	private ContentCategory currentCategory;
	private ArrayList<ContentCategory>catgoryList;


	public CategoryExplorerBean() {
		super();
		currentCategory=null;
	}
	

	
	@PostConstruct
	public void load()
	{
		loadCategories();
		loadContents();
	}
	
	private void loadContents() {
		
		goToPage(1);
		
	}

	
	private void loadCategories() {

		try{
			long categoryId= currentCategory==null?0 : currentCategory.getId();
			ArrayList<ContentCategory> tmpList=categoryDao.getSubCategoriesByParentId(categoryId);
			if(tmpList!=null && tmpList.size()>0)
				catgoryList= tmpList;
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
			//System.out.println("count:"+contentCounter+"  pages:"+pageCounter);
		}
		else
			pageCounter=0;
	}
	
	public void deepOutCategory()
	{
		
		if(currentCategory!=null)
		{
			currentCategory=categoryDao.getCategoryById(currentCategory.getParentCategory());
			reEvaluatePages();
			load();
		}
		
	}
	
	public void deepInCategory(long categoryId)
	{
		currentCategory=categoryDao.getCategoryById(categoryId);
		reEvaluatePages();
		load();
	}

	public ArrayList<Content> getContentList() {
		return contentList;
	}



	public ArrayList<ContentCategory> getCatgoryList() {
		return catgoryList;
	}



	public String currentCategoryName() {
		if(currentCategory!=null)
			return currentCategory.getName();
		else return "";
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


