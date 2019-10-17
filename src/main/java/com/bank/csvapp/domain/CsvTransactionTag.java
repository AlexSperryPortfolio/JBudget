package com.bank.csvapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CsvTransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String typeName;

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

    public Integer getId() {
        return this.id;
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
