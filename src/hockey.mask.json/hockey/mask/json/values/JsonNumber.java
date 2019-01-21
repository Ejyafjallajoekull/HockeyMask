package hockey.mask.json.values;

import java.math.BigDecimal;
import java.util.Objects;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonNumber class represents a JSON formatted number.
 * 
 * @author Planters
 *
 */
public final class JsonNumber extends JsonValue implements Comparable<JsonNumber> {
	
	/**
	 * The JSON representation of a minus.
	 */
	public static final char JSON_MINUS_VALUE = '-';
	
	/**
	 * The string representation of the minus value. Used for a helper function.
	 */
	private static final String JSON_MINUS_VALUE_STRING_REPRESENTATION = Character.toString(JsonNumber.JSON_MINUS_VALUE);
	
	/**
	 * The JSON representation of a plus.
	 */
	public static final char JSON_PLUS_VALUE = '+';
	
	/**
	 * The JSON representation of a exponent.
	 */
	public static final char JSON_EXPONENT_VALUE = 'e';
	
	/**
	 * The JSON representation of a exponent.
	 */
	public static final char JSON_EXPONENT_CAPITAL_VALUE = 'E';
	
	/**
	 * The JSON representation of a floating point separator.
	 */
	public static final char JSON_FLOATING_SEPARATOR_VALUE = '.';

	private final BigDecimal value;
	
	/**
	 * Create a new JSON number with the specified value.
	 * 
	 * @param jsonNumber - the value of this JSON number
	 */
	public JsonNumber(int jsonNumber) {
		this.value = new BigDecimal(jsonNumber);
	}
	
	/**
	 * Create a new JSON number with the specified value.
	 * 
	 * @param jsonNumber - the value of this JSON number
	 */
	public JsonNumber(long jsonNumber) {
		this.value = new BigDecimal(jsonNumber);
	}
	
	/**
	 * Create a new JSON number with the specified value.
	 * 
	 * @param jsonNumber - the value of this JSON number
	 * @throws JsonStandardException if the passed number is infinity or NaN
	 */
	public JsonNumber(float jsonNumber) throws JsonStandardException {
		if (Float.isFinite(jsonNumber)) {
			this.value = new BigDecimal(jsonNumber);
		} else {
			throw new JsonStandardException("The number " + jsonNumber + "can not be represented "
					+ "by the JSON foramt.");
		}
	}
	
	/**
	 * Create a new JSON number with the specified value.
	 * 
	 * @param jsonNumber - the value of this JSON number
	 * @throws JsonStandardException if the passed number is infinity or NaN
	 */
	public JsonNumber(double jsonNumber) throws JsonStandardException {
		if (Double.isFinite(jsonNumber)) {
			this.value = new BigDecimal(jsonNumber);
		} else {
			throw new JsonStandardException("The number " + jsonNumber + "can not be represented "
					+ "by the JSON foramt.");
		}
	}
	
	/**
	 * Create a new JSON number with the specified value.
	 * 
	 * @param jsonNumber - the value of this JSON number
	 * @throws NullPointerException if the specified value is null
	 */
	public JsonNumber(BigDecimal jsonNumber) {
			this.value = Objects.requireNonNull(jsonNumber, "A JSON number cannot be created from null.");
	}
	
	
	
	/**
	 * Get the value of this JSON number.
	 * 
	 * @return the BigDecimal representation of this JSON number
	 */
	public BigDecimal getValue() {
		return this.value;
	}
	
	/**
	 * Get a JSON formatted number from the internal representation of this JSON number.
	 * 
	 * @return the JSON string representation of the according number
	 */
	@Override
	public String toJson() {
		return this.getValue().toString();
	}

	/**
	 * Parse the specified JSON formatted number and return its internal representation.
	 * 
	 * @param jsonNumber - the JSON formatted number
	 * @return the internal representation of the JSON formatted number
	 * @throws JsonStandardException if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonNumber parse(String jsonNumber) throws JsonStandardException {
		Objects.requireNonNull(jsonNumber, "A JSON formatted number may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonNumber);
		JsonNumber parsedNumber = JsonNumber.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedNumber;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure "
					+ "JSON number.", jsonNumber)); 
		}
	}
	
	/**
	 * Parse the next JSON formatted number from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted number from
	 * @return the internal representation of the JSON formatted number
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted number
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonNumber parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int startingPosition = parser.getPosition();
		parser.skipWhitespace();
		try {
			// make sure the JSON specification is fulfilled
			String numberAsString = JsonNumber.parseSignificandSign(parser) 
					+ JsonNumber.parseSignificand(parser) + JsonNumber.parseExponent(parser);
			return new JsonNumber(new BigDecimal(numberAsString));
		} catch (NumberFormatException | JsonStandardException e) {
			parser.setPosition(startingPosition); // the parser should not be modified
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON number.", parser), e);
		}
	}
	
	/**
	 * Helper function to correctly parse the sign in front of the significand.
	 * The sign may be minus or and empty string.
	 * 
	 * @param parser - the parser to use
	 * @return the sign of the significand
	 * @throws JsonStandardException if the number is not correctly formatted
	 */
	private static String parseSignificandSign(JsonParser parser) throws JsonStandardException {
		parser.skipWhitespace();
		if (parser.isNext(JsonNumber.JSON_MINUS_VALUE, true)) {
			return JsonNumber.JSON_MINUS_VALUE_STRING_REPRESENTATION;
		} else if (parser.isNextDigit()) {
			return "";
		} else {
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON number.", parser));
		}
	}
	
