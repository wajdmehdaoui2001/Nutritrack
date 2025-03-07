package com.machinestalk.loadTest.services;

import com.machinestalk.loadTest.dto.request.LoginRequest;
import com.machinestalk.loadTest.dto.request.SignUpRequest;
import com.machinestalk.loadTest.dto.response.JwtResponse;
import com.machinestalk.loadTest.dto.response.MessageResponse;
import com.machinestalk.loadTest.entities.Role;
import com.machinestalk.loadTest.entities.User;
import com.machinestalk.loadTest.enums.ERole;
import com.machinestalk.loadTest.exception.UserException;
import com.machinestalk.loadTest.repositories.RoleRepository;
import com.machinestalk.loadTest.repositories.UserRepository;
import com.machinestalk.loadTest.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private  JwtUtils jwtUtils;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private  UserDetailsServiceImpl userDetailsService;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        if (userDetails.getPassword() == null || userDetails.getPassword().isEmpty()) {
            throw new UserException("User found but no password set");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        addJwtCookie(response, jwt);

        return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles));
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserException("Email is already in use!");
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        //Objects.isNull
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new UserException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new UserException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new UserException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new UserException("No user is logged in.");
        }

        removeJwtCookie(response);
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new MessageResponse("You've been signed out!"));
    }

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }
    @Transactional
    public User updateUser(Long id, User updatedUserData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        if (updatedUserData.getUsername() != null) {
            existingUser.setUsername(updatedUserData.getUsername());
        }
        if (updatedUserData.getEmail() != null) {
            existingUser.setEmail(updatedUserData.getEmail());
        }
        if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }
        return userRepository.save(existingUser);
    }



    public ResponseEntity<?> archiveUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setArchived(true); // Mark as archived instead of deleting
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User archived successfully"));
        } else {
            return ResponseEntity.ok("User not found");
        }
    }



    private void addJwtCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);
    }

    private void removeJwtCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
    }
}
