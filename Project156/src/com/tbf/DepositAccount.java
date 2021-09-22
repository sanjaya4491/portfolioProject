package com.tbf;

/**
 * This Stores the the deposit account of the assets
 */

public class DepositAccount extends Asset {

	private double apr;
	private double totalBalance;

	public DepositAccount(String code, String type, String label, double apr) {
		super(code, type, label);
		this.apr = apr;
	}
	
	public DepositAccount(Integer assetId, String code, String type, String label, double apr) {
		super(assetId, code, type, label);
		this.apr = apr;
	}
	
	public DepositAccount(DepositAccount d) {
		super(d.code, d.type, d.label);
		this.code = d.code;
		this.type = d.type;
		this.label = d.label;
		this.apr = d.apr;
	}

	public double getApr() {
		return this.apr;
	}
	
	public double getTotalBalance() {
		return this.totalBalance;
	}

	/**
	 * This method returns a annualReturn of a deposit account
	 */
	@Override
	public double getAnnualReturn() {
		if(this.apr >= 0 && this.apr <= 1) {
			return ((Math.pow(Math.E, (this.apr))) - 1) * this.totalBalance;
		} else {
			return ((Math.pow(Math.E, (this.apr) / 100)) - 1) * this.totalBalance;
		}
	}

	/**
	 * This method returns a returnRate of a deposit account
	 */
	@Override
	public double getReturnRate() {
		double annualReturn = this.getAnnualReturn();
		return (annualReturn / this.totalBalance) * 100;
	}

	/**
	 * This method returns the value of a deposit account
	 */
	@Override
	public double getTotal() {
		return this.totalBalance;
	}

	/**
	 * This method returns the risk of a deposit account.
	 * Risk is always 0 for a deposit account
	 */
	@Override
	public double getRisk() {
		return 0;
	}

	/**
	 * Method that sets the total balance of a deposit account
	 */
	@Override
	public double setValue(double assetValue) {
		return this.totalBalance = assetValue;
	}

	@Override
	public String toString() {
		return "DepositAccount [apr=" + apr + ", totalBalance=" + totalBalance + "]";
	}
	
	
}