package com.bank.csvapp.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;

public class BudgetPlanCsvTransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BudgetPlanCsvTransactionId")
    private Integer id;

    @Version
    private Integer version;

    @ManyToOne
    private BudgetPlan budgetPlan;

    @ManyToOne
    private CsvTransactionTag csvTransactionTag;

    @Size(max = 100)
    private Integer weight;

    public BudgetPlanCsvTransactionTag() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BudgetPlan getBudgetPlan() {
        return budgetPlan;
    }

    public void setBudgetPlan(BudgetPlan budgetPlan) {
        this.budgetPlan = budgetPlan;
    }

    public CsvTransactionTag getCsvTransactionTag() {
        return csvTransactionTag;
    }

    public void setCsvTransactionTag(CsvTransactionTag csvTransactionTag) {
        this.csvTransactionTag = csvTransactionTag;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
