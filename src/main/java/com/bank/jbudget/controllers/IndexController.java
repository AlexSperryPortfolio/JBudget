package com.bank.jbudget.controllers;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.utility.CsvTransactionTagManager;
import com.bank.jbudget.utility.FileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private CsvTransactionService csvTransactionService;
    @Autowired
    private CsvTransactionTagManager csvTransactionTagManager;

    @CrossOrigin
    @GetMapping(value = "/transactionTags", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> dateRange() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        if(CsvTransactionTagManager.csvTransactionTagCache.size() == 0) {
            csvTransactionTagManager.resetCsvTransactionTagCacheWithDbTags();
        }

        List<CsvTransactionTag> csvTagList = csvTransactionTagManager.getAllCsvTransactionTags();

        String csvTransactionListString = objectMapper.writeValueAsString(csvTagList);
        JsonNode node = objectMapper.readTree(csvTransactionListString);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/dateRange", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> dateRange(@RequestBody JsonNode requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        //get fields from body
        JsonNode startDateNode = requestBody.findValue(CommonConstants.START_DATE);
        JsonNode endDateNode = requestBody.findValue(CommonConstants.END_DATE);
        JsonNode filterTagArrayNode = requestBody.findValue(CommonConstants.FILTER_TAGS);

        List<String> filterTags = new ArrayList<>();
        if(filterTagArrayNode != null) {//get list of strings from filterTags json string
            CollectionType stringType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
            filterTags = objectMapper.readValue(filterTagArrayNode.toString(), stringType);
        }

        Date startDate = null;
        Date endDate = null;
        if(startDateNode != null && endDateNode != null) {
            startDate = new Date(startDateNode.asLong());
            endDate = new Date(endDateNode.asLong());
        }

        Set<CsvTransaction> matchingTransactions;

        if(filterTags.isEmpty()) {
            matchingTransactions = csvTransactionService.getAllTransactionsInRange(startDate, endDate);
        } else {
            System.out.println(Arrays.toString(filterTags.toArray())); //todo make this a log
            Set<Long> csvTagIdSet = filterTags.parallelStream()
                    .map(x -> CsvTransactionTagManager.csvTransactionTagCache.get(x))
                    .filter(Objects::nonNull)
                    .map(CsvTransactionTag::getId)
                    .collect(Collectors.toSet());
            if(!csvTagIdSet.isEmpty()) {
                matchingTransactions = csvTransactionService.getAllTransactionsInRangeAndWithTags(startDate, endDate, csvTagIdSet);
            } else {
                System.out.println("No tags match"); //todo make this a log
                matchingTransactions = new HashSet<>();
            }
        }

        String csvTransactionListString = objectMapper.writeValueAsString(matchingTransactions);
        JsonNode node = objectMapper.readTree(csvTransactionListString);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    //TODO: add front-end for adding new filters
    //TODO: add ability to apply or remove filters from view output
    //TODO: add types to table
    //TODO: use RollingFileAppender for logging

    //TODO: remove this when using a permanent database
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