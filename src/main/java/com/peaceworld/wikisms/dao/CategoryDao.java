package com.peaceworld.wikisms.dao;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.ContentCategory;

@Stateless
public class CategoryDao {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	public ArrayList<ContentCategory> getSubCategoriesByParentId(long parentId)
	{
			try{
				ArrayList<ContentCategory> categoryList=(ArrayList<ContentCategory>)
						em.createNamedQuery("ContentCategory.findAllSubCategoriesById")
						.setParameter("ParentCategory", parentId).getResultList();
				return categoryList;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return null;
	}
	
	public ContentCategory getCategoryById(long Id) {
		
		try {
			ContentCategory contentCategory = (ContentCategory) em.find(ContentCategory.class, Id);
			return contentCategory;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public ContentCategory getCategoryByName(String catName) {
		
		try {
			ContentCategory categorie= (ContentCategory) em.createNamedQuery("ContentCategory.findAllContentCategoriesByCategoryName")
					.setParameter("catName", catName).getSingleResult();
			return categorie;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<ContentCategory> getAll()
	{
		
		try {
			ArrayList<ContentCategory> categories=(ArrayList<ContentCategory>) em.createNamedQuery("ContentCategory.findAllContentCategories").getResultList();
			return categories;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String create(long id,String newCategoryName,long parentCategory)
	{
		try {
			 boolean preDef=em.createNamedQuery("ContentCategory.checkContentCategoryExistance").
			setParameter("catName", newCategoryName).setParameter("ParentCategory", parentCategory).getResultList().isEmpty();
			if(preDef)
			{
				ContentCategory newCategory=new ContentCategory();
				newCategory.setId(id);
				newCategory.setName(newCategoryName);
				if(parentCategory<=0)
					parentCategory=0;
				newCategory.setParentCategory(parentCategory);
				em.persist(newCategory);
				em.flush();
				return newCategory.getId()+"";
			}
			else
				return "Previously exist";
				
			
		} catch (Exception e) {
			return e.toString();
		}
		
	}
	
	
}
