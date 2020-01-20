package com.bank.jbudget.utility;

import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.services.CsvTransactionTagService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CsvTransactionTagManager {

    private static final Logger logger = Logger.getLogger(CsvTransactionTagManager.class);

    private CsvTransactionTagService csvTransactionTagService;

    private final Map<String, CsvTransactionTag> csvTransactionTagCache = new HashMap<>();

    public CsvTransactionTagManager(CsvTransactionTagService csvTransactionTagService) {
        this.csvTransactionTagService = csvTransactionTagService;
    }

    private void warmUpCsvTransactionTagCache(final List<CsvTransactionTag> csvTransactionTagList) {
        csvTransactionTagList.parallelStream().forEach(x -> csvTransactionTagCache.put(x.getName(), x));
    }

    public CsvTransactionTag getTagFromCache(String key) {
        if(csvTransactionTagCache.isEmpty()) { //todo: handle this better
            logger.info("Warm up csvTransactionTagCache");
            resetCsvTransactionTagCacheWithDbTags();
        }
        return csvTransactionTagCache.get(key);
    }

    public void resetCsvTransactionTagCacheWithDbTags() {
        csvTransactionTagCache.clear();
        warmUpCsvTransactionTagCache(csvTransactionTagService.getAllCsvTransactionTags());
    }

    public List<CsvTransactionTag> getAllCsvTransactionTags() {
        if(csvTransactionTagCache.isEmpty()) { //todo: handle this better
            logger.info("Warm up csvTransactionTagCache");
            resetCsvTransactionTagCacheWithDbTags();
        }
        return csvTransactionTagCache.entrySet().parallelStream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
