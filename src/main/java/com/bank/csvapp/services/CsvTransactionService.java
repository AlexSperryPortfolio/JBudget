package com.bank.csvapp.services;

import com.bank.csvapp.domain.CsvTransaction;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CsvTransactionService {
	
	List<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Integer id);

    Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate);

    Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Integer> csvTransactionIds);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionIterable);

    void deleteCsvTransaction(CsvTransaction csvTransaction);

}
