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
	 * 
	 * @throws NullPointerException if null is passed string value
	 */
	public JsonString(String value) {
		if (value != null) {
			this.value = value;
		} else {
			throw new NullPointerException("A JSON formatted string cannot be null");
		}
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
//			String jsonS = this.toString();
			StringBuilder sb = new StringBuilder(this.toString());
			int endIndex = 0;
			for (int i = 0; i < sb.length(); i++) {
				for (String[] escape : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
					endIndex = i + escape[0].length();
					if (endIndex <= sb.length() && sb.subSequence(i, endIndex).equals(escape[0])) {
						sb.replace(i, endIndex, JsonString.JSON_STRING_ESCAPE_CHARACTER + escape[1]);
						i += JsonString.JSON_STRING_ESCAPE_CHARACTER.length() + escape[1].length() - 1;
					}
				}
			}
			return sb.insert(0, JsonString.JSON_STRING_IDENTIFIER).append(JsonString.JSON_STRING_IDENTIFIER).toString();
//			// replace all special characters with their JSON representation
//			for (String[] specialCharacter : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
//				jsonS = jsonS.replace(specialCharacter[0], JSON_STRING_ESCAPE_CHARACTER + specialCharacter[1]);
//			}
//			return JsonString.JSON_STRING_IDENTIFIER + jsonS + JsonString.JSON_STRING_IDENTIFIER;
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
	public static JsonString parse(String jsonString) throws JsonStandardException {
		if (jsonString != null) {
			String trimmed = jsonString.trim();
			if (trimmed.startsWith(JsonString.JSON_STRING_IDENTIFIER) 
					&& trimmed.endsWith(JsonString.JSON_STRING_IDENTIFIER)) {
				// strip identifiers and create StringBuilder 
				StringBuilder sb = new StringBuilder(trimmed.substring(JsonString.JSON_STRING_IDENTIFIER.length(), trimmed.length()-JsonString.JSON_STRING_IDENTIFIER.length()));
				// replace all escaped characters with their Java representation
//				for (String[] specialCharacter : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
//					trimmed = trimmed.replace(JSON_STRING_ESCAPE_CHARACTER + specialCharacter[1], specialCharacter[0]);
//					System.out.println(String.format("Replacing [%s] with [%s], yielding [%s]",
//							JSON_STRING_ESCAPE_CHARACTER + specialCharacter[1], specialCharacter[0], trimmed));
//				}
				int endIndex = 0;
				for (int i = 0; i < sb.length(); i++) {
					endIndex = i + JsonString.JSON_STRING_ESCAPE_CHARACTER.length();
					// find escape characters
					if (endIndex <= sb.length() && sb.subSequence(i, endIndex).equals(JsonString.JSON_STRING_ESCAPE_CHARACTER)) {
						for (String[] escape : JsonString.JSON_STRING_ESCAPED_CHARACTERS) {
							if (endIndex + escape[1].length() <= sb.length() && sb.subSequence(endIndex, endIndex + escape[1].length()).equals(escape[1])) {
								sb.replace(i, endIndex + escape[1].length(), escape[0]);
								i += escape[0].length() - 1;
								/*
								 * Exit the loop if the according character is found, so 
								 * subsequent meaningful characters are not processed afterwards.
								 */
								break;
							}
						}
					}
					
				}
				return new JsonString(sb.toString());
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
