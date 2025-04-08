package com.machinestalk.loadTest.services;

import com.machinestalk.loadTest.entities.Role;
import com.machinestalk.loadTest.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl  implements  RoleService{
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Role createRole(Role role) {
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()){
            throw new RuntimeException("Role already exists with the name :" + role.getName());
        }
        Role createdRole =new Role();
        createdRole.setName(role.getName());
        return roleRepository.save(createdRole);
    }

    @Override
    public Role findRoleById(Long id) throws Exception {
        Optional<Role> r=roleRepository.findById(id);
        if(r.isPresent()){
            return r.get();
        }
        throw new Exception("role not found with this ID ::"+ id);
    }

    @Override
    public void deleteRole(Long id) throws Exception {
        Optional<Role> role= roleRepository.findById(id);
        if(role.isPresent()) {
            roleRepository.deleteById(id);
        }
        throw new Exception("Role not found with this ID ::"+id);
    }

    @Override
    public Role updateRole(Role role, Long Id) throws Exception {
        Role oldRole=findRoleById(Id);
        if(oldRole.getName() != null){
            oldRole.setName(role.getName());
        }
        return roleRepository.save(oldRole);

    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }
}