	/**
	 * Helper function to correctly parse the significand of the number.
	 * 
	 * @param parser - the parser to use
	 * @return the significand as string
	 * @throws JsonStandardException if the number is not correctly formatted
	 */
	private static String parseSignificand(JsonParser parser) throws JsonStandardException {
		StringBuilder integerPartBuilder = new StringBuilder();
		parser.skipWhitespace();
		while (parser.isNextDigit()) {
			integerPartBuilder.append(parser.get());
			parser.skipWhitespace();
		}
		String integerPart = integerPartBuilder.toString();
		/*
		 * There must be digits and no leading zeros are allowed.
		 */
		if (integerPart.length() > 0 && !(integerPart.startsWith("0") && integerPart.length() > 1)) {
			if (parser.isNext(JsonNumber.JSON_FLOATING_SEPARATOR_VALUE, true)) {
				StringBuilder fractionPartBuilder = new StringBuilder();
				parser.skipWhitespace();
				while (parser.isNextDigit()) {
					fractionPartBuilder.append(parser.get());
					parser.skipWhitespace();
				}
				String fractionPart = fractionPartBuilder.toString();
				/*
				 * There must be digits after the separator.
				 */
				if (fractionPart.length() > 0) {
					return integerPart + JsonNumber.JSON_FLOATING_SEPARATOR_VALUE + fractionPart;
				}
			} else {
				return integerPart;
			}
		}
		// in all other cases throw an exception
		throw new JsonStandardException(String.format("The next element in the JSON parser "
				+ "%s is not a JSON number.", parser));
	}
	/**
	 * Helper function to correctly parse the exponent of the number.
	 * The exponent may be an empty string
	 * 
	 * @param parser - the parser to use
	 * @return the exponent as string
	 * @throws JsonStandardException if the number is not correctly formatted
	 */
	private static String parseExponent(JsonParser parser) throws JsonStandardException {
		StringBuilder exponentBuilder = new StringBuilder();
		parser.skipWhitespace();
		// check if there is an exponent character
		if (parser.isNext(JsonNumber.JSON_EXPONENT_VALUE, true)) {
			exponentBuilder.append(JsonNumber.JSON_EXPONENT_VALUE);
		} else if (parser.isNext(JsonNumber.JSON_EXPONENT_CAPITAL_VALUE, true)) {
			exponentBuilder.append(JsonNumber.JSON_EXPONENT_CAPITAL_VALUE);
		} else {
			return "";
		}
		parser.skipWhitespace();
		// check if there is a sign for the exponent
		if (parser.isNext(JsonNumber.JSON_MINUS_VALUE, true)) {
			exponentBuilder.append(JsonNumber.JSON_MINUS_VALUE);
		} else if (parser.isNext(JsonNumber.JSON_PLUS_VALUE, true)) {
			exponentBuilder.append(JsonNumber.JSON_PLUS_VALUE);
		}
		parser.skipWhitespace();
		int digitStart = exponentBuilder.length();
		while (parser.isNextDigit()) {
			exponentBuilder.append(parser.get());
			parser.skipWhitespace();
		}
		// obligatory digit part
		if (exponentBuilder.length() - digitStart > 0) {
			return exponentBuilder.toString();
		} else {
			throw new JsonStandardException(String.format("The next element in the JSON parser "
					+ "%s is not a JSON number.", parser));	
		}	
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof JsonNumber) {
			return this.getValue().equals(((JsonNumber) obj).getValue());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.toJson();
	}

	@Override
	public int compareTo(JsonNumber jsonNumber) {
		Objects.requireNonNull(jsonNumber, String.format("The JSON number \"%s\" cannot be compared to null.", this));
		return this.value.compareTo(jsonNumber.value);
	}

}
