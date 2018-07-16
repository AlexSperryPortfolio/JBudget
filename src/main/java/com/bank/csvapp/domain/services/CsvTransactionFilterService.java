package com.bank.csvapp.domain.services;

import com.bank.csvapp.domain.CsvTransactionFilter;

public interface CsvTransactionFilterService {
	Iterable<CsvTransactionFilter> listAllTransactionFilters();

    CsvTransactionFilter getTransactionFilterById(Integer id);

    CsvTransactionFilter saveTransactionFilter(CsvTransactionFilter transactionFilter);

    Iterable<CsvTransactionFilter> saveCsvTransactionFilterList(Iterable<CsvTransactionFilter> transactionFilterIterable);

    void deleteCsvTransactionFilter(Integer id);
}
