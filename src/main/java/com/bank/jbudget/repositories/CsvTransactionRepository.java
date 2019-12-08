package com.bank.jbudget.repositories;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.constants.Queries;
import com.bank.jbudget.domain.CsvTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Set;

public interface CsvTransactionRepository extends CrudRepository<CsvTransaction, Long>{

    @Query(Queries.GET_CSV_TRANSACTIONS_BY_DATE_RANGE)
    Set<CsvTransaction> getAllTransactionsInRange(
            @Param(CommonConstants.START_DATE) Date startDate,
            @Param(CommonConstants.END_DATE) Date endDate);

    @Query(Queries.GET_CSV_TRANSACTIONS_BY_DATE_RANGE_AND_TAG_IDS)
    Set<CsvTransaction> getAllTransactionsInRangeAndWithTags(
            @Param(CommonConstants.START_DATE) Date startDate,
            @Param(CommonConstants.END_DATE) Date endDate,
            @Param(CommonConstants.CSV_TAG_IDS) Set<Long> csvTagIds);

//    @Query( value = Queries.GET_UNCATEGORIZED_TRANSACTIONS,
//            countQuery = Queries.GET_UNCATEGORIZED_TRANSACTIONS_COUNT)
//    Page<CsvTransaction> getAllUncategorizedTransactionsPaginated(
//            @Param(CommonConstants.UNCATEGORIZED_TAG_ID) Long uncategorizedTagId,
//            Pageable pageable);


    @Query( value = Queries.GET_UNCATEGORIZED_TRANSACTION_IDS_PAGINATED, nativeQuery = true)
    long[] getUncategorizedTransactionIds(@Param(CommonConstants.UNCATEGORIZED_TAG_ID) Long uncategorizedTagId);

    @Query(value = Queries.GET_UNCATEGORIZED_TRANSACTIONS_PAGINATED_IDS)
    Set<CsvTransaction> getUncategorizedTransactionsPaginated(@Param("uncategorizedTransactionIds") long[] uncategorizedTransactionIds);

    @Query(Queries.GET_ALL_MATCHING_TRANSACTIONS)
    Set<CsvTransaction> getAllMatchingTransactions(@Param("matchString") String matchString);
}
