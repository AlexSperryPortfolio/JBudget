package com.bank.csvapp.utility.serializers;

import com.bank.csvapp.domain.CsvTransaction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CsvTransactionSerializer extends StdSerializer<CsvTransaction> {

    public CsvTransactionSerializer() {
        this(null);
    }

    public CsvTransactionSerializer(Class<CsvTransaction> t) {
        super(t);
    }

    @Override
    public void serialize(CsvTransaction csvTransaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("accountNumber",csvTransaction.getAccountNumber());
        jsonGenerator.writeNumberField("balance",csvTransaction.getBalance());
        jsonGenerator.writeStringField("checkColumn",csvTransaction.getCheckColumn());
        jsonGenerator.writeNumberField("credit",csvTransaction.getCredit());
        jsonGenerator.writeNumberField("debit",csvTransaction.getDebit());
        jsonGenerator.writeStringField("description",csvTransaction.getDescription());
        jsonGenerator.writeNumberField("postDate",csvTransaction.getPostDate().getTime());
        jsonGenerator.writeStringField("status",csvTransaction.getStatus());
        jsonGenerator.writeEndObject();
    }
}
