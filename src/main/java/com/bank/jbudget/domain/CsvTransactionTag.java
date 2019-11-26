package com.bank.jbudget.domain;

import com.bank.jbudget.constants.CommonConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "transaction_tag")
public class CsvTransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = CommonConstants.BIGINT, name = "transaction_tag_id")
    private Long id;

    @Column(name = "name")
    private String typeName;

    @Column(name = "match_string")
    private String matchString;

    public CsvTransactionTag() {//default constructor
    }

    public CsvTransactionTag(String typeName) {
        this.typeName = typeName;
    }

    public CsvTransactionTag(String typeName, String matchString) {
        this.typeName = typeName;
        this.matchString = matchString;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getMatchString() {
        return matchString;
    }

    public void setMatchString(String matchString) {
        this.matchString = matchString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvTransactionTag that = (CsvTransactionTag) o;
        return Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getMatchString(), that.getMatchString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTypeName(), getMatchString());
    }
}
