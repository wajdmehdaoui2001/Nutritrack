package com.machinestalk.loadTest.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.mail.javamail.MimeMessageHelper;

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
    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserException("Email is already in use!");
        }

        String generatedPassword = generateRandomPassword();
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(generatedPassword));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
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

        String resetToken = generateResetToken();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        user.setProfileimage("https://res.cloudinary.com/dv0d8yhvc/image/upload/v1742288310/userlogo_z3xvwm.png");
        userRepository.save(user);

        String resetLink = "http://localhost:3000/resetpassword?token=" + resetToken;
        emailService.sendWelcomeEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @Autowired
    private  UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CloudinaryService cloudinaryService ;

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

        return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles,userDetailsImpl.getPhonenumber() , userDetailsImpl.getProfileimage()) );
    }
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
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
    public User updateUser(Long id, User updatedUserData, MultipartFile file) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        if (updatedUserData.getUsername() != null) {
            existingUser.setUsername(updatedUserData.getUsername());
        }
        if (updatedUserData.getEmail() != null) {
            existingUser.setEmail(updatedUserData.getEmail());
        }
        if (updatedUserData.getPhonenumber() != null) {
            existingUser.setPhonenumber(updatedUserData.getPhonenumber());
        }
        if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }
        if (file != null && !file.isEmpty()) {
            try {
                if (existingUser.getProfileimage() != null) {
                    cloudinaryService.deleteImage(existingUser.getProfileimage());
                }
                String imageUrl = cloudinaryService.uploadImage(file);
                existingUser.setProfileimage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload new image", e);
            }
        }

        return userRepository.save(existingUser);
    }

    public ResponseEntity<MessageResponse> archiveUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    boolean currentStatus = user.getArchived();
                    user.setArchived(!currentStatus); // Toggle the archived status
                    userRepository.save(user);
                    String statusMessage = currentStatus
                            ? "User unarchived successfully"
                            : "User archived successfully";

                    return ResponseEntity.ok(new MessageResponse(statusMessage));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("User not found")));
    }

    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return userRepository.findAllUsersByRoleUser(pageable);
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
    @Autowired
    private EmailService emailService;

    // Generate random token
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String token = generateResetToken();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String resetLink = "http://localhost:3000/resetpassword?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }


    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new UserException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new UserException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
    public List<Map<String, Object>> getUserRegistrations() {
        List<Object[]> results = userRepository.countUsersByMonth();
        List<Map<String, Object>> data = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("month", result[0]);  // Month (1-12)
            entry.put("count", result[1]);  // Number of users
            data.add(entry);
        }

        return data;
    }
}
