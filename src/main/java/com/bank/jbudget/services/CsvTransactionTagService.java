package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransactionTag;

import java.util.List;

public interface CsvTransactionTagService {
	
	List<CsvTransactionTag> getAllCsvTransactionTags();

    CsvTransactionTag getCsvTransactionTagById(Long id);

    CsvTransactionTag saveCsvTransactionTag(CsvTransactionTag csvTransactionTag);

    List<CsvTransactionTag> saveCsvTransactionTagList(List<CsvTransactionTag> csvTransactionTagList);

    void deleteCsvTransactionTag(CsvTransactionTag csvTransactionTag);

}
