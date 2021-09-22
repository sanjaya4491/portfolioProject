package com.tbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
* Class that contains methods that read in from flat data files and parse
* the records into objects which are then put into usable lists 
*/

public class DataParser {
	
	public static final String Person_file = "data/Persons.dat";
	public static final String Asset_file = "data/Assets.dat";
	public static final String Portfolio_file = "data/Portfolios.dat";

	/**
	 * Method that reads from a flat data file containing person information and
	 * returns a list of person objects
	 * 
	 * @return
	 */
	public static List<Person> parsePersonDataFile() {
		// create empty list of people
		List<Person> result = new ArrayList<Person>();
		// open file
		File file = new File(Person_file);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// first line in the file is the number of records in the file
		int numlines = Integer.parseInt(sc.nextLine());
		int i = 0;
		while (i < numlines) {
			// tokenize the record
			String line[] = sc.nextLine().split(";");
			String personCode = line[0];
			String brok[] = line[1].split(",");
			String name[] = line[2].split(",");
			String lastName = name[0];
			// remove the space in the first name that is not needed
			String firstName = name[1].replaceAll("\\s+", "");
			String location[] = line[3].split(",");
			Address address = new Address(location[0], location[1].replaceAll("\\s+", ""), location[2], location[3],
					location[4]);
			List<Email> emails = new ArrayList<>();
			List<Email> emptyEmails = new ArrayList<>();
			// initialize broker
			Broker broker = null;
			// if there are 5 tokens then its a complete record with email(s)
			if (line.length == 5) {
				String email[] = line[4].split(",");
				// iterate over how many email's the person has and them to the email array
				for (int j = 0; j < email.length; j++) {
					emails.add(new Email(email[j]));
				}
			}
			// check if the broker token is not empty and if the broker has email's
			if (brok.length == 2 && line.length == 5) {
				// check if the broker is a junior broker
				if (brok[0].equals("J")) {
					broker = new JuniorBroker(personCode, lastName, firstName, address, emails, brok[0], brok[1]);
					result.add(broker);
				} else {
					// else its an expert broker
					broker = new ExpertBroker(personCode, lastName, firstName, address, emails, brok[0], brok[1]);
					result.add(broker);
				}
				// check if the broker token is not empty and if the broker has no email's
			} else if (brok.length == 2 && line.length != 5) {
				// check if the broker is a junior broker
				if (brok[0].equals("J")) {
					broker = new JuniorBroker(personCode, lastName, firstName, address, emptyEmails, brok[0], brok[1]);
					result.add(broker);
				} else {
					// else its an expert broker
					broker = new ExpertBroker(personCode, lastName, firstName, address, emptyEmails, brok[0], brok[1]);
					result.add(broker);
				}
				// check if the person is not a broker and has email's
			} else if (brok.length != 2 && line.length == 5) {
				Person person = new Person(personCode, lastName, firstName, address, emails);
				result.add(person);
				// else the person is not a broker and has no email's
			} else {
				Person person = new Person(personCode, lastName, firstName, address, emptyEmails);
				result.add(person);
			}
			i++;
		}
		sc.close();
		return result;
	}
	
	/**
	 * Method that returns a map containing person codes with their corresponding person objects
	 * @param personList
	 * @return
	 */
	public static Map<String, Person> personMap(List<Person> personList) {
		Map<String, Person> result = new HashMap<>();
		for(Person p : personList) {
			String personCode = p.getPersonCode();
			result.put(personCode, p);
		}
		return result;
	}

	/**
	 * Method that reads from a flat data file containing asset information and
	 * returns a list of asset objects
	 * 
	 * @return
	 */
	public static List<Asset> parseAssetDataFile() {
		// create list of assets
		List<Asset> result = new ArrayList<Asset>();
		// open file
		File file = new File(Asset_file);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// first line is the number of records in the file
		int numlines = Integer.parseInt(sc.nextLine());
		int i = 0;
		while (i < numlines) {
			// generic asset
			Asset a = null;
			// tokenize the record
			String line[] = sc.nextLine().split(";");
			// code, type, label are all common to the 3 types of assets
			String code = line[0];
			String type = line[1];
			String label = line[2];
			// checks if the record is a deposit account
			if (line.length == 4) {
				double apr = Double.parseDouble(line[3]);
				// create deposit account
				a = new DepositAccount(code, type, label, apr);
				// checks if the record is a stock
			} else if (line.length == 8) {
				double quarterlyDividend = Double.parseDouble(line[3]);
				double baseReturn = Double.parseDouble(line[4]);
				double betaMeasure = Double.parseDouble(line[5]);
				String stockSymbol = line[6];
				double sharePrice = Double.parseDouble(line[7]);
				// create stock
				a = new Stock(code, type, label, quarterlyDividend, baseReturn, betaMeasure, stockSymbol, sharePrice);
				// checks if the record is a private investment
			} else if (line.length == 7) {
				double quarterlyDividend = Double.parseDouble(line[3]);
				double baseReturn = Double.parseDouble(line[4]);
				double baseOmegaMeasure = Double.parseDouble(line[5]);
				double totalValue = Double.parseDouble(line[6]);
				// create private investment
				a = new PrivateInvestment(code, type, label, quarterlyDividend, baseReturn, baseOmegaMeasure,
						totalValue);
			}
			// add the asset to the asset list
			result.add(a);
			i++;
		}
		// close the file
		sc.close();
		return result;
	}
	
