package inxh.softi.webprojekt.detyrekursi.repository;

import inxh.softi.webprojekt.detyrekursi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection= EmbeddedDatabaseConnection.H2) //databaze embedded vetem per testim
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUserSuccessfully() {

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > 0);
    }

    @Test
    public void shouldReturnAllSavedUsers() {

        User user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .build();

        User user2 = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("janesmith")
                .email("janesmith@example.com")
                .password("anothersecurepassword")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void shouldFindUserById() {

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .build();

        User savedUser = userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        assertTrue(retrievedUser.isPresent());
        assertEquals("johndoe@example.com", retrievedUser.get().getEmail());
    }

    @Test
    public void shouldUpdateUserDetails() {

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .build();

        User savedUser = userRepository.save(user);

        savedUser.setFirstName("Jonathan");
        savedUser.setEmail("jonathandoe@example.com");
        User updatedUser = userRepository.save(savedUser);

        assertNotNull(updatedUser);
        assertEquals("Jonathan", updatedUser.getFirstName());
        assertEquals("jonathandoe@example.com", updatedUser.getEmail());
    }

    @Test
    public void shouldDeleteUserById() {

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .build();

        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        assertTrue(deletedUser.isEmpty());
    }


    @Test
    public void shouldFailToFindNonExistentUserById() {
        Optional<User> user = userRepository.findById(999L);
        assertTrue(user.isEmpty(), "Expected user not to be found with ID 999");
    }

    @Test
    public void shouldHandleDeletionOfNonExistentUser() {
        Optional<User> user = userRepository.findById(999L);
        assertTrue(user.isEmpty(), "Expected no user to be found with ID 999");

        userRepository.deleteById(999L);

        assertDoesNotThrow(() -> userRepository.deleteById(999L),
                "Expected no exception when deleting a non-existent user");
    }

    @Test
    public void shouldFailToSaveUserWithNullFields() {
        User user = User.builder()
                .firstName(null)
                .lastName("Doe")
                .username(null)
                .email("johndoe@example.com")
                .password("securepassword")
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            userRepository.save(user);
        });

        assertNotNull(exception, "Expected an exception when saving a user with null fields");
    }

    @Test
    public void shouldReturnEmptyListWhenNoUsersExist() {
        userRepository.deleteAll();
        assertTrue(userRepository.findAll().isEmpty(), "Expected empty list when no users exist");
    }

    @Test
    public void shouldFailToUpdateNonExistentUser() {
        User user = User.builder()
                .id(999L)
                .firstName("NonExistent")
                .lastName("User")
                .username("nonexistent")
                .email("nonexistent@example.com")
                .password("password")
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            userRepository.save(user);
        });

        assertNotNull(exception, "Expected an exception when updating a non-existent user");
    }

}