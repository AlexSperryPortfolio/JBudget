package com.bank.csvapp.controllers;

import com.bank.csvapp.constants.CommonConstants;
import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.CsvTransactionTag;
import com.bank.csvapp.services.CsvTransactionService;
import com.bank.csvapp.utility.CsvTransactionTagManager;
import com.bank.csvapp.utility.FileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private CsvTransactionService csvTransactionService;
    @Autowired
    private CsvTransactionTagManager csvTransactionTagManager;

    @CrossOrigin
    @GetMapping(value = "/transactionTags", consumes="application/json", produces="application/json")
    public ResponseEntity<JsonNode> dateRange() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        warmUpCacheCuzInMemDb(); //todo: remove this when using a permanent database

        List<CsvTransactionTag> csvTagList = csvTransactionTagManager.getAllCsvTransactionTags();

        String csvTransactionListString = objectMapper.writeValueAsString(csvTagList);
        JsonNode node = objectMapper.readTree(csvTransactionListString);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/dateRange", consumes="application/json", produces="application/json")
    public ResponseEntity<JsonNode> dateRange(@RequestBody String body) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestBody = objectMapper.readTree(body);

        warmUpCacheCuzInMemDb(); //todo: remove this when using a permanent database

        //get fields from body
        Long startDate = requestBody.findValue(CommonConstants.START_DATE).asLong();
        Long endDate = requestBody.findValue(CommonConstants.END_DATE).asLong();
        String filterTagArrayString = requestBody.findValue(CommonConstants.FILTER_TAGS).toString();

        //get list of strings from filterTags json string
        CollectionType stringType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> filterTags = objectMapper.readValue(filterTagArrayString, stringType);

        Date start = new Date(startDate);
        Date end = new Date(endDate);

        Set<CsvTransaction> matchingTransactions;

        if(filterTags.isEmpty()) {
            matchingTransactions = csvTransactionService.getAllTransactionsInRange(start, end);
        } else {
            Set<Integer> csvTagIdSet = filterTags.parallelStream()
                    .map(x -> CsvTransactionTagManager.csvTransactionTagCache.get(x).getId())
                    .collect(Collectors.toSet());
            matchingTransactions = csvTransactionService.getAllTransactionsInRangeAndWithTags(start, end, csvTagIdSet);
        }

        String csvTransactionListString = objectMapper.writeValueAsString(matchingTransactions);
        JsonNode node = objectMapper.readTree(csvTransactionListString);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    //TODO: bootstrap on context change? (HR app way?)
    //TODO: add front-end for adding new filters
    //TODO: add ability to apply or remove filters from view output

    //TODO: add types to table
    //TODO: remove dollar signs from empty columns
    //TODO: stop continuing debit/credit values when next row is null

    private void warmUpCacheCuzInMemDb() {
        //warm up cache
        if(CsvTransactionTagManager.csvTransactionTagCache.size() == 0) {
            FileDto fileDto = new FileDto(csvTransactionTagManager);
            List<CsvTransaction> transactionList = fileDto.csvFileImport();
            csvTransactionService.saveCsvTransactionList(transactionList);
        }

        //update cache with db objects
        csvTransactionTagManager.resetCsvTransactionTagCacheWithDbTags();
    }

}