package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransactionTag;

import java.util.List;

public interface CsvTransactionTagService {
	
	List<CsvTransactionTag> getAllCsvTransactionTags();

    CsvTransactionTag getCsvTransactionTagById(Integer id);

    CsvTransactionTag saveCsvTransactionTag(CsvTransactionTag csvTransactionTag);

    List<CsvTransactionTag> saveCsvTransactionTagList(List<CsvTransactionTag> csvTransactionTagIterable);

    void deleteCsvTransactionTag(CsvTransactionTag csvTransactionTag);

}
