package hockey.mask.json.parser;

import hockey.mask.json.JsonStandardException;

/**
 * The JsonParser class facilitates the processing of a plain string input into a JSON value. 
 * It works similar to a stream.
 * 
 * @author Planters
 *
 */
public class JsonParser {
	
	private String jsonData = null; // the string to parse
	private int pos = 0; // the current position of the parser
	
	/**
	 * Create a new JsonParser based on the supplied string. Null cannot be parsed and will 
	 * throw an exception. The same holds true for empty strings.
	 * 
	 * @param jsonData - the data to parse
	 * @throws JsonStandardException if an empty string is passed
	 * @throws NullPointerException if null is passed
	 */
	public JsonParser(String jsonData) throws JsonStandardException {
		// ensure the data is valid
		if (jsonData == null) {
			throw new NullPointerException("The null string \"" + jsonData
					+ "\" cannot be parsed.");
		} else if (jsonData.length() <= 0) {
			throw new JsonStandardException("The empty string \"" + jsonData
					+ "\" cannot be parsed.");
		} else {
			this.jsonData = jsonData;
		}
	}
	
	/**
	 * Get the current position in the parsed string.
	 * 
	 * @return the position inside the string
	 */
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
	public void setPosition(int position) throws IndexOutOfBoundsException {
		if (position <= this.getData().length() && position >= 0) {
			this.pos = position;
		} else {
			throw new IndexOutOfBoundsException(String.format("The index %s is outside of the "
					+ "bounds of a string of length %s.", position, jsonData.length()));
		}
	}
	
	/**
	 * Get the next characters of the string as substring. A substring with the specified length 
	 * will be returned starting with the character at the current parsers position mark and 
	 * increment the position mark by the length of the substring.
	 * 
	 * @param length the length of the substring to return
	 * @return a substring of the specified length
	 * @throws IndexOutOfBoundsException if the end of the substring is outside of the bounds 
	 * of the parsed string
	 */
	public String get(int length) throws IndexOutOfBoundsException {
		int end = this.getPosition() + length;
		if (end <= this.getData().length()) {
			String sub = this.getData().substring(this.getPosition(), end);
			this.setPosition(this.getPosition() + length); // increment the position mark
			return sub;
		} else {
			throw new IndexOutOfBoundsException(String.format("The index %s is outside of the "
					+ "bounds of a string of length %s.", end, jsonData.length()));
		}
	}
	
	/**
	 * Get the next character of the string as substring. A substring containing
	 * the character at the current parsers position mark will be returned and 
	 * the position mark incremented by one.
	 * 
	 * @return the next character as substring 
	 * @throws IndexOutOfBoundsException if the end of the parsed string has been reached
	 */
	public String get() throws IndexOutOfBoundsException {
		return this.get(1);
	}
	
	/**
	 * Get the data held by this parser.
	 * 
	 * @return the string to be parsed
	 */
	public String getData() {
		return this.jsonData;
	}
	
	/**
	 * Checks there are still characters remaining to parse.
	 * 
	 * @return true if there are remaining characters
	 */
	public boolean hasNext() {
		return this.getPosition() < this.getData().length();
	}
	
	/**
	 * Checks whether the next characters are the query.
	 * 
	 * @param query - the string to query for
	 * @return true if the next characters equal the query
	 */
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
	 * Get the remaining characters of the parsed string as substring.
	 * 
	 * @return a substring from the parsers position mark to the end of the parsed string
	 */
	public String getRemaining() {
		return this.getData().substring(this.getPosition(), this.getData().length());
	}
	
	/**
	 * Reset the position mark to zero.
	 */
	public void rewind() {
		this.setPosition(0);
	}
	
	/**
	 * Moves the position mark to the next non-whitespace character in the parser. If the 
	 * parser only contains whitespace characters, the position mark is moved to the end 
	 * position, at which no character resides.
	 */
	public void skipWhitespace() {
		while (this.hasNext() && Character.isWhitespace(this.getData().charAt(this.getPosition()))) {
			this.setPosition(this.getPosition() + 1);
		}
	}
	
	@Override
	public String toString() {
		return String.format("[Position %s : \"%s\"]", this.getPosition(), this.getData());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jsonData == null) ? 0 : jsonData.hashCode());
		result = prime * result + pos;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonParser other = (JsonParser) obj;
		if (jsonData == null) {
			if (other.jsonData != null)
				return false;
		} else if (!jsonData.equals(other.jsonData))
			return false;
		if (pos != other.pos)
			return false;
		return true;
	}
	

}
