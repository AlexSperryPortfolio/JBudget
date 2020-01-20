package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransactionTag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CsvTransactionTagService {
	
	List<CsvTransactionTag> getAllCsvTransactionTags();

    CsvTransactionTag getCsvTransactionTagById(Long id);

    Set<CsvTransactionTag> getAllTagsByIds(Long[] tagIds);

    CsvTransactionTag saveCsvTransactionTag(CsvTransactionTag csvTransactionTag);

    Iterable<CsvTransactionTag> saveCsvTransactionTags(Collection<CsvTransactionTag> csvTransactionTagList);

    void deleteCsvTransactionTag(CsvTransactionTag csvTransactionTag);

}
