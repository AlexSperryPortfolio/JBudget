package com.bank.csvapp.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bank.csvapp.domain.CsvTransaction;

public class FileDto {
	
	public List<CsvTransaction> csvFileImport() {
		String line = "";
		Scanner sc = null;
		String[] splitArray;
		CsvTransaction transaction;
		List<CsvTransaction> transactionList = new ArrayList<>();
		
		try {
	        sc = new Scanner(new BufferedReader(new FileReader("C:\\Users\\Admin\\Documents\\workspace-sts-3.9.2.RELEASE\\JBudget\\src\\main\\resources\\static\\csv\\AccountHistory.csv")));
	        while (sc.hasNext()) {
	        	line = sc.nextLine();
	        	if(line != null && !line.isEmpty()) {
		            splitArray = line.replaceAll(",,", ", ,").split(",");
//		            0- Account Number, 1- Post Date, 2- Check, 3- Description, 4- Debit, 5- Credit, 6- Status, 7- Balance
		            transaction = new CsvTransaction(splitArray[0],splitArray[1],splitArray[2],splitArray[3],splitArray[4],splitArray[5],splitArray[6],splitArray[7]);
		            transactionList.add(transaction);
	        	}
	        }
	    } catch (final IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (sc != null) {
	            sc.close();
	        }
	    }
		return transactionList;
	}

}
