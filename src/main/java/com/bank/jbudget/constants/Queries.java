package com.bank.jbudget.constants;

public class Queries {
    private Queries() {}

    public static final String GET_CSV_TRANSACTIONS_BY_DATE_RANGE =
            "SELECT c FROM CsvTransaction c INNER JOIN FETCH c.tagList WHERE c.postDate >= :startDate AND c.postDate < :endDate ";

    public static final String GET_CSV_TRANSACTIONS_BY_DATE_RANGE_AND_TAG_IDS =
            "SELECT c FROM CsvTransaction c " +
                    "INNER JOIN FETCH c.tagList tl " +
                    "WHERE c.postDate >= :startDate " +
                    "AND c.postDate < :endDate " +
                    "AND tl.id in :csvTagIds";
}
