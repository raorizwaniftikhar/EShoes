package com.java.shoes.repository;

import org.springframework.data.repository.CrudRepository;

import com.java.shoes.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByName(String name);

}
