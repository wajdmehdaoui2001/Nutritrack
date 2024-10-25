package com.nutritTrack.project.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nutritTrack.project.entities.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;


    @Getter
    private Long id;
    private String username;
    private String email;
    @Setter
    @Getter
    private Double poids;
    @Getter
    @Setter
    private Double taille;
    @Setter
    @Getter
    private Integer age;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(Long id, String username, String email,
                           @NotBlank @Size(max = 120) String password, Collection<? extends GrantedAuthority> authorities,
                           Double poids, Double taille, Integer age) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password=password;
        this.authorities = authorities;
        this.poids = poids;
        this.age = age;
        this.taille = taille;
    }

    // Constructeur pour construire UserDetailsImpl à partir de l'entité User
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getPoids(),
                user.getTaille(),
                user.getAge()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public String getEmail() {
        return email;
    }


}
