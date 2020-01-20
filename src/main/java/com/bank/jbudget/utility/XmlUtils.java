package com.bank.jbudget.utility;

import com.bank.jbudget.domain.CsvTransaction;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

final class XmlUtils {

    private static final Logger logger = Logger.getLogger(XmlUtils.class);

    private static Map<String, Validator> validatorMap = new HashMap<>();

    private XmlUtils() {//private default constructor
    }

    static void validate(Source xml, String xsdPath) throws SAXException, IOException {
        if(!validatorMap.containsKey(xsdPath)) { //insert xsdPath's Validator into map if not already present
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validatorMap.put(xsdPath, validator);
        }

        Validator validator = validatorMap.get(xsdPath);
        validator.validate(xml);
    }

    static Document getTransactionXmlDoc(CSVRecord csvRecord) throws TransformerException, ParserConfigurationException, ParseException {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        icFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder icBuilder;

        icBuilder = icFactory.newDocumentBuilder();
        Document doc = icBuilder.newDocument();
        Element mainRootElement = doc.createElementNS("", "transaction");
        doc.appendChild(mainRootElement);

        //region append child elements to root element
        Element accountNumberElement = doc.createElement("accountNumber");
        accountNumberElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.ACCOUNT_NUMBER_STRING)));
        mainRootElement.appendChild(accountNumberElement);

        Element postDateElement = doc.createElement("postDate");
        Date originalDate = CsvTransaction.STANDARD_TRANSACTION_DATE_FORMAT.parse(csvRecord.get(CsvTransaction.POST_DATE_STRING));
        String xmlDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(originalDate.getTime()));
        postDateElement.appendChild(doc.createTextNode(xmlDateString));
        mainRootElement.appendChild(postDateElement);

        Element checkColumnElement = doc.createElement("checkColumn");
        checkColumnElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.CHECK_COLUMN_STRING)));
        mainRootElement.appendChild(checkColumnElement);

        Element descriptionElement = doc.createElement("description");
        descriptionElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.DESCRIPTION_STRING)));
        mainRootElement.appendChild(descriptionElement);

        if (!StringUtils.isEmpty(csvRecord.get(CsvTransaction.DEBIT_STRING))) {
            Element debitElement = doc.createElement("debit");
            debitElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.DEBIT_STRING)));
            mainRootElement.appendChild(debitElement);
        }

        if (!StringUtils.isEmpty(csvRecord.get(CsvTransaction.CREDIT_STRING))) {
            Element creditElement = doc.createElement("credit");
            creditElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.CREDIT_STRING)));
            mainRootElement.appendChild(creditElement);
        }

        Element statusElement = doc.createElement("status");
        statusElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.STATUS_STRING)));
        mainRootElement.appendChild(statusElement);

        if (!StringUtils.isEmpty(CsvTransaction.BALANCE_STRING)) {
            Element balanceElement = doc.createElement("balance");
            balanceElement.appendChild(doc.createTextNode(csvRecord.get(CsvTransaction.BALANCE_STRING)));
            mainRootElement.appendChild(balanceElement);
        }
        //endregion

        // output DOM XML to console
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.toString();

        logger.debug("Transaction XML:\n" + output);

        return doc;
    }
}
