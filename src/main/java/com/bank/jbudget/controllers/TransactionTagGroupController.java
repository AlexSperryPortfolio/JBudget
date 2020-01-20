package com.bank.jbudget.controllers;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.services.CsvTransactionTagGroupService;
import com.fasterxml.jackson.databind.JsonNode;
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
public class TransactionTagGroupController {

    private static final Logger logger = Logger.getLogger(TransactionTagGroupController.class);

    private CsvTransactionTagGroupService csvTransactionTagGroupService;

    public TransactionTagGroupController(
            CsvTransactionTagGroupService csvTransactionTagGroupService) {
        this.csvTransactionTagGroupService = csvTransactionTagGroupService;
    }

    @CrossOrigin
    @PostMapping(value = "/transactionTagGroup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> createTagGroup(@RequestBody JsonNode requestBody) throws IOException {
        logger.info("Call to createTagGroup " + requestBody.toString());
        HttpStatus responseStatus = HttpStatus.CREATED;
        JsonNode responseNode = csvTransactionTagGroupService.createGroup(requestBody);
        if(responseNode.findValue(CommonConstants.MESSAGE) != null) {
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseNode, responseStatus);
    }

    @CrossOrigin
    @PutMapping(value = "/transactionTagGroup/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> updateTagGroup(@PathVariable("id") Long id, @RequestBody JsonNode requestBody) throws IOException {
        logger.info("Call to updateTagGroup " + requestBody.toString());
        HttpStatus responseStatus = HttpStatus.CREATED;
        JsonNode responseNode = csvTransactionTagGroupService.updateGroup(id, requestBody);
        if(responseNode.findValue(CommonConstants.MESSAGE) != null) {
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseNode, responseStatus);
    }

    @CrossOrigin
    @DeleteMapping(value = "/transactionTagGroup/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> deleteTagGroup(@PathVariable("id") Long id) {
        logger.info("Call to updateTagGroup " + id);
        JsonNode responseNode = csvTransactionTagGroupService.deleteGroup(id);
        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }
}
