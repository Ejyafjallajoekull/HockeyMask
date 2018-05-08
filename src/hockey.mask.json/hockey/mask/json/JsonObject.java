package hockey.mask.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The JsonObject class represents an object formatted in the JSON standard. 
 * Internally however it will be implemented as a list for convenience.
 * This is not implemented as map since duplicate key, while being discouraged, are 
 * explicitly allowed in the standard.
 * 
 * @author Planters
 *
 */
public class JsonObject extends ArrayList<JsonPair> {

	/**
	 * Default serialisation ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *  The identifier used to identify the start of a JSON formatted object.
	 */
	public static final String JSON_OBJECT_START_IDENTIFIER = "{";
	
	/**
	 *  The identifier used to identify the end of a JSON formatted object.
	 */
	public static final String JSON_OBJECT_END_IDENTIFIER = "}";

	/**
	 *  The separator used to separate JSON formatted pairs or members contained in 
	 *  a JSON formatted object.
	 */
	public static final String JSON_OBJECT_PAIR_SEPARATOR = ",";
	
	/**
	 * Create a new JSON object without any members.
	 */
	public JsonObject() {
		super();
	}
	
	/**
	 * Create a new JSON object containing the specified content.
	 * 
	 * @param members - the content to be contained
	 */
	public JsonObject(Collection<? extends JsonPair> members) {
		super(members);
	}
	
	/**
	 * Create a new JSON object with the specified capacity.
	 * 
	 * @param capacity - the underlying lists capacity
	 */
	public JsonObject(int capacity) {
		super(capacity);
	}
	
	/**
	 * Get the names of all member pairs.
	 * 
	 * @return the names of all member pairs
	 */
	public JsonString[] getMemberNames() {
		ArrayList<JsonString> names = new ArrayList<JsonString>();
		for (JsonPair pair : this) {
			if (pair != null) {
				names.add(pair.getName());
			}
		}
		return names.toArray(new JsonString[names.size()]);
	}
	
	/**
	 * Get the member values assigned to the specified member name.
	 * This may be multiple values, however the JSON standard discourages 
	 * the use of duplicate member names, so following those recommendations most 
	 * of the time a array holding a single value will be returned.
	 * 
	 * @param name - the member name to get the values from
	 * @return the values assigned to the specified member name
	 */
	public JsonValue[] getValuesByName(JsonString name) {
		ArrayList<JsonValue> values = new ArrayList<JsonValue>();
		for (JsonPair pair : this) {
			if (pair != null && pair.getName().equals(name)) {
				values.add(pair.getValue());
			}
		}
		return values.toArray(new JsonValue[values.size()]);
	}
	
	/**
	 * A convenience function, which will return the value of the first occurrence 
	 * of a member with the specified name.<br>
	 * This function should always be used if it is known that the JSON standard 
	 * recommendation of unique member names was satisfied.<br>
	 * Null will be returned if no member with the specified name does exist.
	 * 
	 * @param name - the name assigned to the member
	 * @return the value of the first member with the specified name
	 */
	public JsonValue getFirstValueByName(JsonString name) {
		for (JsonPair pair : this) {
			if (pair != null && pair.getName().equals(name)) {
				return pair.getValue();
			}
		}
		return null;
		// TODO: No discrimination between pairs with value null and the return possible.
	}
	
	/**
	 * Checks whether this JSON object has a member with the specified name.
	 * 
	 * @param name - the name of the member
	 * @return true if a member with the specified name is present, false if not
	 */
	public boolean hasMember(JsonString name) {
		for (JsonPair pair : this) {
			if (pair != null && pair.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Convert this JsonObject to a JSON formatted object string. 
	 * Null members will be ignored.
	 * 
	 * @return the JSON representation of this object
	 */
	public String toJson() {
		StringBuilder jsonString = new StringBuilder(JsonObject.JSON_OBJECT_START_IDENTIFIER);
		JsonPair val = null;
		for (int i = 0; i < this.size(); i++) {
			val = this.get(i);
			// ignore null values
			if (val != null) {
				jsonString.append(val.toJson());
				if (i != this.size() - 1) {
					jsonString.append(JsonObject.JSON_OBJECT_PAIR_SEPARATOR);
				}
			}
		}
		jsonString.append(JsonObject.JSON_OBJECT_END_IDENTIFIER);
		return jsonString.toString();
	}
	
	/**
	 * Parse the specified string as a JSON formatted object and create a JSON object 
	 * object from it.
	 * 
	 * @param jsonObject - the JSON formatted object string
	 * @return - the object created from the string
	 * @throws JsonStandardException thrown if the string is not a JSON formatted object
	 */
	public static JsonObject parse(String jsonObject) throws JsonStandardException {
		// TODO: fix this, it does not work
		if (jsonObject != null) {
			String trimmed = jsonObject.trim();
			if (trimmed.startsWith(JsonObject.JSON_OBJECT_START_IDENTIFIER) 
					&& trimmed.endsWith(JsonObject.JSON_OBJECT_END_IDENTIFIER)) {
				trimmed = trimmed.substring(1,trimmed.length()-1);
				String[] values = null;
				// this is needed as empty strings will cause an error
				if (trimmed.length() > 0) {
					values = trimmed.split(JsonObject.JSON_OBJECT_PAIR_SEPARATOR, -1);
				} else {
					values = new String[0];
				}
				JsonObject parsedObject = new JsonObject(values.length);
				for (String val : values) {
					parsedObject.add(JsonPair.parse(val));
				}
				return parsedObject;
			} else {
				throw new JsonStandardException(String.format("A JSON object has to start "
						+ "with \"%s\" and end with \"%s\", but the supplied string \"%s\" "
						+ "does not.", JsonObject.JSON_OBJECT_START_IDENTIFIER, 
						JsonObject.JSON_OBJECT_END_IDENTIFIER, trimmed));
			}
		} else {
			return null;
		}
	}
	
	/*
	 * equals() and hashCode() functions of the super class should be sufficient.
	 */
	
}
