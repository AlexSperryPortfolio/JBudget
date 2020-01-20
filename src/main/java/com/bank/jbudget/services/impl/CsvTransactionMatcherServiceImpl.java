package com.bank.jbudget.services.impl;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionMatcher;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.domain.CsvTransactionTagGroup;
import com.bank.jbudget.repositories.CsvTransactionMatcherRepository;
import com.bank.jbudget.repositories.CsvTransactionTagGroupRepository;
import com.bank.jbudget.repositories.CsvTransactionTagRepository;
import com.bank.jbudget.services.CsvTransactionMatcherService;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.utility.TagApplicationManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CsvTransactionMatcherServiceImpl implements CsvTransactionMatcherService {

    private CsvTransactionMatcherRepository csvTransactionMatcherRepository;
    private CsvTransactionTagGroupRepository csvTransactionTagGroupRepository;
    private CsvTransactionTagRepository csvTransactionTagRepository;
    private CsvTransactionService csvTransactionService;
    private TagApplicationManager tagApplicationManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    public CsvTransactionMatcherServiceImpl(
            CsvTransactionMatcherRepository csvTransactionMatcherRepository,
            CsvTransactionTagGroupRepository csvTransactionTagGroupRepository,
            CsvTransactionTagRepository csvTransactionTagRepository,
            CsvTransactionService csvTransactionService,
            TagApplicationManager tagApplicationManager) {
        this.csvTransactionMatcherRepository = csvTransactionMatcherRepository;
        this.csvTransactionTagGroupRepository = csvTransactionTagGroupRepository;
        this.csvTransactionTagRepository = csvTransactionTagRepository;
        this.csvTransactionService = csvTransactionService;
        this.tagApplicationManager = tagApplicationManager;
    }


    //region CRUD Methods
    @Override
    public CsvTransactionMatcher getCsvTransactionMatcherById(long id) {
        Optional<CsvTransactionMatcher> csvTransactionMatcherOptional = csvTransactionMatcherRepository.findById(id);
        return csvTransactionMatcherOptional.orElse(null);
    }

    @Override
    public List<CsvTransactionMatcher> getAllCsvTransactionMatchers() {
        return (List<CsvTransactionMatcher>) csvTransactionMatcherRepository.findAll();
    }

    @Override
    public CsvTransactionMatcher saveCsvTransactionMatcher(CsvTransactionMatcher csvTransactionMatcher) {
        return csvTransactionMatcherRepository.save(csvTransactionMatcher);
    }

    @Override
    public Iterable<CsvTransactionMatcher> saveCsvTransactionMatchers(Collection<CsvTransactionMatcher> csvTransactionMatcherList) {
        return csvTransactionMatcherRepository.saveAll(csvTransactionMatcherList);
    }

    @Override
    public void deleteCsvTransactionMatcher(CsvTransactionMatcher csvTransactionMatcher) {
        csvTransactionMatcherRepository.delete(csvTransactionMatcher);
    }
    //endregion

    //region Helper Methods
    @Override
    public Set<CsvTransactionMatcher> getAllCsvTransactionMatchersEagerFetches() {
        return csvTransactionMatcherRepository.getAllCsvTransactionMatchersEagerFetches();
    }

    @Override
    public Set<CsvTransactionMatcher> getAllCsvTransactionMatchersUsingGroupId(long id) {
        return csvTransactionMatcherRepository.getAllCsvTransactionMatchersUsingGroupId(id);
    }

    @Override
    public JsonNode createMatcher(JsonNode requestNode) throws JsonProcessingException {
        ObjectNode responseNode = objectMapper.createObjectNode();

        CsvTransactionMatcher requestMatcher = objectMapper.treeToValue(requestNode, CsvTransactionMatcher.class);

        //requestMatcher must be new and its csvTransactionTag must be present and new
        boolean noIdPresent = requestMatcher.getId() == null;
        boolean requestMatcherTagIsPresentAndIsNew = requestMatcher.getCsvTransactionTag() != null && requestMatcher.getCsvTransactionTag().getId() == null;
        boolean requestMatcherGroupIsPresentAndIsntNew = requestMatcher.getCsvTransactionTagGroup() != null && requestMatcher.getCsvTransactionTagGroup().getId() != null;
        if(noIdPresent && requestMatcherTagIsPresentAndIsNew && requestMatcherGroupIsPresentAndIsntNew) {
            //uppercase match string in case lower case characters present
            CsvTransactionMatcher matcherToCreate = new CsvTransactionMatcher(requestMatcher.getName(), requestMatcher.getMatchString());
            matcherToCreate.setCsvTransactionTag(requestMatcher.getCsvTransactionTag());
            CsvTransactionTagGroup group = csvTransactionTagGroupRepository.getCsvTransactionTagGroupWithTagsById(requestMatcher.getCsvTransactionTagGroup().getId());
            matcherToCreate.setCsvTransactionTagGroup(group);
            CsvTransactionMatcher savedMatcher = csvTransactionMatcherRepository.save(matcherToCreate);

            //retroactively apply new matcher's tags (own and group) to all matching transactions
            Set<CsvTransaction> csvTransactions = csvTransactionService.getAllMatchingTransactions(requestMatcher.getMatchString());
            Set<CsvTransaction> csvTransactionsWithTags = tagApplicationManager.addTagsToTransactionsWithOneMatcher(csvTransactions, savedMatcher);

            //delete uncategorized tag if present
            csvTransactionsWithTags.parallelStream().filter(x -> x.getTags().parallelStream().anyMatch(y -> y.getName().equals(CommonConstants.UNCATEGORIZED))).forEach(z -> z.removeTag(new CsvTransactionTag(CommonConstants.UNCATEGORIZED)));

            //save updated transactions
            Set<CsvTransaction> savedCsvTransactions = StreamSupport.stream(csvTransactionService.saveCsvTransactions(csvTransactionsWithTags).spliterator(), true).collect(Collectors.toSet());

            JsonNode createdMatcherNode = objectMapper.valueToTree(savedMatcher);
            responseNode.set("createdMatcher", createdMatcherNode);
            responseNode.put("numAffectedTransactions", savedCsvTransactions.size());
        } else {
            StringBuilder message = new StringBuilder("ERROR: ");
            if(!noIdPresent) {
                message.append("Matcher to be created cannot contain an id. ");
            }
            if(!requestMatcherTagIsPresentAndIsNew) {
                message.append("Matcher must have a Tag and it cannot contain an id. ");
            }
            if(!requestMatcherGroupIsPresentAndIsntNew) {
                message.append("Matcher must have a Group and it must contain an id.");
            }

            responseNode.put(CommonConstants.MESSAGE, message.toString());
        }
        return responseNode;
    }

    @Override
    public JsonNode updateMatcher(Long id, JsonNode requestNode) throws JsonProcessingException {
        ObjectNode responseNode = objectMapper.createObjectNode();
        CsvTransactionMatcher requestMatcher = objectMapper.treeToValue(requestNode, CsvTransactionMatcher.class);

        boolean noIdPresent = id == null;
        boolean requestMatcherTagIsPresentAndIsntNew = requestMatcher.getCsvTransactionTag() != null && requestMatcher.getCsvTransactionTag().getId() != null;
        boolean requestMatcherGroupIsPresentAndIsntNew = requestMatcher.getCsvTransactionTagGroup() != null && requestMatcher.getCsvTransactionTagGroup().getId() != null;
        if(!noIdPresent && requestMatcherTagIsPresentAndIsntNew && requestMatcherGroupIsPresentAndIsntNew) {
            CsvTransactionMatcher matcherFromDatabase = csvTransactionMatcherRepository.getCsvTransactionMatcherByIdEagerFetches(requestMatcher.getId());
            if(!matcherFromDatabase.getName().equals(requestMatcher.getName())) {
                matcherFromDatabase.setName(requestMatcher.getName());
            }
            if(!matcherFromDatabase.getMatchString().equals(requestMatcher.getMatchString())) {
                matcherFromDatabase.setMatchString(requestMatcher.getMatchString());
            }

            Set<CsvTransaction> transactions = new HashSet<>();
            Set<CsvTransaction> updatedTransactions = new HashSet<>();

            //determine if self matcher changed
            if(!matcherFromDatabase.getCsvTransactionTag().getId().equals(requestMatcher.getCsvTransactionTag().getId())) {
                CsvTransactionTag oldSelfTag = csvTransactionTagRepository.findById(matcherFromDatabase.getCsvTransactionTag().getId()).orElse(null);
                matcherFromDatabase.setCsvTransactionTag(csvTransactionTagRepository.findById(requestMatcher.getCsvTransactionTag().getId()).orElse(null));

                //update transactions
                transactions.addAll(csvTransactionService.getAllMatchingTransactions(matcherFromDatabase.getMatchString()));
                for(CsvTransaction transaction : transactions) {
                    transaction.addTag(matcherFromDatabase.getCsvTransactionTag());
                    transaction.removeTag(oldSelfTag);
                }

                //delete old self tag
                csvTransactionTagRepository.delete(oldSelfTag);
            }

            //determine if group changed
            if(!matcherFromDatabase.getCsvTransactionTagGroup().getId().equals(requestMatcher.getCsvTransactionTagGroup().getId())) {
                Set<CsvTransactionTag> oldGroupTags = matcherFromDatabase.getCsvTransactionTagGroup().getTags();
                matcherFromDatabase.setCsvTransactionTagGroup(csvTransactionTagGroupRepository.getCsvTransactionTagGroupWithTagsById(requestMatcher.getCsvTransactionTagGroup().getId()));

                //update transactions
                if(transactions.isEmpty()) { //check if transactions to update was loaded previously, load if not
                    transactions.addAll(csvTransactionService.getAllMatchingTransactions(matcherFromDatabase.getMatchString()));
                }
                for(CsvTransaction transaction : transactions) {
                    transaction.addTags(matcherFromDatabase.getCsvTransactionTagGroup().getTags());
                    transaction.removeTags(oldGroupTags);
                }

            }

            //save any transactions that were updated
            if(!transactions.isEmpty()) {
                updatedTransactions = StreamSupport.stream(csvTransactionService.saveCsvTransactions(transactions).spliterator(), true).collect(Collectors.toSet());
            }

            CsvTransactionMatcher updatedMatcher = csvTransactionMatcherRepository.save(matcherFromDatabase);
            JsonNode updatedMatcherNode = objectMapper.valueToTree(updatedMatcher);
            responseNode.set("updatedMatcher", updatedMatcherNode);
            responseNode.put("numAffectedTransactions", updatedTransactions.size());
        } else {
            StringBuilder message = new StringBuilder("ERROR: ");
            if(noIdPresent) {
                message.append("Matcher to be updated must contain an id. ");
            }
            if(!requestMatcherTagIsPresentAndIsntNew) {
                message.append("Matcher must have a Tag and it must contain an id. ");
            }
            if(!requestMatcherGroupIsPresentAndIsntNew) {
                message.append("Matcher must have a Group and it must contain an id.");
            }

            responseNode.put(CommonConstants.MESSAGE, message.toString());
        }
        return responseNode;
    }

    @Override
    public JsonNode deleteMatcher(Long id) {
        ObjectNode responseNode = objectMapper.createObjectNode();

        //requestMatcher must have an id
        if(id != null) {
            CsvTransactionMatcher matcherFromDatabase = csvTransactionMatcherRepository.getCsvTransactionMatcherByIdEagerFetches(id);

            //retroactively remove matcher's tags (own and group) from all matching transactions
            Set<CsvTransaction> transactions = csvTransactionService.getAllMatchingTransactions(matcherFromDatabase.getMatchString());
            for(CsvTransaction transaction : transactions) {
                transaction.removeTags(matcherFromDatabase.getCsvTransactionTagGroup().getTags());
                transaction.removeTag(matcherFromDatabase.getCsvTransactionTag());
            }

            Set<CsvTransaction> savedCsvTransactions = StreamSupport.stream(csvTransactionService.saveCsvTransactions(transactions).spliterator(), true).collect(Collectors.toSet());
            csvTransactionMatcherRepository.delete(matcherFromDatabase);

            JsonNode createdMatcherNode = objectMapper.valueToTree(matcherFromDatabase);
            responseNode.set("deletedMatcher", createdMatcherNode);
            responseNode.put("numAffectedTransactions", savedCsvTransactions.size());
        } else {
            responseNode.put(CommonConstants.MESSAGE, "ERROR: path id must be present for Matcher to be deleted");
        }
        return responseNode;
    }
    //endregion

}