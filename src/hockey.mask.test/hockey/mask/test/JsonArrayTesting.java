package hockey.mask.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonArray;
import hockey.mask.json.JsonNull;
import hockey.mask.json.JsonNumber;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonValue;
import hockey.mask.json.JsonValueTypes;
import hockey.mask.json.parser.JsonParser;
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
		JsonArrayTesting.testConstructors();
		JsonArrayTesting.testAddingGettingSize();
		JsonArrayTesting.testClear();
		JsonArrayTesting.testIsEmpty();
		JsonArrayTesting.testToJson();
		JsonArrayTesting.testParsing();
		JsonArrayTesting.testParsingNext();
		JsonArrayTesting.testType();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		JsonArray firstArray = new JsonArray();
		JsonArray secondArray = new JsonArray();
		TestSubject.assertTestCondition(firstArray.equals(secondArray), 
				String.format("The JSON array %s should equal %s.", firstArray, secondArray));
		TestSubject.assertTestCondition(!firstArray.equals(null), 
				String.format("The JSON array %s should not equal %s.", firstArray, null));
		secondArray.add(new JsonNull());
		TestSubject.assertTestCondition(!firstArray.equals(secondArray), 
				String.format("The JSON array %s should not equal %s.", firstArray, secondArray));
		firstArray.add(new JsonNull());
		TestSubject.assertTestCondition(firstArray.equals(secondArray), 
				String.format("The JSON array %s should equal %s.", firstArray, secondArray));
	}
	
	/**
	 * Test adding values to the JSON array and retrieving them. Additionally the size is tested.
	 * 
	 * @throws TestFailureException
	 */
	private static void testAddingGettingSize() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test simple add
			JsonArray simpleAdd = new JsonArray();
			int sizeSimpleAdd = JsonArrayTesting.RANDOM.nextInt(20);
			JsonValue[] testArray = new JsonValue[sizeSimpleAdd];
			for (int j = 0; j < sizeSimpleAdd; j++) {
				testArray[j] = new JsonNumber(JsonArrayTesting.RANDOM.nextInt());
				boolean addBool = simpleAdd.add(testArray[j]);
				TestSubject.assertTestCondition(addBool, 
						String.format("Adding the value %s to the JSON array %s should return true.", 
								testArray[j], simpleAdd));
			}
			TestSubject.assertTestCondition(simpleAdd.size() == sizeSimpleAdd, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							simpleAdd, sizeSimpleAdd, simpleAdd.size()));
			for (int j = 0; j < simpleAdd.size(); j++) {
				TestSubject.assertTestCondition(simpleAdd.get(j).equals(testArray[j]), 
						String.format("The JSON array %s should hold the value %s at index %s, "
								+ "but holds %s instead.", simpleAdd, testArray[j], j, simpleAdd.get(j)));
			}
			// test iterator with for-each loop
			JsonArray forEachArray = new JsonArray();
			for (JsonValue value : simpleAdd) {
				forEachArray.add(value);
			}
			TestSubject.assertTestCondition(simpleAdd.equals(forEachArray), 
					String.format("The JSON array %s should equal %s.", simpleAdd, forEachArray));
			// test insertion
			JsonValue insertionValue = new JsonNull();
			int insertionIndex = 0;
			if (simpleAdd.size() > 0) {
				insertionIndex = JsonArrayTesting.RANDOM.nextInt(simpleAdd.size());
			}
			simpleAdd.add(insertionIndex, insertionValue);
			TestSubject.assertTestCondition(simpleAdd.size() == sizeSimpleAdd + 1, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							simpleAdd, sizeSimpleAdd + 1, simpleAdd.size()));
			TestSubject.assertTestCondition(simpleAdd.get(insertionIndex).equals(insertionValue), 
					String.format("The JSON array %s should hold the value %s at index %s, "
							+ "but holds %s instead.", simpleAdd, insertionValue, insertionIndex, 
							simpleAdd.get(insertionIndex)));
			try {
				simpleAdd.add(simpleAdd.size() + 1, new JsonNull());
				throw new TestFailureException("Adding out of bounds to a JSON array should fail.");
			} catch (IndexOutOfBoundsException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test adding null
			JsonArray nullAdd = new JsonArray();
			boolean addNullBool = nullAdd.add(null);
			TestSubject.assertTestCondition(!addNullBool, 
					String.format("Adding null to the JSON array %s should not be possible.", 
							nullAdd));
			TestSubject.assertTestCondition(nullAdd.size() == 0, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							nullAdd, 0, nullAdd.size()));
			// test add all
			JsonArray addAll = new JsonArray();
			int sizeAddAll = JsonArrayTesting.RANDOM.nextInt(20);
			ArrayList<JsonValue> testArrayAll = new ArrayList<JsonValue>();
			for (int j = 0; j < sizeAddAll; j++) {
				testArrayAll.add(new JsonNumber(JsonArrayTesting.RANDOM.nextInt()));
			}
			boolean addAllBool = addAll.addAll(testArrayAll);
			if (testArrayAll.size() != 0) {
				TestSubject.assertTestCondition(addAllBool, 
						String.format("Adding the collection %s to the JSON array %s should return true.", 
								testArrayAll, addAll));
			} else {
				TestSubject.assertTestCondition(!addAllBool, 
						String.format("Adding the collection %s to the JSON array %s should return false.", 
								testArrayAll, addAll));
			}	
			TestSubject.assertTestCondition(addAll.size() == sizeAddAll, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							addAll, sizeAddAll, addAll.size()));
			for (int j = 0; j < addAll.size(); j++) {
				TestSubject.assertTestCondition(addAll.get(j).equals(testArrayAll.get(j)), 
						String.format("The JSON array %s should hold the value %s at index %s, "
								+ "but holds %s instead.", addAll, testArrayAll.get(j), j, addAll.get(j)));
			}
			// test adding null
			try {
				JsonArray addNullColl = new JsonArray();
				addNullColl.addAll(null);
				throw new TestFailureException("Adding a null collection to a JSON array should fail.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test null containing collections
			JsonArray addCollContainingNull = new JsonArray();
			int sizeBeforeNull = addCollContainingNull.size();
			ArrayList<JsonValue> testListContainingNull = new ArrayList<JsonValue>();
			for (int j = JsonArrayTesting.RANDOM.nextInt(20); j >= 0; j--) {
				testListContainingNull.add(new JsonNumber(JsonArrayTesting.RANDOM.nextInt()));
				testListContainingNull.add(null);
			}
			boolean addNullContainingListBool = addCollContainingNull.addAll(testListContainingNull);
			TestSubject.assertTestCondition(!addNullContainingListBool, 
					String.format("Adding the collection %s to the JSON array %s should return false.", 
							testListContainingNull, addCollContainingNull));
			TestSubject.assertTestCondition(addCollContainingNull.size() == sizeBeforeNull, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							addCollContainingNull, sizeBeforeNull, addCollContainingNull.size()));
			// test inserting collections
			JsonArray insertAll = new JsonArray();
			int sizeInsert = JsonArrayTesting.RANDOM.nextInt(20);
			ArrayList<JsonValue> testInsertList = new ArrayList<JsonValue>();
			ArrayList<JsonValue> testInsertListAfter = new ArrayList<JsonValue>();
			for (int j = 0; j < sizeInsert; j++) {
				testInsertList.add(new JsonNumber(JsonArrayTesting.RANDOM.nextInt()));
				insertAll.add(testInsertList.get(j));
				testInsertListAfter.add(testInsertList.get(j));
			}
			int listInsertionIndex = 0;
			if (sizeInsert != 0) {
				listInsertionIndex = JsonArrayTesting.RANDOM.nextInt(sizeInsert);
			}
			boolean insertAllBool = insertAll.addAll(listInsertionIndex, testInsertList);
			testInsertListAfter.addAll(listInsertionIndex, testInsertList);
			if (testInsertList.size() != 0) {
				TestSubject.assertTestCondition(insertAllBool, 
						String.format("Adding the collection %s to the JSON array %s at index %s should return true.", 
								testInsertList, insertAll, insertionIndex));
			} else {
				TestSubject.assertTestCondition(!insertAllBool, 
						String.format("Adding the collection %s to the JSON array %s at index %s should return false.", 
								testInsertList, insertAll, insertionIndex));
			}	
			TestSubject.assertTestCondition(insertAll.size() == sizeInsert * 2, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							insertAll, sizeInsert * 2, insertAll.size()));
			
			for (int j = 0; j < insertAll.size(); j++) {
				TestSubject.assertTestCondition(insertAll.get(j).equals(testInsertListAfter.get(j)), 
						String.format("The JSON array %s should hold the value %s at index %s, "
								+ "but holds %s instead.", insertAll, testInsertListAfter.get(j), j, insertAll.get(j)));
			}
			// test inserting null
			try {
				JsonArray insertNullColl = new JsonArray();
				insertNullColl.addAll(0, null);
				throw new TestFailureException("Inserting a null collection into a JSON array should fail.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test inserting null containing collections
			JsonArray insertCollContainingNull = new JsonArray();
			int sizeBeforeNullInsertion = insertCollContainingNull.size();
			ArrayList<JsonValue> testListContainingNullForInsertion = new ArrayList<JsonValue>();
			for (int j = JsonArrayTesting.RANDOM.nextInt(20); j >= 0; j--) {
				testListContainingNullForInsertion.add(new JsonNumber(JsonArrayTesting.RANDOM.nextInt()));
				testListContainingNullForInsertion.add(null);
			}
			boolean insertNullContainingListBool = insertCollContainingNull.addAll(0, testListContainingNullForInsertion);
			TestSubject.assertTestCondition(!insertNullContainingListBool, 
					String.format("Inserting the collection %s into the JSON array %s should return false.", 
							testListContainingNullForInsertion, insertCollContainingNull));
			TestSubject.assertTestCondition(insertCollContainingNull.size() == sizeBeforeNullInsertion, 
					String.format("The JSON array %s should have the size %s, but has %s.", 
							insertCollContainingNull, sizeBeforeNullInsertion, insertCollContainingNull.size()));

		}
	}
	
	/**
	 * Test clearing JSON arrays.
	 * 
	 * @throws TestFailureException
	 */
	private static void testClear() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			JsonArray testArray = new JsonArray();
			testArray.clear();
			TestSubject.assertTestCondition(testArray.size() == 0, String.format("The JSON array %s "
					+ "should be empty after clearing.", testArray));
			TestSubject.assertTestCondition(testArray.isEmpty(), String.format("The JSON array %s "
					+ "should be empty after clearing.", testArray));
			testArray.addAll(JsonArrayTesting.generateRandomList());
			testArray.clear();
			TestSubject.assertTestCondition(testArray.size() == 0, String.format("The JSON array %s "
					+ "should be empty after clearing.", testArray));
			TestSubject.assertTestCondition(testArray.isEmpty(), String.format("The JSON array %s "
					+ "should be empty after clearing.", testArray));
		}
		
	}
	
	/**
	 * Test checking empty JSON arrays.
	 * 
	 * @throws TestFailureException
	 */
	private static void testIsEmpty() throws TestFailureException {
		JsonArray testArray = new JsonArray();
		TestSubject.assertTestCondition(testArray.isEmpty(), String.format("The JSON array %s "
				+ "should be empty.", testArray));
		testArray.add(new JsonNull());
		TestSubject.assertTestCondition(!testArray.isEmpty(), String.format("The JSON array %s "
				+ "should not be empty.", testArray));
		testArray.clear();
		TestSubject.assertTestCondition(testArray.isEmpty(), String.format("The JSON array %s "
				+ "should be empty.", testArray));
	}
	
	/**
	 * Test conversion to the JSON format.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			JsonArray testArray = new JsonArray();
			ArrayList<JsonValue> testList = JsonArrayTesting.generateRandomList();
			String perfectString = JsonArray.JSON_ARRAY_START_IDENTIFIER;
			for (int j = 0; j < testList.size(); j++) {
				testArray.add(testList.get(j));
				perfectString += testList.get(j).toJson();
				if (j < testList.size() - 1) {
					perfectString += JsonArray.JSON_ARRAY_VALUE_SEPARATOR;
				}
			}
			perfectString += JsonArray.JSON_ARRAY_END_IDENTIFIER;
			TestSubject.assertTestCondition(testArray.toJson().equals(perfectString), 
					String.format("The JSON array's JSON representation should be \"%s\", "
							+ "but is \"%s\".",	perfectString, testArray.toJson()));
		}		
	}
	
	/**
	 * Test the parsing of JSON formatted arrays to JSON arrays.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonArray.parse(null);
			throw new TestFailureException("Parsing of null for a JSON array should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON array should throw a "
					+ "NullPointerException.", e);
		}
		// test empty strings
		try {
			JsonArray.parse("");
			throw new TestFailureException("Parsing of empty for a JSON array should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test random arrays
		for (int i = 0; i < 10000; i++) {
			JsonArray initialArray = JsonArrayTesting.generateRandomArray();
			try {
				JsonArray parsedArray = JsonArray.parse(initialArray.toJson());
				TestSubject.assertTestCondition(initialArray.equals(parsedArray), 
						String.format("The JSON array %s should equal the array %s parsed from "
								+ "the JSON formatted string \"%s\".",	initialArray, parsedArray, initialArray.toJson()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("The string \"%s\" violates the JSON standard.", 
						initialArray.toJson()), e);
			}
		}
	}
	
	/**
	 * Test sequentially parsing JSON formatted array.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonArray.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON array should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON arrays
		for (int i = 0; i < 10000; i++) {
			JsonArray[] jsonTestArrays = new JsonArray[JsonArrayTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestArrays.length; j++) {
				// create random arrays
				jsonTestArrays[j] = JsonArrayTesting.generateRandomArray();
				testString += jsonTestArrays[j].toJson() + "   ";
			}
			try {
				JsonParser jp = new JsonParser(testString);
				JsonArray[] parsedJsonArrays = new JsonArray[jsonTestArrays.length];
				for (int j = 0; j < jsonTestArrays.length; j++) {
					parsedJsonArrays[j] = JsonArray.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestArrays, parsedJsonArrays),
					String.format("The parsed JSON strings %s should equal %s.", 
							Arrays.toString(parsedJsonArrays), 
							Arrays.toString(jsonTestArrays)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "arrays failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		for (int i = 0; i < 10000; i++) {
			JsonArray jsonTestString = JsonArrayTesting.generateRandomArray();
			String testString = jsonTestString.toJson();
			// remove the last bracket so an exception will be raised
			testString = testString.substring(0, testString.length() - 1);
			try {
				JsonParser jp = new JsonParser(testString);
				int initialPosition = jp.getPosition();
				try {
					JsonArray.parseNext(jp);
					throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
							+ "arrays should fail, but resulted in the parser %s.", 
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
	
	/**
	 * Test getting the correct type for a JSON array.
	 * 
	 * @throws TestFailureException
	 */
	private static void testType() throws TestFailureException {
			JsonArray testArray = new JsonArray();
			TestSubject.assertTestCondition(testArray.getType() == JsonValueTypes.ARRAY, 
					String.format("The JSON array %s should be of type %s, but is of type %s "
					+ "instead.", testArray, JsonValueTypes.ARRAY, testArray.getType()));
	}
	
	/**
	 * Generate a JSON array with random elements.
	 * 
	 * @return a random JSON array
	 */
	private static JsonArray generateRandomArray() {
		JsonArray randomArray = new JsonArray();
		int arraySize = JsonArrayTesting.RANDOM.nextInt(20);
		for (int j = 0; j < arraySize; j++) {
			randomArray.add(new JsonNumber(JsonArrayTesting.RANDOM.nextInt()));
		}
		return randomArray;
	}
	
	/**
	 * Generate a random list of JSON values.
	 * 
	 * @return a random JSON value list
	 */
	private static ArrayList<JsonValue> generateRandomList() {
		ArrayList<JsonValue> randomList = new ArrayList<JsonValue>();
		int listSize = JsonArrayTesting.RANDOM.nextInt(20);
		for (int j = 0; j < listSize; j++) {
			randomList.add(new JsonNumber(JsonArrayTesting.RANDOM.nextInt()));
		}
		return randomList;
	}

}
