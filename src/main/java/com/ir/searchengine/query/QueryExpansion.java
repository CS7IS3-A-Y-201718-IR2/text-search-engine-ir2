package com.ir.searchengine.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.wordnet.SynonymMap;
import org.tartarus.snowball.ext.PorterStemmer;

import com.ir.searchengine.constants.Constants;
import com.ir.searchengine.utils.MapUtil;
import com.ir.searchengine.utils.WordFrequency;

public class QueryExpansion {

	private boolean isMap = false;
	private SynonymMap synonymMap = null;

	private List<String> stopWords;

	public QueryExpansion() {
//		try {
//			File mapFile = new File(Constants.SYNONYM_FILE_PATH);
//			synonymMap = new SynonymMap(new FileInputStream(mapFile));
//			isMap = true;
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}

	public String expandQuery(String query) {
		 StringBuilder sbQuery = new StringBuilder();
		 for (String qWord : query.split(" ")) {
			 qWord = removeSpecialChars(qWord);
			 qWord = removeStopWords(qWord);
			 
			 sbQuery.append(qWord);
			 sbQuery.append(" ");
			// sbQuery.append(getSynonyms(synonymMap, qWord));
			// sbQuery.append(" ");
			//
		 }
		 
		 // rank words according to frequency and only consider top k
		query = sbQuery.toString();
		WordFrequency wf = new WordFrequency();
		Map<String, Integer> queryMap = wf.getFrequency(query);
		queryMap = MapUtil.sortByValue(queryMap);
		
		query = generateQueryUsingTopKWord(queryMap, 4);
		
		return query;
	}

	private String generateQueryUsingTopKWord(Map<String, Integer> queryMap, int k) {
		String query = "";
		for (Map.Entry<String, Integer> entry : queryMap.entrySet())
		{
			if ( k >= 0) {
				query += entry.getKey() + " ";
				k--;
			}
		}
		return query;
	}

	private String getSynonyms(SynonymMap synonymMap, String word) {
		StringBuilder sb = new StringBuilder();
		word = word.trim();
		word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		word = stemTerm(word);
		if (!word.equals("")) {

			if (isMap) {
				int syncount = 0;
				for (String syn : synonymMap.getSynonyms(word)) {
					if (syncount < 3) {
						sb.append(syn);
						sb.append(" ");
						syncount++;
					}
				}
				return sb.toString();

			}
		}
		return "";

	}

	private String stemTerm(String term) {
		PorterStemmer stemmer = new PorterStemmer();
		stemmer.setCurrent(term);
		stemmer.stem();
		String stemmedWord = stemmer.getCurrent();
		return stemmedWord;
	}

	
	private String removeStopWords(String word) {
		List<String> stopWords = getListOfStopWords();

		if (containsCaseInsensitive(word, stopWords)) {
			return "";
		}
		
		return word;

	}

	/**
	 * Helper function to return a list of stop words.
	 * 
	 * @return
	 */
	private List<String> getListOfStopWords() {

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
		return stopWords;
	}

	private String removeSpecialChars(String word) {
		word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		return word;
	}

	public boolean containsCaseInsensitive(String s, List<String> l) {
		for (String string : l) {
			if (string.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
}
