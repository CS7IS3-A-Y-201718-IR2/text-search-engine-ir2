package com.ir.searchengine.query;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TopicHandler extends DefaultHandler {

	String num;

	String title;

	String desc;

	boolean isNum = false;
	boolean isTitle = false;
	boolean isDesc = false;
	
	List<TopicDto> topics;

	public TopicHandler() {
		topics = new ArrayList<TopicDto>();
	}
	
	@Override
	public void startElement(String uri, String localName, String tName, Attributes attributes) throws SAXException {

		System.out.println("Start Element :" + tName);

		if (tName.equalsIgnoreCase("num")) {
			isNum = true;
		}

		if (tName.equalsIgnoreCase("title")) {
			isTitle = true;
		}

		if (tName.equalsIgnoreCase("desc")) {
			isDesc = true;
		}

		// Ignoring the narrative
	}

	
	@Override
	public void endElement(String uri, String localName, String tName) throws SAXException {

		System.out.println("End Element :" + tName);

	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (isNum) {
			num = new String(ch, start, length);
			isNum = false;
		}
		
		if (isTitle) {
			title = new String(ch, start, length);
			isTitle = false;
		}
		
		if (isDesc) {
			desc = new String(ch, start, length);
			isDesc = false;
			
			TopicDto topic = new TopicDto(num, title, desc);
			topics.add(topic);
		}

	}
	
	public List<TopicDto> getTopics() {
		return topics;
	}
}
