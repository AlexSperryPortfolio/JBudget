package com.bank.jbudget.controllers;

import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.utility.CsvTransactionTagManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TransactionTagController {

    private static final Logger logger = Logger.getLogger(TransactionTagController.class);

    private CsvTransactionTagManager csvTransactionTagManager;

    public TransactionTagController(CsvTransactionTagManager csvTransactionTagManager) {
        this.csvTransactionTagManager = csvTransactionTagManager;
    }

    @CrossOrigin
    @GetMapping(value = "/transactionTags", consumes= MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> getAllTransactionTags() {
        logger.info("Call to getAllTransactionTags");
        ObjectMapper objectMapper = new ObjectMapper();
        List<CsvTransactionTag> csvTagList = csvTransactionTagManager.getAllCsvTransactionTags();
        JsonNode tagListNode = objectMapper.valueToTree(csvTagList);//cant just put this in return statement for some reason
        return new ResponseEntity<>(tagListNode, HttpStatus.OK);
    }
}
