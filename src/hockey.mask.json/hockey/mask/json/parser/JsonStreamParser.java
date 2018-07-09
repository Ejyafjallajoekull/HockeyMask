package hockey.mask.json.parser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * The JsonStreamParser class facilitates the processing of a a character stream input into a JSON value.
 * As this class works solely on a BufferedReader the usage of JsonStaticStreamParser is advised if a 
 * snapshot of the stream at the time of parser creation is sufficient as it is much safer. 
 * 
 * @author Planters
 *
 */
public class JsonStreamParser extends JsonParser implements Closeable, AutoCloseable {

	private BufferedReader data = null;
	private int maxPosition = 200;
	private int currentPosition = 0;
	
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
	 */
	public JsonStreamParser(InputStream in) throws JsonParserInternalException {
		if (in != null) {
			if (in.markSupported()) {
				this.data = new BufferedReader(new InputStreamReader(in));
				// mark the initial position
				try {
					this.data.mark(this.maxPosition);
				} catch (IOException e) {
					throw new JsonParserInternalException(String.format("Marking the first character "
							+ "of reader %s failed.", this.data), e);
				}
			} else {
				throw new JsonParserInternalException("The InputStream must support marking.");
			}
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
	 */
	public JsonStreamParser(InputStream in, Charset charset) throws JsonParserInternalException {
		if (in != null) {
			if (in.markSupported()) {
				if (charset != null) {
					this.data = new BufferedReader(new InputStreamReader(in, charset));
				} else {
					this.data = new BufferedReader(new InputStreamReader(in));
				}
				// mark the initial position
				try {
					this.data.mark(this.maxPosition);
				} catch (IOException e) {
					throw new JsonParserInternalException(String.format("Marking the first character "
							+ "of reader %s failed.", this.data), e);
				}
			} else {
				throw new JsonParserInternalException("The InputStream must support marking.");
			}
		} else {
			throw new NullPointerException("The input stream for the JSON parser may not be null.");
		}
	}
	
	/**
	 * Use an input stream reader for parsing JSON data.<br>
	 * The input stream reader must support marking as this is required for some of the 
	 * functionality of the parser.<br>
	 * Also the stream reader is not closed at any point. This needs to be performed separately.
	 * 
	 * @param in - the input stream reader to use for parsing
	 * @throws NullPointerException if the specified stream reader is null
	 * @throws JsonParserInternalException if the specified stream reader does not support marking or marking the 
	 * initial position failed
	 */
//	public JsonStreamParser(InputStreamReader in) throws JsonParserInternalException {
//		if (in != null) {
//			if (in.markSupported()) {
//				this.data = new BufferedReader(in);
//				// mark the initial position
//				try {
//					this.data.mark(this.maxPosition);
//				} catch (IOException e) {
//					throw new JsonParserInternalException(String.format("Marking the first character "
//							+ "of reader %s failed.", this.data), e);
//				}
//			} else {
//				throw new JsonParserInternalException("The InputStream must support marking.");
//			}
//		} else {
//			throw new NullPointerException("The input stream for the JSON parser may not be null.");
//		}
//	}
	
	/**
	 * Use an buffered reader for parsing JSON data.<br>
	 * The buffered reader must support marking as this is required for some of the 
	 * functionality of the parser.<br>
	 * Also the buffered reader is not closed at any point. This needs to be performed separately.
	 * 
	 * @param in - the buffered reader to use for parsing
	 * @throws NullPointerException if the specified buffered reader is null
	 * @throws JsonParserInternalException if the specified buffered reader does not support marking or marking the 
	 * initial position failed
	 */
//	public JsonStreamParser(BufferedReader in) throws JsonParserInternalException {
//		if (in != null) {
//			if (in.markSupported()) {
//				this.data = new BufferedReader(in);
//				// mark the initial position
//				try {
//					this.data.mark(this.maxPosition);
//				} catch (IOException e) {
//					throw new JsonParserInternalException(String.format("Marking the first character "
//							+ "of reader %s failed.", this.data), e);
//				}
//			} else {
//				throw new JsonParserInternalException("The InputStream must support marking.");
//			}
//		} else {
//			throw new NullPointerException("The input stream for the JSON parser may not be null.");
//		}
//	}
	
	/**
	 * Expand the internal buffer for moving the position mark at least to the specified size.
	 * 
	 * @throws JsonParserInternalException if resetting the internal reader failed or if restoring the position 
	 * mark to the previous position failed
	 */
	private void expandMarkBuffer(int minimalSize) throws JsonParserInternalException {
		if (this.maxPosition <= minimalSize) {
			try {
				int pos = this.getPosition(); // reset the position to this point after updating
				this.data.reset();
				this.maxPosition = 2 * minimalSize; // expand the buffer to double the specified size
				this.data.mark(this.maxPosition);
				this.currentPosition = (int) this.data.skip(pos);
				if (pos != this.currentPosition) {
					throw new JsonParserInternalException(String.format("The reader %s has been at position %s before "
							+ "updating the mark, but could only be reset to position %s.", 
							this.data, pos, this.currentPosition));
				}	
			} catch (IOException e) {
				throw new JsonParserInternalException(String.format("An I/O error occurred while resetting "
						+ "the reader %s.", this.data), e);
			}
		}
	}
	
	/**
	 * Reads the next character from the reader and ensures the capacity of the mark for resetting 
	 * is large enough to not be invalidated by any read action.
	 * 
	 * @return the read character
	 * @throws IOException if an I/O error occurred
	 */
	private int checkedRead() throws IOException {
		this.expandMarkBuffer(this.currentPosition + 1);
		return this.data.read();
	}
	
	/**
	 * Get the current position of the position mark of this parser.
	 * 
	 * @return the position of this parser
	 */
	@Override
	public int getPosition() {
		return this.currentPosition;
	}

	/**
	 * Set the position mark of the parser to the specified value.
	 * 
	 * @param position - the position to set the parser to
	 * @throws IndexOutOfBoundsException if the passed position is outside of the bounds of the 
	 * parsed data
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public void setPosition(int position) throws IndexOutOfBoundsException, JsonParserInternalException {
		if (position >= 0) {
			int initialPosition = this.getPosition();
			// expand the buffer if necessary
			if (position >= this.maxPosition) {
				this.expandMarkBuffer(position);
			}
			try {
				this.data.reset();
				this.currentPosition = (int) this.data.skip(position);
				if (this.currentPosition != position) {
					int failPosition = this.currentPosition;
					// reset the reader to the initial position
					this.data.reset();
					this.currentPosition = (int) this.data.skip(initialPosition);
					throw new IndexOutOfBoundsException(String.format("The reader %s should be set to "
							+ "position %s, but could only be set to position %s.", 
							this.data, position, failPosition));
				}
			} catch (IOException e) {
				throw new JsonParserInternalException(String.format("An I/O error occurred while resetting "
						+ "the reader %s.", this.data), e);
			}
		} else {
			throw new IndexOutOfBoundsException(String.format("The reader %s cannot be set to "
					+ "position %s.", 
					this.data, position));
		}
	}

	/**
	 * Get the next characters of the reader as string. A string with the specified length 
	 * will be returned starting with the character at the current parsers position mark and 
	 * increment the position mark by the length of the string.
	 * 
	 * @param length - the number of characters to read from the reader
	 * @return the read characters as string
	 * @throws IndexOutOfBoundsException if the end of the string is outside of the bounds 
	 * of the parsed data
	 * @throws JsonParserInternalException if an I/O error occurred
	 */
	@Override
	public String get(int length) throws IndexOutOfBoundsException, JsonParserInternalException {
		if (length >= 0) {
			int initialPosition = this.getPosition();
			char[] readString = new char[length];
			for (int i = 0; i < readString.length; i++) {
				try {
					int readCharacter = this.checkedRead();
					if (readCharacter >= 0) {
						readString[i] = (char) readCharacter;
						this.currentPosition++;
					} else {
						this.setPosition(initialPosition); // reset the position mark
						throw new IndexOutOfBoundsException("Less than " + length + " characters are "
								+ "available.");
					}
				} catch (IOException e) {
					throw new JsonParserInternalException(String.format("An I/O error occurred while reading the "
							+ "reader %s.", this.data), e);
				}
				
			}
			return new String(readString);
		} else {
			throw new IndexOutOfBoundsException(String.format("The reader %s cannot read %s characters.", 
					this.data, length));
		}
	}
	
	/**
	 * Get the next characters as string. This will not increment the position mark.
	 * 
	 * @param length - the number of characters to retrieve
	 * @return the next characters as string
	 * @throws JsonParserInternalException if an I/O error occurred
	 */
	private String getNextCharacters(int length) throws JsonParserInternalException {
		int initialPosition = this.getPosition();
		try {
			String next = this.get(length);
			this.setPosition(initialPosition);
			return next;
		} catch (IndexOutOfBoundsException e) {
			this.setPosition(initialPosition);
			return null;
		}
	}

	/**
	 * Get the entire data of the underlying reader.
	 * 
	 * @return all characters of the reader as string
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public String getData() throws JsonParserInternalException {
		int initialPosition = this.getPosition();
		StringBuilder sb = new StringBuilder();
		this.rewind(); // reset the reader to the first position
		// read all data
		while (this.hasNext()) {
			sb.append(this.get());
		}
		this.setPosition(initialPosition); // restore the position
		return sb.toString();
	}

	/**
	 * Checks if there are still characters remaining to parse.
	 * 
	 * @return true if there are remaining characters
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public boolean hasNext() throws JsonParserInternalException {
		try {
			int initialPosition = this.getPosition();
			int response = this.checkedRead();
			this.setPosition(initialPosition);
			return response >= 0;
		} catch (IOException e) {
			throw new JsonParserInternalException(String.format("An I/O error occurred while reading the "
					+ "reader %s.", this.data), e);
		}
	}
	
	@Override
	public boolean isNext(char query) {
		return this.hasNext() && query == this.getNextCharacters(1).charAt(0);
	}

	@Override
	public boolean isNext(char query, boolean incrementPosition) {
		boolean next = this.isNext(query);
		if (incrementPosition && next) { // query cannot be null if true
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
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public boolean isNext(String query) throws JsonParserInternalException {
		if (query != null) {
			return query.equals(this.getNextCharacters(query.length()));
		} else {
			return false;
		}
	}

	/**
	 * Checks whether the next characters are the query.
	 * Optionally the position mark can be incremented if the query is found.
	 * 
	 * @param query - the string to query for
	 * @param incrementPosition - true to increment the position mark by the search if found
	 * @return true if the next characters equal the query
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public boolean isNext(String query, boolean incrementPosition) throws JsonParserInternalException {
		boolean next = this.isNext(query);
		if (incrementPosition && next) { // query cannot be null if true
			this.setPosition(this.getPosition() + query.length());
		}
		return next;
	}

	/**
	 * Checks whether the next character is a digit.
	 * This will not increment the position mark.
	 * 
	 * @return true if the next character is a digit
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public boolean isNextDigit() throws JsonParserInternalException {
		return this.hasNext() && Character.isDigit(this.getNextCharacters(1).charAt(0));
	}

	/**
	 * Get the remaining characters of the parsed data as string.
	 * 
	 * @return a string from the parsers position mark to the end of the parsed data
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public String getRemaining() throws JsonParserInternalException {
		int initialPosition = this.getPosition();
		StringBuilder sb = new StringBuilder();
		// read remaining data
		while (this.hasNext()) {
			sb.append(this.get());
		}
		this.setPosition(initialPosition); // restore the position
		return sb.toString();
	}

	/**
	 * Moves the position mark to the next non-whitespace character in the parser. If the 
	 * parser only contains whitespace characters, the position mark is moved to the end 
	 * position, at which no character resides.
	 * 
	 * @throws JsonParserInternalException if an I/O error occurs
	 */
	@Override
	public void skipWhitespace() throws JsonParserInternalException {
		int position = this.getPosition();
		while (this.hasNext() && Character.isWhitespace(this.get())) {
			position++;
		}
		this.setPosition(position);
	}

	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void close() throws IOException {
		this.data.close();
	}

	/*
	 * The functions hashCode(), equals() and toString() of the super class should be 
	 * sufficient.
	 */

}
