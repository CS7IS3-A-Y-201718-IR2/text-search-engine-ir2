package com.ir.searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.xml.sax.SAXException;

import com.ir.searchengine.query.TopicDto;
import com.ir.searchengine.query.XMLQueryParser;

public class Application {

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String args[]) {

		Application app = new Application();

		try {
			List<TopicDto> topics = app.loadTopics();

		} catch (Exception e) {
			logger.error(e.getMessage());
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
		boosts.put("title", 5f);
		boosts.put("author", 1f);
		boosts.put("content", 10f);

		List<Query> queries = new ArrayList<>();

		for (TopicDto topic : topics) {
			MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(
					new String[] { "title", "author", "content" }, analyzer, boosts);
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
}
