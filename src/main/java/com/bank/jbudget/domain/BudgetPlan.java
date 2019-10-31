package com.bank.jbudget.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.List;

public class BudgetPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BudgetPlanId")
    private Integer id;

    @Version
    private Integer version;

    @Column
    private String name;

    @OneToMany
    private List<BudgetPlanCsvTransactionTag> budgetPlanCsvTransactionTagList;

    @ManyToMany
    private List<CsvTransactionTag> exclusionTagList;

    public BudgetPlan() {
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
