package com.bank.jbudget.services.impl;

import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.repositories.CsvTransactionTagRepository;
import com.bank.jbudget.services.CsvTransactionTagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CsvTransactionTagServiceImpl implements CsvTransactionTagService {

	private CsvTransactionTagRepository csvTransactionTagRepository;

	public CsvTransactionTagServiceImpl(CsvTransactionTagRepository csvTransactionTagRepository) {
		this.csvTransactionTagRepository = csvTransactionTagRepository;
	}

	//region crud methods
	public List<CsvTransactionTag> getAllCsvTransactionTags() {
		return (List<CsvTransactionTag>) csvTransactionTagRepository.findAll();
	}

    public CsvTransactionTag getCsvTransactionTagById(Long id) {
		Optional<CsvTransactionTag> csvTransactionTagOptional = csvTransactionTagRepository.findById(id);
    	return csvTransactionTagOptional.orElse(null);
    }

    public CsvTransactionTag saveCsvTransactionTag(CsvTransactionTag csvTransactionTag) {
    	return csvTransactionTagRepository.save(csvTransactionTag);
    }

    public List<CsvTransactionTag> saveCsvTransactionTagList(List<CsvTransactionTag> csvTransactionTagList) {
    	return (List<CsvTransactionTag>) csvTransactionTagRepository.saveAll(csvTransactionTagList);
    }

    public void deleteCsvTransactionTag(CsvTransactionTag csvTransactionTag) {
    	csvTransactionTagRepository.delete(csvTransactionTag);
    }
    //endregion

	//region helper methods

	//endregion

}
