package com.ir.searchengine.indexing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class DocLoader {

	Logger logger = Logger.getLogger(DocLoader.class);

	public List<Document> loadFbisDocs(String dataSource) {

		List<Document> documents = new ArrayList<Document>();

		File folder = new File(dataSource);
		for (final File file : folder.listFiles()) {
			String docId = null;
			try {
				String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				docId = file.getName();

				Document doc = new Document();
				doc.add(new StringField("docId", docId, Field.Store.YES));
				doc.add(new TextField("content", content, Field.Store.YES));
				documents.add(doc);
			} catch (IOException e) {
				logger.error("Error loading Fbis: " + docId);
			}

		}

		logger.info("Finished loading Fbis docs in memory.");
		return documents;
	}

	public List<Document> loadFr94Docs(String dataSource) {

		List<Document> documents = new ArrayList<Document>();

		File folder = new File(dataSource);
		for (final File file : folder.listFiles()) {
			String docId = null;
			try {
				String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				docId = file.getName();

				Document doc = new Document();
				doc.add(new StringField("docId", docId, Field.Store.YES));
				doc.add(new TextField("content", content, Field.Store.YES));
				documents.add(doc);
			} catch (IOException e) {
				logger.error("Error loading Fr94Doc: " + docId);
			}

		}

		logger.info("Finished loading Fr94 docs in memory.");
		return documents;
	}

	public List<Document> loadFtDocs(String dataSource) {

		List<Document> documents = new ArrayList<Document>();

		File folder = new File(dataSource);
		for (final File file : folder.listFiles()) {
			String docId = null;
			try {
				String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				docId = file.getName();

				Document doc = new Document();
				doc.add(new StringField("docId", docId, Field.Store.YES));
				doc.add(new TextField("content", content, Field.Store.YES));
				documents.add(doc);
			} catch (IOException e) {
				logger.error("Error loading FtDoc: " + docId);
			}

		}

		logger.info("Finished loading Ft docs in memory.");
		return documents;
	}

	public List<Document> loadLatTimesDocs(String dataSource) {

		List<Document> documents = new ArrayList<Document>();

		File folder = new File(dataSource);
		for (final File file : folder.listFiles()) {
			String docId = null;
			try {
				String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				docId = file.getName();

				Document doc = new Document();
				doc.add(new StringField("docId", docId, Field.Store.YES));
				doc.add(new TextField("content", content, Field.Store.YES));
				documents.add(doc);
			} catch (IOException e) {
				logger.error("Error loading LatTimesDoc: " + docId);
			}

		}

		logger.info("Finished loading LatTimes docs in memory.");
		return documents;
	}

}
