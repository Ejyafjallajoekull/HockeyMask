package hockey.mask.json.values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonObject class represents an object formatted in the JSON standard. 
 * Internally however it will be backed up by a list for convenience.
 * This is not implemented as map since duplicate key, while being discouraged, are 
 * explicitly allowed in the standard.
 * 
 * @author Planters
 *
 */
public final class JsonObject extends JsonValue {

	/**
	 *  The identifier used to identify the start of a JSON formatted object.
	 */
	public static final char JSON_OBJECT_START_IDENTIFIER = '{';
	
	/**
	 *  The identifier used to identify the end of a JSON formatted object.
	 */
	public static final char JSON_OBJECT_END_IDENTIFIER = '}';

	/**
	 * The separator used to separate a name and value pair in a JSON object.
	 */
	public static final char JSON_OBJECT_NAME_VALUE_SEPARATOR = JsonPair.JSON_PAIR_SEPARATOR;
	
	/**
	 *  The separator used to separate JSON formatted pairs or members contained in 
	 *  a JSON formatted object.
	 */
	public static final char JSON_OBJECT_PAIR_SEPARATOR = ',';
	
	private final ArrayList<JsonPair> members = new ArrayList<JsonPair>();
	
	/**
	 * Create a new JSON object without any members.
	 */
	public JsonObject() {
		super();
	}
	
	/**
	 * Get the names of all members of this JSON object.
	 * 
	 * @return the names of all members
	 */
	public JsonString[] getNames() {
		JsonString[] names = new JsonString[this.members.size()];
		for (int i = 0; i < this.members.size(); i++) {
			names[i] = this.members.get(i).getName();
		}
		return names;
	}
	
	/**
	 * Get the member values assigned to the specified member name.
	 * This may be multiple values, however, the JSON standard discourages 
	 * the use of duplicate member names, so following those recommendations most 
	 * of the time a array holding a single value will be returned.
	 * 
	 * @param name - the member name to get the values from
	 * @return the values assigned to the specified member name
	 */
	public JsonValue[] getValues(JsonString name) {
		ArrayList<JsonValue> values = new ArrayList<JsonValue>();
		for (JsonPair pair : this.members) {
			if (pair.getName().equals(name)) {
				values.add(pair.getValue());
			}
		}
		return values.toArray(new JsonValue[values.size()]);
	}
	
