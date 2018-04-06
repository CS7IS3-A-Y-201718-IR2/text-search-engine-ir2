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
		File[] file = new File(Constants.DOCS_FILE_PATH + "/fr94").listFiles();
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
			try {
				System.out.println(f);
				File input = new File(f);
				Document doc = Jsoup.parse(input, "UTF-8", "");

				// Remove elements from the doc
				doc.select("docid").remove();

				Elements docs = doc.select("doc");

				for (Element e : docs) {

					String DocNo = e.getElementsByTag("Docno").text();

					File result = new File(Constants.PARSED_DOCS_FILE_PATH + "/fr94_docs/" + DocNo);
					PrintWriter writer = new PrintWriter(result, "UTF-8");
					writer.println(e.text());
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				// System.out.println("Documents parsed: " + i);
			}
		}

	}
}