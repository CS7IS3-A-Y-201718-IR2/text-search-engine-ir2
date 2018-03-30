package com.ir.searchengine.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class UnwantedWordsFilter extends FilteringTokenFilter {
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	public static final CharArraySet SPECIAL_CHARS;

	static {
		final List<String> unwantedWords = Arrays.asList("Description:", "Narrative:");
		
		final CharArraySet specialCharsSet = new CharArraySet(unwantedWords, false);
		SPECIAL_CHARS = CharArraySet.unmodifiableSet(specialCharsSet); 
	}

	public UnwantedWordsFilter(TokenStream in) {
		super(in);
	}

	/* 
	 * Return true if the term is not a special character.
	 * (non-Javadoc)
	 * @see org.apache.lucene.analysis.FilteringTokenFilter#accept()
	 */
	@Override
	protected boolean accept() throws IOException {
		return !SPECIAL_CHARS.contains(termAtt.buffer(), 0, termAtt.length());
	}

}
