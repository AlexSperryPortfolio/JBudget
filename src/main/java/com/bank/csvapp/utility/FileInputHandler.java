package com.bank.csvapp.utility;

import com.bank.csvapp.constants.CommonConstants;
import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.CsvTransactionTag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

class FileInputHandler {

    private final CsvTransactionTagManager csvTransactionTagManager;

    FileInputHandler(CsvTransactionTagManager csvTransactionTagManager) {
        this.csvTransactionTagManager = csvTransactionTagManager;
    }

    String[] translateLineToArray(String line) {
        String[] splitArray;

        //get description quote indices
        int descIndexBeg = line.substring(CommonConstants.INDEX_OF_LAST_ACCOUNT_NUM_QUOTE).indexOf('\"') + 13;
        int descIndexEnd = line.substring(CommonConstants.INDEX_OF_LAST_ACCOUNT_NUM_QUOTE).lastIndexOf('\"') + 13;

        //isolate description using indices
        String desc = line.substring(descIndexBeg + 1, descIndexEnd);

        //clean description of commas
        desc = desc.replaceAll(",", "");

        //graft cleaned description section into cleaned line
        String cleanedLine = line.substring(0, descIndexBeg) + desc + line.substring(descIndexEnd + 1);

        splitArray = cleanedLine.split(",");

        //filter quotes on account number
        splitArray[0] = splitArray[0].replaceAll("\"", "");

        return splitArray;
    }

    CsvTransaction cleanLineArrayToCsvTransaction(String[] cleanLineArray) throws ParseException {
        java.util.Date postDate;
        java.sql.Date postDateDb = null;
        Float debit = null;
        Float credit = null;
        Float balance = null;
        CsvTransaction csvTransaction;

        //add Default Transactions
        List<CsvTransactionTag> typeList = csvTransactionTagManager.addDefaultTypes(cleanLineArray[3]);

        if (!cleanLineArray[1].isEmpty()) {
            postDate = new SimpleDateFormat("MM/dd/yyyy").parse(cleanLineArray[1]);
            postDateDb = new java.sql.Date(postDate.getTime());
        }
        if (!cleanLineArray[4].isEmpty())
            debit = Float.parseFloat(cleanLineArray[4]);
        if (!cleanLineArray[5].isEmpty())
            credit = Float.parseFloat(cleanLineArray[5]);
        if (!cleanLineArray[7].isEmpty())
            balance = Float.parseFloat(cleanLineArray[7]);

        //0- Account Number, 1- Post Date, 2- Check, 3- Description, 4- Debit, 5- Credit, 6- Status, 7- Balance
        csvTransaction = new CsvTransaction(cleanLineArray[0], postDateDb, cleanLineArray[2], cleanLineArray[3],
                debit, credit, cleanLineArray[6], balance, typeList);

        return csvTransaction;
    }
}