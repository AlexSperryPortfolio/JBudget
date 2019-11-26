package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransaction;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CsvTransactionService {
	
	List<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Long id);

    Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate);

    Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Long> csvTransactionIds);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionList);

    void deleteCsvTransaction(CsvTransaction csvTransaction);

}
