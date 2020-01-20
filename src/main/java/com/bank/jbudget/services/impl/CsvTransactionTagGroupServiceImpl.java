package com.bank.jbudget.services.impl;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionMatcher;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.domain.CsvTransactionTagGroup;
import com.bank.jbudget.repositories.CsvTransactionMatcherRepository;
import com.bank.jbudget.repositories.CsvTransactionTagGroupRepository;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.services.CsvTransactionTagGroupService;
import com.bank.jbudget.services.CsvTransactionTagService;
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
public class CsvTransactionTagGroupServiceImpl implements CsvTransactionTagGroupService {

    private CsvTransactionTagGroupRepository csvTransactionTagGroupRepository;
    private CsvTransactionMatcherRepository csvTransactionMatcherRepository;
    private CsvTransactionTagService csvTransactionTagService;
    private CsvTransactionService csvTransactionService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public CsvTransactionTagGroupServiceImpl(
            CsvTransactionTagGroupRepository csvTransactionTagGroupRepository,
            CsvTransactionMatcherRepository csvTransactionMatcherRepository,
            CsvTransactionTagService csvTransactionTagService,
            CsvTransactionService csvTransactionService) {
        this.csvTransactionTagGroupRepository = csvTransactionTagGroupRepository;
        this.csvTransactionMatcherRepository = csvTransactionMatcherRepository;
        this.csvTransactionTagService = csvTransactionTagService;
        this.csvTransactionService = csvTransactionService;
    }

    @Override
    public CsvTransactionTagGroup getCsvTransactionTagGroupById(long id) {
        Optional<CsvTransactionTagGroup> csvTransactionTagGroupOptional = csvTransactionTagGroupRepository.findById(id);
        return csvTransactionTagGroupOptional.orElse(null);
    }

    @Override
    public List<CsvTransactionTagGroup> getAllCsvTransactionTagGroups() {
        return (List<CsvTransactionTagGroup>) csvTransactionTagGroupRepository.findAll();
    }

    @Override
    public CsvTransactionTagGroup saveCsvTransactionTagGroup(CsvTransactionTagGroup csvTransactionTagGroup) {
        return csvTransactionTagGroupRepository.save(csvTransactionTagGroup);
    }

    @Override
    public Iterable<CsvTransactionTagGroup> saveCsvTransactionTagGroups(Collection<CsvTransactionTagGroup> csvTransactionTagGroupList) {
        return csvTransactionTagGroupRepository.saveAll(csvTransactionTagGroupList);
    }

    @Override
    public void deleteCsvTransactionTagGroup(CsvTransactionTagGroup csvTransactionTagGroup) {
        csvTransactionTagGroupRepository.delete(csvTransactionTagGroup);
    }

    //region Helper Methods
    @Override
    public JsonNode createGroup(JsonNode requestNode) throws JsonProcessingException {
        ObjectNode responseNode = objectMapper.createObjectNode();
        CsvTransactionTagGroup csvTransactionTagGroup = objectMapper.treeToValue(requestNode, CsvTransactionTagGroup.class);

        if(csvTransactionTagGroup.getId() == null) {
            CsvTransactionTagGroup group = new CsvTransactionTagGroup();
            group.setName(csvTransactionTagGroup.getName());
            Long[] existingTagIds = csvTransactionTagGroup.getTags().parallelStream().filter(x -> x.getId() != null)
                    .map(CsvTransactionTag::getId).toArray(Long[]::new);
            //add new tags
            group.addTags(csvTransactionTagGroup.getTags().parallelStream().filter(x -> x.getId() == null).collect(Collectors.toSet()));
            //add existing tags
            group.addTags(csvTransactionTagService.getAllTagsByIds(existingTagIds));
            CsvTransactionTagGroup savedTransactionTagGroup = csvTransactionTagGroupRepository.save(group);
            ObjectNode savedTransactionTagNode = objectMapper.valueToTree(savedTransactionTagGroup); //change to savedTransactionTagGroup
            responseNode.set("createdTagGroup", savedTransactionTagNode);
        } else {
            responseNode.put(CommonConstants.MESSAGE, "ERROR: entity to be created cannot contain an id");
        }
        return responseNode;
    }

