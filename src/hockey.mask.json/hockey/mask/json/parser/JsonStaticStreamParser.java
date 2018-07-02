package hockey.mask.json.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import hockey.mask.json.JsonStandardException;

/**
 * The JsonStaticStreamParser class facilitates the processing of a a character stream input into a JSON value.
 * This parser only reads the input once at creation and works on this fixed character sequence, so it 
 * does not utilise any dynamic properties of streams, but is much safer to use. 
 * 
 * @author Planters
 *
 */
public class JsonStaticStreamParser extends JsonParser implements Cloneable, AutoCloseable {
	
	private BufferedReader readerData = null;
	private String jsonData = null; // the string to parse
	private int pos = 0; // the current position of the parser
	
	/**
	 * Use an input stream for parsing JSON data.<br>
	 * The input stream must support marking as this is required for some of the 
	 * functionality of the parser.<br>
	 * Also the stream is not closed at any point. This needs to be performed separately.
	 * 
	 * @param in - the input stream to use for parsing
	 * @throws NullPointerException if the specified stream is null
	 * @throws JsonParserInternalException if the specified stream does not support marking or marking the 
	 * initial position failed
	 * @throws JsonStandardException if the string retrieved from the stream is empty
	 */
	public JsonStaticStreamParser(InputStream in) throws JsonParserInternalException, JsonStandardException {
		if (in != null) {
				this.readerData = new BufferedReader(new InputStreamReader(in));
				this.setData();
		} else {
			throw new NullPointerException("The input stream for the JSON parser may not be null.");
		}
	}
	
	/**
	 * Use an input stream for parsing JSON data with the specified charset. If the charset is 
	 * null the system default will be used.<br>
	 * The input stream must support marking as this is required for some of the 
	 * functionality of the parser.<br>
	 * Also the stream is not closed at any point. This needs to be performed separately.
	 * 
	 * @param in - the input stream to use for parsing
	 * @param charset - the charset to use for decoding
	 * @throws NullPointerException if the specified stream or charset is null
	 * @throws JsonParserInternalException if the specified stream does not support marking or marking the 
	 * initial position failed
	 * @throws JsonStandardException if the string retrieved from the stream is empty
	 */
	public JsonStaticStreamParser(InputStream in, Charset charset) throws JsonParserInternalException, JsonStandardException {
		if (in != null) {
			if (charset != null) {
				this.readerData = new BufferedReader(new InputStreamReader(in, charset));
			} else {
				this.readerData = new BufferedReader(new InputStreamReader(in));
			}
			this.setData();
		} else {
			throw new NullPointerException("The input stream for the JSON parser may not be null.");
		}
	}
	
	/**
	 * Set the data as string based on the content of the reader.
	 * 
	 * @throws JsonParserInternalException if an I/O error occurs
	 * @throws JsonStandardException if the string retrieved from the reader is empty
	 */
	private void setData() throws JsonParserInternalException, JsonStandardException {
		try {
			StringBuilder sb = new StringBuilder();
			int next = -1;
			while ((next = this.readerData.read()) >= 0) {
				sb.append((char) next);
			}
			String streamContent = sb.toString();
			if (streamContent.length() > 0) {
				this.jsonData = sb.toString();
			} else {
				throw new JsonStandardException("The empty string \"" + streamContent
						+ "\" cannot be parsed.");
			}
		} catch (IOException e) {
			throw new JsonParserInternalException(String.format("Reading from the buffered reader %s failed.", 
					this.readerData), e);
		}
	}
	
	public JsonStringParser toStringParser() {
		try {
			JsonStringParser equivalentStringParser = new JsonStringParser(this.getData());
			equivalentStringParser.setPosition(this.getPosition());
			return equivalentStringParser;
		} catch (JsonStandardException e) {
			/*
			 * This should not be possible as the only way a JsonStringParser will throw a 
			 * JsonStandardException is if the string has a length of zero, which would also 
			 * cause the JsonStaticStreamParser to throw the same exception.
			 * If it happens non the less, that means there is something utterly wrong in 
			 * the code.
			 */
			throw new JsonParserInternalException("Every static JSON stream parser should be convertable to "
					+ "a JSON string parser.",e);
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
	 * @param length - the length of the substring to return
	 * @return a substring of the specified length
	 * @throws IndexOutOfBoundsException if the end of the substring is outside of the bounds 
	 * of the parsed string
	 */
	@Override
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
	@Override
	public String get() throws IndexOutOfBoundsException {
		return this.get(1);
	}
	
	/**
	 * Get the data held by this parser.
	 * 
	 * @return the string to be parsed
	 */
	@Override
	public String getData() {
		return this.jsonData;
	}
	
	/**
	 * Checks if there are still characters remaining to parse.
	 * 
	 * @return true if there are remaining characters
	 */
	@Override
	public boolean hasNext() {
		return this.getPosition() < this.getData().length();
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
		return this.hasNext() && Character.isDigit(this.getData().charAt(this.getPosition()));
	}
	
	/**
	 * Get the remaining characters of the parsed string as substring.
	 * 
	 * @return a substring from the parsers position mark to the end of the parsed string
	 */
	@Override
	public String getRemaining() {
		return this.getData().substring(this.getPosition(), this.getData().length());
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
		while (this.hasNext() && Character.isWhitespace(this.getData().charAt(this.getPosition()))) {
			this.setPosition(this.getPosition() + 1);
		}
	}

	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void close() throws IOException {
		this.readerData.close();
	}

	/*
	 * The functions hashCode(), equals() and toString() of the super class should be 
	 * sufficient.
	 */
	
}
