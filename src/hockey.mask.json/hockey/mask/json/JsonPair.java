package hockey.mask.json;

import java.util.Arrays;

/**
 * The JsonPair class represents a single name-value-pair formated in JSON.
 * 
 * @author Planters
 *
 */
public class JsonPair {

	private String name = "";
	private JsonValue value = null;
	
	/**
	 * The separator used to separate name and value in a JSON pair.
	 */
	public static final String JSON_PAIR_SEPARATOR = ":";
	
	/**
	 * Create a new JSON name-value-pair.
	 * 
	 * @param name - the name of the pair
	 * @param value - the value of the pair
	 * @throws JsonStandardException thrown if null is supplied for name as a JSON string 
	 * cannot be null
	 */
	public JsonPair(String name, JsonValue value) throws JsonStandardException {
		this.setName(name);
		this.setValue(value);
	}
	
	/**
	 * Get the name of the pair.
	 * 
	 * @return the name of the pair
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Transforms the name of this pair to a JSON formatted string.
	 * 
	 * @return the JSON string representation of the pair name
	 */
	public String getNameAsJson() {
		return JsonValue.stringToJson(this.getName());
	}
	
	/**
	 * Set the name of the pair.
	 * 
	 * @param name - the name of the pair
	 * @throws JsonStandardException thrown if null is supplied for name as a JSON string 
	 * cannot be null
	 */
	public void setName(String name) throws JsonStandardException {
		if (name != null) {
			this.name = name;
		} else {
			throw new JsonStandardException("JSON strings may not be null.");
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
			return String.format("%s%s%s", this.getNameAsJson(), JsonPair.JSON_PAIR_SEPARATOR, this.getValue().toJson());
		} else {
			return String.format("%s%s%s", this.getNameAsJson(), JsonPair.JSON_PAIR_SEPARATOR, JsonValue.JSON_NULL_VALUE);
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
			String[] pair = jsonPair.split(JsonPair.JSON_PAIR_SEPARATOR, 2);
			if (pair.length != 2) {
				return new JsonPair(JsonValue.parseString(pair[0]), 
						JsonValue.parse(pair[1]));
			} else {
				throw new JsonStandardException(String.format("A name-value-pair was expected, "
						+ "but %s was supplied.", Arrays.toString(pair)));
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
