package com.nutritTrack.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotNull
    private Double poids;  // Weight in kg (example)

    @NotNull
    private Double taille; // Height in meters (example)

    @NotNull
    private Integer age;   // Age in years

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User() {}


    public User(String username, String email, String password, Double poids, Double taille, Integer age) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
    }


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public User(String username, String email, Double poids, Double taille, Integer age, Set<Role> roles, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.roles = roles;
    }

}
