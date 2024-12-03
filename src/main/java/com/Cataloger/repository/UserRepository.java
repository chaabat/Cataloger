package com.Cataloger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.Cataloger.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}
