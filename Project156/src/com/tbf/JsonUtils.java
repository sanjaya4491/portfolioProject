package com.tbf;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class that contains methods for serializing lists of objects into Json files
 */

public class JsonUtils {
	
	public static final String Person_file = "data/Persons.dat";
	public static final String Asset_file = "data/Assets.dat";
	
	/**
	 *	Method that converts a list of person objects into Json strings and prints them to a file
	 */
	public static void personListToJsonFile(List<Person> personList) {
		personList = DataParser.parsePersonDataFile();
		PrintWriter out = null;
		try {
			out = new PrintWriter(Person_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = "{" + "\n" + "\"persons\": " + gson.toJson(personList)  + "}";
		out.write(json);
		out.close();
	}
	
	/**
	 * Method that converts a list of asset objects into Json strings and prints them to a file
	 */
	public static void assetListToJsonFile(List<Asset> assetList) {
		assetList = DataParser.parseAssetDataFile();
		PrintWriter out = null;
		try {
			out = new PrintWriter(Asset_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = "{" + "\n" + "\"assets\": " + gson.toJson(assetList) + "}";
		out.write(json);
		out.close();
	}
	

}
