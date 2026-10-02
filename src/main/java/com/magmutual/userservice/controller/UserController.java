package com.magmutual.userservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.magmutual.userservice.dto.UserResponse;
import com.magmutual.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;

@Tag(
        name = "Users",
        description = "Operations for retrieving and searching users"
)
@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a specific user by their unique ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable @Positive Long id) {

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get users by creation date range",
            description = "Returns users created between the specified start and end dates."
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsersByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<UserResponse> users =
                userService.getUsersByDateRange(startDate, endDate);

        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Search users",
            description = "Searches users by first name, last name, email, profession, country, or city."
    )
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam String q) {

        List<UserResponse> users =
                userService.searchUsers(q);

        return ResponseEntity.ok(users);
    }
}