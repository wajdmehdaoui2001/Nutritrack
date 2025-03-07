package com.machinestalk.loadTest.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "users" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private  Long id;

    public Long getId() {
        return id;
    }
    private Boolean isArchived = false;

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public @NotBlank @Size(max = 20) String getUsername() {
        return username;
    }

    public @NotBlank @Email @Size(max = 50) String getEmail() {
        return email;
    }

    public @NotBlank @Size(max = 120) String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User() {}
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(@NotBlank @Size(max = 120) String password) {
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

    }


    public void setUsername(@NotBlank @Size(max = 20) String username) {
        this.username = username;
    }

    public void setEmail(@NotBlank @Email @Size(max = 50) String email) {
        this.email = email;
    }

    public User(String username, String email, Set<Role> roles, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
