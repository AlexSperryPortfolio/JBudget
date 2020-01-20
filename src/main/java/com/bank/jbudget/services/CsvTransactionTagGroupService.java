package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransactionTagGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collection;
import java.util.List;

public interface CsvTransactionTagGroupService {

    //CRUD methods
    CsvTransactionTagGroup getCsvTransactionTagGroupById(long id);

    List<CsvTransactionTagGroup> getAllCsvTransactionTagGroups();

    CsvTransactionTagGroup saveCsvTransactionTagGroup(CsvTransactionTagGroup csvTransactionTagGroup);

    Iterable<CsvTransactionTagGroup> saveCsvTransactionTagGroups(Collection<CsvTransactionTagGroup> csvTransactionTagGroupList);

    void deleteCsvTransactionTagGroup(CsvTransactionTagGroup csvTransactionTagGroup);
    //endregion

    //region Helper Methods
    JsonNode createGroup(JsonNode requestNode) throws JsonProcessingException;

    JsonNode updateGroup(Long id, JsonNode requestNode) throws JsonProcessingException;

    JsonNode deleteGroup(Long id);
    //endregion
}