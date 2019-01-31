package hockey.mask.json.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParser;
import hockey.mask.json.parser.JsonStringParser;

/**
 * The JsonArray class represents an array formatted in the JSON standard. 
 * Internally however it will be backed up by a list for convenience.
 * 
 * @author Planters
 *
 */
public final class JsonArray extends JsonValue implements List<JsonValue> {
	
	/**
	 *  The identifier used to identify the start of a JSON formatted array.
	 */
	public static final String JSON_ARRAY_START_IDENTIFIER = "[";
	
	/**
	 *  The identifier used to identify the end of a JSON formatted array.
	 */
	public static final String JSON_ARRAY_END_IDENTIFIER = "]";

	/**
	 *  The separator used to separate JSON formatted values contained in 
	 *  a JSON formatted array.
	 */
	public static final String JSON_ARRAY_VALUE_SEPARATOR = ",";
	
	private final List<JsonValue> array = new ArrayList<JsonValue>();
		
	/**
	 * Create a new, empty JSON array.
	 */
	public JsonArray() {
		super();
	}
	
	/**
	 * Create a new JSON array containing the specified content.
	 * 
	 * @param content - the content to be contained
	 */
//	public JsonArray(Collection<? extends JsonValue> content) {
//		super(content);
//	}
	
	/**
	 * Convert this JsonArray to a JSON formatted array string. 
	 * Null values will be interpreted as JSON value of type null.
	 * 
	 * @return the JSON representation of this array
	 */
	@Override
	public String toJson() {
		return this.array.stream().map(JsonValue::toJson)
		.collect(Collectors.joining(JsonArray.JSON_ARRAY_VALUE_SEPARATOR, JsonArray.JSON_ARRAY_START_IDENTIFIER, JsonArray.JSON_ARRAY_END_IDENTIFIER));
	}
	
	/**
	 * Parse the specified JSON formatted array and return its internal representation.
	 * 
	 * @param jsonArray - the JSON formatted array
	 * @return the internal representation of the JSON formatted array
	 * @throws JsonStandardException thrown if the string was not JSON formatted
	 * @throws NullPointerException - if null is passed as JSON input string
	 */
	public static JsonArray parse(String jsonArray) throws JsonStandardException {
		Objects.requireNonNull(jsonArray, "A JSON formatted array may not be null.");
		JsonStringParser jp = new JsonStringParser(jsonArray);
		JsonArray parsedArray = JsonArray.parseNext(jp);
		jp.skipWhitespace(); // needed for checking against garbage data
		if (!jp.hasNext()) {
			return parsedArray;
		} else { // the string should not contain any more garbage data
			throw new JsonStandardException(String.format("The string \"%s\" is not a pure JSON array.", 
					jsonArray)); 
		}
	}
	
	/**
	 * Parse the next JSON formatted array from the specified JSON parser and return its 
	 * internal representation.
	 * 
	 * @param parser - the parser to retrieve the JSON formatted array from
	 * @return the internal representation of the JSON formatted array
	 * @throws JsonStandardException if the next element in the parser is not a JSON formatted array
	 * @throws NullPointerException - if null is passed as JSON parser
	 */
	public static JsonArray parseNext(JsonParser parser) throws JsonStandardException {
		Objects.requireNonNull(parser, "The JSON parser may not be null.");
		int startingPosition = parser.getPosition();
		parser.skipWhitespace();
		// start condition
		if (parser.isNext(JsonArray.JSON_ARRAY_START_IDENTIFIER, true)) {
			JsonArray parsedArray = new JsonArray();
			boolean firstPassed = false;
			while (parser.hasNext()) {
				parser.skipWhitespace();
				// end condition
				if (parser.isNext(JsonArray.JSON_ARRAY_END_IDENTIFIER, true)) {
					return parsedArray;
				} else { // append value
					if (firstPassed) {
						if (parser.isNext(JsonArray.JSON_ARRAY_VALUE_SEPARATOR, true)) {
							parsedArray.add(JsonValue.parseNext(parser));
						} else {
							// throw exception if there is no value separation
							parser.setPosition(startingPosition); // the parser should not be modified
							throw new JsonStandardException(String.format("The next element in the JSON parser "
									+ "%s is not a JSON array.", parser));
						}
					} else { // there is no separator on the first element
						parsedArray.add(JsonValue.parseNext(parser));
						firstPassed = true;
					}
				}
			}
		}
		// throw exception if end condition is not reached
		parser.setPosition(startingPosition); // the parser should not be modified
		throw new JsonStandardException(String.format("The next element in the JSON parser "
				+ "%s is not a JSON array.", parser));
	}
	
