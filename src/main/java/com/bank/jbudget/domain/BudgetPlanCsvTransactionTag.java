package com.bank.jbudget.domain;

import com.bank.jbudget.constants.CommonConstants;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

public class BudgetPlanCsvTransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = CommonConstants.BIGINT, name = "BudgetPlanCsvTransactionId")
    private Long id;

    @ManyToOne
    private BudgetPlan budgetPlan;

    @ManyToOne
    private CsvTransactionTag csvTransactionTag;

    @Size(max = 100)
    private Integer weight;

    public BudgetPlanCsvTransactionTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
