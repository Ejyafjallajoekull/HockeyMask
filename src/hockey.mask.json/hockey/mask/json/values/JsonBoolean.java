package hockey.mask.json.values;

import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonBoolean class represents a JSON formatted boolean value.
 * 
 * @author Planters
 *
 */
public final class JsonBoolean extends JsonValue implements Comparable<JsonBoolean> {

	/**
	 * The JSON representation of a true boolean.
	 */
	public static final String JSON_TRUE_VALUE = "true";
	
	/**
	 * The JSON representation of a false boolean.
	 */
	public static final String JSON_FALSE_VALUE = "false";
	
	/**
	 * The JSON boolean true.
	 */
	public static final JsonBoolean JSON_TRUE = new JsonBoolean(true);
	
	/**
	 * The JSON boolean false.
	 */
	public static final JsonBoolean JSON_FALSE = new JsonBoolean(false);
	
	private boolean value = false;
	
	/**
	 * Create a new JSON boolean with the specified value.
	 * 
	 * @param jsonBoolean - the value of this JSON boolean
	 */
	private JsonBoolean(boolean jsonBoolean) {
		// private constructor to prevent instantiation
		this.value = jsonBoolean;
	}
	
	/**
	 * Get the value of this JSON boolean.
	 * 
	 * @return the boolean representation of this JSON boolean
	 */
	public boolean getValue() {
		return this.value;
	}
	
	/**
	 * Get a JSON formatted string from the internal representation of this JSON boolean.
	 * 
	 * @return the JSON string representation of the according boolean
	 */
	@Override
	public String toJson() {
		if (this.value) {
			return JsonBoolean.JSON_TRUE_VALUE;
		} else {
			return JsonBoolean.JSON_FALSE_VALUE;
		}
	}

	/**
	 * Parse the specified JSON formatted boolean and return its internal representation.
	 * 
	 * @param jsonBoolean - the JSON formatted boolean
	 * @return the internal representation of the JSON formatted boolean
	 * @throws JsonStandardException if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonBoolean parse(String jsonBoolean) throws JsonStandardException {
		Objects.requireNonNull(jsonBoolean, "A JSON formatted boolean may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonBoolean);
		JsonBoolean parsedBoolean = JsonBoolean.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedBoolean;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure "
					+ "JSON boolean.", jsonBoolean)); 
		}
	}
	
	/**
	 * Parse the next JSON formatted boolean from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted boolean from
	 * @return the internal representation of the JSON formatted boolean
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted boolean
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonBoolean parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int startingPosition = parser.getPosition();
		parser.skipWhitespace();
		if (parser.isNext(JsonBoolean.JSON_TRUE_VALUE, true)) {
			return new JsonBoolean(true);
		} else if (parser.isNext(JsonBoolean.JSON_FALSE_VALUE, true)) {
			return new JsonBoolean(false);
		} else {
			parser.setPosition(startingPosition); // the parser should not be modified
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON boolean.", parser));
		}
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(this.value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof JsonBoolean) {
			return this.value == ((JsonBoolean) obj).value;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.toJson();
	}
	
	@Override
	public int compareTo(JsonBoolean jsonBoolean) {
		Objects.requireNonNull(jsonBoolean, String.format("The JSON boolean \"%s\" cannot be compared to null.", this));
		return Boolean.compare(this.value, jsonBoolean.value);
	}

}
