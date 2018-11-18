package com.szakdolg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.szakdolg.entity.Role;
import com.szakdolg.repository.RoleRepository;

@Service
public class RoleService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private RoleRepository roleRepo;
	@Autowired
	public RoleService(RoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}

	public Role findByRole (String role) {
		log.debug("findByRole");
		log.debug(role);
		return roleRepo.findByRole(role);
	}

}
