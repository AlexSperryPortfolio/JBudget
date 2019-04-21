package com.bank.csvapp.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SeedData implements CommandLineRunner{

//	private CsvTransactionService csvTransactionService;
//
//	public SeedData(CsvTransactionService csvTransactionService) {
//	    this.csvTransactionService = csvTransactionService;
//    }

    @Transactional
    @Override
    public void run(String...strings) throws Exception {

//       if(csvTransactionService.listAllTransactions().spliterator().getExactSizeIfKnown() == 0) {
            //only load data if no data loaded
//            initData();
//        }
    }

//    private void initData() {
//    	FileDto fileDto = new FileDto();
//        fileDto.csvFileImport();
//    }

}
