package com.bank.csvapp.domain;

import java.util.List;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

@Entity
public class CsvTransaction {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CsvTransactionId")
	private Integer id;
	
	@Version
	private Integer version;
	
//	Account Number,Post Date,Check,Description,Debit,Credit,Status,Balance
	private String accountNumber;
	private Date postDate;
	private String checkColumn;
	private String description;
	private Float debit;
	private Float credit;
	private String status;
	private Float balance;
//	private CsvTransactionType csvTransactionType;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CsvTransactionType> typeList;
	
	public CsvTransaction(String accountNumber, Date postDate, String checkColumn, String description, 
			Float debit, Float credit, String status, Float balance, List<CsvTransactionType> types) {
		this.accountNumber = accountNumber;
		this.postDate = postDate;
		this.checkColumn = checkColumn;
		this.description = description;
		this.debit = debit;
		this.credit = credit;
		this.status = status;
		this.balance = balance;
		this.typeList = types;
	}

	public CsvTransaction() {};
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public Date getPostDate() {
		return this.postDate;
	}
	
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	
	public String getCheckColumn() {
		return this.checkColumn;
	}
	
	public void setCheckColumn(String checkColumn) {
		this.checkColumn = checkColumn;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Float getCredit() {
		return this.credit;
	}
	
	public void setCredit(Float credit) {
		this.credit = credit;
	}
	
	public Float getDebit() {
		return this.debit;
	}
	
	public void setDebit(Float debit) {
		this.debit = debit;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Float getBalance() {
		return this.balance;
	}
	
	public void setBalance(Float balance) {
		this.balance = balance;
	}
	
	public List<CsvTransactionType> getTypeList() {
		return this.typeList;
	}
	
	public void setTypeList(List<CsvTransactionType> typeList) {
		this.typeList = typeList;
	}

//	public CsvTransactionType getCsvTransactionType() {
//		return csvTransactionType;
//	}
//
//	public void setCsvTransactionType(CsvTransactionType csvTransactionType) {
//		this.csvTransactionType = csvTransactionType;
//	}
}
