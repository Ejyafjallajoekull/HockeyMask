package hockey.mask.test;

import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonBoolean;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

public class JsonBooleanTesting implements TestSubject {

	private static final Random RANDOM = new Random();

	@Override
	public void runAllTests() throws TestFailureException {
		JsonBooleanTesting.testConstructors();
		JsonBooleanTesting.testGetValue();
		JsonBooleanTesting.testToJson();
		JsonBooleanTesting.testParsing();
		JsonBooleanTesting.testParsingNext();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		// test true and true
		JsonBoolean firstBool = new JsonBoolean(true);
		JsonBoolean secondBool = new JsonBoolean(true);
		TestSubject.assertTestCondition(firstBool.equals(secondBool), 
				String.format("The JSON boolean %s should equal %s", 
						firstBool, secondBool));
		// test true and null
		TestSubject.assertTestCondition(!firstBool.equals(null), 
				String.format("The JSON boolean %s should not equal a Java null.", firstBool));
		// test false and false
		firstBool = new JsonBoolean(false);
		secondBool = new JsonBoolean(false);
		TestSubject.assertTestCondition(firstBool.equals(secondBool), 
				String.format("The JSON boolean %s should equal %s", 
						firstBool, secondBool));
		// test false and null
		TestSubject.assertTestCondition(!firstBool.equals(null), 
				String.format("The JSON boolean %s should not equal a Java null.", firstBool));
		// test false and false
		firstBool = new JsonBoolean(true);
		secondBool = new JsonBoolean(false);
		TestSubject.assertTestCondition(!firstBool.equals(secondBool), 
				String.format("The JSON boolean %s should not equal %s", 
						firstBool, secondBool));
	}
	
	/**
	 * Test getting the Java representation of the value.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetValue() throws TestFailureException {
		JsonBoolean testBool = new JsonBoolean(true);
		TestSubject.assertTestCondition(testBool.getValue(), 
				String.format("The JSON boolean %s should hold the value %s, but holds %s.", 
						testBool, true, testBool.getValue()));
		testBool = new JsonBoolean(false);
		TestSubject.assertTestCondition(!testBool.getValue(), 
				String.format("The JSON boolean %s should hold the value %s, but holds %s.", 
						testBool, false, testBool.getValue()));
	}
	
	/**
	 * Test the conversion of the boolean to a JSON formatted string.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		JsonBoolean testBool = new JsonBoolean(true);
		TestSubject.assertTestCondition(testBool.toJson().equals(JsonBoolean.JSON_TRUE_VALUE), 
				String.format("The JSON representation of a true boolean "
						+ "should equal the value \"%s\", but is \"%s\".", 
						JsonBoolean.JSON_TRUE_VALUE, testBool.toJson()));
		testBool = new JsonBoolean(false);
		TestSubject.assertTestCondition(testBool.toJson().equals(JsonBoolean.JSON_FALSE_VALUE), 
				String.format("The JSON representation of a false boolean "
						+ "should equal the value \"%s\", but is \"%s\".", 
						JsonBoolean.JSON_FALSE_VALUE, testBool.toJson()));
	}
	
	/**
	 * Test parsing JSON formatted booleans.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonBoolean.parse(null);
			throw new TestFailureException("Parsing of null for a JSON boolean should fail.");
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON boolean should throw a "
					+ "NullPointerException.", e);
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test empty strings
		try {
			JsonBoolean.parse("");
			throw new TestFailureException("Parsing of empty for a JSON boolean should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test parsing the correct strings
		try {
			JsonBoolean testBool = JsonBoolean.parse(JsonBoolean.JSON_TRUE_VALUE);
			TestSubject.assertTestCondition(testBool.getValue(), 
					String.format("Parsing \"%s\" should result in a JSON %s, but restulted in %s.", 
							JsonBoolean.JSON_TRUE_VALUE, true, testBool));
		} catch (JsonStandardException e) {
			throw new TestFailureException(String.format("Parsing \"%s\" should result in a "
					+ " Json boolean", JsonBoolean.JSON_TRUE_VALUE), e);
		}
		try {
			JsonBoolean testBool = JsonBoolean.parse(JsonBoolean.JSON_FALSE_VALUE);
			TestSubject.assertTestCondition(!testBool.getValue(), 
					String.format("Parsing \"%s\" should result in a JSON %s, but restulted in %s.", 
							JsonBoolean.JSON_FALSE_VALUE, false, testBool));
		} catch (JsonStandardException e) {
			throw new TestFailureException(String.format("Parsing \"%s\" should result in a "
					+ " Json boolean", JsonBoolean.JSON_TRUE_VALUE), e);
		}
		// test random strings
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonBooleanTesting.RANDOM.nextInt(200)];
			JsonBooleanTesting.RANDOM.nextBytes(randomString);
			String testString = (new String(randomString)).replace(JsonBoolean.JSON_TRUE_VALUE, "")
					.replace(JsonBoolean.JSON_FALSE_VALUE, "");
			if (testString.length() > 0) {
				try {
					JsonBoolean parsedJsonBoolean = JsonBoolean.parse(testString);
					throw new TestFailureException(String.format("Parsing the string \"%s\" "
							+ "as JSON boolean should fail, but resulted into %s.", testString, 
							parsedJsonBoolean));
				} catch (JsonStandardException e) {
					/*
					 * Do nothing as this is expected behaviour.
					 */
				}
			}
		}
	}
	
	/**
	 * Test sequentially parsing JSON formatted booleans.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonBoolean.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON boolean should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			JsonBoolean[] jsonTestBools = new JsonBoolean[JsonBooleanTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestBools.length; j++) {
				jsonTestBools[j] = new JsonBoolean(JsonBooleanTesting.RANDOM.nextBoolean());
				testString += jsonTestBools[j].toJson() + "   ";
			}
			try {
				JsonParser jp = new JsonParser(testString);
				JsonBoolean[] parsedJsonBooleans = new JsonBoolean[jsonTestBools.length];
				for (int j = 0; j < jsonTestBools.length; j++) {
					parsedJsonBooleans[j] = JsonBoolean.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestBools, parsedJsonBooleans),
					String.format("The parsed JSON booleans %s should equal %s.", 
							Arrays.toString(parsedJsonBooleans), 
							Arrays.toString(jsonTestBools)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "booleans failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		String testString = JsonBoolean.JSON_TRUE_VALUE;
		// remove the last "e" so an exception will be raised
		testString = testString.substring(0, testString.length() - 1);
		try {
			JsonParser jp = new JsonParser(testString);
			int initialPosition = jp.getPosition();
			try {
				JsonBoolean.parseNext(jp);
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "boolean should fail, but resulted in the parser %s.", 
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