	/**
	 * Get the value of the first occurrence of a member with the specified name.<br>
	 * This function should always be used if it is known that the JSON standard 
	 * recommendation of unique member names was satisfied.<br>
	 * Null will be returned if no member with the specified name does exist.
	 * 
	 * @param name - the name of the member
	 * @return the value of the first member with the specified name
	 */
	public JsonValue get(JsonString name) {
		for (JsonPair pair : this.members) {
			if (pair.getName().equals(name)) {
				return pair.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Checks whether this JSON object has a member with the specified name.
	 * 
	 * @param name - the name of the member
	 * @return true if a member with the specified name is present, false if not
	 */
	public boolean hasMember(JsonString name) {
		for (JsonPair pair : this.members) {
			if (pair.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Convert this JSON object to a JSON formatted object string. 
	 * 
	 * @return the JSON representation of this object
	 */
	@Override
	public String toJson() {
		StringBuilder jsonString = new StringBuilder(Character.toString(JsonObject.JSON_OBJECT_START_IDENTIFIER));
		for (int i = 0; i < this.members.size(); i++) {
			JsonPair val = this.members.get(i);
			jsonString.append(val.toJson());
			if (i != this.size() - 1) {
				jsonString.append(JsonObject.JSON_OBJECT_PAIR_SEPARATOR);
			}
		}
		jsonString.append(JsonObject.JSON_OBJECT_END_IDENTIFIER);
		return jsonString.toString();
	}
	
	/**
	 * Parse the specified JSON formatted object and return its internal representation.
	 * 
	 * @param jsonObject - the JSON formatted object
	 * @return the internal representation of the JSON formatted object
	 * @throws JsonStandardException thrown if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonObject parse(String jsonObject) throws JsonStandardException {
		Objects.requireNonNull(jsonObject, "A JSON formatted object may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonObject);
		JsonObject parsedObject = JsonObject.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedObject;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON object.", 
					jsonObject)); 
		}
	}
	
	/**
	 * Parse the next JSON formatted object from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted object from
	 * @return the internal representation of the JSON formatted object
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted object
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonObject parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int startingPosition = parser.getPosition();
		parser.skipWhitespace();
		// start condition
		if (parser.isNext(JsonObject.JSON_OBJECT_START_IDENTIFIER, true)) {
			JsonObject parsedObject = new JsonObject();
			boolean firstPassed = false;
			while (parser.hasNext()) {
				parser.skipWhitespace();
				// end condition
				if (parser.isNext(JsonObject.JSON_OBJECT_END_IDENTIFIER, true)) {
					return parsedObject;
				} else { // append value
					if (firstPassed) {
						if (parser.isNext(JsonObject.JSON_OBJECT_PAIR_SEPARATOR, true)) {
							parsedObject.add(JsonPair.parseNext(parser));
						} else {
							// throw exception if there is no value separation
							parser.setPosition(startingPosition); // the parser should not be modified
							throw new JsonStandardException(String.format("The next element in the JSON parser "
									+ "%s is not a JSON object.", parser));
						}
					} else { // there is no separator on the first element
						parsedObject.add(JsonPair.parseNext(parser));
						firstPassed = true;
					}
				}
			}
		}
		// throw exception if end condition is not reached
		parser.setPosition(startingPosition); // the parser should not be modified
		throw new JsonStandardException(String.format("The next element in the JSON parser "
				+ "%s is not a JSON object.", parser));
	}
	
	/**
	 * Adds the specified pair as member to the JSON object.
	 * While not recommended, it is possible to add duplicate members to the JSON object.
	 * Null is not permitted.
	 * 
	 * @param pair - the member to add
	 * @throws NullPointerException if the member to add is null
	 */
	private void add(JsonPair pair) {
		Objects.requireNonNull(pair, "Null is no valid member for a JSON object.");
		this.members.add(pair);
	}

	/**
	 * Adds the specified pair as member to the JSON object.
	 * While not recommended, it is possible to add duplicate members to the JSON object.
	 * Null is not permitted.
	 * 
	 * @param name - the name of the member to add
	 * @param value - the value to add for the specified member
	 * @throws NullPointerException if the member name or value is null
	 */
	public void add(JsonString name, JsonValue value) {
		Objects.requireNonNull(name, "Null is no valid member name for a JSON object.");
		Objects.requireNonNull(value, "Null is no valid member value for a JSON object.");
		this.members.add(new JsonPair(name, value));
	}

	/**
	 * Removes all members from the JSON object.
	 */
	public void clear() {
		this.members.clear();
	}

	/**
	 * Checks whether this JSON object has any members.
	 * 
	 * @return true if this JSON array does have at least one member
	 */
	public boolean hasMembers() {
		return !this.members.isEmpty();
	}

	/**
	 * Remove all members with the specified name from the JSON object.
	 * 
	 * @param name - the name of the member to remove
	 */
	public void remove(JsonString name) {
		for (Iterator<JsonPair> i = this.members.iterator(); i.hasNext();) {
			JsonPair pair = i.next();
			if (pair.getName().equals(name)) {
				i.remove();
			}
		}
	}

	/**
	 * Get the number of members of this JSON object.
	 * 
	 * @return the number of members
	 */
	public int size() {
		return this.members.size();
	}

	/**
	 * Set the first member with the specified name to the specified value.
	 * If no member with this name does exist a new one is created and added.
	 * The value previously held by the member is returned. If the member has been 
	 * newly created null is returned.
	 * 
	 * @param name - the name of the member to set
	 * @param value - the value to set for the specified member
	 * @return the value previously held by the set member or null if the member has not 
	 * existed prior to this function call
	 * @throws NullPointerException if name or value is null
	 */
	public JsonValue set(JsonString name, JsonValue value) {
		Objects.requireNonNull(name, "Null is no valid member name for a JSON object.");
		Objects.requireNonNull(value, "Null is no valid member value for a JSON object.");
		for (JsonPair pair : this.members) {
			if (pair.getName().equals(name)) {
				JsonValue oldValue = pair.getValue();
				pair.setValue(value);
				return oldValue;
			}
		}
		this.add(name, value);
		return null;
	}

	@Override
	public int hashCode() {
		return this.members.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof JsonObject) {
			return this.members.equals(((JsonObject) obj).members);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.members.toString();
	}
		
}
