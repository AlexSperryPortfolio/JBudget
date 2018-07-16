package com.bank.csvapp.domain.services;

import com.bank.csvapp.domain.CsvTransactionType;

public interface CsvTransactionTypeService {
	
	Iterable<CsvTransactionType> listAllCsvTransactionTypes();

    CsvTransactionType getCsvTransactionTypeById(Integer id);

    CsvTransactionType saveCsvTransactionType(CsvTransactionType csvTransactionType);

    Iterable<CsvTransactionType> saveCsvTransactionTypeList(Iterable<CsvTransactionType> csvTransactionTypeIterable);

    void deleteCsvTransactionType(Integer id);

}
