package com.ir.searchengine.query;

public class TopicDto {

	String num;

	String title;

	String desc;

	public TopicDto(String num, String title, String desc) {
		super();
		this.num = num;
		this.title = title;
		this.desc = desc;
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
}
