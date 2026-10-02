package com.magmutual.userservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserById_whenIdIsNegative_returns400() throws Exception {

        mockMvc.perform(get("/api/v1/users/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("User ID must be greater than 0"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/users/-1"));
    }
    
    @Test
    void getUsersByDateRange_whenRangeIsValid_returnsUsers()
            throws Exception {

        mockMvc.perform(get("/api/v1/users")
                        .param("startDate", "2021-03-20")
                        .param("endDate", "2021-04-24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].firstname").value("Di"))
                .andExpect(jsonPath("$[0].dateCreated")
                        .value("2021-03-20"));
    }
    
    @Test
    void getUsersByDateRange_whenStartDateIsAfterEndDate_returns400()
            throws Exception {

        mockMvc.perform(get("/api/v1/users")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2021-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Start date must be on or before end date"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/users"));
    }
    
    @Test
    void searchUsers_whenQueryMatches_returnsUsers()
            throws Exception {

        mockMvc.perform(get("/api/v1/users/search")
                        .param("q", "Chennai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].city").value("Chennai"))
                .andExpect(jsonPath("$[1].id").value(1012))
                .andExpect(jsonPath("$[1].city").value("Chennai"));
    }
    
}