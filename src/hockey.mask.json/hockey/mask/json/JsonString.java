package hockey.mask.json;

/**
 * The JsonString class represents a string formatted in the JSON format.
 * 
 * @author Planters
 *
 */
public class JsonString {

	/*
	 * String is final so the JsonString class cannot extend it.
	 */
	
	/**
	 *  The identifier used to identify JSON formatted strings.
	 */
	public static final String JSON_STRING_IDENTIFIER = "\"";
	/**
	 * Characters to be escaped with the JSON string escape character.<br>
	 * First element: Java character to escape<br>
	 * Second element: JSON escaped character without escape character
	 */
	public static final String[][] JSON_STRING_ESCAPED_CHARACTERS =
			/*
			 *  Backslash needs to be the first in order for the toJson() method to
			 *  work correctly.
			 *  An array is probably more efficient than a linked map.
			 */
			{	{"\\", "\\"},	// backslash
				{"\"", "\""},	// apostrophe
				{"/", "/"},		// slash
				{"\b", "b"},	// backspace 
				{"\r", "r"},	// carriage return
				{"\f", "f"},	// formfeed
				{"\n", "n"},	// new line
				{"\t", "t"}};	// tab
			// TODO: is \\u unicode support necessary?
			
	/**
	 * The escape character for JSON formatted strings.
	 */
	public static final String JSON_STRING_ESCAPE_CHARACTER = "\\";
	
	String value = null;
	
	/**
	 * Create a new JSON formatted string from a Java string.
	 * 
	 * @param value - the java string content
	 */
	public JsonString(String value) {
		this.value = value;
	}
	
	/**
	 * Transforms the specified string to a JSON formatted string.
	 * If the string is null, the JSON representation of null is returned.
	 * 
	 * @param string . the string to transform
	 * @return the JSON string representation of the specified string
	 */
	public String toJson() {
		if (this.toString() != null) {
			/*
			 * TODO: Make this method more efficient and independent of the position 
			 * of the backslash in the escaped characters array by use of a StringBuilder.
			 */
			String jsonS = this.toString();
			// replace all special characters with their JSON representation
			for (String[] specialCharacter : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
				jsonS = jsonS.replace(specialCharacter[0], JSON_STRING_ESCAPE_CHARACTER + specialCharacter[1]);
			}
			return JsonString.JSON_STRING_IDENTIFIER + jsonS + JsonString.JSON_STRING_IDENTIFIER;
		} else {
			return JsonValue.JSON_NULL_VALUE;
		}
	}
	
	/**
	 * Parse the specified JSON formatted string and return its internal representation.
	 * This basically strips the string from the JSON string identifier characters.
	 * 
	 * @param jsonString - the JSON formatted string
	 * @return the string without the JSON string identifier
	 * @throws JsonStandardException thrown if the string was not JSON formatted
	 */
	public static String parse(String jsonString) throws JsonStandardException {
		//TODO: adpate function
		if (jsonString != null) {
			String trimmed = jsonString.trim();
			if (trimmed.startsWith(JsonString.JSON_STRING_IDENTIFIER) 
					&& trimmed.endsWith(JsonString.JSON_STRING_IDENTIFIER)) {
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
	public String toString() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonString other = (JsonString) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}