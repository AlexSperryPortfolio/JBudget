package com.bank.jbudget.utility;

import com.bank.jbudget.JBudget;
import com.bank.jbudget.domain.CsvTransaction;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FileImportManager {

    private static final Logger logger = Logger.getLogger(FileImportManager.class);

    private FileImportManager() {//private default constructor
    }

    public static Set<CsvTransaction> csvFileImport(InputStream accountHistoryStream) {
        Set<CsvTransaction> transactions = new HashSet<>();
        List<CSVRecord> csvRecords = new ArrayList<>();//initialize as empty

        try (BufferedReader br = new BufferedReader(new InputStreamReader(accountHistoryStream, StandardCharsets.UTF_8))) {
            CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(br);
            csvRecords = csvParser.getRecords();
        } catch (IOException ioEx) {
            logger.error(ioEx);
        }

        String xsdPath = JBudget.class.getResource("/transaction.xsd").getPath();

        for (CSVRecord csvRecord : csvRecords) {
            try {
                //validate transaction against xsd
                Document doc = XmlUtils.getTransactionXmlDoc(csvRecord);
                XmlUtils.validate(new DOMSource(doc), xsdPath); //throw exception if invalid against XSD
                CsvTransaction csvTransaction = new CsvTransaction(csvRecord);
                transactions.add(csvTransaction);
            } catch (final IOException | ParseException | ParserConfigurationException | TransformerException | SAXException ex) {
                logger.error(ex);
            }
        }

        return transactions;
    }
}