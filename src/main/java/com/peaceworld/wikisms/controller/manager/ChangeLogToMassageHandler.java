package com.peaceworld.wikisms.controller.manager;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.peaceworld.wikisms.model.CategoryNotification;
import com.peaceworld.wikisms.model.Content;
import com.peaceworld.wikisms.model.ContentCategory;
import com.peaceworld.wikisms.model.ContentNotification;

@Stateless
public class ChangeLogToMassageHandler {
	
	@PersistenceContext(name = "wiki")
	EntityManager em;

	@Resource
	UserTransaction ut;
	
	final static String notification_dialog_comment = "توضیحات شما:";
	final static String notification_dialog_content_delete = "درخواست کرده اید پیامک زیر از سیستم حذف شود ";
	final static String notification_dialog_content_edited = "درخواست کرده اید متن پیامک اصلاح شود";
	final static String notification_dialog_content_edited_new_value = "متن اصلاح شده";
	final static String notification_dialog_content_add_category = "پیشنهاد کرده اید  پیامک زیر به یک گروه دیگر نیز افزوده شود ";
	final static String notification_dialog_content_changed_category = "پیشنهاد کرده اید گروه پیامک زیر عوض شود";
	final static String notification_dialog_content_create = "یک پیامک جدید ارسال کرده اید ";
	final static String notification_dialog_category_create = "یک گروه جدید در سیستم ایجاد کرده اید";
	final static String notification_dialog_category_merge = "چند گروه را  در سیستم ادغام کرده اید";
	final static String notification_dialog_category_move = "چند گروه را  در سیستم جابه جا کرده اید";
	final static String notification_dialog_category_edit = "یک گروه در سیستم تغییر داده اید";
	final static String notification_dialog_category_delete = "یک گروه از سیستم حذف کرده اید";
	final static String notification_dialog_unkown = "این اطلاع برای شما غیر قابل نمایش است";
	
	private String showInfo1,showInfo2,showInfo3;
	private String showTitle1,showTitle2,showTitle3;
	private boolean showInfo1Visibility,showInfo2Visibility,showInfo3Visibility;
	private boolean showTitle1Visibility,showTitle2Visibility,showTitle3Visibility;
	private ContentNotification contentNotification;
	private CategoryNotification categoryNotification;
	
	public ChangeLogToMassageHandler() {
		setParamToDefualt();
	}
	
	private void setParamToDefualt()
	{
		contentNotification=null;
		categoryNotification=null;
		showInfo1Visibility=showInfo2Visibility=showInfo3Visibility=
				showTitle1Visibility=showTitle2Visibility=showTitle3Visibility=false;
		showInfo1=showInfo2=showInfo3=showTitle1=showTitle2=showTitle3="";
	}
	
