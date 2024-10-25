package com.nutritTrack.project.repositories;

import com.nutritTrack.project.entities.Role;
import com.nutritTrack.project.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

