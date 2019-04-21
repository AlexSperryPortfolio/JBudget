package com.bank.csvapp.utility;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

final class XmlUtils {

    private static final Logger log = Logger.getLogger(XmlUtils.class);

    private XmlUtils() {
    }

    static void validate(InputStream xml, InputStream xsd) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(xsd));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xml));
    }

    static Document getTransactionXmlDoc(String[] cleanCsvLineArray) throws TransformerException, ParserConfigurationException, ParseException {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;

        icBuilder = icFactory.newDocumentBuilder();
        Document doc = icBuilder.newDocument();
        Element mainRootElement = doc.createElementNS("", "transaction");
        doc.appendChild(mainRootElement);

        // append child elements to root element
        Element accountNumberElement = doc.createElement("accountNumber");
        accountNumberElement.appendChild(doc.createTextNode(cleanCsvLineArray[0]));
        mainRootElement.appendChild(accountNumberElement);

        Element postDateElement = doc.createElement("postDate");
        java.util.Date originalDate = new SimpleDateFormat("MM/dd/yyyy").parse(cleanCsvLineArray[1]);
        String xmlDateString = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(originalDate.getTime()));
        postDateElement.appendChild(doc.createTextNode(xmlDateString));
        mainRootElement.appendChild(postDateElement);

        Element checkColumnElement = doc.createElement("checkColumn");
        checkColumnElement.appendChild(doc.createTextNode(cleanCsvLineArray[2]));
        mainRootElement.appendChild(checkColumnElement);

        Element descriptionElement = doc.createElement("description");
        descriptionElement.appendChild(doc.createTextNode(cleanCsvLineArray[3]));
        mainRootElement.appendChild(descriptionElement);

        if (!cleanCsvLineArray[4].isEmpty()) {
            Element debitElement = doc.createElement("debit");
            debitElement.appendChild(doc.createTextNode(cleanCsvLineArray[4]));
            mainRootElement.appendChild(debitElement);
        }

        if (!cleanCsvLineArray[5].isEmpty()) {
            Element creditElement = doc.createElement("credit");
            creditElement.appendChild(doc.createTextNode(cleanCsvLineArray[5]));
            mainRootElement.appendChild(creditElement);
        }

        Element statusElement = doc.createElement("status");
        statusElement.appendChild(doc.createTextNode(cleanCsvLineArray[6]));
        mainRootElement.appendChild(statusElement);

        if (!cleanCsvLineArray[7].isEmpty()) {
            Element balanceElement = doc.createElement("balance");
            balanceElement.appendChild(doc.createTextNode(cleanCsvLineArray[7]));
            mainRootElement.appendChild(balanceElement);
        }

        // output DOM XML to console
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.toString();

        log.debug("Transaction XML:\n" + output);

        return doc;
    }

    static InputStream docToInputStream(Document doc) throws TransformerException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(doc);
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
