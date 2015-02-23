package com.peaceworld.wikisms.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: ContentCategory
 *
 */

@XmlRootElement
@Entity


@NamedQueries({

@NamedQuery(name="ContentCategory.findAllContentCategories", query="SELECT cat FROM ContentCategory cat"),
@NamedQuery(name="ContentCategory.findAllContentCategoriesByCategoryId", query="SELECT cat FROM ContentCategory cat WHERE cat.id = :catId"),
@NamedQuery(name="ContentCategory.findAllContentCategoriesByCategoryName", query="SELECT cat FROM ContentCategory cat WHERE cat.name = :catName"),
@NamedQuery(name="ContentCategory.checkContentCategoryExistance", query="SELECT cat FROM ContentCategory cat WHERE cat.name = :catName AND cat.parentCategory = :ParentCategory"),
@NamedQuery(name="ContentCategory.findAllSubCategoriesById", query="SELECT cat FROM ContentCategory cat WHERE  cat.parentCategory = :ParentCategory")
})



public class ContentCategory implements Serializable {

	public ContentCategory() {
		super();
	}
	
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	private String name;
	
	private long parentCategory;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(long parentCategory) {
		this.parentCategory = parentCategory;
	}
	
	

	
   
}
