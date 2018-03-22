package com.ir.searchengine.Analyzer;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.ir.searchengine.filters.SpecialCharFilter;

/**
 * Analyzer developed taking the StandardAnalyzer as base.
 * 
 * @author amit
 */
public class CustomAnalyzer extends StopwordAnalyzerBase {

    /**
     * Builds the Analyzer
     * @param stopwords list of stopwords
     */
    public CustomAnalyzer() {

    }
    
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
    	Tokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new StandardFilter(tokenizer);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, stopwords);
        tokenStream = new KStemFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);
        TokenStreamComponents tokenStreamComponents = new TokenStreamComponents(tokenizer, tokenStream);
        return tokenStreamComponents;
    }

}