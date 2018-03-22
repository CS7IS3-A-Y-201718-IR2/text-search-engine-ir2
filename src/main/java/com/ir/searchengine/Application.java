package com.ir.searchengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.xml.sax.SAXException;

import com.ir.searchengine.analyzer.CustomAnalyzer;
import com.ir.searchengine.constants.Constants;
import com.ir.searchengine.indexing.IndexDocuments;
import com.ir.searchengine.query.TopicDto;
import com.ir.searchengine.query.XMLQueryParser;

public class Application {

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String args[]) {

		Application app = new Application();

		try {
			Directory dir = FSDirectory.open(Paths.get(Constants.INDEX_DIR));

			// Analyzer analyzer = new CustomAnalyzer();
			Analyzer analyzer = new StandardAnalyzer();
			Similarity similarity = new BM25Similarity();

			List<TopicDto> topics = app.loadTopics();
			List<Query> queries = app.createQuery(analyzer, topics);
			
			// If the directory exists then do not recreate indexes
			if (!new File(Constants.INDEX_DIR).exists()) {
				
				// create the directory
				new File(Constants.INDEX_DIR).mkdirs();
				
				IndexDocuments idx = new IndexDocuments(analyzer, similarity);
				idx.indexFbisDocs();
				idx.indexFr94Docs();
				idx.indexFtDocs();
				idx.indexLatTimeDocs();
			}

			for (Query query : queries)
				app.fireQuery(query, dir, similarity, 10);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}
	}

	/**
	 * Helper method that loads all the topics from the file.
	 * 
	 * @throws Exception
	 */
	public List<TopicDto> loadTopics() throws Exception {
		XMLQueryParser xmlqp;
		try {
			xmlqp = new XMLQueryParser();
			List<TopicDto> topics = xmlqp.getQueryTopics();
			return topics;
		} catch (SAXException | ParserConfigurationException e) {
			logger.error("Error thrown when loading topics.", e);
			throw e;
		}
	}

	public List<Query> createQuery(Analyzer analyzer, List<TopicDto> topics) {
		HashMap<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("content", 1f);

		List<Query> queries = new ArrayList<>();

		for (TopicDto topic : topics) {
			MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "content" }, analyzer,
					boosts);
			Query q;
			try {
				q = multiFieldQP.parse(topic.getDesc());
				queries.add(q);
			} catch (ParseException e) {
				logger.error("Error parsing query.");
			}
		}

		return queries;
	}

	public ScoreDoc[] fireQuery(Query query, Directory dir, Similarity similarity, int hitsPerPage) {

		try (IndexReader reader = DirectoryReader.open(dir)) {
			IndexSearcher searcher = new IndexSearcher(reader);

			// Scoring - Similarity
			searcher.setSimilarity(similarity);

			TopDocs docs = searcher.search(query, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			// display results
			boolean printResults = true;
			if (printResults) {
				System.out.println("Found " + hits.length + " hits.");
				System.err.print("\nRES_NO | DOCID | TITLE | SCORE\n");
				for (int i = 0; i < hits.length; ++i) {
					int docId = hits[i].doc;
					double score = hits[i].score;
					Document d = searcher.doc(docId);
					System.out.println((i + 1) + ". " + d.get("docId") + "\t" + d.get("title") + "\t" + score);
				}
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
			reader.close();

			return hits;
		} catch (IOException e) {
			logger.error("Failed to execute query '" + query + "'", e);
		}

		return null;

	}
}
