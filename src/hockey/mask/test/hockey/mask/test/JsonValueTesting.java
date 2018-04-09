package hockey.mask.test.hockey.mask.test;

import java.math.BigDecimal;
import java.util.Random;

import hockey.mask.json.hockey.mask.json.JsonArray;
import hockey.mask.json.hockey.mask.json.JsonObject;
import hockey.mask.json.hockey.mask.json.JsonStandardException;
import hockey.mask.json.hockey.mask.json.JsonValue;
import hockey.mask.json.hockey.mask.json.JsonValueTypes;
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
		JsonValueTesting.testToJsonString();
		JsonValueTesting.testConstructors();
		JsonValueTesting.testSetterGetter();
		JsonValueTesting.testParsing();
		JsonValueTesting.testToJson();
	}
	
	/**
	 * Test whether the parsing of JSON formatted strings works correctly.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParseString() throws TestFailureException {
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
	
	/**
	 * Test the conversion of strings to JSON formatted strings.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJsonString() throws TestFailureException {
		String testString = null;
		String resultString = null;
		byte[] randomString = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
			JsonValueTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			// the resulting string should look like this
			resultString = JsonValue.JSON_STRING_IDENTIFIER + testString
					+ JsonValue.JSON_STRING_IDENTIFIER;
			TestSubject.assertTestCondition(resultString.equals(JsonValue.stringToJson(
					testString)), String.format("The string \"%s\" should be transfromed "
							+ "to \"%s\", but is transformed to \"%s\".", testString, 
							resultString, JsonValue.stringToJson(testString)));
		}
	}
	
	/**
	 * Test constructors and some basic equality.
	 */
	private static void testConstructors() throws TestFailureException {
		JsonValue testValue = new JsonValue();
		// test JSON null
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
				String.format("A JSON null should be of type %s, but is %s.", 
						JsonValueTypes.NULL, testValue.getType()));
		TestSubject.assertTestCondition(testValue.getValue() == null, 
				String.format("A JSON null should have the value null, but has %s.", 
						testValue.getValue()));
		// test JSON string
		String testString = null;
		byte[] randomString = null;
		testValue = new JsonValue(testString);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
				String.format("A JSON null string should be of type %s, but is %s.", 
						JsonValueTypes.NULL, testValue.getType()));
		TestSubject.assertTestCondition(testValue.getValue() == null, 
				String.format("A JSON null string should have the value null, but has %s.", 
						testValue.getValue()));
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
			JsonValueTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			testValue = new JsonValue(testString);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.STRING, 
					String.format("A JSON string should be of type %s, but is %s.", 
							JsonValueTypes.STRING, testValue.getType()));
			TestSubject.assertTestCondition(testString.equals(testValue.getValue()), 
					String.format("The JSON string should hold the value %s, but has %s.", 
							testString, testValue.getValue()));
		}
		// test JSON boolean
		boolean testBool = false;
		for (int i = 0; i < 100; i++) {
			testBool = JsonValueTesting.RANDOM.nextBoolean();
			testValue = new JsonValue(testBool);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.BOOLEAN, 
					String.format("A JSON boolean should be of type %s, but is %s.", 
							JsonValueTypes.BOOLEAN, testValue.getType()));
			TestSubject.assertTestCondition((boolean) testValue.getValue() == testBool, 
					String.format("The JSON boolean should hold the value %s, but has %s.", 
							testBool, testValue.getValue()));
		}
		// test JSON number
		int testInt = 0;
		long testLong = 0;
		float testFloat = 0;
		double testDouble = 0;
		BigDecimal testDecimal = null;
		// test null number
		testValue = new JsonValue(testDecimal);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
				String.format("A JSON null number should be of type %s, but is %s.", 
						JsonValueTypes.NULL, testValue.getType()));
		TestSubject.assertTestCondition(testValue.getValue() == null, 
				String.format("A JSON null number should have the value null, but has %s.", 
						testValue.getValue()));
		for (int i = 0; i < 10000; i++) {
			// test ints
			testInt = JsonValueTesting.RANDOM.nextInt();
			testValue = new JsonValue(testInt);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).intValue() == testInt, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testInt, testValue.getValue()));
			// test longs
			testLong = JsonValueTesting.RANDOM.nextLong();
			testValue = new JsonValue(testLong);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).longValue() == testLong, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testLong, testValue.getValue()));
			// test floats
			testFloat = JsonValueTesting.RANDOM.nextFloat();
			testValue = new JsonValue(testFloat);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).floatValue() == testFloat, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testFloat, testValue.getValue()));
			// test double
			testDouble = JsonValueTesting.RANDOM.nextDouble();
			testValue = new JsonValue(testDouble);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).doubleValue() == testDouble, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testDouble, testValue.getValue()));
			// test BigDecimal
			testDecimal = new BigDecimal(testDouble);
			testValue = new JsonValue(testDecimal);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).equals(testDecimal), 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testDecimal, testValue.getValue()));
		}
		// test arrays
		// TODO: test more than just empty arrays and objects
		JsonArray testArray = new JsonArray();
		testValue = new JsonValue(testArray);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.ARRAY, 
				String.format("A JSON array should be of type %s, but is %s.", 
						JsonValueTypes.ARRAY, testValue.getType()));
		TestSubject.assertTestCondition(((JsonArray) testValue.getValue()).equals(testArray), 
				String.format("The JSON array should hold the value %s, but holds %s.", 
						testArray, testValue.getValue()));
		// test objects
		JsonObject testObject = new JsonObject();
		testValue = new JsonValue(testObject);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.OBJECT, 
				String.format("A JSON object should be of type %s, but is %s.", 
						JsonValueTypes.OBJECT, testValue.getType()));
		TestSubject.assertTestCondition(((JsonObject) testValue.getValue()).equals(testObject), 
				String.format("The JSON object should hold the value %s, but holds %s.", 
						testObject, testValue.getValue()));
	}
	
	/**
	 * Test setting and getting the JsonValue.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testSetterGetter() throws TestFailureException {
		JsonValue testValue = new JsonValue();
		// test JSON null
		testValue.setValueToNull();
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
				String.format("A JSON null should be of type %s, but is %s.", 
						JsonValueTypes.NULL, testValue.getType()));
		TestSubject.assertTestCondition(testValue.getValue() == null, 
				String.format("A JSON null should have the value null, but has %s.", 
						testValue.getValue()));
		// test JSON string
		String testString = null;
		byte[] randomString = null;
		testValue.setValue(testString);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
				String.format("A JSON null string should be of type %s, but is %s.", 
						JsonValueTypes.NULL, testValue.getType()));
		TestSubject.assertTestCondition(testValue.getValue() == null, 
				String.format("A JSON null string should have the value null, but has %s.", 
						testValue.getValue()));
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
			JsonValueTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			testValue.setValue(testString);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.STRING, 
					String.format("A JSON string should be of type %s, but is %s.", 
							JsonValueTypes.STRING, testValue.getType()));
			TestSubject.assertTestCondition(testString.equals(testValue.getValue()), 
					String.format("The JSON string should hold the value %s, but has %s.", 
							testString, testValue.getValue()));
		}
		// test JSON boolean
		boolean testBool = false;
		for (int i = 0; i < 100; i++) {
			testBool = JsonValueTesting.RANDOM.nextBoolean();
			testValue.setValue(testBool);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.BOOLEAN, 
					String.format("A JSON boolean should be of type %s, but is %s.", 
							JsonValueTypes.BOOLEAN, testValue.getType()));
			TestSubject.assertTestCondition((boolean) testValue.getValue() == testBool, 
					String.format("The JSON boolean should hold the value %s, but has %s.", 
							testBool, testValue.getValue()));
		}
		// test JSON number
		int testInt = 0;
		long testLong = 0;
		float testFloat = 0;
		double testDouble = 0;
		BigDecimal testDecimal = null;
		// test null number
		testValue.setValue(testDecimal);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
				String.format("A JSON null number should be of type %s, but is %s.", 
						JsonValueTypes.NULL, testValue.getType()));
		TestSubject.assertTestCondition(testValue.getValue() == null, 
				String.format("A JSON null number should have the value null, but has %s.", 
						testValue.getValue()));
		for (int i = 0; i < 10000; i++) {
			// test ints
			testInt = JsonValueTesting.RANDOM.nextInt();
			testValue.setValue(testInt);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).intValue() == testInt, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testInt, testValue.getValue()));
			// test longs
			testLong = JsonValueTesting.RANDOM.nextLong();
			testValue.setValue(testLong);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).longValue() == testLong, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testLong, testValue.getValue()));
			// test floats
			testFloat = JsonValueTesting.RANDOM.nextFloat();
			testValue.setValue(testFloat);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).floatValue() == testFloat, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testFloat, testValue.getValue()));
			// test double
			testDouble = JsonValueTesting.RANDOM.nextDouble();
			testValue.setValue(testDouble);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).doubleValue() == testDouble, 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testDouble, testValue.getValue()));
			// test BigDecimal
			testDecimal = new BigDecimal(testDouble);
			testValue.setValue(testDecimal);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
					String.format("A JSON number should be of type %s, but is %s.", 
							JsonValueTypes.NUMBER, testValue.getType()));
			TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).equals(testDecimal), 
					String.format("The JSON number should hold the value %s, but holds %s.", 
							testDecimal, testValue.getValue()));
		}
		// test arrays
		// TODO: test more than just empty arrays and objects
		JsonArray testArray = new JsonArray();
		testValue.setValue(testArray);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.ARRAY, 
				String.format("A JSON array should be of type %s, but is %s.", 
						JsonValueTypes.ARRAY, testValue.getType()));
		TestSubject.assertTestCondition(((JsonArray) testValue.getValue()).equals(testArray), 
				String.format("The JSON array should hold the value %s, but holds %s.", 
						testArray, testValue.getValue()));
		// test objects
		JsonObject testObject = new JsonObject();
		testValue.setValue(testObject);
		TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.OBJECT, 
				String.format("A JSON object should be of type %s, but is %s.", 
						JsonValueTypes.OBJECT, testValue.getType()));
		TestSubject.assertTestCondition(((JsonObject) testValue.getValue()).equals(testObject), 
				String.format("The JSON object should hold the value %s, but holds %s.", 
						testObject, testValue.getValue()));
	}
	
	/**
	 * Test the parsing of JSON values.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		JsonValue testValue = null;
		// test strings
		String testString = null;
		byte[] randomString = null;
		for (int i = 0; i < 10000; i++) {
			// create random strings
			randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
			JsonValueTesting.RANDOM.nextBytes(randomString);
			testString = new String(randomString);
			try {
				testValue = JsonValue.parse(JsonValue.stringToJson(testString));
				TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.STRING, 
						String.format("A JSON string should be of type %s, but is %s.", 
								JsonValueTypes.STRING, testValue.getType()));
				TestSubject.assertTestCondition(testString.equals(testValue.getValue()), 
						String.format("The JSON string should hold the value %s, but has %s.", 
								testString, testValue.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("No JSON representation for "
						+ "the string \"%s\" could be generated.", testString), e);
			}
		}
		// test booleans
		try {
			testValue = JsonValue.parse(JsonValue.JSON_FALSE_VALUE);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.BOOLEAN, 
					String.format("A JSON boolean should be of type %s, but is %s.", 
							JsonValueTypes.BOOLEAN, testValue.getType()));
			TestSubject.assertTestCondition((boolean) testValue.getValue() == false, 
					String.format("The JSON boolean should hold the value %s, but has %s.", 
							false, testValue.getValue()));
		} catch (JsonStandardException e) {
			throw new TestFailureException(String.format("No JSON representation for "
					+ "the boolean \"%s\" could be generated.", false), e);
		}
		try {
			testValue = JsonValue.parse(JsonValue.JSON_TRUE_VALUE);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.BOOLEAN, 
					String.format("A JSON boolean should be of type %s, but is %s.", 
							JsonValueTypes.BOOLEAN, testValue.getType()));
			TestSubject.assertTestCondition((boolean) testValue.getValue() == true, 
					String.format("The JSON boolean should hold the value %s, but has %s.", 
							true, testValue.getValue()));
		} catch (JsonStandardException e) {
			throw new TestFailureException(String.format("No JSON representation for "
					+ "the boolean \"%s\" could be generated.", true), e);
		}
		// test null
		try {
			testValue = JsonValue.parse(JsonValue.JSON_NULL_VALUE);
			TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NULL, 
					String.format("A JSON null should be of type %s, but is %s.", 
							JsonValueTypes.NULL, testValue.getType()));
			TestSubject.assertTestCondition(testValue.getValue() == null, 
					String.format("The JSON null should hold the value %s, but has %s.", 
							null, testValue.getValue()));
		} catch (JsonStandardException e) {
			throw new TestFailureException("No JSON representation for "
					+ "null could be generated.", e);
		}
		// test numbers
		BigDecimal testDecimal = null;
		for (int i = 0; i < 10000; i++) {
			try {
				// test longs
				testDecimal = new BigDecimal(JsonValueTesting.RANDOM.nextLong());
				testValue = JsonValue.parse(testDecimal.toString());
				TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
						String.format("A JSON number should be of type %s, but is %s.", 
								JsonValueTypes.NUMBER, testValue.getType()));
				TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).equals(testDecimal), 
						String.format("The JSON number should hold the value %s, but holds %s.", 
								testDecimal, testValue.getValue()));
				// test doubles
				testDecimal = new BigDecimal(JsonValueTesting.RANDOM.nextDouble());
				testValue = JsonValue.parse(testDecimal.toString());
				TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.NUMBER, 
						String.format("A JSON number should be of type %s, but is %s.", 
								JsonValueTypes.NUMBER, testValue.getType()));
				TestSubject.assertTestCondition(((BigDecimal) testValue.getValue()).equals(testDecimal), 
						String.format("The JSON number should hold the value %s, but holds %s.", 
								testDecimal, testValue.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("No JSON representation for "
						+ "the number \"%s\" could be generated.", testDecimal), e);
			}
			// test arrays
			// TODO: test more than just empty arrays and objects
			JsonArray testArray = new JsonArray();
			try {
				testValue = JsonValue.parse(testArray.toJson());
				TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.ARRAY, 
						String.format("A JSON array should be of type %s, but is %s.", 
								JsonValueTypes.ARRAY, testValue.getType()));
				TestSubject.assertTestCondition(((JsonArray) testValue.getValue()).equals(testArray), 
						String.format("The JSON array should hold the value %s, but holds %s.", 
								testArray, testValue.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("No JSON representation for "
						+ "the array \"%s\" could be generated.", testArray), e);
			}
			// test objects
			JsonObject testObject = new JsonObject();
			try {
				testValue = JsonValue.parse(testObject.toJson());
				TestSubject.assertTestCondition(testValue.getType() == JsonValueTypes.OBJECT, 
						String.format("A JSON object should be of type %s, but is %s.", 
								JsonValueTypes.OBJECT, testValue.getType()));
				TestSubject.assertTestCondition(((JsonObject) testValue.getValue()).equals(testObject), 
						String.format("The JSON object should hold the value %s, but holds %s.", 
								testObject, testValue.getValue()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("No JSON representation for "
						+ "the object \"%s\" could be generated.", testObject), e);
			}
		}
	}
	
	/**
	 * Test the serialisation of the object to a JSON formatted string.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		JsonValue testValue = new JsonValue();
		JsonValue serialised = null;
		// test JSON null
		try {
			serialised = JsonValue.parse(testValue.toJson());
			TestSubject.assertTestCondition(testValue.equals(serialised), 
					String.format("The JSON value created by parsing the string \"%s\" "
							+ "should have the value %s, but has %s.", testValue.toJson(),
							testValue, serialised));
			// test JSON string
			String testString = null;
			byte[] randomString = null;
			testValue.setValue(testString);
			serialised = JsonValue.parse(testValue.toJson());
			TestSubject.assertTestCondition(testValue.equals(serialised), 
					String.format("The JSON value created by parsing the string \"%s\" "
							+ "should have the value %s, but has %s.", testValue.toJson(),
							testValue, serialised));
			for (int i = 0; i < 10000; i++) {
				// create random strings
				randomString = new byte[JsonValueTesting.RANDOM.nextInt(200)];
				JsonValueTesting.RANDOM.nextBytes(randomString);
				testString = new String(randomString);
				testValue = new JsonValue(testString);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
			}
			// test JSON boolean
			boolean testBool = false;
			for (int i = 0; i < 100; i++) {
				testBool = JsonValueTesting.RANDOM.nextBoolean();
				testValue = new JsonValue(testBool);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
			}
			// test JSON number
			int testInt = 0;
			long testLong = 0;
			float testFloat = 0;
			double testDouble = 0;
			BigDecimal testDecimal = null;
			// test null number
			testValue = new JsonValue(testDecimal);
			serialised = JsonValue.parse(testValue.toJson());
			TestSubject.assertTestCondition(testValue.equals(serialised), 
					String.format("The JSON value created by parsing the string \"%s\" "
							+ "should have the value %s, but has %s.", testValue.toJson(),
							testValue, serialised));
			for (int i = 0; i < 10000; i++) {
				// test ints
				testInt = JsonValueTesting.RANDOM.nextInt();
				testValue = new JsonValue(testInt);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
				// test longs
				testLong = JsonValueTesting.RANDOM.nextLong();
				testValue = new JsonValue(testLong);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
				// test floats
				testFloat = JsonValueTesting.RANDOM.nextFloat();
				testValue = new JsonValue(testFloat);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
				// test double
				testDouble = JsonValueTesting.RANDOM.nextDouble();
				testValue = new JsonValue(testDouble);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
				// test BigDecimal
				testDecimal = new BigDecimal(testDouble);
				testValue = new JsonValue(testDecimal);
				serialised = JsonValue.parse(testValue.toJson());
				TestSubject.assertTestCondition(testValue.equals(serialised), 
						String.format("The JSON value created by parsing the string \"%s\" "
								+ "should have the value %s, but has %s.", testValue.toJson(),
								testValue, serialised));
			}
			// test arrays
			// TODO: test more than just empty arrays and objects
			JsonArray testArray = new JsonArray();
			testValue = new JsonValue(testArray);
			serialised = JsonValue.parse(testValue.toJson());
			TestSubject.assertTestCondition(testValue.equals(serialised), 
					String.format("The JSON value created by parsing the string \"%s\" "
							+ "should have the value %s, but has %s.", testValue.toJson(),
							testValue, serialised));
			// test objects
			JsonObject testObject = new JsonObject();
			testValue = new JsonValue(testObject);
			serialised = JsonValue.parse(testValue.toJson());
			TestSubject.assertTestCondition(testValue.equals(serialised), 
					String.format("The JSON value created by parsing the string \"%s\" "
							+ "should have the value %s, but has %s.", testValue.toJson(),
							testValue, serialised));
		} catch (JsonStandardException e) {
			throw new TestFailureException(String.format("The serialisation of \"%s\" "
					+ "did return \"%s\", which could not be parsed.", testValue, 
					testValue.toJson()), e);
		}

	}

}