package com.triploguebe.user.repository;

import com.triploguebe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { //<해당 레포지터리가 해당하는 엔티티, User 엔티티의 id값의 타입>
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

