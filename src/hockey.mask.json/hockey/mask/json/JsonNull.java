package hockey.mask.json;

import hockey.mask.json.parser.JsonParser;

/**
 * The JsonNull class represents a JSON formatted null value.
 * 
 * @author Planters
 *
 */
public class JsonNull {
	
	/**
	 * The JSON representation of a null value.
	 */
	public static final String JSON_NULL_VALUE = "null";

	/**
	 * Create a new JSON null value.
	 */
	public JsonNull() {
		// empty constructor
	}
	
	/**
	 * Get a JSON formatted string from the internal representation of this JSON null.
	 * 
	 * @return the JSON string representation of null
	 */
	public String toJson() {
		return JsonNull.JSON_NULL_VALUE;
	}

	/**
	 * Parse the specified JSON formatted string and return its internal representation.
	 * This basically strips the string from the JSON string identifier characters.
	 * 
	 * @param jsonString - the JSON formatted string
	 * @return the internal representation of the JSON formatted string
	 * @throws JsonStandardException if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonNull parse(String jsonString) throws JsonStandardException {
		if (jsonString != null) {
			JsonParser jp = new JsonParser(jsonString);
			JsonNull parsedNull = JsonNull.parseNext(jp);
			jp.skipWhitespace(); // needed for checking against garbage data#
			if (!jp.hasNext()) {
				return parsedNull;
			} else { // the string should not contain any more garbage data
				throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON null.", 
						jsonString)); 
			}
		} else {
			throw new NullPointerException("A JSON formatted string may not be null.");
		}
	}
	
	/**
	 * Parse the next JSON formatted null from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted string from
	 * @return the internal representation of the JSON formatted string
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted null
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonNull parseNext(JsonParser parser) throws JsonStandardException {
		if (parser != null) {
			int startingPosition = parser.getPosition();
			parser.skipWhitespace();
			if (parser.isNext(JsonNull.JSON_NULL_VALUE, true)) {
				return new JsonNull();
			} else {
				parser.setPosition(startingPosition); // the parser should not be modified
				throw new JsonStandardException(String.format("The next element in the JSON parser "
						+ "%s is not a JSON null.", parser));
			}
		} else {
			throw new NullPointerException("The JSON parser may not be null.");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof JsonNull;
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
}
