package com.bank.jbudget.domain;

import com.bank.jbudget.constants.CommonConstants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "transaction_matcher")
public class CsvTransactionMatcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = CommonConstants.BIGINT, name = "transaction_matcher_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "match_string")
    private String matchString;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transaction_tag_id")
    private CsvTransactionTag csvTransactionTag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transaction_tag_group_id")
    private CsvTransactionTagGroup csvTransactionTagGroup;

    public CsvTransactionMatcher() {//default constructor
    }

    public CsvTransactionMatcher(String name, String matchString) {
        this.name = name;
        this.matchString = matchString;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchString() {
        return matchString;
    }

    public void setMatchString(String matchString) {
        this.matchString = matchString;
    }

    public CsvTransactionTag getCsvTransactionTag() {
        return this.csvTransactionTag;
    }

    public void setCsvTransactionTag(CsvTransactionTag csvTransactionTag) {
        this.csvTransactionTag = csvTransactionTag;
    }

    public CsvTransactionTagGroup getCsvTransactionTagGroup() {
        return this.csvTransactionTagGroup;
    }

    public void setCsvTransactionTagGroup(CsvTransactionTagGroup csvTransactionTagGroup) {
        this.csvTransactionTagGroup = csvTransactionTagGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvTransactionMatcher that = (CsvTransactionMatcher) o;
        return Objects.equals(getMatchString(), that.getMatchString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMatchString());
    }
}
