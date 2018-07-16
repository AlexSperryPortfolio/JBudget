package com.bank.csvapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bank.csvapp.domain.CsvTransactionType;

public interface CsvTransactionTypeRepository extends CrudRepository<CsvTransactionType,Integer>{
}
