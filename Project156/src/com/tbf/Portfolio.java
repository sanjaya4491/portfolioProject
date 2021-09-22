package com.tbf;

import java.util.Comparator;
import java.util.List;

/**
 * Class that holds information of a clients portfolio
 */

public class Portfolio {

	private Integer portfolioId;
	private String portfolioCode;
	private Person owner;
	private Broker manager;
	private Person beneficiary;
	private List<Asset> assetList;

	public Portfolio(String portfolioCode, Person owner, Broker manager, Person beneficiary,
			List<Asset> assetList) {
		this.portfolioCode = portfolioCode;
		this.owner = owner;
		this.manager = manager;
		this.beneficiary = beneficiary;
		this.assetList = assetList;
	}

	public Portfolio(Integer portfolioId, String portfolioCode, Person owner, Broker manager, Person beneficiary,
			List<Asset> assetList) {
		this.portfolioId = portfolioId;
		this.portfolioCode = portfolioCode;
		this.owner = owner;
		this.manager = manager;
		this.beneficiary = beneficiary;
		this.assetList = assetList;
	}

	public String getPortfolioCode() {
		return this.portfolioCode;
	}

	public Person getOwner() {
		return this.owner;
	}

	public Broker getManager() {
		return this.manager;
	}

	public Person getBeneficiary() {
		return this.beneficiary;
	}

	public List<Asset> getAssetList() {
		return this.assetList;
	}

	public Integer getPortfolioId() {
		return this.portfolioId;
	}

	/**
	 * This calculates the value of all the assets in the portfolio.
	 */
	public double getTotalPortfolioValue() {
		double total = 0;
		for (Asset a : this.assetList) {
			total = total + a.getTotal();
		}
		return total;
	}
	
	/**
	 * This calculates risk of all assets in the portfolio.
	 */
	public double getWeightedRisk() {
		double totalRisk = 0;
		for (Asset a : this.assetList) {
			totalRisk = totalRisk
					+ (a.getRisk() * (a.getTotal() / getTotalPortfolioValue()));
		}
		return totalRisk;
	}
	
	/**
	 * This gets the rateofReturn of all the assets in the portfolio.
	 */
	public double getTotalRateOfReturn() {
		double total = 0;
		for (Asset a : this.assetList) {
			total = total + a.getAnnualReturn();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Portfolio [portfolioId=" + portfolioId + ", portfolioCode=" + portfolioCode + ", owner=" + owner
				+ ", manager=" + manager + ", beneficiary=" + beneficiary + ", assetList=" + assetList + "]";
	}
	
	/**
	 * A comparator that orders portfolios by the owners last name/ first name
	 * in alphabetic ordering
	 */
	public static final class OwnerNameComparator implements Comparator<Portfolio> {

		@Override
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
		
	}
	
	/**
	 * A comparator that orders portfolios by their total value,
	 * highest value first
	 *
	 */
	public static final class TotalValueComparator implements Comparator<Portfolio> {

		@Override
		public int compare(Portfolio o1, Portfolio o2) {
			double totalValue1 = o1.getTotalPortfolioValue();
			double totalValue2 = o2.getTotalPortfolioValue();
			
			if (totalValue1 < totalValue2) {
				return 1;
			} else if (totalValue1 > totalValue2) {
				return -1;
			} else {
				return 0;
			}
		}
		
	}
	
	/**
	 * A comparator that orders portfolios by the managers type, then by their last name,
	 * then by their first name in alphabetic ordering
	 */
	public static final class ManagerNameComparator implements Comparator<Portfolio> {

		@Override
		public int compare(Portfolio o1, Portfolio o2) {
			String brokerType1 = o1.getManager().getTitle();
			String brokerType2 = o2.getManager().getTitle();
			
			int result = brokerType1.compareTo(brokerType2);
			if(result != 0) {
				return result;
			} else {
				String lastName1 = o1.getManager().getLastName();
				String lastName2 = o2.getManager().getLastName();
				
				result = lastName1.compareTo(lastName2);
				if(result != 0) {
					return result;
				} else {
					String firstName1 = o1.getManager().getFirstName();
					String firstName2 = o2.getManager().getFirstName();
					return firstName1.compareTo(firstName2);
				}
			}
		}
		
	}

}
