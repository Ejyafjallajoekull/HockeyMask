package hockey.mask.json;

/**
 * The JsonStandardException class will be thrown if the JSON standard is violated.
 * 
 * @author Planters
 *
 */
public class JsonStandardException extends Exception {

	/**
	 * Default serialisation.
	 */
	private static final long serialVersionUID = 1L;

	public JsonStandardException() {
		
	}

	public JsonStandardException(String arg0) {
		super(arg0);
	}

	public JsonStandardException(Throwable arg0) {
		super(arg0);
	}

	public JsonStandardException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JsonStandardException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
