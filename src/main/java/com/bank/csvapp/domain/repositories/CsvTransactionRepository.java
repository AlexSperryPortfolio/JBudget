package com.bank.csvapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bank.csvapp.domain.CsvTransaction;

public interface CsvTransactionRepository extends CrudRepository<CsvTransaction, Integer>{

}
