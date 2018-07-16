package com.bank.csvapp.domain.services;

import com.bank.csvapp.domain.CsvTransaction;

public interface CsvTransactionService {
	
	Iterable<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Integer id);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    Iterable<CsvTransaction> saveCsvTransactionList(Iterable<CsvTransaction> csvTransactionIterable);

    void deleteCsvTransaction(Integer id);

}
