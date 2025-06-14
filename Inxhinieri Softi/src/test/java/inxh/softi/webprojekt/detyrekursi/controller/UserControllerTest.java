package inxh.softi.webprojekt.detyrekursi.controller;

import inxh.softi.webprojekt.detyrekursi.controllers.UserController;
import inxh.softi.webprojekt.detyrekursi.entity.User;
import inxh.softi.webprojekt.detyrekursi.models.LoginRequest;
import inxh.softi.webprojekt.detyrekursi.models.UserResponseDTO;
import inxh.softi.webprojekt.detyrekursi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private HttpSession session;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginSuccessful() {
        LoginRequest loginRequest = new LoginRequest("henri34", "password");
        User user = new User();
        user.setRole("USER");
        user.setEmail("henri@gmail.com");
        user.setUsername("henri34");

        when(userService.authenticateUser(loginRequest.getIdentifier(), loginRequest.getPassword())).thenReturn(true);
        when(userService.getUserProfile(loginRequest.getIdentifier())).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody().get("message"));
        assertEquals("USER", response.getBody().get("role"));
        assertEquals("henri@gmail.com", response.getBody().get("email"));

        verify(session).setAttribute("username", "henri34");
        verify(session).setAttribute("role", "USER");
        verify(session).setAttribute("email", "henri@gmail.com");
    }

    @Test
    void loginUnsuccessful() {
        LoginRequest loginRequest = new LoginRequest("henri@gmail.com", "wrongpassword");

        when(userService.authenticateUser(loginRequest.getIdentifier(), loginRequest.getPassword())).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

        assertEquals(401, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Invalid username or password", response.getBody().get("message"));

        verifyNoInteractions(session);
    }

    @Test
    void loginWithException() {
        LoginRequest loginRequest = new LoginRequest("henri34", "password");

        when(userService.authenticateUser(loginRequest.getIdentifier(), loginRequest.getPassword()))
                .thenThrow(new RuntimeException("Authentication failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> userController.login(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void createUser() {
        User user = new User();
        user.setEmail("newhenri@gmail.com");
        UserResponseDTO createdUser = new UserResponseDTO();
        createdUser.setId(1L);

        when(userService.createUser(user)).thenReturn(createdUser);

        ResponseEntity<UserResponseDTO> response = userController.createUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getAllUsers() {
        UserResponseDTO user1 = new UserResponseDTO();
        UserResponseDTO user2 = new UserResponseDTO();
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllUsersEmpty() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void deleteUserSuccessful() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted successfully.", response.getBody());
    }

    @Test
    void deleteUserNotFound() {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> userController.deleteUser(1L));
        assertEquals("User not found", exception.getMessage());
    }
}
