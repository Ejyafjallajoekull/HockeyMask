package hockey.mask.test;

import java.math.BigDecimal;
import java.util.Random;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonValue;
import hockey.mask.json.JsonValueTypes;
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
		JsonValueTesting.testConstructors();
		JsonValueTesting.testSetterGetter();
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
	
	public static void testConstructors() {
		//TODO: implement once JsonValue.equals() works
	}
	
	/**
	 * Test setting and getting the JsonValue.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	public static void testSetterGetter() throws TestFailureException {
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
		}
		
	}

}
