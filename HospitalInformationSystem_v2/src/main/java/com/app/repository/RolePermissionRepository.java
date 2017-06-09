package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.RolePermission;



public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

}
