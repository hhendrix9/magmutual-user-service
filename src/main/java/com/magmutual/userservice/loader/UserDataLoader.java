package com.magmutual.userservice.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.magmutual.userservice.model.User;
import com.magmutual.userservice.repository.UserRepository;

@Component
public class UserDataLoader implements CommandLineRunner {

    private static final String USER_DATA_FILE = "data/users.csv";

    private final UserRepository userRepository;

    public UserDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        UserCsvReader csvReader = new UserCsvReader();

        ClassPathResource resource =
                new ClassPathResource(USER_DATA_FILE);

        try (InputStream inputStream = resource.getInputStream()) {

            List<User> users = csvReader.read(inputStream);

            userRepository.saveAll(users);
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to load user data from "
                            + USER_DATA_FILE,
                    ex);
        }
    }
}