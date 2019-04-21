package com.bank.csvapp.services;

import com.bank.csvapp.domain.CsvTransactionTag;

import java.util.List;

public interface CsvTransactionTagService {
	
	List<CsvTransactionTag> listAllCsvTransactionTags();

    CsvTransactionTag getCsvTransactionTagById(Integer id);

    CsvTransactionTag saveCsvTransactionTag(CsvTransactionTag csvTransactionTag);

    List<CsvTransactionTag> saveCsvTransactionTagList(List<CsvTransactionTag> csvTransactionTagIterable);

    void deleteCsvTransactionTag(Integer id);

}
