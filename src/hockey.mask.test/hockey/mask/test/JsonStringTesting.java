package hockey.mask.test;

import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonString;
import hockey.mask.json.parser.JsonParser;
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
		JsonStringTesting.testParsing();
		JsonStringTesting.testParsingNext();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		// test JSON null strings
//		TestSubject.assertTestCondition(jsonTestString.toString() == null, 
//				String.format("A JSON null string should have the value null, but has %s.", 
//						jsonTestString.toString()));
//		TestSubject.assertTestCondition(jsonTestString.equals(eT), 
//				String.format("The JSON null string \"%s\" should equal the JSON null string \"%s\", "
//						+ "but does not.", jsonTestString, eT));
//		TestSubject.assertTestCondition(!jsonTestString.equals(null), 
//				String.format("The JSON null string \"%s\" should not equal the Java null value.", 
//						jsonTestString));
		try {
			new JsonString(null);
			throw new TestFailureException("A JSON formatted string cannot contain the value null.");
		} catch (NullPointerException e1) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonStringTesting.RANDOM.nextInt(200)];
			JsonStringTesting.RANDOM.nextBytes(randomString);
			String testString = new String(randomString);
			JsonString jsonTestString = new JsonString(testString);
			JsonString similarTestString = new JsonString(testString);
			TestSubject.assertTestCondition(testString.equals(jsonTestString.toString()), 
					String.format("The JSON string should hold the value %s, but has %s.", 
							testString, jsonTestString));
			TestSubject.assertTestCondition(jsonTestString.equals(similarTestString), 
					String.format("The JSON string \"%s\" should equal the JSON string \"%s\", "
							+ "but does not.", jsonTestString, similarTestString));
		}
	}
	
	/**
	 * Test the conversion of the Java string representation to a JSON formatted string.
	 * 
	 * @throws TestFailureException - the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			// create random substrings
			String testString = "";
			String perfectString = "";
			for (int j = 0; j < 100; j++) {
				if (JsonStringTesting.RANDOM.nextDouble() <= 0.2) { // add escape character
					int escapeIndex = JsonStringTesting.RANDOM.nextInt(JsonString.JSON_STRING_ESCAPED_CHARACTERS.length);
					testString = testString + JsonString.JSON_STRING_ESCAPED_CHARACTERS[escapeIndex][0];
					perfectString = perfectString + JsonString.JSON_STRING_ESCAPE_CHARACTER + JsonString.JSON_STRING_ESCAPED_CHARACTERS[escapeIndex][1];
				} else { // add substring
					/*
					 * Only select characters from the alphabet, so no untracked escaped 
					 * characters are added by accident and lead the test to fail.
					 */
					char randomString = (char) (JsonStringTesting.RANDOM.nextInt(26) + 97);
					testString = testString + randomString;
					perfectString = perfectString + randomString;
				}
			}
			perfectString = JsonString.JSON_STRING_IDENTIFIER + perfectString + JsonString.JSON_STRING_IDENTIFIER;
			JsonString jsonTestString = new JsonString(testString);
			TestSubject.assertTestCondition(perfectString.equals(jsonTestString.toJson()), 
					String.format("The JSON representation of the Java string \"%s\" "
							+ "should equal the value \"%s\", but is \"%s\".", 
							testString, perfectString, jsonTestString.toJson()));
			
		}
	}
	
	/**
	 * Test parsing JSON formatted strings.
	 * 
	 * @throws TestFailureException - the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonString.parse(null);
			throw new TestFailureException("Parsing of null for a JSON string should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonStringTesting.RANDOM.nextInt(200)];
			JsonStringTesting.RANDOM.nextBytes(randomString);
			String testString = new String(randomString);
			JsonString jsonTestString = new JsonString(testString);
			try {
				JsonString parsedJsonString = JsonString.parse(jsonTestString.toJson());
				TestSubject.assertTestCondition(jsonTestString.equals(parsedJsonString), 
					String.format("The parsed JSON string \"%s\" should equal \"%s\".", 
							parsedJsonString, jsonTestString));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "string failed.", testString
						), e);
			}
		}
	}
	
	/**
	 * Test sequentially parsing JSON formatted strings.
	 * 
	 * @throws TestFailureException - the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonString.parse(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			JsonString[] jsonTestStrings = new JsonString[JsonStringTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestStrings.length; j++) {
				// create random strings
				byte[] randomString = new byte[JsonStringTesting.RANDOM.nextInt(4)];
				JsonStringTesting.RANDOM.nextBytes(randomString);
				jsonTestStrings[j] = new JsonString(new String(randomString));
				testString += jsonTestStrings[j].toJson() + "   ";
			}
			try {
				JsonParser jp = new JsonParser(testString);
				JsonString[] parsedJsonStrings = new JsonString[jsonTestStrings.length];
				for (int j = 0; j < jsonTestStrings.length; j++) {
					parsedJsonStrings[j] = JsonString.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestStrings, parsedJsonStrings),
					String.format("The parsed JSON strings %s should equal %s.", 
							Arrays.toString(parsedJsonStrings), 
							Arrays.toString(jsonTestStrings)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "strings failed.", testString
						), e);
			}
		}
	}


}
