package com.bank.csvapp.utility;

import com.bank.csvapp.domain.CsvTransaction;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDto {

    private static final Logger log = Logger.getLogger(FileDto.class);

    private static final String ACCOUNT_HISTORY_CSV_PATH = "C:\\Users\\Admin\\Desktop\\Personal files\\CsvFiles\\AccountHistory.csv";
    private static final String TRANSACTION_XSD_PATH = "C:\\Users\\Admin\\git\\com.bank.csvapp\\src\\main\\resources\\transaction.xsd";

    private final CsvTransactionTagManager csvTransactionTagManager;

    public FileDto(CsvTransactionTagManager csvTransactionTagManager) {
        this.csvTransactionTagManager = csvTransactionTagManager;
    }

    public List<CsvTransaction> csvFileImport() {
        String line;
        List<CsvTransaction> transactionList = new ArrayList<>();

        FileInputHandler fileInputHandler = new FileInputHandler(csvTransactionTagManager);

        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(ACCOUNT_HISTORY_CSV_PATH)))) {

            csvTransactionTagManager.seedBaseTags();

            while (sc.hasNext()) {
                line = sc.nextLine();
                if (line != null && !line.isEmpty() && !line.equals("Account Number,Post Date,Check,Description,Debit,Credit,Status,Balance")) {

                    String[] cleanCsvLineArray = fileInputHandler.translateLineToArray(line);

                    //validate transaction against xsd
                    Document doc = XmlUtils.getTransactionXmlDoc(cleanCsvLineArray);
                    InputStream docInputStream = XmlUtils.docToInputStream(doc);
                    InputStream xsdInputStream = new FileInputStream(new File(TRANSACTION_XSD_PATH));
                    XmlUtils.validate(docInputStream, xsdInputStream);

                    CsvTransaction csvTransaction = fileInputHandler.cleanLineArrayToCsvTransaction(cleanCsvLineArray);
                    transactionList.add(csvTransaction);
                }
            }
        } catch (final IOException | ParseException | ParserConfigurationException | TransformerException | SAXException ex) {
            log.error(ex);
        }
        return transactionList;
    }
}