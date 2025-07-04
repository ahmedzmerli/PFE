package com.example.GestionUser.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdv_history_dimii")
public class PdvHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pdv_master")
    private Long pdvMasterId;

    private String username;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "date_action")
    private LocalDateTime dateAction;

    public PdvHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPdvMasterId() {
        return pdvMasterId;
    }

    public void setPdvMasterId(Long pdvMasterId) {
        this.pdvMasterId = pdvMasterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }
}
