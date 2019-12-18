package com.bank.jbudget.services.impl;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.repositories.CsvTransactionRepository;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.utility.CsvTransactionTagManager;
import com.bank.jbudget.utility.FileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CsvTransactionServiceImpl implements CsvTransactionService {

	private CsvTransactionRepository csvTransactionRepository;
	private CsvTransactionTagManager csvTransactionTagManager;

	public CsvTransactionServiceImpl(CsvTransactionRepository csvTransactionRepository, CsvTransactionTagManager csvTransactionTagManager) {
		this.csvTransactionRepository = csvTransactionRepository;
		this.csvTransactionTagManager = csvTransactionTagManager;
	}

	//region crud operations
	@Override
	public List<CsvTransaction> listAllCsvTransactions() {
		return (List<CsvTransaction>)csvTransactionRepository.findAll();
	}

	@Override
	public CsvTransaction getCsvTransactionById(Long id) {
		Optional<CsvTransaction> csvTransactionOptional = csvTransactionRepository.findById(id);
		return csvTransactionOptional.orElse(null);
	}

	@Override
	public Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate) {
		return csvTransactionRepository.getAllTransactionsInRange(startDate, endDate);
	}

	@Override
	public Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Long> csvTagIds) {
		return csvTransactionRepository.getAllTransactionsInRangeAndWithTags(startDate, endDate, csvTagIds);
	}

	@Override
	public long getUncategorizedTransactionsCount(Long uncategorizedTagId) {
		return csvTransactionRepository.getUncategorizedTransactionsCount(uncategorizedTagId);
	}

	@Override
	public long[] getUncategorizedTransactionIdsPaginated(Long uncategorizedTagId, int limit, int offset) {
		return csvTransactionRepository.getUncategorizedTransactionIdsPaginated(uncategorizedTagId, limit, offset);
	}

	@Override
	public Set<CsvTransaction> getUncategorizedTransactionsById(long[] uncategorizedTransactionIds) {
		return csvTransactionRepository.getUncategorizedTransactionsById(uncategorizedTransactionIds);
	}

	@Override
	public CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction) {
		return csvTransactionRepository.save(csvTransaction);
	}

	@Override
	public Set<CsvTransaction> getAllMatchingTransactions(String matchString) {
		return csvTransactionRepository.getAllMatchingTransactions("%" + matchString + "%");
	}

	@Override
	public List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionList) {
		return (List<CsvTransaction>) csvTransactionRepository.saveAll(csvTransactionList);
	}

	@Override
	public void deleteCsvTransaction(CsvTransaction csvTransaction) {
		csvTransactionRepository.delete(csvTransaction);
	}
	//endregion

	//region helper methods
	@Override
	public JsonNode dateRange(JsonNode requestBody) throws IOException {
		JsonNode responseNode;
		ObjectMapper objectMapper = new ObjectMapper();

		//get fields from body
		JsonNode startDateNode = requestBody.findValue(CommonConstants.START_DATE);
		JsonNode endDateNode = requestBody.findValue(CommonConstants.END_DATE);
		JsonNode filterTagArrayNode = requestBody.findValue(CommonConstants.FILTER_TAGS);

		if(startDateNode != null && endDateNode != null) {
			Date startDate = new Date(startDateNode.asLong());
			Date endDate = new Date(endDateNode.asLong());

			List<String> filterTags = new ArrayList<>();
			if (filterTagArrayNode != null) {//get list of strings from filterTags json string
				CollectionType stringType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
				filterTags = objectMapper.readValue(filterTagArrayNode.toString(), stringType);
			}

			Set<CsvTransaction> matchingTransactions;
			if (filterTags.isEmpty()) {
				matchingTransactions = getAllTransactionsInRange(startDate, endDate); //paginate this
			} else {
				System.out.println(Arrays.toString(filterTags.toArray())); //todo make this a log
				Set<Long> csvTagIdSet = filterTags.parallelStream()
						.map(x -> csvTransactionTagManager.getTagFromCache(x))
						.filter(Objects::nonNull)
						.map(CsvTransactionTag::getId)
						.collect(Collectors.toSet());
				if (!csvTagIdSet.isEmpty()) {
					matchingTransactions = getAllTransactionsInRangeAndWithTags(startDate, endDate, csvTagIdSet);
				} else {
					System.out.println("No tags match"); //todo make this a log
					matchingTransactions = new HashSet<>();
				}
			}

			responseNode = objectMapper.valueToTree(matchingTransactions);
		} else {
			responseNode = objectMapper.createObjectNode();
			((ObjectNode) responseNode).put(CommonConstants.MESSAGE,"ERROR: no startDate and/or endDate");
		}
		return responseNode;
	}

	@Override
	public JsonNode getUncategorizedTransactions(int page, int size) {
		ObjectMapper objectMapper = new ObjectMapper();
		Long uncategorizedTagId = csvTransactionTagManager.getTagFromCache(CommonConstants.UNCATEGORIZED).getId();

		long numUncategorizedTransactions = getUncategorizedTransactionsCount(uncategorizedTagId);
		long numPages = numUncategorizedTransactions/size + (numUncategorizedTransactions % size == 0 ? 0 : 1);
		long[] uncategorizedTransactionIds = getUncategorizedTransactionIdsPaginated(uncategorizedTagId, size, size * page);
		Set<CsvTransaction> uncategorizedTransactions = getUncategorizedTransactionsById(uncategorizedTransactionIds);

		JsonNode uncategorizedTransactionsArray = objectMapper.valueToTree(uncategorizedTransactions);
		ObjectNode retNode = objectMapper.createObjectNode();
		retNode.set("uncategorizedTransactions", uncategorizedTransactionsArray);
		retNode.put("totalPages", numPages);
		return retNode;
	}

	@Override
	public JsonNode applyTagsToTransactions(JsonNode requestBody) throws JsonProcessingException {
		long numTransactionsAffected = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode responseNode = objectMapper.createObjectNode();

		JsonNode transactionTagsNode = requestBody.findValue("transactionTags");
		JsonNode transactionIdsNode = requestBody.findValue("transactionIds");

		CsvTransactionTag[] transactionTagsArray;
		long[] transactionIds;
		if(transactionTagsNode != null && transactionIdsNode != null) {
			transactionTagsArray = objectMapper.treeToValue(transactionTagsNode, CsvTransactionTag[].class);
			transactionIds = objectMapper.treeToValue(transactionIdsNode, long[].class);
			final List<CsvTransactionTag> transactionTags = Arrays.stream(transactionTagsArray).collect(Collectors.toList());
			CsvTransactionTag uncategorizedTag = csvTransactionTagManager.getTagFromCache(CommonConstants.UNCATEGORIZED);

			if(transactionIds.length == 1) { //apply many tags to one uncategorized transaction case
				transactionTags.clear(); //clear transaction tags and load from cache (apply existing tags don't create a new one case)
				Arrays.stream(transactionTagsArray).forEach(x -> transactionTags.add(csvTransactionTagManager.getTagFromCache(x.getTypeName())));

				CsvTransaction transaction = getCsvTransactionById(transactionIds[0]);
				transaction.addTags(transactionTags);
				transaction.removeTag(uncategorizedTag);

				//todo: replace below after actually calling db
				numTransactionsAffected = 1;
//                csvTransactionService.saveCsvTransaction(transaction);
			} else if(transactionTags.size() == 1) { //apply one tag to all matching transactions case
				Set<CsvTransaction> matchingTransactions = getAllMatchingTransactions(transactionTags.get(0).getMatchString());
				matchingTransactions.parallelStream().forEach(x -> x.addTag(transactionTags.get(0)));
				matchingTransactions.parallelStream().forEach(x -> x.removeTag(uncategorizedTag));

				//todo: replace below after actually calling db
				numTransactionsAffected = matchingTransactions.size();
				responseNode.put("createdTag", transactionTags.get(0).getTypeName());
//                csvTransactionService.saveCsvTransactionList(matchingTransactions.parallelStream().collect(Collectors.toList()));
			} else {
				System.out.println("ERROR: only one tag can be used if no transactionId present");
				responseNode.put(CommonConstants.MESSAGE,"only one tag can be used if no transactionId present");
			}
		} else { //request body error
			System.out.println("ERROR: no transactionTags and/or transactionIds");
			responseNode.put(CommonConstants.MESSAGE,"request body requires transactionTags attribute and transactionTags attribute");
		}

		//todo: replace this dummy response with meaningful one based on response from db
		responseNode.put("numTransactionsAffected", numTransactionsAffected);
		return responseNode;
	}

	//TODO: remove this when using a permanent database
	public void warmUpCacheCuzInMemDb() {
		//warm up cache
		if(CsvTransactionTagManager.csvTransactionTagCache.size() == 0) {
			FileDto fileDto = new FileDto(csvTransactionTagManager);
			List<CsvTransaction> transactionList = fileDto.csvFileImport();
			saveCsvTransactionList(transactionList);
		}
		//update cache with db objects
		csvTransactionTagManager.resetCsvTransactionTagCacheWithDbTags();
	}

	//endregion

}
