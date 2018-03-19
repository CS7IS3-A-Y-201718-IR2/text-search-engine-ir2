package com.ir.searchengine;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.ir.searchengine.query.TopicDto;
import com.ir.searchengine.query.XMLQueryParser;

public class Application {

	private static final Logger logger = Logger.getLogger(Application.class);
	
	public static void main(String args[]) {

		Application app = new Application();
		
		try {
			List<TopicDto> topics = app.loadTopics();
			
			
		} catch(Exception e) {
			logger.error(e.getMessage());
			System.exit(-1);
		}
	}

	/**
	 * Helper method that loads all the topics from the file. 
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
}
