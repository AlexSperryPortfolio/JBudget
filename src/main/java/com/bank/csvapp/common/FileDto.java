package com.bank.csvapp.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bank.csvapp.constants.Constants;
import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.CsvTransactionType;

public class FileDto {
	
	public List<CsvTransaction> csvFileImport() {
		String line;
		Scanner sc = null;
		String[] splitArray;
		CsvTransaction transaction;
		java.util.Date postDate;
		java.sql.Date postDateDb = null;
		Float debit = null;
		Float credit = null;
		Float balance = null;
		List<CsvTransaction> transactionList = new ArrayList<>();
		
		try {
	        sc = new Scanner(new BufferedReader(new FileReader("C:\\Users\\Admin\\Desktop\\Personal files\\CsvFiles\\AccountHistory.csv")));
	        while (sc.hasNext()) {
	        	line = sc.nextLine();
	        	if(line != null && !line.isEmpty() && !line.equals("Account Number,Post Date,Check,Description,Debit,Credit,Status,Balance")) {
		            
	        		//replace empty entries with a space so the split method counts it still
	        		line = line.replaceAll(",,", ", ,");

	        		//get description quote indices
	        		int descIndexBeg = line.substring(Constants.INDEX_OF_LAST_ACCOUNT_NUM_QUOTE).indexOf('\"')+13;
	        		int descIndexEnd = line.substring(Constants.INDEX_OF_LAST_ACCOUNT_NUM_QUOTE).lastIndexOf('\"')+13;

	        		//isolate description using indices
	        		String desc = line.substring(descIndexBeg+1,descIndexEnd);

					//apply Citibank nospace fix by adding a space
					if(desc.contains("CITIBANK")) {
						desc = desc.replace("CITIBANK,N.A.","CITIBANK N.A.");
					}

					//clean description of commas
					desc = desc.replaceAll(",","");

					//graft cleaned description section into cleaned line
					String cleanedLine = line.substring(0,descIndexBeg) + desc + line.substring(descIndexEnd+1);

	        		splitArray = cleanedLine.split(",");

	        		//filter quotes on account number
	        		splitArray[0] = splitArray[0].replaceAll("\"","");
	        		
	        		//add Default Transactions
	        		List<CsvTransactionType> typeList = TypeManager.addDefaultTypes(splitArray[3]);
	        		
	        		if(!splitArray[1].equals(" ")) {
	        			postDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitArray[1]);
	        			postDateDb = new java.sql.Date(postDate.getTime());
	        		}
	        		if(!splitArray[4].equals(" "))
	        			debit = Float.parseFloat(splitArray[4]);
	        		if(!splitArray[5].equals(" "))
	        			credit = Float.parseFloat(splitArray[5]);
	        		if(!splitArray[7].equals(" "))
	        			balance = Float.parseFloat(splitArray[7]);
	        		
		            //0- Account Number, 1- Post Date, 2- Check, 3- Description, 4- Debit, 5- Credit, 6- Status, 7- Balance
		            transaction = new CsvTransaction(splitArray[0],postDateDb,splitArray[2],splitArray[3],
		            		debit, credit, splitArray[6], balance, typeList);
		            transactionList.add(transaction);
	        	}
	        }
	    } catch (final IOException | ParseException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (sc != null) {
	            sc.close();
	        }
	    }
		return transactionList;
	}
}
