package com.ir.searchengine.docparsers;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ir.searchengine.constants.Constants;

public class Fr94Parser {
	public static void main(String[] args) throws IOException {

		File baseDir = new File(Constants.PARSED_DOCS_FILE_PATH + "/fr94_docs/");
		baseDir.mkdirs();
		
		List<String> results = new ArrayList<String>();
		File[] file = new File(Constants.DOCS_FILE_PATH+"/fr94").listFiles();
		System.out.println(file);
		ArrayList<String> files1 = new ArrayList<String>();
		for (File files : file) {
			if (files.isDirectory()) {
				System.out.println(files.getPath());
				System.out.println();
				for (File f : files.listFiles()) {
					files1.add(f.getAbsolutePath());

				}
			}
		}

		for (String f : files1) {
			System.out.println(f);
			File input = new File(f);
			Document doc = Jsoup.parse(input, "UTF-8", "");

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
			// doc.select("date").remove();
			doc.select("correction-date").remove();

			Elements docs = doc.select("doc");
			// System.out.println(docs.size());

			for (Element e : docs) {

				String DocNo = e.getElementsByTag("Docno").text();
				String Parent = e.getElementsByTag("Parent").text();
				String USDept = e.getElementsByTag("USDept").text();
				String USBureau = e.getElementsByTag("USBureau").text();
				String Agency = e.getElementsByTag("Agency").text();
				String Date = e.getElementsByTag("Date").text();
				String Further = e.getElementsByTag("Further").text();
				String Supplem = e.getElementsByTag("Supplem").text();
				String Summary = e.getElementsByTag("Summary").text();

				System.out.println(DocNo + ":   ");
				System.out.println("Parent" + ":   " + Parent);
				System.out.println("USDept" + ":   " + USDept);
				System.out.println("USBureau" + ":   " + USBureau);
				System.out.println("Date" + ":   " + Date);
				System.out.println("Agency" + ":   " + Agency);
				System.out.println("Summary" + ":   " + Summary);
				System.out.println("Further info" + ":   " + Further);
				System.out.println("Supplem" + ":   " + Supplem);

				File result = new File(Constants.PARSED_DOCS_FILE_PATH+"/fr94_docs/" + DocNo);
				PrintWriter writer = new PrintWriter(result, "UTF-8");
				writer.println(DocNo + '\n' + Parent + '\n' + USDept + '\n' + USBureau + '\n' + Agency + '\n' + Date
						+ '\n' + Summary + '\n' + Further + '\n' + Supplem);
				writer.close();
			}

		}

	}
}