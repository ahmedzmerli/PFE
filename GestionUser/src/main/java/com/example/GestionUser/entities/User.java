package com.example.GestionUser.entities;

import javax.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "account_locked", nullable = false)
    private boolean account_locked;

    private boolean enabled;

    @ManyToMany(fetch = EAGER)
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Token> tokens;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_date;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime last_modified_date;

    public User() {
    }

    public User(Integer id, String firstname, String lastname, String email, String password,
                boolean account_locked, boolean enabled, Set<Role> roles, List<Token> tokens,
                LocalDateTime created_date, LocalDateTime last_modified_date) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.account_locked = account_locked;
        this.enabled = enabled;
        this.roles = roles;
        this.tokens = tokens;
        this.created_date = created_date;
        this.last_modified_date = last_modified_date;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccount_locked() {
        return account_locked;
    }

    public void setAccount_locked(boolean account_locked) {
        this.account_locked = account_locked;
    }

    public boolean isEnabledFlag() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
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

    // UserDetails & Principal implementations

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return Collections.emptySet();

        return roles.stream()
                .filter(role -> role.getPermissionLists() != null)
                .flatMap(role -> role.getPermissionLists().stream())
                .filter(pl -> pl.getPermissions() != null)
                .flatMap(pl -> pl.getPermissions().stream())
                .map(Permission::toString)
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account_locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public String fullName() {
        return getFirstname() + " " + getLastname();
    }
}
