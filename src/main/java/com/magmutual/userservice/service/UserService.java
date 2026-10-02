package com.magmutual.userservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magmutual.userservice.dto.UserResponse;
import com.magmutual.userservice.exception.UserNotFoundException;
import com.magmutual.userservice.model.User;
import com.magmutual.userservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date must be on or before end date");
        }

        return userRepository
                .findByDateCreatedBetweenOrderByDateCreatedAsc(
                        startDate, endDate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String query) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query must not be blank");
        }

        return userRepository.searchUsers(query.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getProfession(),
                user.getDateCreated(),
                user.getCountry(),
                user.getCity());
    }
}