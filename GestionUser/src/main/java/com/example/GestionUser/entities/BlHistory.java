package com.example.GestionUser.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BL_HISTORY") // On ajoute schema = "CCADMIN"
public class BlHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MSISDN")
    private String msisdn;

    @Column(name = "USERNAME")
    private String username;



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MOTIF_ID")
//    private Motif motif;
    @Column(name = "MOTIF")
    private String motif;

    @Column(name = "STATUBL")
    private String statutBl;

    @Column(name = "OFFRE")
    private String offre;

    @Column(name = "SEGMENT")
    private String segment;

    @Column(name = "TYPECLIENT")
    private String typeClient;

    @Column(name = "DATE_ACTION")
    private LocalDateTime dateAction;

    @Column(name = "TYPEBLACK")
    private String typeBlack;

    @Column(name = "DATE_DEBUT")
    private LocalDateTime dateDebut;

    @Column(name = "DATE_FIN")
    private LocalDateTime dateFin;

    @Column(name = "TMCODE")
    private String tmcode;

    @Column(name = "HOURBL")
    private String hourBl;

    @Column(name = "DATE_LAST_BLACKLIST")
    private LocalDateTime dateLastBlacklist;

    // Constructeurs

    public BlHistory() {
    }

    public BlHistory(Long id, String msisdn, String username, String motif, String statutBl, String offre,
                     String segment, String typeClient, LocalDateTime dateAction, String typeBlack,
                     LocalDateTime dateDebut, LocalDateTime dateFin, String tmcode, String hourBl,
                     LocalDateTime dateLastBlacklist) {
        this.id = id;
        this.msisdn = msisdn;
        this.username = username;
        this.motif = motif;
        this.statutBl = statutBl;
        this.offre = offre;
        this.segment = segment;
        this.typeClient = typeClient;
        this.dateAction = dateAction;
        this.typeBlack = typeBlack;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tmcode = tmcode;
        this.hourBl = hourBl;
        this.dateLastBlacklist = dateLastBlacklist;
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

    public String getStatutBl() {
        return statutBl;
    }

    public void setStatutBl(String statutBl) {
        this.statutBl = statutBl;
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

    public String getTypeBlack() {
        return typeBlack;
    }

    public void setTypeBlack(String typeBlack) {
        this.typeBlack = typeBlack;
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

    public String getTmcode() {
        return tmcode;
    }

    public void setTmcode(String tmcode) {
        this.tmcode = tmcode;
    }

    public String getHourBl() {
        return hourBl;
    }

    public void setHourBl(String hourBl) {
        this.hourBl = hourBl;
    }

    public LocalDateTime getDateLastBlacklist() {
        return dateLastBlacklist;
    }

    public void setDateLastBlacklist(LocalDateTime dateLastBlacklist) {
        this.dateLastBlacklist = dateLastBlacklist;
    }

    /* ---------- Audit technique ---------- */
//    @CreatedDate
//    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
//    private LocalDateTime createdDate;          // date d’insertion
//
//    @LastModifiedDate
//    @Column(name = "LAST_MODIFIED_DATE")
//    private LocalDateTime lastModifiedDate;     // date de mise à jour
//
//    @CreatedBy
//    @Column(name = "CREATED_BY", updatable = false, length = 100)
//    private String createdBy;                   // email créateur
//
//    @LastModifiedBy
//    @Column(name = "MODIFIED_BY", length = 100)
//    private String modifiedBy;                  // email modificateur
}
