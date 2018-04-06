package com.ir.searchengine.docparsers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ir.searchengine.constants.Constants;

public class FbisParser {
	public static void main(String[] args) throws IOException {

		List<String> results = new ArrayList<String>();
		File[] file = new File(Constants.DOCS_FILE_PATH + "/fbis").listFiles();
		// System.out.println("hello");

		File baseDir = new File(Constants.PARSED_DOCS_FILE_PATH + "/fbis_docs/");
		baseDir.mkdirs();
		int i = 0;
		try {

			for (File files : file) {
				try {
					if (files.isFile()) {
						System.out.println(files.getAbsolutePath());
						// System.out.println("Directory: " + file.length);
						results.add(files.getName());
						// System.out.println(files.getName());
						File input = new File(files.getAbsolutePath());
						Document doc = Jsoup.parse(input, "UTF-8", "");
	
						doc.select("docid").remove();
	//					doc.select("tablerow").remove();
	//					// doc.select("table").remove();
	//					doc.select("tablecell").remove();
	//					doc.select("rowrule").remove();
	//					doc.select("cellrule").remove();
	//					doc.select("section").remove();
	//					doc.select("length").remove();
	//					doc.select("graphic").remove();
	//					doc.select("docid").remove();
	//					doc.select("dateline").remove();
	//					doc.select("date").remove();
	//					doc.select("correction-date").remove();
	
						Elements docs = doc.select("doc");
						// System.out.println("Doc Size"+docs.size());
	
						for (Element e : docs) {
							i++;
							String DocNo = e.getElementsByTag("Docno").text();
							// String TextContent = e.getElementsByTag("Text").text();
							// String ByLine = e.getElementsByTag("Byline").text();
							// String Correction = e.getElementsByTag("Correction").text();
							// String Header = e.getElementsByTag("Header").text();
							// String Subject = e.getElementsByTag("Subject").text();
							// String Type = e.getElementsByTag("Type").text();
							File result = new File(Constants.PARSED_DOCS_FILE_PATH + "/fbis_docs/" + DocNo);
	
							String entireDoc = e.getAllElements().text();
	
							PrintWriter writer = new PrintWriter(result, "UTF-8");
							writer.println(entireDoc);
							writer.close();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Documents parsed: " + i);
		}
		
		System.out.println(results.size());
	}
}
