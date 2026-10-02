package com.magmutual.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.magmutual.userservice.dto.UserResponse;
import com.magmutual.userservice.exception.UserNotFoundException;
import com.magmutual.userservice.model.User;
import com.magmutual.userservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_whenUserExists_returnsUserResponse() {

        // Arrange
        User user = new User(
                100L,
                "Andree",
                "Flita",
                "Andree.Flita@gmail.com",
                "worker",
                LocalDate.of(2020, 8, 31),
                "Wallis and Futuna",
                "Nanjing");

        when(userRepository.findById(100L))
                .thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.getUserById(100L);

        // Assert
        assertEquals(100L, response.id());
        assertEquals("Andree", response.firstname());
        assertEquals("Flita", response.lastname());
        assertEquals("Andree.Flita@gmail.com", response.email());
        assertEquals("worker", response.profession());
        assertEquals(
                LocalDate.of(2020, 8, 31),
                response.dateCreated());
        assertEquals("Wallis and Futuna", response.country());
        assertEquals("Nanjing", response.city());
    }

    @Test
    void getUserById_whenUserDoesNotExist_throwsUserNotFoundException() {

        // Arrange
        when(userRepository.findById(999999L))
                .thenReturn(Optional.empty());

        // Act
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(999999L));

        // Assert
        assertEquals(
                "User with ID 999999 was not found",
                exception.getMessage());

        verify(userRepository).findById(999999L);
    }

    @Test
    void getUsersByDateRange_whenRangeIsValid_returnsUsers() {

        // Arrange
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 12, 31);

        User user1 = new User(
                101L,
                "Di",
                "Lauraine",
                "Di.Lauraine@gmail.com",
                "developer",
                LocalDate.of(2021, 3, 20),
                "Virgin Islands, British",
                "Chennai");

        User user2 = new User(
                102L,
                "Cassondra",
                "Cadmar",
                "Cassondra.Cadmar@gmail.com",
                "developer",
                LocalDate.of(2021, 4, 24),
                "United Kingdom",
                "Douglas");

        when(userRepository.findByDateCreatedBetweenOrderByDateCreatedAsc(
                startDate, endDate))
                .thenReturn(List.of(user1, user2));

        // Act
        List<UserResponse> results =
                userService.getUsersByDateRange(startDate, endDate);

        // Assert
        assertEquals(2, results.size());

        assertEquals(101L, results.get(0).id());
        assertEquals("Di", results.get(0).firstname());
        assertEquals(
                LocalDate.of(2021, 3, 20),
                results.get(0).dateCreated());

        assertEquals(102L, results.get(1).id());
        assertEquals("Cassondra", results.get(1).firstname());
        assertEquals(
                LocalDate.of(2021, 4, 24),
                results.get(1).dateCreated());

        verify(userRepository)
                .findByDateCreatedBetweenOrderByDateCreatedAsc(
                        startDate, endDate);
    }

    @Test
    void getUsersByDateRange_whenStartDateIsAfterEndDate_throwsIllegalArgumentException() {

        // Arrange
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 1);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUsersByDateRange(
                        startDate, endDate));

        // Assert
        assertEquals(
                "Start date must be on or before end date",
                exception.getMessage());

        verify(userRepository, never())
                .findByDateCreatedBetweenOrderByDateCreatedAsc(
                        startDate, endDate);
    }

    @Test
    void getUsersByDateRange_whenNoUsersMatch_returnsEmptyList() {

        // Arrange
        LocalDate startDate = LocalDate.of(2030, 1, 1);
        LocalDate endDate = LocalDate.of(2030, 12, 31);

        when(userRepository.findByDateCreatedBetweenOrderByDateCreatedAsc(
                startDate, endDate))
                .thenReturn(List.of());

        // Act
        List<UserResponse> results =
                userService.getUsersByDateRange(startDate, endDate);

        // Assert
        assertEquals(0, results.size());

        verify(userRepository)
                .findByDateCreatedBetweenOrderByDateCreatedAsc(
                        startDate, endDate);
    }

    @Test
    void searchUsers_whenMatchesExist_returnsUsers() {

        // Arrange
        User user1 = new User(
                101L,
                "Di",
                "Lauraine",
                "Di.Lauraine@gmail.com",
                "developer",
                LocalDate.of(2021, 3, 20),
                "Virgin Islands, British",
                "Chennai");

        User user2 = new User(
                102L,
                "Cassondra",
                "Cadmar",
                "Cassondra.Cadmar@gmail.com",
                "developer",
                LocalDate.of(2021, 4, 24),
                "United Kingdom",
                "Douglas");

        when(userRepository.searchUsers("developer"))
                .thenReturn(List.of(user1, user2));

        // Act
        List<UserResponse> results =
                userService.searchUsers("developer");

        // Assert
        assertEquals(2, results.size());

        assertEquals(101L, results.get(0).id());
        assertEquals("Di", results.get(0).firstname());
        assertEquals("developer", results.get(0).profession());

        assertEquals(102L, results.get(1).id());
        assertEquals("Cassondra", results.get(1).firstname());
        assertEquals("developer", results.get(1).profession());

        verify(userRepository).searchUsers("developer");
    }

    @Test
    void searchUsers_whenNoUsersMatch_returnsEmptyList() {

        // Arrange
        when(userRepository.searchUsers("nonexistent"))
                .thenReturn(List.of());

        // Act
        List<UserResponse> results =
                userService.searchUsers("nonexistent");

        // Assert
        assertEquals(0, results.size());

        verify(userRepository).searchUsers("nonexistent");
    }

    @Test
    void searchUsers_whenQueryIsBlank_throwsIllegalArgumentException() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.searchUsers("   "));

        // Assert
        assertEquals(
                "Search query must not be blank",
                exception.getMessage());

        verify(userRepository, never())
                .searchUsers(org.mockito.ArgumentMatchers.anyString());
    }
}