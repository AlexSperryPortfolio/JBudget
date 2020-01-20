package com.bank.jbudget.services.impl;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.repositories.CsvTransactionRepository;
import com.bank.jbudget.services.CsvTransactionService;
import com.bank.jbudget.utility.CsvTransactionTagManager;
import com.bank.jbudget.utility.FileImportManager;
import com.bank.jbudget.utility.TagApplicationManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CsvTransactionServiceImpl implements CsvTransactionService {

	private static final Logger logger = Logger.getLogger(CsvTransactionServiceImpl.class);

	private CsvTransactionRepository csvTransactionRepository;
	private CsvTransactionTagManager csvTransactionTagManager;
	private TagApplicationManager tagApplicationManager;
	private ObjectMapper objectMapper = new ObjectMapper();

	public CsvTransactionServiceImpl(CsvTransactionRepository csvTransactionRepository, CsvTransactionTagManager csvTransactionTagManager, TagApplicationManager tagApplicationManager) {
		this.csvTransactionRepository = csvTransactionRepository;
		this.csvTransactionTagManager = csvTransactionTagManager;
		this.tagApplicationManager = tagApplicationManager;
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
	public Set<CsvTransaction> getAllMatchingTransactions(String matchString) {
		return csvTransactionRepository.getAllMatchingTransactions("%" + matchString + "%");
	}

	@Override
	public CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction) {
		return csvTransactionRepository.save(csvTransaction);
	}

	@Override
	public Iterable<CsvTransaction> saveCsvTransactions(Collection<CsvTransaction> csvTransactionList) {
		return csvTransactionRepository.saveAll(csvTransactionList);
	}

	@Override
	public void deleteCsvTransaction(CsvTransaction csvTransaction) {
		csvTransactionRepository.delete(csvTransaction);
	}
	//endregion

	//region helper methods
	@Override
	public JsonNode dateRange(JsonNode requestBody) throws IOException {
		ObjectNode responseNode;

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
				matchingTransactions = csvTransactionRepository.getAllTransactionsInRange(startDate, endDate); //paginate this
			} else {
				Set<Long> csvTagIdSet = filterTags.parallelStream()
						.map(x -> csvTransactionTagManager.getTagFromCache(x))
						.filter(Objects::nonNull)
						.map(CsvTransactionTag::getId)
						.collect(Collectors.toSet());
				if (!csvTagIdSet.isEmpty()) {
					matchingTransactions = csvTransactionRepository.getAllTransactionsInRangeAndWithTags(startDate, endDate, csvTagIdSet);
				} else {
					logger.info("No tags match");
					matchingTransactions = new HashSet<>();
				}
			}

			responseNode = objectMapper.valueToTree(matchingTransactions);
		} else {
			responseNode = objectMapper.createObjectNode();
			responseNode.put(CommonConstants.MESSAGE,"ERROR: no startDate and/or endDate");
		}
		return responseNode;
	}

	@Override
	public JsonNode getUncategorizedTransactions(int page, int size) {
		ObjectNode retNode = objectMapper.createObjectNode();
		Long uncategorizedTagId = csvTransactionTagManager.getTagFromCache(CommonConstants.UNCATEGORIZED).getId();

		long numUncategorizedTransactions = csvTransactionRepository.getUncategorizedTransactionsCount(uncategorizedTagId);
		long numPages = numUncategorizedTransactions/size + (numUncategorizedTransactions % size == 0 ? 0 : 1);
		long[] uncategorizedTransactionIds = csvTransactionRepository.getUncategorizedTransactionIdsPaginated(uncategorizedTagId, size, size * page);
		if(uncategorizedTransactionIds.length > 0) {
			Set<CsvTransaction> uncategorizedTransactions = csvTransactionRepository.getUncategorizedTransactionsById(uncategorizedTransactionIds);

			JsonNode uncategorizedTransactionsArray = objectMapper.valueToTree(uncategorizedTransactions);
			retNode.set("uncategorizedTransactions", uncategorizedTransactionsArray);
			retNode.put("totalPages", numPages);
		} else {
			retNode.put(CommonConstants.MESSAGE, "ERROR: Page number out of Range");
		}
		return retNode;
	}

	@Override
	public JsonNode importCsvFile(MultipartFile accountHistoryFile) throws IOException {
		ObjectNode responseNode = objectMapper.createObjectNode();
		InputStream inputStream = new ByteArrayInputStream(accountHistoryFile.getBytes());
		Set<CsvTransaction> transactions = FileImportManager.csvFileImport(inputStream);

		Set<CsvTransaction> transactionsWithTags = tagApplicationManager.addTagsToTransactionsWithAllMatchers(transactions);
		Set<CsvTransaction> savedTransactions = StreamSupport.stream(csvTransactionRepository.saveAll(transactionsWithTags).spliterator(), true).collect(Collectors.toSet());

		responseNode.put("numTransactions", savedTransactions.size());
		return responseNode;
	}
	//endregion

//	TODO: remove this when using a permanent database
//	public List<CsvTransaction> warmUpCacheCuzInMemDb() {
//		//warm up tag cache
//		csvTransactionTagManager.resetCsvTransactionTagCacheWithDbTags();
//		InputStream accountHistoryStream = JBudget.class.getResourceAsStream("/accountHistory.csv");
//		Set<CsvTransaction> transactions = fileDto.csvFileImport(accountHistoryStream);
//		Set<CsvTransaction> transactionsWithTags = csvTransactionImportManager.addTagsToTransactionsWithAllMatchers(transactions, csvTransactionTagManager.getTagFromCache(CommonConstants.UNCATEGORIZED));
//		transactionsWithTagsList = saveCsvTransactionList(transactionsWithTags);
//		return transactionsWithTagsList;
//	}

	//endregion

}
