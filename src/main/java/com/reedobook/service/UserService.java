
package com.reedobook.service;

import com.reedobook.entity.Role;
import com.reedobook.entity.User;
import com.reedobook.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for repository and password encoder
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param user The User object to be registered.
     * @return The saved User object.
     * @throws RuntimeException if the email is already registered.
     */
    @Transactional
    public User registerUser(User user) {

        System.out.println(
                "Attempting to register user: " + user.getEmail());

        // Check if a user with the given email already exists
        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            System.out.println(
                    "Email already registered: " + user.getEmail());

            throw new RuntimeException("Email already registered");
        }

        // Encode the password before storing it in the database
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        // Public registration must never assign ADMIN or TEACHER
        user.setRole(Role.STUDENT);

        // Save the new user to the database
        User savedUser = userRepository.save(user);

        System.out.println(
                "User successfully saved with ID: " + savedUser.getId());

        return savedUser;
    }

    /**
     * Authenticates a user based on email and password.
     *
     * @param email The user's email address.
     * @param password The user's raw password.
     * @return The authenticated User object.
     * @throws RuntimeException if the user is not found
     *         or the password is invalid.
     *
     * NOTE: JWT login is handled by UserController using
     * AuthenticationManager. This method is retained for
     * compatibility with existing code but is not used by
     * the new JWT login endpoint.
     */
    public User loginUser(String email, String password) {

        System.out.println(
                "Login requested for email: " + email);

        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        // Compare the raw password with the stored BCrypt hash
        if (!passwordEncoder.matches(
                password, user.getPassword())) {

            System.out.println(
                    "Invalid password for email: " + email);

            throw new RuntimeException("Invalid email or password");
        }

        System.out.println(
                "Login successful for email: " + email);

        return user;
    }

    /**
     * Finds a user using their database ID.
     *
     * @param userId The ID of the user.
     * @return The user if found.
     * @throws RuntimeException if the user does not exist.
     */
    public User findUserByUserId(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return user.get();
    }

    /**
     * Retrieves all registered users.
     *
     * @return A list of registered users.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}