	/**
	 * Appends the specified element to the end of the JSON array.
	 * Null is not permitted.
	 * 
	 * @param value - the value to append
	 * @return true if the value has been added successfully, false if null is passed
	 */
	@Override
	public boolean add(JsonValue value) {
		if (value != null) {
			return this.array.add(value);
		} else {
			return false;
		}
	}

	/**
	 * Appends all elements of the specified collection to this JSON array.
	 * This method will fail if the collection contains null values.
	 * 
	 * @param elementsToAdd - the collection to append
	 * @return true if the collection has successfully been added, false if the collection 
	 * contains null
	 * @throws NullPointerException if the specified collection is null
	 */
	@Override
	public boolean addAll(Collection<? extends JsonValue> elementsToAdd) {
		Objects.requireNonNull(elementsToAdd, "The collection " + elementsToAdd + " cannot be added.");
		if (!elementsToAdd.contains(null)) {
			return this.array.addAll(elementsToAdd);
		} else {
			return false;
		}
	}
	
	/**
	 * Insert the specified element at the specified index into the JSON array.
	 * 
	 * @param index - the index at which to insert the element
	 * @param value - the element to insert
	 * @throws NullPointerException if the element is null
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public void add(int index, JsonValue value) {
		Objects.requireNonNull(value, "Null cannot be added to a JSON array.");
		this.array.add(index, value);		
	}

	/**
	 * Inserts all elements of the specified collection into this JSON array at the specified position.
	 * This method will fail if the collection contains null values.
	 * 
	 * @param elementsToAdd - the collection to insert
	 * @return true if the collection has successfully been added, false if the collection 
	 * contains null
	 * @throws NullPointerException if the specified collection is null
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public boolean addAll(int index, Collection<? extends JsonValue> elementsToAdd) {
		Objects.requireNonNull(elementsToAdd, String.format("The collection %s cannot be added at "
				+ "position %s.", elementsToAdd, index));
		if (!elementsToAdd.contains(null)) {
			return this.array.addAll(index, elementsToAdd);
		} else {
			return false;
		}
	}

	/**
	 * Removes all elements from the JSON array.
	 */
	@Override
	public void clear() {
		this.array.clear();
	}

	/**
	 * Check whether the JSON array contains the specified element.
	 * 
	 * @param value - the element to test for
	 * @return true if this JSON array contains the specified element
	 */
	@Override
	public boolean contains(Object value) {
		return this.array.contains(value);
	}

	/**
	 * Check whether the JSON array contains all the specified elements.
	 * 
	 * @param elementsToTest - the collection specifying the elements to test
	 * @return true if the JSON array contains all the elements of the specified collection
	 */
	@Override
	public boolean containsAll(Collection<?> elementsToTest) {
		return this.array.containsAll(elementsToTest);
	}

	/**
	 * Checks whether this JSON array is empty.
	 * 
	 * @return true if this JSON array does not contain any element
	 */
	@Override
	public boolean isEmpty() {
		return this.array.isEmpty();
	}

	@Override
	public Iterator<JsonValue> iterator() {
		return this.array.iterator();
	}

