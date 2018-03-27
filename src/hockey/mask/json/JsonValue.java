package hockey.mask.json;

import java.math.BigDecimal;

/**
 * The JsonValue class represents a single value formatted in the JSON format. This may 
 * be a string, a number, a boolean, null, an JSON object or a JSON array.
 * 
 * @author Planters
 *
 */
public class JsonValue {
	
	/**
	 *  The identifier used to identify JSON formatted strings.
	 */
	public static final String JSON_STRING_IDENTIFIER = "\"";
	
	Object value = null;

	public JsonValue() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Set the value of this JSON value to null.
	 */
	public void setValueToNull() {
		this.value = null;
	}
	
	/**
	 * Set the value of this JSON value to a string.
	 * 
	 * @param string - the value to set
	 */
	public void setValue(String string) {
		this.value = string;
	}
	
	/**
	 * Set the value of this JSON value to a boolean.
	 * 
	 * @param bool - the value to set
	 */
	public void setValue(boolean bool) {
		this.value = bool;
	}
	
	/**
	 * Set the value of this JSON value to a number by 
	 * supplying an integer.
	 * Internally the number is represented as BigDecimal.
	 * 
	 * @param number - the value to set
	 */
	public void setValue(int number) {
		this.value = new BigDecimal(number);
	}
	
	/**
	 * Set the value of this JSON value to a number by 
	 * supplying a long.
	 * Internally the number is represented as BigDecimal.
	 * 
	 * @param number - the value to set
	 */
	public void setValue(long number) {
		this.value = new BigDecimal(number);
	}
	
	/**
	 * Set the value of this JSON value to a number by 
	 * supplying a float.
	 * Internally the number is represented as BigDecimal.
	 * 
	 * @param number - the value to set
	 */
	public void setValue(float number) {
		this.value = new BigDecimal(number);
	}
	
	/**
	 * Set the value of this JSON value to a number by 
	 * supplying a double.
	 * Internally the number is represented as BigDecimal.
	 * 
	 * @param number - the value to set
	 */
	public void setValue(double number) {
		this.value = new BigDecimal(number);
	}
	
	/**
	 * Set the value of this JSON value to a number by 
	 * supplying a BigDecimal.
	 * This is also how the number will be represented 
	 * internally.
	 * 
	 * @param number - the value to set
	 */
	public void setValue(BigDecimal number) {
		this.value = number;
	}
	
	/**
	 * Set the value of this JSON value to a JSON object.
	 * 
	 * @param jsonObject - the value to set
	 */
	public void setValue(JsonObject jsonObject) {
		this.value = jsonObject;
	}
	
	/**
	 * Set the value of this JSON value to a JSON array.
	 * 
	 * @param jsonArray - the value to set
	 */
	public void setValue(JsonArray jsonArray) {
		this.value = jsonArray;
	}
	
	/**
	 * Get the value of this JSON value.
	 * 
	 * @return - the value
	 */
	public Object getValue() {
		return this.value;
	}
	
	public static JsonValue parse(String jsonValue) {
		return null;
	}
	
	/**
	 * Parse the specified JSON formatted string and return its internal representation.
	 * This basically strips the string from the JSON string identifier characters.
	 * 
	 * @param jsonString - the JSON formatted string
	 * @return the string without the JSON string identifier
	 * @throws JsonStandardException thrown if the string was not JSON formatted
	 */
	public static String parseString(String jsonString) throws JsonStandardException {
		if (jsonString != null) {
			String trimmed = jsonString.trim();
			if (trimmed.startsWith(JsonValue.JSON_STRING_IDENTIFIER) 
					&& trimmed.endsWith(JsonValue.JSON_STRING_IDENTIFIER)) {
				return trimmed.substring(1, trimmed.length()-1);
			} else {
				throw new JsonStandardException(String.format("The string \"%s\" is not a JSON string.", 
						jsonString));
			}
		} else {
			throw new JsonStandardException("A JSON formatted string may not be null.");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JsonValue) {
			
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (this.getValue() != null) {
			return this.getValue().toString();
		} else {
			return "null";
		}
	}

}
