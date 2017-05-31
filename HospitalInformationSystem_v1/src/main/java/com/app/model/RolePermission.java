package com.app.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "role_permission")
public class RolePermission implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Role role;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Permission permission;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	
}
