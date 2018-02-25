package com.bank.csvapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class CsvTransaction {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CsvTransactionId")
	private Integer id;
	
	@Version
	private Integer version;
	
//	Account Number,Post Date,Check,Description,Debit,Credit,Status,Balance
	private String accountNumber;
	private String postDate;
	private String checkColumn;
	private String description;
	private String debit;
	private String credit;
	private String status;
	private String balance;
	
	private String type;
	
	public CsvTransaction(String accountNumber, String postDate, String checkColumn, String description, String debit, String credit, String status, String balance) {
		this.accountNumber = accountNumber;
		this.postDate = postDate;
		this.checkColumn = checkColumn;
		this.description = description;
		this.debit = debit;
		this.credit = credit;
		this.status = status;
		this.balance = balance;
	}

	public CsvTransaction() {};
	
}
