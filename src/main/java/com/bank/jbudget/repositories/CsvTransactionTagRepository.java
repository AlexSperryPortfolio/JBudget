package com.bank.jbudget.repositories;

import com.bank.jbudget.constants.Queries;
import com.bank.jbudget.domain.CsvTransactionTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CsvTransactionTagRepository extends CrudRepository<CsvTransactionTag, Long>{
    @Query(Queries.GET_ALL_TAGS_BY_IDS)
    Set<CsvTransactionTag> getAllTagsByIds(@Param("tagIds") Long[] tagIds);
}
