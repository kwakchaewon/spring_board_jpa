package com.example.bootboard.user;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser,Long> {
    Optional<SiteUser> findByUsername(String username);
}
