package com.example.GestionUser.dto;

import com.example.GestionUser.entities.BlHistory;

import java.time.LocalDateTime;

public class BlHistoryDTO {

    private String msisdn;
    private String username;
    private String motif;
    private String statut;
    private String offre;
    private String segment;
    private String typeClient;
    private LocalDateTime dateAction;
    private LocalDateTime startDate;   // pour DATE_DEBUT
    private LocalDateTime endDate;     // pour DATE_FIN
    private String typeBlack;




    public static BlHistoryDTO fromEntity(BlHistory b) {
        if (b == null) return null;
        BlHistoryDTO dto = new BlHistoryDTO();
        dto.setMsisdn(b.getMsisdn());
        dto.setUsername(b.getUsername());
        dto.setMotif(b.getMotif());
        dto.setStatut(b.getStatutBl());
        dto.setOffre(b.getOffre());
        dto.setSegment(b.getSegment());
        dto.setTypeClient(b.getTypeClient());
        dto.setDateAction(b.getDateAction());
        dto.setStartDate(b.getDateDebut());
        dto.setEndDate(b.getDateFin());
        dto.setTypeBlack(b.getTypeBlack());
        // + autres champs si besoin
        return dto;
    }




    //public static BlHistoryDTO fromEntity(BlHistory b) {
    //    BlHistoryDTO dto = new BlHistoryDTO();
    //    dto.setMsisdn(b.getMsisdn());
    //    dto.setUsername(b.getUsername());
    //    dto.setMotif(b.getMotif());
    //    dto.setStatut(b.getStatutBl());
    //    dto.setOffre(b.getOffre());
    //    dto.setSegment(b.getSegment());
    //    dto.setTypeClient(b.getTypeClient());
    //    dto.setDateAction(b.getDateAction());
    //    dto.setStartDate(b.getDateDebut());
    //    dto.setEndDate(b.getDateFin());
    //    dto.setTypeBlack(b.getTypeBlack());
    //    return dto;
    //}

    public BlHistoryDTO() {
    }

    public BlHistoryDTO(String msisdn, String username, String motif, String statut,
                        String offre, String segment, String typeClient,
                        LocalDateTime dateAction, LocalDateTime startDate,
                        LocalDateTime endDate, String typeBlack) {
        this.msisdn = msisdn;
        this.username = username;
        this.motif = motif;
        this.statut = statut;
        this.offre = offre;
        this.segment = segment;
        this.typeClient = typeClient;
        this.dateAction = dateAction;
        this.startDate = startDate;
        this.endDate = endDate;
        this.typeBlack = typeBlack;
    }

    // Getters et Setters

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getOffre() {
        return offre;
    }

    public void setOffre(String offre) {
        this.offre = offre;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getTypeBlack() {
        return typeBlack;
    }

    public void setTypeBlack(String typeBlack) {
        this.typeBlack = typeBlack;
    }
}
