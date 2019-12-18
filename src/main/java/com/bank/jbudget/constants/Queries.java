package com.bank.jbudget.constants;

public class Queries {
    private Queries() {}

    //region transaction queries
    public static final String GET_CSV_TRANSACTIONS_BY_DATE_RANGE =
            "SELECT c FROM CsvTransaction c INNER JOIN FETCH c.tagList WHERE c.postDate >= :startDate AND c.postDate < :endDate ";

    public static final String GET_CSV_TRANSACTIONS_BY_DATE_RANGE_AND_TAG_IDS =
            "SELECT c FROM CsvTransaction c " +
                    "INNER JOIN FETCH c.tagList tl " +
                    "WHERE c.postDate >= :startDate " +
                    "AND c.postDate < :endDate " +
                    "AND tl.id in :csvTagIds";

    public static final String GET_UNCATEGORIZED_TRANSACTIONS_COUNT =
            "SELECT COUNT(c) FROM CsvTransaction c " +
                    "INNER JOIN c.tagList tl " +
                    "WHERE tl.id = :uncategorizedTagId";

    public static final String GET_UNCATEGORIZED_TRANSACTION_IDS_PAGINATED =
            "SELECT ttg.transaction_id FROM transaction_transaction_tag ttg " +
                    "WHERE ttg.transaction_tag_id = :uncategorizedTagId " +
                    "LIMIT :limit " +
                    "OFFSET :offset";

    public static final String GET_UNCATEGORIZED_TRANSACTIONS_BY_ID =
            "SELECT c FROM CsvTransaction c " +
                    "INNER JOIN FETCH c.tagList tl " +
                    "WHERE c.id in :uncategorizedTransactionIds";

    public static final String GET_ALL_MATCHING_TRANSACTIONS =
            "SELECT c FROM CsvTransaction c " +
                    "WHERE c.description like :matchString";
    //endregion
}