	/**
	 * Method that returns a map containing asset codes corresponding to their assets
	 * @param assetList
	 * @return
	 */
	public static Map<String, Asset> assetMap(List<Asset> assetList) {
		Map<String, Asset> result = new HashMap<>();
		for(Asset a : assetList) {
			String assetCode = a.getCode();
			result.put(assetCode, a);
		}
		return result;
	}
	
	/**
	 * Method that reads from a flat data file containing portfolio information and returns a list of portfolios
	 * @return
	 */
	public static List<Portfolio> parsePortfolioDataFile() {
		// create list of empty porfolio's
		List<Portfolio> result = new ArrayList<Portfolio>();
		// create list of people calling method
		List<Person> people = parsePersonDataFile();
		// create list of assets calling method
		List<Asset> allAssets = parseAssetDataFile();
		// open file
		File file = new File(Portfolio_file);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// create map to hold the codes and person objects
		Map<String, Person> personMap = personMap(people);
		// create map to hold the codes and asset objects 
		Map<String, Asset> assetMap = assetMap(allAssets);
		// first line is the number of records
		int numlines = Integer.parseInt(sc.nextLine());
		int i = 0;
		while (i < numlines) {
			// tokenize the record
			String line[] = sc.nextLine().split(";");
			// get codes from portfolio data file
			String portfolioCode = line[0];
			// only codes given to the corresponding people are given
			String ownerCode = line[1];
			String managerCode = line[2];
			// use the codes to find the person in the map
			Person owner = personMap.get(ownerCode);
			Broker manager = (Broker) personMap.get(managerCode);
			// the record may or may not have a beneficiary
			// assume there is not a beneficiary
			String beneficiaryCode = null;
			// a complete record has 5 tokens and also checks if there are 4
			// tokens and no assets
			if (line.length == 4 || line.length == 5) {
				beneficiaryCode = line[3];
				// if the record has 4 tokens but no beneficiary
				if (line[3].isEmpty() == true) {
					beneficiaryCode = null;
				}
			}
			// initialize to null
			Person beneficiary = null;
			// if the code is not null then there is a beneficiary for the portfolio
			if (beneficiaryCode != null) {
				// get the person from the map
				beneficiary = personMap.get(beneficiaryCode);
			}
			// may or may not have assets
			// assume there are no assets so set to null
			String assetsList[] = null;
			// initialize empty asset list
			List<Asset> assetList = new ArrayList<>();
			// initialize asset object to null
			Asset asset = null;
			// initialize asset copy to null
			Asset copyAsset = null;
			// checks if it is a complete record
			if (line.length == 5) {
				// tokenize the assets if there are multiple assets
				assetsList = line[4].split(",");
				for (int j = 0; j < assetsList.length; j++) {
					// tokenize the individual assets to get the asset code
					String assets[] = assetsList[j].split(":");
					// find asset in the map
					asset = assetMap.get(assets[0]);
					// get the asset value
					double assetValue = Double.parseDouble(assets[1]);
					// check if the asset is a specific type and copy it
					if(asset instanceof DepositAccount) {
						copyAsset = new DepositAccount((DepositAccount) asset);
					} else if(asset instanceof PrivateInvestment) {
						copyAsset = new PrivateInvestment((PrivateInvestment) asset);
					} else if(asset instanceof Stock) {
						copyAsset = new Stock((Stock) asset);
					}
					// put it into the list
					assetList.add(copyAsset);
					// set asset value to the asset
					copyAsset.setValue(assetValue);
				}
				// create the portfolio
				Portfolio portfolio = new Portfolio(portfolioCode, owner, manager, beneficiary, assetList);
				// add it to the portfolio list
				result.add(portfolio);
				// if the record does not have any assets
			} else {
				Portfolio portfolio = new Portfolio(portfolioCode, owner, manager, beneficiary, assetList);
				result.add(portfolio);
			}
			i++;
		}
		// close the file
		sc.close();
		// sort the portfolio list by a custom comparator where it sorts by the owner's
		// last name and then by their first name
		Collections.sort(result, new Comparator<Portfolio>() {

			public int compare(Portfolio o1, Portfolio o2) {
				String lastName1 = o1.getOwner().getLastName();
				String lastName2 = o2.getOwner().getLastName();

				int result = lastName1.compareTo(lastName2);
				if (result != 0) {
					return result;
				} else {
					String firstName1 = o1.getOwner().getFirstName();
					String firstName2 = o2.getOwner().getFirstName();
					return firstName1.compareTo(firstName2);
				}
			}
		});
		
		return result;
	}

}
