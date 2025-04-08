package com.machinestalk.loadTest.repositories;

import com.machinestalk.loadTest.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByResetToken(String token);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'USER' ORDER BY u.id DESC")
    Page<User> findAllUsersByRoleUser(Pageable pageable);

    @Query("SELECT FUNCTION('MONTH', u.createdAt), COUNT(u) FROM User u GROUP BY FUNCTION('MONTH', u.createdAt)")
    List<Object[]> countUsersByMonth();
}
