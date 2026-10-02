package com.magmutual.userservice.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.magmutual.userservice.exception.CsvParsingException;
import com.magmutual.userservice.model.User;

public class UserCsvReader {

    public List<User> read(InputStream inputStream) throws IOException {

        List<User> users = new ArrayList<>();

        try (Reader reader = new InputStreamReader(
                inputStream,
                StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);

            for (CSVRecord record : records) {
                users.add(parseRecord(record));
            }
        }

        return users;
    }

    private User parseRecord(CSVRecord record) {

        try {
            return new User(
                    Long.valueOf(record.get("id")),
                    record.get("firstname"),
                    record.get("lastname"),
                    record.get("email"),
                    record.get("profession"),
                    LocalDate.parse(record.get("dateCreated")),
                    record.get("country"),
                    record.get("city"));

        } catch (RuntimeException ex) {
            throw new CsvParsingException(
                    "Failed to parse CSV record "
                            + record.getRecordNumber(),
                    ex);
        }
    }
}