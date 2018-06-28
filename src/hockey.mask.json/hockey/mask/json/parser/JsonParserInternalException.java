package hockey.mask.json.parser;

/**
 * The JsonParserInternalException class will be thrown if a internal failure in a JSON 
 * parser occurs.
 * 
 * @author Planters
 *
 */
public class JsonParserInternalException extends RuntimeException {
	
	/**
	 * Default serialisation.
	 */
	private static final long serialVersionUID = 1L;

	public JsonParserInternalException() {
		super();
	}

	public JsonParserInternalException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public JsonParserInternalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JsonParserInternalException(String arg0) {
		super(arg0);
	}

	public JsonParserInternalException(Throwable arg0) {
		super(arg0);
	}

}
