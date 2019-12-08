package com.bank.jbudget.controllers;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.utility.CsvTransactionTagManager;
import com.bank.jbudget.utility.FileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
                    .map(x -> csvTransactionTagManager.getTagFromCache(x))
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

    @CrossOrigin
    @GetMapping(value = "/uncategorizedTransactions", params = {CommonConstants.PAGE, CommonConstants.SIZE}, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> getUncategorizedTransactions(@RequestParam(CommonConstants.PAGE) int page, @RequestParam(CommonConstants.SIZE) int size) throws IOException {
        System.out.println("Call to getUncategorizedTransactions (page=" + page + ", size=" + size + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        Long uncategorizedTagId = csvTransactionTagManager.getTagFromCache("Not Categorized").getId();
//        Page<CsvTransaction> uncategorizedTransactionPage = csvTransactionService.getAllUncategorizedTransactionsPaginated(uncategorizedTagId, page, size);
//        List<CsvTransaction> uncategorizedTransactionList = uncategorizedTransactionPage.getContent();
//        Integer totalPages = uncategorizedTransactionPage.getTotalPages();

        long[] uncategorizedTransactionIds = csvTransactionService.getUncategorizedTransactionIds(uncategorizedTagId);
        int startIndex = page * size;
        int endIndex = Math.min(uncategorizedTransactionIds.length, startIndex + size);
        long[] paginatedIds = Arrays.copyOfRange(uncategorizedTransactionIds, startIndex, endIndex);
        Set<CsvTransaction> uncategorizedTransactions = csvTransactionService.getUncategorizedTransactionsPaginated(paginatedIds);
        int numPages = uncategorizedTransactionIds.length/size + (uncategorizedTransactionIds.length % size == 0 ? 0 : 1);

        String uncategorizedTransactionSetString = objectMapper.writeValueAsString(uncategorizedTransactions);
        JsonNode uncategorizedTransactionsArray = objectMapper.readTree(uncategorizedTransactionSetString);
        ObjectNode retNode = objectMapper.createObjectNode();
        retNode.set("uncategorizedTransactions", uncategorizedTransactionsArray);
        retNode.put("totalPages", numPages);
        return new ResponseEntity<>(retNode, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/applyTag", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> createTransaction(@RequestBody JsonNode requestBody) throws IOException {
        HttpStatus returnStatus = HttpStatus.CREATED;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();
        JsonNode transactionTagsNode = requestBody.findValue("transactionTags");
        JsonNode transactionIdNode = requestBody.findValue("transactionId");

        String[] transactionTagKeys;
        long transactionId;
        if(transactionTagsNode != null) {
            transactionTagKeys = objectMapper.treeToValue(transactionTagsNode, String[].class);
            List<CsvTransactionTag> transactionTags = new ArrayList<>();
            for(String key : transactionTagKeys) {
                transactionTags.add(csvTransactionTagManager.getTagFromCache(key));
            }

            if(transactionIdNode != null) { //single transaction case
                transactionId = transactionIdNode.asLong();

                //todo: test apply tags in taglist to transactionID
                CsvTransaction transaction = csvTransactionService.getCsvTransactionById(transactionId);
                transaction.addTags(transactionTags);
                csvTransactionService.saveCsvTransaction(transaction);
            } else {
                if(transactionTags.size() == 1) {
                    //todo: test apply tag (taglist[0]) to all transactions
                    Set<CsvTransaction> matchingTransactions = csvTransactionService.getAllMatchingTransactions(transactionTags.get(0).getMatchString());
                    matchingTransactions.parallelStream().forEach(x -> x.addTag(transactionTags.get(0)));
                    csvTransactionService.saveCsvTransactionList(matchingTransactions.parallelStream().collect(Collectors.toList()));
                } else {
                    System.out.println("ERROR: only one tag can be used if no transactionId present");
                    returnStatus = HttpStatus.BAD_REQUEST;
                    responseNode.put("message","only one tag can be used if no transactionId present");
                }
            }
        } else { //multiple transactions case using matchString
            System.out.println("ERROR: no transactionTags");
            returnStatus = HttpStatus.BAD_REQUEST;
            responseNode.put("message","requires transactionTags attribute");
        }


        JsonNode node = objectMapper.readTree("");
        return new ResponseEntity<>(node, returnStatus);
    }

    //TODO: add front-end for adding new filters
    //TODO: add ability to apply or remove filters from view output
    //TODO: use RollingFileAppender for logging and use loggers

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