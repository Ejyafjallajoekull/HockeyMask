/**
 * 
 * Defines a testing module for the module hockey.mask.json.
 * 
 * @author Planters
 */
module hockey.mask.test {

	exports hockey.mask.test;
	exports hockey.mask.test.parser;
	exports hockey.mask.test.values;
//	exports hockey.mask.test.io;
	
	requires transitive hockey.mask.json;
	requires java.base;
	requires koro.sensei.tester;
	
}