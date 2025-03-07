package com.machinestalk.loadTest.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Response containing JWT token and user details")
public class JwtResponse {
    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    @Schema(description = "Token type (usually 'Bearer')", example = "Bearer")
    private String type = "Bearer";
    @Schema(description = "User ID", example = "13")
    private Long id;
    @NotBlank
    @Schema(description = "Username", example = "testest")
    private String username;
    @Schema(description = "Email address", example = "Louutt@gmail.com")
    private String email;
    @Schema(description = "Roles assigned to the user", example = "[\"USER\"]")
    private List<String> roles;


    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}