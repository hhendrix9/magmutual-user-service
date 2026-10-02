package com.magmutual.userservice.dto;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String firstname,
        String lastname,
        String email,
        String profession,
        LocalDate dateCreated,
        String country,
        String city) {
}