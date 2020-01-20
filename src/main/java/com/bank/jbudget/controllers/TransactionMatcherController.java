package com.bank.jbudget.controllers;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransactionMatcher;
import com.bank.jbudget.services.CsvTransactionMatcherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
public class TransactionMatcherController {

    private static final Logger logger = Logger.getLogger(TransactionMatcherController.class);

    private CsvTransactionMatcherService csvTransactionMatcherService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public TransactionMatcherController(CsvTransactionMatcherService csvTransactionMatcherService) {
        this.csvTransactionMatcherService = csvTransactionMatcherService;
    }

//    @CrossOrigin
//    @GetMapping(value = "/transactionMatcher", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<JsonNode> getAllTransactionMatchers() {
//        logger.info("Call to getAllTransactionMatchers");
//        List<CsvTransactionMatcher> matchers = csvTransactionMatcherService.getAllCsvTransactionMatchers();
//        ObjectNode responseNode = objectMapper.valueToTree(matchers);
//        return new ResponseEntity<>(responseNode, HttpStatus.OK);
//    }

    @CrossOrigin
    @PostMapping(value = "/transactionMatcher", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> createMatcher(@RequestBody JsonNode requestBody) throws IOException {
        logger.info("Call to createMatcher " + requestBody.toString());
        JsonNode responseNode = csvTransactionMatcherService.createMatcher(requestBody);
        HttpStatus responseStatus = responseNode.findValue(CommonConstants.MESSAGE) == null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseNode, responseStatus);
    }

    @CrossOrigin
    @PutMapping(value = "/transactionMatcher/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> updateMatcher(@PathVariable("id") Long id, @RequestBody JsonNode requestBody) throws JsonProcessingException {
        logger.info("Call to updateMatcher " + requestBody);
        JsonNode responseNode = csvTransactionMatcherService.updateMatcher(id, requestBody);
        HttpStatus responseStatus = responseNode.findValue(CommonConstants.MESSAGE) == null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseNode, responseStatus);
    }

    @CrossOrigin
    @DeleteMapping(value = "/transactionMatcher/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> deleteMatcher(@PathVariable("id") Long id) {
        logger.info("Call to deleteMatcher " + id);
        JsonNode responseNode = csvTransactionMatcherService.deleteMatcher(id);
        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }
}
