package hockey.mask.test.values;

import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonStringParser;
import hockey.mask.json.values.JsonArray;
import hockey.mask.json.values.JsonBoolean;
import hockey.mask.json.values.JsonNull;
import hockey.mask.json.values.JsonNumber;
import hockey.mask.json.values.JsonObject;
import hockey.mask.json.values.JsonPair;
import hockey.mask.json.values.JsonString;
import hockey.mask.json.values.JsonValue;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonValueTesting class test the JsonValue class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonValueTesting implements TestSubject {

	private static final Random RANDOM = new Random();
	
	@Override
	public void runAllTests() throws TestFailureException {
		JsonValueTesting.testParsing();
		JsonValueTesting.testParsingNext();
	}
	
	/**
	 * Test the parsing of JSON formatted value to JSON values.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonValue.parse(null);
			throw new TestFailureException("Parsing of null for a JSON value should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON value should throw a "
					+ "NullPointerException.", e);
		}
		// test empty strings
		try {
			JsonValue.parse("");
			throw new TestFailureException("Parsing of empty for a JSON value should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test random values
		for (int i = 0; i < 10000; i++) {
			JsonValue initialValue = JsonValueTesting.generateRandomValue();
			try {
				JsonValue parsedValue = JsonValue.parse(initialValue.toJson());
				TestSubject.assertTestCondition(initialValue.equals(parsedValue), 
						String.format("The JSON object %s should equal the object %s parsed from "
								+ "the JSON formatted string \"%s\".",	initialValue, parsedValue, initialValue.toJson()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("The string \"%s\" violates the JSON standard.", 
						initialValue.toJson()), e);
			}
		}
	}
	
	/**
	 * Test sequentially parsing JSON formatted values.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonValue.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON value should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON objects
		for (int i = 0; i < 10000; i++) {
			JsonValue[] jsonTestValues = new JsonValue[JsonValueTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestValues.length; j++) {
				// create random values
				jsonTestValues[j] = JsonValueTesting.generateRandomValue();
				testString += jsonTestValues[j].toJson() + ",   ";
			}
			try {
				JsonStringParser jp = new JsonStringParser(testString);
				JsonValue[] parsedJsonValues = new JsonValue[jsonTestValues.length];
				for (int j = 0; j < jsonTestValues.length; j++) {
					parsedJsonValues[j] = JsonValue.parseNext(jp);
					jp.isNext(",", true);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestValues, parsedJsonValues),
					String.format("The parsed JSON strings %s should equal %s.", 
							Arrays.toString(parsedJsonValues), 
							Arrays.toString(jsonTestValues)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "objects failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		for (int i = 0; i < 10000; i++) {
			JsonValue jsonTestString = JsonValueTesting.generateRandomValue();
			String testString = jsonTestString.toJson();
			// remove the last curly bracket so an exception will be raised
			testString = testString.substring(0, testString.length() - 1);
			try {
				JsonStringParser jp = new JsonStringParser(testString);
				int initialPosition = jp.getPosition();
				try {
					JsonArray.parseNext(jp);
					throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
							+ "values should fail, but resulted in the parser %s.", 
							testString, jp));
				} catch (JsonStandardException e) {
					TestSubject.assertTestCondition(jp.getPosition() == initialPosition,
							String.format("The JSON parser %s should be reset to position %s after "
									+ "failing to parse, but is at %s.", 
									jp, initialPosition, jp.getPosition()));	
				}
			} catch (JsonStandardException e) {
				throw new TestFailureException("Creating the JSON parser failed.", e);
			}
		}
	}
	
	/**
	 * Generate a JSON object with random members.
	 * 
	 * @return a random JSON object
	 */
	private static JsonObject generateRandomObject() {
		JsonObject randomObject = new JsonObject();
		int objectSize = JsonValueTesting.RANDOM.nextInt(20);
		for (int j = 0; j < objectSize; j++) {
			randomObject.add(JsonValueTesting.generateRandomPair());
		}
		return randomObject;
	}

	/**
	 * Generates a random JSON string, which may contain an empty string.
	 * 
	 * @return a random JSON string
	 */
	private static JsonString generateRandomString() {
		byte[] randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
		JsonValueTesting.RANDOM.nextBytes(randomString);
		return new JsonString(new String(randomString));
	}
	
	/**
	 * Generate a random JSON pair.
	 * 
	 * @return a random JSON pair
	 */
	private static JsonPair generateRandomPair() {
		return new JsonPair(JsonValueTesting.generateRandomString(), 
				JsonValueTesting.generateRandomValue());
	}
	
	/**
	 * Generate a JSON array with random elements.
	 * 
	 * @return a random JSON array
	 */
	private static JsonArray generateRandomArray() {
		JsonArray randomArray = new JsonArray();
		int arraySize = JsonValueTesting.RANDOM.nextInt(20);
		for (int j = 0; j < arraySize; j++) {
			randomArray.add(JsonValueTesting.generateRandomValue());
		}
		return randomArray;
	}
	
	/**
	 * Generate a random JSON boolean.
	 * 
	 * @return a random JSON boolean
	 */
	private static JsonBoolean generateRandomBoolean() {
		return JsonValueTesting.RANDOM.nextBoolean() ? JsonBoolean.JSON_TRUE : JsonBoolean.JSON_FALSE;
	}
	
	/**
	 * Generate a random JSON number.
	 * 
	 * @return a random JSON number
	 */
	private static JsonNumber generateRandomNumber() {
		int randomChoice = JsonValueTesting.RANDOM.nextInt(4);
		switch (randomChoice) {
			
			case 0:
				try {
					return new JsonNumber(JsonValueTesting.RANDOM.nextFloat());
				} catch (JsonStandardException e) {
					return new JsonNumber(JsonValueTesting.RANDOM.nextInt());
				}
		
			case 1:
				try {
					return new JsonNumber(JsonValueTesting.RANDOM.nextDouble());
				} catch (JsonStandardException e) {
					return new JsonNumber(JsonValueTesting.RANDOM.nextLong());
				}
		
			case 2:
				return new JsonNumber(JsonValueTesting.RANDOM.nextLong());
		
			default:
				return new JsonNumber(JsonValueTesting.RANDOM.nextInt());
		
		}
	}
	
	/**
	 * Generate a random JSON value.
	 * 
	 * @return a random JSON value
	 */
	private static JsonValue generateRandomValue() {
		double randomChoice = JsonValueTesting.RANDOM.nextDouble();
		if (randomChoice < 0.25d) {
			return JsonValueTesting.generateRandomNumber();
		} else if (randomChoice < 0.5d) {
			return JsonValueTesting.generateRandomString();
		} else if (randomChoice < 0.75d) {
			return JsonValueTesting.generateRandomBoolean();
		} else if (randomChoice < 0.76d) {	// the chance needs to be small or otherwise very big objects will be created
			return JsonValueTesting.generateRandomObject();
		} else if (randomChoice < 0.77d) {	// the chance needs to be small or otherwise very big arrays will be created
			return JsonValueTesting.generateRandomArray();
		} else {
			return JsonNull.JSON_NULL;
		}
	}

}
