package hockey.mask.test;

import java.util.Random;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonValue;
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
		JsonValueTesting.testParseString();
	}
	
	/**
	 * Test whether the parsing of JSON formatted strings works correctly.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	public static void testParseString() throws TestFailureException {
		String testString = null;
		String resultString = null;
		byte[] randomString = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
			JsonValueTesting.RANDOM.nextBytes(randomString);
			resultString = new String(randomString);
			// test well formed strings
			testString = JsonValue.JSON_STRING_IDENTIFIER + resultString
					+ JsonValue.JSON_STRING_IDENTIFIER;
			try {
				TestSubject.assertTestCondition(resultString.equals(JsonValue.parseString(
						testString)), String.format("The JSON string \"%s\" should be parsed "
								+ "as \"%s\", but is parsed as \"%s\".", testString, 
								resultString, JsonValue.parseString(testString)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(e);
			}
			// test malformed strings
			testString = testString.replaceAll(JsonValue.JSON_STRING_IDENTIFIER, "");
			try {
				JsonValue.parseString(testString);
				throw new TestFailureException(String.format("The string \"%s\" should "
						+ "not be JSON formatted and thereby cause an error.", testString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as malformed strings should cause an exception to be thrown.
				 */
			}
		}
		// test null strings
		try {
			JsonValue.parseString(null);
			throw new TestFailureException("A null string should "
					+ "not be JSON formatted and thereby cause an error.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as malformed strings should cause an exception to be thrown.
			 */
		}
	}

}
