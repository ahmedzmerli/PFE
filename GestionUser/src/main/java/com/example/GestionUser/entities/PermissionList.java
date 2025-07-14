package com.example.GestionUser.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permission_list")
//@Table(name = "perm_list_dimii", schema = "ccadmin")
public class PermissionList {

    @Id
    @GeneratedValue
    //@Id
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perm_list_seq")
    //    @SequenceGenerator(name = "perm_list_seq", sequenceName = "ccadmin.seq_perm_list_dimii", allocationSize = 1)
    private Integer id;

    @Column(name = "list_name", unique = true, nullable = false)
    private String name; // ex: PL1, AdminPL

    @ManyToMany(fetch = FetchType.EAGER)
    //@JoinTable(
    //        name = "permlist_perm_dimii",
    //        joinColumns = @JoinColumn(name = "perm_list_id"),
    //        inverseJoinColumns = @JoinColumn(name = "perm_id")
    //    )
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
