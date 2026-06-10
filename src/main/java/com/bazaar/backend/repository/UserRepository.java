package com.bazaar.backend.repository;
import com.bazaar.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method username se user dhoondne ke liye (Interview favourite)
    Optional<User> findByUsername(String username);

    // Custom query method email se check karne ke liye
    Optional<User> findByEmail(String email);

    // Check karne ke liye ki username pehle se exists karta hai ya nahi
    Boolean existsByUsername(String username);

    // Check karne ke liye ki email pehle se registered hai ya nahi
    Boolean existsByEmail(String email);
}

