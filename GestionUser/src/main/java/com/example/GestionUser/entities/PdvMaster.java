package com.example.GestionUser.entities;

import javax.persistence.*;

@Entity
@Table(name = "pdv_master")
public class PdvMaster {

    @Id
    private String msisdn;

    @Column(name = "nom_pdv")
    private String nomPdv;

    private String adresse;
    private String codePdv;

    public PdvMaster() {
    }

    public PdvMaster(String msisdn, String nomPdv, String adresse, String codePdv) {
        this.msisdn = msisdn;
        this.nomPdv = nomPdv;
        this.adresse = adresse;
        this.codePdv = codePdv;
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
}
