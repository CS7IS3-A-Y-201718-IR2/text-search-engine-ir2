package com.ir.searchengine.filters;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TempFilter extends TokenFilter {
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	protected TempFilter(TokenStream input) {
		super(input);
	}


	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
