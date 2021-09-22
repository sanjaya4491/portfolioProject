package com.tbf;

import java.util.List;

/**
 * Class that produces a report on the portfolio's
 */

public class PortfolioReport {
	
	/**
	 * Main to retrieve the portfolio list and create the summary of it
	 * @param args
	 */
	public static void main(String args[]) {

		//create three lists for the three comparators
		SortedList<Portfolio> sortedListOwner = new SortedList<>(new Portfolio.OwnerNameComparator());
		SortedList<Portfolio> sortedListValue = new SortedList<>(new Portfolio.TotalValueComparator());
		SortedList<Portfolio> sortedListManager = new SortedList<>(new Portfolio.ManagerNameComparator());
		
		//get the normal list
		List<Portfolio> list = DatabaseLoader.getAllPortfolio();
		
		//use the list to insert into the sorted lists
		sortedListOwner.batchInsert(list);
		sortedListValue.batchInsert(list);
		sortedListManager.batchInsert(list);
		
		PortfolioUtils.portfolioSummaryReport(sortedListOwner);
		PortfolioUtils.portfolioSummaryReport(sortedListValue);
		PortfolioUtils.portfolioSummaryReport(sortedListManager);
	}
}
