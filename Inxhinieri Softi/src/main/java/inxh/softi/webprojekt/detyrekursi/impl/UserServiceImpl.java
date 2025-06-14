package inxh.softi.webprojekt.detyrekursi.impl;

import java.util.ArrayList;
import java.util.List;

import inxh.softi.webprojekt.detyrekursi.models.UserResponseDTO;
import inxh.softi.webprojekt.detyrekursi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import inxh.softi.webprojekt.detyrekursi.entity.User;
import inxh.softi.webprojekt.detyrekursi.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password do not match.");
        }

        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return convertToUserResponseDTO(savedUser);
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            throw new IllegalArgumentException("Password must be at least 6 characters long, contain one uppercase letter, " + "one lowercase letter, one digit, and one special character.");
        }
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return convertToUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> responseDTOs = new ArrayList<>();
        for (User user : users) {
            responseDTOs.add(convertToUserResponseDTO(user));
        }
        return responseDTOs;
    }

    @Override
    public UserResponseDTO updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            if (!userDetails.getPassword().equals(userDetails.getConfirmPassword())) {
                throw new IllegalArgumentException("Password and Confirm Password do not match.");
            }
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        return convertToUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean authenticateUser(String identifier, String password) {
        User user;
        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier);
        } else {
            user = userRepository.findByUsername(identifier);
        }
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public User getUserProfile(String identifier) {
        User user;

        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier);
        } else {
            user = userRepository.findByUsername(identifier);
        }

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    @Override
    public boolean doesUserExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
