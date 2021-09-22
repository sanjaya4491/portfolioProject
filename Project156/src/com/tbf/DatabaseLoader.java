package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that holds methods that load data from a MySQL database and creates
 * java objects using the data
 */

public class DatabaseLoader {

	/**
	 * Method that returns a person given the corresponding id and all major
	 * information the person has
	 * 
	 * @param personId
	 * @return
	 */
	public static Person getPerson(int personId) {
		// instantiate a person and email list
		Person p = null;
		List<Email> emails = new ArrayList<Email>();

		Connection conn = DatabaseInfo.databaseConnector();

		// get the email(s) of the person
		String query = "select e.emailid, e.email from Email e " + "join Person p on p.personId = e.personId "
				+ "where p.personId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while (rs.next()) {
				int emailId = rs.getInt("emailId");
				String email = rs.getString("email");
				// add email to list
				emails.add(new Email(emailId, email));
			}
			// close the result set
			rs.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		// gets all major fields of a person
		query = "select p.personCode, p.firstName, p.lastName, p.brokerType, p.brokerSection, "
				+ "a.addressId, a.street, a.city, s.state, a.zipcode, c.country from Person p "
				+ "join Address a on p.addressId = a.addressId " + "join State s on s.stateId = a.stateId "
				+ "join Country c on c.countryId = s.countryId " + "where p.personId = ?;";

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String personCode = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String brokerType = rs.getString("brokerType");
				String brokerSection = rs.getString("brokerSection");
				int addressId = rs.getInt("addressId");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zipcode = rs.getString("zipcode");
				String country = rs.getString("country");
				Address a = new Address(addressId, street, city, state, zipcode, country);
				// checks if the person is not a broker
				if (brokerType == null || brokerSection == null) {
					p = new Person(personId, personCode, lastName, firstName, a, emails);
					// checks if the person is a junior broker
				} else if (brokerType.equals("J")) {
					p = new JuniorBroker(personId, personCode, lastName, firstName, a, emails, brokerType,
							brokerSection);
					// checks if the person is an expert broker
				} else if (brokerType.equals("E")) {
					p = new ExpertBroker(personId, personCode, lastName, firstName, a, emails, brokerType,
							brokerSection);
				}
			} else {
				// no person exists with the given id
				throw new IllegalStateException("no such person with personId = " + personId);
			}
			rs.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return p;
	}

	/**
	 * Method returns all people from a database with all major information fields
	 * 
	 * @return
	 */
	public static List<Person> getAllPerson() {
		List<Person> people = new ArrayList<>();

		Connection conn = DatabaseInfo.databaseConnector();

		// get id's of all people
		String query = "select p.personId from Person p;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int personId = rs.getInt("personId");
				// give the id to the method that returns all information on one person
				Person p = getPerson(personId);
				// add the person to the list
				people.add(p);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return people;
	}

	/**
	 * Method that returns an asset given the corresponding id and all major
	 * information the asset has
	 * 
	 * @param assetId
	 * @return
	 */
	public static Asset getAsset(int assetId) {
		Asset a = null;

		Connection conn = DatabaseInfo.databaseConnector();

		// get all major fields of all the asset types
		String query = "select a.assetCode, a.assetType, a.assetName, a.apr, a.quarterlyDividend, "
				+ "a.baseReturn, a.omegaMeasure, a.totalValue, a.stockSymbol, "
				+ "a.sharePrice from Asset a where a.assetId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, assetId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String assetCode = rs.getString("assetCode");
				String assetType = rs.getString("assetType");
				String assetName = rs.getString("assetName");
				// checks if the asset is a deposit account
				if (assetType.equals("D")) {
					double apr = rs.getDouble("apr");
					a = new DepositAccount(assetId, assetCode, assetType, assetName, apr);
					// checks if the asset is a private investment
				} else if (assetType.equals("P")) {
					double quarterlyDividend = rs.getDouble("quarterlyDividend");
					double baseReturn = rs.getDouble("baseReturn");
					double omegaMeasure = rs.getDouble("omegaMeasure");
					double totalValue = rs.getDouble("totalValue");
					a = new PrivateInvestment(assetId, assetCode, assetType, assetName, quarterlyDividend, baseReturn,
							omegaMeasure, totalValue);
					// checks if the asset is a stock
				} else if (assetType.equals("S")) {
					double quarterlyDividend = rs.getDouble("quarterlyDividend");
					double baseReturn = rs.getDouble("baseReturn");
					double omegaMeasure = rs.getDouble("omegaMeasure");
					String stockSymbol = rs.getString("stockSymbol");
					double sharePrice = rs.getDouble("sharePrice");
					a = new Stock(assetId, assetCode, assetType, assetName, quarterlyDividend, baseReturn, omegaMeasure,
							stockSymbol, sharePrice);
				}
			} else {
				// no asset with the given id exists
				throw new IllegalStateException("no such asset with assetId = " + assetId);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return a;
	}

	/**
	 * Method that returns a list of all assets in the database with all major
	 * information
	 * 
	 * @return
	 */
	public static List<Asset> getAllAsset() {
		List<Asset> assets = new ArrayList<>();

		Connection conn = DatabaseInfo.databaseConnector();

		// get id's of all assets
		String query = "select a.assetId from Asset a;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				// get id
				int assetId = rs.getInt("assetId");
				// give id to method that returns an asset with all corresponding information
				Asset a = getAsset(assetId);
				// add the asset to the list
				assets.add(a);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return assets;
	}

	/**
	 * Method that returns a portfolio given the corresponding id and all major
	 * information associated with the portfolio
	 * 
	 * @param portfolioId
	 * @return
	 */
	public static Portfolio getPortfolio(int portfolioId) {
		// instantiate portfolio and its assets
		Portfolio p = null;
		List<Asset> assets = new ArrayList<>();

		Connection conn = DatabaseInfo.databaseConnector();

		// get the assets associated with the portfolio
		String query = "select a.assetId, pa.value from Portfolio p "
				+ "join PortfolioAsset pa on pa.portfolioId = p.portfolioId "
				+ "join Asset a on a.assetId = pa.assetId " + "where p.portfolioId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			rs = ps.executeQuery();
			while (rs.next()) {
				// get id
				int assetId = rs.getInt("assetId");
				// get value associated with the asset
				double value = rs.getDouble("value");
				// use id to get asset
				Asset a = getAsset(assetId);
				// set the value of the asset
				a.setValue(value);
				// add asset to the list
				assets.add(a);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// get all major information on a particular portfolio
		query = "select p.portfolioCode, p.personId, p.brokerId, " + "p.beneficiaryId from Portfolio p "
				+ "where p.portfolioId = ?;";

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String portfolioCode = rs.getString("portfolioCode");
				// get id's
				int personId = rs.getInt("personId");
				int brokerId = rs.getInt("brokerId");
				int beneficiaryId = rs.getInt("beneficiaryId");
				// use id's to find the people associated with the portfolio
				Person owner = getPerson(personId);
				Broker manager = (Broker) getPerson(brokerId);
				Person beneficiary = null;
				// checks if the portfolio has a beneficiary
				// if not then it remains null
				if (beneficiaryId != 0) {
					beneficiary = getPerson(beneficiaryId);
				}
				p = new Portfolio(portfolioId, portfolioCode, owner, manager, beneficiary, assets);
			} else {
				// no portfolio with the id exists
				throw new IllegalStateException("no such portfolio with portfolioId = " + portfolioId);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return p;
	}

	/**
	 * Method that returns a list of portfolios in the database with all major
	 * information
	 * 
	 * @return
	 */
	public static List<Portfolio> getAllPortfolio() {
		List<Portfolio> portfolios = new ArrayList<>();

		Connection conn = DatabaseInfo.databaseConnector();

		// get all id's of porfolios
		String query = "select p.portfolioId from Portfolio p;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				// get id
				int portfolioId = rs.getInt("portfolioId");
				// use id to get the portfolio with all major information
				Portfolio p = getPortfolio(portfolioId);
				// add portfolio to the list
				portfolios.add(p);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		// sort the portfolio list by a custom comparator where it sorts by the owner's
		// last name and then by their first name
		Collections.sort(portfolios, new Comparator<Portfolio>() {

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

		return portfolios;
	}

}
