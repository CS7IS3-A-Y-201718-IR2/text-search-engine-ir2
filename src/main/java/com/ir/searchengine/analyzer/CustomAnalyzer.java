package com.ir.searchengine.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.ir.searchengine.filters.SpecialCharFilter;
import com.ir.searchengine.filters.UnwantedWordsFilter;

/**
 * Analyzer developed taking the StandardAnalyzer as base.
 * 
 * @author amit
 */
public class CustomAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		final StandardTokenizer src = new StandardTokenizer();
		TokenStream tok = new StandardFilter(src);
		TokenFilter filter = new SpecialCharFilter(tok);
		filter = new UnwantedWordsFilter(filter);
		filter = new EnglishPossessiveFilter(filter);
		filter = new LowerCaseFilter(filter);
		filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
		filter = new PorterStemFilter(filter);
		return new TokenStreamComponents(src, filter);
	}

}