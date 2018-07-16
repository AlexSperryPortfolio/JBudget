package com.bank.csvapp.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.repositories.CsvTransactionRepository;
import com.bank.csvapp.domain.services.CsvTransactionService;

@Service
public class CsvTransactionServiceImpl implements CsvTransactionService{
	
	@Autowired
	private CsvTransactionRepository csvTransactionRepository;

	@Override
	public Iterable<CsvTransaction> listAllCsvTransactions() {
		return csvTransactionRepository.findAll();
	}

	@Override
	public CsvTransaction getCsvTransactionById(Integer id) {
		return csvTransactionRepository.findOne(id);
	}

	@Override
	public CsvTransaction saveCsvTransaction(CsvTransaction csvTransaction) {
		return csvTransactionRepository.save(csvTransaction);
	}

	@Override
	public Iterable<CsvTransaction> saveCsvTransactionList(Iterable<CsvTransaction> csvTransactionIterable) {
		return csvTransactionRepository.save(csvTransactionIterable);
	}

	@Override
	public void deleteCsvTransaction(Integer id) {
		csvTransactionRepository.delete(id);
	}

}
