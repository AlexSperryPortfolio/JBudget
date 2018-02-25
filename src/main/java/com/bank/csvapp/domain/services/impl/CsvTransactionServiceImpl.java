package com.bank.csvapp.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.domain.repositories.CsvTransactionRepository;
import com.bank.csvapp.domain.services.CsvTransactionService;

@Service
public class CsvTransactionServiceImpl implements CsvTransactionService{
	
	@Autowired
	private CsvTransactionRepository transactionRepository;

	@Override
	public Iterable<CsvTransaction> listAllTransactions() {
		return transactionRepository.findAll();
	}

	@Override
	public CsvTransaction getTransactionById(Integer id) {
		return transactionRepository.findOne(id);
	}

	@Override
	public CsvTransaction saveTransaction(CsvTransaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	public Iterable<CsvTransaction> saveTransactionList(Iterable<CsvTransaction> transactionIterable) {
		return transactionRepository.save(transactionIterable);
	}

	@Override
	public void deleteTransaction(Integer id) {
		transactionRepository.delete(id);
	}

}
