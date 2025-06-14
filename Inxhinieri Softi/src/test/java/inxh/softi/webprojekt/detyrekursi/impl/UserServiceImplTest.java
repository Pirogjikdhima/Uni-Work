package inxh.softi.webprojekt.detyrekursi.impl;

import inxh.softi.webprojekt.detyrekursi.entity.User;
import inxh.softi.webprojekt.detyrekursi.models.UserResponseDTO;
import inxh.softi.webprojekt.detyrekursi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setEmail("henridauti@gmail.com");
        user.setUsername("henridauti");
        user.setPassword("Test123!");
        user.setConfirmPassword("Test123!");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("EncodedTest123!");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(user);

        assertNotNull(response);
        assertEquals("henridauti@gmail.com", response.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsTaken() {
        User user = new User();
        user.setEmail("henridauti@gmail.com");
        user.setUsername("henridauti");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Email is already taken.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenUsernameIsTaken() {
        User user = new User();
        user.setEmail("henridauti@gmail.com");
        user.setUsername("henridauti");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Username is already taken.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        User user = new User();
        user.setEmail("henridauti@gmail.com");
        user.setUsername("henridauti");
        user.setPassword("Test123!");
        user.setConfirmPassword("WrongPass123!");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Password and Confirm Password do not match.", exception.getMessage());
    }

    @Test
    public void shouldGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setEmail("henridauti@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("henridauti@gmail.com", response.getEmail());
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void shouldUpdateUserSuccessfully() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("EncodedPassword");

        User updatedUser = new User();
        updatedUser.setFirstName("Henri");
        updatedUser.setLastName("Dauti");
        updatedUser.setEmail("henridauti@gmail.com");
        updatedUser.setPassword("Test123!");
        updatedUser.setConfirmPassword("Test123!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("EncodedTest123!");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDTO response = userService.updateUser(1L, updatedUser);

        assertNotNull(response);
        assertEquals("henridauti@gmail.com", response.getEmail());
    }

    @Test
    public void shouldDeleteUserSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentUser() {
        when(userRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void shouldAuthenticateUserSuccessfully() {
        User user = new User();
        user.setEmail("henridauti@gmail.com");
        user.setPassword("EncodedTest123!");

        when(userRepository.findByEmail("henridauti@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("Test123!", "EncodedTest123!")).thenReturn(true);

        boolean isAuthenticated = userService.authenticateUser("henridauti@gmail.com", "Test123!");

        assertTrue(isAuthenticated);
    }

    @Test
    public void shouldReturnFalseWhenAuthenticationFails() {
        when(userRepository.findByEmail("henridauti@gmail.com")).thenReturn(null);

        boolean isAuthenticated = userService.authenticateUser("henridauti@gmail.com", "Test123!");

        assertFalse(isAuthenticated);
    }

    @Test
    public void shouldGetUserProfileByEmail() {
        User user = new User();
        user.setEmail("henridauti@gmail.com");
        user.setUsername("henridauti");

        when(userRepository.findByEmail("henridauti@gmail.com")).thenReturn(user);

        User retrievedUser = userService.getUserProfile("henridauti@gmail.com");

        assertNotNull(retrievedUser);
        assertEquals("henridauti@gmail.com", retrievedUser.getEmail());
    }

    @Test
    public void shouldReturnTrueIfUserExistsByUsername() {
        when(userRepository.findByUsername("henridauti")).thenReturn(new User());

        boolean exists = userService.doesUserExists("henridauti");

        assertTrue(exists);
    }

    @Test
    public void shouldReturnFalseIfUserDoesNotExistByUsername() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        boolean exists = userService.doesUserExists("nonexistentuser");

        assertFalse(exists);
    }
}
