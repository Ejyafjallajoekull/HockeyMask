package hockey.mask.json.values;

import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonString class represents a string formatted in the JSON format.
 * 
 * @author Planters
 *
 */
public final class JsonString extends JsonValue implements Comparable<JsonString> {

	/*
	 * String is final so the JsonString class cannot extend it.
	 */
	
	/**
	 *  The identifier used to identify JSON formatted strings.
	 */
	public static final char JSON_STRING_IDENTIFIER = '\"';
	/**
	 * Characters to be escaped with the JSON string escape character.<br>
	 * First element: Java character to escape<br>
	 * Second element: JSON escaped character without escape character
	 */
	public static final char[][] JSON_STRING_ESCAPED_CHARACTERS =
			/*
			 *  Backslash needs to be the first in order for the toJson() method to
			 *  work correctly.
			 *  An array is probably more efficient than a linked map.
			 */
			{	{'\\', '\\'},	// backslash
				{'\"', '\"'},	// apostrophe
				{'/', '/'},		// slash
				{'\b', 'b'},	// backspace 
				{'\r', 'r'},	// carriage return
				{'\f', 'f'},	// formfeed
				{'\n', 'n'},	// new line
				{'\t', 't'} };	// tab
			// TODO: is \\u unicode support necessary?
			
	/**
	 * The escape character for JSON formatted strings.
	 */
	public static final char JSON_STRING_ESCAPE_CHARACTER = '\\';
	
	private final String value;
	
	/**
	 * Create a new JSON formatted string from a Java string.
	 * 
	 * @param value - the java string content
	 * 
	 * @throws NullPointerException if null is passed string value
	 */
	public JsonString(String value) {
		this.value = Objects.requireNonNull(value, "A JSON formatted string cannot be null.");
	}
	
	/**
	 * Get the value of this JSON string.
	 * This will return the Java representation of the JSON string.
	 * 
	 * @return the string representation of this JSON string
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Get a JSON formatted string from the internal representation of this JSON string.
	 * 
	 * @return the JSON string representation of the specified string
	 */
	@Override
	public String toJson() {
		// the string cannot be null at this point
		StringBuilder sb = new StringBuilder(this.getValue());
			for (int i = 0; i < sb.length(); i++) {
				for (char[] escape : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
					if (sb.charAt(i) == escape[0]) {
						sb.replace(i, i+1, Character.toString(JsonString.JSON_STRING_ESCAPE_CHARACTER) + escape[1]);
						i++; // skip the inserted character
					}
				}
			}
			return sb.insert(0, JsonString.JSON_STRING_IDENTIFIER).append(JsonString.JSON_STRING_IDENTIFIER).toString();
	}
	
	/**
	 * Parse the specified JSON formatted string and return its internal representation.
	 * This basically strips the string from the JSON string identifier characters.
	 * 
	 * @param jsonString - the JSON formatted string
	 * @return the internal representation of the JSON formatted string
	 * @throws JsonStandardException thrown if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonString parse(String jsonString) throws JsonStandardException {
		Objects.requireNonNull(jsonString, "A JSON formatted string may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonString);
		JsonString parsedString = JsonString.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedString;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON string.", 
					jsonString)); 
		}
	}
	
	/**
	 * Parse the next JSON formatted string from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted string from
	 * @return the internal representation of the JSON formatted string
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted string
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonString parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int startingPosition = parser.getPosition();
		parser.skipWhitespace();
		if (parser.isNext(JsonString.JSON_STRING_IDENTIFIER, true)) {
			// strip identifiers and create StringBuilder 
			StringBuilder sb = new StringBuilder();
			while (parser.hasNext()) {
				// end condition
				if (parser.isNext(JsonString.JSON_STRING_IDENTIFIER, true)) {
					return new JsonString(sb.toString());
				} else if (parser.isNext(JsonString.JSON_STRING_ESCAPE_CHARACTER, true)) { // escape characters
					for (char[] escape : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
						if (parser.isNext(escape[1], true)) {
							sb.append(escape[0]);
							/*
							 * Exit the loop if the according character is found, so 
							 * subsequent meaningful characters are not processed afterwards.
							 */
							break;
						}
					}
				} else { // standard characters
					sb.append(parser.get());
				}
			}
			// if the end condition has not been triggered throw an exception
			parser.setPosition(startingPosition); // do not modify the parser
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON string.", parser));
		} else {
			parser.setPosition(startingPosition); // the parser should not be modified
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON string.", parser));
		}
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof JsonString) {
			return this.value.equals(((JsonString) obj).value);
		}
		return false;
	}

	@Override
	public int compareTo(JsonString jsonString) {
		Objects.requireNonNull(jsonString, String.format("The JSON string \"%s\" cannot be compared to null.", this));
		return this.value.compareTo(jsonString.value);
	}

}
