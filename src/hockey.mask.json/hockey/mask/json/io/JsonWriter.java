package hockey.mask.json.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import hockey.mask.json.values.JsonValue;

/**
 * The JsonWriter class writes JSON data to an external target.
 * 
 * @author Planters
 *
 */
public class JsonWriter {

	File target = null;
	Charset encoding = Charset.defaultCharset();
	
	/**
	 * Create a new writer for JSON data writing to the specified file with the default encoding.
	 * 
	 * @param file - the file to write to
	 * @throws NullPointerException if null is passed for the target file
	 */
	public JsonWriter(File file) throws NullPointerException {
		if (file != null) {
			this.target = file;
		} else {
			throw new NullPointerException("Cannot write to a null file.");
		}
	}
	
	/**
	 * Create a new writer for JSON data writing to the specified file with the specified encoding.
	 * 
	 * @param file - the file to write to
	 * @param encoding - the encoding to use
	 * @throws NullPointerException if null is passed for the target file
	 */
	public JsonWriter(File file, Charset encoding) throws NullPointerException {
		if (file != null) {
			this.target = file;
			if (encoding != null) {
				this.encoding = encoding;
			}
		} else {
			throw new NullPointerException("Cannot write to a null file.");
		}
	}
	
	/**
	 * Write the specified JSON data to the target file.
	 * 
	 * @param jsonData - the JSON data to write
	 * @throws FileNotFoundException if the target file is a directory or cannot be created of accessed
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException - if null is passed for the JSON data
	 */
	public void write(JsonValue jsonData) throws FileNotFoundException, IOException, NullPointerException {
		if (jsonData != null) {
			try (OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(this.getTarget()), this.getEncoding());) {
				osw.write(jsonData.toJson());
			}
		} else {
			throw new NullPointerException(String.format("Null cannot be written to the %s", this.getTarget()));
		}
	}
	
	/**
	 * Get the charset that will be used for writing JSON data to the target destination.
	 * 
	 * @return the encoding used for writing
	 */
	public Charset getEncoding() {
		return this.encoding;
	}
	
	/**
	 * Get the destination to write to.
	 * 
	 * @return the destination to write to
	 */
	public File getTarget() {
		return this.target;
	}
	
}
