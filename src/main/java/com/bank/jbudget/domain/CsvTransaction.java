package com.bank.jbudget.domain;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.utility.serializers.CsvTransactionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transaction")
@JsonSerialize(contentUsing = CsvTransactionSerializer.class)
public class CsvTransaction {

    public static final SimpleDateFormat STANDARD_TRANSACTION_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static final String ACCOUNT_NUMBER_STRING = "Account Number";
    public static final String POST_DATE_STRING = "Post Date";
    public static final String CHECK_COLUMN_STRING = "Check";
    public static final String DESCRIPTION_STRING = "Description";
    public static final String DEBIT_STRING = "Debit";
    public static final String CREDIT_STRING = "Credit";
    public static final String STATUS_STRING = "Status";
    public static final String BALANCE_STRING = "Balance";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = CommonConstants.BIGINT, name = "transaction_id")
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "post_date", nullable = false)
    private Date postDate;

    @Column(name = "check_column", nullable = false)
    private String checkColumn;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "debit")
    private BigDecimal debit;

    @Column(name = "credit")
    private BigDecimal credit;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "transaction_transaction_tag",
            joinColumns = { @JoinColumn(name = "transaction_id") },
            inverseJoinColumns = { @JoinColumn(name = "transaction_tag_id") }
    )
    private List<CsvTransactionTag> tagList;

    public CsvTransaction() {//default constructor
    }

    public CsvTransaction(CSVRecord csvRecord) throws ParseException {
        this.accountNumber = csvRecord.get(ACCOUNT_NUMBER_STRING);
        postDate = STANDARD_TRANSACTION_DATE_FORMAT.parse(csvRecord.get(POST_DATE_STRING));
        this.postDate = new java.sql.Date(postDate.getTime());
        this.checkColumn = csvRecord.get(CHECK_COLUMN_STRING);
        this.description = csvRecord.get(DESCRIPTION_STRING);
        String debitString = csvRecord.get(DEBIT_STRING);
        this.debit = StringUtils.isEmpty(debitString) ? BigDecimal.ZERO : new BigDecimal(debitString);
        String creditString = csvRecord.get(CREDIT_STRING);
        this.credit = (StringUtils.isEmpty(creditString) ? BigDecimal.ZERO : new BigDecimal(creditString));
        this.status = csvRecord.get(STATUS_STRING);
        String balanceString = csvRecord.get(BALANCE_STRING);
        this.balance = (StringUtils.isEmpty(balanceString) ? BigDecimal.ZERO : new BigDecimal(balanceString));
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getCredit() {
        return this.credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebit() {
        return this.debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<CsvTransactionTag> getTagList() {
        return this.tagList;
    }

    public void setTagList(List<CsvTransactionTag> typeList) {
        this.tagList = typeList;
    }

    public void addTag(CsvTransactionTag transactionTag) {
        this.tagList.add(transactionTag);
    }

    public void addTags(Collection<CsvTransactionTag> transactionTags) {
        this.tagList.addAll(transactionTags);
    }

    @Override
    public String toString() {
        return this.postDate+" "+this.description+" "+this.debit+" "+this.credit+" "+this.balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvTransaction that = (CsvTransaction) o;
        return Objects.equals(getAccountNumber(), that.getAccountNumber()) &&
                Objects.equals(getPostDate(), that.getPostDate()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getDebit(), that.getDebit()) &&
                Objects.equals(getCredit(), that.getCredit()) &&
                Objects.equals(getBalance(), that.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountNumber(), getPostDate(), getDescription(), getDebit(), getCredit(), getBalance());
    }
}
