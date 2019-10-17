package com.bank.csvapp.utility;

import com.bank.csvapp.domain.BudgetPlan;

import java.util.Date;

public class BudgetPlanReport {

    private BudgetPlan budgetPlan;

    private String displayName;

    private Date startDate;
    private Date endDate;

    public BudgetPlanReport() {
    }

    public BudgetPlanReport(BudgetPlan budgetPlan, Date startDate, Date endDate) {
        this.budgetPlan = budgetPlan;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BudgetPlan getBudgetPlan() {
        return budgetPlan;
    }

    public void setBudgetPlan(BudgetPlan budgetPlan) {
        this.budgetPlan = budgetPlan;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

}
