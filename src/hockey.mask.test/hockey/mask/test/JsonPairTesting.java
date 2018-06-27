package hockey.mask.test;

import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonNull;
import hockey.mask.json.JsonNumber;
import hockey.mask.json.JsonPair;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonString;
import hockey.mask.json.JsonValue;
import hockey.mask.json.parser.JsonParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonPairTesting class tests the JsonPair class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonPairTesting implements TestSubject {

	private static final Random RANDOM = new Random();

	@Override
	public void runAllTests() throws TestFailureException {
		JsonPairTesting.testConstructors();
		JsonPairTesting.testSettingGetting();
		JsonPairTesting.testToJson();
		JsonPairTesting.testParsing();
		JsonPairTesting.testParsingNext();
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonString testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonValue testValue = new JsonString(new String(randomString));
			JsonPair jp = new JsonPair(testName, testValue);
			JsonPair jjp = new JsonPair(jp.getName(), jp.getValue());
			TestSubject.assertTestCondition(jp.getName().equals(testName), 
					String.format("The JSON pair %s should have the name \"%s\", "
							+ "but has the name \"%s\".", jp, testName, jp.getName()));
			TestSubject.assertTestCondition(jp.getValue().equals(testValue), 
					String.format("The JSON pair %s should have the value \"%s\", "
							+ "but has the value \"%s\".", jp, testValue, jp.getValue()));
			TestSubject.assertTestCondition(jp.equals(jjp), 
					String.format("The JSON pair %s should equal the pair %s.",	jp, jjp));
			jjp = new JsonPair(new JsonString(testName.getValue() + "test"), testValue);
			TestSubject.assertTestCondition(!jp.equals(jjp), 
					String.format("The JSON pair %s should not equal the pair %s.",	jp, jjp));
			jjp = new JsonPair(testName, null);
			TestSubject.assertTestCondition(!jp.equals(jjp), 
					String.format("The JSON pair %s should not equal the pair %s.",	jp, jjp));
			try {
				jp = new JsonPair(null ,testValue);
				throw new TestFailureException("An exception should have been thrown as "
						+ "null is no JSON formatted string.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			}
		}
	}
	
	/**
	 * Test the getters and setters.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testSettingGetting() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonString testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonValue testValue = new JsonString(new String(randomString));
//				jp.setName(testName); // immutable
			// test null values
			JsonPair jp = new JsonPair(testName, null);
			JsonValue someNull = new JsonNull();
			TestSubject.assertTestCondition(jp.getName().equals(testName), 
					String.format("The JSON pair %s should have the name \"%s\", "
							+ "but has the name \"%s\".", jp, testName, jp.getName()));
			TestSubject.assertTestCondition(jp.getValue().equals(someNull), 
					String.format("The JSON pair %s should have the value \"%s\", "
							+ "but has the value \"%s\".", jp, testValue, jp.getValue()));
			// test actual values
			jp.setValue(testValue);
			TestSubject.assertTestCondition(jp.getName().equals(testName), 
					String.format("The JSON pair %s should have the name \"%s\", "
							+ "but has the name \"%s\".", jp, testName, jp.getName()));
			TestSubject.assertTestCondition(jp.getValue().equals(testValue), 
					String.format("The JSON pair %s should have the value \"%s\", "
							+ "but has the value \"%s\".", jp, testValue, jp.getValue()));
			// test setting null
			jp.setValue(null);
			TestSubject.assertTestCondition(jp.getName().equals(testName), 
					String.format("The JSON pair %s should have the name \"%s\", "
							+ "but has the name \"%s\".", jp, testName, jp.getName()));
			TestSubject.assertTestCondition(jp.getValue().equals(someNull), 
					String.format("The JSON pair %s should have the value \"%s\", "
							+ "but has the value \"%s\".", jp, someNull, jp.getValue()));
			// name is now immutable
//			try {
//				jp.setName(null);
//				throw new TestFailureException("An exception should have been thrown as "
//						+ "the JSON standard has been violated.");
//			} catch (JsonStandardException e) {
//				/*
//				 * Do nothing as this is the expected behaviour.
//				 */
//			}
		}
	}
	
	/**
	 * Test the parsing of JSON formatted strings to JSON pairs.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonPair.parse(null);
			throw new TestFailureException("Parsing of null for a JSON pair should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON pair should throw a "
					+ "NullPointerException.", e);
		}
		// test empty strings
		try {
			JsonPair.parse("");
			throw new TestFailureException("Parsing of empty for a JSON pair should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonString testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonValue testValue = new JsonString(new String(randomString));
			JsonPair jp = new JsonPair(testName, testValue);
			try {
				JsonPair jjp = JsonPair.parse(jp.toJson());
				TestSubject.assertTestCondition(jp.equals(jjp), 
						String.format("The JSON pair %s should equal the pair %s parsed from "
								+ "the JSON formatted string \"%s\".",	jp, jjp, jp.toJson()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("The string \"%s\" violates the JSON standard.", 
						jp.toJson()), e);
			}
		}
	}
		
	/**
	 * Test conversion to the JSON format.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonString testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonValue testValue = new JsonString(new String(randomString));
			JsonPair jp = new JsonPair(testName, testValue);
			String perfectString = testName.toJson() + JsonPair.JSON_PAIR_SEPARATOR + testValue.toJson();
			TestSubject.assertTestCondition(jp.toJson().equals(perfectString), 
					String.format("The JSON pair's JSON representation should be \"%s\", "
							+ "but is \"%s\".",	perfectString, jp.toJson()));
		}		
	}
	
	/**
	 * Test sequentially parsing JSON formatted pairs.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonPair.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON pair should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON pairs
		for (int i = 0; i < 10000; i++) {
			JsonPair[] jsonTestPairs = new JsonPair[JsonPairTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestPairs.length; j++) {
				// create random strings
				byte[] randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
				JsonPairTesting.RANDOM.nextBytes(randomString);
				jsonTestPairs[j] = new JsonPair(new JsonString(new String(randomString)), 
						new JsonNumber(JsonPairTesting.RANDOM.nextInt()));
				testString += jsonTestPairs[j].toJson() + "   ";
			}
			try {
				JsonParser jp = new JsonParser(testString);
				JsonPair[] parsedJsonStrings = new JsonPair[jsonTestPairs.length];
				for (int j = 0; j < jsonTestPairs.length; j++) {
					parsedJsonStrings[j] = JsonPair.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestPairs, parsedJsonStrings),
					String.format("The parsed JSON pairs %s should equal %s.", 
							Arrays.toString(parsedJsonStrings), 
							Arrays.toString(jsonTestPairs)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "pairs failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			JsonPair jsonTestString = new JsonPair(new JsonString(new String(randomString)), new JsonNull());
			String testString = jsonTestString.toJson();
			// remove the separator
			testString = testString.replace(JsonPair.JSON_PAIR_SEPARATOR, "");
			try {
				JsonParser jp = new JsonParser(testString);
				int initialPosition = jp.getPosition();
				try {
					JsonPair.parseNext(jp);
					throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
							+ "pairs should fail, but resulted in the parser %s.", 
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
	
}
