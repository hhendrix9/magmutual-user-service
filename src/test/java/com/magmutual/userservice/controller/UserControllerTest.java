package com.magmutual.userservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.magmutual.userservice.dto.UserResponse;
import com.magmutual.userservice.exception.GlobalExceptionHandler;
import com.magmutual.userservice.exception.UserNotFoundException;
import com.magmutual.userservice.service.UserService;

class UserControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new UserController(userService))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void getUserById_whenUserExists_returns200AndUser() throws Exception {
        UserResponse user = new UserResponse(
                100L,
                "Andree",
                "Flita",
                "Andree.Flita@gmail.com",
                "worker",
                LocalDate.of(2020, 8, 31),
                "Wallis and Futuna",
                "Nanjing");

        when(userService.getUserById(100L))
                .thenReturn(user);
        mockMvc.perform(get("/api/v1/users/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.firstname").value("Andree"))
                .andExpect(jsonPath("$.lastname").value("Flita"))
                .andExpect(jsonPath("$.email").value("Andree.Flita@gmail.com"))
                .andExpect(jsonPath("$.profession").value("worker"))
                .andExpect(jsonPath("$.dateCreated").value("2020-08-31"))
                .andExpect(jsonPath("$.country").value("Wallis and Futuna"))
                .andExpect(jsonPath("$.city").value("Nanjing"));
    }

    @Test
    void getUserById_whenUserDoesNotExist_returns404() throws Exception {
        when(userService.getUserById(999999L))
                .thenThrow(new UserNotFoundException(999999L));
        mockMvc.perform(get("/api/v1/users/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("User with ID 999999 was not found"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/users/999999"));
    }

    @Test
    void getUserById_whenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/users/ABC"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid value 'ABC' for parameter 'id'. Expected type: Long"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/users/ABC"));
    }

    @Test
    void getUsersByDateRange_whenRangeIsValid_returns200AndUsers()
            throws Exception {
        LocalDate startDate = LocalDate.of(2021, 3, 20);
        LocalDate endDate = LocalDate.of(2021, 4, 24);

        UserResponse user1 = new UserResponse(
                101L,
                "Di",
                "Lauraine",
                "Di.Lauraine@gmail.com",
                "developer",
                LocalDate.of(2021, 3, 20),
                "Virgin Islands, British",
                "Chennai");

        UserResponse user2 = new UserResponse(
                102L,
                "Cassondra",
                "Cadmar",
                "Cassondra.Cadmar@gmail.com",
                "developer",
                LocalDate.of(2021, 4, 24),
                "United Kingdom",
                "Douglas");

        when(userService.getUsersByDateRange(startDate, endDate))
                .thenReturn(List.of(user1, user2));
        mockMvc.perform(get("/api/v1/users")
                        .param("startDate", "2021-03-20")
                        .param("endDate", "2021-04-24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].firstname").value("Di"))
                .andExpect(jsonPath("$[0].dateCreated")
                        .value("2021-03-20"))
                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].firstname")
                        .value("Cassondra"))
                .andExpect(jsonPath("$[1].dateCreated")
                        .value("2021-04-24"));
    }

    @Test
    void getUsersByDateRange_whenNoUsersMatch_returns200AndEmptyList()
            throws Exception {
        LocalDate startDate = LocalDate.of(2030, 1, 1);
        LocalDate endDate = LocalDate.of(2030, 12, 31);

        when(userService.getUsersByDateRange(startDate, endDate))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/v1/users")
                        .param("startDate", "2030-01-01")
                        .param("endDate", "2030-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getUsersByDateRange_whenDateIsInvalid_returns400()
            throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .param("startDate", "ABC")
                        .param("endDate", "2021-12-31"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid value 'ABC' for parameter 'startDate'. Expected type: LocalDate"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/users"));
    }

    @Test
    void searchUsers_whenMatchesExist_returns200AndUsers()
            throws Exception {
        UserResponse user1 = new UserResponse(
                101L,
                "Di",
                "Lauraine",
                "Di.Lauraine@gmail.com",
                "developer",
                LocalDate.of(2021, 3, 20),
                "Virgin Islands, British",
                "Chennai");

        UserResponse user2 = new UserResponse(
                102L,
                "Cassondra",
                "Cadmar",
                "Cassondra.Cadmar@gmail.com",
                "developer",
                LocalDate.of(2021, 4, 24),
                "United Kingdom",
                "Douglas");

        when(userService.searchUsers("developer"))
                .thenReturn(List.of(user1, user2));
        mockMvc.perform(get("/api/v1/users/search")
                        .param("q", "developer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].firstname").value("Di"))
                .andExpect(jsonPath("$[0].profession")
                        .value("developer"))
                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].firstname")
                        .value("Cassondra"))
                .andExpect(jsonPath("$[1].profession")
                        .value("developer"));
    }

    @Test
    void searchUsers_whenNoUsersMatch_returns200AndEmptyList()
            throws Exception {
        when(userService.searchUsers("nonexistent"))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/v1/users/search")
                        .param("q", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}