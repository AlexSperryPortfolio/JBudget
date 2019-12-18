package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CsvTransactionService {

    //region crud methods
	List<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Long id);

    Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate);

    Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Long> csvTransactionIds);

    long getUncategorizedTransactionsCount(Long uncategorizedTagId);

    long[] getUncategorizedTransactionIdsPaginated(Long uncategorizedTagId, int limit, int offset);

    Set<CsvTransaction> getUncategorizedTransactionsById(long[] uncategorizedTagIds);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    Set<CsvTransaction> getAllMatchingTransactions(String matchString);

    List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionList);

    void deleteCsvTransaction(CsvTransaction csvTransaction);
    //endregion

    //region helper methods
    JsonNode dateRange(JsonNode requestBody) throws IOException;

    JsonNode getUncategorizedTransactions(int page, int size);

    JsonNode applyTagsToTransactions(JsonNode requestBody) throws JsonProcessingException;
    //endregion

}
