package com.ir.searchengine.query;

import org.apache.lucene.index.IndexReader;

public class ScorePair implements Comparable {
	int count = 0;
	double idf;
	String field;
	String term;

	public ScorePair(IndexReader reader, int docfreq, String field, String term) {
		count++;
		// Standard Lucene idf calculation. This is calculated once per field:term
		idf = Math.pow((1 + Math.log(reader.numDocs() / ((double) docfreq + 1))) , 2);
		this.field = field;
		this.term = term;
	}

	public void increment() {
		count++;
	}

	double score() {
		return Math.sqrt(count) * idf;
	}

	// Standard Lucene TF/IDF calculation
	@Override
	public int compareTo(Object o) {
		ScorePair pair = (ScorePair) o;
		if (this.score() > pair.score())
			return -1;
		else if (this.score() < pair.score())
			return 1;
		else
			return 0;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
}
