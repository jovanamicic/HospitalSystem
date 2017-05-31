package com.app.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<RolePermission> rolePermissions = new HashSet<>();
	

	public Permission() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<RolePermission> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(Set<RolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

}
