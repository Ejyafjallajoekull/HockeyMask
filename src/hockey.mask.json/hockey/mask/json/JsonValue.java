package hockey.mask.json;

import java.math.BigDecimal;

import hockey.mask.json.parser.JsonParser;

/**
 * The JsonValue class represents a single value formatted in the JSON format. This may 
 * be a string, a number, a boolean, null, an JSON object or a JSON array.
 * 
 * @author Planters
 *
 */
public class JsonValue {
	
	Object value = new JsonNull();
	JsonValueTypes type = JsonValueTypes.NULL;

	/**
	 * Create a null JSON value.
	 */
	public JsonValue() {
		this.setValueToNull();
	}
	
	/**
	 * Create a null JSON value.
	 */
	public JsonValue(JsonNull jsonNull) {
		this.setValue(jsonNull);
	}
	
	/**
	 * Create a string JSON value.
	 * 
	 * @param string - the value
	 */
	public JsonValue(JsonString string) {
		this.setValue(string);
	}
	
	/**
	 * Create a boolean JSON value.
	 * 
	 * @param bool - the value
	 */
	public JsonValue(JsonBoolean bool) {
		this.setValue(bool);
	}
	
	/**
	 * Create a number JSON value.
	 * 
	 * @param number - the value
	 */
	public JsonValue(JsonNumber number) {
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
		this.value = new JsonNull();
		this.type = JsonValueTypes.NULL;
	}
	
	/**
	 * Set the value of this JSON value to a null.
	 * 
	 * @param jsonNull - the value to set
	 */
	public void setValue(JsonNull jsonNull) {
		this.type = JsonValueTypes.NULL;
		if (jsonNull != null) {
			this.value = jsonNull;
		} else {
			this.value = new JsonNull();
		}
	}
	
	/**
	 * Set the value of this JSON value to a string.
	 * 
	 * @param string - the value to set
	 */
	public void setValue(JsonString string) {
		if (string != null) {
			this.value = string;
			this.type = JsonValueTypes.STRING;
		} else {
			this.value = new JsonNull();
			this.type = JsonValueTypes.NULL;
		}
	}
	
	/**
	 * Set the value of this JSON value to a JSON boolean.
	 * 
	 * @param bool - the value to set
	 */
	public void setValue(JsonBoolean bool) {
		if (bool != null) {
			this.type = JsonValueTypes.BOOLEAN;
			this.value = bool;
		} else {
			this.value = new JsonNull();
			this.type = JsonValueTypes.NULL;
		}
	}
	
	/**
	 * Set the value of this JSON value to a number.
	 * 
	 * @param number - the value to set
	 */
	public void setValue(JsonNumber number) {
		if (number != null) {
			this.value = number;
			this.type = JsonValueTypes.NUMBER;
		} else {
			this.value = new JsonNull();
			this.type = JsonValueTypes.NULL;
		}
	}
	
	/**
	 * Set the value of this JSON value to a JSON object.
	 * 
	 * @param jsonObject - the value to set
	 */
	public void setValue(JsonObject jsonObject) {
		if (jsonObject != null) {
			this.value = jsonObject;
			this.type = JsonValueTypes.OBJECT;
		} else {
			this.value = new JsonNull();
			this.type = JsonValueTypes.NULL;
		}
	}
	
	/**
	 * Set the value of this JSON value to a JSON array.
	 * 
	 * @param jsonArray - the value to set
	 */
	public void setValue(JsonArray jsonArray) {
		if (jsonArray != null) {
			this.value = jsonArray;
			this.type = JsonValueTypes.ARRAY;
		} else {
			this.value = new JsonNull();
			this.type = JsonValueTypes.NULL;
		}
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
	
	/**
	 * Parse the specified JSON formatted value and return its internal representation.
	 * 
	 * @param jsonValue - the JSON formatted value
	 * @return the internal representation of the JSON formatted value
	 * @throws JsonStandardException if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonValue parse(String jsonValue) throws JsonStandardException {
		if (jsonValue != null) {
			JsonParser jp = new JsonParser(jsonValue);
			JsonValue parsedValue = JsonValue.parseNext(jp);
			jp.skipWhitespace(); // needed for checking against garbage data
			if (!jp.hasNext()) {
				return parsedValue;
			} else { // the string should not contain any more garbage data
				throw new JsonStandardException(String.format("The string \"%s\" is not a pure "
						+ "JSON value.", jsonValue)); 
			}
		} else {
			throw new NullPointerException("A JSON formatted value may not be null.");
		}
	}
	
	/**
	 * Parse the next JSON formatted value from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted value from
	 * @return the internal representation of the JSON formatted value
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted value
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonValue parseNext(JsonParser parser) throws JsonStandardException {
		if (parser != null) {
			try {
				/*
				 * Try parsing it as JSON string, which will fail if it is not a JSON 
				 * formatted string.
				 */
				return new JsonValue(JsonString.parseNext(parser));
			} catch (JsonStandardException stringException) {
				try {
					/*
					 * Next, try parsing it as a JSON array, which may also fail if 
					 * it is not a JSON formatted array.
					 */
					return new JsonValue(JsonArray.parseNext(parser));
				} catch (JsonStandardException arrayException) {
					try {
						/*
						 * Next, try parsing it as a JSON object, which may also fail if 
						 * it is not a JSON formatted object.
						 */
						return new JsonValue(JsonObject.parseNext(parser));
					} catch (JsonStandardException ObjectException) {
						try {
							/*
							 * Next, try parsing it as a JSON boolean, which may also fail if 
							 * it is not a JSON formatted boolean.
							 */
							return new JsonValue(JsonBoolean.parseNext(parser));
						} catch (JsonStandardException booleanExcption) {
							try {
								/*
								 * Next, try parsing it as a JSON null, which may also fail if 
								 * it is not a JSON formatted null.
								 */
								return new JsonValue(JsonNull.parseNext(parser));
							} catch (JsonStandardException nullExcption) {
								try {
									/*
									 * Last, parse it as a number. 
									 */
									return new JsonValue(JsonNumber.parseNext(parser));
								} catch (Exception lastException) {
									/*
									 * Catch every other exception, which might be thrown 
									 * by trying to read in the string as number and throw 
									 * a exception flagging it as not JSON formatted.
									 */
									throw new JsonStandardException(String.format(
											"The JSON parser %s does not hold a JSON formatted value.", 
											parser), lastException);
								}
							}
						}
					}
				}
			}
		} else {
			throw new NullPointerException("The JSON parser may not be null.");
		}
	}
	
	/**
	 * Get a string representing the value following the JSON standard.
	 * 
	 * @return this value in JSON format
	 */
	public String toJson() {
		switch (this.type) {
		
		case NULL:
			return ((JsonNull) this.getValue()).toJson();
			
		case STRING:
			return ((JsonString) this.getValue()).toJson();
			
		case NUMBER:
			return ((JsonNumber) this.getValue()).toJson();
			
		case BOOLEAN:
			return ((JsonBoolean) this.getValue()).toJson();
			
		case OBJECT:
			return ((JsonObject) this.getValue()).toJson();
			
		case ARRAY:
			return ((JsonArray) this.getValue()).toJson();
		
		default:
			return null;
			
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JsonValue) {
			JsonValue val = (JsonValue) obj;
			if (this.getType() == val.getType()) {
				return this.getValue().equals(val.getValue());				
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
		return String.format("%s:%s", this.getType(), this.getValue());
	}

}