    @Override
    public JsonNode updateGroup(Long id, JsonNode requestNode) throws JsonProcessingException {
        ObjectNode responseNode = objectMapper.createObjectNode();
        CsvTransactionTagGroup csvTransactionTagGroup = objectMapper.treeToValue(requestNode, CsvTransactionTagGroup.class);

        if(csvTransactionTagGroup.getId() != null) {
            //update group tags
            CsvTransactionTagGroup tagGroupFromDatabase = csvTransactionTagGroupRepository.getCsvTransactionTagGroupWithTagsById(id);
            if(!csvTransactionTagGroup.getName().equals(tagGroupFromDatabase.getName())) {
                tagGroupFromDatabase.setName(csvTransactionTagGroup.getName());
            }
            Set<CsvTransactionTag> addedTags = getAddedEntities(tagGroupFromDatabase.getTags(), csvTransactionTagGroup.getTags());
            Set<CsvTransactionTag> removedTags = getRemovedEntities(tagGroupFromDatabase.getTags(), csvTransactionTagGroup.getTags());
            tagGroupFromDatabase.addTags(addedTags);
            tagGroupFromDatabase.removeTags(removedTags);
            CsvTransactionTagGroup savedTransactionTagGroup = csvTransactionTagGroupRepository.save(tagGroupFromDatabase);

            //update transactions with matchers with this group
            Set<CsvTransactionMatcher> matchers = csvTransactionMatcherRepository.getAllCsvTransactionMatchersUsingGroupId(tagGroupFromDatabase.getId());
            Set<CsvTransaction> transactions = new HashSet<>();
            for(CsvTransactionMatcher matcher : matchers) {
                transactions.addAll(csvTransactionService.getAllMatchingTransactions(matcher.getMatchString()));
            }
            for(CsvTransaction transaction : transactions) {
                transaction.addTags(addedTags);
                transaction.removeTags(removedTags);
            }
            csvTransactionService.saveCsvTransactions(transactions);

            ObjectNode savedTransactionTagNode = objectMapper.valueToTree(savedTransactionTagGroup); //change to savedTransactionTagGroup
            responseNode.set("updatedTagGroup", savedTransactionTagNode);
        } else {
            responseNode.put(CommonConstants.MESSAGE, "ERROR: entity to be updated must contain an id");
        }
        return responseNode;
    }

    @Override
    public JsonNode deleteGroup(Long id) {
        ObjectNode responseNode = objectMapper.createObjectNode();

        if(id != null) {
            //update group tags
            CsvTransactionTagGroup tagGroupFromDatabase = csvTransactionTagGroupRepository.getCsvTransactionTagGroupWithTagsById(id);

            //update transactions with matchers with this group
            Set<CsvTransactionMatcher> matchers = csvTransactionMatcherRepository.getAllCsvTransactionMatchersUsingGroupId(tagGroupFromDatabase.getId());
            Set<CsvTransaction> transactions = new HashSet<>();
            for(CsvTransactionMatcher matcher : matchers) {
                transactions.addAll(csvTransactionService.getAllMatchingTransactions(matcher.getMatchString()));
            }
            for(CsvTransaction transaction : transactions) {
                transaction.removeTags(tagGroupFromDatabase.getTags());
            }
            Set<CsvTransaction> savedTransactions = StreamSupport.stream(csvTransactionService.saveCsvTransactions(transactions).spliterator(), true).collect(Collectors.toSet());
            csvTransactionTagGroupRepository.delete(tagGroupFromDatabase);

            responseNode.put("deletedTransactionGroupId", tagGroupFromDatabase.getId());
            responseNode.put("numTransactionsAffected", savedTransactions.size());
        } else {
            responseNode.put(CommonConstants.MESSAGE, "ERROR: entity to be deleted must contain an id");
        }
        return responseNode;
    }

    public <T> Set<T> getAddedEntities(final Collection<T> beforeCollection, final Collection<T> afterCollection) {
        return afterCollection.parallelStream().filter(x -> !beforeCollection.contains(x)).collect(Collectors.toSet());
    }

    public <T> Set<T> getRemovedEntities(final Collection<T> beforeCollection, final Collection<T> afterCollection) {
        return beforeCollection.parallelStream().filter(x -> !afterCollection.contains(x)).collect(Collectors.toSet());
    }
    //endregion
}