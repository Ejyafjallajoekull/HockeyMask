package hockey.mask.json.values;

import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonNull class represents a JSON formatted null value.
 * 
 * @author Planters
 *
 */
public final class JsonNull extends JsonValue implements Comparable<JsonNull> {
	
	/**
	 * The JSON representation of a null value.
	 */
	public static final String JSON_NULL_VALUE = "null";
	
	/**
	 * The JSON null value.
	 */
	public static final JsonNull JSON_NULL = new JsonNull();

	/**
	 * Create a new JSON null value.
	 */
	private JsonNull() {
		super();
	}
	
	/**
	 * Get a JSON formatted string from the internal representation of this JSON null.
	 * 
	 * @return the JSON string representation of null
	 */
	@Override
	public String toJson() {
		return JsonNull.JSON_NULL_VALUE;
	}

	/**
	 * Parse the specified JSON formatted null and return its internal representation.
	 * 
	 * @param jsonNull - the JSON formatted null
	 * @return the internal representation of the JSON formatted null
	 * @throws JsonStandardException if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonNull parse(String jsonNull) throws JsonStandardException {
		Objects.requireNonNull(jsonNull, "A JSON formatted string may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonNull);
		JsonNull parsedNull = JsonNull.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedNull;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON null.", 
					jsonNull)); 
		}
	}
	
	/**
	 * Parse the next JSON formatted null from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted null from
	 * @return the internal representation of the JSON formatted null
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted null
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonNull parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int startingPosition = parser.getPosition();
		parser.skipWhitespace();
		if (parser.isNext(JsonNull.JSON_NULL_VALUE, true)) {
			return new JsonNull();
		} else {
			parser.setPosition(startingPosition); // the parser should not be modified
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON null.", parser));
		}
	}

	@Override
	public int hashCode() {
		return 42;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonNull;
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
	@Override
	public int compareTo(JsonNull jsonNull) {
		Objects.requireNonNull(jsonNull, String.format("The JSON null \"%s\" cannot be compared to null.", this));
		return 0;
	}
	
}
