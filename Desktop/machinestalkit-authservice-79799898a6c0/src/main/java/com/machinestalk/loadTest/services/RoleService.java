package com.machinestalk.loadTest.services;

import com.machinestalk.loadTest.entities.Role;

import java.util.List;

public interface RoleService {
    public Role createRole(Role role);
    public Role findRoleById(Long id) throws Exception ;
    public void deleteRole(Long id) throws Exception ;
    public Role updateRole(Role recipe,Long Id) throws Exception ;

    public List<Role> findAllRole();
}
