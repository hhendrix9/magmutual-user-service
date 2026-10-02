package com.magmutual.userservice.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String profession;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    protected User() {
    }

    public User(
            Long id,
            String firstname,
            String lastname,
            String email,
            String profession,
            LocalDate dateCreated,
            String country,
            String city) {

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.profession = profession;
        this.dateCreated = dateCreated;
        this.country = country;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfession() {
        return profession;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}