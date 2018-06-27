package hockey.mask.json.parser;

/**
 * The JsonParser class facilitates the processing of an input into a JSON value. 
 * 
 * @author Planters
 *
 */
public abstract class JsonParser {
	
	/**
	 * Get the current position in the parsed data.
	 * 
	 * @return the position inside the data
	 */
	public abstract int getPosition();
	
	/**
	 * Set the position mark of the parser to the specified value.
	 * 
	 * @param position - the position to set the parser to
	 * @throws IndexOutOfBoundsException if the passed value is outside of the bounds of the 
	 * parsed data
	 */
	public abstract void setPosition(int position) throws IndexOutOfBoundsException;
	
	/**
	 * Get the next characters of the data as string. A string with the specified length 
	 * will be returned starting with the character at the current parsers position mark and 
	 * increment the position mark by the length of the string.
	 * 
	 * @param length the length of the string to return
	 * @return a string of the specified length
	 * @throws IndexOutOfBoundsException if the end of the string is outside of the bounds 
	 * of the parsed data
	 */
	public abstract String get(int length) throws IndexOutOfBoundsException;
	
	/**
	 * Get the next character of the data as string. A string containing
	 * the character at the current parsers position mark will be returned and 
	 * the position mark incremented by one.
	 * 
	 * @return the next character as string 
	 * @throws IndexOutOfBoundsException if the end of the parsed data has been reached
	 */
	public String get() throws IndexOutOfBoundsException {
		return this.get(1);
	}
	
	/**
	 * Get the data held by this parser.
	 * 
	 * @return the data to be parsed
	 */
	public abstract String getData();
	
	/**
	 * Checks if there are still characters remaining to parse.
	 * 
	 * @return true if there are remaining characters
	 */
	public abstract boolean hasNext();
	
	/**
	 * Checks whether the next characters are the query.
	 * The position mark will not be incremented.
	 * 
	 * @param query - the string to query for
	 * @return true if the next characters equal the query
	 */
	public abstract boolean isNext(String query);
	
	/**
	 * Checks whether the next characters are the query.
	 * Optionally the position mark can be incremented if the query is found.
	 * 
	 * @param query - the string to query for
	 * @param incrementPosition - true to increment the position mark by the search if found
	 * @return true if the next characters equal the query
	 */
	public abstract boolean isNext(String query, boolean incrementPosition);	
	
	/**
	 * Checks whether the next character is a digit.
	 * This will not increment the position mark.
	 * 
	 * @return true if the next character is a digit
	 */
	public abstract boolean isNextDigit();
	
	/**
	 * Get the remaining characters of the parsed data as string.
	 * 
	 * @return a string from the parsers position mark to the end of the parsed data
	 */
	public abstract String getRemaining();
	
	/**
	 * Reset the position mark to zero.
	 */
	public abstract void rewind();
	
	/**
	 * Moves the position mark to the next non-whitespace character in the parser. If the 
	 * parser only contains whitespace characters, the position mark is moved to the end 
	 * position, at which no character resides.
	 */
	public abstract void skipWhitespace();

}
