package com.ir.searchengine;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ir.searchengine.query.XMLQueryParser;

public class Application {

	public static void main(String args[]) {
		
		XMLQueryParser xmlqp;
		try {
			xmlqp = new XMLQueryParser();
			xmlqp.getQueryTopics();
		} catch (SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		} 
		
	}
}
