package hockey.mask.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import hockey.mask.json.JsonArray;
import hockey.mask.json.JsonNumber;
import hockey.mask.json.JsonObject;
import hockey.mask.json.JsonPair;
import hockey.mask.json.JsonStandardException;
import hockey.mask.json.JsonString;
import hockey.mask.json.JsonValue;
import hockey.mask.json.parser.JsonParser;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonObjectTesting class test the JsonObject class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonObjectTesting implements TestSubject {

	private static final Random RANDOM = new Random();
	
	@Override
	public void runAllTests() throws TestFailureException {
		JsonObjectTesting.testConstructors();
		JsonObjectTesting.testAdding();
		JsonObjectTesting.testAddingAll();
		JsonObjectTesting.testClear();
		JsonObjectTesting.testHasMembers();
		JsonObjectTesting.testHasMember();
		JsonObjectTesting.testRemove();
		JsonObjectTesting.testSetting();
		JsonObjectTesting.testGetNames();
		JsonObjectTesting.testGetting();
		JsonObjectTesting.testToJson();
		JsonObjectTesting.testParsing();
		JsonObjectTesting.testParsingNext();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		JsonObject firstObject = new JsonObject();
		JsonObject secondObject = new JsonObject();
		TestSubject.assertTestCondition(firstObject.equals(secondObject), 
				String.format("The JSON object %s should equal %s.", firstObject, secondObject));
		TestSubject.assertTestCondition(!firstObject.equals(null), 
				String.format("The JSON object %s should not equal %s.", firstObject, null));
		JsonPair pair = JsonObjectTesting.generateRandomPair();
		secondObject.add(pair);
		TestSubject.assertTestCondition(!firstObject.equals(secondObject), 
				String.format("The JSON object %s should not equal %s.", firstObject, secondObject));
		firstObject.add(pair);
		TestSubject.assertTestCondition(firstObject.equals(secondObject), 
				String.format("The JSON object %s should equal %s.", firstObject, secondObject));
	}
	
	/**
	 * Test adding members to JSON objects. Additionally tests size function and iterator.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testAdding() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test adding null
			try {
				JsonObject addNull = new JsonObject();
				addNull.add(null);
				throw new TestFailureException("Adding a null member to a JSON object should fail.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test adding random members
			JsonObject addObject = new JsonObject();
			ArrayList<JsonPair> testMembers = JsonObjectTesting.generateRandomMemberList();
			for (int j = 0; j < testMembers.size(); j++) {
				addObject.add(testMembers.get(j));
			}
			TestSubject.assertTestCondition(addObject.size() == testMembers.size(), 
					String.format("The JSON object %s should have the size %s, but has %s.", 
							addObject, testMembers.size(), addObject.size()));
			int indexMember = 0;
			for (JsonPair member : addObject) {
				TestSubject.assertTestCondition(member.equals(testMembers.get(indexMember)), 
						String.format("The JSON object %s should hold the value %s at index %s, "
								+ "but holds %s instead.", addObject, testMembers.get(indexMember), indexMember, member));
				indexMember++;
			}
		}
	}
	
	/**
	 * Test adding multiple members to JSON objects. Additionally tests size function and iterator.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testAddingAll() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test adding null
			try {
				JsonObject addNull = new JsonObject();
				addNull.addAll(null);
				throw new TestFailureException("Adding a null member to a JSON object should fail.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test adding null containing collections
			JsonObject addContainNullObject = new JsonObject();
			ArrayList<JsonPair> testContainNullMembers = JsonObjectTesting.generateRandomMemberList();
			testContainNullMembers.add(null);
			boolean addContainNullBool = addContainNullObject.addAll(testContainNullMembers);
			TestSubject.assertTestCondition(!addContainNullBool, 
					String.format("Adding the collection %s to the JSON object %s should return false.", 
							testContainNullMembers, addContainNullObject));
			TestSubject.assertTestCondition(addContainNullObject.size() == 0, 
					String.format("The JSON object %s should have the size %s, but has %s.", 
							addContainNullObject, 0, addContainNullObject.size()));
			// test adding random members
			JsonObject addObject = new JsonObject();
			ArrayList<JsonPair> testMembers = JsonObjectTesting.generateRandomMemberList();
			boolean addBool = addObject.addAll(testMembers);
			if (testMembers.size() != 0) {
				TestSubject.assertTestCondition(addBool, 
						String.format("Adding the collection %s to the JSON object %s should return true.", 
								testMembers, addObject));
			} else {
				TestSubject.assertTestCondition(!addBool, 
						String.format("Adding the collection %s to the JSON object %s should return false.", 
								testMembers, addObject));
			}
			TestSubject.assertTestCondition(addObject.size() == testMembers.size(), 
					String.format("The JSON object %s should have the size %s, but has %s.", 
							addObject, testMembers.size(), addObject.size()));
			int indexMember = 0;
			for (JsonPair member : addObject) {
				TestSubject.assertTestCondition(member.equals(testMembers.get(indexMember)), 
						String.format("The JSON object %s should hold the value %s at index %s, "
								+ "but holds %s instead.", addObject, testMembers.get(indexMember), indexMember, member));
				indexMember++;
			}
		}
	}
	
	/**
	 * Test clearing JSON objects of any members.
	 * 
	 * @throws TestFailureException
	 */
	private static void testClear() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			JsonObject testObject = new JsonObject();
			testObject.clear();
			TestSubject.assertTestCondition(testObject.size() == 0, String.format("The JSON object %s "
					+ "should be empty after clearing.", testObject));
			TestSubject.assertTestCondition(!testObject.hasMembers(), String.format("The JSON object %s "
					+ "should not have any members after clearing.", testObject));
			testObject = JsonObjectTesting.generateRandomObject();
			testObject.clear();
			TestSubject.assertTestCondition(testObject.size() == 0, String.format("The JSON object %s "
					+ "should be empty after clearing.", testObject));
			TestSubject.assertTestCondition(!testObject.hasMembers(), String.format("The JSON object %s "
					+ "should not have any members after clearing.", testObject));
		}
	}
	
	/**
	 * Test checking JSON objects for members.
	 * 
	 * @throws TestFailureException
	 */
	private static void testHasMembers() throws TestFailureException {
		JsonObject testObject = new JsonObject();
		TestSubject.assertTestCondition(!testObject.hasMembers(), String.format("The JSON object %s "
				+ "should not have any members.", testObject));
		testObject.add(JsonObjectTesting.generateRandomPair());
		TestSubject.assertTestCondition(testObject.hasMembers(), String.format("The JSON object %s "
				+ "should have members.", testObject));
	}
	
	/**
	 * Test checking JSON objects for specific members.
	 * 
	 * @throws TestFailureException
	 */
	private static void testHasMember() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			JsonObject testObject = JsonObjectTesting.generateRandomObject();
			TestSubject.assertTestCondition(!testObject.hasMember(null), String.format("The JSON object %s "
					+ "can not contain a member with a null key.", testObject));
			JsonPair query = JsonObjectTesting.generateRandomPair();
			testObject.add(query);
			TestSubject.assertTestCondition(testObject.hasMember(query.getName()), String.format("The JSON object %s "
					+ "should have the member %s.", testObject, query.getName()));
			testObject.remove(query.getName());
			TestSubject.assertTestCondition(!testObject.hasMember(query.getName()), String.format("The JSON object %s "
					+ "should not have the member %s.", testObject, query.getName()));
		}
	}
	
	/**
	 * Test removing members from JSON objects.
	 * 
	 * @throws TestFailureException
	 */
	private static void testRemove() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test removal of null
			JsonObject testObject = JsonObjectTesting.generateRandomObject();
			int sizeBeforeRemoval = testObject.size();
			testObject.remove(null);
			TestSubject.assertTestCondition(testObject.size() == sizeBeforeRemoval, String.format("The JSON object %s "
					+ "should have the size %s after removal of null, but has %s.", testObject, 
					sizeBeforeRemoval, testObject.size()));
			// test removal of random member
			JsonPair query = JsonObjectTesting.generateRandomPair();
			testObject.remove(query.getName()); // remove potential collisions to ensure a correct test
			int sizeBefore = testObject.size();
			for (int j = JsonObjectTesting.RANDOM.nextInt(20); j >= 0; j--) {
				testObject.add(query);
			}
			testObject.remove(query.getName());
			TestSubject.assertTestCondition(testObject.size() == sizeBefore, String.format("The JSON object %s "
					+ "should have the size %s after removal of %s, but has %s.", testObject, 
					sizeBeforeRemoval, query.getName(), testObject.size()));
			TestSubject.assertTestCondition(!testObject.hasMember(query.getName()), String.format("The JSON object %s "
					+ "should not have the member %s after removal.", testObject, query.getName()));
		}
	}
	
	/**
	 * Test setting members to specific JSON values.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testSetting() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test setting a null key
			try {
				JsonObject setNull = new JsonObject();
				setNull.set(null, new JsonValue());
				throw new TestFailureException("Setting a null member at a JSON object should fail.");
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			// test setting a null value
			JsonObject nullValueObject = new JsonObject();
			JsonString nullValueKey = JsonObjectTesting.generateRandomString();
			nullValueObject.set(nullValueKey, null);
			JsonValue someNull = new JsonValue();
			TestSubject.assertTestCondition(nullValueObject.get(nullValueKey).equals(someNull), 
					String.format("Setting the member %s of the JSON object %s to null should result "
							+ "in the value %s, but resulted into %s.", 
							nullValueKey, nullValueObject, someNull, nullValueObject.get(nullValueKey)));
			// test adding new members by setting
			JsonObject addObject = JsonObjectTesting.generateRandomObject();
			JsonPair addPair = JsonObjectTesting.generateRandomPair();
			addObject.remove(addPair.getName()); // remove potential collisions to ensure a correct test
			int sizeBeforeAdding = addObject.size();
			JsonValue addReturn  = addObject.set(addPair.getName(), addPair.getValue());
			TestSubject.assertTestCondition(addObject.size() == sizeBeforeAdding + 1, 
					String.format("The JSON object %s should have the size %s, but has %s.", 
							addObject, sizeBeforeAdding + 1, addObject.size()));
			TestSubject.assertTestCondition(addReturn == null, 
					String.format("Adding the member %s to the JSON object %s by setting should return "
							+ "null, but returned %s instead.", 
							addPair, addObject, addReturn));
			TestSubject.assertTestCondition(addPair.getValue().equals(addObject.get(addPair.getName())), 
					String.format("The JSON object %s should hold the value %s at member %s, "
							+ "but holds %s instead.", addObject, addPair.getValue(), 
							addPair.getName(), addObject.get(addPair.getName())));
			// test setting existent members
			JsonObject setObject = JsonObjectTesting.generateRandomObject();
			JsonPair setPair = JsonObjectTesting.generateRandomPair();
			setObject.remove(setPair.getName()); // remove potential collisions to ensure a correct test
			setObject.set(setPair.getName(), setPair.getValue());
			JsonValue newValue = new JsonValue(new JsonNumber(JsonObjectTesting.RANDOM.nextInt()));
			int sizeBeforeSetting = setObject.size();
			JsonValue setReturn  = setObject.set(setPair.getName(), newValue);

			TestSubject.assertTestCondition(setObject.size() == sizeBeforeSetting, 
					String.format("The JSON object %s should have the size %s, but has %s.", 
							setObject, sizeBeforeSetting, setObject.size()));
			TestSubject.assertTestCondition(setReturn.equals(setPair.getValue()), 
					String.format("Setting the member %s to %s for the JSON object %s should return "
							+ "%s, but returned %s instead.", 
							setPair, newValue, setObject, setPair.getValue(), setReturn));
			TestSubject.assertTestCondition(newValue.equals(setObject.get(setPair.getName())), 
					String.format("The JSON object %s should hold the value %s at member %s, "
							+ "but holds %s instead.", setObject, newValue, 
							setPair.getName(), setObject.get(setPair.getName())));
		}
	}
	
	/**
	 * Test getting the member names of the JSON object.
	 * 
	 * @throws TestFailureException
	 */
	private static void testGetNames() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			JsonObject testObject = JsonObjectTesting.generateRandomObject();
			JsonString[] memberNames = testObject.getNames();
			for (JsonString name : memberNames) {
				TestSubject.assertTestCondition(testObject.hasMember(name), 
						String.format("The JSON object %s should contain the member %s "
						+ "as its name has been returned in the member array %s.", testObject, name, 
						Arrays.toString(memberNames)));
			}
			TestSubject.assertTestCondition(testObject.size() == memberNames.length, 
					String.format("The JSON object %s has %s members, but returning their name "
					+ "as array %s yielded only %s.", testObject, testObject.size(), 
					Arrays.toString(memberNames), memberNames.length));
		}
	}
	
	/**
	 * Test getting the values of specific members of the JSON object.
	 * 
	 * @throws TestFailureException
	 */
	private static void testGetting() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test singular members
			JsonObject testObject = JsonObjectTesting.generateRandomObject();
			testObject.add(JsonObjectTesting.generateRandomPair()); // needed since empty objects can be generated
			// pick a random member
			JsonString[] memberNames = testObject.getNames();
			JsonString testMember = memberNames[JsonObjectTesting.RANDOM.nextInt(memberNames.length)];
			// ensure its appearance is singular by removing and readding it with a defined value
			testObject.remove(testMember);
			TestSubject.assertTestCondition(testObject.get(testMember) == null, 
					String.format("The JSON object %s should hold the value %s for the member %s "
					+ "but holds %s instead.", testObject, null, testMember, testObject.get(testMember)));
			JsonValue testValue = new JsonValue(new JsonNumber(JsonObjectTesting.RANDOM.nextInt()));
			testObject.set(testMember, testValue);
			TestSubject.assertTestCondition(testObject.get(testMember).equals(testValue), 
					String.format("The JSON object %s should hold the value %s for the member %s "
					+ "but holds %s instead.", testObject, testValue, testMember, testObject.get(testMember)));
			TestSubject.assertTestCondition(testObject.get(null) == null, 
					String.format("The JSON object %s should hold the value %s for the member %s "
					+ "but holds %s instead.", testObject, null, null, testObject.get(null)));
			// testing same member occurring multiple time
			testObject.remove(testMember);
			int memberCount = JsonObjectTesting.RANDOM.nextInt(20);
			JsonValue[] testValues = new JsonValue[memberCount];
			for (int j = 0; j < testValues.length; j++) {
				testValues[j] = new JsonValue(new JsonNumber(JsonObjectTesting.RANDOM.nextInt()));
				testObject.add(new JsonPair(testMember, testValues[j]));
			}
			TestSubject.assertTestCondition(Arrays.equals(testObject.getValues(testMember), testValues), 
					String.format("The JSON object %s should hold the values %s for the member %s "
					+ "but holds %s instead.", testObject, Arrays.toString(testValues), testMember, Arrays.toString(testObject.getValues(testMember))));
			// test null
			JsonValue[] emptyArray = new JsonValue[0];
			TestSubject.assertTestCondition(Arrays.equals(testObject.getValues(null), emptyArray), 
					String.format("The JSON object %s should hold the values %s for the member %s "
					+ "but holds %s instead.", testObject, Arrays.toString(emptyArray), null, Arrays.toString(testObject.getValues(null))));
		}
	}
	
	/**
	 * Test conversion to the JSON format.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testToJson() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			JsonObject testObject = new JsonObject();
			ArrayList<JsonPair> testList = JsonObjectTesting.generateRandomMemberList();
			String perfectString = JsonObject.JSON_OBJECT_START_IDENTIFIER;
			for (int j = 0; j < testList.size(); j++) {
				testObject.add(testList.get(j));
				perfectString += testList.get(j).toJson();
				if (j < testList.size() - 1) {
					perfectString += JsonObject.JSON_OBJECT_PAIR_SEPARATOR;
				}
			}
			perfectString += JsonObject.JSON_OBJECT_END_IDENTIFIER;
			TestSubject.assertTestCondition(testObject.toJson().equals(perfectString), 
					String.format("The JSON object's JSON representation should be \"%s\", "
							+ "but is \"%s\".",	perfectString, testObject.toJson()));
		}		
	}
	
	/**
	 * Test the parsing of JSON formatted objects to JSON objects.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsing() throws TestFailureException {
		// test JSON null strings
		try {
			JsonObject.parse(null);
			throw new TestFailureException("Parsing of null for a JSON object should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON object should throw a "
					+ "NullPointerException.", e);
		}
		// test empty strings
		try {
			JsonObject.parse("");
			throw new TestFailureException("Parsing of empty for a JSON object should fail.");
		} catch (JsonStandardException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		}
		// test random objects
		for (int i = 0; i < 10000; i++) {
			JsonObject initialObject = JsonObjectTesting.generateRandomObject();
			try {
				JsonObject parsedObject = JsonObject.parse(initialObject.toJson());
				TestSubject.assertTestCondition(initialObject.equals(parsedObject), 
						String.format("The JSON object %s should equal the object %s parsed from "
								+ "the JSON formatted string \"%s\".",	initialObject, parsedObject, initialObject.toJson()));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("The string \"%s\" violates the JSON standard.", 
						initialObject.toJson()), e);
			}
		}
	}
	
	/**
	 * Test sequentially parsing JSON formatted object.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testParsingNext() throws TestFailureException {
		// test null
		try {
			JsonObject.parseNext(null);
			throw new TestFailureException("Parsing of null for a JSON parser should fail.");
		} catch (NullPointerException e) {
			/*
			 * Do nothing as this is expected behaviour.
			 */
		} catch (JsonStandardException e) {
			throw new TestFailureException("Parsing of null for a JSON object should throw a "
					+ "NullPointerException.", e);
		}
		// test JSON objects
		for (int i = 0; i < 10000; i++) {
			JsonObject[] jsonTestObjects = new JsonObject[JsonObjectTesting.RANDOM.nextInt(20) + 1];
			String testString = "  "; // Some whitespace
			for (int j = 0; j < jsonTestObjects.length; j++) {
				// create random objects
				jsonTestObjects[j] = JsonObjectTesting.generateRandomObject();
				testString += jsonTestObjects[j].toJson() + "   ";
			}
			try {
				JsonParser jp = new JsonParser(testString);
				JsonObject[] parsedJsonObjects = new JsonObject[jsonTestObjects.length];
				for (int j = 0; j < jsonTestObjects.length; j++) {
					parsedJsonObjects[j] = JsonObject.parseNext(jp);
				}
				TestSubject.assertTestCondition(Arrays.equals(jsonTestObjects, parsedJsonObjects),
					String.format("The parsed JSON strings %s should equal %s.", 
							Arrays.toString(parsedJsonObjects), 
							Arrays.toString(jsonTestObjects)));
			} catch (JsonStandardException e) {
				throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
						+ "objects failed.", testString
						), e);
			}
		}
		// test reseting position mark after exception
		for (int i = 0; i < 10000; i++) {
			JsonObject jsonTestString = JsonObjectTesting.generateRandomObject();
			String testString = jsonTestString.toJson();
			// remove the last curly bracket so an exception will be raised
			testString = testString.substring(0, testString.length() - 1);
			try {
				JsonParser jp = new JsonParser(testString);
				int initialPosition = jp.getPosition();
				try {
					JsonArray.parseNext(jp);
					throw new TestFailureException(String.format("Parsing of the string \"%s\" as JSON "
							+ "objects should fail, but resulted in the parser %s.", 
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
	 * Generate a JSON object with random members.
	 * 
	 * @return a random JSON object
	 */
	private static JsonObject generateRandomObject() {
		JsonObject randomObject = new JsonObject();
		int objectSize = JsonObjectTesting.RANDOM.nextInt(20);
		for (int j = 0; j < objectSize; j++) {
			randomObject.add(JsonObjectTesting.generateRandomPair());
		}
		return randomObject;
	}
	
	/**
	 * Generate a random list of JSON pairs to be used as members.
	 * 
	 * @return a random JSON pair list
	 */
	private static ArrayList<JsonPair> generateRandomMemberList() {
		ArrayList<JsonPair> randomList = new ArrayList<JsonPair>();
		int objectSize = JsonObjectTesting.RANDOM.nextInt(20);
		for (int j = 0; j < objectSize; j++) {
			randomList.add(JsonObjectTesting.generateRandomPair());
		}
		return randomList;
	}
	
	/**
	 * Generates a random JSON string, which may contain an empty string.
	 * 
	 * @return a random JSON string
	 */
	private static JsonString generateRandomString() {
		byte[] randomString = new byte[JsonObjectTesting.RANDOM.nextInt(200)];
		JsonObjectTesting.RANDOM.nextBytes(randomString);
		return new JsonString(new String(randomString));
	}
	
	/**
	 * Generate a random JSON pair.
	 * 
	 * @return a random JSON pair
	 */
	private static JsonPair generateRandomPair() {
		try {
			return new JsonPair(JsonObjectTesting.generateRandomString(), 
					new JsonValue(new JsonNumber(JsonObjectTesting.RANDOM.nextDouble())));
		} catch (JsonStandardException e) {
			return new JsonPair(JsonObjectTesting.generateRandomString(), 
					new JsonValue(new JsonNumber(JsonObjectTesting.RANDOM.nextLong())));
		}
	}

}
