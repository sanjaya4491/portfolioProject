package com.tbf;

import java.util.List;

/**
 * This class Converts a list of person and a asset into a Json format and it
 * is written to a file.
 */

public class DataConverter {
	
	public static void main(String args[]) {
		
		List<Person> personList = DataParser.parsePersonDataFile();
		List<Asset> assetList = DataParser.parseAssetDataFile();
		JsonUtils.personListToJsonFile(personList);
		JsonUtils.assetListToJsonFile(assetList);
		
	}
	
}
