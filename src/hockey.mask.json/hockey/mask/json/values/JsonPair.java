package hockey.mask.json.values;

import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonPair class represents a single name-value-pair formated in JSON.
 * 
 * @author Planters
 *
 */
final class JsonPair {

	private final JsonString name;
	private JsonValue value = null;
	
	/**
	 * The separator used to separate name and value in a JSON pair.
	 */
	public static final char JSON_PAIR_SEPARATOR = ':';
	
	/**
	 * Create a new JSON name-value-pair. The name will be immutable.
	 * 
	 * @param name - the name of the pair
	 * @param value - the value of the pair
	 * @throws NullpointerException if null is supplied for name as it must be a JSON 
	 * formatted string or value as it must be any JSON formatted value
	 */
	public JsonPair(JsonString name, JsonValue value) {
		this.name = Objects.requireNonNull(name, "JSON strings may not be null.");
		this.setValue(value);
	}
	
	/**
	 * Get the name of the pair.
	 * 
	 * @return the name of the pair
	 */
	public JsonString getName() {
		return this.name;
	}
	
	/**
	 * Get the value of the pair.
	 * 
	 * @return the value of the pair
	 */
	public JsonValue getValue() {
		return this.value;
	}
	
	/**
	 * Set the value of the pair.
	 * 
	 * @param value - the value of the pair
	 * @throws NullPointerException if the value is null
	 */
	public void setValue(JsonValue value) {
		this.value = Objects.requireNonNull(value, "A the value of a JSON formatted pair cannot be null.");
	}
	
	/**
	 * Convert this JsonPair to a JSON formatted pair string. 
	 * Null values will be interpreted as JSON value of type null.
	 * 
	 * @return the JSON representation of this pair
	 */
	public String toJson() {
		return String.format("%s%s%s", this.getName().toJson(), JsonPair.JSON_PAIR_SEPARATOR, this.getValue().toJson());
	}
	
	/**
	 * Create a new JsonPair from a JSON formatted pair.
	 * 
	 * @param jsonPair - the string representing the name-value-pair
	 * @return the internal representation of the JSON formatted pair
	 * @throws JsonStandardException if the describing string is not 
	 * formatted according to the JSON standard
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonPair parse(String jsonPair) throws JsonStandardException {
		Objects.requireNonNull(jsonPair, "A null string cannot be parsed as JSON pair.");
		JsonStringParser jp = new JsonStringParser(jsonPair);
		JsonPair parsedPair = JsonPair.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedPair;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON pair.", 
					jsonPair)); 
		}
	}
	
	/**
	 * Create a new JsonPair from a JSON formatted string.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted pair from
	 * @return the internal representation of the JSON formatted pair
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted pair
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonPair parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int initialPosition = parser.getPosition();
		try {
			JsonString name = JsonString.parseNext(parser);
			parser.skipWhitespace();
			if (!parser.isNext(JsonPair.JSON_PAIR_SEPARATOR, true)) {
				parser.setPosition(initialPosition);
				throw new JsonStandardException(String.format("The next element in the JSON parser "
						+ "%s is not a JSON pair.", parser));
			}
			JsonValue value = JsonValue.parseNext(parser);
			return new JsonPair(name, value);
		} catch (JsonStandardException e) {
			parser.setPosition(initialPosition);
			throw e;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof JsonPair) {
			JsonPair pair = (JsonPair) obj;
			// neither name nor value can be null
			return this.value.equals(pair.value) 
					&& this.name.equals(pair.name);	
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31 * this.name.hashCode() + this.value.hashCode();
	}
		
	@Override
	public String toString() {
		return String.format("\"%s\"%s%s", this.getName(), JsonPair.JSON_PAIR_SEPARATOR, this.getValue());
	}

}
