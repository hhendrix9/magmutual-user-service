package com.magmutual.userservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.magmutual.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDateCreatedBetweenOrderByDateCreatedAsc(
            LocalDate startDate,
            LocalDate endDate);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(u.profession) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(u.country) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(u.city) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY u.id ASC
            """)
    List<User> searchUsers(@Param("query") String query);
}