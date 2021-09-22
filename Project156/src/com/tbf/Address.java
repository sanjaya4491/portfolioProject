package com.tbf;

/**
 * This "Address" class stores the Address of a person. That contains their
 * street, city, state, zip-code and their country
 */

public class Address {
	
	private Integer addressId;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	
	public Address(String street, String city, String state, String zipCode, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	public Address(Integer addressId, String street, String city, String state, String zipCode, String country) {
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	@Override
	public String toString() {
		return String.format("%s, %s %s %s", this.city, this.state, this.country, this.zipCode);
	}

	public String getStreet() {
		return this.street;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public String getCountry() {
		return this.country;
	}

	public Integer getAddressId() {
		return this.addressId;
	}
	
	

}
