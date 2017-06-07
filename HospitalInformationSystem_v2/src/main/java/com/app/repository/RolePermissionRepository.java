package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.RolePermission;



public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

}
