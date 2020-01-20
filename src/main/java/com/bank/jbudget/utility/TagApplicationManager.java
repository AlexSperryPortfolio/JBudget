package com.bank.jbudget.utility;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionMatcher;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.repositories.CsvTransactionMatcherRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagApplicationManager {

    private CsvTransactionMatcherRepository csvTransactionMatcherRepository;
    private CsvTransactionTagManager csvTransactionTagManager;

    public TagApplicationManager(CsvTransactionMatcherRepository csvTransactionMatcherRepository, CsvTransactionTagManager csvTransactionTagManager) {
        this.csvTransactionMatcherRepository = csvTransactionMatcherRepository;
        this.csvTransactionTagManager = csvTransactionTagManager;
    }

    public Set<CsvTransaction> addTagsToTransactionsWithAllMatchers(Set<CsvTransaction> transactionSet) {
        Set<CsvTransactionMatcher> matchers = csvTransactionMatcherRepository.getAllCsvTransactionMatchersEagerFetches();
        return addTagsToTransactionsWithMatchers(transactionSet, matchers);
    }

    public Set<CsvTransaction> addTagsToTransactionsWithOneMatcher(Set<CsvTransaction> transactionSet, CsvTransactionMatcher matcher) {
        Set<CsvTransactionMatcher> matchers = new HashSet<>();
        matchers.add(matcher);
        return addTagsToTransactionsWithMatchers(transactionSet, matchers);
    }

    private Set<CsvTransaction> addTagsToTransactionsWithMatchers(Set<CsvTransaction> transactionSet, Set<CsvTransactionMatcher> matchers) {
        CsvTransactionTag uncategorizedTag = csvTransactionTagManager.getTagFromCache(CommonConstants.UNCATEGORIZED);

        for(CsvTransaction transaction : transactionSet) {
            addTagsToTransactionWithMatchers(transaction, matchers);
            if(transaction.getTags().isEmpty()) {
                transaction.addTag(uncategorizedTag);
            }
        }

        return transactionSet;
    }

    private void addTagsToTransactionWithMatchers(CsvTransaction transaction, Set<CsvTransactionMatcher> matchers) {
        for (CsvTransactionMatcher matcher : matchers) {
            if (transaction.getDescription().toUpperCase().contains(matcher.getMatchString().toUpperCase())) {
                if (matcher.getCsvTransactionTagGroup() != null) {
                    transaction.addTags(matcher.getCsvTransactionTagGroup().getTags());
                }
                if (matcher.getCsvTransactionTag() != null) {
                    transaction.addTag(matcher.getCsvTransactionTag());
                }
            }
        }
    }
}
