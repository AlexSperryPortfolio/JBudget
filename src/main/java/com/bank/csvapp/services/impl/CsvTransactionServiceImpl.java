package com.bank.csvapp.services.impl;

import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.repositories.CsvTransactionRepository;
import com.bank.csvapp.services.CsvTransactionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CsvTransactionServiceImpl implements CsvTransactionService{

	private CsvTransactionRepository csvTransactionRepository;

	public CsvTransactionServiceImpl(CsvTransactionRepository csvTransactionRepository) {
		this.csvTransactionRepository = csvTransactionRepository;
	}

	@Override
	public List<CsvTransaction> listAllCsvTransactions() {
		return (List<CsvTransaction>)csvTransactionRepository.findAll();
	}

	@Override
	public CsvTransaction getCsvTransactionById(Integer id) {
		Optional<CsvTransaction> csvTransactionOptional = csvTransactionRepository.findById(id);
		return csvTransactionOptional.orElse(null);
	}

	public Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate) {
		return csvTransactionRepository.getAllTransactionsInRange(startDate, endDate);
	}

	public Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Integer> csvTagIds) {
		return csvTransactionRepository.getAllTransactionsInRangeAndWithTags(startDate, endDate, csvTagIds);
	}

	@Override
	public CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction) {
		return csvTransactionRepository.save(csvTransaction);
	}

	@Override
	public List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionIterable) {
		return (List<CsvTransaction>) csvTransactionRepository.saveAll(csvTransactionIterable);
	}

	@Override
	public void deleteCsvTransaction(CsvTransaction csvTransaction) {
		csvTransactionRepository.delete(csvTransaction);
	}

}
