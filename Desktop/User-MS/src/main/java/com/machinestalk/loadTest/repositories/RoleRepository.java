package com.machinestalk.loadTest.repositories;

import com.machinestalk.loadTest.entities.Role;
import com.machinestalk.loadTest.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);

}
