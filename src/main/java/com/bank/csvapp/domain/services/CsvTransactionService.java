package com.bank.csvapp.domain.services;

import com.bank.csvapp.domain.CsvTransaction;

public interface CsvTransactionService {
	
	Iterable<CsvTransaction> listAllTransactions();

    CsvTransaction getTransactionById(Integer id);

    CsvTransaction saveTransaction(CsvTransaction transaction);

    Iterable<CsvTransaction> saveTransactionList(Iterable<CsvTransaction> transactionIterable);

    void deleteTransaction(Integer id);

}
