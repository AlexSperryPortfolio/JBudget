package com.bank.csvapp.services.impl;

import com.bank.csvapp.domain.CsvTransactionTag;
import com.bank.csvapp.repositories.CsvTransactionTagRepository;
import com.bank.csvapp.services.CsvTransactionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CsvTransactionTagServiceImpl implements CsvTransactionTagService {
	
	@Autowired
	private CsvTransactionTagRepository csvTransactionTagRepository;
	
	public List<CsvTransactionTag> getAllCsvTransactionTags() {
		return (List<CsvTransactionTag>) csvTransactionTagRepository.findAll();
	}

    public CsvTransactionTag getCsvTransactionTagById(Integer id) {
		Optional<CsvTransactionTag> csvTransactionTagOptional = csvTransactionTagRepository.findById(id);
    	return csvTransactionTagOptional.orElse(null);
    }

    public CsvTransactionTag saveCsvTransactionTag(CsvTransactionTag csvTransactionTag) {
    	return csvTransactionTagRepository.save(csvTransactionTag);
    }

    public List<CsvTransactionTag> saveCsvTransactionTagList(List<CsvTransactionTag> csvTransactionTagIterable) {
    	return (List<CsvTransactionTag>) csvTransactionTagRepository.saveAll(csvTransactionTagIterable);
    }

    public void deleteCsvTransactionTag(CsvTransactionTag csvTransactionTag) {
    	csvTransactionTagRepository.delete(csvTransactionTag);
    }

}
