package com.bank.jbudget.constants;

public class Queries {
    private Queries() {}

    //region transaction queries
    public static final String SELECT_TRANSACTIONS_FETCH_TAGS = "SELECT c FROM CsvTransaction c INNER JOIN FETCH c.tags tags ";

    public static final String GET_CSV_TRANSACTIONS_BY_DATE_RANGE =
            SELECT_TRANSACTIONS_FETCH_TAGS + "WHERE c.postDate >= :startDate AND c.postDate < :endDate ";

    public static final String GET_CSV_TRANSACTIONS_BY_DATE_RANGE_AND_TAG_IDS =
            SELECT_TRANSACTIONS_FETCH_TAGS +
                    "WHERE c.postDate >= :startDate " +
                    "AND c.postDate < :endDate " +
                    "AND tags.id in :csvTagIds";

    public static final String GET_UNCATEGORIZED_TRANSACTIONS_COUNT =
            "SELECT COUNT(c) FROM CsvTransaction c " +
                    "INNER JOIN c.tags tags " +
                    "WHERE tags.id = :uncategorizedTagId";

    public static final String GET_UNCATEGORIZED_TRANSACTION_IDS_PAGINATED =
            "SELECT ttg.transaction_id FROM transaction_transaction_tag ttg " +
                    "WHERE ttg.transaction_tag_id = :uncategorizedTagId " +
                    "LIMIT :limit " +
                    "OFFSET :offset";

    public static final String GET_ALL_UNCATEGORIZED_TRANSACTIONS_USING_UNCATEGORIZED_TRANSACTION_ID =
            SELECT_TRANSACTIONS_FETCH_TAGS + "WHERE c.id in :uncategorizedTransactionIds";

    public static final String GET_ALL_MATCHING_TRANSACTIONS =
            SELECT_TRANSACTIONS_FETCH_TAGS + "WHERE c.description like :matchString";
    //endregion

    //region Tag Queries
    public static final String GET_ALL_TAGS_BY_IDS =
            "SELECT t FROM CsvTransactionTag t " +
                "WHERE t.id in :tagIds";
    //endregion

    //region Matcher Queries
    public static final String SELECT_MATCHERS_FETCH_GROUP = "SELECT m FROM CsvTransactionMatcher m LEFT OUTER JOIN FETCH m.csvTransactionTagGroup g ";

    public static final String GET_MATCHER_BY_ID_EAGER =
             SELECT_MATCHERS_FETCH_GROUP +
                    "LEFT OUTER JOIN FETCH m.csvTransactionTag tt " +
                    "INNER JOIN FETCH g.tags " +
                    "WHERE m.id = :id";

    public static final String GET_ALL_MATCHERS_EAGER =
            SELECT_MATCHERS_FETCH_GROUP +
                    "LEFT OUTER JOIN FETCH m.csvTransactionTag tt " +
                    "INNER JOIN FETCH g.tags";

    public static final String GET_ALL_MATCHERS_USING_GROUP_ID =
            SELECT_MATCHERS_FETCH_GROUP +
                    "WHERE g.id = :id";
    //endregion

    //region Transaction Tag Group Queries
    public static final String GET_TRANSACTION_TAG_GROUP_WITH_TAGS_BY_ID =
            "SELECT ttg FROM CsvTransactionTagGroup ttg " +
                    "INNER JOIN FETCH ttg.tags " +
                    "WHERE ttg.id = :id";
    //endregion
}
