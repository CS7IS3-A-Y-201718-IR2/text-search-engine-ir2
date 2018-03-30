package com.ir.searchengine.query;

import org.apache.lucene.util.Attribute;

public interface TermAttribute extends Attribute { 
	  /** Returns the Token's term text.
	   *  
	   * This method has a performance penalty 
	   * because the text is stored internally in a char[].  If 
	   * possible, use {@link #termBuffer()} and {@link 
	   * #termLength()} directly instead.  If you really need a 
	   * String, use this method, which is nothing more than 
	   * a convenience call to <b>new String(token.termBuffer(), 0, token.termLength())</b> 
	   */ 
	  public String term(); 
	   
	  /** Copies the contents of buffer, starting at offset for
	   *  length characters, into the termBuffer array. 
	   *  @param buffer the buffer to copy 
	   *  @param offset the index in the buffer of the first character to copy 
	   *  @param length the number of characters to copy 
	   */ 
	  public void setTermBuffer(char[] buffer, int offset, int length); 
	 
	  /** Copies the contents of buffer into the termBuffer array.
	   *  @param buffer the buffer to copy 
	   */ 
	  public void setTermBuffer(String buffer); 
	 
	  /** Copies the contents of buffer, starting at offset and continuing
	   *  for length characters, into the termBuffer array. 
	   *  @param buffer the buffer to copy 
	   *  @param offset the index in the buffer of the first character to copy 
	   *  @param length the number of characters to copy 
	   */ 
	  public void setTermBuffer(String buffer, int offset, int length); 
	   
	  /** Returns the internal termBuffer character array which
	   *  you can then directly alter.  If the array is too 
	   *  small for your token, use {@link 
	   *  #resizeTermBuffer(int)} to increase it.  After 
	   *  altering the buffer be sure to call {@link 
	   *  #setTermLength} to record the number of valid 
	   *  characters that were placed into the termBuffer. */ 
	  public char[] termBuffer(); 
	 
	  /** Grows the termBuffer to at least size newSize, preserving the
	   *  existing content. Note: If the next operation is to change 
	   *  the contents of the term buffer use 
	   *  {@link #setTermBuffer(char[], int, int)}, 
	   *  {@link #setTermBuffer(String)}, or 
	   *  {@link #setTermBuffer(String, int, int)} 
	   *  to optimally combine the resize with the setting of the termBuffer. 
	   *  @param newSize minimum size of the new termBuffer 
	   *  @return newly created termBuffer with length >= newSize 
	   */ 
	  public char[] resizeTermBuffer(int newSize); 
	 
	  /** Return number of valid characters (length of the term)   *  in the termBuffer array. */ 
	  public int termLength(); 
	   
	  /** Set number of valid characters (length of the term) in
	   *  the termBuffer array. Use this to truncate the termBuffer 
	   *  or to synchronize with external manipulation of the termBuffer. 
	   *  Note: to grow the size of the array, 
	   *  use {@link #resizeTermBuffer(int)} first. 
	   *  @param length the truncated length 
	   */ 
	  public void setTermLength(int length); 
	}