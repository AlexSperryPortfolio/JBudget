package com.bank.jbudget.controllers;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.services.CsvTransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class TransactionController {

    private static final Logger logger = Logger.getLogger(TransactionController.class);

    private CsvTransactionService csvTransactionService;

    public TransactionController(CsvTransactionService csvTransactionService) {
        this.csvTransactionService = csvTransactionService;
    }

    @CrossOrigin
    @PostMapping(value = "/dateRange", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> dateRange(@RequestBody JsonNode requestBody) throws IOException {
        logger.info("Call to dateRange " + requestBody);
        JsonNode responseNode = csvTransactionService.dateRange(requestBody);
        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/uncategorizedTransactions", params = {CommonConstants.PAGE, CommonConstants.SIZE}, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> getUncategorizedTransactions(@RequestParam(CommonConstants.PAGE) int page, @RequestParam(CommonConstants.SIZE) int size) {
        logger.info("Call to getUncategorizedTransactions (page=" + page + ", size=" + size + ")");
        JsonNode responseNode = csvTransactionService.getUncategorizedTransactions(page, size);
        HttpStatus responseStatus = responseNode.findValue("message") == null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseNode, responseStatus);
    }

    @CrossOrigin
    @PostMapping(value = "/uploadFile", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> uploadFile(@RequestParam(value = "file") MultipartFile uploadedFile) throws IOException {
        logger.info("Call to uploadFile " + uploadedFile.getOriginalFilename() + " " + uploadedFile.getSize() + " bytes");
        JsonNode responseNode = csvTransactionService.importCsvFile(uploadedFile);
        return new ResponseEntity<>(responseNode, HttpStatus.CREATED);
    }

    //TODO: use RollingFileAppender for logging and use loggers

//    @CrossOrigin
//    @GetMapping(value = "/seed", produces=MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<JsonNode> seed() {
//
//        tagAndMatcherBootstrapper.bootstrapBaseTagsGroupsAndMatchersIntoDb();
//        csvTransactionService.warmUpCacheCuzInMemDb();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseNode = objectMapper.createObjectNode();
//        return new ResponseEntity<>(responseNode, HttpStatus.OK);
//    }

}