	public String getMessage(ContentNotification contentNotification)
	{
		try{
			setParamToDefualt();
			this.contentNotification=contentNotification;
			constractMessageFromContentNotification();
			return sumupResultMessage();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}
	
	public String getMessage(CategoryNotification categoryNotification)
	{
		try{
			setParamToDefualt();
			this.categoryNotification=categoryNotification;
			constractMessageFromCategoryNotification();
			return sumupResultMessage();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}
	
	public String sumupResultMessage()
	{
		String result="";
		if(showTitle1Visibility)
			result+="<span style=\"color: red;font-weight:bold;\">"+showTitle1+"</span><br />";
		if(showInfo1Visibility)
			result+=showInfo1+"<br />";
		if(showTitle2Visibility)
			result+="<span style=\"color: red;font-weight:lighter;\">"+showTitle2+"</span><br />";
		if(showInfo2Visibility)
			result+=showInfo2+"<br />";
		if(showTitle3Visibility)
			result+="<span style=\"color: red;font-weight:lighter;\">"+showTitle3+"</span><br />";
		if(showInfo3Visibility)
			result+=showInfo3+"<br />";
		
		
		return result;
	}
	public void constractMessageFromCategoryNotification()
	{
		if(categoryNotification!=null)
		{
			CategoryNotification.ACTION action=CategoryNotification.ACTION.valueOf(categoryNotification.getAction());
			switch(action)
			{
			case CATEGORY_CHANGED:
				notification_action_CATEGORY_CHANGED();
				break;
			case CATEGORY_CREATE:
				notification_action_CATEGORY_CREATE();
				break;
			case CATEGORY_DELETE:
				notification_action_CATEGORY_DELETE();
				break;
			case CATEGORY_MERGE:
				notification_action_CATEGORY_MERGE();
				break;
			case CATEGORY_MOVE:
				notification_action_CATEGORY_MOVE();
				break;
				
			default:
				notification_action_UNKONWN();
				break;
			}
			
		}
		
	}
	public void constractMessageFromContentNotification()
	{
		if(contentNotification!=null)
		{
			ContentNotification.ACTION action= ContentNotification.ACTION.valueOf(contentNotification.getAction());
			switch(action)
			{
			case CONTENT_DELETE:
				constructContentNotification_action_CONTENT_DELETE();
				break;
			case CONTENT_EDIT:
				constructContentNotification_action_CONTENT_EDIT();
				break;
			case CONTENT_TAG_ADDED:
				constructContentNotification_action_CONTENT_TAG_ADDED();
				break;
			case CONTENT_TAG_CHANGED:
				constructContentNotification_action_CONTENT_TAG_CHANGED();
				break;
			case CONTENT_CREATE:
				notification_action_CONTENT_CREATE();
				break;
				
			default:
				notification_action_UNKONWN();
				break;
			
			}
		}
	}
	
	private void notification_action_CONTENT_CREATE() {

		showTitle1=notification_dialog_content_create;
		showInfo1=contentNotification.getMetaInfo();
		showInfo1Visibility=showTitle1Visibility=true;
	}

	private void notification_action_UNKONWN() {

		showTitle1=notification_dialog_unkown;
		showTitle1Visibility=true;
	}

	private void notification_action_CATEGORY_MOVE() {

		showTitle1=notification_dialog_category_move;
		ContentCategory c1= em.find(ContentCategory.class,categoryNotification.getOperand1());
		ContentCategory c2= em.find(ContentCategory.class,categoryNotification.getOperand2());
		String c1String= c1==null?"---":c1.getName();
		String c2String= c2==null?"---":c2.getName();
		showInfo1=" گروه "+c1String+" به زیر گروه های "+c2String+" منتقل شده";
		showInfo1Visibility=showTitle1Visibility=true;
		
	}

	private void notification_action_CATEGORY_MERGE() {

		showTitle1=notification_dialog_category_merge;
		ContentCategory c1= em.find(ContentCategory.class,categoryNotification.getOperand1());
		ContentCategory c2= em.find(ContentCategory.class,categoryNotification.getOperand2());
		String c1String= c1==null?"---":c1.getName();
		String c2String= c2==null?"---":c2.getName();
		showInfo1=(" گروه "+c1String+" و گروه "+c2String+" ادغام شده "+"\r\n با نام جدید :"+categoryNotification.getMetadata());
		showInfo1Visibility=showTitle1Visibility=true;
	}

	private void notification_action_CATEGORY_DELETE() {
		
		showTitle1=notification_dialog_category_delete;
		ContentCategory cc= em.find(ContentCategory.class,categoryNotification.getOperand1());
		String ccString= cc==null?"---":cc.getName();
		showInfo1="نام گروه :"+ccString;
		showInfo3=categoryNotification.getComment();
		
		showTitle1Visibility=true;
		showInfo1Visibility=true;
		showTitle3Visibility=true;
		showInfo3Visibility=true;
	}

	private void notification_action_CATEGORY_CREATE() {
		
		showTitle1=notification_dialog_category_create;
		String parentName="";
		if(categoryNotification.getOperand2()==0)
			parentName="  اصلی ";
		else{
			ContentCategory pc= em.find(ContentCategory.class,categoryNotification.getOperand2());
			String pcString= pc==null?"---":pc.getName();
			parentName=" "+ pcString+" ";
		}
		showInfo1=" گروه "+categoryNotification.getMetadata()+" به عنوان یک زیرگروه "+parentName+" ایجاد شود";
		
		showInfo1Visibility=showTitle1Visibility=true;
	}

	private void notification_action_CATEGORY_CHANGED() {
		
		showTitle1=notification_dialog_category_edit;
		ContentCategory c1= em.find(ContentCategory.class,categoryNotification.getOperand1());
		showInfo2=c1.getName()+" تغییر نام یافته به "+categoryNotification.getMetadata();
		showTitle1Visibility=showInfo1Visibility=showTitle2Visibility=showInfo2Visibility=true;
	}

	private void constructContentNotification_action_CONTENT_TAG_CHANGED() {
		
		Content content= em.find(Content.class,contentNotification.getContentId());
		showTitle1=notification_dialog_content_changed_category;
		showInfo1=content.getPlainText();
		ContentCategory newCategory= em.find(ContentCategory.class,contentNotification.getOperand2());
		ContentCategory oldCategory= em.find(ContentCategory.class,contentNotification.getOperand1());
		String newCategoryString= newCategory==null?"---":newCategory.getName();
		String oldCategoryString= oldCategory==null?"---":oldCategory.getName();
		showTitle2=""+oldCategoryString+"  به  "+newCategoryString+" تغییر میکند";
		showTitle1Visibility=showInfo1Visibility=showTitle2Visibility=showInfo2Visibility=true;
	}

	private void constructContentNotification_action_CONTENT_TAG_ADDED() {
		Content content= em.find(Content.class,contentNotification.getContentId());
		showTitle1=notification_dialog_content_add_category;
		showInfo1=content.getPlainText();
		
		ContentCategory newCategory= em.find(ContentCategory.class,contentNotification.getOperand1());
		String newCategoryString= newCategory==null?"---":newCategory.getName();
		showTitle2=" به گروه "+newCategoryString+"نیز افزوده شود .";
		showTitle1Visibility=showInfo1Visibility=showTitle2Visibility=showInfo2Visibility=true;
		
	}

	private void constructContentNotification_action_CONTENT_EDIT() {
		
		Content content=em.find(Content.class,contentNotification.getContentId());
		showTitle1=notification_dialog_content_edited;
		showInfo1=content.getPlainText();
		
		showTitle2=notification_dialog_content_edited_new_value;
		showInfo2=contentNotification.getMetaInfo();
		showTitle1Visibility=showInfo1Visibility=showTitle2Visibility=showInfo2Visibility=true;
		
		if(contentNotification.getComment() !=null &&contentNotification.getComment().length()>3)
		{
			showTitle3=notification_dialog_comment;
			showInfo3=contentNotification.getComment();
			showTitle3Visibility=showInfo3Visibility=true;
		}
	
	}

	private void constructContentNotification_action_CONTENT_DELETE() {
		
		Content content= em.find(Content.class,contentNotification.getContentId());
		showTitle1=notification_dialog_content_delete;
		showInfo1=content.getPlainText();
		showTitle1Visibility=showInfo1Visibility=true;
		if(contentNotification.getComment() !=null &&contentNotification.getComment().length()>3)
		{
			showTitle2=notification_dialog_comment;
			showInfo2=contentNotification.getComment();
			showTitle2Visibility=showInfo2Visibility=true;
		}
		
	}

}
