package com.ir.searchengine.query;

public class TopicDto {

	String num;

	String title;

	String desc;
	
	String narrative;

	public TopicDto(String num, String title, String desc, String narrative) {
		super();
		this.num = num;
		this.title = title;
		this.desc = desc;
		this.narrative = narrative;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
}
