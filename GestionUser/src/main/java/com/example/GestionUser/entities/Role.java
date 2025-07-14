package com.example.GestionUser.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
//@Table(name = "role_dimii", schema = "ccadmin")
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue
    //@Id
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    //    @SequenceGenerator(name = "role_seq", sequenceName = "ccadmin.seq_role_dimii", allocationSize = 1)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> user;

    @ManyToMany(fetch = FetchType.EAGER)
    //@JoinTable(
    //        name = "role_perm_list_dimii",
    //        joinColumns = @JoinColumn(name = "role_id"),
    //        inverseJoinColumns = @JoinColumn(name = "perm_list_id")
    //    )
    @JoinTable(
            name = "role_permission_list",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_lists_id")
    )
    private List<PermissionList> permissionLists;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime created_date;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime last_modified_date;

    public Role() {
    }

    public Role(Integer id, String name, Set<User> user, List<PermissionList> permissionLists,
                LocalDateTime created_date, LocalDateTime last_modified_date) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.permissionLists = permissionLists;
        this.created_date = created_date;
        this.last_modified_date = last_modified_date;
    }

    // Getters & Setters

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

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }

    public List<PermissionList> getPermissionLists() {
        return permissionLists;
    }

    public void setPermissionLists(List<PermissionList> permissionLists) {
        this.permissionLists = permissionLists;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }

    public LocalDateTime getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(LocalDateTime last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    // equals & hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
