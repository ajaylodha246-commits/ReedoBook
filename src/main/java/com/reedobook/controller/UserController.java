
package com.reedobook.controller;

import com.reedobook.dto.AuthResponse;
import com.reedobook.entity.Role;
import com.reedobook.entity.User;
import com.reedobook.repository.UserRepository;
import com.reedobook.security.JwtService;
import com.reedobook.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for user-related operations:
 * registration, JWT login, logout, and user retrieval.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    // Constructor injection for required services
    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user.
     *
     * @param user The user details supplied by the frontend.
     * @return Registration result.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody User user) {

        try {
            // Register the user and encode the password
            userService.registerUser(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful");

        } catch (RuntimeException e) {

            // Return a safe error message
            if ("Email already registered".equals(e.getMessage())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Email already registered");
            }

            return ResponseEntity
                    .badRequest()
                    .body("Registration failed");
        }
    }

    /**
     * Logs in a user and generates a JWT access token.
     *
     * @param loginRequest Contains the user's email and password.
     * @return JWT token if authentication succeeds.
     */

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerTeacher")
    public ResponseEntity<?> registerTeacher(
            @RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        // Automatically assign teacher role
        user.setRole(Role.TEACHER);

        // Hash the password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        User savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Teacher registered successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(
//            @RequestBody User loginRequest) {
//
//        try {
//            // Authenticate the email and password using Spring Security
//            var authentication =
//                    authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(
//                                    loginRequest.getEmail(),
//                                    loginRequest.getPassword()
//                            )
//                    );
//
//            // Get the authenticated Spring Security user
//            UserDetails userDetails =
//                    (UserDetails) authentication.getPrincipal();
//
//            // Generate a signed JWT access token
//            String token =
//                    jwtService.generateToken(userDetails);
//
//            User user = userService.findUserByEmail(loginRequest.getEmail());
//
//            // Return the token instead of the User entity
//            return ResponseEntity.ok(
//                    new AuthResponse(token, "Bearer",
//                            user.getId(),
//                            user.getName(),
//                            user.getEmail(),
//                            user.getRole().name()));
//
//        } catch (AuthenticationException e) {
//
//            // Invalid credentials return HTTP 401
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email or password");
//        }
//    }



    @PostMapping("/login")
    public ResponseEntity<?> studentLogin(
            @RequestBody User loginRequest) {

        return loginWithAllowedRoles(
                loginRequest, List.of("STUDENT")
        );
    }

    @PostMapping("/staff/login")
    public ResponseEntity<?> staffLogin(
            @RequestBody User loginRequest) {

        return loginWithAllowedRoles(
                loginRequest, List.of("TEACHER", "ADMIN")
        );
    }


    private ResponseEntity<?> loginWithAllowedRoles(
            User loginRequest,
            List<String> allowedRoles) {

        try {
            var authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

            User user = userService.findUserByEmail(
                    loginRequest.getEmail()
            );

            if (user == null) {
                return ResponseEntity.status(401)
                        .body("Invalid email or password");
            }

            String role = String.valueOf(user.getRole());

            if (!allowedRoles.contains(role)) {
                return ResponseEntity.status(403)
                        .body("You are not allowed to use this login");
            }

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    new AuthResponse(
                            token,
                            "Bearer",
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            role
                    )
            );

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }
    }


    /**
     * Logs out the user on the frontend.
     *
     * @param request The incoming HTTP request.
     * @return Logout result.
     *
     * NOTE: With stateless JWT authentication, invalidating an
     * HTTP session does not revoke an issued JWT. The frontend
     * should discard its token. The token remains valid until
     * expiration unless token revocation is implemented.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request) {

        return ResponseEntity.ok(
                "Logout successful. Please remove the JWT from the frontend.");
    }

    /**
     * Finds a registered user by ID.
     *
     * @param userId The user's database ID.
     * @return The user if found.
     */
    @GetMapping("/getUserByUserId/{userId}")
    public ResponseEntity<?> getUserByUserId(
            @PathVariable Long userId) {

        try {
            User user = userService.findUserByUserId(userId);

            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

    /**
     * Retrieves all registered users.
     *
     * @return The registered user list.
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/getAllRegisteredUsers")
    public ResponseEntity<List<User>> getAllRegisteredUsers() {

        List<User> allUsers = userService.findAllUsers();

        if (allUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allUsers);
    }
}
