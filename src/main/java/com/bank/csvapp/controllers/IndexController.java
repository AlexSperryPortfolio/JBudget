package com.bank.csvapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.csvapp.common.FileDto;
import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.services.CsvTransactionService;

@Controller
public class IndexController {
	
	@Autowired
	private CsvTransactionService csvTransactionService;

	@RequestMapping(value = "/")
    public String index() {
		
		FileDto fileDto = new FileDto();
		List<CsvTransaction> transactionList = fileDto.csvFileImport();
		
		csvTransactionService.saveTransactionList(transactionList);
		
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }
}
