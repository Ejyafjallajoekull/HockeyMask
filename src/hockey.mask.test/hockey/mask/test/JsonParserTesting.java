package hockey.mask.test;

import java.util.Random;

import hockey.mask.json.parser.JsonParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonPareserTesting class tests the JsonParser class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonParserTesting implements TestSubject {
	
	private static final Random RANDOM = new Random();

	@Override
	public void runAllTests() throws TestFailureException {
		JsonParserTesting.testConstructors();
		JsonParserTesting.testSettingGetting();
		JsonParserTesting.testNext();
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		String testString = null;
		byte[] randomString = null;
		JsonParser jp = null;
		JsonParser jjp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonParserTesting.RANDOM.nextInt(200)];
			JsonParserTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			if (testString.length() > 0) {
				jp = new JsonParser(testString);
				jjp = new JsonParser(jp.getData());
				TestSubject.assertTestCondition(jp.equals(jjp), 
						String.format("The JSON parser %s should equal the parser %s.",	jp, jjp));
				jjp = new JsonParser(testString + "test");
				TestSubject.assertTestCondition(!jp.equals(jjp), 
						String.format("The JSON parser %s should not equal the pair %s.",	jp, jjp));

			} else {
				try {
					jp = new JsonParser(testString);
					throw new TestFailureException("An exception should have been thrown as \""
							+ testString + "\" is no valid input for a JSON parser.");
				} catch (IllegalArgumentException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			}
			try {
				jp = new JsonParser(null);
				throw new TestFailureException("An exception should have been thrown as "
						+ "null is no valid input for a JSON parser.");
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
		String testString = null;
		byte[] randomString = null;
		JsonParser jp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonParserTesting.RANDOM.nextInt(200)];
			JsonParserTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			if (testString.length() > 0) {
				jp = new JsonParser(testString);
				TestSubject.assertTestCondition(jp.getPosition() == 0, 
						String.format("The newly initialised JSON parser %s should start at position "
								+ "%s, but starts at %s.", jp, 0, jp.getPosition()));
				TestSubject.assertTestCondition(jp.getData().equals(testString), 
						String.format("The JSON parsers data %s should contain the data \"%s\", "
								+ "but contains \"%s\" instead.", jp, testString, jp.getData()));
				int pos = JsonParserTesting.RANDOM.nextInt(testString.length());
				jp.setPosition(pos);
				TestSubject.assertTestCondition(jp.getPosition() == pos, 
						String.format("The JSON parser %s should be at position "
								+ "%s, but is at %s.", jp, pos, jp.getPosition()));
				// test greater position mark than length of the data
				try {
					jp.setPosition(testString.length() + 1);
					throw new TestFailureException(String.format("An exception should have been thrown as "
							+ "the position mark %s is outside of the JSON parsers %s data bounds.", pos, jp));
				} catch (IndexOutOfBoundsException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
				// test negative position marks
				try {
					pos = (JsonParserTesting.RANDOM.nextInt() - 1) * -1;
					jp.setPosition(pos);
					throw new TestFailureException(String.format("An exception should have been thrown as "
							+ "the position mark %s set for the JSON parser %s is negative.", pos, jp));
				} catch (IndexOutOfBoundsException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			} else { // empty strings should throw an exception
				try {
					jp = new JsonParser(testString);
					throw new TestFailureException("An exception should have been thrown as \""
							+ testString + "\" is no valid input for a JSON parser.");
				} catch (IllegalArgumentException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			}
		}
	}
	
	/**
	 * Test the retrieval of characters from the parsed string.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testNext() throws TestFailureException {
		String testString = null;
		String perfectString = null;
		byte[] randomString = null;
		JsonParser jp = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonParserTesting.RANDOM.nextInt(200)];
			JsonParserTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			if (testString.length() > 0) {
				jp = new JsonParser(testString);
				// test parsing character after character and retrieving remaining characters
				perfectString = "";
				TestSubject.assertTestCondition(jp.getRemaining().equals(testString), 
						String.format("The remaining characters of the data parsed by the "
								+ "JSON parser %s should read \"%s\", "
								+ "but read \"%s\" instead.", jp, testString, jp.getRemaining()));
				int pos = 0;
				while (jp.hasNext()) {
					perfectString += jp.get();
					pos++;
					TestSubject.assertTestCondition(jp.getPosition() == pos, 
							String.format("The JSON parser %s should be at position "
									+ "%s, but is at %s.", jp, pos, jp.getPosition()));
					TestSubject.assertTestCondition(jp.getRemaining().equals(testString.substring(pos)), 
							String.format("The remaining characters of the data parsed by the "
									+ "JSON parser %s should read \"%s\", "
									+ "but read \"%s\" instead.", jp, testString, jp.getRemaining()));

				}
				TestSubject.assertTestCondition(perfectString.equals(testString), 
						String.format("The JSON parsers data %s should read the data \"%s\", "
								+ "but reads \"%s\" instead.", jp, testString, perfectString));
				// test rewinding the parser to the first index
				jp.rewind();
				TestSubject.assertTestCondition(jp.getPosition() == 0, 
						String.format("The rewinded JSON parser %s should start at position "
								+ "%s, but starts at %s.", jp, 0, jp.getPosition()));
				// test empty get
				perfectString = jp.get(0);
				TestSubject.assertTestCondition(perfectString.equals(""), 
						String.format("Retrieving a substring of length zero from the JSON parser "
								+ "%s should yield the empty string \"%s\", "
								+ "but reads \"%s\" instead.", jp, "", perfectString));
				// get with length greater than 1
				pos = JsonParserTesting.RANDOM.nextInt(testString.length()+1);
				jp.setPosition(pos); // randomly selected start position
				int length = JsonParserTesting.RANDOM.nextInt(testString.length()); // randomly selected length
				String retrievedString = null;
				try {
					retrievedString = jp.get(length);
					perfectString = testString.substring(pos, pos+length);
					TestSubject.assertTestCondition(jp.getPosition() == pos+length, 
							String.format("The JSON parser %s should be at position "
									+ "%s, but is at %s.", jp, pos+length, jp.getPosition()));
					TestSubject.assertTestCondition(perfectString.equals(retrievedString), 
							String.format("Retrieving a substring of length %s from the JSON parser "
									+ "%s should yield the string \"%s\", but reads \"%s\" instead.", 
									length, jp, perfectString, retrievedString));
					//TODO: test negative values
					// test isNext()
					jp.setPosition(pos);
					TestSubject.assertTestCondition(jp.isNext(retrievedString), 
							String.format("The JSON parsers %s next sequence characters should be "
									+ "\"%s\".", jp, retrievedString));
					
					// TODO: test false
				} catch (IndexOutOfBoundsException e) {
					if (pos + length > jp.getData().length()) {
						/*
						 * Do nothing as this is the expected behaviour.
						 */
					} else {
						throw e;
					}
				}
				
			} else { // empty strings should throw an exception
				try {
					jp = new JsonParser(testString);
					throw new TestFailureException("An exception should have been thrown as \""
							+ testString + "\" is no valid input for a JSON parser.");
				} catch (IllegalArgumentException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			}
		}
	}

}