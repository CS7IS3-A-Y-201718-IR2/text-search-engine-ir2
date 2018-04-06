package com.ir.searchengine.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.wordnet.SynonymMap;
import org.apache.lucene.wordnet.SynonymTokenFilter;
import org.tartarus.snowball.ext.PorterStemmer;

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
	
	private List<String> stopWords;
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
		//TokenFilter filter = new SpecialCharFilter(tok);
		//filter = new UnwantedWordsFilter(filter);
		TokenFilter filter = new EnglishPossessiveFilter(tok);
		filter = new LowerCaseFilter(filter);
		//filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
		filter = new StopFilter(filter, getListOfStopWords());
		filter = new PorterStemFilter(filter);
//		filter = new EnglishMinimalStemFilter(filter);
		
		if(mapFound) 
			filter = new SynonymTokenFilter(filter, synonymMap, 3);
		
		return new TokenStreamComponents(src, filter);
	}
	
	
	private CharArraySet getListOfStopWords() {

		if (stopWords == null) {
			stopWords = new ArrayList<>();
			try {
				File file = new File(Constants.STOP_WORDS_FILE_PATH);
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = br.readLine()) != null) {
						stopWords.add(line.trim());
					}
				}
			} catch (IOException e) {
				stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
						"into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then",
						"there", "these", "they", "this", "to", "was", "will", "with");
				e.printStackTrace();
			}
		}
		
		return new CharArraySet(stopWords, true);
	}
}