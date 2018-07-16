package com.bank.csvapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.csvapp.common.FileDto;
import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.services.CsvTransactionService;

@Controller
public class IndexController {
	
	@Value("${server.servlet.path}")
	private String rootUrl;
	
	@Autowired
	private CsvTransactionService csvTransactionService;

	@RequestMapping(value = {"/", ""})
    public String index(Model model) {
		model.addAttribute("rootUrl",rootUrl);
		
        return "index";
    }
	
	@RequestMapping(value="/seed")
	public String seedDb(Model model) {
		model.addAttribute("rootUrl",rootUrl);
		
		FileDto fileDto = new FileDto();
		List<CsvTransaction> transactionList = fileDto.csvFileImport();
		
		csvTransactionService.saveCsvTransactionList(transactionList);
		
		//TODO: bootstrap on context change? (HR app way?)
		//TODO: add front-end for adding new filters
		//TODO: add ability to apply or remove filters from view output

        //TODO: add types to table
        //TODO: remove dollar signs from empty columns
        //TODO: stop continuing debit/credit values when next row is null
		
		return "seed";
	}
	
	@RequestMapping(value = "/table")
    public String table(Model model) {
		model.addAttribute("rootUrl",rootUrl);
		model.addAttribute("transactionList",csvTransactionService.listAllCsvTransactions());
		
    	return "table";
    }

    @RequestMapping(value = "/login")
    public String login(Model model) {
    	model.addAttribute("rootUrl",rootUrl);
    	
        return "login";
    }
}
