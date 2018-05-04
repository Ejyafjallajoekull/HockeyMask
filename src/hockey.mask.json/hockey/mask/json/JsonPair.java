package hockey.mask.json;

/**
 * The JsonPair class represents a single name-value-pair formated in JSON.
 * 
 * @author Planters
 *
 */
public class JsonPair {

	private JsonString name = null;
	private JsonValue value = null;
	
	/**
	 * The separator used to separate name and value in a JSON pair.
	 */
	public static final String JSON_PAIR_SEPARATOR = ":";
	
	/**
	 * Create a new JSON name-value-pair. The name will be immutable.
	 * 
	 * @param name - the name of the pair
	 * @param value - the value of the pair
	 * @throws NullpointerException if null is supplied for name as it must be a JSON 
	 * formatted string
	 */
	public JsonPair(JsonString name, JsonValue value) {
		this.setName(name);
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
	 * Set the name of the pair. This method is private since the name/key should be immutable. 
	 * Only the value is allowed to change.
	 * 
	 * @param name - the name of the pair
	 * @throws NullPointerException if null is supplied for name as it must be a JSON 
	 * formatted string
	 */
	private void setName(JsonString name) {
		if (name != null) {
			this.name = name;
		} else {
			throw new NullPointerException("JSON strings may not be null.");
		}
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
	 */
	public void setValue(JsonValue value) {
		this.value = value;
	}
	
	/**
	 * Convert this JsonPair to a JSON formatted pair string. 
	 * Null values will be interpreted as JSON value of type null.
	 * 
	 * @return the JSON representation of this pair
	 */
	public String toJson() {
		if (this.getValue() != null) {
			return String.format("%s%s%s", this.getName().toJson(), JsonPair.JSON_PAIR_SEPARATOR, this.getValue().toJson());
		} else {
			return String.format("%s%s%s", this.getName().toJson(), JsonPair.JSON_PAIR_SEPARATOR, JsonValue.JSON_NULL_VALUE);
		}
	}
	
	/**
	 * Create a new JsonPair from a JSON formatted string.
	 * 
	 * @param jsonPair - the string representing the name-value-pair
	 * @throws JsonStandardException - thrown if the describing string is not 
	 * formatted according to the JSON standard
	 */
	public static JsonPair parse(String jsonPair) throws JsonStandardException {
		if (jsonPair != null) {
			String[] firstJsonString = JsonString.splitByFirstJsonString(jsonPair);
			// trim all the splitted strings for convenience
			for (int i = 0; i < firstJsonString.length; i++) {
				firstJsonString[i] = firstJsonString[i].trim();
			}
			/*
			 * Ensure no invalid data comes before the first appearance of a JSON formatted 
			 * string. Furthermore, there must be a JSON formatted string defining the name of 
			 * the pair and also trailing data defining the value.
			 */
			if (firstJsonString[0].length() == 0 && firstJsonString[1].length() > 0 &&
					firstJsonString[2].length() > JsonPair.JSON_PAIR_SEPARATOR.length()) {
				// check if the next character of the trailing data is a pair separator
				if (firstJsonString[2].substring(0, JsonPair.JSON_PAIR_SEPARATOR.length()).equals(
						JsonPair.JSON_PAIR_SEPARATOR)) {
					firstJsonString[2] = firstJsonString[2].substring(JsonPair.JSON_PAIR_SEPARATOR.length());
					return new JsonPair(JsonString.parse(firstJsonString[1]), JsonValue.parse(firstJsonString[2]));
				} else {
					throw new JsonStandardException(String.format("The string \"%s\" is not a JSONPair.", 
							jsonPair));	
				}
			} else {
				throw new JsonStandardException(String.format("The string \"%s\" is not a JSONPair.", 
						jsonPair));
			}
		} else {
			throw new JsonStandardException(String.format("The string \"%s\" is not a JSONPair.", 
					jsonPair));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JsonPair) {
			JsonPair pair = (JsonPair) obj;
			// the name cannot be null
			if (this.getName().equals(pair.getName())) {
				if (this.getValue() != null) {
					return this.getValue().equals(pair.getValue());
				} else {
					return pair.getValue() == null;
				}
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
		result = prime * result + ((this.getValue() == null) ? 0 : this.getValue().hashCode());
		return result;
	}
		
	@Override
	public String toString() {
		return String.format("\"%s\"%s%s", this.getName(), JsonPair.JSON_PAIR_SEPARATOR, this.getValue());
	}

}
