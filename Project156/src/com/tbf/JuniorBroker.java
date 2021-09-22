package com.tbf;

import java.util.List;

/**
 * Class that holds information on a junior broker.
 * Extends super class Broker
 * 
 */

public class JuniorBroker extends Broker {
	
	public JuniorBroker(String personCode, String lastName, String firstName, Address address, List<Email> email,
			String title, String section) {
		super(personCode, lastName, firstName, address, email, title, section);
	}
	
	public JuniorBroker(Integer personId, String personCode, String lastName, String firstName, Address address, List<Email> email,
			String title, String section) {
		super(personId, personCode, lastName, firstName, address, email, title, section);
	}

	/**
	 * Method that gets the fees of a junior broker.
	 * $75 is the base rate for each asset
	 */
	@Override
	public double getFee() {
		return 75;
	}

	/**
	 * Method that gets the commissions of a junior broker
	 * 1.25% is the base rate
	 */
	@Override
	public double getCommission() {
		return .0125;
	}

}
