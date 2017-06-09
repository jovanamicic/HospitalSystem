package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.RoleMember;

public interface RoleMemberRepository extends JpaRepository<RoleMember, Integer>{
	
}
