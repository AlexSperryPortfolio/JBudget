package com.bank.csvapp.services;

import com.bank.csvapp.domain.CsvTransaction;

import java.util.List;

public interface CsvTransactionService {
	
	List<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Integer id);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionIterable);

    void deleteCsvTransaction(Integer id);

}
