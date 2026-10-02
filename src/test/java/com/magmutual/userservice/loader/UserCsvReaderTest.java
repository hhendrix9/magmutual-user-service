package com.magmutual.userservice.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.magmutual.userservice.exception.CsvParsingException;
import com.magmutual.userservice.model.User;

class UserCsvReaderTest {

    private final UserCsvReader csvReader = new UserCsvReader();

    @Test
    void read_whenCsvIsValid_returnsUsers() throws Exception {
        String csv = """
                id,firstname,lastname,email,profession,dateCreated,country,city
                100,Andree,Flita,Andree.Flita@gmail.com,worker,2020-08-31,Wallis and Futuna,Nanjing
                """;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                csv.getBytes(StandardCharsets.UTF_8));
        List<User> users = csvReader.read(inputStream);
        assertEquals(1, users.size());

        User user = users.get(0);

        assertEquals(100L, user.getId());
        assertEquals("Andree", user.getFirstname());
        assertEquals("Flita", user.getLastname());
        assertEquals("Andree.Flita@gmail.com", user.getEmail());
        assertEquals("worker", user.getProfession());
        assertEquals(LocalDate.of(2020, 8, 31), user.getDateCreated());
        assertEquals("Wallis and Futuna", user.getCountry());
        assertEquals("Nanjing", user.getCity());
    }

    @Test
    void read_whenDateIsInvalid_throwsCsvParsingException() {
        String csv = """
                id,firstname,lastname,email,profession,dateCreated,country,city
                100,Andree,Flita,Andree.Flita@gmail.com,worker,NOT-A-DATE,Wallis and Futuna,Nanjing
                """;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                csv.getBytes(StandardCharsets.UTF_8));
        CsvParsingException exception = assertThrows(
                CsvParsingException.class,
                () -> csvReader.read(inputStream));
        assertEquals(
                "Failed to parse CSV record 1",
                exception.getMessage());
    }
}