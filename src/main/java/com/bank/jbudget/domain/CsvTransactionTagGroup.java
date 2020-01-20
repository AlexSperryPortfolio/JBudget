package com.bank.jbudget.domain;

import com.bank.jbudget.constants.CommonConstants;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "transaction_tag_group")
public class CsvTransactionTagGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = CommonConstants.BIGINT, name = "transaction_tag_group_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "transaction_tag_group_transaction_tag",
            joinColumns = { @JoinColumn(name = "transaction_tag_group_id") },
            inverseJoinColumns = { @JoinColumn(name = "transaction_tag_id") }
    )
    private Set<CsvTransactionTag> tags = new HashSet<>();

    public CsvTransactionTagGroup() {//default constructor
    }

    public CsvTransactionTagGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CsvTransactionTag> getTags() {
        return this.tags;
    }

    public void setTags(Set<CsvTransactionTag> typeList) {
        this.tags = typeList;
    }

    public void addTag(CsvTransactionTag transactionTag) {
        this.tags.add(transactionTag);
    }

    public void addTags(Collection<CsvTransactionTag> transactionTags) {
        this.tags.addAll(transactionTags);
    }

    public void removeTag(CsvTransactionTag transactionTag) {
        this.tags.remove(transactionTag);
    }

    public void removeTags(Collection<CsvTransactionTag> transactionTags) {
        this.tags.removeAll(transactionTags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvTransactionTagGroup that = (CsvTransactionTagGroup) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
