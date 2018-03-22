package com.ir.searchengine.indexing;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.ir.searchengine.constants.Constants;

public class IndexDocuments {

	Logger logger = Logger.getLogger(IndexDocuments.class);
	DocLoader docLoader;
	Analyzer analyzer;
	Similarity similarity;

	public IndexDocuments(Analyzer analyzer, Similarity similarity) {
		this.docLoader = new DocLoader();
		this.analyzer = analyzer;
		this.similarity = similarity;
	}

	public void indexFbisDocs() {
		List<Document> docs = docLoader.loadFbisDocs();
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);
		
		try (Directory dir = FSDirectory.open(Paths.get(Constants.INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void indexFr94Docs() {
		List<Document> docs = docLoader.loadFr94Docs();
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);

		try (Directory dir = FSDirectory.open(Paths.get(Constants.INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void indexFtDocs() {
		List<Document> docs = docLoader.loadFtDocs();
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);

		try (Directory dir = FSDirectory.open(Paths.get(Constants.INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void indexLatTimeDocs() {
		List<Document> docs = docLoader.loadLatTimesDocs();
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);

		try (Directory dir = FSDirectory.open(Paths.get(Constants.INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private IndexWriterConfig getIndexWriterConfig(Analyzer analyzer, Similarity similarity) {
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setSimilarity(similarity);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		return iwc;
	}
}
