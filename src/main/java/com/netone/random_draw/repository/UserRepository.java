package com.netone.random_draw.repository;

import com.netone.random_draw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndActiveStatus(String username, boolean activeStatus);
    List<User> findByActiveStatus(boolean activeStatus);
}
