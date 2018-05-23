package hockey.mask.test;

import java.util.Random;

import hockey.mask.json.JsonPair;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonString;
import hockey.mask.json.JsonValue;
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
		JsonPairTesting.testParsing();
		JsonPairTesting.testToJson();
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		JsonValue testValue = null;
		JsonString testName = null;
		byte[] randomString = null;
		JsonPair jp = null;
		JsonPair jjp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new JsonString(new String(randomString)));
			jp = new JsonPair(testName, testValue);
			jjp = new JsonPair(jp.getName(), jp.getValue());
			TestSubject.assertTestCondition(jp.getName().equals(testName), 
					String.format("The JSON pair %s should have the name \"%s\", "
							+ "but has the name \"%s\".", jp, testName, jp.getName()));
			TestSubject.assertTestCondition(jp.getValue().equals(testValue), 
					String.format("The JSON pair %s should have the value \"%s\", "
							+ "but has the value \"%s\".", jp, testValue, jp.getValue()));
			
			TestSubject.assertTestCondition(jp.equals(jjp), 
					String.format("The JSON pair %s should equal the pair %s.",	jp, jjp));
			jjp = new JsonPair(new JsonString(testName.toString() + "test"), testValue);
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
		JsonValue testValue = null;
		JsonString testName = null;
		byte[] randomString = null;
		JsonPair jp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new JsonString(new String(randomString)));
//				jp.setName(testName); // immutable
			jp = new JsonPair(testName, null);
			jp.setValue(testValue);
			TestSubject.assertTestCondition(jp.getName().equals(testName), 
					String.format("The JSON pair %s should have the name \"%s\", "
							+ "but has the name \"%s\".", jp, testName, jp.getName()));
			TestSubject.assertTestCondition(jp.getValue().equals(testValue), 
					String.format("The JSON pair %s should have the value \"%s\", "
							+ "but has the value \"%s\".", jp, testValue, jp.getValue()));
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
		JsonValue testValue = null;
		JsonString testName = null;
		byte[] randomString = null;
		JsonPair jp = null;
		JsonPair jjp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new JsonString(new String(randomString)));
			jp = new JsonPair(testName, testValue);
			try {
				jjp = JsonPair.parse(jp.toJson());
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
		JsonValue testValue = null;
		JsonString testName = null;
		byte[] randomString = null;
		JsonPair jp = null;
		String perfectString = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new JsonString(new String(randomString));
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new JsonString(new String(randomString)));
			jp = new JsonPair(testName, testValue);
			perfectString = testName.toJson() + JsonPair.JSON_PAIR_SEPARATOR + testValue.toJson();
			TestSubject.assertTestCondition(jp.toJson().equals(perfectString), 
					String.format("The JSON pair's JSON representation should be \"%s\", "
							+ "but is \"%s\".",	perfectString, jp.toJson()));
		}		
	}
	
}
