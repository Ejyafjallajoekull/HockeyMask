package hockey.mask.json.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;

/**
 * The JsonObject class represents an object formatted in the JSON standard. 
 * Internally however it will be backed up by a list for convenience.
 * This is not implemented as map since duplicate key, while being discouraged, are 
 * explicitly allowed in the standard.
 * 
 * @author Planters
 *
 */
public class JsonObject extends JsonValue implements Iterable<JsonPair> {

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
	
	private ArrayList<JsonPair> members = new ArrayList<JsonPair>();
	
	/**
	 * Create a new JSON object without any members.
	 */
	public JsonObject() {
	}
	
	/**
	 * Create a new JSON object containing the specified content.
	 * 
	 * @param members - the content to be contained
	 */
//	public JsonObject(Collection<? extends JsonPair> members) {
//		super(members);
//	}
	
	/**
	 * Create a new JSON object with the specified capacity.
	 * 
	 * @param capacity - the underlying lists capacity
	 */
//	public JsonObject(int capacity) {
//		super(capacity);
//	}
	
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
	
	@Override
	public JsonValueTypes getType() {
		return JsonValueTypes.OBJECT;
	}
	
	/**
	 * Convert this JSON object to a JSON formatted object string. 
	 * 
	 * @return the JSON representation of this object
	 */
	@Override
	public String toJson() {
		StringBuilder jsonString = new StringBuilder(JsonObject.JSON_OBJECT_START_IDENTIFIER);
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
		if (jsonObject != null) {
			JsonParser jp = new JsonParser(jsonObject);
			JsonObject parsedObject = JsonObject.parseNext(jp);
			jp.skipWhitespace(); // needed for checking against garbage data
			if (!jp.hasNext()) {
				return parsedObject;
			} else { // the string should not contain any more garbage data
				throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON object.", 
						jsonObject)); 
			}
		} else {
			throw new NullPointerException("A JSON formatted object may not be null.");
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
		if (parser != null) {
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
		} else {
			throw new NullPointerException("The JSON parser may not be null.");
		}
	}
	
	/**
	 * Adds the specified pair as member to the JSON object.
	 * While not recommended, it is possible to add duplicate members to the JSON object.
	 * Null is not permitted.
	 * 
	 * @param pair - the member to add
	 * @throws NullPointerException if the member to add is null
	 */
	public void add(JsonPair pair) {
		if (pair != null) {
			this.members.add(pair);
		} else {
			throw new NullPointerException("Null is no valid member for a JSON object.");
		}
	}

	/**
	 * Adds all pairs of the specified collection as members to this JSON object.
	 * While not recommended, it is possible to add duplicate members to the JSON object.
	 * This method will fail if the collection contains null values.
	 * 
	 * @param pairsToAdd - the collection of members to add
	 * @return true if the collection has successfully been added, false if the collection 
	 * contains null
	 * @throws NullPointerException if the specified collection is null
	 */
	public boolean addAll(Collection<? extends JsonPair> pairsToAdd) {
		if (pairsToAdd != null) {
			if (!pairsToAdd.contains(null)) {
				return this.members.addAll(pairsToAdd);
			} else {
				return false;
			}
		} else {
			throw new NullPointerException("The collection " + pairsToAdd + " cannot be added.");
		}
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

	@Override
	public Iterator<JsonPair> iterator() {
		return this.members.iterator();
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
	 * @throws NullPointerException if the name is null
	 */
	public JsonValue set(JsonString name, JsonValue value) {
		for (JsonPair pair : this.members) {
			if (pair.getName().equals(name)) {
				JsonValue oldValue = pair.getValue();
				pair.setValue(value);
				return oldValue;
			}
		}
		// this will throw the necessary exception
		this.add(new JsonPair(name, value));
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JsonObject) {
			return this.members.equals(((JsonObject) obj).members);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.members.toString();
	}
		
}
