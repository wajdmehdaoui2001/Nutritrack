package com.machinestalk.loadTest.controllers;

import com.machinestalk.loadTest.entities.User;
import com.machinestalk.loadTest.dto.request.LoginRequest;
import com.machinestalk.loadTest.dto.request.SignUpRequest;
import com.machinestalk.loadTest.dto.response.JwtResponse;
import com.machinestalk.loadTest.dto.response.MessageResponse;
import com.machinestalk.loadTest.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="Authentication", description = "Endpoints for user authentication and management")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    @Operation(summary = "User Signin", description = "Authenticate user and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request format"),

            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Invalid Credentials",
                                            description = "When the user provides an incorrect email or password.",
                                            value = """
                        {
                            "message": "Invalid username or password",
                            "status": 401
                        }
                        """
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "User Archived",
                                            description = "When the user account is archived and cannot sign in.",
                                            value = """
                        {
                            "message": "Your account has been archived.",
                            "status": 401
                        }
                        """
                                    )
                            }
                    ))
    })
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.authenticateUser(loginRequest, response);
    }


    @PostMapping("/signup")
    @Operation(summary = "Create User", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or user already exists")
    })
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PostMapping("/signout")
    @Operation(summary = "Signout User", description = "Sign out the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed out successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "401", description = "No user is logged in")
    })
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        return authService.logoutUser(response);
    }

    @GetMapping("/details/{id}")
    @Operation(summary = "Get User Details", description = "Retrieve details of a specific user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Optional<User> findUser(@PathVariable Long id) {
        return authService.findUser(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Archive User", description = "Archive an existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> archiveUser(@PathVariable("id") Long id){
        return authService.archiveUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User", description = "Update an existing user's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUserData
    ) {
        User updatedUser = authService.updateUser(id, updatedUserData);
        return ResponseEntity.ok(updatedUser);
    }
}
