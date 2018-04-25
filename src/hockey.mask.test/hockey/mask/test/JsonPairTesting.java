package hockey.mask.test;

import java.util.Random;

import hockey.mask.json.JsonPair;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonValue;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonPairTesting class test the JsonPair class for correct functionality.
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
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		JsonValue testValue = null;
		String testName = null;
		byte[] randomString = null;
		JsonPair jp = null;
		JsonPair jjp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new String(randomString);
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new String(randomString));
			try {
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
				jjp = new JsonPair(testName + "test", testValue);
				TestSubject.assertTestCondition(!jp.equals(jjp), 
						String.format("The JSON pair %s should not equal the pair %s.",	jp, jjp));
				jjp = new JsonPair(testName, null);
				TestSubject.assertTestCondition(!jp.equals(jjp), 
						String.format("The JSON pair %s should not equal the pair %s.",	jp, jjp));
			} catch (JsonStandardException e) {
				throw new TestFailureException("The JSON standard was violated.", e);
			}
			try {
				jp = new JsonPair(null ,testValue);
				throw new TestFailureException("An exception should have been thrown as "
						+ "the JSON standard has been violated.");
			} catch (JsonStandardException e) {
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
		String testName = "";
		byte[] randomString = null;
		JsonPair jp = null;
		try {
			jp = new JsonPair(testName, testValue);
		} catch (JsonStandardException e1) {
			throw new TestFailureException(String.format("The JSON standard has been violated "
					+ "by trying to create a JSON pair with name \"%s\" and value \"%s\"", testName, 
					testValue), e1);
		}
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new String(randomString);
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new String(randomString));
			try {
				jp.setName(testName);
				jp.setValue(testValue);
				TestSubject.assertTestCondition(jp.getName().equals(testName), 
						String.format("The JSON pair %s should have the name \"%s\", "
								+ "but has the name \"%s\".", jp, testName, jp.getName()));
				TestSubject.assertTestCondition(jp.getNameAsJson().equals(JsonValue.stringToJson(testName)), 
						String.format("The JSON pair %s should have the json name string \"%s\", "
								+ "but has the name string \"%s\".", jp, 
								JsonValue.stringToJson(testName), jp.getNameAsJson()));
				TestSubject.assertTestCondition(jp.getValue().equals(testValue), 
						String.format("The JSON pair %s should have the value \"%s\", "
								+ "but has the value \"%s\".", jp, testValue, jp.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException("The JSON standard was violated.", e);
			}
			try {
				jp.setName(null);
				throw new TestFailureException("An exception should have been thrown as "
						+ "the JSON standard has been violated.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			}
		}
	}
	
	/**
	 * Test the parsing of JSON formatted strings to JSON pairs.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		JsonValue testValue = null;
		String testName = "";
		byte[] randomString = null;
		JsonPair jp = null;
		JsonPair jjp = null;
		try {
			jp = new JsonPair(testName, testValue);
		} catch (JsonStandardException e1) {
			throw new TestFailureException(String.format("The JSON standard has been violated "
					+ "by trying to create a JSON pair with name \"%s\" and value \"%s\"", testName, 
					testValue), e1);
		}
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonPairTesting.RANDOM.nextInt(200)];
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testName = new String(randomString);
			JsonPairTesting.RANDOM.nextBytes(randomString);
			testValue = new JsonValue(new String(randomString));
			try {
				jp.setName(testName);
				jp.setValue(testValue);
				jjp = JsonPair.parse(jp.toJson());
				TestSubject.assertTestCondition(jp.equals(jjp), 
						String.format("The JSON pair %s should equal the pair %s parsed from "
								+ "the JSON formatted string \"%s\".",	jp, jjp, jp.toJson()));
			} catch (JsonStandardException e) {
				throw new TestFailureException("The JSON standard was violated.", e);
			}
		}
	}
	
}
