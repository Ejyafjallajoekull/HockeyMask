package hockey.mask.json.values;

import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The abstract JsonValue class represents a single value formatted in the JSON format. This may 
 * be a string, a number, a boolean, null, an JSON object or a JSON array.
 * 
 * @author Planters
 *
 */
public abstract class JsonValue {
		
	/**
	 * Get a string representing the value following the JSON standard.
	 * 
	 * @return this value in JSON format
	 */
	public abstract String toJson();
	
	/**
	 * Parse the specified JSON formatted value and return its internal representation.
	 * 
	 * @param jsonValue - the JSON formatted value
	 * @return the internal representation of the JSON formatted value
	 * @throws JsonStandardException if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonValue parse(String jsonValue) throws JsonStandardException {
		Objects.requireNonNull(jsonValue, "A JSON formatted value may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonValue);
		JsonValue parsedValue = JsonValue.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedValue;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure "
					+ "JSON value.", jsonValue)); 
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
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		try {
			/*
			 * Try parsing it as JSON string, which will fail if it is not a JSON 
			 * formatted string.
			 */
			return JsonString.parseNext(parser);
		} catch (JsonStandardException stringException) {
			try {
				/*
				 * Next, try parsing it as a JSON array, which may also fail if 
				 * it is not a JSON formatted array.
				 */
				return JsonArray.parseNext(parser);
			} catch (JsonStandardException arrayException) {
				try {
					/*
					 * Next, try parsing it as a JSON object, which may also fail if 
					 * it is not a JSON formatted object.
					 */
					return JsonObject.parseNext(parser);
				} catch (JsonStandardException ObjectException) {
					try {
						/*
						 * Next, try parsing it as a JSON boolean, which may also fail if 
						 * it is not a JSON formatted boolean.
						 */
						return JsonBoolean.parseNext(parser);
					} catch (JsonStandardException booleanExcption) {
						try {
							/*
							 * Next, try parsing it as a JSON null, which may also fail if 
							 * it is not a JSON formatted null.
							 */
							return JsonNull.parseNext(parser);
						} catch (JsonStandardException nullExcption) {
							try {
								/*
								 * Last, parse it as a number. 
								 */
								return JsonNumber.parseNext(parser);
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
	}

}
