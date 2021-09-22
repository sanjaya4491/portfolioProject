package com.tbf;

import java.util.List;

/**
 * This class stores the information a broker and it extends the person class
 */

public abstract class Broker extends Person {
	
	private String title;
	private String section;
	
	public Broker(String personCode, String lastName, String firstName, Address address, List<Email> email,
			String title, String section) {
		super(personCode, lastName, firstName, address, email);
		this.title = title;
		this.section = section;
	}
	
	public Broker(Integer personId, String personCode, String lastName, String firstName, Address address, List<Email> email,
			String title, String section) {
		super(personId, personCode, lastName, firstName, address, email);
		this.title = title;
		this.section = section;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getSection() {
		return this.section;
	}
	
	/**
	 * abstract method that gets the fees for the broker
	 */
	public abstract double getFee();

	/**
	 * abstract method gets the commission for the broker
	 */
	public abstract double getCommission();
	
}
