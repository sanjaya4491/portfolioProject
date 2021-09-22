package com.tbf;

/**
 * This class stores all the information about the stock for ex the name and the symbol and the price.
 */

public class Stock extends Asset {
	
	private double quarterlyDividend;
	private double baseReturn;
	private double betaMeasure;
	private String stockSymbol;
	private double sharePrice;
	private double numberOfShares;
	
	public Stock(String code, String type, String label, double quarterlyDividend, double baseReturn,
			double betaMeasure, String stockSymbol, double sharePrice) {
		super(code, type, label);
		this.quarterlyDividend = quarterlyDividend;
		this.baseReturn = baseReturn;
		this.betaMeasure = betaMeasure;
		this.stockSymbol = stockSymbol;
		this.sharePrice = sharePrice;
	}
	
	public Stock(Integer assetId, String code, String type, String label, double quarterlyDividend, double baseReturn,
			double betaMeasure, String stockSymbol, double sharePrice) {
		super(assetId, code, type, label);
		this.quarterlyDividend = quarterlyDividend;
		this.baseReturn = baseReturn;
		this.betaMeasure = betaMeasure;
		this.stockSymbol = stockSymbol;
		this.sharePrice = sharePrice;
	}
	
	public Stock(Stock s) {
		super(s.code, s.type, s.label);
		this.code = s.code;
		this.type = s.type;
		this.label = s.label;
		this.quarterlyDividend = s.quarterlyDividend;
		this.baseReturn = s.baseReturn;
		this.betaMeasure = s.betaMeasure;
		this.stockSymbol = s.stockSymbol;
		this.sharePrice = s.sharePrice;
	}

	public double getQuarterlyDividend() {
		return this.quarterlyDividend;
	}

	public double getBaseReturn() {
		return this.baseReturn;
	}

	public double getBetaMeasure() {
		return this.betaMeasure;
	}

	public String getStockSymbol() {
		return this.stockSymbol;
	}

	public double getSharePrice() {
		return this.sharePrice;
	}

	/**
	 * Method that gets the return rate of a stock
	 */
	@Override
	public double getReturnRate() {
		double annualReturn = this.getAnnualReturn();
		return (annualReturn / (this.sharePrice * this.numberOfShares)) * 100;
	}

	/**
	 * Method that gets the total value of a stock
	 */
	@Override
	public double getTotal() {
		return this.numberOfShares * this.sharePrice;
	}

	/**
	 * Method that gets the risk of a stock
	 */
	@Override
	public double getRisk() {
		return this.betaMeasure;
	}

	/**
	 * Method that gets the annual return of a stock
	 */
	@Override
	public double getAnnualReturn() {
		if(this.baseReturn >= 0 && this.baseReturn <= 1) {
			return (this.baseReturn * this.sharePrice * this.numberOfShares) +
					(4 * this.quarterlyDividend * this.numberOfShares);
		} else {
			return ((this.baseReturn / 100) * this.sharePrice * this.numberOfShares) +
					(4 * this.quarterlyDividend * this.numberOfShares);
		}
	}

	/**
	 * Method that sets the number of shares for a stock
	 */
	@Override
	public double setValue(double assetValue) {
		return this.numberOfShares = assetValue;
	}

	@Override
	public String toString() {
		return "Stock [quarterlyDividend=" + quarterlyDividend + ", baseReturn=" + baseReturn + ", betaMeasure="
				+ betaMeasure + ", stockSymbol=" + stockSymbol + ", sharePrice=" + sharePrice + ", numberOfShares="
				+ numberOfShares + "]";
	}
	
}