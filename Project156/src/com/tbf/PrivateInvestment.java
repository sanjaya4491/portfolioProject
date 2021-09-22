package com.tbf;

/**
 * This class has the total value of the investment , qurterlyDividend, baseReturn, and baseOmegaMeasure.
 */

public class PrivateInvestment extends Asset {
	
	private double quarterlyDividend;
	private double baseReturn;
	private double baseOmegaMeasure;
	private double totalValue;
	private double percentageStake;
	
	public PrivateInvestment(String code, String type, String label, double quarterlyDividend, double baseReturn,
			double baseOmegaMeasure, double totalValue) {
		super(code, type, label);
		this.quarterlyDividend = quarterlyDividend;
		this.baseReturn = baseReturn;
		this.baseOmegaMeasure = baseOmegaMeasure;
		this.totalValue = totalValue;
	}
	
	public PrivateInvestment(Integer assetId, String code, String type, String label, double quarterlyDividend, double baseReturn,
			double baseOmegaMeasure, double totalValue) {
		super(assetId, code, type, label);
		this.quarterlyDividend = quarterlyDividend;
		this.baseReturn = baseReturn;
		this.baseOmegaMeasure = baseOmegaMeasure;
		this.totalValue = totalValue;
	}
	
	public PrivateInvestment(PrivateInvestment p) {
		super(p.code, p.type, p.label);
		this.code = p.code;
		this.type = p.type;
		this.label = p.label;
		this.quarterlyDividend = p.quarterlyDividend;
		this.baseReturn = p.baseReturn;
		this.baseOmegaMeasure = p.baseOmegaMeasure;
		this.totalValue = p.totalValue;
	}

	public double getQuarterlyDividend() {
		return this.quarterlyDividend;
	}

	public double getBaseReturn() {
		return this.baseReturn;
	}

	public double getBaseOmegaMeasure() {
		return this.baseOmegaMeasure;
	}

	public double getTotalValue() {
		return this.totalValue;
	}
	
	public double getPercentageStake() {
		return this.percentageStake;
	}

	/**
	 * Method that return the annual return of a private investment
	 */
	@Override
	public double getAnnualReturn() {
		if(this.baseReturn >= 0 && this.baseReturn <= 1 && this.percentageStake >= 0 && this.percentageStake <= 1) {
			return ((this.baseReturn * this.totalValue) + 
					(4 * this.quarterlyDividend)) * this.percentageStake;
		} else {
			return (((this.baseReturn / 100) * this.totalValue) + 
					(4 * this.quarterlyDividend)) * (this.percentageStake / 100);
		}
	}
	
	/**
	 * Method that returns the return rate of a private investment
	 */
	@Override
	public double getReturnRate() {
		double annualReturn = this.getAnnualReturn();
		if(this.percentageStake >= 0 && this.percentageStake <= 1) {
			return (annualReturn / (this.getTotalValue() * (this.percentageStake))) * 100;
		} else {
			return (annualReturn / (this.getTotalValue() * (this.percentageStake / 100))) * 100;
		}
	}
	
	/**
	 * Method that returns the total value of a private investment
	 */
	@Override
	public double getTotal() {
		if(this.percentageStake >= 0 && this.percentageStake <= 1) {
			return this.totalValue * this.percentageStake;
		} else {
			return this.totalValue * (this.percentageStake / 100);
		}
	}
	
	/**
	 * Method that returns the risk of a private investment
	 */
	@Override
	public double getRisk() {
		return (this.baseOmegaMeasure + Math.pow(Math.E, (-125500 / this.totalValue)));
	}

	/**
	 * Method that sets the percentage stake of a private investment
	 */
	@Override
	public double setValue(double assetValue) {
		return this.percentageStake = assetValue;
	}

	@Override
	public String toString() {
		return "PrivateInvestment [quarterlyDividend=" + quarterlyDividend + ", baseReturn=" + baseReturn
				+ ", baseOmegaMeasure=" + baseOmegaMeasure + ", totalValue=" + totalValue + ", percentageStake="
				+ percentageStake + "]";
	}
	
}