package com.bank.csvapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CsvTransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String typeName;

    private String matchString;

    public CsvTransactionTag() {
    }

    public CsvTransactionTag(String typeName) {
        this.typeName = typeName;
    }

    public CsvTransactionTag(String typeName, String matchString) {
        this.typeName = typeName;
        this.matchString = matchString;
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
}
