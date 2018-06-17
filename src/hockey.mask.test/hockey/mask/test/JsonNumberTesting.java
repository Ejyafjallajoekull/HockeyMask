package hockey.mask.test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonNumber;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

public class JsonNumberTesting implements TestSubject {

	private static final Random RANDOM = new Random();
	
	@Override
	public void runAllTests() throws TestFailureException {
		JsonNumberTesting.testConstructors();
		JsonNumberTesting.testGetValue();
		JsonNumberTesting.testToJson();
		JsonNumberTesting.testParsing();
		JsonNumberTesting.testParsingNext();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test integer
			int testInt = JsonNumberTesting.RANDOM.nextInt();
			JsonNumber firstInteger = new JsonNumber(testInt);
			JsonNumber secondInteger = new JsonNumber(testInt);
			TestSubject.assertTestCondition(firstInteger.equals(secondInteger), 
					String.format("The JSON number %s should equal %s", 
							firstInteger, secondInteger));
			secondInteger = new JsonNumber(testInt + 1);
			TestSubject.assertTestCondition(!firstInteger.equals(secondInteger), 
					String.format("The JSON number %s should not equal %s", 
							firstInteger, secondInteger));
			TestSubject.assertTestCondition(!firstInteger.equals(null), 
					String.format("The JSON number %s should not equal a Java null.", firstInteger));
			// test long
			long testLong = JsonNumberTesting.RANDOM.nextLong();
			JsonNumber firstLong = new JsonNumber(testLong);
			JsonNumber secondLong = new JsonNumber(testLong);
			TestSubject.assertTestCondition(firstLong.equals(secondLong), 
					String.format("The JSON number %s should equal %s", 
							firstLong, secondLong));
			secondLong = new JsonNumber(testInt + 1l);
			TestSubject.assertTestCondition(!firstLong.equals(secondLong), 
					String.format("The JSON number %s should not equal %s", 
							firstLong, secondLong));
			TestSubject.assertTestCondition(!firstLong.equals(null), 
					String.format("The JSON number %s should not equal a Java null.", firstLong));
			// test float
			try {
				float testFloat = JsonNumberTesting.RANDOM.nextFloat() * 3000000f;
				JsonNumber firstFloat = new JsonNumber(testFloat);
				JsonNumber secondFloat = new JsonNumber(testFloat);
				TestSubject.assertTestCondition(firstFloat.equals(secondFloat), 
						String.format("The JSON number %s should equal %s", 
								firstFloat, secondFloat));
				secondFloat = new JsonNumber(testFloat + 1f);
				TestSubject.assertTestCondition(!firstFloat.equals(secondFloat), 
						String.format("The JSON number %s should not equal %s", 
								firstFloat, secondFloat));
				TestSubject.assertTestCondition(!firstFloat.equals(null), 
						String.format("The JSON number %s should not equal a Java null.", firstFloat));
			} catch (JsonStandardException e) {
				throw new TestFailureException(e);
			}
			try {
				new JsonNumber(Float.POSITIVE_INFINITY);
				throw new TestFailureException("Representing positive infinity as JSON number should fail.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			try {
				new JsonNumber(Float.NEGATIVE_INFINITY);
				throw new TestFailureException("Representing negative infinity as JSON number should fail.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			try {
				new JsonNumber(Float.NaN);
				throw new TestFailureException("Representing NaN as JSON number should fail.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test double
			try {
				double testDouble = JsonNumberTesting.RANDOM.nextDouble() * 3000000d;
				JsonNumber firstDouble = new JsonNumber(testDouble);
				JsonNumber secondDouble = new JsonNumber(testDouble);
				TestSubject.assertTestCondition(firstDouble.equals(secondDouble), 
						String.format("The JSON number %s should equal %s", 
								firstDouble, secondDouble));
				secondDouble = new JsonNumber(testDouble + 1d);
				TestSubject.assertTestCondition(!firstDouble.equals(secondDouble), 
						String.format("The JSON number %s should not equal %s", 
								firstDouble, secondDouble));
				TestSubject.assertTestCondition(!firstDouble.equals(null), 
						String.format("The JSON number %s should not equal a Java null.", firstDouble));
			} catch (JsonStandardException e) {
				throw new TestFailureException(e);
			}
			try {
				new JsonNumber(Double.POSITIVE_INFINITY);
				throw new TestFailureException("Representing positive infinity as JSON number should fail.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			try {
				new JsonNumber(Double.NEGATIVE_INFINITY);
				throw new TestFailureException("Representing negative infinity as JSON number should fail.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			try {
				new JsonNumber(Double.NaN);
				throw new TestFailureException("Representing NaN as JSON number should fail.");
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test BigDecimal
			double testDouble = JsonNumberTesting.RANDOM.nextDouble() * 3000000d;
			BigDecimal testNumber = new BigDecimal(testDouble);
			JsonNumber firstNumber = new JsonNumber(testNumber);
			JsonNumber secondNumber = new JsonNumber(testNumber);
			TestSubject.assertTestCondition(firstNumber.equals(secondNumber), 
					String.format("The JSON number %s should equal %s", 
							firstNumber, secondNumber));
			secondNumber = new JsonNumber(new BigDecimal(testDouble + 1d));
			TestSubject.assertTestCondition(!firstNumber.equals(secondNumber), 
					String.format("The JSON number %s should not equal %s", 
							firstNumber, secondNumber));
			TestSubject.assertTestCondition(!firstNumber.equals(null), 
					String.format("The JSON number %s should not equal a Java null.", firstNumber));
			try {
				new JsonNumber(null);
				throw new TestFailureException("Creating a JSON number from null should fail.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
		}
	}
	
	/**
	 * Test getting the Java representation of the value.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetValue() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test integer
			int testInt = JsonNumberTesting.RANDOM.nextInt();
			BigDecimal testIntegerValue = new BigDecimal(testInt);
			JsonNumber testJsonInteger = new JsonNumber(testInt);
			TestSubject.assertTestCondition(testJsonInteger.getValue().equals(testIntegerValue), 
					String.format("The JSON number %s should hold the value %s, but holds %s.", 
							testJsonInteger, testIntegerValue, testJsonInteger.getValue()));
			// test long
			long testLong = JsonNumberTesting.RANDOM.nextLong();
			BigDecimal testLongValue = new BigDecimal(testLong);
			JsonNumber testJsonLong = new JsonNumber(testLong);
			TestSubject.assertTestCondition(testJsonLong.getValue().equals(testLongValue), 
					String.format("The JSON number %s should hold the value %s, but holds %s.", 
							testJsonLong, testLongValue, testJsonLong.getValue()));
			// test float
			try {
				float testFloat = JsonNumberTesting.RANDOM.nextFloat() * 3000000f;
				BigDecimal testFloatValue = new BigDecimal(testFloat);
				JsonNumber testJsonFloat = new JsonNumber(testFloat);
				TestSubject.assertTestCondition(testJsonFloat.getValue().equals(testFloatValue), 
						String.format("The JSON number %s should hold the value %s, but holds %s.", 
								testJsonFloat, testFloatValue, testJsonFloat.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(e);
			}
			// test double
			try {
				double testDouble = JsonNumberTesting.RANDOM.nextDouble() * 3000000d;
				BigDecimal testDoubleValue = new BigDecimal(testDouble);
				JsonNumber testJsonDouble = new JsonNumber(testDouble);
				TestSubject.assertTestCondition(testJsonDouble.getValue().equals(testDoubleValue), 
						String.format("The JSON number %s should hold the value %s, but holds %s.", 
								testJsonDouble, testDoubleValue, testJsonDouble.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(e);
			}
			// test BigDecimal
			double testDouble = JsonNumberTesting.RANDOM.nextDouble() * 3000000d;
			BigDecimal testDecimalValue = new BigDecimal(testDouble);
			JsonNumber testJsonDecimal = new JsonNumber(testDecimalValue);
			TestSubject.assertTestCondition(testJsonDecimal.getValue().equals(testDecimalValue), 
					String.format("The JSON number %s should hold the value %s, but holds %s.", 
							testJsonDecimal, testDecimalValue, testJsonDecimal.getValue()));
		}
	}
	
	/**
	 * Test the conversion of the number to a JSON formatted string.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			double testDouble = JsonNumberTesting.RANDOM.nextDouble() * 3000000d;
			BigDecimal testDecimalValue = new BigDecimal(testDouble);
			JsonNumber testJsonDecimal = new JsonNumber(testDecimalValue);
			TestSubject.assertTestCondition(testJsonDecimal.toJson().equals(testDecimalValue.toString()), 
					String.format("The JSON representation of the number %s"
							+ " should equal the value \"%s\", but is \"%s\".", 
							testJsonDecimal, testDecimalValue, testJsonDecimal.toJson()));
		}
	}
	
	/**
	 * Test parsing JSON formatted numbers.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonNumber.parse(null);
			throw new TestFailureException("Parsing of null for a JSON number should fail.");
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON number should throw a "
					+ "NullPointerException.", e);
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test empty strings
		try {
			JsonNumber.parse("");
			throw new TestFailureException("Parsing of an empty string for a JSON number should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test random numbers
		for (int i = 0; i < 10000; i++) {
			// test correct numbers
			try {
				double testDouble = JsonNumberTesting.RANDOM.nextDouble() * 3000000d;
				JsonNumber testJsonNumber = new JsonNumber(testDouble);
				JsonNumber parsedJsonNumber = JsonNumber.parse(testJsonNumber.toJson());
				TestSubject.assertTestCondition(testJsonNumber.equals(parsedJsonNumber), 
						String.format("The parsed JSON number %s should equal %s", 
								parsedJsonNumber, testJsonNumber));
			} catch (JsonStandardException e) {
				throw new TestFailureException(e);
			}
			// test plus sign
			try {
				int testInt = JsonNumberTesting.RANDOM.nextInt();
				String plusString = JsonNumber.JSON_PLUS_VALUE + testInt;
				JsonNumber.parse(plusString);
				throw new TestFailureException(String.format("Parsing the number %s proceeded by a "
						+ "plus sign should fail.", plusString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test leading zero
			try {
				int testInt = JsonNumberTesting.RANDOM.nextInt();
				String leadingZeroString = "0" + testInt;
				JsonNumber.parse(leadingZeroString);
				throw new TestFailureException(String.format("Parsing the number %s proceeded by a "
						+ "leading zero should fail.", leadingZeroString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test wrong fraction
			try {
				int testInt = JsonNumberTesting.RANDOM.nextInt();
				String fractionString = JsonNumber.JSON_FLOATING_SEPARATOR_VALUE + testInt;
				JsonNumber.parse(fractionString);
				throw new TestFailureException(String.format("Parsing the fraction %s without a "
						+ "leading zero should fail.", fractionString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			try {
				int testInt = JsonNumberTesting.RANDOM.nextInt();
				String fractionString = testInt + JsonNumber.JSON_FLOATING_SEPARATOR_VALUE;
				JsonNumber.parse(fractionString);
				throw new TestFailureException(String.format("Parsing the fraction %s without "
						+ "trailing digits should fail.", fractionString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test wrong exponent
			try {
				int testInt = JsonNumberTesting.RANDOM.nextInt();
				String exponentString = testInt + JsonNumber.JSON_EXPONENT_VALUE;
				JsonNumber.parse(exponentString);
				throw new TestFailureException(String.format("Parsing the number %s without "
						+ "trailing digits at the exponent should fail.", exponentString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test additional garbage data
			try {
				int testInt = JsonNumberTesting.RANDOM.nextInt();
				String garbageString = testInt + "garbage";
				JsonNumber.parse(garbageString);
				throw new TestFailureException(String.format("Parsing the number %s with "
						+ "trailing data should fail.", garbageString));
			} catch (JsonStandardException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
		}		
	}
	
	/**
	 * Test sequentially parsing JSON formatted booleans.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonNumber.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON number should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON string
		for (int i = 0; i < 10000; i++) {
			JsonNumber[] jsonTestNumbers = new JsonNumber[JsonNumberTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestNumbers.length; j++) {
				jsonTestNumbers[j] = new JsonNumber(-1l*Math.abs(JsonNumberTesting.RANDOM.nextLong()));
				testString += jsonTestNumbers[j].toJson() + "   ";
			}
			try {
				JsonParser jp = new JsonParser(testString);
				JsonNumber[] parsedJsonNumbers = new JsonNumber[jsonTestNumbers.length];
				for (int j = 0; j < jsonTestNumbers.length; j++) {
					parsedJsonNumbers[j] = JsonNumber.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestNumbers, parsedJsonNumbers),
					String.format("The parsed JSON numbers %s should equal %s.", 
							Arrays.toString(parsedJsonNumbers), 
							Arrays.toString(jsonTestNumbers)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "numbers failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		String testString = JsonNumberTesting.RANDOM.nextInt() + JsonNumber.JSON_EXPONENT_CAPITAL_VALUE;
		try {
			JsonParser jp = new JsonParser(testString);
			int initialPosition = jp.getPosition();
			try {
				JsonNumber.parseNext(jp);
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "number should fail, but resulted in the parser %s.", 
						testString, jp));
			} catch (JsonStandardException e) {
				TestSubject.assertTestCondition(jp.getPosition() == initialPosition,
						String.format("The JSON parser %s should be reset to position %s after "
								+ "failing to parse, but is at %s.", 
								jp, initialPosition, jp.getPosition()));
			}
		} catch (JsonStandardException e) {
			throw new TestFailureException("Creating the JSON parser failed.", e);
		}
	}


}
