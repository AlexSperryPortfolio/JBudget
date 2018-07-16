package com.bank.csvapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bank.csvapp.domain.CsvTransactionFilter;

public interface CsvTransactionFilterRepository extends CrudRepository<CsvTransactionFilter,Integer>{

}
