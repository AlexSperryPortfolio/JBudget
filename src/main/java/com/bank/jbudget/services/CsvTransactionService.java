package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransaction;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CsvTransactionService {
	
	List<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Long id);

    Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate);

    Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Long> csvTransactionIds);

//    Page<CsvTransaction> getAllUncategorizedTransactionsPaginated(Long uncategorizedTagId, int page, int size);

    long[] getUncategorizedTransactionIds(Long uncategorizedTagId);

    Set<CsvTransaction> getUncategorizedTransactionsPaginated(long[] uncategorizedTagIds);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    Set<CsvTransaction> getAllMatchingTransactions(String matchString);

    List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionList);

    void deleteCsvTransaction(CsvTransaction csvTransaction);

}