	/**
	 * Remove the first occurrence of the specified object from the JSON array.
	 * 
	 * @param value - the value to remove
	 * @return if the array contained the specified value and it has been removed successfully
	 */
	@Override
	public boolean remove(Object value) {
		return this.array.remove(value);
	}

	@Override
	public boolean removeAll(Collection<?> elementsToRemove) {
		return this.array.removeAll(elementsToRemove);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.array.retainAll(c);
	}

	/**
	 * Get the size of this JSON array.
	 * 
	 * @return the number of elements contained by this array
	 */
	@Override
	public int size() {
		return this.array.size();
	}

	@Override
	public Object[] toArray() {
		return this.array.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.array.toArray(a);
	}

	/**
	 * Get the element at the specified index.
	 * 
	 * @param index - the index of the element to return
	 * @return the element at the specified position
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	@Override
	public JsonValue get(int index) {
		return this.array.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.array.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.array.lastIndexOf(o);
	}

	@Override
	public ListIterator<JsonValue> listIterator() {
		/*
		 * Create a wrapper for the list iterator to prevent
		 * addition of null values.
		 */
		ListIterator<JsonValue> iterator = this.array.listIterator();
		return iterator != null ? new NonNullListIterator(iterator): null;
	}

	@Override
	public ListIterator<JsonValue> listIterator(int index) {
		/*
		 * Create a wrapper for the list iterator to prevent
		 * addition of null values.
		 */
		ListIterator<JsonValue> iterator = this.array.listIterator(index);
		return iterator != null ? new NonNullListIterator(iterator): null;
	}

	/**
	 * Remove the element at the specified position of the JSON array and return it.
	 * 
	 * @param index - the index of the element to remove
	 * @return the removed element
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	@Override
	public JsonValue remove(int index) {
		return this.array.remove(index);
	}

	/**
	 * Set the element at the specified position to the specified value.
	 * 
	 * @param index - the index of the element to set
	 * @param value - the value to set at the specified position
	 * @return the element previously contained at the specified position
	 * @throws NullPointerException if the element to set is null
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public JsonValue set(int index, JsonValue value) {
		Objects.requireNonNull(value, "Null cannot be inserted into a JSON array.");
		return this.array.set(index, value);
	}

	@Override
	public List<JsonValue> subList(int fromIndex, int toIndex) {
		// TODO: finish doc
		return this.array.subList(fromIndex, toIndex);
	}

	@Override
	public int hashCode() {
		return this.array.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof JsonArray) {
			return this.array.equals(((JsonArray) obj).array);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.array.toString();
	}
	
	/**
	 * The {@code NonNullListIterator} class represents wrapper for a 
	 * {@link ListIterator} to prevent addition of null values to the {@link JsonArray}.
	 * 
	 * @author Planters
	 *
	 */
	private static class NonNullListIterator implements ListIterator<JsonValue> {
		
		private final ListIterator<JsonValue> iterator;

		/**
		 * Create a new wrapper for a {@link ListIterator} to prevent addition of null
		 * values to the {@link JsonArray}.
		 * 
		 * @param iterator - the iterator to wrap
		 */
		private NonNullListIterator(ListIterator<JsonValue> iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public void add(JsonValue arg0) {
			if (arg0 != null) {
				this.iterator.add(arg0);
			} else {
				throw new NullPointerException("Null cannot be added to a JSON array.");
			}
		}

		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public boolean hasPrevious() {
			return this.iterator.hasPrevious();
		}

		@Override
		public JsonValue next() {
			return this.iterator.next();
		}

		@Override
		public int nextIndex() {
			return this.iterator.nextIndex();
		}

		@Override
		public JsonValue previous() {
			return this.iterator.previous();
		}

		@Override
		public int previousIndex() {
			return this.iterator.previousIndex();
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

		@Override
		public void set(JsonValue arg0) {
			if (arg0 != null) {
				this.iterator.set(arg0);
			} else {
				throw new NullPointerException("Null cannot be added to a JSON array.");
			}
		}
		
	}
	
}
