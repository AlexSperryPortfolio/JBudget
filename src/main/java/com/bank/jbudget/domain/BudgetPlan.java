package com.bank.jbudget.domain;

import com.bank.jbudget.constants.CommonConstants;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

public class BudgetPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = CommonConstants.BIGINT, name = "BudgetPlanId")
    private Long id;

    @Column
    private String name;

    @OneToMany
    private List<BudgetPlanCsvTransactionTag> budgetPlanCsvTransactionTagList;

    @ManyToMany
    private List<CsvTransactionTag> exclusionTagList;

    public BudgetPlan() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BudgetPlanCsvTransactionTag> getBudgetPlanCsvTransactionTagList() {
        return budgetPlanCsvTransactionTagList;
    }

    public void setBudgetPlanCsvTransactionTagList(List<BudgetPlanCsvTransactionTag> budgetPlanCsvTransactionTagList) {
        this.budgetPlanCsvTransactionTagList = budgetPlanCsvTransactionTagList;
    }

    public List<CsvTransactionTag> getExclusionTagList() {
        return exclusionTagList;
    }

    public void setExclusionTagList(List<CsvTransactionTag> exclusionTagList) {
        this.exclusionTagList = exclusionTagList;
    }
}
