package com.Cataloger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Cataloger.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}
