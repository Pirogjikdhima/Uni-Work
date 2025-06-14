package inxh.softi.webprojekt.detyrekursi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import inxh.softi.webprojekt.detyrekursi.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByEmail(String identifier);
}
