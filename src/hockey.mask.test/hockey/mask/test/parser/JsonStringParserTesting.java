package hockey.mask.test.parser;

import java.util.Random;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonStringParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonStringParserTesting class tests the JsonStringParser class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonStringParserTesting implements TestSubject {

	private static final Random RANDOM = new Random();

	@Override
	public void runAllTests() throws TestFailureException {
		JsonStringParserTesting.testConstructors();
		JsonStringParserTesting.testSettingGetting();
		JsonStringParserTesting.testNext();
		JsonStringParserTesting.testSkipWhitespace();
		JsonStringParserTesting.testIsNextDigit();
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonStringParserTesting.RANDOM.nextInt(200)];
			JsonStringParserTesting.RANDOM.nextBytes(randomString);
			String testString = new String(randomString);
			if (testString.length() > 0) {
				try {
					JsonStringParser jp = new JsonStringParser(testString);
					JsonStringParser jjp = new JsonStringParser(jp.getData());
					TestSubject.assertTestCondition(jp.equals(jjp), 
							String.format("The JSON parser %s should equal the parser %s.",	jp, jjp));
					jjp = new JsonStringParser(testString + "test");
					TestSubject.assertTestCondition(!jp.equals(jjp), 
							String.format("The JSON parser %s should not equal the parser %s.",	jp, jjp));
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parsers failed.", e);
				}
			} else {
				try {
					new JsonStringParser(testString);
					throw new TestFailureException("An exception should have been thrown as \""
							+ testString + "\" is no valid input for a JSON parser.");
				} catch (JsonStandardException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			}
			// test empty string
			try {
				new JsonStringParser("");
				throw new TestFailureException("An exception should have been thrown as \""
						+ "" + "\" is no valid input for a JSON parser.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			}
			// test null
			try {
				new JsonStringParser(null);
				throw new TestFailureException("An exception should have been thrown as "
						+ "null is no valid input for a JSON parser.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			} catch (JsonStandardException e) {
				throw new TestFailureException("A NullPointerException should be thrown upon "
						+ "passing null to a JSON parser.", e);
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
			byte[] randomString = new byte[JsonStringParserTesting.RANDOM.nextInt(200)];
			JsonStringParserTesting.RANDOM.nextBytes(randomString);
			String testString = new String(randomString);
			if (testString.length() > 0) {
				try {
					JsonStringParser jp = new JsonStringParser(testString);
					TestSubject.assertTestCondition(jp.getPosition() == 0, 
							String.format("The newly initialised JSON parser %s should start at position "
									+ "%s, but starts at %s.", jp, 0, jp.getPosition()));
					TestSubject.assertTestCondition(jp.getData().equals(testString), 
							String.format("The JSON parsers data %s should contain the data \"%s\", "
									+ "but contains \"%s\" instead.", jp, testString, jp.getData()));
					int pos = JsonStringParserTesting.RANDOM.nextInt(testString.length());
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
						pos = (JsonStringParserTesting.RANDOM.nextInt(3000000) + 1) * -1;
						jp.setPosition(pos);
						throw new TestFailureException(String.format("An exception should have been thrown as "
								+ "the position mark %s set for the JSON parser %s is negative.", pos, jp));
					} catch (IndexOutOfBoundsException e) {
						/*
						 * Do nothing as this is the expected behaviour.
						 */
					}
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			} else { // empty strings should throw an exception
				try {
					new JsonStringParser(testString);
					throw new TestFailureException("An exception should have been thrown as \""
							+ testString + "\" is no valid input for a JSON parser.");
				} catch (JsonStandardException e) {
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
		for (int i = 0; i < 10000; i++) {
			// create random strings
			byte[] randomString = new byte[JsonStringParserTesting.RANDOM.nextInt(200)];
			JsonStringParserTesting.RANDOM.nextBytes(randomString);
			String testString = new String(randomString);
			if (testString.length() > 0) {
				try {
					JsonStringParser jp = new JsonStringParser(testString);
					// test parsing character after character and retrieving remaining characters
					String perfectString = "";
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
					pos = JsonStringParserTesting.RANDOM.nextInt(testString.length()+1);
					jp.setPosition(pos); // randomly selected start position
					int length = JsonStringParserTesting.RANDOM.nextInt(testString.length()); // randomly selected length
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
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			} else { // empty strings should throw an exception
				try {
					new JsonStringParser(testString);
					throw new TestFailureException("An exception should have been thrown as \""
							+ testString + "\" is no valid input for a JSON parser.");
				} catch (JsonStandardException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			}
		}
	}

	/**
	 * Test skipping whitespaces.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testSkipWhitespace() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test strings only consisting of whitespace
			String whitespaceString = JsonStringParserTesting.createRandomWhitespaceSequence();
			if (whitespaceString.length() > 0) { // otherwise an exception will be thrown
				try {
					JsonStringParser whiteSpaceParser = new JsonStringParser(whitespaceString);
					whiteSpaceParser.skipWhitespace();
					TestSubject.assertTestCondition(whiteSpaceParser.getPosition() == whitespaceString.length(), 
							String.format("The JSON parser %s should be at position "
									+ "%s after skiping all whitespaces, but is at %s.", 
									whiteSpaceParser, whitespaceString.length(), whiteSpaceParser.getPosition()));
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			}
			// test strings without whitespace
			String stringWithoutWhitespace = JsonStringParserTesting.createRandomSequenceWithoutWhitespace();
			if (stringWithoutWhitespace.length() > 0) { // otherwise an exception will be thrown
				try {
					JsonStringParser noWhiteSpaceParser = new JsonStringParser(stringWithoutWhitespace);
					noWhiteSpaceParser.skipWhitespace();
					TestSubject.assertTestCondition(noWhiteSpaceParser.getPosition() == 0, 
							String.format("The JSON parser %s should be at position "
									+ "%s after skipping all whitespaces, but is at %s.", 
									noWhiteSpaceParser, 0, noWhiteSpaceParser.getPosition()));	
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			}
			// test normal strings
			String testString = JsonStringParserTesting.createRandomSequenceWithoutWhitespace();
			int whiteSpaceStart = testString.length();
			testString += JsonStringParserTesting.createRandomWhitespaceSequence();
			int whiteSpaceEnd = testString.length();
			testString += JsonStringParserTesting.createRandomSequenceWithoutWhitespace();
			if (testString.length() > 0) { // otherwise an exception will be thrown
				try {
					JsonStringParser jp = new JsonStringParser(testString);
					jp.setPosition(whiteSpaceStart);
					jp.skipWhitespace();
					TestSubject.assertTestCondition(jp.getPosition() == whiteSpaceEnd, 
							String.format("The JSON parser %s should be at position "
									+ "%s after skipping all whitespaces, but is at %s.", 
									jp, whiteSpaceEnd, jp.getPosition()));	
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			}
		}
	}
	
	/**
	 * Test detecting if the next character is a digit.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testIsNextDigit() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test strings only consisting of digits
			String digitString = JsonStringParserTesting.createRandomDigitSequence();
			if (digitString.length() > 0) { // otherwise an exception will be thrown
				try {
					JsonStringParser digitParser = new JsonStringParser(digitString);
					for (int j = 0; j < digitString.length(); j++) {
						char nextChar = digitParser.get();
						TestSubject.assertTestCondition(Character.isDigit(nextChar), 
								String.format("The parsed char in the JSON parser %s should be a digit "
										+ ", but is \"%s\".", 
										digitParser, nextChar));
					}	
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			}
			// test strings without digits
			String stringWithoutDigits = JsonStringParserTesting.createRandomSequenceWithoutDigits();
			if (stringWithoutDigits.length() > 0) { // otherwise an exception will be thrown
				try {
					JsonStringParser noDigitsParser = new JsonStringParser(stringWithoutDigits);
					for (int j = 0; j < stringWithoutDigits.length(); j++) {
						char nextChar = noDigitsParser.get();
						TestSubject.assertTestCondition(!Character.isDigit(nextChar), 
								String.format("The parsed char in the JSON parser %s should not be a digit "
										+ ", but is \"%s\".", 
										noDigitsParser, nextChar));
					}	
				} catch (JsonStandardException e) {
					throw new TestFailureException("Creating the JSON parser failed.", e);
				}
			}
		}
	}
	
	/**
	 * Helper function to create a string only containing random whitespaces.
	 */
	private static String createRandomWhitespaceSequence() {
		/*
		 * This string creation method is heavily biased against empty strings, but for the sake
		 * of testing it should be fine.
		 */
		byte[] randomString = new byte[JsonStringParserTesting.RANDOM.nextInt(1000)];
		JsonStringParserTesting.RANDOM.nextBytes(randomString);
		String testString = new String(randomString);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < testString.length(); i++) {
			if (Character.isWhitespace(testString.charAt(i))) {
				sb.append(testString.charAt(i));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Helper function to create a random string without any whitespaces.
	 */
	private static String createRandomSequenceWithoutWhitespace() {
		/*
		 * This creation method is biased against empty strings, but for the sake
		 * of testing it should be fine.
		 */
		byte[] randomString = new byte[JsonStringParserTesting.RANDOM.nextInt(300)];
		JsonStringParserTesting.RANDOM.nextBytes(randomString);
		String testString = new String(randomString);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < testString.length(); i++) {
			if (!Character.isWhitespace(testString.charAt(i))) {
				sb.append(testString.charAt(i));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Helper function to create a string only containing random digits.
	 */
	private static String createRandomDigitSequence() {
		int length = JsonStringParserTesting.RANDOM.nextInt(200);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			sb.append(JsonStringParserTesting.RANDOM.nextInt(10));
		}
		return sb.toString();
	}
	
	/**
	 * Helper function to create a random string without any digits.
	 */
	private static String createRandomSequenceWithoutDigits() {
		byte[] randomString = new byte[JsonStringParserTesting.RANDOM.nextInt(300)];
		JsonStringParserTesting.RANDOM.nextBytes(randomString);
		String testString = new String(randomString);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < testString.length(); i++) {
			if (!Character.isDigit(testString.charAt(i))) {
				sb.append(testString.charAt(i));
			}
		}
		return sb.toString();
	}
	
}
