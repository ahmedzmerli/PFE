package com.example.GestionUser.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "permission")
//@Table(name = "permission_dimii", schema = "ccadmin")
public class Permission {

    @Id
    @GeneratedValue
    //@Id
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perm_seq")
    //    @SequenceGenerator(name = "perm_seq", sequenceName = "ccadmin.seq_permission_dimii", allocationSize = 1)
    private Integer id;

    @Column(name = "feature", nullable = false)
    private String feature; // ex: "blacklist"
    @Column(name = "action", nullable = false)
    private String action;  // ex: "read"



    public Permission() {
    }

    public Permission(Integer id, String feature, String action) {
        this.id = id;
        this.feature = feature;
        this.action = action;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // toString
    @Override
    public String toString() {
        return feature + "." + action;
    }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(feature, that.feature) && Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feature, action);
    }
}
