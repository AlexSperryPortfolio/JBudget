package com.bank.csvapp.utility;

import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.CsvTransactionTag;
import com.bank.csvapp.services.CsvTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvAppEventListeners implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private CsvTransactionTagManager csvTransactionTagManager;
    @Autowired
    private CsvTransactionService csvTransactionService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
//        csvTransactionTagManager.seedBaseTags();
    }
}
