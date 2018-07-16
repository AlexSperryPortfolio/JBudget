package com.bank.csvapp.domain;

import javax.persistence.*;

@Entity
public class CsvTransactionFilter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CsvTransactionFilterId")
	private Integer id;
	
	@Version
	private Integer version;
	
	public CsvTransactionFilter() {}
	
	public CsvTransactionFilter(String filterText) {
		this.filterText = filterText;
	}
	
	private String filterField;
	private String filterText;

	public String getFilterField() {
		return this.filterField;
	}
	
	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}
	
	public String getFilterText() {
		return this.filterText;
	}
	
	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}
}
