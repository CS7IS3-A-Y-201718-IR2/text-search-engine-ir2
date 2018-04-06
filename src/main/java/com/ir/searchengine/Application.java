package com.ir.searchengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.xml.sax.SAXException;

import com.ir.searchengine.analyzer.CustomAnalyzer;
import com.ir.searchengine.constants.Constants;
import com.ir.searchengine.indexing.IndexDocuments;
import com.ir.searchengine.query.QueryExpansion;
import com.ir.searchengine.query.ScorePair;
import com.ir.searchengine.query.TopicDto;
import com.ir.searchengine.query.XMLQueryParser;
import com.ir.searchengine.results.Result;
import com.ir.searchengine.results.ResultsWriter;

public class Application {

	private static final Logger logger = Logger.getLogger(Application.class);
	private static boolean isMap = false;

	public static void main(String args[]) {

		String outputFolder = null;
		String dataSourcePath = null;
		String queryFile = null;
		String indexDirPath = null;
		String assets = null;

		for (int i = 0; i < args.length; i++) {
			if ("-output".equals(args[i])) {
				if (args[i + 1] != null || !args[i + 1].equals(""))
					outputFolder = args[i + 1];
				else
					throw new RuntimeException("Output directory not present! Must specify as argument.");
				i++;
			} else if ("-dataSource".equals(args[i])) {
				if (args[i + 1] != null || !args[i + 1].equals(""))
					dataSourcePath = args[i + 1];
				else
					throw new RuntimeException("Must specify source folder for data.");
				i++;
			} else if ("-queryFile".equals(args[i])) {
				if (args[i + 1] != null || !args[i + 1].equals(""))
					queryFile = args[i + 1];
				else
					throw new RuntimeException("Must specify the location of query file.");
				i++;
			} else if ("-indexDir".equals(args[i])) {
				if (args[i + 1] != null || !args[i + 1].equals(""))
					indexDirPath = args[i + 1];
				else
					throw new RuntimeException("Specify a location to store indexes.");
				i++;
			}
			else if ("-assets".equals(args[i])) {
				if (args[i + 1] != null || !args[i + 1].equals(""))
					assets = args[i + 1];
				else
					throw new RuntimeException("Specify a location to store indexes.");
				i++;
			}
		}

		Constants.SYNONYM_FILE_PATH = assets + "/stopwords.txt";
		Constants.STOP_WORDS_FILE_PATH = assets + "/wn_s.pl";
		
;		// Declare a Application object
		Application app = new Application();

		QueryExpansion queryExpander = new QueryExpansion();

		try {
			Directory dir = null;
			//Analyzer analyzer = new CustomAnalyzer();
			Analyzer analyzer = new EnglishAnalyzer();
			Similarity similarity = new BM25Similarity();

			List<TopicDto> topics = app.loadTopics(queryFile);
			Map<String, Query> queries = app.createQuery(queryExpander, analyzer, topics);

			// If the directory exists then do not recreate indexes
			if (!new File(indexDirPath).exists()) {

				dir = FSDirectory.open(Paths.get(indexDirPath));

				// create the directory
				new File(indexDirPath).mkdirs();

				IndexDocuments idx = new IndexDocuments(analyzer, similarity, indexDirPath, dataSourcePath);
				idx.indexFbisDocs();
				idx.indexFr94Docs();
				idx.indexFtDocs();
				idx.indexLatTimeDocs();
				logger.info("Finished indexing all docs.");
			} else {
				dir = FSDirectory.open(Paths.get(indexDirPath));
			}

			List<Result> results = new ArrayList<>();

			for (Map.Entry<String, Query> entry : queries.entrySet()) {
				String[][] hits = app.fireQuery(entry.getValue(), dir, similarity, 1000);
				for (int i = 0; i < hits.length; ++i) {
					String docId = hits[i][0];
					String score = hits[i][1];

					Result result = new Result(entry.getKey().trim(), "q0", docId, (i + 1) + "", score + "", "exp");
					results.add(result);
				}
			}

			try {
				new ResultsWriter(results, outputFolder).writeResults();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
	public List<TopicDto> loadTopics(String queryFile) throws Exception {
		XMLQueryParser xmlqp;
		try {
			xmlqp = new XMLQueryParser(queryFile);
			List<TopicDto> topics = xmlqp.getQueryTopics();

			for (TopicDto topic : topics) {
				String num = topic.getNum().split(": ")[1];
				topic.setNum(num);
			}

			return topics;
		} catch (SAXException | ParserConfigurationException e) {
			logger.error("Error thrown when loading topics.", e);
			throw e;
		}
	}

	public Map<String, Query> createQuery(QueryExpansion queryExpander, Analyzer analyzer, List<TopicDto> topics) {
		HashMap<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("title", 1f);
		boosts.put("content",10f);

		Map<String, Query> queries = new HashMap();

		for (TopicDto topic : topics) {
			 MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] {"title", "content" }, analyzer, boosts);
//			MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "content" }, analyzer);
			Query qT;
			Query qD;
			Query q;
			try {
				qT = multiFieldQP.parse(topic.getTitle());
				qD = multiFieldQP.parse(topic.getDesc());

//				String query = "";
//				query = queryExpander.expandQuery(topic.getDesc());
//				query += topic.getTitle();
//				q = multiFieldQP.parse(query);

				org.apache.lucene.search.BooleanQuery.Builder bq = new BooleanQuery.Builder();
				bq.add(qT, Occur.SHOULD);
				bq.add(qD, Occur.SHOULD);

//				queries.put(topic.getNum(), q);
				queries.put(topic.getNum(), qT);
			} catch (ParseException e) {
				logger.error("Error parsing query.");
			}
		}

		return queries;
	}

	public String[][] fireQuery(Query query, Directory dir, Similarity similarity, int hitsPerPage) {

		try (IndexReader reader = DirectoryReader.open(dir)) {
			IndexSearcher searcher = new IndexSearcher(reader);

			// Scoring - Similarity
			searcher.setSimilarity(similarity);

			// Expand Query
			query = expandQuery(searcher, reader, query);

			TopDocs docs = searcher.search(query, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			String[][] res = new String[hits.length][2];

			// display results
			boolean printResults = true;

			if (printResults) {
				System.out.println("Found " + hits.length + " hits.");
				System.err.print("\nRES_NO | DOCID | TITLE | SCORE\n");
			}

			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				double score = hits[i].score;
				Document d = searcher.doc(docId);
				res[i][0] = d.get("docId");
				res[i][1] = score + "";
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
			reader.close();

			return res;
		} catch (IOException e) {
			logger.error("Failed to execute query '" + query + "'", e);
		}

		return null;

	}

	private Query expandQuery(IndexSearcher searcher, IndexReader reader, Query query) throws IOException {

		TopDocs docs = searcher.search(query, 15);
		HashMap<String, ScorePair> map = new HashMap<String, ScorePair>();
		for (int i = 0; i < docs.scoreDocs.length; i++) {
			// Iterate fields for each result

			Fields fieldContainer = reader.getTermVectors(docs.scoreDocs[i].doc);
			Iterator<String> fields = reader.getTermVectors(docs.scoreDocs[i].doc).iterator();

			// Iterate over each field
			while (fields.hasNext()) {
				String fieldName = fields.next();

				Terms terms = fieldContainer.terms(fieldName);

				if (terms != null) {
					TermsEnum tEnum = terms.iterator();

					while (tEnum.next() != null) {

						putTermInMap(fieldName, tEnum.term().utf8ToString(), tEnum.docFreq(), map, reader);
					}
				}
			}

		}

		org.apache.lucene.search.BooleanQuery.Builder bq = new BooleanQuery.Builder();
		query.createWeight(searcher, true, 15f);
		bq.add(query, BooleanClause.Occur.SHOULD);

		List<ScorePair> byScore = new ArrayList<ScorePair>(map.values());
		Collections.sort(byScore);
		
		for (int i = 0; i < 25; i++) {
			// Add all our found terms to the final query
			ScorePair pair = byScore.get(i);
			bq.add(new TermQuery(new Term(pair.getField(), pair.getTerm())), BooleanClause.Occur.SHOULD);
		}

		return bq.build();
	}

	void putTermInMap(String field, String term, int freq, Map<String, ScorePair> map, IndexReader reader) {
		String key = field + ":" + term;
		if (map.containsKey(key))
			map.get(key).increment();
		else
			map.put(key, new ScorePair(reader, freq, field, term));
	}

}
