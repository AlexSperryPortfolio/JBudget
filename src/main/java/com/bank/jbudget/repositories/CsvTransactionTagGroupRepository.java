package com.bank.jbudget.repositories;

import com.bank.jbudget.constants.Queries;
import com.bank.jbudget.domain.CsvTransactionTagGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CsvTransactionTagGroupRepository extends CrudRepository<CsvTransactionTagGroup, Long> {

    @Query(value = Queries.GET_TRANSACTION_TAG_GROUP_WITH_TAGS_BY_ID)
    CsvTransactionTagGroup getCsvTransactionTagGroupWithTagsById(@Param("id") long id);
}
