package com.ir.searchengine.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Filter class that removes all the special characters.
 * @author amit
 */
public class SpecialCharFilter extends FilteringTokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	public static final CharArraySet SPECIAL_CHARS;

	static {
		final List<String> specialChars = Arrays.asList("?", ",", ":");
		
		final CharArraySet specialCharsSet = new CharArraySet(specialChars, false);
		SPECIAL_CHARS = CharArraySet.unmodifiableSet(specialCharsSet); 
	}

	public SpecialCharFilter(TokenStream in) {
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
