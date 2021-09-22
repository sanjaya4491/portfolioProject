package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class PortfolioData {

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to remove all people
		String removeAllPersonsQuery = "delete from Person;";

		PreparedStatement ps = null;

		try {
			// prepare and execute remove all persons query
			ps = conn.prepareStatement(removeAllPersonsQuery);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to remove all addresses
		String removeAllAddressesQuery = "delete from Address;";

		try {
			// prepare and excute remove all addresses query
			ps = conn.prepareStatement(removeAllAddressesQuery);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to remove all states
		String removeAllStatesQuery = "delete from State;";

		try {
			// prepare and execute remove all states query
			ps = conn.prepareStatement(removeAllStatesQuery);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to remove all countries
		String removeAllCountriesQuery = "delete from Country;";

		try {
			// prepare and execute remove all countries query
			ps = conn.prepareStatement(removeAllCountriesQuery);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the person record from the database corresponding to the provided
	 * <code>personCode</code>
	 * 
	 * @param personCode
	 */
	public static void removePerson(String personCode) {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select a person
		String selectPersonQuery = "select personId from Person where personCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute select person query
			ps = conn.prepareStatement(selectPersonQuery);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			// if the person does not exist in the database, throw an exception
			if (!rs.next()) {
				throw new IllegalStateException("person does not exist with personCode = " + personCode);
				// else get the id from the person and delete the person
			} else {
				int personId = rs.getInt("personId");

				String removePersonQuery = "delete from Person where personId = ?;";

				ps = conn.prepareStatement(removePersonQuery);
				ps.setInt(1, personId);
				ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method to add a person record to the database with the provided data. The
	 * <code>brokerType</code> will either be "E" or "J" (Expert or Junior) or
	 * <code>null</code> if the person is not a broker.
	 * 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @param brokerType
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country, String brokerType, String secBrokerId) {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select a country
		String selectCountryQuery = "select countryId from Country where country = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int countryId = 0;
		try {
			// prepare and execute select country query
			ps = conn.prepareStatement(selectCountryQuery);
			ps.setString(1, country);
			rs = ps.executeQuery();
			// if the country already exists, get the id
			if (rs.next()) {
				countryId = rs.getInt("countryId");
				// else the country does not exist and insert it into the database
			} else {
				String insertCountryQuery = "insert into Country(country) values (?);";

				ps = conn.prepareStatement(insertCountryQuery);
				ps.setString(1, country);
				ps.executeUpdate();

				// retrieve the countryId
				ps = conn.prepareStatement("select last_insert_id()");
				rs = ps.executeQuery();
				rs.next();
				countryId = rs.getInt("last_insert_id()");
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to select a state
		String selectStateQuery = "select stateId from State where state = ?;";

		int stateId = 0;
		try {
			// prepare and execute select state query
			ps = conn.prepareStatement(selectStateQuery);
			ps.setString(1, state);
			rs = ps.executeQuery();
			// if the state already exists, retrieve the id
			if (rs.next()) {
				stateId = rs.getInt("stateId");
				// else insert the state
			} else {
				String insertStateQuery = "insert into State(state, countryId) values (?, ?);";

				ps = conn.prepareStatement(insertStateQuery);
				ps.setString(1, state);
				ps.setInt(2, countryId);
				ps.executeUpdate();

				// retrieve the state id
				ps = conn.prepareStatement("select last_insert_id()");
				rs = ps.executeQuery();
				rs.next();
				stateId = rs.getInt("last_insert_id()");
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to insert an address
		String insertAddressQuery = "insert into Address(street, city, zipCode, stateId) values (?, ?, ?, ?);";

		int addressId = 0;
		try {
			// prepare and execute the insert address query
			ps = conn.prepareStatement(insertAddressQuery);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateId);
			ps.executeUpdate();

			// retrieve the address id
			ps = conn.prepareStatement("select last_insert_id()");
			rs = ps.executeQuery();
			rs.next();
			addressId = rs.getInt("last_insert_id()");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to select a person
		String selectPersonQuery = "select personCode from Person where personCode = ?";

		try {
			// prepare and execute the select a person query;
			ps = conn.prepareStatement(selectPersonQuery);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			// if a person already exists with the given personCode throw an exception
			if (rs.next()) {
				throw new IllegalStateException("a person already exists with personCode = " + personCode);
				// else insert the person record
			} else {
				String insertPersonQuery = "insert into Person(personCode, firstName, lastName, brokerType, "
						+ "brokerSection, addressId) values (?, ?, ?, ?, ?, ?);";

				ps = conn.prepareStatement(insertPersonQuery);
				ps.setString(1, personCode);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, brokerType);
				ps.setString(5, secBrokerId);
				ps.setInt(6, addressId);
				ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * 
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		PreparedStatement ps = null;

		try {
			String insertEmailQuery = "insert into Email(email, personId) values "
					+ "(?, (select personId from Person where personCode = ?));";

			ps = conn.prepareStatement(insertEmailQuery);
			ps.setString(1, email);
			ps.setString(2, personCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			// no person exists with the given personCode
			throw new RuntimeException("no such person with personCode = " + personCode, e);
		}

		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes all asset records from the database
	 */
	public static void removeAllAssets() {
		// establish connection database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to remove all assets
		String removeAllAssetsQuery = "delete from Asset;";

		PreparedStatement ps = null;

		try {
			// prepare and execute remove all assets query
			ps = conn.prepareStatement(removeAllAssetsQuery);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Removes the asset record from the database corresponding to the provided
	 * <code>assetCode</code>
	 * 
	 * @param assetCode
	 */
	public static void removeAsset(String assetCode) {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select an asset
		String selectAssetQuery = "select assetId from Asset where assetCode = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute select asset query
			ps = conn.prepareStatement(selectAssetQuery);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			// if asset does not exist, throw an exception
			if (!rs.next()) {
				throw new IllegalStateException("asset does not exist with assetCode = " + assetCode);
				// else get the asset id and delete the asset
			} else {
				int assetId = rs.getInt("assetId");

				String removeAssetQuery = "delete from Asset where assetId = ?;";

				ps = conn.prepareStatement(removeAssetQuery);
				ps.setInt(1, assetId);
				ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a deposit account asset record to the database with the provided data.
	 * 
	 * @param assetCode
	 * @param label
	 * @param apr
	 */
	public static void addDepositAccount(String assetCode, String label, double apr) {

		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select an asset
		String selectAssetQuery = "select assetCode from Asset where assetCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute select an asset query
			ps = conn.prepareStatement(selectAssetQuery);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			// if an asset exists with the given asset code, throw an exception
			if (rs.next()) {
				throw new IllegalStateException("an asset already exists with assetCode = " + assetCode);
				// else insert the deposit account
			} else {
				String insertDepositAccountQuery = "insert into Asset(assetName, assetType, assetCode, apr) "
						+ "values (?, ?, ?, ?);";

				ps = conn.prepareStatement(insertDepositAccountQuery);
				ps.setString(1, label);
				ps.setString(2, "D");
				ps.setString(3, assetCode);
				ps.setDouble(4, apr);
				ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a private investment asset record to the database with the provided
	 * data.
	 * 
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param baseOmega
	 * @param totalValue
	 */
	public static void addPrivateInvestment(String assetCode, String label, Double quarterlyDividend,
			Double baseRateOfReturn, Double baseOmega, Double totalValue) {

		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select an asset
		String selectAssetQuery = "select assetCode from Asset where assetCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute the select an asset query
			ps = conn.prepareStatement(selectAssetQuery);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			// if the asset exists with the given assetCode, throw an exception
			if (rs.next()) {
				throw new IllegalStateException("an asset already exists with assetCode = " + assetCode);
				// else insert a private investment
			} else {
				String insertPrivateInvestmentQuery = "insert into Asset(assetName, assetType, assetCode, "
						+ "quarterlyDividend, baseReturn, omegaMeasure, totalValue) values (?, ?, ?, ?, ?, ?, ?);";

				ps = conn.prepareStatement(insertPrivateInvestmentQuery);
				ps.setString(1, label);
				ps.setString(2, "P");
				ps.setString(3, assetCode);
				ps.setDouble(4, quarterlyDividend);
				ps.setDouble(5, baseRateOfReturn);
				ps.setDouble(6, baseOmega);
				ps.setDouble(7, totalValue);
				ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a stock asset record to the database with the provided data.
	 * 
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param beta
	 * @param stockSymbol
	 * @param sharePrice
	 */
	public static void addStock(String assetCode, String label, Double quarterlyDividend, Double baseRateOfReturn,
			Double beta, String stockSymbol, Double sharePrice) {

		// establish a connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select an asset
		String selectAssetQuery = "select assetCode from Asset where assetCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute the select asset query
			ps = conn.prepareStatement(selectAssetQuery);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			// if an asset exists with the given assetCode, throw an exception
			if (rs.next()) {
				throw new IllegalStateException("an asset already exists with assetCode = " + assetCode);
				// else insert a stock
			} else {
				String insertStockQuery = "insert into Asset(assetName, assetType, assetCode, "
						+ "quarterlyDividend, baseReturn, omegaMeasure, stockSymbol, sharePrice) values "
						+ "(?, ?, ?, ?, ?, ?, ?, ?);";

				ps = conn.prepareStatement(insertStockQuery);
				ps.setString(1, label);
				ps.setString(2, "S");
				ps.setString(3, assetCode);
				ps.setDouble(4, quarterlyDividend);
				ps.setDouble(5, baseRateOfReturn);
				ps.setDouble(6, beta);
				ps.setString(7, stockSymbol);
				ps.setDouble(8, sharePrice);
				ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Removes all portfolio records from the database
	 */
	public static void removeAllPortfolios() {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to remove all portfolios
		String removeAllPortfoliosQuery = "delete from Portfolio;";

		PreparedStatement ps = null;

		try {
			// prepare and execute remove all portfolios query
			ps = conn.prepareStatement(removeAllPortfoliosQuery);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Removes the portfolio record from the database corresponding to the provided
	 * <code>portfolioCode</code>
	 * 
	 * @param portfolioCode
	 */
	public static void removePortfolio(String portfolioCode) {
		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select a portfolio
		String selectPortfolioQuery = "select portfolioId from Portfolio where portfolioCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute select portfolio query
			ps = conn.prepareStatement(selectPortfolioQuery);
			ps.setString(1, portfolioCode);
			rs = ps.executeQuery();
			// if the portfolio does not exist with the given code, throw an exception
			if (!rs.next()) {
				throw new IllegalStateException("portfolio does not exist with portfolioCode = " + portfolioCode);
				// else get the portfolio id and delete the portfolio
			} else {
				int portfolioId = rs.getInt("portfolioId");

				String removePortfolioQuery = "delete from Portfolio where portfolioId = ?;";

				ps = conn.prepareStatement(removePortfolioQuery);
				ps.setInt(1, portfolioId);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a portfolio records to the database with the given data. If the
	 * portfolio has no beneficiary, the <code>beneficiaryCode</code> will be
	 * <code>null</code>
	 * 
	 * @param portfolioCode
	 * @param ownerCode
	 * @param managerCode
	 * @param beneficiaryCode
	 */
	public static void addPortfolio(String portfolioCode, String ownerCode, String managerCode,
			String beneficiaryCode) {

		// establish connection to database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select a portfolio
		String selectPortfolioQuery = "select portfolioCode from Portfolio where portfolioCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// prepare and execute select portfolio query
			ps = conn.prepareStatement(selectPortfolioQuery);
			ps.setString(1, portfolioCode);
			rs = ps.executeQuery();
			// if a portfolio already exists with the given portfolioCode, throw an
			// exception
			if (rs.next()) {
				throw new IllegalStateException("a portfolio already exists with portfolioCode = " + portfolioCode);
				// else insert a portfolio
			} else {
				String insertPortfolioQuery = "insert into Portfolio(portfolioCode, personId, brokerId, beneficiaryId) values "
						+ "(?, (select personId from Person where personCode = ?), "
						+ "(select personId from Person where personCode = ?), "
						+ "(select personId from Person where personCode = ?));";

				ps = conn.prepareStatement(insertPortfolioQuery);
				ps.setString(1, portfolioCode);
				ps.setString(2, ownerCode);
				ps.setString(3, managerCode);
				ps.setString(4, beneficiaryCode);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// if one or more codes given do not match to any people in the database
			throw new RuntimeException("one or more codes do not associate with any people", e);
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Associates the asset record corresponding to <code>assetCode</code> with the
	 * portfolio corresponding to the provided <code>portfolioCode</code>. The third
	 * parameter, <code>value</code> is interpreted as a <i>balance</i>, <i>number
	 * of shares</i> or <i>stake percentage</i> depending on the type of asset the
	 * <code>assetCode</code> is associated with.
	 * 
	 * @param portfolioCode
	 * @param assetCode
	 * @param value
	 */
	public static void addAsset(String portfolioCode, String assetCode, double value) {

		// establish connection database
		Connection conn = DatabaseInfo.databaseConnector();

		// query to select an asset
		String selectAssetQuery = "select assetId from Asset where assetCode = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int assetId = 0;
		try {
			// prepare and execute the select asset query
			ps = conn.prepareStatement(selectAssetQuery);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			// if an asset does not exist with the given assetCode, throw an exception
			if (!rs.next()) {
				throw new IllegalStateException("an asset does not exist with assetCode = " + assetCode);
				// else get the assetId
			} else {
				assetId = rs.getInt("assetId");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to select a portfolio
		String selectPortfolioQuery = "select portfolioId from Portfolio where portfolioCode = ?;";

		int portfolioId = 0;
		try {
			// prepare and execute the select portfolio query
			ps = conn.prepareStatement(selectPortfolioQuery);
			ps.setString(1, portfolioCode);
			rs = ps.executeQuery();
			// if the portfolio does not exist with the given portfolioCode, throw an
			// exception
			if (!rs.next()) {
				throw new IllegalStateException("a portfolio does not exist with portfolioCode = " + portfolioCode);
				// else get the portfolioId
			} else {
				portfolioId = rs.getInt("portfolioId");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// query to insert a portfolio asset
		String insertPortfolioAssetQuery = "insert into PortfolioAsset(portfolioId, assetId, value) values (?, ?, ?);";

		try {
			// prepare and execute insert portfolio asset query
			ps = conn.prepareStatement(insertPortfolioAssetQuery);
			ps.setInt(1, portfolioId);
			ps.setInt(2, assetId);
			ps.setDouble(3, value);
			ps.executeUpdate();
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
