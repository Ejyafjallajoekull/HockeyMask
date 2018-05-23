/**
 * 
 * Defines a JSON parsing API.
 * 
 * @author Planters
 */
module hockey.mask.json {
	
	exports hockey.mask.json;
	exports hockey.mask.json.io;
	
	exports hockey.mask.json.parser to hockey.mask.test;
	
	requires java.base;
	
}