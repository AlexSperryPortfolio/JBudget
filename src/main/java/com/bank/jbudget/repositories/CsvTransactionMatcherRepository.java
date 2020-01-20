package com.bank.jbudget.repositories;

import com.bank.jbudget.constants.Queries;
import com.bank.jbudget.domain.CsvTransactionMatcher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CsvTransactionMatcherRepository extends CrudRepository<CsvTransactionMatcher, Long> {

    @Query(Queries.GET_MATCHER_BY_ID_EAGER)
    CsvTransactionMatcher getCsvTransactionMatcherByIdEagerFetches(@Param("id") long id);

    @Query(Queries.GET_ALL_MATCHERS_EAGER)
    Set<CsvTransactionMatcher> getAllCsvTransactionMatchersEagerFetches();

    @Query(Queries.GET_ALL_MATCHERS_USING_GROUP_ID)
    Set<CsvTransactionMatcher> getAllCsvTransactionMatchersUsingGroupId(@Param("id") long id);
}
