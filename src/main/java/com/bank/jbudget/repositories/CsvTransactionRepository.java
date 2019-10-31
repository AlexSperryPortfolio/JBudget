package com.bank.jbudget.repositories;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.constants.Queries;
import com.bank.jbudget.domain.CsvTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Set;

public interface CsvTransactionRepository extends CrudRepository<CsvTransaction, Integer>{

    @Query(Queries.GET_CSV_TRANSACTIONS_BY_DATE_RANGE)
    Set<CsvTransaction> getAllTransactionsInRange(
            @Param(CommonConstants.START_DATE) Date startDate,
            @Param(CommonConstants.END_DATE) Date endDate);

    @Query(Queries.GET_CSV_TRANSACTIONS_BY_DATE_RANGE_AND_TAG_IDS)
    Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(
            @Param(CommonConstants.START_DATE) Date startDate,
            @Param(CommonConstants.END_DATE) Date endDate,
            @Param(CommonConstants.CSV_TAG_IDS) Set<Integer> csvTagIds);
}
