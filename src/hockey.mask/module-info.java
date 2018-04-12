/**
 * 
 * Defines a JSON parsing API.
 * 
 * @author Planters
 */
module hockey.mask {
	
	/*
	 *  TODO: split the testing up to another module as soon as eclipse allows it
	 *  to remove the necessity for the testing framework
	 */
	
	exports hockey.mask.json;
	exports hockey.mask.json.io;
	exports hockey.mask.test;
	
	requires transitive java.base;
	requires koro.sensei.tester;
	
}