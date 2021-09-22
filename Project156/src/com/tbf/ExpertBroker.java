package com.tbf;

import java.util.List;

/**
 * Class that holds the information of an expert broker.
 * Extends the super class Broker
 */

public class ExpertBroker extends Broker {

	public ExpertBroker(String personCode, String lastName, String firstName, Address address, List<Email> email,
			String title, String section) {
		super(personCode, lastName, firstName, address, email, title, section);
	}
	
	public ExpertBroker(Integer personId, String personCode, String lastName, String firstName, Address address, List<Email> email,
			String title, String section) {
		super(personId, personCode, lastName, firstName, address, email, title, section);
	}
	
	/**
	 * method that returns the fees of an expert broker which is always 0
	 */
	@Override
	public double getFee() {
		return 0;
	}

	/**
	 * method that returns the commission of an expert broker
	 * 3.75% is the base commission rate
	 */
	@Override
	public double getCommission() {
		return .0375;
	}

}
