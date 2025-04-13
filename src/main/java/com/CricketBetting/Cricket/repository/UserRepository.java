package com.CricketBetting.Cricket.repository;

import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);

}