package com.ir.searchengine.docparsers;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ir.searchengine.constants.Constants;

public class LaTimesParser {
	public static void main(String[] args) throws IOException {
	
		File baseDir = new File(Constants.PARSED_DOCS_FILE_PATH + "/latimes_docs/");
		baseDir.mkdirs();
		
	    	List<String> results = new ArrayList<String>();
	    	File[] file = new File(Constants.DOCS_FILE_PATH+"/latimes").listFiles();
	   // 	System.out.println("hello");
	    	for(File files : file){
	    		if(files.isFile()){
	    			System.out.println(files.getAbsolutePath());
    		//	System.out.println("Directory: " + file.length);
	    			results.add(files.getName());    		
	    		//System.out.println(files.getName());
	    		File input = new File(files.getAbsolutePath());
	    		Document doc = Jsoup.parse(input,"UTF-8", "");
	    		   
	    		doc.select("docid").remove();
	    	    doc.select("tablerow").remove();
	    	    doc.select("table").remove();
	    	    doc.select("tablecell").remove();
	    	    doc.select("rowrule").remove();
	    	    doc.select("cellrule").remove();
	    	    doc.select("section").remove();
	    	    doc.select("length").remove();
	    	    doc.select("graphic").remove();
	    	    doc.select("docid").remove();
	    	    doc.select("dateline").remove();
	    	    doc.select("date").remove();
	    	    doc.select("correction-date").remove();

	    	  
	    	    Elements docs = doc.select("doc");
	    	    //System.out.println(docs.size());

	    	    for (Element e: docs) {

	    	    		String DocNo = e.getElementsByTag("Docno").text();
	    	    		String TextContent = e.getElementsByTag("Text").text();
	    	    		String ByLine = e.getElementsByTag("Byline").text();
	    	    		String Correction = e.getElementsByTag("Correction").text();
	    	    		String Headline = e.getElementsByTag("Headline").text();
	    	    		String Subject = e.getElementsByTag("Subject").text();
	    	    		String Type = e.getElementsByTag("Type").text();

	    	    			System.out.println(DocNo + ":   ");
	    				System.out.println("Headline" + ":   " + Headline);
	    				System.out.println("TextContent" + ":   " + TextContent);
	    				System.out.println("ByLine" + ":   " + ByLine);
	    				System.out.println("Subject" + ":   " + Subject);
	    				System.out.println("Correction" + ":   " + Correction);
	    				System.out.println("Type" + ":   " + Type);
	    			
	    				 File result = new File(Constants.PARSED_DOCS_FILE_PATH+"/latimes_docs/"+DocNo);
	    	             PrintWriter writer = new PrintWriter(result, "UTF-8");
	    	             writer.println(DocNo + '\n'+ Headline + '\n'+ TextContent+'\n'+ ByLine + '\n'+ Subject + '\n'+ Correction + '\n' + Type);
	    	             writer.close();
	    			
	    		}
	    		}
	    	System.out.println(results.size());
	}
}
}

