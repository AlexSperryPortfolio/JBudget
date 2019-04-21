package com.bank.csvapp.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.csvapp.domain.CsvTransaction;
import com.bank.csvapp.repositories.CsvTransactionRepository;
import com.bank.csvapp.services.CsvTransactionService;

import java.util.List;

@Service
public class CsvTransactionServiceImpl implements CsvTransactionService{
	
	@Autowired
	private CsvTransactionRepository csvTransactionRepository;

	@Override
	public List<CsvTransaction> listAllCsvTransactions() {
		return (List<CsvTransaction>)csvTransactionRepository.findAll();
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
	public List<CsvTransaction> saveCsvTransactionList(List<CsvTransaction> csvTransactionIterable) {
		return (List<CsvTransaction>) csvTransactionRepository.save(csvTransactionIterable);
	}

	@Override
	public void deleteCsvTransaction(Integer id) {
		csvTransactionRepository.delete(id);
	}

}
