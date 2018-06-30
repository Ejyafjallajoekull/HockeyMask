package hockey.mask.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import hockey.mask.json.parser.JsonParserInternalException;
import hockey.mask.json.parser.JsonStreamParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonStreamParserTesting class tests the JsonStreamParser class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonStreamParserTesting implements TestSubject {

	private static final Random RANDOM = new Random();
	
	@Override
	public void runAllTests() throws TestFailureException {
		JsonStreamParserTesting.testConstructors();
		JsonStreamParserTesting.testGetData();
		JsonStreamParserTesting.testPosition();
		JsonStreamParserTesting.testRewind();
		JsonStreamParserTesting.testHasNext();
		JsonStreamParserTesting.testIsNext();
		JsonStreamParserTesting.testGet();
		JsonStreamParserTesting.testGetRemaining();
		JsonStreamParserTesting.testSkipWhitespace();
		JsonStreamParserTesting.testIsNextDigit();		
	}
	
	/**
	 * Test the constructors and equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			// create a random byte array
			byte[] randomString = new byte[JsonStreamParserTesting.RANDOM.nextInt(2000)];
			JsonStreamParserTesting.RANDOM.nextBytes(randomString);
			// default charset
			try (
					JsonStreamParser jp = new JsonStreamParser(new ByteArrayInputStream(randomString));
					JsonStreamParser jjp = new JsonStreamParser(new ByteArrayInputStream(randomString));
				) {
				TestSubject.assertTestCondition(jp.equals(jjp), 
						String.format("The JSON parser %s should equal the parser %s.",	jp, jjp));
				if (jp.hasNext()) {
					jp.get();
					TestSubject.assertTestCondition(!jp.equals(jjp), 
							String.format("The JSON parser %s should not equal the parser %s.",	jp, jjp));
					jjp.get();
					TestSubject.assertTestCondition(jp.equals(jjp), 
							String.format("The JSON parser %s should equal the parser %s.",	jp, jjp));
				}
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// different charsets
			try (
					JsonStreamParser jp = new JsonStreamParser(new ByteArrayInputStream(randomString), JsonStreamParserTesting.createRandomEncoding());
					JsonStreamParser jjp = new JsonStreamParser(new ByteArrayInputStream(randomString), JsonStreamParserTesting.createRandomEncoding());
				) {
				/*
				 * Just test constructors as there are many overlaps between the encodings.
				 */
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test null
			try (JsonStreamParser jp = new JsonStreamParser(null);) {
				throw new TestFailureException("An exception should have been thrown as "
						+ "null is no valid input for a JSON parser.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			} catch (JsonParserInternalException | IOException e1) {
				throw new TestFailureException("Creating or closing the JSON parsers failed.", e1);
			}
			try (JsonStreamParser jp = new JsonStreamParser(null, Charset.defaultCharset());) {
				throw new TestFailureException("An exception should have been thrown as "
						+ "null is no valid input for a JSON parser.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			} catch (JsonParserInternalException | IOException e1) {
				throw new TestFailureException("Creating or closing the JSON parsers failed.", e1);
			}
			try (JsonStreamParser jp = new JsonStreamParser(new ByteArrayInputStream(randomString), null);) {
				/*
				 * Do nothing as this is the expected behaviour.
				 */
			} catch (NullPointerException e) {
				throw new TestFailureException("The system default charset should be used if null is "
						+ "passed.", e);
			} catch (JsonParserInternalException | IOException e1) {
				throw new TestFailureException("Creating or closing the JSON parsers failed.", e1);
			} 
		}
	}
	
	/**
	 * Test retrieval of the entire data.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetData() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			// create a random string from a random byte array
			byte[] randomString = new byte[JsonStreamParserTesting.RANDOM.nextInt(2000)];
			JsonStreamParserTesting.RANDOM.nextBytes(randomString);
			// test default encoding
			try (JsonStreamParser jp = new JsonStreamParser(new ByteArrayInputStream(randomString));) {
				String defaultEncodedString = new String(randomString);
				TestSubject.assertTestCondition(defaultEncodedString.equals(jp.getData()), 
						String.format("The JSON parser %s should hold the data %s, but holds %s instead.",	
								jp, defaultEncodedString, jp.getData()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test random encoding
			Charset encoding = JsonStreamParserTesting.createRandomEncoding();
			try (JsonStreamParser jp = new JsonStreamParser(new ByteArrayInputStream(randomString), encoding);) {
				String randomEncodedString = new String(randomString, encoding);
				TestSubject.assertTestCondition(randomEncodedString.equals(jp.getData()), 
						String.format("The JSON parser %s should hold the data %s, but holds %s instead.",	
								jp, randomEncodedString, jp.getData()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test the getters and setters for the parsers position.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testPosition() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				TestSubject.assertTestCondition(jp.getPosition() == 0, 
						String.format("The newly initialised JSON parser %s should start at position "
								+ "%s, but starts at %s.", jp, 0, jp.getPosition()));
				int maxLength = jp.getData().length(); // fixed in this case
				int pos = JsonStreamParserTesting.RANDOM.nextInt(maxLength + 1);
				jp.setPosition(pos);
				TestSubject.assertTestCondition(jp.getPosition() == pos, 
						String.format("The JSON parser %s should be at position "
								+ "%s, but is at %s.", jp, pos, jp.getPosition()));
				// test greater position mark than length of the data
				try {
					jp.setPosition(maxLength + 1);
					throw new TestFailureException(String.format("An exception should have been thrown as "
							+ "the position mark %s is outside of the JSON parsers %s data bounds.", pos, jp));
				} catch (IndexOutOfBoundsException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
				// test negative position marks
				try {
					pos = (JsonStreamParserTesting.RANDOM.nextInt(3000000) + 1) * -1;
					jp.setPosition(pos);
					throw new TestFailureException(String.format("An exception should have been thrown as "
							+ "the position mark %s set for the JSON parser %s is negative.", pos, jp));
				} catch (IndexOutOfBoundsException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test resetting the parser.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testRewind() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String allData = jp.getData();
				// test rewinding the parser to the first index
				jp.rewind();
				TestSubject.assertTestCondition(jp.getPosition() == 0, 
						String.format("The rewinded JSON parser %s should start at position "
								+ "%s, but starts at %s.", jp, 0, jp.getPosition()));
				jp.setPosition(JsonStreamParserTesting.RANDOM.nextInt(allData.length() + 1));
				jp.rewind();
				TestSubject.assertTestCondition(jp.getPosition() == 0, 
						String.format("The rewinded JSON parser %s should start at position "
								+ "%s, but starts at %s.", jp, 0, jp.getPosition()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test checking the next characters in the parser.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testHasNext() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String allData = jp.getData();
				while (jp.getPosition() < allData.length()) {
					TestSubject.assertTestCondition(jp.hasNext(), 
							String.format("The JSON parser %s should still have characters to read.", jp));
					jp.setPosition(jp.getPosition() + 1);
				}
				TestSubject.assertTestCondition(!jp.hasNext(), 
						String.format("The JSON parser %s should not have any more characters to read.", jp));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test checking the next characters in the parser for specific strings.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testIsNext() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			// test parser without incrementing the position
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String allData = jp.getData();
				int randomIndex = 0;
				if (allData.length() > 0) {
					randomIndex = JsonStreamParserTesting.RANDOM.nextInt(allData.length());
				}
				jp.setPosition(randomIndex);
				String query = allData.substring(randomIndex, allData.length());
				TestSubject.assertTestCondition(jp.isNext(query), String.format("The JSON parser %s "
						+ "should have %s as next characters.", jp, query));
				TestSubject.assertTestCondition(jp.getPosition() == randomIndex, String.format("The "
						+ "JSON parser %s should still be at position %s after checking for the "
						+ "characters %s, but is at position %s.", jp, randomIndex, query, jp.getPosition()));
				TestSubject.assertTestCondition(jp.isNext(query, false), String.format("The JSON parser %s "
						+ "should have %s as next characters.", jp, query));
				TestSubject.assertTestCondition(jp.getPosition() == randomIndex, String.format("The "
						+ "JSON parser %s should still be at position %s after checking for the "
						+ "characters %s, but is at position %s.", jp, randomIndex, query, jp.getPosition()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test parser with incrementing the position
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String allData = jp.getData();
				int randomIndex = 0;
				if (allData.length() > 0) {
					randomIndex = JsonStreamParserTesting.RANDOM.nextInt(allData.length());
				}
				jp.setPosition(randomIndex);
				String query = allData.substring(randomIndex, allData.length());
				TestSubject.assertTestCondition(jp.isNext(query, true), String.format("The JSON parser %s "
						+ "should have %s as next characters.", jp, query));
				TestSubject.assertTestCondition(jp.getPosition() == randomIndex + query.length(), String.format("The "
						+ "JSON parser %s should still be at position %s after checking for the "
						+ "characters %s, but is at position %s.", jp, randomIndex, query, jp.getPosition()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test the retrieval of characters from the parsed data.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGet() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			// test single get
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String testString = jp.getData();
				int pos = 0;
				if (testString.length() > 0) {
					pos = JsonStreamParserTesting.RANDOM.nextInt(testString.length() + 1);
				}
				jp.setPosition(pos);
				if (jp.hasNext()) {
					String readCharacter = jp.get();
					TestSubject.assertTestCondition(readCharacter.equals(testString.substring(pos, pos + 1)), 
							String.format("The JSON parsers data %s should read the data \"%s\", "
									+ "but reads \"%s\" instead.", jp, testString.substring(pos, pos + 1), 
									readCharacter));
					TestSubject.assertTestCondition(jp.getPosition() == pos + 1, 
							String.format("The JSON parser %s should be at position "
									+ "%s, but is at %s.", jp, pos + 1, jp.getPosition()));
				} else {
					try {
						String readCharacter = jp.get();
						throw new TestFailureException(String.format("An exception should have been thrown as "
								+ "the parser %s has no characters left, but still %s is read.", jp, readCharacter));
					} catch (IndexOutOfBoundsException e) {
						/*
						 * Do nothing as this is the expected behaviour.
						 */
					}
				}
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test multiple get
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String testString = jp.getData();
				int pos = 0;
				if (testString.length() > 0) {
					pos = JsonStreamParserTesting.RANDOM.nextInt(testString.length() + 1);
				}
				jp.setPosition(pos);
				int length = 0;
				if (pos < testString.length()) {
					length = JsonStreamParserTesting.RANDOM.nextInt(testString.length() - pos);
				}
				String readCharacters = jp.get(length);
				TestSubject.assertTestCondition(readCharacters.equals(testString.substring(pos, pos + length)), 
						String.format("The JSON parsers data %s should read the data \"%s\", "
								+ "but reads \"%s\" instead.", jp, testString.substring(pos, pos + length), 
								readCharacters));
				TestSubject.assertTestCondition(jp.getPosition() == pos + length, 
						String.format("The JSON parser %s should be at position "
								+ "%s, but is at %s.", jp, pos + length, jp.getPosition()));
				// test negative values
				jp.rewind();
				try {
					int negativeRead = (JsonStreamParserTesting.RANDOM.nextInt(3000000) + 1) * -1;
					jp.get(negativeRead);
					throw new TestFailureException(String.format("An exception should have been thrown as "
							+ "the parser %s if %s characters should be read.", jp, negativeRead));
				} catch (IndexOutOfBoundsException e) {
					/*
					 * Do nothing as this is the expected behaviour.
					 */
				}
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test the retrieval of all remaining characters from the parser.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetRemaining() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			try (JsonStreamParser jp = new JsonStreamParser(JsonStreamParserTesting.createRandomStream());) {
				String testString = jp.getData();
				TestSubject.assertTestCondition(jp.getRemaining().equals(testString), 
						String.format("The remaining characters of the data parsed by the "
								+ "JSON parser %s should read \"%s\", "
								+ "but read \"%s\" instead.", jp, testString, jp.getRemaining()));
				int pos = 0;
				if (testString.length() > 0) {
					pos = JsonStreamParserTesting.RANDOM.nextInt(testString.length());
				}
				jp.setPosition(pos);
				TestSubject.assertTestCondition(jp.getRemaining().equals(testString.substring(pos)), 
						String.format("The remaining characters of the data parsed by the "
								+ "JSON parser %s should read \"%s\", "
								+ "but read \"%s\" instead.", jp, testString, jp.getRemaining()));
				TestSubject.assertTestCondition(jp.getPosition() == pos, 
						String.format("The JSON parser %s should be at position "
								+ "%s, but is at %s.", jp, pos, jp.getPosition()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test skipping whitespaces.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testSkipWhitespace() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			// test streams only consisting of whitespace
			String whitespaceString = null;
			try (JsonStreamParser whiteSpaceParser = new JsonStreamParser(JsonStreamParserTesting.createRandomWhitespaceSequence());) {
				whitespaceString = whiteSpaceParser.getData();
				whiteSpaceParser.skipWhitespace();
				TestSubject.assertTestCondition(whiteSpaceParser.getPosition() == whitespaceString.length(), 
						String.format("The JSON parser %s should be at position "
								+ "%s after skiping all whitespaces, but is at %s.", 
								whiteSpaceParser, whitespaceString.length(), whiteSpaceParser.getPosition()));
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test streams without whitespace
			String noWhitespaceString = null;
			try (JsonStreamParser noWhiteSpaceParser = new JsonStreamParser(JsonStreamParserTesting.createRandomSequenceWithoutWhitespace());) {
				noWhitespaceString = noWhiteSpaceParser.getData();
				noWhiteSpaceParser.skipWhitespace();
				TestSubject.assertTestCondition(noWhiteSpaceParser.getPosition() == 0, 
						String.format("The JSON parser %s should be at position "
								+ "%s after skipping all whitespaces, but is at %s.", 
								noWhiteSpaceParser, 0, noWhiteSpaceParser.getPosition()));	
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test normal streams
			String testString = noWhitespaceString;
			int whiteSpaceStart = testString.length();
			testString += whitespaceString;
			int whiteSpaceEnd = testString.length();
			testString += noWhitespaceString;
			try (JsonStreamParser jp = new JsonStreamParser(new ByteArrayInputStream(testString.getBytes()));) {
				jp.setPosition(whiteSpaceStart);
				jp.skipWhitespace();
				TestSubject.assertTestCondition(jp.getPosition() == whiteSpaceEnd, 
						String.format("The JSON parser %s should be at position "
								+ "%s after skipping all whitespaces, but is at %s.", 
								jp, whiteSpaceEnd, jp.getPosition()));	
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Test detecting if the next character is a digit.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testIsNextDigit() throws TestFailureException {
		for (int i = 0; i < 3000; i++) {
			// test streams only consisting of digits
			try (JsonStreamParser digitParser = new JsonStreamParser(JsonStreamParserTesting.createRandomDigitSequence());) {
				String digitString = digitParser.getData();
				for (int j = 0; j < digitString.length(); j++) {
					char nextChar = digitParser.get().charAt(0);
					TestSubject.assertTestCondition(Character.isDigit(nextChar), 
							String.format("The parsed char in the JSON parser %s should be a digit "
									+ ", but is \"%s\".", 
									digitParser, nextChar));
				}	
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
			// test streams without digits
			try (JsonStreamParser noDigitsParser = new JsonStreamParser(JsonStreamParserTesting.createRandomSequenceWithoutDigits());) {
				String stringWithoutDigits = noDigitsParser.getData();
				for (int j = 0; j < stringWithoutDigits.length(); j++) {
					char nextChar = noDigitsParser.get().charAt(0);
					TestSubject.assertTestCondition(!Character.isDigit(nextChar), 
							String.format("The parsed char in the JSON parser %s should not be a digit "
									+ ", but is \"%s\".", 
									noDigitsParser, nextChar));
				}	
			} catch (JsonParserInternalException | IOException e) {
				throw new TestFailureException("Creating  or closing the JSON parsers failed.", e);
			}
		}
	}
	
	/**
	 * Create a random input stream from a random string.
	 * 
	 * @return a random input stream
	 */
	private static InputStream createRandomStream() {
		// create random strings
		byte[] randomString = new byte[JsonStreamParserTesting.RANDOM.nextInt(2000)];
		JsonStreamParserTesting.RANDOM.nextBytes(randomString);
		return new ByteArrayInputStream(randomString);
	}
	
	/**
	 * Picks randomly a standard encoding.
	 * 
	 * @return a random standard encoding
	 */
	private static Charset createRandomEncoding() {
		// create random strings
		int randomEncoding = JsonStreamParserTesting.RANDOM.nextInt(6);
		switch (randomEncoding) {
		
			case 0:
				return StandardCharsets.ISO_8859_1;
				
			case 1:
				return StandardCharsets.US_ASCII;
				
			case 2:
				return StandardCharsets.UTF_16;
				
			case 3:
				return StandardCharsets.UTF_16BE;
				
			case 4:
				return StandardCharsets.UTF_16LE;
				
			case 5:
				return StandardCharsets.UTF_8;
	
			default:
				return Charset.defaultCharset();
			
		}
	}
	
	/**
	 * Helper function to create a input stream only containing random whitespaces.
	 */
	private static InputStream createRandomWhitespaceSequence() {
		/*
		 * This string creation method is heavily biased against empty strings, but for the sake
		 * of testing it should be fine.
		 */
		byte[] randomString = new byte[JsonStreamParserTesting.RANDOM.nextInt(1000)];
		JsonStreamParserTesting.RANDOM.nextBytes(randomString);
		String testString = new String(randomString);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < testString.length(); i++) {
			if (Character.isWhitespace(testString.charAt(i))) {
				sb.append(testString.charAt(i));
			}
		}
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
	
	/**
	 * Helper function to create a random input stream without any whitespaces.
	 */
	private static InputStream createRandomSequenceWithoutWhitespace() {
		/*
		 * This creation method is biased against empty strings, but for the sake
		 * of testing it should be fine.
		 */
		byte[] randomString = new byte[JsonStreamParserTesting.RANDOM.nextInt(300)];
		JsonStreamParserTesting.RANDOM.nextBytes(randomString);
		String testString = new String(randomString);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < testString.length(); i++) {
			if (!Character.isWhitespace(testString.charAt(i))) {
				sb.append(testString.charAt(i));
			}
		}
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
	
	/**
	 * Helper function to create a input stream only containing random digits.
	 */
	private static InputStream createRandomDigitSequence() {
		int length = JsonStreamParserTesting.RANDOM.nextInt(200);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			sb.append(JsonStreamParserTesting.RANDOM.nextInt(10));
		}
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
	
	/**
	 * Helper function to create a random input stream without any digits.
	 */
	private static InputStream createRandomSequenceWithoutDigits() {
		byte[] randomString = new byte[JsonStreamParserTesting.RANDOM.nextInt(300)];
		JsonStreamParserTesting.RANDOM.nextBytes(randomString);
		String testString = new String(randomString);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < testString.length(); i++) {
			if (!Character.isDigit(testString.charAt(i))) {
				sb.append(testString.charAt(i));
			}
		}
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

}
