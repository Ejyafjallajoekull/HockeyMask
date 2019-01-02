package hockey.mask.test.values;

import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonStringParser;
import hockey.mask.json.values.JsonNull;
import hockey.mask.json.values.JsonValueTypes;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonNullTesting class test the JsonNull class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonNullTesting implements TestSubject {

	private static final Random RANDOM = new Random();
	
	@Override
	public void runAllTests() throws TestFailureException {
		JsonNullTesting.testConstructors();
		JsonNullTesting.testToJson();
		JsonNullTesting.testParsing();
		JsonNullTesting.testParsingNext();
		JsonNullTesting.testType();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		JsonNull firstNull = JsonNull.JSON_NULL;
		JsonNull secondNull = JsonNull.JSON_NULL;
		TestSubject.assertTestCondition(firstNull.equals(secondNull), 
				String.format("The JSON null %s should equal %s", 
						firstNull, secondNull));
		TestSubject.assertTestCondition(!firstNull.equals(null), 
				String.format("The JSON null %s should not equal a Java null.", firstNull));
	}
	
	/**
	 * Test the conversion of null to a JSON formatted string.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		JsonNull testNull = JsonNull.JSON_NULL;
		TestSubject.assertTestCondition(testNull.toJson().equals(JsonNull.JSON_NULL_VALUE), 
				String.format("The JSON representation of null "
						+ "should equal the value \"%s\", but is \"%s\".", 
						JsonNull.JSON_NULL_VALUE, testNull.toJson()));
	}
	
	/**
	 * Test parsing JSON formatted null.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonNull.parse(null);
			throw new TestFailureException("Parsing of null for a JSON null should fail.");
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON null should throw a "
					+ "NullPointerException.", e);
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test empty strings
		try {
			JsonNull.parse("");
			throw new TestFailureException("Parsing of an empty string for a JSON null should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test parsing the correct string
		try {
			JsonNull.parse(JsonNull.JSON_NULL_VALUE);
		} catch (JsonStandardException e) {
			throw new TestFailureException(String.format("Parsing \"%s\" should result in a "
					+ " Json null", JsonNull.JSON_NULL_VALUE), e);
		}
		// test random strings
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonNullTesting.RANDOM.nextInt(200)];
			JsonNullTesting.RANDOM.nextBytes(randomString);
			String testString = (new String(randomString)).replace(JsonNull.JSON_NULL_VALUE, "");
			if (testString.length() > 0) {
				try {
					JsonNull parsedJsonNull = JsonNull.parse(testString);
					throw new TestFailureException(String.format("Parsing the string \"%s\" "
							+ "as JSON null should fail, but resulted into %s.", testString, 
							parsedJsonNull));
				} catch (JsonStandardException e) {
					/*
					 * Do nothing as this is expected behaviour.
					 */
				}
			}
		}
	}
	
	/**
	 * Test sequentially parsing JSON formatted nulls.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonNull.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON null should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			JsonNull[] jsonTestNulls = new JsonNull[JsonNullTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestNulls.length; j++) {
				jsonTestNulls[j] = JsonNull.JSON_NULL;
				testString += jsonTestNulls[j].toJson() + "   ";
			}
			try {
				JsonStringParser jp = new JsonStringParser(testString);
				JsonNull[] parsedJsonNulls = new JsonNull[jsonTestNulls.length];
				for (int j = 0; j < jsonTestNulls.length; j++) {
					parsedJsonNulls[j] = JsonNull.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestNulls, parsedJsonNulls),
					String.format("The parsed JSON nulls %s should equal %s.", 
							Arrays.toString(parsedJsonNulls), 
							Arrays.toString(jsonTestNulls)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "nulls failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		String testString = JsonNull.JSON_NULL_VALUE;
		// remove the last "l" so an exception will be raised
		testString = testString.substring(0, testString.length() - 1);
		try {
			JsonStringParser jp = new JsonStringParser(testString);
			int initialPosition = jp.getPosition();
			try {
				JsonNull.parseNext(jp);
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "null should fail, but resulted in the parser %s.", 
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

	/**
	 * Test getting the correct type for a JSON null.
	 * 
	 * @throws TestFailureException
	 */
	private static void testType() throws TestFailureException {
			JsonNull testNull = JsonNull.JSON_NULL;
			TestSubject.assertTestCondition(testNull.getType() == JsonValueTypes.NULL, 
					String.format("The JSON null %s should be of type %s, but is of type %s "
					+ "instead.", testNull, JsonValueTypes.NULL, testNull.getType()));
	}
	
}
