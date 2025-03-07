package com.machinestalk.loadTest.controllers;

import com.machinestalk.loadTest.entities.Role;
import com.machinestalk.loadTest.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a role by its ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
        try {
            Role role = roleService.findRoleById(id);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Role not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing role
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@RequestBody Role role, @PathVariable("id") Long id) {
        try {
            Role updatedRole = roleService.updateRole(role, id);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating role: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete a role by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id) {
        try {

            roleService.deleteRole(id);
            return new ResponseEntity<>("Role deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting role: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAllRole();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
