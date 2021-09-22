package com.tbf;

/**
 * Class that holds methods that print summary's about portfolio's to the standard output
 */

public class PortfolioUtils {

	/**
	 * Method thats lists of portfolios, assets, and people and prints the portfolio code, owner,
	 * manager, the fees and commissions for the manager, and the weighted risk, return, and total of each
	 * portfolio
	 * @param portfolio
	 * @param assetList
	 * @param personList
	 */
	public static void portfolioSummaryReport(SortedList<Portfolio> portfolioList) {
		
		//empty variables to store totals to print later
		double feesTotal = 0;
		double commissionsTotal = 0;
		double returnTotal = 0;
		double totalTotal = 0;

		System.out.println("Portfolio Summary Report");
		System.out.println(
				"============================================================================================================================================");
		System.out.println(String.format("%-10s %-20s %-26s %-16s %-13s %-19s %-16s %-16s", "Portfolio", "Owner",
				"Manager", "Fees", "Commissions", "Weighted Risk", "Return", "Total"));
		
		//iterate over each portfolio
		for (Portfolio portfolio : portfolioList) {
			//get the size of the list
			int portfolioAssetListSize = portfolio.getAssetList().size();
			//adding the cumulative totals for all the portfolios
			feesTotal += portfolio.getManager().getFee() * portfolioAssetListSize;
			commissionsTotal += portfolio.getManager().getCommission() * portfolio.getTotalRateOfReturn();
			returnTotal += portfolio.getTotalRateOfReturn();
			totalTotal += portfolio.getTotalPortfolioValue();

			System.out.printf(String.format("%-10s %-20s %-20s %-5s %-10.2f %-5s %-13.2f %-13.4f %-5s %-10.2f %-5s %-10.2f\n",
					portfolio.getPortfolioCode(), portfolio.getOwner().toString(), portfolio.getManager().toString(), "$", portfolio.getManager().getFee() * portfolioAssetListSize, "$",
					portfolio.getManager().getCommission() * portfolio.getTotalRateOfReturn(), portfolio.getWeightedRisk(), "$",
					portfolio.getTotalRateOfReturn(), "$", portfolio.getTotalPortfolioValue(), "$",
					portfolio.getWeightedRisk(), "$", portfolio.getTotalRateOfReturn()));
		}

		System.out.println(
				"                                                     ========================================================================================");

		System.out.printf(String.format(" %50s  $ %10.2f     $ %10.2f   %20s %12.2f   $ %12.2f\n\n\n", "Total", feesTotal, commissionsTotal,
				"$", returnTotal, totalTotal));
	}
	
	/**
	 * Prints detailed information about the portfolio that includes information on the
	 * owner, manager, beneficiary and the owners assets
	 * @param portfolio
	 * @param assetList
	 * @param personList
	 */
	public static void portfolioDetails(SortedList<Portfolio> portfolioList) {
		
		System.out.println("Portfolio Details");
		System.out.println("==================================================================================================================");

		//iterate for each portfolio
		for (Portfolio portfolio : portfolioList) {
			System.out.println("Portfolio " + portfolio.getPortfolioCode());
			System.out.println("-------------------------------------------------------------");
			
			System.out.println("Owner:");
			System.out.println(portfolio.getOwner().toString());
			System.out.println(portfolio.getOwner().getEmail().toString());
			System.out.println(portfolio.getOwner().getAddress().getStreet());
			System.out.println(portfolio.getOwner().getAddress().toString());
			
			System.out.println("Manager:");
			System.out.println(portfolio.getManager().toString());
			
			System.out.println("Beneficiary:");
			if(portfolio.getBeneficiary() == null) {
				System.out.println("none");
			} else if(portfolio.getBeneficiary() instanceof JuniorBroker) {
				System.out.println(portfolio.getBeneficiary().toString());
				System.out.println("Junior Broker");
				System.out.println(portfolio.getBeneficiary().getEmail().toString());
				System.out.println(portfolio.getBeneficiary().getAddress().getStreet());
				System.out.println(portfolio.getBeneficiary().getAddress().toString());
			} else if(portfolio.getBeneficiary() instanceof ExpertBroker) {
				System.out.println(portfolio.getBeneficiary().toString());
				System.out.println("Expert Broker");
				System.out.println(portfolio.getBeneficiary().getEmail().toString());
				System.out.println(portfolio.getBeneficiary().getAddress().getStreet());
				System.out.println(portfolio.getBeneficiary().getAddress().toString());
			} else {
				System.out.println(portfolio.getBeneficiary().toString());
				System.out.println(portfolio.getBeneficiary().getEmail());
				System.out.println(portfolio.getBeneficiary().getAddress().getStreet());
				System.out.println(portfolio.getBeneficiary().getAddress().toString());
			}
			
			System.out.println("Assets");
			System.out.println(String.format("%-10s %-38s %-18s %-10s %-22s %-16s", "Code", "Asset",
					"Return Rate", "Risk", "Annual Return", "Value"));
			for(Asset a : portfolio.getAssetList()) {
				System.out.printf("%-10s %-35s %12.2f %%  %10.2f  $ %16.2f  $ %11.2f\n", a.getCode(), a.getLabel(), a.getReturnRate(), a.getRisk(),a.getAnnualReturn(), a.getTotal());
			}
			System.out.println();
			System.out.println("                                                    ---------------------------------------------------------------------");
			System.out.printf(String.format("%60s %12.4f  $  %15.2f  $  %10.2f", "Totals" , portfolio.getWeightedRisk() , portfolio.getTotalRateOfReturn() , portfolio.getTotalPortfolioValue()));
			System.out.println();
		}
	}


}
