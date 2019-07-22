package com.bank.csvapp.utility;

import com.bank.csvapp.domain.CsvTransaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FileDto {

    private static final Logger log = Logger.getLogger(FileDto.class);

    //todo: make these env vars
    private static final String ACCOUNT_HISTORY_CSV_PATH = "C:\\Users\\Admin\\Desktop\\Personal files\\CsvFiles\\AccountHistory.csv";
    private static final String TRANSACTION_XSD_PATH = "C:\\Users\\Admin\\git\\com.bank.csvapp\\src\\main\\resources\\transaction.xsd";

    private final CsvTransactionTagManager csvTransactionTagManager;

    public FileDto(CsvTransactionTagManager csvTransactionTagManager) {
        this.csvTransactionTagManager = csvTransactionTagManager;
    }

    public List<CsvTransaction> csvFileImport() {
        List<CsvTransaction> transactionList = new ArrayList<>();
        List<CSVRecord> csvRecordList = new ArrayList<>();//initialize as empty

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_HISTORY_CSV_PATH))) {
            CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(br);
            csvRecordList = csvParser.getRecords();
        } catch (IOException ioEx) {
            log.error(ioEx);
        }

        csvTransactionTagManager.seedBaseTags();

        for (CSVRecord csvRecord : csvRecordList) {
            try {
                //validate transaction against xsd
                Document doc = XmlUtils.getTransactionXmlDoc(csvRecord);
                XmlUtils.validate(new DOMSource(doc), TRANSACTION_XSD_PATH);

                //if valid (no exceptions thrown) create CsvTransaction and add to list
                CsvTransaction csvTransaction = new CsvTransaction(csvRecord);
                csvTransaction.setTagList(csvTransactionTagManager.addDefaultTags(csvTransaction.getDescription()));
                transactionList.add(csvTransaction);

            } catch (final IOException | ParseException | ParserConfigurationException | TransformerException | SAXException ex) {
                log.error(ex);
            }
        }

        return transactionList;
    }
}