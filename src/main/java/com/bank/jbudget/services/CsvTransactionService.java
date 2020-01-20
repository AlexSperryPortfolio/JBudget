package com.bank.jbudget.services;

import com.bank.jbudget.domain.CsvTransaction;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CsvTransactionService {

    //region crud methods
	List<CsvTransaction> listAllCsvTransactions();

    CsvTransaction getCsvTransactionById(Long id);

    CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction);

    Iterable<CsvTransaction> saveCsvTransactions(Collection<CsvTransaction> csvTransactionList);

    void deleteCsvTransaction(CsvTransaction csvTransaction);
    //endregion

    //region helper methods
    JsonNode dateRange(JsonNode requestBody) throws IOException;

    JsonNode getUncategorizedTransactions(int page, int size);

    Set<CsvTransaction> getAllMatchingTransactions(String matchString);

    JsonNode importCsvFile(MultipartFile accountHistoryFile) throws IOException;
    //endregion

//    List<CsvTransaction> warmUpCacheCuzInMemDb();

}
