package hockey.mask.json.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;

import hockey.mask.json.JsonStandardException;
import hockey.mask.json.parser.JsonParserInternalException;
import hockey.mask.json.parser.JsonStaticStreamParser;
import hockey.mask.json.parser.JsonStringParser;
import hockey.mask.json.values.JsonArray;
import hockey.mask.json.values.JsonBoolean;
import hockey.mask.json.values.JsonNull;
import hockey.mask.json.values.JsonNumber;
import hockey.mask.json.values.JsonObject;
import hockey.mask.json.values.JsonString;
import hockey.mask.json.values.JsonValue;

/**
 * The JSON reader class facilitates reading JSON data from different sources.
 * 
 * @author Planters
 *
 */
public class JsonReader {

	JsonStringParser jsonParser = null;
	
	/**
	 * Create a reader to read JSON data from the specified string.
	 * 
	 * @param data - the string to acquire the JSON data from
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws NullPointerException if the specified string is null
	 */
	public JsonReader(String data) throws JsonStandardException, NullPointerException {
		if (data != null) {
				this.jsonParser = new JsonStringParser(data);
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Create a reader to read JSON data from the specified URL.
	 * The default charset will be used for decoding the data.
	 * 
	 * @param url - the URL to acquire the JSON data from
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws IOException if an I/O error occurs
	 * @throws JsonParserInternalException if creation of the underlying JSON parser failed
	 * @throws NullPointerException if the specified URL is null
	 */
	public JsonReader(URL url) throws JsonStandardException, IOException, JsonParserInternalException, NullPointerException {
		if (url != null) {
			try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(url.openStream());) {
				this.jsonParser = jsonStreamParser.toStringParser();
			}
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Create a reader to read JSON data from the specified URL using the specified 
	 * charset for decoding the data. <br>
	 * If the specified encoding is null, the system default will be used.
	 * 
	 * @param url - the URL to acquire the JSON data from
	 * @param encoding - the charset used to decode the JSON data
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws IOException if an I/O error occurs
	 * @throws JsonParserInternalException if creation of the underlying JSON parser failed
	 * @throws NullPointerException if the specified URL is null
	 */
	public JsonReader(URL url, Charset encoding) throws JsonStandardException, IOException, JsonParserInternalException, NullPointerException {
		if (url != null) {
			if (encoding != null) {
				try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(url.openStream(), encoding);) {
					this.jsonParser = jsonStreamParser.toStringParser();
				}
			} else {
				try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(url.openStream());) {
					this.jsonParser = jsonStreamParser.toStringParser();
				}
			}
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Create a reader to read JSON data from the specified file.
	 * The default charset will be used for decoding the data.
	 * 
	 * @param file - the file to acquire the JSON data from
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws IOException if an I/O error occurs
	 * @throws JsonParserInternalException if creation of the underlying JSON parser failed
	 * @throws FileNotFoundException if the specified file could not be found
	 * @throws NullPointerException if the specified file is null
	 */
	public JsonReader(File file) throws JsonStandardException, IOException, JsonParserInternalException, NullPointerException {
		if (file != null) {
			try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(
					new FileInputStream(file));) {
				this.jsonParser = jsonStreamParser.toStringParser();
			}
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Create a reader to read JSON data from the specified file using the specified 
	 * charset for decoding the data. <br>
	 * If the specified encoding is null, the system default will be used.
	 * 
	 * @param file - the file to acquire the JSON data from
	 * @param encoding - the charset used to decode the JSON data
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws IOException if an I/O error occurs
	 * @throws JsonParserInternalException if creation of the underlying JSON parser failed
	 * @throws FileNotFoundException if the specified file could not be found
	 * @throws NullPointerException if the specified file is null
	 */
	public JsonReader(File file, Charset encoding) throws JsonStandardException, IOException, JsonParserInternalException, NullPointerException {
		if (file != null) {
			if (encoding != null) {
				try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(
						new FileInputStream(file), encoding);) {
					this.jsonParser = jsonStreamParser.toStringParser();
				}
			} else {
				try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(
						new FileInputStream(file));) {
					this.jsonParser = jsonStreamParser.toStringParser();
				}
			}
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Create a reader to read JSON data from the specified file at the specified path.
	 * The default charset will be used for decoding the data.
	 * 
	 * @param path - the path to acquire the JSON data from
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws IOException if an I/O error occurs
	 * @throws JsonParserInternalException if creation of the underlying JSON parser failed
	 * @throws FileNotFoundException if the specified file could not be found
	 * @throws NullPointerException if the specified path is null
	 */
	public JsonReader(Path path) throws JsonStandardException, IOException, JsonParserInternalException, NullPointerException {
		if (path != null) {
			try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(
					new FileInputStream(path.toFile()));) {
				this.jsonParser = jsonStreamParser.toStringParser();
			}
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Create a reader to read JSON data from the file at the specified path using the specified 
	 * charset for decoding the data. <br>
	 * If the specified encoding is null, the system default will be used.
	 * 
	 * @param path - the path to acquire the JSON data from
	 * @param encoding - the charset used to decode the JSON data
	 * @throws JsonStandardException if the supplied data is empty
	 * @throws IOException if an I/O error occurs
	 * @throws JsonParserInternalException if creation of the underlying JSON parser failed
	 * @throws FileNotFoundException if the specified file could not be found
	 * @throws NullPointerException if the specified path is null
	 */
	public JsonReader(Path path, Charset encoding) throws JsonStandardException, IOException, JsonParserInternalException, NullPointerException {
		if (path != null) {
			if (encoding != null) {
				try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(
						new FileInputStream(path.toFile()), encoding);) {
					this.jsonParser = jsonStreamParser.toStringParser();
				}
			} else {
				try (JsonStaticStreamParser jsonStreamParser = new JsonStaticStreamParser(
						new FileInputStream(path.toFile()));) {
					this.jsonParser = jsonStreamParser.toStringParser();
				}
			}
		} else {
			throw new NullPointerException("The data of a JSON reader may not be null.");
		}
	}
	
	/**
	 * Read the next JSON value from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON value or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not JSON formatted
	 */
	public JsonValue read() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonValue.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
	/**
	 * Read the next JSON object from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON object or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not a JSON formatted object
	 */
	public JsonObject readObject() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonObject.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
	/**
	 * Read the next JSON array from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON array or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not a JSON formatted array
	 */
	public JsonArray readArray() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonArray.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
	/**
	 * Read the next JSON string from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON string or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not a JSON formatted string
	 */
	public JsonString readString() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonString.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
	/**
	 * Read the next JSON number from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON number or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not a JSON formatted number
	 */
	public JsonNumber readNumber() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonNumber.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
	/**
	 * Read the next JSON boolean from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON boolean or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not a JSON formatted boolean
	 */
	public JsonBoolean readBoolean() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonBoolean.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
	/**
	 * Read the next JSON null from the specified data source.
	 * If there is no more data to read, null is returned.
	 * 
	 * @return the next JSON null or null if the end of the data has been reached
	 * @throws JsonStandardException - if the data is not a JSON formatted null
	 */
	public JsonNull readNull() throws JsonStandardException {
		if (this.jsonParser.hasNext()) {
			return JsonNull.parseNext(this.jsonParser);
		} else {
			return null;
		}
	}
	
}
