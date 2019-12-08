package com.bank.jbudget.controllers;

import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.utility.CsvTransactionTagManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.List;

@Controller
public class TransactionTagController {

    @Autowired
    private CsvTransactionTagManager csvTransactionTagManager;

    @CrossOrigin
    @GetMapping(value = "/transactionTags", consumes= MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> getAllTransactionTags() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CsvTransactionTag> csvTagList = csvTransactionTagManager.getAllCsvTransactionTags();
        JsonNode tagListNode = objectMapper.valueToTree(csvTagList);//cant just put this in return statement for some reason
        return new ResponseEntity<>(tagListNode, HttpStatus.OK);
    }
}
