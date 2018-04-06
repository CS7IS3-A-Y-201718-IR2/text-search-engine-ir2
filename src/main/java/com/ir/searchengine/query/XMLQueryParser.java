package com.ir.searchengine.query;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.ir.searchengine.constants.Constants;

public class XMLQueryParser {

	private static final Logger logger = Logger.getLogger(XMLQueryParser.class);

	private TopicHandler th;
	private String QUERY_FILE_PATH;

	public XMLQueryParser(String QUERY_FILE_PATH) throws ParserConfigurationException, SAXException {
		this.QUERY_FILE_PATH = QUERY_FILE_PATH;
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		th = new TopicHandler();
		try {
			logger.info("Parsing topics...");
			saxParser.parse(QUERY_FILE_PATH, th);
			logger.info("Topics parsed successfully.");
		} catch (IOException e) {
			logger.error("Unable to parse topics.", e);
		}
	}

	public List<TopicDto> getQueryTopics() {
		return th.getTopics();
	}
}
