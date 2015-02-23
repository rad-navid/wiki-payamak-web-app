package com.peaceworld.wikisms.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

public class Utility {

	public static String toJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static String gzipCompress(String data) {

		try {

			byte[] binaryData = data.getBytes("UTF-8");
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(outStream);
			gzos.write(binaryData, 0, binaryData.length);
			gzos.finish();
			gzos.close();
			binaryData = null;
			System.out.println("Done");
			String compressedData = new sun.misc.BASE64Encoder().encode(outStream.toByteArray());
			outStream.close();
			return compressedData;
		} catch (IOException ex) {
			return ex.toString();
		}

	}

	
	public static String gzipUncompress(String data){
		 
	     byte[] buffer = new byte[1024];
	 
	     try{
	    	 byte[] bytearray = new sun.misc.BASE64Decoder().decodeBuffer(data);
	    	 ByteArrayInputStream inStream = new ByteArrayInputStream(bytearray);
	    	 GZIPInputStream gzis = new GZIPInputStream(inStream);
	 
	    	 ByteArrayOutputStream outStream=new ByteArrayOutputStream();
	    	 
	        int len;
	        while ((len = gzis.read(buffer)) > 0) {
	        	outStream.write(buffer, 0, len);
	        }
	        gzis.close();
	        inStream.close();
	        String result=outStream.toString("UTF-8");
	    	outStream.close();
	    	
	    	System.out.println("Done");
	    	return result;
	 
	    }catch(IOException ex){
	       return ex.toString(); 
	    }
	   } 

	
	public static double getSimilarity(HashSet<String>list1,HashSet<String>list2)
	{
		
		int l1=list1.size();
		int l2=list2.size();
		
		HashSet<String>bigSet = l1>=l2 ? list1 : list2;
		HashSet<String>smallSet = l1>=l2 ? list2 : list1;
		
		if(Math.abs(smallSet.size()-bigSet.size())>0.25d*smallSet.size())
			return 0.0d;
		
		int repeted=0;
		Iterator<String> itrator=smallSet.iterator();
		while(itrator.hasNext())
		{
			String token=itrator.next();
			if(bigSet.contains(token))
				repeted++;
		}
		
		return (repeted*1.0d)/bigSet.size();
	}
	

	public static String removeUselessCharacters(String s1) {
		return s1.replace(".", " ").replace("!", " ").replace("?", " ").replace("؟", " ").
				replace("|", " ").replace("(", " ").replace(")", " ").replace(":", " ").replace(";", " ")
				.replace("،", " ").replace(",", " ").replace("؛", " ").replace("”", " ").replace("'", " ")
				.replace("“", " ").replace("”", " ").replace("…", " ")
				.replace("\"", " ")
				.replace("\u064a", "\u06cc") //replace Ye with Arabic Ye
				.replace("\u0626", "\u06cc")	//replace Capped Hamze Ye with Arabic Ye
				.replace("\u0643", "\u06a9")	//replace Capped Kaf with Kaf 
				.replace("\u0624", "\u0648")	//replace Capped Vav with Vav
				.replace("\u0629", "\u0647");	//replace Arabic He with He	
	}
	
	public static String milisecondsToJalaliDateTime(long milis)
	{
		ULocale locale = new ULocale("fa_IR@calendar=persian");
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(milis);
		DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);

		String jalaliDate=df.format(calendar);
		return jalaliDate;
	}
	
	public static String HtmlToPlainText(String html)
	{
		Document doc = Jsoup.parse(html); 
		String text = doc.body().text(); 
		return text;
	}
	
	public static void invalidateSession(HttpServletRequest httpResuest)
	{
		HttpSession session=null;
		try{
			if(httpResuest==null)
				return ;
			
			session= httpResuest.getSession(false);
			if(session!=null)
			{
				try{
					session.invalidate();
				}
				catch(Exception ex2)
				{
					
				}
			}
		}
		catch(Exception ex)
		{
			
		}
	
		
	}

}
