package hockey.mask.test;

import hockey.mask.json.JsonString;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The JsonStringTesting class test the JsonString class for correct functionality.
 * 
 * @author Planters
 *
 */
public class JsonStringTesting implements TestSubject {

	@Override
	public void runAllTests() throws TestFailureException {
		String s = "So\\\nme\non\te";
		JsonString ss = new JsonString(s);
		System.out.println(s);
		System.out.println(ss);
		System.out.println(ss.toJson());

	}

}
