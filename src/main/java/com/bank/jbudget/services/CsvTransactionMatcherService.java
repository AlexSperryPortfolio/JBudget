package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransactionMatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CsvTransactionMatcherService {

    //region CRUD Methods
    CsvTransactionMatcher getCsvTransactionMatcherById(long id);

    List<CsvTransactionMatcher> getAllCsvTransactionMatchers();

    CsvTransactionMatcher saveCsvTransactionMatcher(CsvTransactionMatcher csvTransactionMatcher);

    Iterable<CsvTransactionMatcher> saveCsvTransactionMatchers(Collection<CsvTransactionMatcher> csvTransactionMatcherList);

    void deleteCsvTransactionMatcher(CsvTransactionMatcher csvTransactionMatcher);
    //endregion

    //region Helper Methods
    Set<CsvTransactionMatcher> getAllCsvTransactionMatchersEagerFetches();

    Set<CsvTransactionMatcher> getAllCsvTransactionMatchersUsingGroupId(long id);

    JsonNode createMatcher(JsonNode requestBody) throws JsonProcessingException;

    JsonNode updateMatcher(Long id, JsonNode requestBody) throws JsonProcessingException;

    JsonNode deleteMatcher(Long id);
    //endregion
}
