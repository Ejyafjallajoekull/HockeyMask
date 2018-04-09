package hockey.mask.json.hockey.mask.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The JsonArray class represents an array formatted in the JSON standard. 
 * Internally however it will be implemented as a list for convenience.
 * 
 * @author Planters
 *
 */
public class JsonArray extends ArrayList<JsonValue> {
	
	/**
	 * Default serialisation ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *  The identifier used to identify the start of a JSON formatted array.
	 */
	public static final String JSON_ARRAY_START_IDENTIFIER = "[";
	
	/**
	 *  The identifier used to identify the end of a JSON formatted array.
	 */
	public static final String JSON_ARRAY_END_IDENTIFIER = "]";

	/**
	 *  The separator used to separate JSON formatted values contained in 
	 *  a JSON formatted array.
	 */
	public static final String JSON_ARRAY_VALUE_SEPARATOR = ",";
		
	/**
	 * Create a new, empty JSON array.
	 */
	public JsonArray() {
		super();
	}
	
	/**
	 * Create a new JSON array containing the specified content.
	 * 
	 * @param content - the content to be contained
	 */
	public JsonArray(Collection<? extends JsonValue> content) {
		super(content);
	}
	
	/**
	 * Create a new, empty JSON array with the specified capacity.
	 * 
	 * @param capacity - the underlying lists capacity
	 */
	public JsonArray(int capacity) {
		super(capacity);
	}
	
	/**
	 * Convert this JsonArray to a JSON formatted array string. 
	 * Null values will be interpreted as JSON value of type null.
	 * 
	 * @return the JSON representation of this array
	 */
	public String toJson() {
		StringBuilder jsonString = new StringBuilder(JsonArray.JSON_ARRAY_START_IDENTIFIER);
		JsonValue val = null;
		for (int i = 0; i < this.size(); i++) {
			val = this.get(i);
			if (val != null) {
				jsonString.append(val.toJson());
			} else {
				jsonString.append(JsonValue.JSON_NULL_VALUE);
			}
			if (i != this.size() - 1) {
				jsonString.append(JsonArray.JSON_ARRAY_VALUE_SEPARATOR);
			}
		}
		jsonString.append(JsonArray.JSON_ARRAY_END_IDENTIFIER);
		return jsonString.toString();
	}
	
	/**
	 * Parse the specified string as a JSON formatted array and create a JSON array 
	 * object from it.
	 * 
	 * @param jsonArray - the JSON formatted array string
	 * @return - the object created from the string
	 * @throws JsonStandardException thrown if the string is not a JSON formatted array
	 */
	public static JsonArray parse(String jsonArray) throws JsonStandardException {
		if (jsonArray != null) {
			String trimmed = jsonArray.trim();
			if (trimmed.startsWith(JsonArray.JSON_ARRAY_START_IDENTIFIER) 
					&& trimmed.endsWith(JsonArray.JSON_ARRAY_END_IDENTIFIER)) {
				trimmed = trimmed.substring(1,trimmed.length()-1);
				String[] values = null;
				// this is needed as empty strings will cause an error
				if (trimmed.length() > 0) {
					values = trimmed.split(JsonArray.JSON_ARRAY_VALUE_SEPARATOR, -1);
				} else {
					values = new String[0];
				}
				JsonArray parsedArray = new JsonArray(values.length);
				for (String val : values) {
					parsedArray.add(JsonValue.parse(val));
				}
				return parsedArray;
			} else {
				throw new JsonStandardException(String.format("A JSON array has to start "
						+ "with \"%s\" and end with \"%s\", but the supplied string \"%s\" "
						+ "does not.", JsonArray.JSON_ARRAY_START_IDENTIFIER, 
						JsonArray.JSON_ARRAY_END_IDENTIFIER, trimmed));
			}
		} else {
			return null;
		}
	}
	
	/*
	 * equals() and hashCode() functions of the super class should be sufficient.
	 */
	
}
