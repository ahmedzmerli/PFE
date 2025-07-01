package com.example.GestionUser.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permission_list")
public class PermissionList {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name; // ex: PL1, AdminPL

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    @ManyToMany(mappedBy = "permissionLists")
    @JsonIgnore
    private List<Role> roles;

    public PermissionList() {
    }

    public PermissionList(Integer id, String name, Set<Permission> permissions, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.roles = roles;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
