package com.bank.jbudget.services.impl;

import com.bank.jbudget.domain.CsvTransaction;
import com.bank.jbudget.repositories.CsvTransactionRepository;
import com.bank.jbudget.services.CsvTransactionService;
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
	public CsvTransaction getCsvTransactionById(Long id) {
		Optional<CsvTransaction> csvTransactionOptional = csvTransactionRepository.findById(id);
		return csvTransactionOptional.orElse(null);
	}

	public Set<CsvTransaction> getAllTransactionsInRange(Date startDate, Date endDate) {
		return csvTransactionRepository.getAllTransactionsInRange(startDate, endDate);
	}

	public Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(Date startDate, Date endDate, Set<Long> csvTagIds) {
		return csvTransactionRepository.getAllTransactionsInRangeAndWithTags(startDate, endDate, csvTagIds);
	}

	@Override
	public CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction) {
		return csvTransactionRepository.save(csvTransaction);
	}

	@Override
	public List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionList) {
		return (List<CsvTransaction>) csvTransactionRepository.saveAll(csvTransactionList);
	}

	@Override
	public void deleteCsvTransaction(CsvTransaction csvTransaction) {
		csvTransactionRepository.delete(csvTransaction);
	}

}
