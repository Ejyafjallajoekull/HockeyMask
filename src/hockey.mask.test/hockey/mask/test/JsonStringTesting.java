package hockey.mask.test;

import java.util.Random;

import hockey.mask.json.JsonString;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonStringTesting class test the JsonString class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonStringTesting implements TestSubject {

	private static final Random RANDOM = new Random();
	
	@Override
	public void runAllTests() throws TestFailureException {
		JsonStringTesting.testConstructors();
		JsonStringTesting.testToJson();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		JsonString jsonTestString = new JsonString(null);
		JsonString eT = new JsonString(null);
		// test JSON null strings
		TestSubject.assertTestCondition(jsonTestString.toString() == null, 
				String.format("A JSON null string should have the value null, but has %s.", 
						jsonTestString.toString()));
		TestSubject.assertTestCondition(jsonTestString.equals(eT), 
				String.format("The JSON null string \"%s\" should equal the JSON null string \"%s\", "
						+ "but does not.", jsonTestString, eT));
		TestSubject.assertTestCondition(!jsonTestString.equals(null), 
				String.format("The JSON null string \"%s\" should not equal the Java null value.", 
						jsonTestString));
		// test JSON string
		String testString = null;
		byte[] randomString = null;
		jsonTestString = new JsonString(testString);
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonStringTesting.RANDOM.nextInt(200)];
			JsonStringTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			jsonTestString = new JsonString(testString);
			eT = new JsonString(testString);
			TestSubject.assertTestCondition(testString.equals(jsonTestString.toString()), 
					String.format("The JSON string should hold the value %s, but has %s.", 
							testString, jsonTestString));
			TestSubject.assertTestCondition(jsonTestString.equals(eT), 
					String.format("The JSON string \"%s\" should equal the JSON string \"%s\", "
							+ "but does not.", jsonTestString, eT));
		}
	}
	
	/**
	 * Test the conversion of the Java string representation to a JSON formatted string.
	 * 
	 * @throws TestFailureException - the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		JsonString jsonTestString = new JsonString(null);
		String perfectString = null;
		// test JSON string
		String testString = null;
		char randomString = 0;
		jsonTestString = new JsonString(testString);
		int escapeIndex = 0;
		for (int i = 0; i < 10000; i++) {
			// create random substrings
			testString = "";
			perfectString = "";
			for (int j = 0; j < 100; j++) {
				if (JsonStringTesting.RANDOM.nextDouble() <= 0.2) { // add escape character
					escapeIndex = JsonStringTesting.RANDOM.nextInt(JsonString.JSON_STRING_ESCAPED_CHARACTERS.length);
					testString = testString + JsonString.JSON_STRING_ESCAPED_CHARACTERS[escapeIndex][0];
					perfectString = perfectString + JsonString.JSON_STRING_ESCAPE_CHARACTER + JsonString.JSON_STRING_ESCAPED_CHARACTERS[escapeIndex][1];
//					System.out.println(perfectString);
//					System.out.println(JsonString.JSON_STRING_ESCAPE_CHARACTER + JsonString.JSON_STRING_ESCAPED_CHARACTERS[escapeIndex][1]);
				} else { // add substring
					/*
					 * Only select characters from the alphabet, so no untracked escaped 
					 * characters are added by accident and lead the test to fail.
					 */
					randomString = (char) (JsonStringTesting.RANDOM.nextInt(26) + 97);
					testString = testString + randomString;
					perfectString = perfectString + randomString;
				}
			}
			perfectString = JsonString.JSON_STRING_IDENTIFIER + perfectString + JsonString.JSON_STRING_IDENTIFIER;
			jsonTestString = new JsonString(testString);
			TestSubject.assertTestCondition(perfectString.equals(jsonTestString.toJson()), 
					String.format("The JSON representation of the Java string \"%s\" "
							+ "should equal the value \"%s\", but is \"%s\".", 
							testString, perfectString, jsonTestString.toJson()));
			
		}
	}

}
