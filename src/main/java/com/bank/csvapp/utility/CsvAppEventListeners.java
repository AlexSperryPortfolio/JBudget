package com.bank.csvapp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CsvAppEventListeners implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private CsvTransactionTagManager csvTransactionTagManager;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        csvTransactionTagManager.seedBaseTags();
    }
}
