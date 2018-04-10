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
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	public static void testConstructors() throws TestFailureException {
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

}
