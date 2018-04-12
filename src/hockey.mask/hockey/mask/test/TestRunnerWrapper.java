package hockey.mask.test;

import koro.sensei.tester.TestRunner;

/**
 * The TestRunnerWrapper class wraps the TestRunner classes main method from the Korosensei 
 * testing framework for easier execution. 
 * 
 * @author Planters
 *
 */
public class TestRunnerWrapper {

	/**
	 * Passes the command line arguments to the TestRunners main function.
	 * 
	 * @param args - the command line arguments for the test runner
	 */
	public static void main(String[] args) {
		TestRunner.main(args);
	}

}
