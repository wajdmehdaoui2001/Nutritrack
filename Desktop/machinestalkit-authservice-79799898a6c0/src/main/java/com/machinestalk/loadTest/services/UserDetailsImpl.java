package com.machinestalk.loadTest.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machinestalk.loadTest.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;


    @Getter
    private Long id;
    @Getter
    private String username;
    @Getter
    private String email;
    @JsonIgnore
    @Getter
    private String password;
    @Getter
    private  String profileimage ;
    @Getter
    private Integer phonenumber ;

    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(Long id, String username, String email,
                           @NotBlank @Size(max = 120) String password,
                           Collection<? extends GrantedAuthority> authorities
            , Integer phonenumber, String profileimage   ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.phonenumber = phonenumber;
        this.profileimage = profileimage;
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
                authorities ,
                user.getPhonenumber(),
                user.getProfileimage()

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
    public Long getId() {
        return id;
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

    public String getProfileimage() {
        return profileimage;
    }

    public Integer getPhonenumber() {
        return phonenumber;
    }
}
