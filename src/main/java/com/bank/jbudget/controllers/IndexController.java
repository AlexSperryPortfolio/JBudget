package com.bank.jbudget.controllers;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.utility.FileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class IndexController {

    @Autowired
    private CsvTransactionService csvTransactionService;

    @CrossOrigin
    @PostMapping(value = "/dateRange", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> dateRange(@RequestBody JsonNode requestBody) throws IOException {
        System.out.println("Call to dateRange " + requestBody.toString());
        JsonNode responseNode = csvTransactionService.dateRange(requestBody);
        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/uncategorizedTransactions", params = {CommonConstants.PAGE, CommonConstants.SIZE}, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> getUncategorizedTransactions(@RequestParam(CommonConstants.PAGE) int page, @RequestParam(CommonConstants.SIZE) int size) throws IOException {
        System.out.println("Call to getUncategorizedTransactions (page=" + page + ", size=" + size + ")");
        JsonNode retNode = csvTransactionService.getUncategorizedTransactions(page, size);
        return new ResponseEntity<>(retNode, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/applyTags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> applyTagsToTransactions(@RequestBody JsonNode requestBody) throws IOException {
        System.out.println("Call to applyTags " + requestBody.toString());
        JsonNode responseNode = csvTransactionService.applyTagsToTransactions(requestBody);
        HttpStatus responseStatus = responseNode.findValue("message") == null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseNode, responseStatus);
    }

    @CrossOrigin
    @PostMapping(value = "/uploadFile", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> uploadFile(@RequestParam(value = "file") MultipartFile uploadedFile) throws IOException {
        System.out.println("Call to uploadFile " + uploadedFile.getOriginalFilename());

        csvTransactionService.importCsvFile(uploadedFile);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseNode = objectMapper.createObjectNode();
        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }

    //TODO: add front-end for adding new filters
    //TODO: add ability to apply or remove filters from view output
    //TODO: use RollingFileAppender for logging and use loggers

}