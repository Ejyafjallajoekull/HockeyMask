package hockey.mask.json;

import hockey.mask.json.parser.JsonParser;

/**
 * The JsonBoolean class represents a JSON formatted boolean value.
 * 
 * @author Planters
 *
 */
public class JsonBoolean extends JsonValue {

	/**
	 * The JSON representation of a true boolean.
	 */
	public static final String JSON_TRUE_VALUE = "true";
	
	/**
	 * The JSON representation of a false boolean.
	 */
	public static final String JSON_FALSE_VALUE = "false";
	
	private boolean value = false;
	
	/**
	 * Create a new JSON boolean with the specified value.
	 * 
	 * @param jsonBoolean - the value of this JSON boolean
	 */
	public JsonBoolean(boolean jsonBoolean) {
		this.setValue(jsonBoolean);
	}
	
	/**
	 * Set the JSON boolean to the specified boolean value.
	 * 
	 * @param value - the value to set
	 */
	private void setValue(boolean value) {
		this.value = value;
	}
	
	/**
	 * Get the value of this JSON boolean.
	 * 
	 * @return the boolean representation of this JSON boolean
	 */
	public boolean getValue() {
		return this.value;
	}

	@Override
	public JsonValueTypes getType() {
		return JsonValueTypes.BOOLEAN;
	}
	
	/**
	 * Get a JSON formatted string from the internal representation of this JSON boolean.
	 * 
	 * @return the JSON string representation of the according boolean
	 */
	@Override
	public String toJson() {
		if (this.getValue()) {
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
		if (jsonBoolean != null) {
			JsonParser jp = new JsonParser(jsonBoolean);
			JsonBoolean parsedBoolean = JsonBoolean.parseNext(jp);
			jp.skipWhitespace(); // needed for checking against garbage data
			if (!jp.hasNext()) {
				return parsedBoolean;
			} else { // the string should not contain any more garbage data
				throw new JsonStandardException(String.format("The string \"%s\" is not a pure "
						+ "JSON boolean.", jsonBoolean)); 
			}
		} else {
			throw new NullPointerException("A JSON formatted boolean may not be null.");
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
		if (parser != null) {
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
		} else {
			throw new NullPointerException("The JSON parser may not be null.");
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JsonBoolean) {
			return this.getValue() == ((JsonBoolean) obj).getValue();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.toJson();
	}

}
