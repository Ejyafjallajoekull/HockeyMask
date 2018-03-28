package hockey.mask.json;

import java.util.ArrayList;

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

	public static final String JSON_ARRAY_START_IDENTIFIER = "[";
	
	public static final String JSON_ARRAY_END_IDENTIFIER = "]";

	public static final String JSON_ARRAY_VALUE_SEPARATOR = ",";
	
	public ArrayList<JsonValue> content = new ArrayList<JsonValue>();
	
	/**
	 * Create a new, empty JSON array.
	 */
	public JsonArray() {
		
	}
	
//	/**
//	 * Set the content of the JSON array to the specified JSON values.
//	 * 
//	 * @param content - the new content of the array
//	 */
//	public void setContent(List<JsonValue> content) {
//		if (content != null) {
//			this.content = new ArrayList<JsonValue>(content);
//		}
//	}
//	
//	/**
//	 * Set the content of the JSON array to the specified JSON values.
//	 * 
//	 * @param content - the new content of the array
//	 */
//	public void setContent(JsonValue[] content) {
//		if (content != null) {
//			this.content = new ArrayList<JsonValue>(Arrays.asList(content));
//			this.
//		}
//	}
	
//	public int size() {
//		return this.content.size();
//	}
	
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

//	@Override
//	public Iterator<JsonValue> iterator() {
//		return content.iterator();
//	}

}
