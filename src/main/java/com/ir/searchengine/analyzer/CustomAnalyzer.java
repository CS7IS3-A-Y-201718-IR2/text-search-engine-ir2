package com.ir.searchengine.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
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
import org.apache.lucene.wordnet.SynonymMap;
import org.apache.lucene.wordnet.SynonymTokenFilter;

import com.ir.searchengine.constants.Constants;
import com.ir.searchengine.filters.SpecialCharFilter;
import com.ir.searchengine.filters.UnwantedWordsFilter;

/**
 * Analyzer developed taking the StandardAnalyzer as base.
 * 
 * @author amit
 */
public class CustomAnalyzer extends Analyzer {

	Logger logger = Logger.getLogger(CustomAnalyzer.class);
	
	public SynonymMap synonymMap; 
	boolean mapFound = false;
	
	public CustomAnalyzer() {
			File mapFile = new File(Constants.SYNONYM_FILE_PATH);
			try {
				synonymMap = new SynonymMap(new FileInputStream(mapFile));
				mapFound = true;
			} catch (IOException e) {
				logger.error(e);
			}
	}
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		final StandardTokenizer src = new StandardTokenizer();
		TokenStream tok = new StandardFilter(src);
		TokenFilter filter = new SpecialCharFilter(tok);
		filter = new UnwantedWordsFilter(filter);
		//filter = new EnglishPossessiveFilter(filter);
		filter = new LowerCaseFilter(filter);
		
		if(mapFound) 
			filter = new SynonymTokenFilter(filter, synonymMap, 10);
		
		filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
		filter = new PorterStemFilter(filter);
		return new TokenStreamComponents(src, filter);
	}

}