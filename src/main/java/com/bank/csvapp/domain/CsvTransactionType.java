package com.bank.csvapp.domain;

import javax.persistence.*;

@Entity
public class CsvTransactionType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String typeName;
	
	public CsvTransactionType() {};
	
	public CsvTransactionType(String typeName) {
		this.typeName = typeName;
	}
	
	public String getTypeName() {
		return this.typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
