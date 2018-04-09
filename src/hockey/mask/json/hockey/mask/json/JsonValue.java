package hockey.mask.json.hockey.mask.json;

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
	
	/**
	 * The JSON representation of a null value.
	 */
	public static final String JSON_NULL_VALUE = "null";
	
	/**
	 * The JSON representation of a true boolean.
	 */
	public static final String JSON_TRUE_VALUE = "true";
	
	/**
	 * The JSON representation of a false boolean.
	 */
	public static final String JSON_FALSE_VALUE = "false";
	
	Object value = null;
	JsonValueTypes type = JsonValueTypes.NULL;

	/**
	 * Create a null JSON value.
	 */
	public JsonValue() {
		this.setValueToNull();
	}
	
	/**
	 * Create a string JSON value.
	 * 
	 * @param string - the value
	 */
	public JsonValue(String string) {
		this.setValue(string);
	}
	
	/**
	 * Create a boolean JSON value.
	 * 
	 * @param bool - the value
	 */
	public JsonValue(boolean bool) {
		this.setValue(bool);
	}
	
	/**
	 * Create a number JSON value.
	 * 
	 * @param number - the value
	 */
	public JsonValue(int number) {
		this.setValue(number);
	}
	
	/**
	 * Create a number JSON value.
	 * 
	 * @param number - the value
	 */
	public JsonValue(long number) {
		this.setValue(number);
	}
	
	/**
	 * Create a number JSON value.
	 * 
	 * @param number - the value
	 */
	public JsonValue(float number) {
		this.setValue(number);
	}
	
	/**
	 * Create a number JSON value.
	 * 
	 * @param number - the value
	 */
	public JsonValue(double number) {
		this.setValue(number);
	}
	
	/**
	 * Create a number JSON value.
	 * 
	 * @param number - the value
	 */
	public JsonValue(BigDecimal number) {
		this.setValue(number);
	}
	
	/**
	 * Create a JSON object JSON value.
	 * 
	 * @param jsonObject - the value
	 */
	public JsonValue(JsonObject jsonObject) {
		this.setValue(jsonObject);
	}
	
	/**
	 * Create a JSON array JSON value.
	 * 
	 * @param jsonArray - the value
	 */
	public JsonValue(JsonArray jsonArray) {
		this.setValue(jsonArray);
	}
	
	/**
	 * Set the value of this JSON value to null.
	 */
	public void setValueToNull() {
		this.value = null;
		this.type = JsonValueTypes.NULL;
	}
	
	/**
	 * Set the value of this JSON value to a string.
	 * 
	 * @param string - the value to set
	 */
	public void setValue(String string) {
		if (string != null) {
			this.type = JsonValueTypes.STRING;
		} else {
			this.type = JsonValueTypes.NULL;
		}
		this.value = string;
	}
	
	/**
	 * Set the value of this JSON value to a boolean.
	 * 
	 * @param bool - the value to set
	 */
	public void setValue(boolean bool) {
		this.type = JsonValueTypes.BOOLEAN;
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
		this.type = JsonValueTypes.NUMBER;
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
		this.type = JsonValueTypes.NUMBER;
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
		this.type = JsonValueTypes.NUMBER;
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
		this.type = JsonValueTypes.NUMBER;
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
		if (number != null) {
			this.type = JsonValueTypes.NUMBER;
		} else {
			this.type = JsonValueTypes.NULL;
		}
		this.value = number;
	}
	
	/**
	 * Set the value of this JSON value to a JSON object.
	 * 
	 * @param jsonObject - the value to set
	 */
	public void setValue(JsonObject jsonObject) {
		if (jsonObject != null) {
			this.type = JsonValueTypes.OBJECT;
		} else {
			this.type = JsonValueTypes.NULL;
		}
		this.value = jsonObject;
	}
	
	/**
	 * Set the value of this JSON value to a JSON array.
	 * 
	 * @param jsonArray - the value to set
	 */
	public void setValue(JsonArray jsonArray) {
		if (jsonArray != null) {
			this.type = JsonValueTypes.ARRAY;
		} else {
			this.type = JsonValueTypes.NULL;
		}
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
	
	/**
	 * Get the type of this JSON value.
	 * 
	 * @return the type of the value
	 */
	public JsonValueTypes getType() {
		return this.type;
	}
	
	public static JsonValue parse(String jsonValue) throws JsonStandardException {
		if (jsonValue != null) {
			try {
				/*
				 * Try parsing it as JSON string, which will fail if its not a JSON 
				 * formatted string.
				 */
				return new JsonValue(JsonValue.parseString(jsonValue));
			} catch (JsonStandardException e) {
				try {
					/*
					 * Next, try parsing it as a JSON array, which may also fail if 
					 * its not a JSON formatted array.
					 */
					return new JsonValue(JsonArray.parse(jsonValue));
				} catch (JsonStandardException e1) {
					try {
						/*
						 * Next, try parsing it as a JSON object, which may also fail if 
						 * its not a JSON formatted object.
						 */
						return new JsonValue(JsonObject.parse(jsonValue));
					} catch (JsonStandardException e2) {
						/*
						 * Last, check if it is a JSON null or boolean. 
						 * If not parse it as a number.
						 */
						if (jsonValue.equals(JsonValue.JSON_NULL_VALUE)) {
							return new JsonValue((String) null);
						} else if (jsonValue.equals(JsonValue.JSON_TRUE_VALUE)) {
							return new JsonValue(true);
						} else if (jsonValue.equals(JsonValue.JSON_FALSE_VALUE)) {
							return new JsonValue(false);
						} else {
							try {
								return new JsonValue(new BigDecimal(jsonValue));
							} catch (Exception lastException) {
								/*
								 * Catch every other exception, which might be thrown 
								 * by trying to read in the string as number and throw 
								 * a exception flagging it as JSON formatted.
								 */
								throw new JsonStandardException(String.format(
										"The string \"%s\" is not a JSON formatted value.", 
										jsonValue), lastException);
							}
						}
					}

				}
			}
		} else {
			return null;
		}
		// TODO: implement
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
	
	/**
	 * Transforms the specified string to a JSON formatted string.
	 * 
	 * @param string . the string to transform
	 * @return the JSON string representation of the specified string
	 */
	public static String stringToJson(String string) {
		return JsonValue.JSON_STRING_IDENTIFIER + string + JsonValue.JSON_STRING_IDENTIFIER;
	}
	
	/**
	 * Get a string representing the value following the JSON standard.
	 * 
	 * @return this value in JSON format
	 */
	public String toJson() {
		switch (this.type) {
		
		case NULL:
			return JsonValue.JSON_NULL_VALUE;
			
		case STRING:
			return JsonValue.stringToJson((String) this.getValue());
			
		case NUMBER:
			return ((BigDecimal) this.getValue()).toString();
			
		case OBJECT:
			return ((JsonObject) this.getValue()).toJson();
			
		case ARRAY:
			return ((JsonArray) this.getValue()).toJson();
		
		default:
			return this.getValue().toString();
			
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JsonValue) {
			JsonValue val = (JsonValue) obj;
			if (this.getType() == val.getType()) {
				// simplify the null check for values by using the type
				if (this.getType() == JsonValueTypes.NULL) {
					return true;
				} else {
					return this.getValue().equals(val.getValue());				
				}
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
		result = prime * result + ((this.getValue() == null) ? 0 : this.getValue().hashCode());
		return result;
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
