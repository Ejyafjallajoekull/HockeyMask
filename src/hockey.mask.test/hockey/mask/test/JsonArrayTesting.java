package hockey.mask.test;

import java.util.Random;

import hockey.mask.json.JsonArray;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonString;
import hockey.mask.json.JsonString.JsonStringLinkedList;
import hockey.mask.json.JsonValue;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonArrayTesting class test the JsonArray class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonArrayTesting implements TestSubject {
	
	private static final Random RANDOM = new Random();

	@Override
	public void runAllTests() throws TestFailureException {
		JsonArrayTesting.testToJson();
		JsonArrayTesting.testParsing();
	}
	
	/**
	 * Test the parsing of JSON formatted arrays to JSON arrays.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		JsonValue testValue = null;
		byte[] randomString = null;
		JsonArray jp = null;
		JsonArray jjp = null;
		int numberOfElements = 0;
		for (int i = 0; i < 10000; i++) {
			jp = new JsonArray();
			numberOfElements = JsonArrayTesting.RANDOM.nextInt(200);
			for (int j = 0; j < numberOfElements; j++) {
				// create random strings
				randomString = new byte[JsonArrayTesting.RANDOM.nextInt(200)];
				JsonArrayTesting.RANDOM.nextBytes(randomString);
				testValue = new JsonValue(new JsonString(new String(randomString)));
				jp.add(testValue);
			}
			try {
				jjp = JsonArray.parse(jp.toJson());
				TestSubject.assertTestCondition(jp.equals(jjp), 
						String.format("The JSON array %s should equal the array %s parsed from "
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
		JsonArray testArray = null;
		int numberOfValues = 0;
		String perfectString = null;
		double randomNumber = 0.0d;
		for (int i = 0; i < 10000; i++) {
			// draw random number of array values
			numberOfValues = JsonArrayTesting.RANDOM.nextInt(200);
			testArray = new JsonArray();
			perfectString = JsonArray.JSON_ARRAY_START_IDENTIFIER;
			for (int j = 0; j < numberOfValues; j++) {
				randomNumber = JsonArrayTesting.RANDOM.nextDouble();
				// do not set them to be the same object to add another layer of testing
				testArray.add(new JsonValue(randomNumber));
				perfectString += (new JsonValue(randomNumber)).toJson();
				if (j < numberOfValues - 1) {
					perfectString += JsonArray.JSON_ARRAY_VALUE_SEPARATOR;
				}
			}
			perfectString += JsonArray.JSON_ARRAY_END_IDENTIFIER;
			TestSubject.assertTestCondition(testArray.toJson().equals(perfectString), 
					String.format("The JSON array's JSON representation should be \"%s\", "
							+ "but is \"%s\".",	perfectString, testArray.toJson()));
		}		
	}

}
