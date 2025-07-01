package com.example.GestionUser.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdv_history")
public class PdvHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msisdn;
    private String nomPdv;
    private String adresse;
    private String codePdv;

    private String username;      // qui a fait l'action
    private String actionType;    // CREATE ou DELETE
    private LocalDateTime dateAction;

    public PdvHistory() {
    }

    public PdvHistory(Long id, String msisdn, String nomPdv, String adresse, String codePdv,
                      String username, String actionType, LocalDateTime dateAction) {
        this.id = id;
        this.msisdn = msisdn;
        this.nomPdv = nomPdv;
        this.adresse = adresse;
        this.codePdv = codePdv;
        this.username = username;
        this.actionType = actionType;
        this.dateAction = dateAction;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNomPdv() {
        return nomPdv;
    }

    public void setNomPdv(String nomPdv) {
        this.nomPdv = nomPdv;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePdv() {
        return codePdv;
    }

    public void setCodePdv(String codePdv) {
        this.codePdv = codePdv;
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
