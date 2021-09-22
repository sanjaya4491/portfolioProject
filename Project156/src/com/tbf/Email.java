package com.tbf;

/**
 * This class stores a persons email
 */

public class Email {
	
	private Integer emailId;
	private String address;

	public Email(String address) {
		this.address = address;
	}

	public Email(Integer emailId, String address) {
		this.emailId = emailId;
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public Integer getEmailId() {
		return this.emailId;
	}

	@Override
	public String toString() {
		return String.format("%s", this.address);
	}
	
	

}
