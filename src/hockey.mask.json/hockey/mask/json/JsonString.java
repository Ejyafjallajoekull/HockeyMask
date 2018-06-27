package hockey.mask.json;

import hockey.mask.json.parser.JsonParser;

/**
 * The JsonString class represents a string formatted in the JSON format.
 * 
 * @author Planters
 *
 */
public class JsonString extends JsonValue {

	/*
	 * String is final so the JsonString class cannot extend it.
	 */
	
	/**
	 *  The identifier used to identify JSON formatted strings.
	 */
	public static final String JSON_STRING_IDENTIFIER = "\"";
	/**
	 * Characters to be escaped with the JSON string escape character.<br>
	 * First element: Java character to escape<br>
	 * Second element: JSON escaped character without escape character
	 */
	public static final String[][] JSON_STRING_ESCAPED_CHARACTERS =
			/*
			 *  Backslash needs to be the first in order for the toJson() method to
			 *  work correctly.
			 *  An array is probably more efficient than a linked map.
			 */
			{	{"\\", "\\"},	// backslash
				{"\"", "\""},	// apostrophe
				{"/", "/"},		// slash
				{"\b", "b"},	// backspace 
				{"\r", "r"},	// carriage return
				{"\f", "f"},	// formfeed
				{"\n", "n"},	// new line
				{"\t", "t"} };	// tab
			// TODO: is \\u unicode support necessary?
			
	/**
	 * The escape character for JSON formatted strings.
	 */
	public static final String JSON_STRING_ESCAPE_CHARACTER = "\\";
	
	private String value = null;
	
	/**
	 * Create a new JSON formatted string from a Java string.
	 * 
	 * @param value - the java string content
	 * 
	 * @throws NullPointerException if null is passed string value
	 */
	public JsonString(String value) {
		this.setValue(value);
	}
	
	/**
	 * Set the JSON string to the specified string value.
	 * 
	 * @param value - the value to set
	 */
	private void setValue(String value) {
		if (value != null) {
			this.value = value;
		} else {
			throw new NullPointerException("A JSON formatted string cannot be null");
		}
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

	@Override
	public JsonValueTypes getType() {
		return JsonValueTypes.STRING;
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
			int endIndex = 0;
			for (int i = 0; i < sb.length(); i++) {
				for (String[] escape : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
					endIndex = i + escape[0].length();
					if (endIndex <= sb.length() && sb.subSequence(i, endIndex).equals(escape[0])) {
						sb.replace(i, endIndex, JsonString.JSON_STRING_ESCAPE_CHARACTER + escape[1]);
						i += JsonString.JSON_STRING_ESCAPE_CHARACTER.length() + escape[1].length() - 1;
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
		if (jsonString != null) {
			JsonParser jp = new JsonParser(jsonString);
			JsonString parsedString = JsonString.parseNext(jp);
			jp.skipWhitespace(); // needed for checking against garbage data
			if (!jp.hasNext()) {
				return parsedString;
			} else { // the string should not contain any more garbage data
				throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON string.", 
						jsonString)); 
			}
		} else {
			throw new NullPointerException("A JSON formatted string may not be null.");
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
		if (parser != null) {
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
						for (String[] escape : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
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
		} else {
			throw new NullPointerException("The JSON parser may not be null.");
		}
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		JsonString other = (JsonString) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
