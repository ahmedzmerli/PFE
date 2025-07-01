package com.example.GestionUser.dto;

import java.time.LocalDateTime;

public class BlacklistDTO {
    private Long id;
    private String msisdn;
    private String segment;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String motif;
    private String offre;
    private String statut;
    private String username;
    private String typeClient;
    private Long dureeBlacklist;
    private LocalDateTime dateAction;




    public BlacklistDTO() {
    }

    public BlacklistDTO(Long id, String msisdn, String segment, LocalDateTime dateDebut,
                        LocalDateTime dateFin, String motif, String offre, String statut,
                        String username, String typeClient, Long dureeBlacklist, LocalDateTime dateAction) {
        this.id = id;
        this.msisdn = msisdn;
        this.segment = segment;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.offre = offre;
        this.statut = statut;
        this.username = username;
        this.typeClient = typeClient;
        this.dureeBlacklist = dureeBlacklist;
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

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getOffre() {
        return offre;
    }

    public void setOffre(String offre) {
        this.offre = offre;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }

    public Long getDureeBlacklist() {
        return dureeBlacklist;
    }

    public void setDureeBlacklist(Long dureeBlacklist) {
        this.dureeBlacklist = dureeBlacklist;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }
}
