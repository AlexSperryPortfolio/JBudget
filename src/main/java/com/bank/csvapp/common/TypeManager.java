package com.bank.csvapp.common;

import java.util.ArrayList;
import java.util.List;

import com.bank.csvapp.domain.CsvTransactionType;

public class TypeManager {
	
	public static List<CsvTransactionType> addDefaultTypes(String description) {
		String descLower = description.toLowerCase();
		List<CsvTransactionType> typeList = new ArrayList<>();
		
		switch(descLower) {
			//Fast Food
			case "mcdonald's":
			case "burger king":
			case "culvers":
			case "wendy":
			case "taco bell":
			case "hardee's":
			case "pei wei":
			case "subway":
			case "naf naf":
			case "which wich":
			case "jimmy johns":
			case "arby's":
			case "chipotle":
			case "mim's cafe":
			case "erbert and gerbe":
			case "pizza luce":
			case "potbelly":
			case "new asia":
			case "papa john's":
			case "toppers":
			case "davanni's":
			case "my burger":
			case "panera":
			case "good to go deli":
			case "taco john's":
			case "au bon pain":
			case "bruggers":
			case "dave's downtown":
			case "the brothers del":
			case "hen house":
			case "sporty's pub":
				typeList.add(new CsvTransactionType("Fast Food"));
				break;
				
			//Student Loans
			case "discover":
				typeList.add(new CsvTransactionType("Discover Loans"));
				typeList.add(new CsvTransactionType("Student Loans"));
				typeList.add(new CsvTransactionType("Bills"));
			case "firstmark":
				typeList.add(new CsvTransactionType("Firstmark Loans"));
				typeList.add(new CsvTransactionType("Student Loans"));
				typeList.add(new CsvTransactionType("Bills"));
				break;
			case "edfinancial":
				typeList.add(new CsvTransactionType("EdFinancial Loans"));
				typeList.add(new CsvTransactionType("Student Loans"));
				typeList.add(new CsvTransactionType("Bills"));
				break;
			case "aspire":
				typeList.add(new CsvTransactionType("Aspire Loans"));
				typeList.add(new CsvTransactionType("Student Loans"));
				typeList.add(new CsvTransactionType("Bills"));
				break;
			case "dept education student loan":
				typeList.add(new CsvTransactionType("Department of Education Loans"));
				typeList.add(new CsvTransactionType("Student Loans"));
				typeList.add(new CsvTransactionType("Bills"));
				break;
			case "student loan dslcpymnts":
				typeList.add(new CsvTransactionType("Student Loan DSLCPAYMENTS"));
				typeList.add(new CsvTransactionType("Student Loans"));
				typeList.add(new CsvTransactionType("Bills"));
				break;
				
			//Car Insurance
			case "liberty mutual":
				typeList.add(new CsvTransactionType("Bills"));
				typeList.add(new CsvTransactionType("Car Insurance"));
				break;
				
			//Car Repair
			case "tuan auto repair":
			case "metric auto work":
				typeList.add(new CsvTransactionType("Car Repair"));
				break;
				
			//Car Registration
			case "mn dvs":
				typeList.add(new CsvTransactionType("Car Registration"));
				break;
				
			//Gas Station
			case "bp":
			case "holiday stnstore":
			case "winner":
			case "superamerica":
			case "caseys":
				typeList.add(new CsvTransactionType("Gas Station"));
				break;
				
			//Groceries
			case "cub fo":
			case "tim and tom's":
			case "super one":
			case "rainbow":
				typeList.add(new CsvTransactionType("Groceries"));
				break;
				
			//Target
			case "target":
				typeList.add(new CsvTransactionType("Target"));
				break;
				
			//Wal-Mart
			case "wal-mart":
				typeList.add(new CsvTransactionType("Wal-Mart"));
				break;
			
			//Software
			case "lastpass":
				typeList.add(new CsvTransactionType("Software"));
				typeList.add(new CsvTransactionType("Last Pass"));
				break;
				
			case "atm surcharge":
				typeList.add(new CsvTransactionType("Atm Surcharge"));
				break;
			
			//Entertainment
				//Alcohol
			case "total wine":
			case "mgm wine":
			case "liquor boy":
			case "park liquor stor":
			case "edina liquor":
			case "cub li":
			case "barstock liquors":
			case "the little wine saint paul":
			case "sharretts liquor":
			case "ma and pa liquor":
			case "merwin liquor":
				typeList.add(new CsvTransactionType("Alcohol"));
				break;
				
				//Streaming
			case "netflix":
			case "hbo dig":
				typeList.add(new CsvTransactionType("Entertainment"));
				typeList.add(new CsvTransactionType("Streaming Video"));
				break;
				
				//Games
			case "valve bellevue":
			case "steamgames":
			case "steampowered":
				typeList.add(new CsvTransactionType("Entertainment"));
				typeList.add(new CsvTransactionType("Steam"));
				break;
			case "microsoft *xbo":
				typeList.add(new CsvTransactionType("Entertainment"));
				typeList.add(new CsvTransactionType("Xbox"));
				break;
				
				//Amazon
			case "amazon":
				typeList.add(new CsvTransactionType("Entertainment"));
				typeList.add(new CsvTransactionType("Amazon"));
				break;
			
			//Credits
			case "mn dept of deed":
				typeList.add(new CsvTransactionType("Unemployment"));
				break;
				
			case "aston technologi":
				typeList.add(new CsvTransactionType("Paycheck"));
				typeList.add(new CsvTransactionType("Aston Technologies"));
				break;
			case "payroll":
				typeList.add(new CsvTransactionType("Paycheck"));
				typeList.add(new CsvTransactionType("Backbone Consultants"));
				break;
			case "kasasa tunes":
				typeList.add(new CsvTransactionType("Bank Rewards"));
				break;
			case "deposit":
				typeList.add(new CsvTransactionType("Cash Deposit"));
				break;
				
			default:
				typeList.add(new CsvTransactionType("Not Categorized"));
				break;
		}
		
		return typeList;
	}

}
