package hockey.mask.json.parser;

import hockey.mask.json.JsonStandardException;

/**
 * The JsonStringParser class facilitates the processing of a plain string input into a JSON value. 
 * It works similar to a stream.
 * 
 * @author Planters
 *
 */
public class JsonStringParser extends JsonParser {
	
	private char[] jsonData = null; // the string to parse
	private int pos = 0; // the current position of the parser
	
	/**
	 * Create a new JsonParser based on the supplied string. Null cannot be parsed and will 
	 * throw an exception. The same holds true for empty strings.
	 * 
	 * @param jsonData - the data to parse
	 * @throws JsonStandardException if an empty string is passed
	 * @throws NullPointerException if null is passed
	 */
	public JsonStringParser(String jsonData) throws JsonStandardException {
		// ensure the data is valid
		if (jsonData == null) {
			throw new NullPointerException("The null string \"" + jsonData
					+ "\" cannot be parsed.");
		} else if (jsonData.length() <= 0) {
			throw new JsonStandardException("The empty string \"" + jsonData
					+ "\" cannot be parsed.");
		} else {
			this.jsonData = jsonData.toCharArray();
		}
	}
	
	/**
	 * Get the current position in the parsed string.
	 * 
	 * @return the position inside the string
	 */
	@Override
	public int getPosition() {
		return this.pos;
	}
	
	/**
	 * Set the position mark of the parser to the specified value.
	 * 
	 * @param position - the position to set the parser to
	 * @throws IndexOutOfBoundsException if the passed value is outside of the bounds of the 
	 * parsed string
	 */
	@Override
	public void setPosition(int position) throws IndexOutOfBoundsException {
		if (position <= this.jsonData.length && position >= 0) {
			this.pos = position;
		} else {
			throw new IndexOutOfBoundsException(String.format("The index %s is outside of the "
					+ "bounds of a string of length %s.", position, this.jsonData.length));
		}
	}
	
	/**
	 * Get the next characters of the string as substring. A substring with the specified length 
	 * will be returned starting with the character at the current parsers position mark and 
	 * increment the position mark by the length of the substring.
	 * 
	 * @param length - the length of the substring to return
	 * @return a substring of the specified length
	 * @throws IndexOutOfBoundsException if the end of the substring is outside of the bounds 
	 * of the parsed string
	 */
	@Override
	public String get(int length) throws IndexOutOfBoundsException {
		if (this.getPosition() + length <= this.getData().length()) {
			String sub = new String(this.jsonData, this.getPosition(), length);
			this.setPosition(this.getPosition() + length); // increment the position mark
			return sub;
		} else {
			throw new IndexOutOfBoundsException(String.format("The index %s is outside of the "
					+ "bounds of a string of length %s.", this.getPosition() + length, this.jsonData.length));
		}
	}
	
	/**
	 * Get the next character of the string.
	 * The position mark will be incremented by one.
	 * 
	 * @return the next character 
	 * @throws IndexOutOfBoundsException if the end of the parsed string has been reached
	 */
	@Override
	public char get() throws IndexOutOfBoundsException {
		if (this.hasNext()) {
			char c = this.jsonData[this.getPosition()];
			this.setPosition(this.getPosition() + 1);
			return c;
		} else {
			throw new IndexOutOfBoundsException(String.format("The index %s is outside of the "
					+ "bounds of a string of length %s.", this.getPosition() + 1, this.jsonData.length));
		}
	}
	
	/**
	 * Get the data held by this parser.
	 * 
	 * @return the string to be parsed
	 */
	@Override
	public String getData() {
		return new String(this.jsonData);
	}
	
	/**
	 * Checks if there are still characters remaining to parse.
	 * 
	 * @return true if there are remaining characters
	 */
	@Override
	public boolean hasNext() {
		return this.getPosition() < this.jsonData.length;
	}
	
	/**
	 * Checks whether the next character is the query.
	 * The position mark will not be incremented.
	 * 
	 * @param query - the character to query for
	 * @return true if the next character equals the query
	 */
	@Override
	public boolean isNext(char query) {
		return this.hasNext() && this.jsonData[this.getPosition()] == query;
	}

	/**
	 * Checks whether the next character is the query.
	 * Optionally the position mark can be incremented if the query is found.
	 * 
	 * @param query - the character to query for
	 * @param incrementPosition - true to increment the position mark by the search if found
	 * @return true if the next character equals the query
	 */
	@Override
	public boolean isNext(char query, boolean incrementPosition) {
		boolean next = this.isNext(query);
		if (next && incrementPosition) {
			this.setPosition(this.getPosition() + 1);
		}
		return next;
	}
	
	/**
	 * Checks whether the next characters are the query.
	 * The position mark will not be incremented.
	 * 
	 * @param query - the string to query for
	 * @return true if the next characters equal the query
	 */
	@Override
	public boolean isNext(String query) {
		if (query != null) {
			int end = this.getPosition() + query.length();
			if (end <= this.getData().length()) {
				return this.getData().substring(this.getPosition(), end).equals(query);
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the next characters are the query.
	 * Optionally the position mark can be incremented if the query is found.
	 * 
	 * @param query - the string to query for
	 * @param incrementPosition - true to increment the position mark by the search if found
	 * @return true if the next characters equal the query
	 */
	@Override
	public boolean isNext(String query, boolean incrementPosition) {
		if (query != null) {
			int end = this.getPosition() + query.length();
			if (end <= this.getData().length()) {
				if (this.getData().substring(this.getPosition(), end).equals(query)) {
					if (incrementPosition) {
						this.setPosition(this.getPosition() + query.length());
					}
					return true;
				}
			}
		}
		return false;
	}	
	
	/**
	 * Checks whether the next character is a digit.
	 * This will not increment the position mark.
	 * 
	 * @return true if the next character is a digit
	 */
	@Override
	public boolean isNextDigit() {
		return this.hasNext() && Character.isDigit(this.jsonData[this.getPosition()]);
	}
	
	/**
	 * Get the remaining characters of the parsed string as substring.
	 * 
	 * @return a substring from the parsers position mark to the end of the parsed string
	 */
	@Override
	public String getRemaining() {
		return new String(this.jsonData, this.getPosition(), this.jsonData.length - this.getPosition());
	}
	
	/**
	 * Reset the position mark to zero.
	 */
	@Override
	public void rewind() {
		this.setPosition(0);
	}
	
	/**
	 * Moves the position mark to the next non-whitespace character in the parser. If the 
	 * parser only contains whitespace characters, the position mark is moved to the end 
	 * position, at which no character resides.
	 */
	@Override
	public void skipWhitespace() {
		while (this.hasNext() && Character.isWhitespace(this.jsonData[this.getPosition()])) {
			this.setPosition(this.getPosition() + 1);
		}
	}

	/*
	 * The functions hashCode(), equals() and toString() of the super class should be 
	 * sufficient.
	 */
	
}
