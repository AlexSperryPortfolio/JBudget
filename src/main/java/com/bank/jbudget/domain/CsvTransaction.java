package com.bank.jbudget.domain;

import com.bank.jbudget.utility.serializers.CsvTransactionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Transaction_Id")
    private Integer id;

    @Column(name = "Account_Number", nullable = false)
    private String accountNumber;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "Post_Date", nullable = false)
    private Date postDate;

    @Column(name = "Check_Column", nullable = false)
    private String checkColumn;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "Debit")
    private BigDecimal debit;

    @Column(name = "Credit")
    private BigDecimal credit;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "Balance", nullable = false)
    private BigDecimal balance;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "transaction_transaction_tag",
            joinColumns = { @JoinColumn(name = "Transaction_Id") },
            inverseJoinColumns = { @JoinColumn(name = "Transaction_Tag_Id") }
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
