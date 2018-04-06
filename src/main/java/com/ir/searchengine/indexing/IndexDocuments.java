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

public class IndexDocuments {

	Logger logger = Logger.getLogger(IndexDocuments.class);
	DocLoader docLoader;
	Analyzer analyzer;
	Similarity similarity;
	String INDEX_DIR = null;
	String dataSource;

	public IndexDocuments(Analyzer analyzer, Similarity similarity, String INDEX_DIR, String dataSource) {
		this.docLoader = new DocLoader();
		this.analyzer = analyzer;
		this.similarity = similarity;
		this.INDEX_DIR = INDEX_DIR;
		this.dataSource = dataSource;
	}

	public void indexFbisDocs() {
		List<Document> docs = docLoader.loadFbisDocs(dataSource+"/fbis_docs");
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);
		
		try (Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void indexFr94Docs() {
		List<Document> docs = docLoader.loadFr94Docs(dataSource+"/fr94_docs");
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);

		try (Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void indexFtDocs() {
		List<Document> docs = docLoader.loadFtDocs(dataSource+"/ft_docs");
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);

		try (Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
				IndexWriter writer = new IndexWriter(dir, iwc)) {
			writer.addDocuments(docs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void indexLatTimeDocs() {
		List<Document> docs = docLoader.loadLatTimesDocs(dataSource+"/latimes_docs");
		IndexWriterConfig iwc = getIndexWriterConfig(analyzer, similarity);

		try (Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
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
